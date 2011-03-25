package net.darchangel.shoppingTweeter;

import net.darchangel.shoppingTweeter.exception.NoInputException;
import net.darchangel.shoppingTweeter.exception.tooLongException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ShoppingTweeter extends Activity {

	private EditText item = null;
	private EditText expense = null;
	private EditText comment = null;
	private Spinner category = null;
	private CheckBox creditcard = null;
	private CheckBox secret = null;
	private Button tweet = null;
	private Button reset = null;

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
		// createCategoryList();

		// ボタンのコールバックリスナーを登録
		setButtonCallbackListener();
	}

	/**
	 * メニューボタンを押したときの動作
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	/**
	 * 設定画面の呼び出し
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_pref:
			Intent intent = new Intent(this, (Class<?>) Preference.class);
			startActivity(intent);
			return false;
		}
		return true;
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
					Toast.makeText(ShoppingTweeter.this, tweet_str,
							Toast.LENGTH_SHORT).show();
					// try {
					// Twitter twitter = new TwitterFactory().getInstance();
					// // TODO OAuth認証を実装
					// twitter.updateStatus(tweet_str);
					// } catch (TwitterException e) {
					//
					// }

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

		try {

			// 必須項目の入力チェック
			checkInput(item, getString(R.string.item));
			checkInput(expense, getString(R.string.expense));

			// expenseに数字以外が入力されていないかチェック
			Integer.parseInt(expense.getText().toString());

			// Tweet内容を生成
			tweet_str = makeTweet();

			// Tweet内容の文字数をチェック
			checkTweetLength(tweet_str);

		} catch (NoInputException e) {
			// 必須項目が入力されていなかった場合

			// メッセージを表示
			Toast.makeText(
					ShoppingTweeter.this,
					String.format(getString(R.string.necessary_msg), e
							.getName()), Toast.LENGTH_SHORT).show();

		} catch (NumberFormatException e) {
			// expenseに数字以外が入力されていた場合

			// メッセージを表示
			Toast.makeText(ShoppingTweeter.this,
					getString(R.string.only_number), Toast.LENGTH_SHORT).show();

		} catch (tooLongException e) {
			// 入力内容が規定文字数より長い場合

			// メッセージを表示
			Toast.makeText(
					ShoppingTweeter.this,
					String.format(getString(R.string.too_long_msg), e
							.getLength()), Toast.LENGTH_SHORT).show();

			// Tweet内容をクリア
			tweet_str = "";

		}
		return tweet_str;
	}

	/**
	 * Tweet内容を生成
	 */
	private String makeTweet() {
		String tweet_str = "";

		if (secret.isChecked()) {
			// secretがチェックされている場合はダイレクトメッセージ
			tweet_str += getString(R.string.check_secret) + " ";
		}

		tweet_str += item.getText().toString();
		tweet_str += " ";
		if (Preference.useCurrencyMark(ShoppingTweeter.this)) {
			tweet_str += Preference.getCurrencyMark(ShoppingTweeter.this);
		}
		tweet_str += expense.getText().toString();

		if (comment.getText().length() != 0) {
			// コメントが入力されている場合は、つぶやきに追加
			tweet_str += " " + comment.getText().toString();
		}

		if (!getString(R.string.category_other).equals(
				category.getSelectedItem().toString())) {
			// カテゴリがother以外の場合はハッシュタグとしてつぶやきに追加
			tweet_str += " #" + category.getSelectedItem().toString();
		}

		if (creditcard.isChecked()) {
			tweet_str += " " + getString(R.string.check_creditcard);
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

	/**
	 * EditTextに文字が入力されているかチェックする
	 * 
	 * @param input
	 * @param name
	 * @throws NoInputException
	 */
	private void checkInput(EditText input, String name)
			throws NoInputException {
		// EditTextに入力されている文字列を取得
		String str = input.getText().toString();

		if (str.trim().length() == 0) {
			// 文字列の末尾のスペースを除いた長さが0の場合

			throw new NoInputException(name);
		}
	}

	/**
	 * 文字列が規定文字数(140文字)を越えていないかチェックする
	 * 
	 * @param str
	 *            文字列
	 * @throws IllegalStateException
	 */
	private void checkTweetLength(String str) throws tooLongException {
		// 規定文字数を取得
		int tweet_length = Integer.parseInt(getString(R.string.tweet_length));

		if (str.length() > tweet_length) {
			// Tweet内容が規定文字数を超える場合

			throw new tooLongException(str.length());
		}
	}
}