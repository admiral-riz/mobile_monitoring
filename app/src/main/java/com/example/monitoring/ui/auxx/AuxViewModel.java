package com.example.monitoring.ui.auxx;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AuxViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AuxViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is aux fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}