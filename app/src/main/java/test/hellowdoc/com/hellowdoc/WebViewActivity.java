package test.hellowdoc.com.hellowdoc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    private static final String URL = "URL";
    private WebView myWebView;
    private String webUrl;

    public static Intent getInstance(Context context, String url) {
        final Intent intent = new Intent(context, WebViewActivity.class);
        final Bundle bundle = new Bundle();
        bundle.putString(URL, url);
        intent.putExtras(bundle);
        return intent;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        final Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        if (bundle != null) {
            webUrl = bundle.getString(URL);
        }
        myWebView = findViewById(R.id.webview);

        myWebView.setWebViewClient( new WebViewClient());

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (myWebView != null) {
            myWebView.loadUrl(webUrl);
        }
    }

    @Override
    protected void onDestroy() {
        myWebView.clearCache(true);
        myWebView.clearHistory();
        myWebView = null;
        super.onDestroy();
    }
}
