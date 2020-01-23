package ng.com.babbangona.kymapp;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

//import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private JobScheduler jobScheduler;

    private JobInfo jobInfo;

    ComponentName componentName;

    private static final int JOB_ID =101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityy_main);
        executeService();
    }

    public void Launch(View view) {


        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setComponent(new ComponentName("com.babbangona.accesscontrol", "com.babbangona.accesscontrol.MainActivity"));
            startActivity(intent);
        }catch (Exception e){
            Toast.makeText(this, "You have not installed bg store", Toast.LENGTH_SHORT).show();
        }

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
