package ng.com.babbangona.kymapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import ng.com.babbangona.kymapp.Database.DatabaseHelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class IsPictureMember extends AppCompatActivity {

    ImageView picture;
    String s_id;
    Button yes,no;
    DatabaseHelper mDatabaseHelper2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_is_picture_member);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDatabaseHelper2 = new DatabaseHelper(this);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        s_id = preferences.getString("unique_member_id", "");
        String ik_number = preferences.getString("chosen_member_ik","");

        yes = (Button) findViewById(R.id.yes);
        no = (Button) findViewById(R.id.no);


        picture = findViewById(R.id.capturedPicture);
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Extract/"+ik_number+"_member.jpg";
        Log.d("GARField",dir);
        File imgFile = new File(dir);

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            picture.setImageBitmap(myBitmap);

        }

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(IsPictureMember.this, CaptureFailedTemplateFace.class);
                startActivity(i);
                finish();

            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(IsPictureMember.this);
                alertDialogBuilder.setMessage("Please find the member in the picture");
                        alertDialogBuilder.setPositiveButton("ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {

                                        Intent i = new Intent(IsPictureMember.this, Home.class);
                                        startActivity(i);
                                        finish();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setCancelable(false);
                alertDialog.show();

            }
        });




    }
}
