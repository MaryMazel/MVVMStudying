package com.example.maxim.databindingtraining.presentation.ui.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.InverseBindingListener;
import android.databinding.ViewDataBinding;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.maxim.databindingtraining.BR;
import com.example.maxim.databindingtraining.presentation.ui.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewListBindingAdapter<TModel>
        extends RecyclerView.Adapter<RecyclerViewListBindingAdapter.ViewHolder<TModel>> {
    private final LayoutInflater layoutInflater;
    private final ItemLayoutSelector<TModel> itemLayoutSelector;

    @Nullable
    private List<TModel> items;

    private int selectedPosition = -1;

    public InverseBindingListener selectedItemAttrChanged;
    public RecyclerItemClickListener recyclerItemClickListener;
    public View.OnClickListener itemActivatedListener;

    @SuppressWarnings("WeakerAccess")
    public RecyclerViewListBindingAdapter(Context context, ItemLayoutSelector<TModel> itemLayoutSelector) {
        this.layoutInflater = LayoutInflater.from(context);
        this.itemLayoutSelector = itemLayoutSelector;
        this.items = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        return itemLayoutSelector.layout(getItem(position));
    }

    @Override
    public ViewHolder<TModel> onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding itemBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false);
        return new ViewHolder<>(itemBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder<TModel> holder, int position) {
        //noinspection ConstantConditions
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        if (items == null)
            return 0;

        return items.size();
    }

    @SuppressWarnings("unused")
    public TModel getItem(int position) {
        //noinspection ConstantConditions
        return items.get(position);
    }

    public void setItems(@Nullable List<TModel> items) {
        this.items = items;
        this.notifyDataSetChanged();
    }

    @Nullable
    public List<TModel> getItems() {
        return items;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    static class ViewHolder<TModel> extends RecyclerView.ViewHolder {
        private final ViewDataBinding binding;

        ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(TModel menuItem) {
            binding.setVariable(BR.model, menuItem);
            binding.executePendingBindings();
        }
    }

    public interface ItemLayoutSelector<T> {
        int layout(T item);
    }
}