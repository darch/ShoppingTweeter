package net.darchangel.shoppingTweeter;

import net.darchangel.shoppingTweeter.exception.NoInputException;
import net.darchangel.shoppingTweeter.exception.TooLongException;
import net.darchangel.shoppingTweeter.util.HistoryTableDAO;
import net.darchangel.shoppingTweeter.util.ShoppingItem;
import net.darchangel.shoppingTweeter.util.ShoppingTweeterDBHelper;
import net.darchangel.shoppingTweeter.util.TweetTaskStatus;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;

public class TweetTask extends AsyncTask<Void, Void, TweetTaskStatus> {
    private ShoppingTweeter activity;
    private String item;
    private String expense;
    private String comment;
    private String category;
    private boolean creditcard;
    private boolean secret;

    private TweetTaskStatus tweetStatus;

    private ShoppingItem ShoppingItem;

    public TweetTask(ShoppingTweeter activity) {
        this.activity = activity;

        item = this.activity.item.getText().toString();
        expense = this.activity.expense.getText().toString();
        comment = this.activity.comment.getText().toString();
        category = this.activity.category.getSelectedItem().toString();
        creditcard = this.activity.creditcard.isChecked();
        secret = this.activity.secret.isChecked();
    }

    @Override
    protected void onPreExecute() {
        activity.setProgressBarIndeterminateVisibility(true);
    }

    @Override
    protected TweetTaskStatus doInBackground(Void... params) {
        tweetStatus = new TweetTaskStatus();

        String tweet_str = checkTweetable();
        if (tweet_str.length() != 0) {
            // Tweet可能な場合

            // Tweetする
            try {
                tweetStatus.setTweetStatus(tweet(tweet_str));

            } catch (TwitterException e) {

                tweetStatus.setException(e);
                e.printStackTrace();
            }
        }
        return tweetStatus;
    }

    @Override
    protected void onPostExecute(TweetTaskStatus status) {
        activity.setProgressBarIndeterminateVisibility(false);

        if (status.isSuccess()) {
            Toast.makeText(activity, R.string.tweet_success, Toast.LENGTH_LONG).show();

            // 履歴に登録
            registHistory();

            // フォームをリセット
            activity.clearForm();

            // Itemにカーソル
            activity.item.requestFocus();
        } else {
            Exception exception = status.getException();

            if (exception instanceof NoInputException) {
                NoInputException e = (NoInputException) exception;

                // 必須項目が入力されていなかった場合
                Toast.makeText(activity, String.format(activity.getString(R.string.necessary_msg), e.getName()),
                        Toast.LENGTH_SHORT).show();

            } else if (exception instanceof NumberFormatException) {
                NumberFormatException e = (NumberFormatException) exception;

                // expenseに数字以外が入力されていた場合
                Toast.makeText(activity, activity.getString(R.string.only_number), Toast.LENGTH_SHORT).show();

            } else if (exception instanceof TooLongException) {
                TooLongException e = (TooLongException) exception;

                // 入力内容が規定文字数より長い場合
                Toast.makeText(activity, String.format(activity.getString(R.string.too_long_msg), e.getLength()),
                        Toast.LENGTH_SHORT).show();
            } else if (exception instanceof TwitterException) {
                TwitterException e = (TwitterException) exception;

                // Tweetに失敗した場合
                Toast.makeText(activity, R.string.tweet_fail, Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();

        activity.setProgressBarIndeterminateVisibility(false);
    }

    /**
     * Tweet可能か確認<br />
     * Tweetが可能な場合はTweet内容を、不可能な場合は空文字を返す
     */
    private String checkTweetable() {
        String tweet_str = "";

        try {
            // 必須項目の入力チェック
            checkInput(activity.item);
            checkInput(activity.expense);

            // expenseに数字以外が入力されていないかチェック
            checkNumber(activity.expense);

            // Tweet内容を生成
            tweet_str = makeTweet();

            // Tweet内容の文字数をチェック
            checkTweetLength(tweet_str);

            // DB登録用データの作成
            ShoppingItem = new ShoppingItem(item, Integer.parseInt(expense));
            ShoppingItem.setComment(comment);
            ShoppingItem.setCategory(category);
            ShoppingItem.setUseCreditCard(creditcard);
            ShoppingItem.setSecret(secret);

        } catch (NoInputException e) {
            // 必須項目が入力されていなかった場合

            tweetStatus.setException(e);

        } catch (NumberFormatException e) {
            // expenseに数字以外が入力されていた場合

            tweetStatus.setException(e);

        } catch (TooLongException e) {
            // 入力内容が規定文字数より長い場合

            tweetStatus.setException(e);

            // Tweet内容をクリア
            tweet_str = "";

        }
        return tweet_str;
    }

    /**
     * 購入品目を履歴テーブルに追加
     */
    private void registHistory() {
        // TODO 履歴の無効化

        // 履歴に登録
        ShoppingTweeterDBHelper dbHelper = new ShoppingTweeterDBHelper(activity.getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        HistoryTableDAO historyTableDAO = new HistoryTableDAO(db);
        historyTableDAO.add(ShoppingItem);

        // 登録レコード数を調整
        int maxRowNum = Pref.getHistorySize(activity);
        historyTableDAO.deleteRecord(maxRowNum);
    }

    /**
     * EditTextに文字が入力されているかチェックする
     * 
     * @param input
     * @throws NoInputException
     */
    private void checkInput(EditText input) throws NoInputException {
        // EditTextに入力されている文字列を取得
        String str = input.getText().toString();
        String name = input.getTag().toString();

        if (str.trim().length() == 0) {
            // 文字列の末尾のスペースを除いた長さが0の場合

            throw new NoInputException(name);
        }
    }

    /**
     * EditTextに入力された文字が正の数であることをチェックする
     * 
     * @param input
     * @throws NumberFormatException
     */
    private void checkNumber(EditText input) throws NumberFormatException {

        // EditTextに入力されている文字を数値に変換
        double input_num = Double.parseDouble(input.getText().toString());

        if (input_num < 0) {
            // 入力されている数値が0未満の場合

            throw new NumberFormatException();
        }

    }

    /**
     * Tweet内容を生成
     */
    private String makeTweet() {
        String tweet_str = "";

        if (secret) {
            // secretがチェックされている場合はダイレクトメッセージ
            tweet_str += activity.getString(R.string.check_secret) + " ";
        }

        tweet_str += item;
        tweet_str += " ";
        if (Pref.useCurrencyMark(activity)) {
            tweet_str += Pref.getCurrencyMark(activity);
        }
        tweet_str += expense;

        if (comment.length() != 0) {
            // コメントが入力されている場合は、つぶやきに追加
            tweet_str += " " + comment;
        }

        if (!activity.getString(R.string.category_other).equals(activity.category.getSelectedItem().toString())) {
            // カテゴリがother以外の場合はハッシュタグとしてつぶやきに追加
            tweet_str += " #" + activity.category.getSelectedItem().toString();
        }

        if (creditcard) {
            tweet_str += " " + activity.getString(R.string.check_creditcard);
        }

        return tweet_str;
    }

    /**
     * 文字列が規定文字数(140文字)を越えていないかチェックする
     * 
     * @param str
     *            文字列
     * @throws IllegalStateException
     */
    private void checkTweetLength(String str) throws TooLongException {
        // 規定文字数を取得
        int tweet_length = Integer.parseInt(activity.getString(R.string.tweet_length));

        if (str.length() > tweet_length) {
            // Tweet内容が規定文字数を超える場合

            throw new TooLongException(str.length());
        }
    }

    /**
     * 引数の文字列をつぶやく
     * 
     * @param str
     *            つぶやく文字列
     * @return
     * @throws TwitterException
     */
    private twitter4j.Status tweet(String str) throws TwitterException {
        // AccessTokenの取得
        AccessToken accessToken = new AccessToken(Pref.getOauthToken(activity), Pref.getOauthTokenSecret(activity));

        // Consumer keyとConsumer secretを設定
        ConfigurationBuilder confbuilder = new ConfigurationBuilder();
        confbuilder.setOAuthConsumerKey(activity.getString(R.string.consumer_key));
        confbuilder.setOAuthConsumerSecret(activity.getString(R.string.consumer_secret));

        Twitter twitter = new TwitterFactory(confbuilder.build()).getInstance(accessToken);
        return twitter.updateStatus(str);
    }
}
