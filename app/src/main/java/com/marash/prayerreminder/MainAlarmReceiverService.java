package com.marash.prayerreminder;

import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * Similar to {@link MainAlarmReceiver} but it get fired from {@link JobScheduler} in android 6+ (sdk 23+)
 *
 * author: Arash Khosravi
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainAlarmReceiverService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        AlarmSetter.createOrUpdateAllAlarms(getApplicationContext());
        // always return false because we have no synchronous action.
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
