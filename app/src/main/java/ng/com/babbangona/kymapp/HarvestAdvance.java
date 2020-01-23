package ng.com.babbangona.kymapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import ng.com.babbangona.kymapp.Database.DBHelper;
import ng.com.babbangona.kymapp.Database.DatabaseHelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class HarvestAdvance extends AppCompatActivity {

    Button card, account;
    String chosen_member;
    DatabaseHelper mDatabaseHelper2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harvest_advance);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabaseHelper2 = new DatabaseHelper(this);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        chosen_member = preferences.getString("unique_member_id", "");

        String ik_number = preferences.getString("chosen_member_ik", "");
        account = findViewById(R.id.account);


        card = findViewById(R.id.bg_card);

        DBHelper dbHelper = new DBHelper(this);
        dbHelper.getReadableDatabase();
        int count = dbHelper.getOldTGCount(ik_number);
        if(count <= 0){
            account.setVisibility(View.GONE);
        }

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   mDatabaseHelper2.replaceOldCollectionType(chosen_member,"account");



                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HarvestAdvance.this);
                alertDialogBuilder.setMessage("The name on the account must be member's name or payment won't be made.");
                alertDialogBuilder.setPositiveButton("ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent i=new Intent(v.getContext(),AccountDetails.class);
                                v.getContext().startActivity(i);
                                finish();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setCancelable(false);
                alertDialog.show();

            }
        });

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  mDatabaseHelper2.replaceOldCollectionType(chosen_member,"bg_card");
                Intent i=new Intent(v.getContext(),BGCard.class);
                v.getContext().startActivity(i);
            }
        });
    }
}
