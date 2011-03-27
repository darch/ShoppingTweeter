package net.darchangel.shoppingTweeter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

public class Preference extends PreferenceActivity {

	private PreferenceScreen login;
	private PreferenceScreen logout;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.pref);

		login = (PreferenceScreen) findPreference(getString(R.string.pref_login_key));
		logout = (PreferenceScreen) findPreference(getString(R.string.pref_logout_key));

		logout.setEnabled(false);
	}

	public static boolean useCurrencyMark(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getBoolean(
				context.getString(R.string.currency_mark_use_key), false);
	}

	public static String getCurrencyMark(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getString(
				context.getString(R.string.currency_mark_type_key),
				context.getString(R.string.currency_mark_type_dolar));
	}
}
