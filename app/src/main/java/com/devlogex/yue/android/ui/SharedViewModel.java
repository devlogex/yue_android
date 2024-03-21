package com.devlogex.yue.android.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    private MutableLiveData<Boolean> isCalling;
    private MutableLiveData<Boolean> isLogin;

    public SharedViewModel() {
        isCalling = new MutableLiveData<>();
        isCalling.setValue(false);
        isLogin = new MutableLiveData<>();
        isLogin.setValue(false);
    }

    public LiveData<Boolean> getIsCalling() {
        return isCalling;
    }

    public void setIsCalling(boolean isCalling) {
        this.isCalling.setValue(isCalling);
    }

    public MutableLiveData<Boolean> getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin.setValue(isLogin);
    }

    public void setIsLoginInBackGround(boolean isLogin) {
        this.isLogin.postValue(isLogin);
    }
}