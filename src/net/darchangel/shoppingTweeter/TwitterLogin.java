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

				String CALLBACK_URL = "shoppingtweeter://oauth";
				if (url != null && url.startsWith(CALLBACK_URL)) {
					String[] urlParameters = url.split("\\?")[1].split("&");

					String oauthToken = "";
					String oauthVerifier = "";

					if (urlParameters[0].startsWith("oauth_token")) {
						oauthToken = urlParameters[0].split("=")[1];
					} else if (urlParameters[1].startsWith("oauth_token")) {
						oauthToken = urlParameters[1].split("=")[1];
					}

					if (urlParameters[0].startsWith("oauth_verifier")) {
						oauthVerifier = urlParameters[0].split("=")[1];
					} else if (urlParameters[1].startsWith("oauth_verifier")) {
						oauthVerifier = urlParameters[1].split("=")[1];
					}

					Intent intent = getIntent();
					intent.putExtra("oauth_token", oauthToken);
					intent.putExtra("oauth_verifier", oauthVerifier);

					setResult(Activity.RESULT_OK, intent);
					finish();
				}
			}
		});

		webView.loadUrl(this.getIntent().getExtras().getString("auth_url"));

	}
}