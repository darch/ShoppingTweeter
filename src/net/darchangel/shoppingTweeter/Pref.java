package net.darchangel.shoppingTweeter;

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

	/**
	 * 処理を設定
	 */
	private void setAction() {
		login.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO TwitterのOAuth処理に変更
				Uri uri = Uri.parse("http://www.google.co.jp");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
				return true;
			}
		});

		logout.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO ログアウト処理を実装
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
