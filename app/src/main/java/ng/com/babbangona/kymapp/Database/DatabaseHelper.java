package ng.com.babbangona.kymapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ng.com.babbangona.kymapp.Data.POJO.Capture;
import ng.com.babbangona.kymapp.Data.POJO.MemberApproved;
import ng.com.babbangona.kymapp.Data.POJO.Members;
import ng.com.babbangona.kymapp.Data.POJO.Verification;

public class DatabaseHelper  extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    public static final String DATABASE_NAME = "kym.db";
    private static final int DATABASE_VERSION = 4;

    private static final String DATABASE_ALTER_1 = "ALTER TABLE "
            + Verification.TABLE_NAME + " ADD COLUMN " + Verification.CARD + " string;";

    private static final String DATABASE_ALTER_2 = "ALTER TABLE "
            + Members.TABLE_NAME + " ADD COLUMN " + Members.PASS_DONE + " string;";

    private static final String DATABASE_ALTER_3 = "ALTER TABLE "
            + Members.TABLE_NAME + " ADD COLUMN " + Members.FAIL_PICTURE_DONE + " string;";




    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Members.CREATE_TABLE);
        db.execSQL(Capture.CREATE_TABLE);
        db.execSQL(Verification.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL(DATABASE_ALTER_1);
        }
        if (oldVersion < 3){
            db.execSQL(DATABASE_ALTER_2);
            db.execSQL(DATABASE_ALTER_3);
        }


    }

    public boolean addMembersData(String member_name, String member_id, String member_role, String picked, String template, String done , String ik_number, String p_done,String f, String old_member_id){
        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Members.MEMBER_NAME, member_name);
        contentValues.put(Members.COLUMN_TEMPLATE, template);
        contentValues.put(Members.COLUMN_MEMBER_ID, member_id);
        contentValues.put(Members.IK_NUMBER, ik_number);
        contentValues.put(Members.ROLE, member_role);
        contentValues.put(Members.PICKER, picked);
        contentValues.put(Members.DONE, done);
        contentValues.put(Members.PASS_DONE, p_done);
        contentValues.put(Members.FAIL_PICTURE_DONE, f);
        contentValues.put(Members.PC_APPROVED, "0");
        contentValues.put(Members.MEMBER_ID, old_member_id);

        Log.d("Insert Members","adding members data");

        long result = db2.insertOrThrow(Members.TABLE_NAME,null,contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }
    }


    public List<Members> getTrustGroups() {
        List<Members> myLists = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT ik_number FROM " + Members.TABLE_NAME + " ORDER BY " +
                Members.COLUMN_ID + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Members list = new Members();
                list.setIkNumber(cursor.getString(cursor.getColumnIndex(Members.IK_NUMBER)));
                myLists.add(list);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return myLists;
    }


    public List<Members> getTrustGroupMembers(String ik_number) {
        List<Members> myLists = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + Members.TABLE_NAME +" WHERE " + Members.IK_NUMBER + " ='" +
                ik_number +"';";


//        String selectQuery = "SELECT * FROM " + Members.TABLE_NAME +" WHERE " + Members.IK_NUMBER + " ='" +
//                ik_number + " GROUP BY " + Members.COLUMN_MEMBER_ID + "';";



        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Members list = new Members();
                list.setMemberName(cursor.getString(cursor.getColumnIndex(Members.MEMBER_NAME)));
                list.setMemberRole(cursor.getString(cursor.getColumnIndex(Members.ROLE)));
                list.setPicker(cursor.getString(cursor.getColumnIndex(Members.PICKER)));
                list.setUniqueMemberId(cursor.getString(cursor.getColumnIndex(Members.COLUMN_MEMBER_ID)));
                list.setDone(cursor.getString(cursor.getColumnIndex(Members.DONE)));
                list.setIkNumber(cursor.getString(cursor.getColumnIndex(Members.IK_NUMBER)));
                list.setMember_id(cursor.getString(cursor.getColumnIndex(Members.MEMBER_ID)));
                list.setFail_picture_done(cursor.getString(cursor.getColumnIndex(Members.FAIL_PICTURE_DONE)));
                list.setPass_done(cursor.getString(cursor.getColumnIndex(Members.PASS_DONE)));
                list.setApproved(cursor.getString(cursor.getColumnIndex(Members.PC_APPROVED)));
                myLists.add(list);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return myLists;
    }

    public String getTemplate(String member_id){
        String Query = "SELECT "+ Members.COLUMN_TEMPLATE + " FROM " +  Members.TABLE_NAME +" WHERE " + Members.COLUMN_MEMBER_ID + " ='" +
                member_id +"';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0) return  cursor.getString(0);
        else return null;
    }



    public String getPassStatus(String member_id){
        String Query = "SELECT "+ Verification.PASS_FLAG + " FROM " +  Verification.TABLE_NAME +" WHERE " + Verification.COLUMN_MEMBER_ID + " ='" +
                member_id +"';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0) return  cursor.getString(0);
        else return "0";
    }

    public String getMemberPassStatus(String member_id){
        String Query = "SELECT "+ Members.PASS_DONE + " FROM " +  Members.TABLE_NAME +" WHERE " + Members.COLUMN_MEMBER_ID + " ='" +
                member_id +"';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0) return  cursor.getString(0);
        else return "0";
    }

    public String getApproveStatus(String member_id){
        String Query = "SELECT "+ Members.PC_APPROVED + " FROM " +  Members.TABLE_NAME +" WHERE " + Members.COLUMN_MEMBER_ID + " ='" +
                member_id +"';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0) return  cursor.getString(0);
        else return null;
    }

    public String getGivenCard(String card_number){
        String Query = "SELECT "+ Verification.IK_NUMBER + " FROM " +  Verification.TABLE_NAME +" WHERE " + Verification.BG_CARD + " ='" +
                card_number +"';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0) return  cursor.getString(0);
        else return "";
    }

    public String getCardTime(String card_number){
        String Query = "SELECT "+ Verification.TIME + " FROM " +  Verification.TABLE_NAME +" WHERE " + Verification.BG_CARD + " ='" +
                card_number +"';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0) return  cursor.getString(0);
        else return "";
    }

    public String getCapturedTemplate(String member_id){
        String Query = "SELECT "+ Capture.COLUMN_TEMPLATE + " FROM " +  Capture.TABLE_NAME +" WHERE " + Capture.COLUMN_MEMBER_ID + " ='" +
                member_id +"';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0) return  cursor.getString(0);
        else return null;
    }

    public String getIK(String member_id){
        String Query = "SELECT "+ Members.IK_NUMBER + " FROM " +  Members.TABLE_NAME +" WHERE " + Members.COLUMN_MEMBER_ID + " ='" +
                member_id +"';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0) return  cursor.getString(0);
        else return null;
    }

    public String getApproval(String member_id){
        String Query = "SELECT "+ Members.PC_APPROVED + " FROM " +  Members.TABLE_NAME +" WHERE " + Members.COLUMN_MEMBER_ID + " ='" +
                member_id +"';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0) return  cursor.getString(0);
        else return "";
    }

    public String getPass(String member_id){
        String Query = "SELECT "+ Members.PASS_DONE + " FROM " +  Members.TABLE_NAME +" WHERE " + Members.COLUMN_MEMBER_ID + " ='" +
                member_id +"';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0) return  cursor.getString(0);
        else return "";
    }

    public String getTries(String member_id){
        String Query = "SELECT "+ Verification.TRIES + " FROM " +  Verification.TABLE_NAME +" WHERE " + Verification.COLUMN_MEMBER_ID + " ='" +
                member_id +"';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0) return  cursor.getString(0);
        else return null;
    }

    public String getMemberExists(String member_id){
        String Query = "SELECT "+ Members.COLUMN_MEMBER_ID + " FROM " +  Members.TABLE_NAME +" WHERE " + Members.COLUMN_MEMBER_ID + " ='" +
                member_id +"';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0) return  cursor.getString(0);
        else return null;
    }

    public String getPicker(String member_id){
        String Query = "SELECT "+ Members.PICKER + " FROM " +  Members.TABLE_NAME +" WHERE " + Members.COLUMN_MEMBER_ID + " ='" +
                member_id +"';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0) return  cursor.getString(0);
        else return null;
    }

    public String getCaptureEntry(String member_id){
        String Query = "SELECT "+ Capture.COLUMN_ID + " FROM " +  Capture.TABLE_NAME +" WHERE " + Capture.COLUMN_MEMBER_ID + " ='" +
                member_id +"';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0) return  cursor.getString(0);
        else return null;
    }

    public boolean addFailed(String user_id, String template,  String time, String type){
        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Capture.COLUMN_MEMBER_ID, user_id);
        contentValues.put(Capture.TIME, time);
        contentValues.put(Capture.COLUMN_TEMPLATE, template);
        contentValues.put(Capture.COLUMN_TYPE, type);

        contentValues.put(Capture.SYNC, "0");

         Log.d("TAG","adding failed user data");

        long result = db2.insertOrThrow(Capture.TABLE_NAME,null,contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }
    }
    public boolean addVerification(String user_id, String pass_flag,  String tries, String ik_number, String picture, String time){
        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Verification.COLUMN_MEMBER_ID, user_id);
        contentValues.put(Verification.PASS_FLAG, pass_flag);
        contentValues.put(Verification.TRIES, tries);
        contentValues.put(Verification.COLUMN_MEMBER_ID, user_id);
        contentValues.put(Verification.IK_NUMBER, ik_number);
        contentValues.put(Verification.TIME, time);
        contentValues.put(Verification.SYNC, "0");

         Log.d("TAG","adding verification user data");

        long result = db2.insertOrThrow(Verification.TABLE_NAME,null,contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean replaceStatus(String staff_id) {
        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Members.DONE, "1");

        long result = db2.update(Members.TABLE_NAME,contentValues, Members.COLUMN_MEMBER_ID + "=?", new String[]{staff_id});
        // db.delete("tablename","id=? and name=?",new String[]{"1","jack"});
        //     db2.execSQL("UPDATE MIKRecordsDB.TABLE_NAME SET MIKRecordsDB.COLUMN_TEMPLATE='template' WHERE id=6 ");

        if(result == -1){
            return false;
        }else{
            return true;
        }

    }

    public boolean replaceOldCollectionType(String staff_id, String type) {
        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //contentValues.put(Verification.OLD_COLLECTION_TYPE, type);

        long result = db2.update(Verification.TABLE_NAME,contentValues, Verification.COLUMN_MEMBER_ID + "=?", new String[]{staff_id});
        // db.delete("tablename","id=? and name=?",new String[]{"1","jack"});
        //     db2.execSQL("UPDATE MIKRecordsDB.TABLE_NAME SET MIKRecordsDB.COLUMN_TEMPLATE='template' WHERE id=6 ");

        if(result == -1){
            return false;
        }else{
            return true;
        }

    }

    public boolean replacePassStatus(String staff_id) {
        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Members.PASS_DONE, "1");

        long result = db2.update(Members.TABLE_NAME,contentValues, Members.COLUMN_MEMBER_ID + "=?", new String[]{staff_id});
        // db.delete("tablename","id=? and name=?",new String[]{"1","jack"});
        //     db2.execSQL("UPDATE MIKRecordsDB.TABLE_NAME SET MIKRecordsDB.COLUMN_TEMPLATE='template' WHERE id=6 ");

        if(result == -1){
            return false;
        }else{
            return true;
        }

    }

    public boolean replacePassStatusVariable(String staff_id, String value) {
        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Members.PASS_DONE, value);

        long result = db2.update(Members.TABLE_NAME,contentValues, Members.COLUMN_MEMBER_ID + "=?", new String[]{staff_id});
        // db.delete("tablename","id=? and name=?",new String[]{"1","jack"});
        //     db2.execSQL("UPDATE MIKRecordsDB.TABLE_NAME SET MIKRecordsDB.COLUMN_TEMPLATE='template' WHERE id=6 ");

        if(result == -1){
            return false;
        }else{
            return true;
        }

    }


    public boolean defaultDone(String staff_id) {
        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Members.DONE, "0");

        long result = db2.update(Members.TABLE_NAME,contentValues, Members.COLUMN_MEMBER_ID + "=?", new String[]{staff_id});
        // db.delete("tablename","id=? and name=?",new String[]{"1","jack"});
        //     db2.execSQL("UPDATE MIKRecordsDB.TABLE_NAME SET MIKRecordsDB.COLUMN_TEMPLATE='template' WHERE id=6 ");

        if(result == -1){
            return false;
        }else{
            return true;
        }

    }

    public boolean defaultPass(String staff_id) {
        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Members.PASS_DONE, "0");

        long result = db2.update(Members.TABLE_NAME,contentValues, Members.COLUMN_MEMBER_ID + "=?", new String[]{staff_id});
        // db.delete("tablename","id=? and name=?",new String[]{"1","jack"});
        //     db2.execSQL("UPDATE MIKRecordsDB.TABLE_NAME SET MIKRecordsDB.COLUMN_TEMPLATE='template' WHERE id=6 ");

        if(result == -1){
            return false;
        }else{
            return true;
        }

    }

    public boolean replaceFailPictureStatus(String staff_id) {
        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Members.FAIL_PICTURE_DONE, "1");

        long result = db2.update(Members.TABLE_NAME,contentValues, Members.COLUMN_MEMBER_ID + "=?", new String[]{staff_id});
        // db.delete("tablename","id=? and name=?",new String[]{"1","jack"});
        //     db2.execSQL("UPDATE MIKRecordsDB.TABLE_NAME SET MIKRecordsDB.COLUMN_TEMPLATE='template' WHERE id=6 ");

        if(result == -1){
            return false;
        }else{
            return true;
        }

    }


    public boolean replaceCard(String staff_id, String card) {
        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Verification.CARD, card);

        long result = db2.update(Verification.TABLE_NAME,contentValues, Verification.COLUMN_MEMBER_ID + "=?", new String[]{staff_id});
        // db.delete("tablename","id=? and name=?",new String[]{"1","jack"});
        //     db2.execSQL("UPDATE MIKRecordsDB.TABLE_NAME SET MIKRecordsDB.COLUMN_TEMPLATE='template' WHERE id=6 ");

        if(result == -1){
            return false;
        }else{
            return true;
        }

    }


    public boolean replaceAccountUpdate(String member_id, String state) {
        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Verification.ACCOUNT_UPDATE, state);

        long result = db2.update(Verification.TABLE_NAME,contentValues, Verification.COLUMN_MEMBER_ID + "=?", new String[]{member_id});
        // db.delete("tablename","id=? and name=?",new String[]{"1","jack"});
        //     db2.execSQL("UPDATE MIKRecordsDB.TABLE_NAME SET MIKRecordsDB.COLUMN_TEMPLATE='template' WHERE id=6 ");

        if(result == -1){
            return false;
        }else{
            return true;
        }

    }

    public boolean replaceApproved(String member_id) {
        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Members.PC_APPROVED, "1");

        long result = db2.update(Members.TABLE_NAME,contentValues, Members.COLUMN_MEMBER_ID + "=?", new String[]{member_id});
        // db.delete("tablename","id=? and name=?",new String[]{"1","jack"});
        //     db2.execSQL("UPDATE MIKRecordsDB.TABLE_NAME SET MIKRecordsDB.COLUMN_TEMPLATE='template' WHERE id=6 ");

        ContentValues contentValues2 = new ContentValues();
        contentValues2.put(Verification.PASS_FLAG, "1");
        long result2 = db2.update(Verification.TABLE_NAME,contentValues2, Verification.COLUMN_MEMBER_ID + "=?", new String[]{member_id});

        if(result == -1){
            return false;
        }else{
            return true;
        }

    }

    public boolean replaceCardFillingStatus(String member_id) {
        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Members.PC_APPROVED, "1");
        contentValues.put(Members.DONE, "0");
        contentValues.put(Members.PASS_DONE, "1");

       // long result = db2.update(Members.TABLE_NAME,contentValues, Members.COLUMN_MEMBER_ID + "=?", new String[]{member_id});
        // db.delete("tablename","id=? and name=?",new String[]{"1","jack"});
        //     db2.execSQL("UPDATE MIKRecordsDB.TABLE_NAME SET MIKRecordsDB.COLUMN_TEMPLATE='template' WHERE id=6 ");

        ContentValues contentValues2 = new ContentValues();
        contentValues2.put(Verification.COLUMN_MEMBER_ID, member_id);
        contentValues2.put(Verification.PASS_FLAG, "1");
        contentValues2.put(Verification.TRIES, "1");
        contentValues2.put(Verification.SYNC, "0");

        long result2 = db2.insertOrThrow(Verification.TABLE_NAME,null,contentValues2);

        if(result2 == -1){
            return false;
        }else{
            return true;
        }

    }


    public boolean replaceCardFillingStatusFailedPass(String member_id) {
        SQLiteDatabase db2 = this.getWritableDatabase();

        ContentValues contentValues2 = new ContentValues();
        contentValues2.put(Verification.COLUMN_MEMBER_ID, member_id);
        contentValues2.put(Verification.PASS_FLAG, "0");
        contentValues2.put(Verification.TRIES, "1");
        contentValues2.put(Verification.SYNC, "0");

        long result2 = db2.insertOrThrow(Verification.TABLE_NAME,null,contentValues2);

        if(result2 == -1){
            return false;
        }else{
            return true;
        }

    }

    public boolean replaceSync(String member_id) {
        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Verification.SYNC, "0");

        long result = db2.update(Verification.TABLE_NAME,contentValues, Verification.COLUMN_MEMBER_ID + "=?", new String[]{member_id});
        // db.delete("tablename","id=? and name=?",new String[]{"1","jack"});
        //     db2.execSQL("UPDATE MIKRecordsDB.TABLE_NAME SET MIKRecordsDB.COLUMN_TEMPLATE='template' WHERE id=6 ");

        if(result == -1){
            return false;
        }else{
            return true;
        }

    }

    public boolean updateTries(String staff_id, String tries) {
        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Verification.TRIES, tries);

        long result = db2.update(Verification.TABLE_NAME,contentValues, Verification.COLUMN_MEMBER_ID + "=?", new String[]{staff_id});
        // db.delete("tablename","id=? and name=?",new String[]{"1","jack"});
        //     db2.execSQL("UPDATE MIKRecordsDB.TABLE_NAME SET MIKRecordsDB.COLUMN_TEMPLATE='template' WHERE id=6 ");

        if(result == -1){
            return false;
        }else{
            return true;
        }

    }


    public boolean updateTemplate(String staff_id, String template, String type) {
        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Capture.COLUMN_TEMPLATE, template);
        contentValues.put(Capture.COLUMN_TYPE, type);

        long result = db2.update(Capture.TABLE_NAME,contentValues, Capture.COLUMN_MEMBER_ID + "=?", new String[]{staff_id});
        // db.delete("tablename","id=? and name=?",new String[]{"1","jack"});
        //     db2.execSQL("UPDATE MIKRecordsDB.TABLE_NAME SET MIKRecordsDB.COLUMN_TEMPLATE='template' WHERE id=6 ");

        if(result == -1){
            return false;
        }else{
            return true;
        }

    }

    public boolean updateMemberTemplate(String staff_id, String template) {
        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Members.COLUMN_TEMPLATE, template);


        long result = db2.update(Members.TABLE_NAME,contentValues, Members.COLUMN_MEMBER_ID + "=?", new String[]{staff_id});
        // db.delete("tablename","id=? and name=?",new String[]{"1","jack"});
        //     db2.execSQL("UPDATE MIKRecordsDB.TABLE_NAME SET MIKRecordsDB.COLUMN_TEMPLATE='template' WHERE id=6 ");

        if(result == -1){
            return false;
        }else{
            return true;
        }

    }

    public boolean updatePass(String staff_id, String pass) {
        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Verification.PASS_FLAG, pass);

        long result = db2.update(Verification.TABLE_NAME,contentValues, Verification.COLUMN_MEMBER_ID + "=?", new String[]{staff_id});
        // db.delete("tablename","id=? and name=?",new String[]{"1","jack"});
        //     db2.execSQL("UPDATE MIKRecordsDB.TABLE_NAME SET MIKRecordsDB.COLUMN_TEMPLATE='template' WHERE id=6 ");

        if(result == -1){
            return false;
        }else{
            return true;
        }

    }

    public boolean updateImage(String staff_id) {
        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Verification.PICTURE_TAKEN, "1");

        long result = db2.update(Verification.TABLE_NAME,contentValues, Verification.COLUMN_MEMBER_ID + "=?", new String[]{staff_id});
        // db.delete("tablename","id=? and name=?",new String[]{"1","jack"});
        //     db2.execSQL("UPDATE MIKRecordsDB.TABLE_NAME SET MIKRecordsDB.COLUMN_TEMPLATE='template' WHERE id=6 ");

        if(result == -1){
            return false;
        }else{
            return true;
        }

    }
    public boolean updateBGCard(String staff_id, String bg_number) {
        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Verification.BG_CARD, bg_number);

        long result = db2.update(Verification.TABLE_NAME,contentValues, Verification.COLUMN_MEMBER_ID + "=?", new String[]{staff_id});
        // db.delete("tablename","id=? and name=?",new String[]{"1","jack"});
        //     db2.execSQL("UPDATE MIKRecordsDB.TABLE_NAME SET MIKRecordsDB.COLUMN_TEMPLATE='template' WHERE id=6 ");

        if(result == -1){
            return false;
        }else{
            return true;
        }

    }



    public boolean updateMemberBGCard(String staff_id, String bg_number) {
        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Members.CARD, bg_number);

        long result = db2.update(Members.TABLE_NAME,contentValues, Members.COLUMN_MEMBER_ID + "=?", new String[]{staff_id});
        // db.delete("tablename","id=? and name=?",new String[]{"1","jack"});
        //     db2.execSQL("UPDATE MIKRecordsDB.TABLE_NAME SET MIKRecordsDB.COLUMN_TEMPLATE='template' WHERE id=6 ");

        if(result == -1){
            return false;
        }else{
            return true;
        }

    }

    public boolean updateWrongCard(String staff_id, String state) {
        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Verification.WRONG_CARD, state);

        long result = db2.update(Verification.TABLE_NAME,contentValues, Verification.COLUMN_MEMBER_ID + "=?", new String[]{staff_id});
        // db.delete("tablename","id=? and name=?",new String[]{"1","jack"});
        //     db2.execSQL("UPDATE MIKRecordsDB.TABLE_NAME SET MIKRecordsDB.COLUMN_TEMPLATE='template' WHERE id=6 ");

        if(result == -1){
            return false;
        }else{
            return true;
        }

    }

    public int getTGCount (String ik_number) {
        List<String> myLists = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Members.TABLE_NAME +" WHERE " + Members.IK_NUMBER + " ='" +
                ik_number +"';";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int total_count=cursor.getCount();
        return total_count;
    }



    public int bgCardNoPass (String member_id) {
        List<String> myLists = new ArrayList<>();
        String done = "";
        String selectQuery = "SELECT * FROM " + Members.TABLE_NAME +" WHERE " + Members.CARD + " <>'" +
                done +"'" + "AND " + Members.COLUMN_MEMBER_ID + " ='" +
                member_id +"';";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int total_count=cursor.getCount();
        return total_count;
    }



    public int getDoneStatus(String member_id){
        String done = "1";
        String selectQuery = "SELECT * FROM " + Members.TABLE_NAME +" WHERE " + Members.DONE + " ='" +
                done +"'" + "AND " + Members.COLUMN_MEMBER_ID + " ='" +
                member_id +"';";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int total_count=cursor.getCount();
        return total_count;
    }


    public int getBGCard (String member_id) {
        List<String> myLists = new ArrayList<>();
        String done = "";
        String selectQuery = "SELECT * FROM " + Members.TABLE_NAME +" WHERE " + Members.CARD + " <>'" +
                done +"'" + "AND " + Members.COLUMN_MEMBER_ID + " ='" +
                member_id +"';";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int total_count=cursor.getCount();
        return total_count;
    }

    public int getDoneTGSum (String ik_number) {
        List<String> myLists = new ArrayList<>();
        String done = "1";
        String selectQuery = "SELECT * FROM " + Members.TABLE_NAME +" WHERE " + Members.DONE + " ='" +
                done +"'" + "AND " + Members.IK_NUMBER + " ='" +
                ik_number +"';";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int total_count=cursor.getCount();
        return total_count;
    }
    public boolean updateCaptureFlag(String member_id) {
        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Capture.SYNC, "1");

        long result = db2.update(Capture.TABLE_NAME,contentValues, Capture.COLUMN_MEMBER_ID + "=?", new String[]{member_id});

        if(result == -1){
            return false;
        }else{
            return true;
        }
    }

    public boolean updateVerificationFlag(String member_id) {
        SQLiteDatabase db2 = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Verification.SYNC, "1");

        long result = db2.update(Verification.TABLE_NAME,contentValues, Verification.COLUMN_MEMBER_ID + "=?", new String[]{member_id});

        if(result == -1){
            return false;
        }else{
            return true;
        }
    }



}
