package net.darchangel.shoppingTweeter;

import java.util.List;

import net.darchangel.shoppingTweeter.util.HistoryTableDAO;
import net.darchangel.shoppingTweeter.util.ShoppingItem;
import net.darchangel.shoppingTweeter.util.ShoppingTweeterDBHelper;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class History extends Activity {

    private TableLayout historyTable;
    private static HistoryTableDAO historyTableDAO;

    // ヘッダを含まないデータの行数
    int row_num = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        historyTable = (TableLayout) findViewById(R.id.history_table);

        createHistoryTable();

        TextView header_item = (TextView) findViewById(R.id.header_item);
        header_item.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // SharedPreferenceのSortOrderにitem_nameを設定
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(History.this);
                int sort_key = Pref.getSortKey(History.this);
                int sort_order = Pref.getSortOrder(History.this);
                SharedPreferences.Editor editor = pref.edit();
                if (sort_key == HistoryTableDAO.COLUMN_ITEM_NAME) {
                    // ソートキーがアイテム名の場合
                    if (sort_order == HistoryTableDAO.SORT_BY_ASC) {
                        // ソートオーダが昇順の場合
                        sort_order = HistoryTableDAO.SORT_BYA_DESC;
                    } else {
                        // ソートオーダが降順の場合
                        sort_order = HistoryTableDAO.SORT_BY_ASC;
                    }
                } else {
                    // ソートキーがアイテム名以外の場合
                    sort_key = HistoryTableDAO.COLUMN_ITEM_NAME;
                    sort_order = HistoryTableDAO.SORT_BY_ASC;
                }
                editor.putInt(getString(R.string.sort_key), sort_key);
                editor.putInt(getString(R.string.sort_order), sort_order);
                editor.commit();

                // history tableを再構築
                createHistoryTable();
            }
        });

        TextView header_expense = (TextView) findViewById(R.id.header_expense);
        header_expense.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // SharedPreferenceのSortOrderにitem_expenseを設定
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(History.this);
                int sort_key = Pref.getSortKey(History.this);
                int sort_order = Pref.getSortOrder(History.this);
                SharedPreferences.Editor editor = pref.edit();
                if (sort_key == HistoryTableDAO.COLUMN_EXPENSE) {
                    // ソートキーが金額の場合
                    if (sort_order == HistoryTableDAO.SORT_BY_ASC) {
                        // ソートオーダが昇順の場合
                        sort_order = HistoryTableDAO.SORT_BYA_DESC;
                    } else {
                        // ソートオーダが降順の場合
                        sort_order = HistoryTableDAO.SORT_BY_ASC;
                    }
                } else {
                    // ソートキーが金額以外の場合
                    sort_key = HistoryTableDAO.COLUMN_EXPENSE;
                    sort_order = HistoryTableDAO.SORT_BY_ASC;
                }
                editor.putInt(getString(R.string.sort_key), sort_key);
                editor.putInt(getString(R.string.sort_order), sort_order);
                editor.commit();

                // history tableを再構築
                createHistoryTable();
            }
        });

    }

    /**
     * リストからテーブルを作成する
     * 
     */
    private void createHistoryTable() {
        ShoppingTweeterDBHelper dbHelper = new ShoppingTweeterDBHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        historyTableDAO = new HistoryTableDAO(db);

        // ソート順を取得
        String sortOrder = HistoryTableDAO.COLUMNS[Pref.getSortKey(this)] + " "
                + HistoryTableDAO.SORT_BY[Pref.getSortOrder(this)];

        // 履歴テーブルからデータを取得
        List<ShoppingItem> historyList = historyTableDAO.selectAll(sortOrder);

        // 既存のTableLayoutを初期化
        if (row_num != 0) {
            cleanHistoryTable();
        }

        for (ShoppingItem shoppingItem : historyList) {
            TableRow row = new TableRow(this);
            row.setId(row_num++);

            TableRow.LayoutParams item_layout = new TableRow.LayoutParams();
            item_layout.weight = (float) 0.75;
            item_layout.width = 0;
            item_layout.setMargins(1, 1, 1, 1);

            TableRow.LayoutParams expense_layout = new TableRow.LayoutParams();
            expense_layout.weight = (float) 0.25;
            expense_layout.width = 0;
            expense_layout.setMargins(1, 1, 1, 1);

            // 品目名を取得
            TextView item = new TextView(this);
            item.setText(shoppingItem.getItemName());
            item.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            item.setBackgroundColor(Color.BLACK);
            item.setLayoutParams(item_layout);

            // 金額を取得
            TextView expense = new TextView(this);
            expense.setText(Integer.toString((shoppingItem.getExpense())));
            expense.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            expense.setGravity(Gravity.RIGHT);
            expense.setBackgroundColor(Color.BLACK);
            expense.setLayoutParams(expense_layout);

            // 取得した値を行に追加
            row.addView(item);
            row.addView(expense);

            row.setClickable(true);
            setActionEachRow(row);

            // 作成した行をテーブルに追加
            historyTable.addView(row);

        }
    }

    /**
     * Viewにアクションを付加する。
     * 
     * @param v
     */
    private void setActionEachRow(View v) {
        v.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TableRow row = (TableRow) findViewById(v.getId());
                TextView item = (TextView) row.getChildAt(0);
                TextView expense = (TextView) row.getChildAt(1);

                Intent intent = new Intent(History.this, (Class<?>) ShoppingTweeter.class);
                intent.putExtra(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_ITEM_NAME], item.getText());
                intent.putExtra(HistoryTableDAO.COLUMNS[HistoryTableDAO.COLUMN_EXPENSE], expense.getText());
                setResult(Activity.RESULT_OK, intent);

                // 履歴画面を終了
                finish();
            }
        });
    }

    /**
     * 履歴テーブルをヘッダ以外削除する。
     */
    private void cleanHistoryTable() {
        // TableLayoutの行を削除(ヘッダは消さないためstartを1に指定。)
        historyTable.removeViews(1, row_num);
        // 行数を初期化
        row_num = 0;
    }
}
