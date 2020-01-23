package ng.com.babbangona.kymapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import id.zelory.compressor.Compressor;
import ng.com.babbangona.kymapp.Database.DatabaseHelper;

public class CapturePicture extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    int TAKE_PHOTO_CODE = 0;
    int SAVE_PHOTO_CODE = 1;
    public static int count = 0;
    private File destFile;
    private File file;
    private File sourceFile;
    Uri outputFileUri, outputFileUri2;
    ImageView picture;

    String s_id, ik_number;

    String dir3;

    DatabaseHelper mDatabaseHelper2;

    private SimpleDateFormat dateFormatter;

    Button capture,img_continue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_picture);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDatabaseHelper2 = new DatabaseHelper(this);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        s_id = preferences.getString("unique_member_id", "");




        long startTime = System.currentTimeMillis();
        String theDate = getTime(startTime);
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/KYM/";
        File newdir = new File(dir);
        newdir.mkdirs();

        final String dir2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/KYM/";
        File newdir2 = new File(dir2);
        newdir2.mkdirs();


        capture = (Button) findViewById(R.id.btnCapture);
        img_continue = (Button) findViewById(R.id.btnCaptureContinue);
        picture = findViewById(R.id.capturedPicture);
        img_continue.setVisibility(View.INVISIBLE);
        //startCameraView();
        img_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(v.getContext(),CaptureCard.class);
                v.getContext().startActivity(i);
            }
        });



//        //capture.
        capture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final long time = System.currentTimeMillis();
                count++;
                String file = dir+"verification"+s_id+"_"+time+".jpg";
                String file2 = dir2+"verification"+s_id+"_"+time+".jpg";

                File newfile = new File(file);
                try {
                    newfile.createNewFile();
                }
                catch (IOException e)
                {
                }

                outputFileUri = Uri.fromFile(newfile);

                File newfile2 = new File(file2);
                try {
                    newfile2.createNewFile();
                }
                catch (IOException e)
                {
                }

                outputFileUri2 = Uri.fromFile(newfile2);

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri2);

                Intent cameraIntent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent2.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri2);
                // Bitmap bmp = decodeFile(destFile);

                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
               // startActivityForResult(cameraIntent2, SAVE_PHOTO_CODE);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            Log.d("CameraDemo", "Pic saved");


          //  Bitmap photo = (Bitmap) data.getExtras().get("data");

            Bitmap  mBitmap = null;
            File actualImage = new File(outputFileUri.getPath());
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outputFileUri);
            } catch (IOException e) {
                e.printStackTrace();
            }


            picture.setImageBitmap(mBitmap);
            capture.setText("Recapture");

            capture.setText("Recapture");

            resizeImage(outputFileUri);

            try {
                File compressedImage = new Compressor(this)
                        .setMaxWidth(640)
                        .setMaxHeight(480)
                        .setQuality(75)
                        .setCompressFormat(Bitmap.CompressFormat.WEBP)
                        .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES + "/Visitation Pictures/").getAbsolutePath())
                        .compressToFile(actualImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

//            boolean update = mDatabaseHelper2.updateImage(s_id);
//            if(update){
                img_continue.setVisibility(View.VISIBLE);
               // Toasty.info(getApplicationContext(),s_id,Toast.LENGTH_SHORT).show();
//            }else{
//
//            }






            //new callNextActivity(getApplicationContext()).run();
        }else if(requestCode == SAVE_PHOTO_CODE && resultCode == RESULT_OK){
            Log.d("CameraDemo", "Pic saved");


            Bitmap  mBitmap = null;
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outputFileUri2);
            } catch (IOException e) {
                e.printStackTrace();
            }

//
//            picture.setImageBitmap(mBitmap);
//            capture.setText("Recapture");
//
//            capture.setText("Recapture");

        }else{
            Log.d("CameraDemo", "Pic not saved");

        }
    }
    public void startCameraView(){
        final long time = System.currentTimeMillis();
        SharedPreferences mySPrefs =PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        final String tge_id = mySPrefs.getString("tge_id","");


        // Here, we are making a folder named Visitation Pictures to store
        // pics taken by the camera using this application.

        long startTime = System.currentTimeMillis();
        String theDate = getTime(startTime);
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Visitation Pictures/" + theDate + "/";


        String file = dir+tge_id+"_"+time+".jpg";
        File newfile = new File(file);
        try {
            newfile.createNewFile();
        }
        catch (IOException e)
        {
        }




        outputFileUri = Uri.fromFile(newfile);



        // Toast.makeText(getApplicationContext(),outputFileUri.toString(),Toast.LENGTH_LONG).show();

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);

    }
    public static String getTime(long mlls){
        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(mlls);
        String todayDate = new SimpleDateFormat("yyyy-MM-dd").format(cl.getTime());
        return todayDate;

    }



    public String resizeImage(Uri myUri){

        String filePath = myUri.getPath();
        Bitmap scaledBitmap = null;


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(myUri.getPath(), options);

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        float imgRatio = (float) actualWidth / (float) actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(myUri.getPath(), options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.RGB_565);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        if(bmp!=null)
        {
            bmp.recycle();
        }

        ExifInterface exif;
        try {
            exif = new ExifInterface(myUri.getPath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        String filepath = myUri.getPath();
        try {
            out = new FileOutputStream(filepath);

            //write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 60, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return myUri.getPath();
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
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
