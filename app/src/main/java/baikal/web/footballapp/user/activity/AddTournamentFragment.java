package baikal.web.footballapp.user.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.HttpAuthHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.HashMap;
import java.util.Map;

import baikal.web.footballapp.PersonalActivity;
import baikal.web.footballapp.R;
import baikal.web.footballapp.tournament.activity.TournamentPage;

public class AddTournamentFragment extends Fragment {


    public AddTournamentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("USER_TOURNAMENT", "loading web view...");
        final View view;
        view = inflater.inflate(R.layout.fragment_add_tournament, container, false);
        WebView webView = view.findViewById(R.id.webViewTournament1);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.evaluateJavascript("localStorage.setItem('"+ "auth" +"','"+ PersonalActivity.token +"');", null);
        webView.loadUrl("https://football.bwadm.ru");

        webView.setWebViewClient(new AddTournamentFragment.MyWebViewClient());
       // webView.clearCache(true);
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
            if ("https://football.bwadm.ru".equals(Uri.parse(url).getHost())) {

                return false;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view,
                                                          WebResourceRequest request) {
            return shouldInterceptRequest(view, request.getUrl().toString());
        }
    }
}
