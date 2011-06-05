package net.darchangel.shoppingTweeter;

import net.darchangel.shoppingTweeter.util.HistoryTableDAO;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;

public class Pref extends PreferenceActivity {

	// リクエストコード
	private final int REQUEST_AUTH = 1;

	private Preference login;
	private Preference logout;

	private Twitter twitter = null;
	private RequestToken requestToken = null;

	// ログイン状態
	private String auth_status = "";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.pref);

		// ログイン状態を取得
		auth_status = Pref.getStatus(this);

		login = (Preference) findPreference(getString(R.string.pref_login_key));
		logout = (Preference) findPreference(getString(R.string.pref_logout_key));

		setAction();

	}

	@Override
	protected void onStart() {
		super.onStart();

		// ログイン状態を取得
		auth_status = Pref.getStatus(this);

		// ログイン状態を判定
		if (isConnected(auth_status)) {
			// ログイン済みの場合

			// Loginを非活性化し、Logoutを活性化
			login.setEnabled(false);
			logout.setEnabled(true);
		} else {
			// 未ログインの場合

			// Loginを活性化し、Logoutを非活性化
			login.setEnabled(true);
			logout.setEnabled(false);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

		if (resultCode == RESULT_OK) {
			super.onActivityResult(requestCode, resultCode, intent);

			AccessToken accessToken = null;

			try {
				// OAuthAccessTokenを取得
				accessToken = twitter.getOAuthAccessToken(requestToken,
						intent.getExtras().getString(getString(R.string.twitter_oauth_verifier)));

				SharedPreferences pref = getSharedPreferences(getString(R.string.twitter_prefs_key), MODE_PRIVATE);

				// SharedPreferenceにOAuthAccessTokenを記録
				SharedPreferences.Editor editor = pref.edit();
				editor.putString(getString(R.string.twitter_oauth_token_key), accessToken.getToken());
				editor.putString(getString(R.string.twitter_oauth_token_secret_key), accessToken.getTokenSecret());
				editor.putString(getString(R.string.twitter_connect_key), getString(R.string.status_connected));

				editor.commit();

			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 処理を設定
	 */
	private void setAction() {
		login.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				try {
					// ログイン処理を開始
					connectTwitter();
					return true;
				} catch (TwitterException e) {
					e.printStackTrace();
					return false;
				}
			}
		});

		logout.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				// ログアウト処理を開始
				disconnectTwitter();

				login.setEnabled(true);
				logout.setEnabled(false);

				return true;
			}
		});
	}

	/**
	 * ログイン済みか判定する
	 * 
	 * @param status
	 * @return
	 */
	private boolean isConnected(String status) {
		boolean result = false;

		if (status != null && status.equals(getString(R.string.status_connected))) {
			// statusがnullじゃなく、かつ"connected"の場合はログイン済み
			return true;
		}

		return result;
	}

	/**
	 * ログイン処理を行う
	 * 
	 * @throws TwitterException
	 */
	private void connectTwitter() throws TwitterException {

		// Consumer keyとConsumer secretを設定
		ConfigurationBuilder confbuilder = new ConfigurationBuilder();
		confbuilder.setOAuthConsumerKey(getString(R.string.consumer_key));
		confbuilder.setOAuthConsumerSecret(getString(R.string.consumer_secret));

		twitter = new TwitterFactory(confbuilder.build()).getInstance();

		// requestTokenの取得
		try {
			requestToken = twitter.getOAuthRequestToken(getString(R.string.callback_url));
		} catch (TwitterException e) {
			e.printStackTrace();
		}

		// 認証用URLをインテントにセット。
		// TwitterLoginはActivityのクラス名。
		Intent intent = new Intent(this, TwitterLogin.class);
		intent.putExtra(getString(R.string.auth_url), requestToken.getAuthorizationURL());

		// アクティビティを起動
		this.startActivityForResult(intent, REQUEST_AUTH);

	}

	/**
	 * ログアウト処理を行う
	 */
	private void disconnectTwitter() {

		SharedPreferences pref = getSharedPreferences(getString(R.string.twitter_prefs_key), MODE_PRIVATE);

		SharedPreferences.Editor editor = pref.edit();
		editor.remove(getString(R.string.twitter_oauth_token_key));
		editor.remove(getString(R.string.twitter_oauth_token_secret_key));
		editor.remove(getString(R.string.twitter_connect_key));

		editor.commit();

	}

	/**
	 * currencrMarkの設定値取得メソッド
	 * 
	 * @param context
	 * @return
	 */
	public static boolean useCurrencyMark(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getBoolean(context.getString(R.string.currency_mark_use_key), false);
	}

	/**
	 * currencyMarkの文字取得メソッド
	 * 
	 * @param context
	 * @return
	 */
	public static String getCurrencyMark(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getString(context.getString(R.string.currency_mark_type_key),
				context.getString(R.string.currency_mark_type_dolar));
	}

	/**
	 * default secretの設定値取得メソッド
	 * 
	 * @param context
	 * @return
	 */
	public static boolean getDefaultSecret(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		return prefs.getBoolean(context.getString(R.string.default_secret_key), false);
	}

	/**
	 * History Sizeの設定値取得メソッド
	 * 
	 * @param context
	 * @return
	 */
	public static int getHistorySize(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String historySize = prefs.getString(context.getString(R.string.history_size_key), "10");
		return Integer.parseInt(historySize);
	}

	/**
	 * ログイン状態の取得メソッド
	 * 
	 * @param context
	 * @return
	 */
	public static String getStatus(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.twitter_prefs_key),
				MODE_PRIVATE);

		return prefs.getString(context.getString(R.string.twitter_connect_key), "");
	}

	public static String getSortOrder(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String sortOrder = prefs.getString(context.getString(R.string.sort_order),
				HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_TWEET_DATE]);
		return sortOrder;
	}

	/**
	 * OAuth Token取得メソッド
	 * 
	 * @param context
	 * @return
	 */
	public static String getOauthToken(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.twitter_prefs_key),
				MODE_PRIVATE);

		return prefs.getString(context.getString(R.string.twitter_oauth_token_key), "");
	}

	/**
	 * OAuth Token Secret取得メソッド
	 * 
	 * @param context
	 * @return
	 */
	public static String getOauthTokenSecret(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.twitter_prefs_key),
				MODE_PRIVATE);

		return prefs.getString(context.getString(R.string.twitter_oauth_token_secret_key), "");
	}
}
