package com.example.monitoring.ui.kip;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class KipViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public KipViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is kip fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}