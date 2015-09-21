package com.example.den.converterlab.services;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.content.Intent;
import android.os.Build;

import com.example.den.converterlab.utils.CheckAllForStart;

/**
 * Created by Den on 17.09.15.
 */
public class LoadJsonService extends IntentService {

    public LoadJsonService() {
        super("Loader");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onHandleIntent(Intent intent) {
        CheckAllForStart checkAllForStart = new CheckAllForStart(getApplicationContext());
        checkAllForStart.checkAll();
    }
}
