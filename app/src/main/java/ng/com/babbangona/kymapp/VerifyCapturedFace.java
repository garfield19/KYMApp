package ng.com.babbangona.kymapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.babbangona.bg_face.LuxandAuthActivity;
import com.babbangona.bg_face.LuxandInfo;

import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import ng.com.babbangona.kymapp.Database.DatabaseHelper;

public class VerifyCapturedFace extends AppCompatActivity {
    Button verify;
    TextView member_id,mmember_name;
    String chosen_member, member_name, capture_type;
    DatabaseHelper mDatabaseHelper2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_captured_face);

        member_id = findViewById(R.id.member_id);
        mmember_name = findViewById(R.id.member_name);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        chosen_member = preferences.getString("unique_member_id", "");
        member_name = preferences.getString("chosen_member_name", "");
        capture_type = preferences.getString("capture_type","");


        member_id.setText(chosen_member);
        mmember_name.setText(member_name);

        mDatabaseHelper2 = new DatabaseHelper(this);

        verify = findViewById(R.id.verify_captured_face);


        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                    LuxandInfo luxandInfo = new LuxandInfo(VerifyCapturedFace.this);
                    luxandInfo.putTemplate(databaseHelper.getCapturedTemplate(chosen_member));
                    Intent myIntent = new Intent(getApplicationContext(), LuxandAuthActivity.class);

                    startActivityForResult(myIntent, 419);

            }
        });



    }

    @Override
    public void onBackPressed() {

            super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 419) {
            if (data.getIntExtra("RESULT", 0) == 1) {

                Toasty.success(getApplicationContext(), "Verification Successful", Toast.LENGTH_LONG).show();


    mDatabaseHelper2.replaceSync(chosen_member);
    mDatabaseHelper2.replaceStatus(chosen_member);
    Intent back_intent = new Intent(this,KivaCapture.class);
    startActivity(back_intent);
    finish();

            } else {
                Toasty.error(getApplicationContext(), "Verification Failed", Toast.LENGTH_LONG).show();


            }
        }else if (requestCode == 519){
            if(data != null && data.getIntExtra("RESULT", 0) == 1) {

                String Template = new LuxandInfo(getApplicationContext()).getTemplate();
                String Image = new LuxandInfo(getApplicationContext()).getImage();


                SharedPreferences cit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


                Date currentTime = Calendar.getInstance().getTime();

                //add(chosen_member, currentTime.toString(), Template);
                Toasty.success(getApplicationContext(), "Capture Successful", Toast.LENGTH_LONG).show();
                Intent back_intent = new Intent(this,VerifyCapturedFace.class);
                startActivity(back_intent);
                Log.i("DANNY", "onActivityResult: " + Template);

            } else
                Toasty.error(getApplicationContext(), "Capture Failed", Toast.LENGTH_LONG).show();


        }
        else if(requestCode == 619) {
            if (data.getIntExtra("RESULT", 0) == 1) {

                //my code
                SharedPreferences cit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                int prefC = cit.getInt("face_check_in_count",0);

                if (prefC == 0){
                    cit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor c = cit.edit();
                    c.putInt("face_check_in_count",1);
                    c.putString("my_flag", "1");
                    c.commit();

                }else{
                    int nprefC = prefC + 1;

                    cit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor c = cit.edit();
                    c.putInt("face_check_in_count",nprefC);
                    c.putString("my_flag", "1");

                    c.commit();

                }

                //   addLOG(id,"1",s_id,"0",name);

            } else {
                //  addLOG(id,"0",s_id,"0",name);

            }



        }
    }
    public void replace(String member_id){
        boolean insertData = mDatabaseHelper2.replaceStatus(member_id);
        if(insertData){
            // new callNextActivity(getApplicationContext()).run();

            // Toasty.info(getApplicationContext(), "Replaced", Toast.LENGTH_LONG).show();

        }else{


        }
    }
}
