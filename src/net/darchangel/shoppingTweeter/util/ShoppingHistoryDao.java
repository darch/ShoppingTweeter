package net.darchangel.shoppingTweeter.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ShoppingHistoryDao extends SQLiteOpenHelper {

	private static final String DB_NAME = "ShoppingTweeter";

	public ShoppingHistoryDao(Context context) {
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE shopping_history (item_name TEXT, "
				+ "expense INTEGER, category INTEGER, comment TEXT, "
				+ "creditcard INTEGER, secret INTEGER, tweet_date INTEGER);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
