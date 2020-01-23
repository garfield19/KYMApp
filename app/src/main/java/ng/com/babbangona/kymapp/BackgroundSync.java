package ng.com.babbangona.kymapp;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

public class BackgroundSync extends JobService {


    SyncData.SendAppVersion sendAppVersion;
    @SuppressLint("StaticFieldLeak")

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {

        SharedPreferences mySPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        //SharedPreferences.Editor editor = mySPrefs.edit();
        String staff_id = mySPrefs.getString("staff_id", "");

        String version = "";
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        sendAppVersion = new SyncData.SendAppVersion(getApplicationContext()) {
        };
        sendAppVersion.execute(BuildConfig.APPLICATION_ID, BuildConfig.VERSION_NAME, staff_id);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {

        //downloadApplication.cancel(true);
        return false;
    }
}