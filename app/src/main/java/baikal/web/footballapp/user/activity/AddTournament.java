package baikal.web.footballapp.user.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import baikal.web.footballapp.R;

public class AddTournament extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_tournament);
        WebView webView = (WebView) findViewById(R.id.webViewTournament);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("https://football.bwadm.ru");
        webView.setWebViewClient(new MyWebViewClient());
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true); //check the documentation for info about dbpath
        webSettings.setMinimumFontSize(1);
        webSettings.setMinimumLogicalFontSize(1);
        webSettings.setSupportZoom(false);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);

    }
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if ("https://football.bwadm.ru".equals(Uri.parse(url).getHost())) {
                // This is my website, so do not override; let my WebView load the page
                Log.d("LOADING", "LOADING..........");
                return false;
            }


            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }
}
