package com.petrovic.m.dimitrije.activitytracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.petrovic.m.dimitrije.activitytracker.fit.StepCounterService;
import com.petrovic.m.dimitrije.activitytracker.utils.Utils;

public class BootUpReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = Utils.getLogTag(BootUpReceiver.class);

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "Intent received: " + intent.getAction());

        final String action = intent.getAction();

        switch(action) {
            case Intent.ACTION_BOOT_COMPLETED:
                Log.d(LOG_TAG, "Starting services ...");
                Intent startServiceIntent = new Intent(context, StepCounterService.class);
                ContextCompat.startForegroundService(context, startServiceIntent);
        }
    }
}
