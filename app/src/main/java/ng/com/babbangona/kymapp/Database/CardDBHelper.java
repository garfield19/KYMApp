package ng.com.babbangona.kymapp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class CardDBHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "card_records.db";
    private static final int DATABASE_VERSION = 1;
    private static final String CARDS_TABLE_NAME = "`cards`";

    public CardDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public int getCardCount (String card) {
        List<String> myLists = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + CARDS_TABLE_NAME + " WHERE cards = '" + card +"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int total_count=cursor.getCount();
        return total_count;
    }
}
