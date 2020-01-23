package ng.com.babbangona.kymapp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class AccountDBHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "account.db";
    private static final int DATABASE_VERSION = 1;
    private static final String ACCOUNT_TABLE_NAME = "`account_details`";

    public AccountDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public String getAccountName (String ik_number) {
        List<String> myLists = new ArrayList<>();
        String selectQuery = "SELECT AccountName FROM " + ACCOUNT_TABLE_NAME + " WHERE IKNumber = '" + ik_number +"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0) return  cursor.getString(0);
        else return null;
    }

    public String getAccountNumber (String ik_number) {
        List<String> myLists = new ArrayList<>();
        String selectQuery = "SELECT AccountNumber FROM " + ACCOUNT_TABLE_NAME + " WHERE IKNumber = '" + ik_number +"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0) return  cursor.getString(0);
        else return null;
    }

    public String getBank (String ik_number) {
        List<String> myLists = new ArrayList<>();
        String selectQuery = "SELECT BankName FROM " + ACCOUNT_TABLE_NAME + " WHERE IKNumber = '" + ik_number +"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0) return  cursor.getString(0);
        else return null;
    }
}
