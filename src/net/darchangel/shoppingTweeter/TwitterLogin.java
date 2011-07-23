package net.darchangel.shoppingTweeter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TwitterLogin extends Activity {

    protected void onCreate(Bundle bundle) {

        super.onCreate(bundle);
        setContentView(R.layout.twitterlogin);

        WebView webView = (WebView) findViewById(R.id.twitterlogin);
        WebSettings webSettings = webView.getSettings();
        // これで別のユーザーとしてサインインする。が実行できる
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (url != null && url.startsWith(getString(R.string.callback_url))) {
                    String[] urlParameters = url.split("\\?")[1].split("&");

                    String oauthToken = "";
                    String oauthVerifier = "";

                    // OAuthTokenを取得
                    if (urlParameters[0].startsWith(getString(R.string.twitter_oauth_token_key))) {
                        oauthToken = urlParameters[0].split("=")[1];
                    } else if (urlParameters[1].startsWith(getString(R.string.twitter_oauth_token_key))) {
                        oauthToken = urlParameters[1].split("=")[1];
                    }

                    // OAuthVerifierを取得
                    if (urlParameters[0].startsWith(getString(R.string.twitter_oauth_verifier))) {
                        oauthVerifier = urlParameters[0].split("=")[1];
                    } else if (urlParameters[1].startsWith(getString(R.string.twitter_oauth_verifier))) {
                        oauthVerifier = urlParameters[1].split("=")[1];
                    }

                    // intentにOAuth認証情報を記録
                    Intent intent = getIntent();
                    intent.putExtra(getString(R.string.twitter_oauth_token_key), oauthToken);
                    intent.putExtra(getString(R.string.twitter_oauth_verifier), oauthVerifier);

                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });

        webView.loadUrl(this.getIntent().getExtras().getString(getString(R.string.auth_url)));

    }
}