package ng.com.babbangona.kymapp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

import ng.com.babbangona.kymapp.Data.POJO.Members;


public class DBHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "old_tg.db";
    private static final int DATABASE_VERSION = 1;
    private static final String OLDTGS_TABLE_NAME = "`2019oldtgs`";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public int getOldTGCount (String ik_number) {
        List<String> myLists = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + OLDTGS_TABLE_NAME + " WHERE IKNumber = '" + ik_number +"'";
//        DBHelper dbHelper = new DBHelper();
//        dbHelper.getReadableDatabase();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int total_count=cursor.getCount();
        return total_count;
    }
}
