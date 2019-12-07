package baikal.web.footballapp.user.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;

public class AddTournamentFragment extends Fragment {

    private static final String admin = "https://football.bwadm.ru";
    public AddTournamentFragment() { }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("USER_TOURNAMENT", "loading web view...");
        final View view;
        view = inflater.inflate(R.layout.fragment_add_tournament, container, false);
        WebView webView = view.findViewById(R.id.webViewTournament1);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setVerticalScrollBarEnabled(true);

        String token = null;

        try {
            token = SaveSharedPreference.getObject().getToken();
        } catch (Exception ignored) { }

        webView.evaluateJavascript("localStorage.setItem('"+ "auth" +"','"+ token +"');", null);
        webView.loadUrl(admin);

        webView.setWebViewClient(new AddTournamentFragment.MyWebViewClient());
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setMinimumFontSize(1);
        webSettings.setMinimumLogicalFontSize(1);
        webSettings.setSupportZoom(false);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);

       // getContext().deleteDatabase(webSettings.getDatabasePath());
        return view;
    }
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (admin.equals(Uri.parse(url).getHost())) {

                return false;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            //noinspection deprecation
            return shouldInterceptRequest(view, request.getUrl().toString());
        }
    }
}
