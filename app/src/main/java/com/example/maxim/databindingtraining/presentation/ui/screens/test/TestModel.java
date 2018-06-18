package com.example.maxim.databindingtraining.presentation.ui.screens.test;

import android.databinding.ObservableField;

import com.spryrocks.android.modules.ui.mvvm.model.OnClickCommand;

import java.util.List;

public class TestModel {
    public final ObservableField<String> number = new ObservableField<>();
    public final OnClickCommand buttonClick = new OnClickCommand();
    public final ObservableField<List<TestItemModel>> dividers = new ObservableField<>();

    TestModel(String number) {
        this.number.set(number);
    }
}
