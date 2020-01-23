package ng.com.babbangona.kymapp;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;

//import androidx.appcompat.app.AppCompatActivity;

public class Main2Activity extends AppCompatActivity {
    private JobScheduler jobScheduler;

    private JobInfo jobInfo;

    ComponentName componentName;

    private static final int JOB_ID =101;


    String staff_name,staff_id,staff_role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        executeService();


        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        staff_name = (String) b.get("staff_name");
        staff_role = (String) b.get("staff_role");
        staff_id = (String) b.get("staff_id");

        SharedPreferences session = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = session.edit();
        editor.putString("staff_name",staff_name);
        editor.putString("staff_id",staff_id);
        editor.putString("staff_role",staff_role);

        editor.commit();




        //save values to shared pref

       startActivity(new Intent (getApplicationContext(),Home.class));
    }
    public void onDestroy() {

        super.onDestroy();

        executeService();

    }



    public void executeService(){

        componentName = new ComponentName(this, BackgroundSync.class);

        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, componentName);

        builder.setPeriodic(1000);

        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);

        builder.setPersisted(true);

        jobInfo = builder.build();

        jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

        jobScheduler.schedule(jobInfo);





    }
}
