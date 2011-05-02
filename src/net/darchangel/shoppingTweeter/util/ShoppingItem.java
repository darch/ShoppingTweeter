package net.darchangel.shoppingTweeter.util;

import java.io.Serializable;

import net.darchangel.shoppingTweeter.R;
import android.content.ContentValues;

public class ShoppingItem implements Serializable {

	private static final long serialVersionUID = -2742100531093262990L;

	// カラムデータ
	private ContentValues values;

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
		values = new ContentValues(HistoryTableDAO.COLUMNS.length);
		values.put(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_ITEM_NAME], item_name);
		values.put(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_EXPENSE], expense);
		values.put(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_CATEGORY], R.string.category_other);
		values.put(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_CREDITCARD], false);
		values.put(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_SECRET], false);
		values.put(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_TWEET_DATE], System.currentTimeMillis());
	}

	/**
	 * コンストラクタ<br>
	 * 商品名：指定値<br>
	 * 金額：指定値<br>
	 * カテゴリ：指定値<br>
	 * クレジットカード：指定値<br>
	 * シークレット：指定値
	 * 
	 * @param item_name
	 * @param expense
	 */
	public ShoppingItem(String item_name, int expense, String category, boolean creditcard, boolean secret,
			long tweet_date) {
		values = new ContentValues(HistoryTableDAO.COLUMNS.length);
		values.put(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_ITEM_NAME], item_name);
		values.put(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_EXPENSE], expense);
		values.put(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_CATEGORY], category);
		values.put(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_CREDITCARD], creditcard);
		values.put(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_SECRET], secret);
		values.put(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_TWEET_DATE], tweet_date);
	}

	public ContentValues getValue() {
		return values;
	}

	public String getItemName() {
		return values.getAsString(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_ITEM_NAME]);
	}

	public int getExpense() {
		return values.getAsInteger(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_EXPENSE]);
	}

	public String getCategory() {
		return values.getAsString(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_CATEGORY]);
	}

	public void setCategory(String category) {
		values.put(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_CATEGORY], category);
	}

	public String getComment() {
		return values.getAsString(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_COMMENT]);
	}

	public void setComment(String comment) {
		values.put(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_COMMENT], comment);
	}

	public boolean getUseCreditCard() {
		return values.getAsBoolean(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_CREDITCARD]);
	}

	public void setUseCreditCard(boolean useCreditCard) {
		values.put(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_CREDITCARD], useCreditCard);
	}

	public boolean getSecret() {
		return values.getAsBoolean(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_SECRET]);
	}

	public void setSecret(boolean isSecret) {
		values.put(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_SECRET], isSecret);
	}

	public long getTweetDate() {
		return values.getAsLong(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_TWEET_DATE]);
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_ITEM_NAME]);
		buf.append(":").append(values.get(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_ITEM_NAME])).append("\n")
				.append(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_EXPENSE]).append(":")
				.append(values.get(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_EXPENSE])).append("\n")
				.append(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_CATEGORY]).append(":")
				.append(values.get(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_CATEGORY])).append("\n")
				.append(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_CREDITCARD]).append(":")
				.append(values.get(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_CREDITCARD])).append("\n")
				.append(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_SECRET]).append(":")
				.append(values.get(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_SECRET])).append("\n")
				.append(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_TWEET_DATE]).append(":")
				.append(values.get(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_TWEET_DATE])).append("\n");

		return buf.toString();
	}
}
