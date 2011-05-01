package net.darchangel.shoppingTweeter.util;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class HistoryTableDAO {

	// テーブル名
	public static final String TABLE_SHOPPING_HISTORY = "shopping_history";

	// カラム名
	public static final String[] COLUMNS = { "item_name", "expense", "category", "comment", "creditcard", "secret",
			"tweet_date" };
	public static final int COLUMN_ITEM_NAME = 0;
	public static final int COLUMN_EXPENSE = 1;
	public static final int COLUMN_CATEGORY = 2;
	public static final int COLUMN_COMMENT = 3;
	public static final int COLUMN_CREDITCARD = 4;
	public static final int COLUMN_SECRET = 5;
	public static final int COLUMN_TWEET_DATE = 6;

	private SQLiteDatabase db = null;

	public HistoryTableDAO(SQLiteDatabase db) {
		this.db = db;
	}

	/**
	 * 履歴テーブルに登録
	 * 
	 * @param value
	 *            登録内容
	 */
	public void add(ContentValues value) {
		db.insert(TABLE_SHOPPING_HISTORY, null, value);
	}

	/**
	 * 履歴テーブルから全レコードを取得
	 * 
	 * @return 全レコード
	 */
	public List<ShoppingItem> selectAll() {
		ArrayList<ShoppingItem> result = new ArrayList<ShoppingItem>();
		Cursor cursor;

		if (db != null) {
			// データベースオブジェクトがnullではない場合

			// 取得列を設定
			String[] select = new String[] { COLUMNS[COLUMN_ITEM_NAME], COLUMNS[COLUMN_EXPENSE],
					COLUMNS[COLUMN_CATEGORY], COLUMNS[COLUMN_COMMENT], COLUMNS[COLUMN_CREDITCARD],
					COLUMNS[COLUMN_SECRET], COLUMNS[COLUMN_TWEET_DATE] };

			// 履歴テーブルから全レコードを取得
			cursor = db.query(TABLE_SHOPPING_HISTORY, select, null, null, null, null, COLUMNS[COLUMN_TWEET_DATE]);

			// 取得件数を取得
			int rowNum = cursor.getCount();

			// カーソルを一番最初に移動
			cursor.moveToFirst();

			for (int i = 0; i < rowNum; i++) {
				// カーソル位置が最後ではない場合

				// 取得レコードをShoppingItem型に変換
				ShoppingItem item = new ShoppingItem(cursor.getString(COLUMN_ITEM_NAME), cursor.getInt(COLUMN_EXPENSE));

				// レコードを結果リストに追加
				result.add(item);

				// カーソルを次に移動
				cursor.moveToNext();
			}
		}

		return result;
	}
}
