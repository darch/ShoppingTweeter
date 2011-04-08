package net.darchangel.shoppingTweeter;

import net.darchangel.shoppingTweeter.exception.NoInputException;
import net.darchangel.shoppingTweeter.exception.tooLongException;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.main);

		setProgressBarIndeterminateVisibility(false);

		item = (EditText) findViewById(R.id.item);
		expense = (EditText) findViewById(R.id.expense);
		comment = (EditText) findViewById(R.id.comment);
		category = (Spinner) findViewById(R.id.category);
		creditcard = (CheckBox) findViewById(R.id.creditcard);
		secret = (CheckBox) findViewById(R.id.secret);
		tweet = (Button) findViewById(R.id.tweet);
		reset = (Button) findViewById(R.id.reset);

		// ボタンのコールバックリスナーを登録
		setAction();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (Pref.getDefaultSecret(ShoppingTweeter.this)) {
			// secretデフォルト有効の場合

			// secretをチェック
			secret.setChecked(true);
		} else {
			// secretデフォルト無効の場合

			// secretを未チェック
			secret.setChecked(false);
		}

		String auth_status = Pref.getStatus(this);
		if (auth_status.equals("")) {
			// ログインしてない場合

			// Tweetボタンを非活性化
			tweet.setEnabled(false);

			// ログインするようメッセージを表示
			Toast.makeText(ShoppingTweeter.this, R.string.please_login,
					Toast.LENGTH_LONG).show();

		} else {
			// ログイン済みの場合

			// Tweetボタンを活性化
			tweet.setEnabled(true);
		}
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
			Intent intent = new Intent(this, (Class<?>) Pref.class);
			startActivity(intent);
			return false;
		}
		return true;
	}

	/**
	 * 動作を登録
	 */
	private void setAction() {
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
					try {
						// AccessTokenの取得
						AccessToken accessToken = new AccessToken(Pref
								.getOauthToken(ShoppingTweeter.this), Pref
								.getOauthTokenSecret(ShoppingTweeter.this));

						// Consumer keyとConsumer secretを設定
						ConfigurationBuilder confbuilder = new ConfigurationBuilder();
						confbuilder
								.setOAuthConsumerKey(getString(R.string.consumer_key));
						confbuilder
								.setOAuthConsumerSecret(getString(R.string.consumer_secret));

						Twitter twitter = new TwitterFactory(confbuilder
								.build()).getInstance(accessToken);
						twitter.updateStatus(tweet_str);

						// Tweet成功のメッセージを表示
						Toast.makeText(ShoppingTweeter.this,
								R.string.tweet_success, Toast.LENGTH_LONG)
								.show();

						// フォームをリセット
						clearForm();
					} catch (TwitterException e) {

						// Tweet失敗のメッセージを表示
						Toast.makeText(ShoppingTweeter.this,
								R.string.tweet_fail, Toast.LENGTH_SHORT).show();

						e.printStackTrace();
					}
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
			checkNumber(expense, getString(R.string.expense));

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
		if (Pref.useCurrencyMark(ShoppingTweeter.this)) {
			tweet_str += Pref.getCurrencyMark(ShoppingTweeter.this);
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
	 * EditTextに入力された文字が正の数であることをチェックする
	 * 
	 * @param input
	 * @param name
	 * @throws NumberFormatException
	 */
	private void checkNumber(EditText input, String name)
			throws NumberFormatException {

		// EditTextに入力されている文字を数値に変換
		double input_num = Double.parseDouble(input.getText().toString());

		if (input_num < 0) {
			// 入力されている数値が0未満の場合

			throw new NumberFormatException();
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