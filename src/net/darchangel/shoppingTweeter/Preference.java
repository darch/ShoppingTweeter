package net.darchangel.shoppingTweeter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Preference extends PreferenceActivity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.pref);
	}

	public static boolean useCurrencyMark(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getBoolean(context
				.getString(R.string.currency_mark_use_key), false);
	}

	public static String getCurrencyMark(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getString(context
				.getString(R.string.currency_mark_type_key), context
				.getString(R.string.currency_mark_type_dolar));
	}
}
