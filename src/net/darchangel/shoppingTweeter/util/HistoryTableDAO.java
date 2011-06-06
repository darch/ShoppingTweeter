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

	// ソート順
	public static final String[] SORT_BY = { "ASC", "DESC" };
	public static final int SORT_BY_ASC = 0;
	public static final int SORT_BYA_DESC = 1;

	private SQLiteDatabase db = null;
	private Cursor cursor = null;

	public HistoryTableDAO(SQLiteDatabase db) {
		this.db = db;
	}

	/**
	 * 履歴テーブルにデータを登録/更新
	 * 
	 * @param item
	 *            登録/更新するデータ
	 */
	public void add(ShoppingItem item) {
		int ret = update(item.getValue(), item.getItemName());
		if (ret == 0) {
			add(item.getValue());
		}
	}

	/**
	 * 履歴テーブルに登録
	 * 
	 * @param value
	 *            登録するデータ
	 */
	private void add(ContentValues value) {
		db.insert(TABLE_SHOPPING_HISTORY, null, value);
	}

	/**
	 * 履歴テーブルを更新
	 * 
	 * @param value
	 *            登録するデータ
	 * @param item_name
	 *            更新キー(品名)
	 * @return 更新件数
	 */
	private int update(ContentValues value, String item_name) {
		return db.update(TABLE_SHOPPING_HISTORY, value, COLUMNS[COLUMN_ITEM_NAME] + " = ?", new String[] { item_name });
	}

	/**
	 * 履歴テーブルから全レコードを取得
	 * 
	 * @return 全レコード
	 */
	public List<ShoppingItem> selectAll(String orderBy) {
		ArrayList<ShoppingItem> result = new ArrayList<ShoppingItem>();

		if (db != null) {
			// データベースオブジェクトがnullではない場合

			// 取得列を設定
			String[] select = new String[] { COLUMNS[COLUMN_ITEM_NAME], COLUMNS[COLUMN_EXPENSE],
					COLUMNS[COLUMN_CATEGORY], COLUMNS[COLUMN_COMMENT], COLUMNS[COLUMN_CREDITCARD],
					COLUMNS[COLUMN_SECRET], COLUMNS[COLUMN_TWEET_DATE] };

			// 履歴テーブルから全レコードを取得
			cursor = db.query(TABLE_SHOPPING_HISTORY, select, null, null, null, null, orderBy);

			// 取得件数を取得
			int rowNum = cursor.getCount();

			// カーソルを一番最初に移動
			cursor.moveToFirst();

			for (int i = 0; i < rowNum; i++) {
				// カーソル位置が最後ではない場合

				// 取得レコードをShoppingItem型に変換
				ShoppingItem item = new ShoppingItem(cursor.getString(COLUMN_ITEM_NAME), cursor.getInt(COLUMN_EXPENSE));
				item.setCategory(cursor.getString(COLUMN_CATEGORY));
				item.setComment(cursor.getString(COLUMN_COMMENT));
				item.setUseCreditCard(cursor.getInt(COLUMN_CREDITCARD));
				item.setSecret(cursor.getInt(COLUMN_SECRET));
				item.setTweetDate(cursor.getLong(COLUMN_TWEET_DATE));

				// レコードを結果リストに追加
				result.add(item);

				// カーソルを次に移動
				cursor.moveToNext();
			}
		}

		closeCursor();

		return result;
	}

	/**
	 * 履歴テーブルに登録されているデータ件数を取得
	 * 
	 * @return 履歴テーブルの登録件数
	 */
	public int getRecordCount() {
		int rowNum = 0;

		if (db != null) {
			// 件数を取得
			cursor = db.query(TABLE_SHOPPING_HISTORY, null, null, null, null, null, null);

			rowNum = cursor.getCount();
		}

		closeCursor();

		return rowNum;
	}

	public void deleteRecord(int maxRowNum) {
		List<ShoppingItem> items = selectAll(null);

		while (items.size() > maxRowNum) {
			// 登録件数が最大登録件数を超えている場合
			ShoppingItem item = items.get(0);
			db.delete(TABLE_SHOPPING_HISTORY, COLUMNS[COLUMN_TWEET_DATE] + " = ?",
					new String[] { Long.toString(item.getTweetDate()) });
			items.remove(item);
		}
	}

	private void closeCursor() {
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
	}
}
