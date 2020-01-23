package ng.com.babbangona.kymapp;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

public class SyncData {
    public static class SendAppVersion extends AsyncTask<String,String,Void> {

        Context context;

        public SendAppVersion(Context mCtx){
            this.context = mCtx;
        }


        @Override
        protected Void doInBackground(String... strings) {

            try {
                String package_name = strings[0];
                String version = strings[1];
                String staff_id = strings[2];


                String URL = "content://com.babbangona.accesscontrol/version_binding";
                Uri students = Uri.parse(URL);
                ContentValues contentValues = new ContentValues();

                //Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();

                contentValues.put("staff_id", staff_id);
                contentValues.put("user_version", version);


                //Log.d("response_int", package_name+" "+version+" "+staff_id);
                int x = context.getContentResolver().update(students, contentValues, "package_name=\"" + package_name + "\"", null);
                //Log.d("response_int", x + "");
                //Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();


            }catch (Exception e){

            }


            return null;
        }
    }

}
