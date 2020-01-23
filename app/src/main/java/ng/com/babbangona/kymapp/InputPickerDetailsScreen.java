package ng.com.babbangona.kymapp;

import androidx.appcompat.app.AppCompatActivity;
import ng.com.babbangona.kymapp.Database.DatabaseHelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class InputPickerDetailsScreen extends AppCompatActivity {
    ImageView member_verification_icon ,member_account_icon;
    Button add_account, verify_member;
    TextView account_text, verify_text;
    DatabaseHelper mDatabaseHelper2;
    String ik_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_picker_details_screen);

        mDatabaseHelper2 = new DatabaseHelper(getApplicationContext());

        member_account_icon = findViewById(R.id.member_account_icon);
        member_verification_icon = findViewById(R.id.member_verification_icon);
        add_account = findViewById(R.id.add_account);
        verify_member = findViewById(R.id.verify_member);
        account_text = findViewById(R.id.account_text);
        verify_text = findViewById(R.id.verification_text);

        ik_number = "";

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String chosen_member = preferences.getString("unique_member_id", "");
        String picker = preferences.getString("picker", "");
        ik_number = preferences.getString("chosen_member_ik", "");


        int bgFilledNoPass = mDatabaseHelper2.bgCardNoPass(chosen_member);
        String passStatus = mDatabaseHelper2.getMemberPassStatus(chosen_member);
        int nonInputPicker = mDatabaseHelper2.getDoneStatus(chosen_member);
        Log.d("Garfield", String.valueOf(nonInputPicker));

        if(picker.equals("yes")){
            verify_member.setText("Verify");
        }else{
            verify_member.setText("Capture");

            member_account_icon.setVisibility(View.GONE);
            add_account.setVisibility(View.GONE);
            if(nonInputPicker > 0){
                bgFilledNoPass = 1;
            }
            verify_text.setText("Capture");
            account_text.setVisibility(View.GONE);
        }

       if (bgFilledNoPass > 0) {
member_account_icon.setImageDrawable(getResources().getDrawable(R.drawable.yes));
add_account.setEnabled(false);
       }else{
           member_account_icon.setImageDrawable(getResources().getDrawable(R.drawable.no));
           add_account.setEnabled(true);
           mDatabaseHelper2.replaceSync(chosen_member);
       }

       add_account.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               String tries = mDatabaseHelper2.getTries(chosen_member);
               Date currentTime = Calendar.getInstance().getTime();
               if (TextUtils.isEmpty(tries)) {
                   addVerification(chosen_member, "0", "1", ik_number, "0", currentTime.toString());
                   mDatabaseHelper2.replaceSync(chosen_member);

               } else {
                   int t = Integer.valueOf(tries) + 1;
                   String tt = String.valueOf(t);
                   mDatabaseHelper2.updateTries(chosen_member, tt);
                   mDatabaseHelper2.replaceSync(chosen_member);

               }


               Intent i = new Intent(getApplicationContext(), KivaCapture.class);
               startActivity(i);
           }
       });

       if(passStatus.equals("1")  || nonInputPicker > 0){
           member_verification_icon.setImageDrawable(getResources().getDrawable(R.drawable.yes));
           verify_member.setEnabled(false);
       }else{
           member_verification_icon.setImageDrawable(getResources().getDrawable(R.drawable.no));
           verify_member.setEnabled(true);
       }
       verify_member.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i = new Intent(getApplicationContext(), OldOrNew.class);
               mDatabaseHelper2.replaceSync(chosen_member);
               startActivity(i);
           }
       });

    }

    public void addVerification(String user_id, String pass_flag, String tries, String ik_number, String picture, String time){

        boolean insertData = mDatabaseHelper2.addVerification(user_id,pass_flag,tries,ik_number,picture,time);
        if(insertData){
            //  replace(chosen_member);
            // new callNextActivity(getApplicationContext()).run();


        }else{
            //  toastMessage("Failed");

        }

    }

}
