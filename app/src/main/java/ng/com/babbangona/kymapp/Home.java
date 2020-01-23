package ng.com.babbangona.kymapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;

import com.babbangona.bg_face.LuxandAuthActivity;
import com.babbangona.bg_face.LuxandInfo;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.RotatingPlane;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;


import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import ng.com.babbangona.kymapp.Adapter.TrustGroupListAdapter;
import ng.com.babbangona.kymapp.Data.POJO.Capture;
import ng.com.babbangona.kymapp.Data.POJO.MemberApproved;
import ng.com.babbangona.kymapp.Data.POJO.Members;
import ng.com.babbangona.kymapp.Data.POJO.MembersWhoNeedToFillCardDetails;
import ng.com.babbangona.kymapp.Data.POJO.Verification;
import ng.com.babbangona.kymapp.Data.Remote.APIService;
import ng.com.babbangona.kymapp.Data.Remote.ApiUtils;
import ng.com.babbangona.kymapp.Data.Remote.RetrofitClient;
import ng.com.babbangona.kymapp.Database.DatabaseHelper;
import ng.com.babbangona.kymapp.Model.ServerResponse;
import ng.com.babbangona.kymapp.Tools.DecompressFast;
import ng.com.babbangona.kymapp.Tools.ZipManager;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ng.com.babbangona.kymapp.Data.Remote.ApiUtils.BASE_URL;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int PICKFILE_REQUEST_CODE = 300;


    DatabaseHelper mDatabaseHelper2;

    private APIService mAPIService;

    private List<Members> membersList = new ArrayList<>();

    private DatabaseHelper db;
    final String TAG = "Garfield";

    String version = "";


    String id ,member_verification, data2;

    ArrayList<Capture> aLogList = new ArrayList<Capture>( );
    ArrayList<Verification> vLogList = new ArrayList<Verification>( );

    TrustGroupListAdapter adapter;

    RecyclerView recyclerView;

    TextView app_name, user_id;
    String unzipLocation;
    EditText search;

    ArrayList<String> messageAttachment = new ArrayList<>();

    private ProgressBar progressBar;
    private int progressStatus = 0;
    private TextView textView;
    private Handler handler = new Handler();

    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);





        requestCameraPermission();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDatabaseHelper2 = new DatabaseHelper(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar_cyclic);
        progressBar.setVisibility(View.INVISIBLE);

        thread = new Thread(new Runnable() {
            public void run() {

                RotatingPlane wave = new RotatingPlane();
                wave.setColor(R.color.colorPrimary);
                progressBar.setIndeterminateDrawable(wave);
            }
        });




       // startSpin();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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


        SharedPreferences mySPrefs =PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        id = mySPrefs.getString("staff_id","");
        String staff_name = mySPrefs.getString("staff_name", "");

        search = findViewById(R.id.search);

        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Visitation Pictures/";
        File newdir = new File(dir);
        newdir.mkdirs();

        unzipLocation = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Extract/";
        File newdir2 = new File(unzipLocation);
        newdir2.mkdirs();

        final String dir3 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/KIVA/";
        File newdir3 = new File(dir3);
        newdir3.mkdirs();

        final String dir4 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Card/";
        File newdir4 = new File(dir4);
        newdir4.mkdirs();

        app_name = findViewById(R.id.m_version);
        user_id = findViewById(R.id.m_user_name);

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        user_id.setText(id);
        String v = "KYM version " + version;
        app_name.setText(v);



        db = new DatabaseHelper(this);

        membersList.addAll(db.getTrustGroups());



        recyclerView = findViewById(R.id.rvTrustGroups);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getBaseContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TrustGroupListAdapter(this, membersList);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null){
                    adapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        search.setOnFocusChangeListener((p, hasFocus) -> {
            if (!hasFocus) {
                search.setCursorVisible(false);
            }
        });
    }

    public void traverse (File dir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; ++i) {
                File file = files[i];
                if (file.isDirectory()) {
                    traverse(file);
                } else {

                    Log.d("DIRect:",file.toString());
                    File file2 = new File(file.toString());
                    uploadFile(file2,file.getName());
                }
            }
        }
    }



    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toasty.info(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestCameraPermission() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    public void upMemberTemplates(String member_templates){
        mAPIService = ApiUtils.getAPIService();
        mAPIService.sendCapturedMember(member_templates).enqueue(new Callback<List<Capture>>() {
            @Override
            public void onResponse(Call<List<Capture>> call, Response<List<Capture>> response) {
                if(response.isSuccessful()) {

                    SQLiteDatabase db = mDatabaseHelper2.getWritableDatabase();

                    Toast.makeText(getApplicationContext(),"Syncing Complete",Toast.LENGTH_SHORT).show();

                    Log.i(TAG, "audit data submitted to API." + response.body().toString());
                    int count = response.body().size();
                    for (int i = 0; i< count; i++){
                        String uid = response.body().get(i).getUid();

                        mDatabaseHelper2.updateCaptureFlag(uid);

                        //todo update synced to 1 here

                        if(i == count - 1){

                               }

                        Log.d("Response try" , uid.toString());

                    }
                    data2 = null;
                    data2 =  uploadVerificationLogData();
                    Log.i("VERIFICATION DATA",data2);
                    upMemberVerification(data2);
                }else {
                    Toasty.error(getApplicationContext(), "Unable to sync : SERVER ERROR", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<List<Capture>> call, Throwable t) {
                //   dismissProgressDialog();
                Log.e(TAG, "Unable to submit faces to API." + t);
                Toasty.error(getApplicationContext(), "Unable to sync : NO INTERNET ACCESS", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void upMemberVerification(String member_verification){
        Log.i(TAG, "submit to API." + member_verification);

        mAPIService = ApiUtils.getAPIService();
        mAPIService.sendVerification(member_verification).enqueue(new Callback<List<Verification>>() {
            @Override
            public void onResponse(Call<List<Verification>> call, Response<List<Verification>> response) {
                if(response.isSuccessful()) {

                    SQLiteDatabase db = mDatabaseHelper2.getWritableDatabase();

                    Toasty.success(getApplicationContext(),"Syncing Complete",Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "verification data submitted to API." + response.body().toString());
                    int count = response.body().size();
                    for (int i = 0; i< count; i++){
                        String uid = response.body().get(i).getUid();
                        Log.i(TAG, "verification data submitted to API." + uid);

                        mDatabaseHelper2.updateVerificationFlag(uid);

                        //todo update synced to 1 here

                        if(i == count - 1){
                        }

                        Log.d("Response try" , uid.toString());

                    }

                    mAPIService = ApiUtils.getAPIService();
                    SharedPreferences mySPrefs =PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = mySPrefs.edit();
                    String s_id = mySPrefs.getString("staff_id","");
                    String date = mySPrefs.getString("test date2","");

                    sendID(s_id,date);

                }else {
                    Toasty.error(getApplicationContext(), "Unable to sync : SERVER ERROR", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<List<Verification>> call, Throwable t) {
                //   dismissProgressDialog();
                Log.e(TAG, "Unable to submit faces to API." + t);
                Toasty.error(getApplicationContext(), "Unable to sync : NO INTERNET ACCESS", Toast.LENGTH_SHORT).show();

            }
        });



    }

    public ArrayList<Capture> upload_member_capture_data() {

        SQLiteDatabase database = mDatabaseHelper2.getWritableDatabase();
        Map<String, String> map = null;
        ArrayList<Capture> auditList;
        auditList =new ArrayList<>();
        ArrayList<String> testString = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM captures_records_db where synced = " + "0" , null);

        if(cursor.moveToFirst()){

            do{
                Capture temp = new Capture();
                temp.setMember_id(cursor.getString(1));
                temp.setTemplate(cursor.getString(2));
                temp.setType(cursor.getString(4));
                temp.setVersion(version);
                aLogList.add(temp);

                cursor.moveToNext();
            }
            while (!cursor.isAfterLast());
        }

        cursor.close();
        database.close();


        return aLogList;
    }
    public ArrayList<Verification> upload_verification_data() {

        SQLiteDatabase database = mDatabaseHelper2.getWritableDatabase();
        ArrayList<String> testString = new ArrayList<>();
        //todo sync up only people with details
        Cursor cursor = database.rawQuery("SELECT * FROM verification_records_db where bg_card <> '' and synced <> '1'" , null);

        if(cursor.moveToFirst()){

            do{
                Verification temp = new Verification();
                temp.setMember_id(cursor.getString(1));
                temp.setPass_flag(cursor.getString(3));
                temp.setTries(cursor.getString(4));
                temp.setPicture_taken(cursor.getString(5));
                temp.setBg_card(cursor.getString(6));
                temp.setCard(cursor.getString(9));
                temp.setTime(cursor.getString(8));
                temp.setWrong_card(cursor.getString(10));
                temp.setAccount_update(cursor.getString(11));
                temp.setVersion(version);

                vLogList.add(temp);

                cursor.moveToNext();
            }
            while (!cursor.isAfterLast());
        }

        cursor.close();
        database.close();


        return vLogList;
    }

    public String uploadMemberCaptureLogData(){
        ArrayList<Capture> mAudit = upload_member_capture_data();
        Gson gson = new Gson();
        return gson.toJson(mAudit,mAudit.getClass());
    }

    public String uploadVerificationLogData(){
        ArrayList<Verification> mVerification = upload_verification_data();
        Gson gson = new Gson();
        return gson.toJson(mVerification,mVerification.getClass());
    }
    public void startSpin(){

        new Thread(new Runnable() {
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                RotatingPlane wave = new RotatingPlane();
                wave.setColor(R.color.colorPrimary);
                progressBar.setIndeterminateDrawable(wave);
            }
        }).start();
    }

    public void sendID(String mik_id,String date) {
      //  startSpin();


        mAPIService.sendSupervisorId(mik_id,date).enqueue(new Callback<List<Members>>() {
            @Override
            public void onResponse(Call<List<Members>> call, Response<List<Members>> response) {

                if(response.isSuccessful()) {

                    int count = response.body().size();

                    if(count == 0){
                        getApproved(mik_id);
                    }

                    Log.d("Response" , response.body().toString());

                    SharedPreferences session = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = session.edit();



                    for (int i = 0; i< count; i++){
                        String name = response.body().get(i).getMemberName();
                        String id = response.body().get(i).getUniqueMemberId();
                        String template = response.body().get(i).getTemplate();
                        String role = response.body().get(i).getMemberRole();
                        String ikNumber = response.body().get(i).getIkNumber();
                        String picker = response.body().get(i).getPicker();
                        String old_member_id = response.body().get(i).getMember_id();
                        editor.putString("test date2", response.body().get(i).getM_date());
                        Log.e("Garfield",response.body().get(i).getM_date());
                        editor.apply();

                        String tries = mDatabaseHelper2.getMemberExists(id);


                        if(TextUtils.isEmpty(tries)) {
                            addList(name,id,role,picker,template,"0",ikNumber,"0","0",old_member_id);
                        }




                        if(i == count - 1){

                           getApproved(mik_id);

                        }

                    }


                    Log.i("Garfield", "post submitted to API." + response.body().toString());


                }else{

                    Toasty.error(getApplicationContext(),"Download Failed : No Network Access",Toast.LENGTH_SHORT).show();


                }

            }

            @Override
            public void onFailure(Call<List<Members>> call, Throwable t) {
                // dismissProgressDialog();


                Toasty.error(getApplicationContext(),"Download Failed : Contact the ES Team",Toast.LENGTH_SHORT).show();

                Log.e("Garfield", "NET_ERROR:" + t.toString());
                Log.e("Garfield", "Unable to submit post to API.");
            }
        });

    }



    public void getApproved(String id) {

        mAPIService.getApprovedUsers(id).enqueue(new Callback<List<MemberApproved>>() {
            @Override
            public void onResponse(Call<List<MemberApproved>> call, Response<List<MemberApproved>> response) {

                if(response.isSuccessful()) {

                    int count = response.body().size();
                    Log.d("Response" , response.body().toString());


                    for (int i = 0; i< count; i++){
                        String uid = response.body().get(i).getUnique_member_id();
                        mDatabaseHelper2.replaceApproved(uid);
                    }
                   // Toasty.success(getApplicationContext(),"Download Successful",Toast.LENGTH_SHORT).show();

                   getMembersWhoNeedToFillBGCard(id);


                    Log.i("Garfield", "post submitted to API." + response.body().toString());

                }else{

                    Toasty.error(getApplicationContext(),"Download Failed : No Network Access",Toast.LENGTH_SHORT).show();


                }

            }

            @Override
            public void onFailure(Call<List<MemberApproved>> call, Throwable t) {
                Toasty.error(getApplicationContext(),"Download Failed : Contact ES Team",Toast.LENGTH_SHORT).show();

                Log.e("Garfield", "NET_ERROR222:" + t.toString());
                Log.e("Garfield", "Unable to submit post to API22.");
            }
        });

    }

    public void getMembersWhoNeedToFillBGCard(String id) {

        mAPIService.getUsersWhoNeedToFillCardDetails(id).enqueue(new Callback<List<MembersWhoNeedToFillCardDetails>>() {
            @Override
            public void onResponse(Call<List<MembersWhoNeedToFillCardDetails>> call, Response<List<MembersWhoNeedToFillCardDetails>> response) {

                if(response.isSuccessful()) {

                    int count = response.body().size();
                    Log.d("Response" , response.body().toString());


                    for (int i = 0; i< count; i++){
                        String uid = response.body().get(i).getUniqueMemberId();
                        String pass_flag = response.body().get(i).getPassFlag();
                        String details = response.body().get(i).getDetails();
                        String need_approval = response.body().get(i).getNeed_approval();
                        Log.d("Garfield",uid + "_" + pass_flag + "_" +details);

                        //todo different update for the member | input picker bug
                        mDatabaseHelper2.updateMemberBGCard(uid,details);

                        String picker = mDatabaseHelper2.getPicker(uid);



                        if(pass_flag.equals("1") && details.length() != 0){
                            //update done to 1 = green
                            mDatabaseHelper2.replaceStatus(uid);
                            mDatabaseHelper2.replacePassStatusVariable(uid,"1");

                        }else if(pass_flag.equals("1") && details.length() == 0){
                            //update done to 0, pass to 1, create new record if none exists, update record if exists = yellow
                            mDatabaseHelper2.defaultDone(uid);

                            if(picker.equals("1")){
                                String tries = mDatabaseHelper2.getTries(uid);
                                if(TextUtils.isEmpty(tries)) {
                                    mDatabaseHelper2.replaceCardFillingStatus(uid);
                                }else {
                                    mDatabaseHelper2.replacePassStatusVariable(uid,"1");
                                }
                            }



                        }else if(pass_flag.equals("0") && need_approval.equals("1")){
                            //update done to 0, pass to 0, failed verification to 1 = yellow

                            mDatabaseHelper2.replaceFailPictureStatus(uid);
                            mDatabaseHelper2.defaultDone(uid);
                            if(picker.equals("1")){
                                String tries = mDatabaseHelper2.getTries(uid);
                                if(TextUtils.isEmpty(tries)) {
                                    mDatabaseHelper2.replaceCardFillingStatusFailedPass(uid);
                                }else{
                                    mDatabaseHelper2.replacePassStatusVariable(uid,"0");
                                }
                            }

                        }else if(pass_flag.equals("0")){
                            //this is yellow
                            mDatabaseHelper2.defaultPass(uid);
                            mDatabaseHelper2.defaultDone(uid);
                            if(picker.equals("1")){
                                String tries = mDatabaseHelper2.getTries(uid);
                                if(TextUtils.isEmpty(tries)) {
                                    mDatabaseHelper2.replaceCardFillingStatusFailedPass(uid);
                                }else{
                                    mDatabaseHelper2.replacePassStatusVariable(uid,"0");
                                }
                            }

                        }

                    }
                    Toasty.success(getApplicationContext(),"Download Successful",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), Home.class);
                    startActivity(intent);


                    Log.i("Garfield", "post submitted to API." + response.body().toString());

                }else{

                    Toasty.error(getApplicationContext(),"Download Failed : No Network Access",Toast.LENGTH_SHORT).show();


                }

            }

            @Override
            public void onFailure(Call<List<MembersWhoNeedToFillCardDetails>> call, Throwable t) {
                Toasty.error(getApplicationContext(),"Download Failed : Contact ES Team",Toast.LENGTH_SHORT).show();

                Log.e("Garfield", "NET_ERROR222:" + t.toString());
                Log.e("Garfield", "Unable to submit post to API22.");
            }
        });

    }

    private void uploadFile(File file, String name) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        APIService getResponse = RetrofitClient.getClient(BASE_URL).create(APIService.class);
        Call call = getResponse.uploadFile(fileToUpload, filename);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                ServerResponse serverResponse = (ServerResponse) response.body();
                if (serverResponse != null) {
                    if (serverResponse.getSuccess()) {
                        Toasty.info(getApplicationContext(), serverResponse.getMessage() + " " + name,Toast.LENGTH_SHORT).show();
                        boolean deleted = file.delete();
                    } else {
                        Toasty.info(getApplicationContext(), serverResponse.getMessage() + " " + name,Toast.LENGTH_SHORT).show();
                    }
                } else {
                    assert serverResponse != null;
                    Toasty.info(getApplicationContext(), serverResponse.getMessage(),Toast.LENGTH_SHORT).show();
                    Log.v("Response", serverResponse.toString());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toasty.error(getApplicationContext(), "Error Uploading Files",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addList(String name, String id, String role, String picked, String template, String done,String ik_number , String p_done,String f, String old_member_id){

        boolean insertData = mDatabaseHelper2.addMembersData(name,id,role,picked,template,done,ik_number, p_done,f,old_member_id);
        if(insertData){

        }else{

        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_up) {

            final String data = uploadMemberCaptureLogData();
            // Log.i("MEMBER DATA",data);
            upMemberTemplates(data);

            Toasty.info(getApplicationContext(), "Sync Started...", Toast.LENGTH_SHORT).show();


            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File (sdCard.getAbsolutePath() + "/Pictures/Visitation Pictures/");
            showToast("Uploading Pictures Please wait.");
            traverse(dir);

            // Handle the camera action
        } else if (id == R.id.nav_down) {

            //todo have u synced up previously done KYM
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Home.this);
            alertDialogBuilder.setMessage("Have you synced up previous KYM Done?");
            alertDialogBuilder.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            progressBar.setVisibility(View.VISIBLE);
                            //        thread.start();

                            Toasty.info(getApplicationContext(),"Downloading Data...",Toast.LENGTH_SHORT).show();

                            SQLiteDatabase db = mDatabaseHelper2.getWritableDatabase();
                            mAPIService = ApiUtils.getAPIService();
                            SharedPreferences mySPrefs =PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor editor = mySPrefs.edit();
                            String s_id = mySPrefs.getString("staff_id","");
                            String date = mySPrefs.getString("test date2","");

                            sendID(s_id,date);


                        }
                    });
            alertDialogBuilder.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            Toasty.info(getApplicationContext(),"Sync UP Frist",Toast.LENGTH_LONG).show();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();



        }
//        else if (id == R.id.nav_pic_up) {
//            File sdCard = Environment.getExternalStorageDirectory();
//            File dir = new File (sdCard.getAbsolutePath() + "/Pictures/Visitation Pictures/");
//            showToast("Started Please wait.");
//            traverse(dir);
//
//
//
//        }
        else if (id == R.id.extract_pic) {
            Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
            chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
            chooseFile.setType("*/*");
            startActivityForResult(
                    Intent.createChooser(chooseFile, "Choose a file"),
                    PICKFILE_REQUEST_CODE
            );

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 255 && data != null && resultCode == RESULT_OK)
        {

        }
        else if(requestCode == PICKFILE_REQUEST_CODE && data != null && resultCode == RESULT_OK)
        {

            try {

                Uri uri = data.getData();
                File file = new File(uri.getPath());
                final String[] split = file.getPath().split(":");//split the path.
                String filePath_1 = split[split.length-1];//assign it to a string(your choice).
                String EnvironmentPath = Environment.getExternalStoragePublicDirectory("") + ""; //
                String filePath = filePath_1;
                if(filePath.contains(EnvironmentPath));
                else filePath = EnvironmentPath + "/" + filePath_1;


                if(filePath.contains(".zip"))
                {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("file_name", filePath);
                    editor.apply();

                    String finalFilePath = filePath;
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {

                            String zipFile = finalFilePath; //your zip file location
                            DecompressFast df= new DecompressFast(zipFile, unzipLocation);
                            df.unzip();
                        }
                    });

                    Toasty.success(getApplicationContext(),"Added Successfully",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toasty.error(this,"INVALID FILE, PLEASE SELECT  A .ZIP FILE", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Log.e("FileSelectorActivity", "File select error", e);
                finish();
            }
        }
    }


}
