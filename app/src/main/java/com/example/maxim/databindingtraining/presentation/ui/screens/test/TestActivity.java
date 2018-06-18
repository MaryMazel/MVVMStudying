package com.example.maxim.databindingtraining.presentation.ui.screens.test;

import com.example.maxim.databindingtraining.presentation.ui.screens.base.Activity;
import com.example.maxim.databindingtraining.R;
import com.example.maxim.databindingtraining.databinding.TestActivityBinding;

public class TestActivity extends Activity<TestActivityBinding, TestViewModel> {
    protected TestActivity() {
        super(R.layout.test_activity, TestViewModel.class);
    }
}
