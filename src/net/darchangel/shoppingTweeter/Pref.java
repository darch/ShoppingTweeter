package net.darchangel.shoppingTweeter;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;

public class Pref extends PreferenceActivity {

	private Preference login;
	private Preference logout;

	private Twitter twitter = null;
	private RequestToken requestToken = null;

	private String CONSUMER_KEY = "zx6S2ou4UoIHdLtjQRYg";
	private String CONSUMER_SECRET = "wumWmiYcqvmpB73xx5hCIHfcumPH4sheEWow9DLEw";

	// ログイン状態
	private String auth_status = "";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.pref);

		SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
		// ログイン状態をSharedPreferencesから取得
		auth_status = pref.getString("status", "");

		login = (Preference) findPreference(getString(R.string.pref_login_key));
		logout = (Preference) findPreference(getString(R.string.pref_logout_key));

		setAction();

	}

	@Override
	protected void onStart() {
		super.onStart();

		// ログイン状態をSharedPreferencesから取得
		SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
		auth_status = pref.getString("status", "");

		// ログイン状態を判定
		if (isConnected(auth_status)) {
			// ログイン済みの場合

			// Loginを非活性化
			login.setEnabled(false);
		} else {
			// 未ログインの場合

			// Logoutを非活性化
			logout.setEnabled(false);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {

		// TODO intentがnullになってる
		// TODO RESULT_CANCELEDでいいの？
		if (resultCode == RESULT_CANCELED) {
			super.onActivityResult(requestCode, resultCode, intent);

			AccessToken accessToken = null;

			try {
				accessToken = twitter.getOAuthAccessToken(requestToken, intent
						.getExtras().getString("oauth_verifier"));

				SharedPreferences pref = getSharedPreferences("Twitter_seting",
						MODE_PRIVATE);

				SharedPreferences.Editor editor = pref.edit();
				editor.putString("oauth_token", accessToken.getToken());
				editor.putString("oauth_token_secret", accessToken
						.getTokenSecret());
				editor.putString("status", "connected");

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

		if (status != null && status.equals("connected")) {
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

		ConfigurationBuilder confbuilder = new ConfigurationBuilder();

		confbuilder.setOAuthConsumerKey(CONSUMER_KEY);
		confbuilder.setOAuthConsumerSecret(CONSUMER_SECRET);

		twitter = new TwitterFactory(confbuilder.build()).getInstance();

		String CALLBACK_URL = "shoppingtweeter://oauth";
		// requestTokenもクラス変数。
		try {
			requestToken = twitter.getOAuthRequestToken(CALLBACK_URL);
		} catch (TwitterException e) {
			e.printStackTrace();
		}

		// 認証用URLをインテントにセット。
		// TwitterLoginはActivityのクラス名。
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(requestToken.getAuthorizationURL()));

		// アクティビティを起動
		this.startActivityForResult(intent, 0);

	}

	/**
	 * ログアウト処理を行う
	 */
	private void disconnectTwitter() {

		SharedPreferences pref = getSharedPreferences("Twitter_seting",
				MODE_PRIVATE);

		SharedPreferences.Editor editor = pref.edit();
		editor.remove("oauth_token");
		editor.remove("oauth_token_secret");
		editor.remove("status");

		editor.commit();

	}

	/**
	 * currencrMarkの設定値取得メソッド
	 * 
	 * @param context
	 * @return
	 */
	public static boolean useCurrencyMark(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getBoolean(context
				.getString(R.string.currency_mark_use_key), false);
	}

	/**
	 * currencyMarkの文字取得メソッド
	 * 
	 * @param context
	 * @return
	 */
	public static String getCurrencyMark(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getString(context
				.getString(R.string.currency_mark_type_key), context
				.getString(R.string.currency_mark_type_dolar));
	}
}
