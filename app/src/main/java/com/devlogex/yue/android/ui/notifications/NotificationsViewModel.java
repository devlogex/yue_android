package com.devlogex.yue.android.ui.notifications;

import static kotlinx.coroutines.CoroutineScopeKt.CoroutineScope;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Timer;
import java.util.TimerTask;

import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.Job;

public class NotificationsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private Timer timer;
    private final MutableLiveData<Integer> count = new MutableLiveData<>();

    public NotificationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
        startCounting();
    }

    private void startCounting() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            private int counter = 0;

            @Override
            public void run() {
                count.postValue(counter++);
            }
        }, 0, 1000); // delay 0 ms, repeat every 1000 ms (1 second)
    }


    public LiveData<Integer> getText() {
        return count;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (timer != null) {
            timer.cancel();
        }
    }

}