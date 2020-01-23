package ng.com.babbangona.kymapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.babbangona.bg_face.LuxandActivity;
import com.babbangona.bg_face.LuxandAuthActivity;
import com.babbangona.bg_face.LuxandInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import ng.com.babbangona.kymapp.Database.DBHelper;
import ng.com.babbangona.kymapp.Database.DatabaseHelper;

public class OldOrNew extends AppCompatActivity {
Button btn_old;
TextView member_id, mmember_name;
String chosen_member, member_name, capture_type, picker;
    DatabaseHelper mDatabaseHelper2;
    ProgressDialog pd ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_or_new);



        member_id = findViewById(R.id.member_id);
        mmember_name = findViewById(R.id.member_name);

        Intent intent = getIntent();
        chosen_member = intent.getStringExtra("member_id");
        member_name = intent.getStringExtra("member_name");


        picker = intent.getStringExtra("picker");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        chosen_member = preferences.getString("unique_member_id", "");
        member_name = preferences.getString("chosen_member_name", "");
        picker = preferences.getString("picker", "");
        capture_type = preferences.getString("type","");
        editor.putString("capture_type",capture_type);
        editor.apply();


        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        member_id.setText(chosen_member);
        mmember_name.setText(member_name);

        mDatabaseHelper2 = new DatabaseHelper(this);

        btn_old = findViewById(R.id.old_tg);

        if(picker.equals("yes")){
            btn_old.setText("Verify");
        }else{
            btn_old.setText("Capture");
        }

        btn_old.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(picker.equals("yes")){
                    DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
                    LuxandInfo luxandInfo = new LuxandInfo(OldOrNew.this);
                    String template = databaseHelper.getTemplate(chosen_member);
                    luxandInfo.putTemplate(template);

                    Intent myIntent = new Intent(getApplicationContext(), LuxandAuthActivity.class);

                    if(template.equals("") || template.equals(" ") || template.length() < 800 || TextUtils.isEmpty(template) || template == null){
                        Toasty.error(getApplicationContext(), "No Template : Contact Product Support").show();
                    }else{
                        startActivityForResult(myIntent, 419);
                    }


                }else{
                    final Intent intent = new Intent(getApplicationContext(),LuxandActivity.class);
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                    String s_id = preferences.getString("unique_member_id", "");
                    final long time = System.currentTimeMillis();

                    String file = "capture"+s_id+"_"+time+".png";
                   // intent.putExtra("FILE_NAME",file);
                    //intent.putExtra("FOLDER_NAME","Pictures/Visitation Pictures");
                    startActivityForResult(intent, 519);
                    Log.d("Garfield", "Got HERE 0.0");

                }
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) {
            if (requestCode == 419) {
                if (data.getIntExtra("RESULT", 0) == 1) {

                    Toasty.success(getApplicationContext(), "Verification Successful", Toast.LENGTH_LONG).show();
                    Date currentTime = Calendar.getInstance().getTime();

                    String ik_number = mDatabaseHelper2.getIK(chosen_member);
                    String tries = mDatabaseHelper2.getTries(chosen_member);

                    if (TextUtils.isEmpty(tries)) {
                        addVerification(chosen_member, "1", "1", ik_number, "0", currentTime.toString());
                        mDatabaseHelper2.replaceSync(chosen_member);
                    } else {
                        int t = Integer.valueOf(tries) + 1;
                        String tt = String.valueOf(t);
                        mDatabaseHelper2.updateTries(chosen_member, tt);
                    }


                    Intent back_intent = new Intent(this, KivaCapture.class);
                    mDatabaseHelper2.replacePassStatus(chosen_member);
                    mDatabaseHelper2.replaceSync(chosen_member);
                    back_intent.putExtra("member_id", chosen_member);
                    startActivity(back_intent);


                } else {

                    //TODO change back to failed
                    String ik_number = mDatabaseHelper2.getIK(chosen_member);
                    DBHelper dbHelper = new DBHelper(this);
                    dbHelper.getReadableDatabase();
                    int count = dbHelper.getOldTGCount(ik_number);


                    Toasty.error(getApplicationContext(), "Verification Failed", Toast.LENGTH_LONG).show();
                    Date currentTime = Calendar.getInstance().getTime();

                    String tries = mDatabaseHelper2.getTries(chosen_member);

                    if (TextUtils.isEmpty(tries)) {
                        addVerification(chosen_member, "0", "1", ik_number, "0", currentTime.toString());
                        mDatabaseHelper2.replaceSync(chosen_member);

                    } else {
                        int t = Integer.valueOf(tries) + 1;
                        String tt = String.valueOf(t);
                        mDatabaseHelper2.updateTries(chosen_member, tt);
                        mDatabaseHelper2.replaceSync(chosen_member);

                    }

                    Intent back_intent = new Intent(this, IsPictureMember.class);
                    back_intent.putExtra("member_id", chosen_member);
                    back_intent.putExtra("status", 0);
                    startActivity(back_intent);


                }
            } else if (requestCode == 519) {
                Log.d("Garfield", "Got HERE 0");
                if (data != null && data.getIntExtra("RESULT", 0) == 1) {
                    Log.d("Garfield", "Got HERE 1");
                    String Template = new LuxandInfo(getApplicationContext()).getTemplate();
                    String Image = new LuxandInfo(getApplicationContext()).getImage();


                    SharedPreferences cit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    Date currentTime = Calendar.getInstance().getTime();


                    String tries = mDatabaseHelper2.getCaptureEntry(chosen_member);
                    if (TextUtils.isEmpty(tries)) {
                        if (capture_type.equals("capture_ip")) {
                            add(chosen_member, currentTime.toString(), Template, capture_type);

                            Boolean update = mDatabaseHelper2.updateMemberTemplate(chosen_member, Template);
                            if (update) {
                                Log.d("Garf", "done");
                            } else {
                                Log.d("Garf", "not done");
                            }
                        } else {
                            add(chosen_member, currentTime.toString(), Template, "");
                        }


                    } else {
                        if (capture_type.equals("capture_ip")) {

                            mDatabaseHelper2.updateTemplate(chosen_member, Template, capture_type);
                            Boolean update = mDatabaseHelper2.updateMemberTemplate(chosen_member, Template);
                            if (update) {
                                Log.d("Garf", "done");


                            } else {
                                Log.d("Garf", "not done");
                            }
                        } else {
                            mDatabaseHelper2.updateTemplate(chosen_member, Template, "");
                        }


                        //   addVerification(chosen_member, "0",tt,ik_number,"0",currentTime.toString() );

                    }

                    Toasty.success(getApplicationContext(), "Capture Successful", Toast.LENGTH_LONG).show();
                    Intent back_intent = new Intent(this, VerifyCapturedFace.class);
                    Log.d("Garfield", "Got HERE 2");
                    startActivity(back_intent);
                    Log.i("DANNY", "onActivityResult: " + Template);

                } else
                    Toasty.error(getApplicationContext(), "Capture Failed", Toast.LENGTH_LONG).show();


            } else if (requestCode == 619) {
                if (data.getIntExtra("RESULT", 0) == 1) {

                    //my code
                    SharedPreferences cit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    int prefC = cit.getInt("face_check_in_count", 0);

                    if (prefC == 0) {
                        cit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor c = cit.edit();
                        c.putInt("face_check_in_count", 1);
                        c.putString("my_flag", "1");
                        c.commit();

                    } else {
                        int nprefC = prefC + 1;

                        cit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor c = cit.edit();
                        c.putInt("face_check_in_count", nprefC);
                        c.putString("my_flag", "1");

                        c.commit();

                    }

                    //   addLOG(id,"1",s_id,"0",name);

                } else {
                    //  addLOG(id,"0",s_id,"0",name);

                }


            }
        }
    }

    public void add(String user_id, String time, String user_template, String type){

        boolean insertData = mDatabaseHelper2.addFailed(user_id,user_template,time, type);
        if(insertData){
            replace(chosen_member);
           // new callNextActivity(getApplicationContext()).run();


        }else{
            //  toastMessage("Failed");

        }

    }

    public  void addVerification(String user_id, String pass_flag, String tries, String ik_number, String picture, String time){

        boolean insertData = mDatabaseHelper2.addVerification(user_id,pass_flag,tries,ik_number,picture,time);
        if(insertData){
          //  replace(chosen_member);
            // new callNextActivity(getApplicationContext()).run();


        }else{
            //  toastMessage("Failed");

        }

    }
    public void replace(String member_id){
        boolean insertData = mDatabaseHelper2.replaceSync(member_id);
        if(insertData){
            // new callNextActivity(getApplicationContext()).run();

           // Toasty.info(getApplicationContext(), "Replaced", Toast.LENGTH_LONG).show();

        }else{


        }
    }


}
