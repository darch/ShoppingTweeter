package net.darchangel.shoppingTweeter;

import java.util.List;

import net.darchangel.shoppingTweeter.util.HistoryTableDAO;
import net.darchangel.shoppingTweeter.util.ShoppingItem;
import net.darchangel.shoppingTweeter.util.ShoppingTweeterDBHelper;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class History extends Activity {

	private TableLayout historyTable;
	private static HistoryTableDAO historyTableDAO;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);

		historyTable = (TableLayout) findViewById(R.id.history_table);

		ShoppingTweeterDBHelper dbHelper = new ShoppingTweeterDBHelper(this);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		historyTableDAO = new HistoryTableDAO(db);

		List<ShoppingItem> historyList = historyTableDAO.selectAll();
		createHistoryTable(historyTable, historyList);
	}

	/**
	 * リストからテーブルを作成する
	 * 
	 * @param table
	 *            テーブルレイアウト
	 * @param list
	 *            履歴リスト
	 */
	private void createHistoryTable(TableLayout table, List<ShoppingItem> list) {
		for (ShoppingItem shoppingItem : list) {
			TableRow row = new TableRow(this);

			// 品目名を取得
			TextView item = new TextView(this);
			item.setText(shoppingItem.getItemName());

			// 金額を取得
			TextView expense = new TextView(this);
			expense.setText(Integer.toString((shoppingItem.getExpense())));

			// 取得した値を行に追加
			row.addView(item);
			row.addView(expense);
			
			// 作成した行をテーブルに追加
			table.addView(row);

		}
	}
}
