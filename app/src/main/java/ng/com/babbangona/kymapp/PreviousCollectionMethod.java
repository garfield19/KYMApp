package ng.com.babbangona.kymapp;

import androidx.appcompat.app.AppCompatActivity;
import ng.com.babbangona.kymapp.Database.AccountDBHelper;
import ng.com.babbangona.kymapp.Database.CardDBHelper;
import ng.com.babbangona.kymapp.Database.DatabaseHelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PreviousCollectionMethod extends AppCompatActivity {

    TextView account_name, account_number, bank;

    Button yes, no;

    String ik_number, s_id;

    DatabaseHelper mDatabaseHelper2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_collection_method);

        account_name = findViewById(R.id.account_name);
        account_number = findViewById(R.id.account_number);
        bank = findViewById(R.id.bank);

        yes = findViewById(R.id.yes_collection);
        no = findViewById(R.id.no_collection);

        mDatabaseHelper2 = new DatabaseHelper(this);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        ik_number = preferences.getString("chosen_member_ik", "");
        s_id = preferences.getString("unique_member_id", "");


        AccountDBHelper dbHelper = new AccountDBHelper(getApplicationContext());
        dbHelper.getReadableDatabase();
        String account_name_t = dbHelper.getAccountName(ik_number);
        String account_number_t = dbHelper.getAccountNumber(ik_number);
        String bankName = dbHelper.getBank(ik_number);

        bank.setText(bankName);
        try{
        if(account_name_t.startsWith("BAB")) {
            account_number.setText(account_number_t);
        }else{
            String text = "*****" + account_number_t.substring(account_number_t.length()-5);
            account_number.setText(text);
        }
        }catch (Exception e){
            yes.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "No Account", Toast.LENGTH_LONG).show();
            Intent i=new Intent(getApplicationContext(),HarvestAdvance.class);
            startActivity(i);
        }

        account_name.setText(account_name_t);

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseHelper2.replaceAccountUpdate(s_id,"1");
                //if(account_name_t.startsWith("BAB")){
                    Intent i=new Intent(v.getContext(),HarvestAdvance.class);
                    v.getContext().startActivity(i);
               // }else{
//                    Intent i=new Intent(v.getContext(),AccountDetails.class);
//                    v.getContext().startActivity(i);
               // }

            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseHelper2.replaceAccountUpdate(s_id,"0");
                String account_details = account_name_t + "|" + account_number_t + "|" + bankName;

                String pass_flag = mDatabaseHelper2.getPassStatus(s_id);
                if(pass_flag.equals("1")){
                    mDatabaseHelper2.replaceStatus(s_id);
                    mDatabaseHelper2.replacePassStatus(s_id);
                }

                mDatabaseHelper2.updateBGCard(s_id,account_details);
                mDatabaseHelper2.updateMemberBGCard(s_id,account_details);
                mDatabaseHelper2.replaceSync(s_id);

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(PreviousCollectionMethod.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("capture_type","");
                editor.putString("picker","");
                editor.putString("chosen_member_name","");
                editor.putString("unique_member_id", "");
                editor.putString("unique_member_id", "");
                editor.putString("chosen_member_ik", "");
                editor.putString("old_member_id", "");
                editor.apply();

                Intent i=new Intent(PreviousCollectionMethod.this,Home.class);
                startActivity(i);
                finish();



            }
        });


    }
}
