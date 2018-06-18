package com.example.maxim.databindingtraining.presentation.ui.screens.test;

import android.app.Application;

import com.spryrocks.android.modules.ui.mvvm.ViewModel;

import java.util.ArrayList;

public class TestViewModel extends ViewModel<TestModel> {
    public TestViewModel(Application application) {
        super(application, new TestModel("2"));
        model.buttonClick.addCallback(this::buttonClick);
    }


    private void buttonClick(){
        // создать ArrayList<Integer> и записать в него 10 чисел кот делятся без остатка
        Integer mynumber = Integer.parseInt(model.number.get());

        ArrayList<TestItemModel> dividers = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            dividers.add(new TestItemModel(i * mynumber));
        }
        model.dividers.set(dividers);
    }
}
