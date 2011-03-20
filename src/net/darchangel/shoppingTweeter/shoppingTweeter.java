package net.darchangel.shoppingTweeter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class shoppingTweeter extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);
		adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// アイテムを追加します
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
		Spinner spinner = (Spinner) findViewById(R.id.category);
		// アダプターを設定します
		spinner.setAdapter(adapter);
	}
}