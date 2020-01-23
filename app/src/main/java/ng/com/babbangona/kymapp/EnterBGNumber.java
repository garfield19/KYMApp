package ng.com.babbangona.kymapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.babbangona.bg_face.LuxandAuthActivity;
import com.babbangona.bg_face.LuxandInfo;

import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import ng.com.babbangona.kymapp.Database.CardDBHelper;
import ng.com.babbangona.kymapp.Database.DBHelper;
import ng.com.babbangona.kymapp.Database.DatabaseHelper;

public class EnterBGNumber extends AppCompatActivity {
    EditText t1, t2, t3, t4, t5, t6, t7, t8, t9;
    Button btn_finish;

    String s_id, bg_number, capture_type, wrong_card;

    DatabaseHelper mDatabaseHelper2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_bgnumber);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDatabaseHelper2 = new DatabaseHelper(this);

        t1  = findViewById(R.id.t1);
        t2  = findViewById(R.id.t2);
        t3  = findViewById(R.id.t3);
        t4  = findViewById(R.id.t4);
        t5  = findViewById(R.id.t5);
        t6  = findViewById(R.id.t6);
        t7  = findViewById(R.id.t7);
        t8  = findViewById(R.id.t8);
        t9  = findViewById(R.id.t9);

        btn_finish = findViewById(R.id.finish_verification);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        s_id = preferences.getString("unique_member_id", "");



        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String proceed = mDatabaseHelper2.getPassStatus(s_id);
                String pass = mDatabaseHelper2.getApproveStatus(s_id);

               // if(proceed.equals("1") || pass.equals("1")) {

                bg_number = t1.getText().toString() + t2.getText().toString() + "/" + t3.getText().toString() + t4.getText().toString() + t5.getText().toString() + t6.getText().toString() + t7.getText().toString() + t8.getText().toString() + t9.getText().toString();

                String given = mDatabaseHelper2.getGivenCard(bg_number);

                CardDBHelper dbHelper = new CardDBHelper(getApplicationContext());
                dbHelper.getReadableDatabase();
                String test = t3.getText().toString() + t4.getText().toString() + t5.getText().toString() + t6.getText().toString() + t7.getText().toString() + t8.getText().toString() + t9.getText().toString();
                int count = dbHelper.getCardCount(test);

                Log.d("garfield", test);

                if (count > 0) {
                    if (given.equals("")) {


                        if (bg_number.toString().length() != 10) {
                            Toasty.error(getApplicationContext(), "BG Number Incorrect!", Toast.LENGTH_LONG, true).show();
                        } else {


                            wrong_card = "0";
                            LuxandInfo luxandInfo = new LuxandInfo(getApplicationContext());


                            luxandInfo.putTemplate(mDatabaseHelper2.getTemplate(s_id));


                            Intent myIntent = new Intent(getApplicationContext(), LuxandAuthActivity.class);
                            //myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivityForResult(myIntent, 419);


                        }

                    } else {
                        String when = mDatabaseHelper2.getCardTime(bg_number);
                        String s = when.substring(0, 10);
                        Toasty.error(getApplicationContext(), "Card already given to " + given + " on " + s, Toast.LENGTH_LONG).show();


                    }


                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EnterBGNumber.this);

                    alertDialogBuilder.setMessage("Card incorrect, would you like to retry");
                    alertDialogBuilder.setPositiveButton("CONTINUE",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                    LuxandInfo luxandInfo = new LuxandInfo(getApplicationContext());
                                    luxandInfo.putTemplate(mDatabaseHelper2.getTemplate(s_id));
                                    Intent myIntent = new Intent(getApplicationContext(), LuxandAuthActivity.class);
                                    //myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    wrong_card = "1";
                                    startActivityForResult(myIntent, 419);
                                }
                            });
                    alertDialogBuilder.setNegativeButton("RETRY",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {


                                    t1.setText("");
                                    t2.setText("");
                                    t3.setText("");
                                    t4.setText("");
                                    t9.setText("");
                                    t5.setText("");
                                    t6.setText("");
                                    t7.setText("");
                                    t8.setText("");

                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }


            }



        });



        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        t1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(t1.getText().toString().length()==1)     //size as per your requirement
                {
                    t2.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        t2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(t2.getText().toString().length()==1)     //size as per your requirement
                {
                    t3.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        t3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(t3.getText().toString().length()==1)     //size as per your requirement
                {
                    t4.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        t4.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(t4.getText().toString().length()==1)     //size as per your requirement
                {
                    t5.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        t5.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(t5.getText().toString().length()==1)     //size as per your requirement
                {
                    t6.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        t6.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(t6.getText().toString().length()==1)     //size as per your requirement
                {
                    t7.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        t7.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(t7.getText().toString().length()==1)     //size as per your requirement
                {
                    t8.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        t8.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(t8.getText().toString().length()==1)     //size as per your requirement
                {
                    t9.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 419) {
            if (data.getIntExtra("RESULT", 0) != 1) {

                Toasty.success(getApplicationContext(), "KYM Successful", Toast.LENGTH_LONG).show();


                String pass_flag = mDatabaseHelper2.getPassStatus(s_id);
                if(pass_flag.equals("1")){
                    mDatabaseHelper2.replaceStatus(s_id);
                    mDatabaseHelper2.replacePassStatus(s_id);
                }


                mDatabaseHelper2.updateMemberBGCard(s_id,bg_number);
                mDatabaseHelper2.replaceSync(s_id);
                mDatabaseHelper2.updateWrongCard(s_id,wrong_card);

                mDatabaseHelper2.updateBGCard(s_id,bg_number);




                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("capture_type","");
                editor.putString("picker","");
                editor.putString("chosen_member_name","");
                editor.putString("unique_member_id", "");
                editor.putString("unique_member_id", "");
                editor.putString("chosen_member_ik", "");
                editor.putString("old_member_id", "");
                editor.apply();

                Intent i=new Intent(this,Home.class);
               this.startActivity(i);
                finish();



            }else {
                Toasty.error(getApplicationContext(), "Verification Failed", Toast.LENGTH_LONG).show();

                Toasty.success(getApplicationContext(), "KYM Successful", Toast.LENGTH_LONG).show();

                mDatabaseHelper2.updateBGCard(s_id,bg_number);
                mDatabaseHelper2.updateMemberBGCard(s_id,bg_number);
                //  mDatabaseHelper2.replaceStatus(s_id);
                mDatabaseHelper2.replaceSync(s_id);
                mDatabaseHelper2.updateWrongCard(s_id,wrong_card);
                //  mDatabaseHelper2.replacePassStatus(s_id);


                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("capture_type","");
                editor.putString("picker","");
                editor.putString("chosen_member_name","");
                editor.putString("unique_member_id", "");
                editor.putString("unique_member_id", "");
                editor.putString("chosen_member_ik", "");
                editor.putString("old_member_id", "");
                editor.apply();

                Intent i=new Intent(this,Home.class);
                this.startActivity(i);
                finish();

            }
        }
        }



    @Override
    public boolean onSupportNavigateUp(){
        super.onBackPressed();
        return true;
    }
    @Override
    public void onBackPressed() {
//        if (!shouldAllowBack()) {
//            doSomething();
//        } else {
        super.onBackPressed();
        //   }
    }}
