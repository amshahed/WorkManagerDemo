package com.example.workmanagerdemo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

import workers.OneTimeWork;
import workers.PeriodicWork;

public class MainActivity extends AppCompatActivity {
    public static final String TITLE_KEY = "title";
    public static final String TEXT_KEY = "text";
    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        info = findViewById(R.id.info);

        Data data = new Data.Builder()
                .putString(TITLE_KEY, "One Time Work")
                .putString(TEXT_KEY, "Connected to internet!")
                .build();

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        final String UNIQUE_WORK_NAME = "one-time-work";
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest
                .Builder(OneTimeWork.class)
                .setInputData(data)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(this).enqueueUniqueWork(
                UNIQUE_WORK_NAME,
                ExistingWorkPolicy.KEEP,
                oneTimeWorkRequest
        );

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(oneTimeWorkRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null) {
                            info.append(workInfo.getState().name().concat("\n"));
                        }
                    }
                });

        final String PERIODIC_WORK_NAME = "periodic-work";
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest
                .Builder(PeriodicWork.class, 15, TimeUnit.MINUTES)
                .build();
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                PERIODIC_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicWorkRequest
        );
    }
}
