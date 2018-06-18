package com.example.maxim.databindingtraining.presentation.ui.screens.test;

import android.databinding.ObservableField;

public class TestItemModel {
    public final ObservableField<String> number = new ObservableField<>();

    TestItemModel(int value) {
        number.set(String.valueOf(value));
    }
}
