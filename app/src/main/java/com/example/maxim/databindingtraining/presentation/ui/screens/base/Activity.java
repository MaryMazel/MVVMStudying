package com.example.maxim.databindingtraining.presentation.ui.screens.base;

import android.databinding.ViewDataBinding;

import com.example.maxim.databindingtraining.BR;
import com.spryrocks.android.modules.ui.mvvm.ViewModel;

public abstract class Activity<TBinding extends ViewDataBinding, TViewModel extends ViewModel>
        extends com.spryrocks.android.modules.ui.mvvm.Activity<TBinding, TViewModel> {
    protected Activity(int layoutId, Class<TViewModel> tViewModelClass) {
        super(layoutId, tViewModelClass, BR.model);
    }
}
