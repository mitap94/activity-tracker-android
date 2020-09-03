package com.petrovic.m.dimitrije.activitytracker.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Subscription;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DailyTotalResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.petrovic.m.dimitrije.activitytracker.R;
import com.petrovic.m.dimitrije.activitytracker.databinding.FragmentActivitiesBinding;
import com.petrovic.m.dimitrije.activitytracker.databinding.FragmentMeBinding;
import com.petrovic.m.dimitrije.activitytracker.utils.Utils;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.text.DateFormat.getDateTimeInstance;
import static java.text.DateFormat.getTimeInstance;

public class ActivitiesFragment extends Fragment {

    public static final String LOG_TAG = Utils.getLogTag(ActivitiesFragment.class);

    private static final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1992;


    private FitnessOptions fitnessOptions;
    private FragmentActivitiesBinding binding;
    private ActivitiesViewModel activitiesViewModel;
    private GoogleSignInAccount account;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentActivitiesBinding.inflate(inflater, container, false);
        View root =  binding.getRoot();

        activitiesViewModel = new
                ViewModelProvider(this).get(ActivitiesViewModel.class);

        activitiesViewModel.getText().observe(getViewLifecycleOwner(), s -> binding.textActivities.setText(s));
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(LOG_TAG, "onViewCreated");

        // Options to request access
        fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .build();

        // Get Google account
        account =
                GoogleSignIn.getAccountForExtension(this.getContext(), fitnessOptions);

        // If requested options not yet allowed: request them
        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this, // your activity
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE, // e.g. 1
                    account,
                    fitnessOptions);
        } else {
            accessGoogleFit();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {
                accessGoogleFit();
            }
        }
    }

    private void accessGoogleFit() {
        Log.d(LOG_TAG, "accessGoogleFit");

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_WEEK, -1);
        long startTime = cal.getTimeInMillis();

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .bucketByTime(1, TimeUnit.HOURS)
                .build();

        account = GoogleSignIn
                .getAccountForExtension(this.getContext(), fitnessOptions);

        // Samples the user's activity once per minute.
        Fitness.getRecordingClient(this.getContext(), account)
                .subscribe(DataType.TYPE_STEP_COUNT_DELTA)
                .addOnSuccessListener(aVoid -> Log.i(LOG_TAG, "Successfully subscribed TYPE_STEP_COUNT_DELTA!"))
                .addOnFailureListener(e -> Log.i(LOG_TAG, "There was a problem subscribing TYPE_STEP_COUNT_DELTA."));

        // List active subscriptions
        Fitness.getRecordingClient(this.getContext(), account)
                .listSubscriptions()
                .addOnSuccessListener((OnSuccessListener<List<Subscription>>) subscriptions -> {
                    for (Subscription sc : subscriptions) {
                        DataType dt = sc.getDataType();
                        Log.i(LOG_TAG, "Active subscription for data type: " + dt.getName());
                    }
                });

        DateFormat dateFormat = getDateTimeInstance();

        Fitness.getHistoryClient(this.getContext(), account)
                .readData(readRequest)
                .addOnSuccessListener(response -> {
                    Log.d(LOG_TAG, "OnSuccess() start");
                    // Use response data here
                    for(Bucket b: response.getBuckets()) {
                        Log.d(LOG_TAG, "Bucket " + b.toString() + ", start time: " + dateFormat.format(b.getStartTime(TimeUnit.MILLISECONDS)) + ", end time: " + dateFormat.format(b.getEndTime(TimeUnit.MILLISECONDS)) + ", activity: " + b.getActivity());

                        List<DataSet> dataSets = b.getDataSets();
                        Log.d(LOG_TAG, "dataSet = " + dataSets + ", size = " + dataSets.size());

                        for(DataSet set: dataSets) {
                            dumpDataSet(set);
                        }
                    }

                    Log.d(LOG_TAG, "OnSuccess() end");
                })
                .addOnFailureListener(e -> {
                    Log.e(LOG_TAG, "OnFailure()", e);
                });


        new VerifyDataTask().execute();
    }

    private class VerifyDataTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {

            Log.d(LOG_TAG, "start doInBackground");

            long total = 0;

            Task<DataSet> result = Fitness.getHistoryClient(ActivitiesFragment.this.getContext(), account).readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA);
            DataSet totalResult = null;
            try {
                totalResult = Tasks.await(result, 30, TimeUnit.SECONDS);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }

            dumpDataSet(totalResult);

            return null;
        }
    }

    private static void dumpDataSet(DataSet dataSet) {
        Log.i(LOG_TAG, "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = getDateTimeInstance();

        for (DataPoint dp : dataSet.getDataPoints()) {
            Log.i(LOG_TAG, "Data point:");
            Log.i(LOG_TAG, "\tType: " + dp.getDataType().getName());
            Log.i(LOG_TAG, "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.i(LOG_TAG, "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for (Field field : dp.getDataType().getFields()) {
                Log.i(LOG_TAG, "\tField: " + field.getName() + " Value: " + dp.getValue(field));
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}