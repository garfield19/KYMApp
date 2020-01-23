package ng.com.babbangona.kymapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import ng.com.babbangona.kymapp.Database.DatabaseHelper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class BGCard extends AppCompatActivity {


Button yes, no;
    String chosen_member;
    DatabaseHelper mDatabaseHelper2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bgcard);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabaseHelper2 = new DatabaseHelper(this);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        chosen_member = preferences.getString("unique_member_id", "");

        no = findViewById(R.id.no_card);
        yes = findViewById(R.id.yes_card);

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseHelper2.replaceCard(chosen_member,"0");

                Intent i=new Intent(v.getContext(),CapturePicture.class);
                v.getContext().startActivity(i);
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseHelper2.replaceCard(chosen_member,"1");
                Intent i=new Intent(v.getContext(),CapturePicture.class);
                v.getContext().startActivity(i);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}
