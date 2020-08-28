package com.petrovic.m.dimitrije.activitytracker.fit;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.petrovic.m.dimitrije.activitytracker.R;
import com.petrovic.m.dimitrije.activitytracker.utils.Utils;

public class StepCounterService extends Service {

    private static final String LOG_TAG = Utils.getLogTag(StepCounterService.class);

    private static final String CHANNEL_ID = StepCounterService.class.getCanonicalName();
    private static final int NOTIFICATION_ID = 1994;

    public StepCounterService() {
        Log.d(LOG_TAG, "constructor");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(LOG_TAG, "onCreate");

        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Log.d(LOG_TAG, "onStartCommand");

        /*Intent notificationIntent = new Intent(this, ExampleActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);*/

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                                        .setContentText("No content for now")
                                        .setSmallIcon(R.drawable.baseline_directions_run_24)
                                        .setShowWhen(false)
                                        .build();

        startForeground(NOTIFICATION_ID, notification);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void createNotificationChannel() {
        Log.d(LOG_TAG, "createNotificationChannel");

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notification_channel_name);
            String description = getString(R.string.notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
