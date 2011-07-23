package net.darchangel.shoppingTweeter.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ShoppingTweeterDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "ShoppingTweeter";

    public ShoppingTweeterDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + HistoryTableDAO.TABLE_SHOPPING_HISTORY + " (item_name TEXT, "
                + "expense INTEGER, category TEXT, comment TEXT, "
                + "creditcard INTEGER, secret INTEGER, tweet_date LONG);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // only version 1. so do nothing.

    }

}
