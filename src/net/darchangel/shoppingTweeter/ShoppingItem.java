package net.darchangel.shoppingTweeter;

import java.io.Serializable;
import java.util.Date;

public class ShoppingItem implements Serializable {

	private static final long serialVersionUID = -2742100531093262990L;

	// テーブル名
	public static final String TABLE_SHOPPING_HISTORY = "shopping_history";

	// カラム名
	public static final String COLUMN_ITEM_NAME = "item_name";
	public static final String COLUMN_EXPENSE = "expense";
	public static final String COLUMN_CATEGORY = "category";
	public static final String COLUMN_CREDITCARD = "creditcard";
	public static final String COLUMN_SECRET = "secret";
	public static final String COLUMN_TWEET_DATE = "tweet_date";

	// カラムデータ
	private String item_name;
	private int expense;
	private int category;
	private boolean useCreditCard;
	private boolean isSecret;
	private Date tweet_date;

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
		this.item_name = item_name;
		this.expense = expense;
		category = R.string.category_other;
		useCreditCard = false;
		isSecret = false;
		tweet_date = new Date();
	}

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public boolean isUseCreditCard() {
		return useCreditCard;
	}

	public void setUseCreditCard(boolean useCreditCard) {
		this.useCreditCard = useCreditCard;
	}

	public boolean isSecret() {
		return isSecret;
	}

	public void setSecret(boolean isSecret) {
		this.isSecret = isSecret;
	}

	public String getItem_name() {
		return item_name;
	}

	public int getExpense() {
		return expense;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer(COLUMN_ITEM_NAME);
		buf.append(":").append(item_name).append("\n").append(COLUMN_EXPENSE)
				.append(":").append(expense).append("\n")
				.append(COLUMN_CATEGORY).append(":").append(category)
				.append("\n").append(COLUMN_CREDITCARD).append(":")
				.append(useCreditCard).append("\n").append(COLUMN_SECRET)
				.append(":").append(isSecret).append("\n")
				.append(COLUMN_TWEET_DATE).append(":").append(tweet_date)
				.append("\n");

		return buf.toString();
	}
}
