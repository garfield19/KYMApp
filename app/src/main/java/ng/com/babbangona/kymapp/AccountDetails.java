package ng.com.babbangona.kymapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;
import ng.com.babbangona.kymapp.Database.CardDBHelper;
import ng.com.babbangona.kymapp.Database.DatabaseHelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.babbangona.bg_face.LuxandAuthActivity;
import com.babbangona.bg_face.LuxandInfo;

import java.util.ArrayList;
import java.util.List;

public class AccountDetails extends AppCompatActivity {
    EditText t1, t2, t3, t4, t5, t6, t7, t8, t9,t10, a_name;
    Spinner bank_name;
    Button btn_finish;

    String s_id, account_number, bank, accnt_name, capture_type, wrong_card, account_details;

    DatabaseHelper mDatabaseHelper2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        a_name = findViewById(R.id.account_name);
        bank_name = findViewById(R.id.bank);

       // bank_name.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> banks = new ArrayList<String>();
        banks.add("Access Bank");
        banks.add("Citibank");
        banks.add("Diamond Bank");
        banks.add("Dynamic Standard Bank");
        banks.add("Ecobank Nigeria");
        banks.add("Fidelity Bank Nigeria");
        banks.add("First Bank of Nigeria");
        banks.add("First City Monument Bank");
        banks.add("Guaranty Trust Bank");
        banks.add("Heritage Bank Plc");
        banks.add("Jaiz Bank");
        banks.add("Keystone Bank Limited");
        banks.add("Providus Bank Plc");
        banks.add("Polaris Bank");
        banks.add("Stanbic IBTC Bank Nigeria Limited");
        banks.add("Standard Chartered Bank");
        banks.add("Sterling Bank");
        banks.add("Suntrust Bank Nigeria Limited");
        banks.add("Union Bank of Nigeria");
        banks.add("United Bank for Africa");
        banks.add("Unity Bank Plc");
        banks.add("Wema Bank");
        banks.add("Zenith Bank");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, banks);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        bank_name.setAdapter(dataAdapter);

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
        t10  = findViewById(R.id.t10);

        btn_finish = findViewById(R.id.finish_verification);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        s_id = preferences.getString("unique_member_id", "");



        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String proceed = mDatabaseHelper2.getPassStatus(s_id);
                String pass = mDatabaseHelper2.getApproveStatus(s_id);

            //    if (proceed.equals("1") || pass.equals("1")) {

                    account_number = t1.getText().toString() + t2.getText().toString() + t10.getText().toString() + t3.getText().toString() + t4.getText().toString() + t5.getText().toString() + t6.getText().toString() + t7.getText().toString() + t8.getText().toString() + t9.getText().toString();
                    accnt_name = a_name.getText().toString();
                    bank = bank_name.getSelectedItem().toString();

                    account_details = accnt_name + "|" + account_number + "|" + bank;


                    CardDBHelper dbHelper = new CardDBHelper(getApplicationContext());
                    dbHelper.getReadableDatabase();
                    String test = t3.getText().toString() + t4.getText().toString() + t5.getText().toString() + t6.getText().toString() + t7.getText().toString() + t8.getText().toString() + t9.getText().toString();

                    if (account_number.toString().length() != 10) {
                        Toasty.error(getApplicationContext(), "Account Number Incorrect!", Toast.LENGTH_LONG, true).show();
                    } else {

                        LuxandInfo luxandInfo = new LuxandInfo(getApplicationContext());


                        luxandInfo.putTemplate(mDatabaseHelper2.getTemplate(s_id));


                        Intent myIntent = new Intent(getApplicationContext(), LuxandAuthActivity.class);
                        //myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivityForResult(myIntent, 419);


                    }
            //    }

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
                    t10.requestFocus();
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

                    t10.addTextChangedListener(new TextWatcher() {

                        public void onTextChanged(CharSequence s, int start,int before, int count)
                        {
                            // TODO Auto-generated method stub
                            if(t10.getText().toString().length()==1)     //size as per your requirement
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
            if (data.getIntExtra("RESULT", 0) == 1) {

                Toasty.success(getApplicationContext(), "KYM Successful", Toast.LENGTH_LONG).show();

                String pass_flag = mDatabaseHelper2.getPassStatus(s_id);
                if(pass_flag.equals("1")){
                    mDatabaseHelper2.replaceStatus(s_id);
                    mDatabaseHelper2.replacePassStatus(s_id);
                }

                mDatabaseHelper2.updateBGCard(s_id,account_details);
                mDatabaseHelper2.updateMemberBGCard(s_id,account_details);
                mDatabaseHelper2.replaceSync(s_id);
                mDatabaseHelper2.updateWrongCard(s_id,wrong_card);

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

                mDatabaseHelper2.updateBGCard(s_id,account_details);
                mDatabaseHelper2.updateMemberBGCard(s_id,account_details);
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
    }
}
