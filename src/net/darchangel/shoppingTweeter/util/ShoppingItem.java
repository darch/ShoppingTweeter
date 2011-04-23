package net.darchangel.shoppingTweeter.util;

import java.io.Serializable;

import net.darchangel.shoppingTweeter.R;
import android.content.ContentValues;

public class ShoppingItem implements Serializable {

	private static final long serialVersionUID = -2742100531093262990L;

	// テーブル名
	public static final String TABLE_SHOPPING_HISTORY = "shopping_history";

	// カラム名
	private static final int CLUMN_NUM = 7;
	public static final String COLUMN_ITEM_NAME = "item_name";
	public static final String COLUMN_EXPENSE = "expense";
	public static final String COLUMN_CATEGORY = "category";
	public static final String COLUMN_COMMENT = "comment";
	public static final String COLUMN_CREDITCARD = "creditcard";
	public static final String COLUMN_SECRET = "secret";
	public static final String COLUMN_TWEET_DATE = "tweet_date";

	// カラムデータ
	ContentValues values;

	/**
	 * コンストラクタ<br>
	 * 商品名：指定値<br>
	 * 金額：指定値<br>
	 * カテゴリ：その他<br>
	 * クレジットカード：未使用<br>
	 * シークレット：未使用
	 * 
	 * @param item_name
	 * @param expense
	 */
	public ShoppingItem(String item_name, int expense) {
		values = new ContentValues(CLUMN_NUM);
		values.put(COLUMN_ITEM_NAME, item_name);
		values.put(COLUMN_EXPENSE, expense);
		values.put(COLUMN_CATEGORY, R.string.category_other);
		values.put(COLUMN_CREDITCARD, false);
		values.put(COLUMN_SECRET, false);
		values.put(COLUMN_TWEET_DATE, System.currentTimeMillis());
	}

	public void setCategory(int category) {
		values.put(COLUMN_CATEGORY, category);
	}

	public void setComment(String comment) {
		values.put(COLUMN_COMMENT, comment);
	}

	public void setUseCreditCard(boolean useCreditCard) {
		values.put(COLUMN_CREDITCARD, useCreditCard);
	}

	public void setSecret(boolean isSecret) {
		values.put(COLUMN_SECRET, isSecret);
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer(COLUMN_ITEM_NAME);
		buf.append(":").append(values.get(COLUMN_ITEM_NAME)).append("\n")
				.append(COLUMN_EXPENSE).append(":").append(
						values.get(COLUMN_EXPENSE)).append("\n").append(
						COLUMN_CATEGORY).append(":").append(
						values.get(COLUMN_CATEGORY)).append("\n").append(
						COLUMN_CREDITCARD).append(":").append(
						values.get(COLUMN_CREDITCARD)).append("\n").append(
						COLUMN_SECRET).append(":").append(
						values.get(COLUMN_SECRET)).append("\n").append(
						COLUMN_TWEET_DATE).append(":").append(
						values.get(COLUMN_TWEET_DATE)).append("\n");

		return buf.toString();
	}
}
