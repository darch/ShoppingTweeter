package net.darchangel.shoppingTweeter;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class shoppingTweeter extends Activity {

	EditText item = null;
	EditText expense = null;
	EditText comment = null;
	Spinner category = null;
	CheckBox creditcard = null;
	CheckBox secret = null;
	Button tweet = null;
	Button reset = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		item = (EditText) findViewById(R.id.item);
		expense = (EditText) findViewById(R.id.expense);
		comment = (EditText) findViewById(R.id.comment);
		category = (Spinner) findViewById(R.id.category);
		creditcard = (CheckBox) findViewById(R.id.creditcard);
		secret = (CheckBox) findViewById(R.id.secret);
		tweet = (Button) findViewById(R.id.tweet);
		reset = (Button) findViewById(R.id.reset);

		// カテゴリリストを生成
		createCategoryList();

		// ボタンのコールバックリスナーを登録
		setButtonCallbackListener();
	}

	/**
	 * カテゴリリストを生成
	 */
	private void createCategoryList() {

		// Categoryのリストを生成
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);
		adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// アイテムを追加
		adapter.add(getString(R.string.category_other));
		adapter.add(getString(R.string.category_food));
		adapter.add(getString(R.string.category_lunch));
		adapter.add(getString(R.string.category_dinner));
		adapter.add(getString(R.string.category_alcohol));
		adapter.add(getString(R.string.category_snack_cafe));
		adapter.add(getString(R.string.category_transport));
		adapter.add(getString(R.string.category_stationery_tool));
		adapter.add(getString(R.string.category_book));
		adapter.add(getString(R.string.category_fashion));
		adapter.add(getString(R.string.category_beauty_health));
		adapter.add(getString(R.string.category_hobby));
		adapter.add(getString(R.string.category_Household));
		adapter.add(getString(R.string.category_friendship));

		// アダプターを設定
		category.setAdapter(adapter);
	}

	/**
	 * ボタンのコールバックリスナーを登録
	 */
	private void setButtonCallbackListener() {
		// Tweetボタンがクリックされた時に呼び出されるコールバックリスナーを登録
		tweet.setOnClickListener(new View.OnClickListener() {
			/**
			 * ボタンがクリックされたときに呼び出し
			 */
			@Override
			public void onClick(View v) {
				String tweet_str = checkTweetable();
				if (tweet_str.length() != 0) {
					// Tweet可能な場合

					// Tweetする
					Toast.makeText(shoppingTweeter.this, tweet_str,
							Toast.LENGTH_SHORT).show();
					try {
						Twitter twitter = new TwitterFactory().getInstance();
						// TODO OAuth認証を実装
						twitter.updateStatus(tweet_str);
					} catch (TwitterException e) {

					}

					// フォームをリセット
					clearForm();
				}
			}
		});

		// Resetボタンがクリックされた時に呼び出されるコールバックリスナーを登録
		reset.setOnClickListener(new View.OnClickListener() {
			/**
			 * ボタンがクリックされたときに呼び出し
			 */
			@Override
			public void onClick(View v) {
				// フォームをリセット
				clearForm();
			}
		});
	}

	/**
	 * Tweet可能か確認<br />
	 * Tweetが可能な場合はTweet内容を、不可能な場合は空文字を返す
	 */
	private String checkTweetable() {
		String tweet_str = "";
		String necessary = "";

		if (item.getText().length() == 0) {
			// itemが入力されているかチェック

			necessary = getString(R.string.item);
		} else if (expense.getText().length() == 0) {
			// expenseが入力されているかチェック

			necessary = getString(R.string.expense);
		}

		if (necessary.length() == 0) {
			// 必須項目が入力されている場合

			// Tweet内容を生成
			tweet_str = makeTweet();

			if (tweet_str.length() > 140) {
				// Tweet内容が140文字を超える場合

				int tweet_length = tweet_str.length();
				// メッセージを表示
				Toast.makeText(
						shoppingTweeter.this,
						getString(R.string.too_long_msg) + "(" + tweet_length
								+ ")", Toast.LENGTH_SHORT).show();

				// Tweet内容をクリア
				tweet_str = "";
			}
		} else {
			// 必須項目が入力されていない場合

			// メッセージを表示
			Toast.makeText(shoppingTweeter.this,
					necessary + " " + getString(R.string.necessary_msg),
					Toast.LENGTH_SHORT).show();
		}

		return tweet_str;
	}

	/**
	 * Tweet内容を生成
	 */
	private String makeTweet() {
		String tweet_str = "";

		tweet_str = item.getText().toString();
		tweet_str += " " + expense.getText().toString();

		if (comment.getText().length() != 0) {
			// コメントが入力されている場合は、つぶやきに追加
			tweet_str += " " + comment.getText().toString();
		}

		if (!getString(R.string.category_other).equals(
				category.getSelectedItem().toString())) {
			// カテゴリがother以外の場合はハッシュタグとしてつぶやきに追加
			tweet_str += " #" + category.getSelectedItem().toString();
		}

		return tweet_str;
	}

	/**
	 * フォームをリセットする
	 */
	private void clearForm() {
		item.setText("");
		expense.setText("");
		comment.setText("");
		category.setSelection(0);
		creditcard.setChecked(false);
		secret.setChecked(false);
	}
}