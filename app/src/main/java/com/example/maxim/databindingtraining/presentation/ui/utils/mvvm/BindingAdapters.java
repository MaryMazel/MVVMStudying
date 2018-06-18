package com.example.maxim.databindingtraining.presentation.ui.utils.mvvm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.graphics.Typeface;
import android.support.annotation.ColorRes;
import android.support.annotation.FontRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.maxim.databindingtraining.presentation.ui.adapters.RecyclerViewListBindingAdapter;
import com.example.maxim.databindingtraining.presentation.ui.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BindingAdapters {
    //region Views
    @BindingAdapter("isSelected")
    public static void setSelected(View view, boolean isSelected) {
        view.setSelected(isSelected);
    }

    @BindingAdapter("isEnabled")
    public static void setEnabled(View view, boolean isEnabled) {
        view.setEnabled(isEnabled);
    }

    @BindingAdapter("backgroundTint")
    public static void setBackgroundTint(View view, @ColorRes int colorResId) {
        int color = ContextCompat.getColor(view.getContext(), colorResId);
        ViewCompat.setBackgroundTintList(view, ColorStateList.valueOf(color));
    }
    //endregion

    //region Text
    @BindingAdapter("errorText")
    public static void setErrorMessage(@NonNull TextView view, @Nullable String errorMessage) {
        view.setError(errorMessage);
    }

    @BindingAdapter("fontFamily")
    public static void setFontFamily(TextView textView, @FontRes int fontResId) {
        textView.setTypeface(ResourcesCompat.getFont(textView.getContext(), fontResId));
    }

    @BindingAdapter("hint")
    public static void setHint(EditText editText, @StringRes int hintResId) {
        if (hintResId != 0) {
            editText.setHint(hintResId);
        } else {
            editText.setHint(null);
        }
    }

    @BindingAdapter(value = {"suggestions", "itemLayoutId"}, requireAll = false)
    public static void setSuggestions(AutoCompleteTextView autoCompleteTextView, List<String> suggestions, @LayoutRes int itemLayout) {
        autoCompleteTextView.setThreshold(1);

        if (itemLayout == 0) {
            itemLayout = android.R.layout.simple_dropdown_item_1line;
        }

        if (suggestions == null) {
            suggestions = Collections.emptyList();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(autoCompleteTextView.getContext(), itemLayout, suggestions.toArray(new String[0]));
        autoCompleteTextView.setAdapter(adapter);
    }

    @SuppressWarnings("unused")
    @BindingAdapter("typeface")
    public static void setTypeface(TextView v, String style) {
        switch (style) {
            case "bold":
                v.setTypeface(null, Typeface.BOLD);
                break;
            default:
                v.setTypeface(null, Typeface.NORMAL);
                break;
        }
    }
    //endregion

    //region RecyclerView
    @BindingAdapter(value = {"items", "itemLayoutId", "selectedItem", "selectedItemAttrChanged", "onItemActivated"}, requireAll = false)
    public static <T> void setItems(
            @NonNull RecyclerView recyclerView, @Nullable List<T> items, @LayoutRes int itemLayoutId,
            Object selectedItem, final InverseBindingListener selectedItemAttrChanged,
            View.OnClickListener itemActivatedListener) {
        setItems(recyclerView, items, item -> itemLayoutId, selectedItem, selectedItemAttrChanged, itemActivatedListener);
    }

    @BindingAdapter(value = {"items", "itemLayoutSelector", "selectedItem", "selectedItemAttrChanged", "onItemActivated"}, requireAll = false)
    public static <T> void setItems(
            @NonNull RecyclerView recyclerView,
            @Nullable List<T> items,
            final RecyclerViewListBindingAdapter.ItemLayoutSelector<T> itemLayoutSelector,
            Object selectedItem,
            final InverseBindingListener selectedItemAttrChanged,
            View.OnClickListener itemActivatedListener) {
        //noinspection unchecked
        RecyclerViewListBindingAdapter<T> adapter = (RecyclerViewListBindingAdapter<T>) recyclerView.getAdapter();

        if (adapter == null) {
            Context context = recyclerView.getContext();

            adapter = new RecyclerViewListBindingAdapter<>(context, itemLayoutSelector);
            recyclerView.setAdapter(adapter);
        }

        if (adapter.getItems() != items) {
            adapter.setItems(items);
        }

        if (adapter.selectedItemAttrChanged != selectedItemAttrChanged) {
            adapter.selectedItemAttrChanged = selectedItemAttrChanged;

            recyclerView.removeOnItemTouchListener(adapter.recyclerItemClickListener);

            RecyclerItemClickListener recyclerItemClickListener = null;
            if (selectedItemAttrChanged != null) {
                recyclerItemClickListener = new RecyclerItemClickListener(recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //noinspection unchecked
                        RecyclerViewListBindingAdapter<T> adapter = (RecyclerViewListBindingAdapter<T>) recyclerView.getAdapter();
                        adapter.setSelectedPosition(position);
                        selectedItemAttrChanged.onChange();

                        if (adapter.itemActivatedListener != null) {
                            adapter.itemActivatedListener.onClick(null);
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                });

                recyclerView.addOnItemTouchListener(recyclerItemClickListener);
            }

            adapter.recyclerItemClickListener = recyclerItemClickListener;
        }

        //noinspection SuspiciousMethodCalls
        int selectedPosition = items != null ? items.indexOf(selectedItem) : -1;
        if (selectedPosition != adapter.getSelectedPosition()) {
            adapter.setSelectedPosition(selectedPosition);
        }

        if (adapter.itemActivatedListener != itemActivatedListener) {
            adapter.itemActivatedListener = itemActivatedListener;
        }
    }

    @InverseBindingAdapter(attribute = "selectedItem")
    public static <T> T getSelectedItem(RecyclerView recyclerView) {
        //noinspection unchecked
        RecyclerViewListBindingAdapter<T> adapter = (RecyclerViewListBindingAdapter<T>) recyclerView.getAdapter();
        return adapter.getItem(adapter.getSelectedPosition());
    }
    //endregion

    //region SwipeRefreshLayout
    @BindingAdapter("isRefreshing")
    public static void setRefreshing(SwipeRefreshLayout layout , boolean showRefreshCircle) {
        layout.setRefreshing(showRefreshCircle);
    }
    //endregion

    //region Gift Card
    @BindingAdapter("loadUrl")
    public static void loadUrl(WebView view, String url) {
        view.loadUrl(url);
    }
    //endregion
}
