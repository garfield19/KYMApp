package ng.com.babbangona.kymapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ng.com.babbangona.kymapp.Adapter.MembersAdapter;
import ng.com.babbangona.kymapp.Data.POJO.Members;
import ng.com.babbangona.kymapp.Database.DatabaseHelper;

public class MembersActivity extends AppCompatActivity {

    private DatabaseHelper db;
    DatabaseHelper mDatabaseHelper2;
    TextView ik_number;
    private List<Members> membersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
ik_number = findViewById(R.id.ik_number);


        getSupportActionBar().setTitle("Members");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        Intent intent = getIntent();

        String chosen_ik = intent.getStringExtra("ik_number");
        ik_number.setText(chosen_ik);


        MembersAdapter adapter;

        mDatabaseHelper2 = new DatabaseHelper(this);

        db = new DatabaseHelper(this);

        membersList.addAll(db.getTrustGroupMembers(chosen_ik));

        RecyclerView recyclerView = findViewById(R.id.rvMembers);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MembersAdapter(this, membersList);
        //adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}