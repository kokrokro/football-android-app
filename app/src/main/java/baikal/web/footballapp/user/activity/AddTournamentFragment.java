package baikal.web.footballapp.user.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

import baikal.web.footballapp.R;
import baikal.web.footballapp.SaveSharedPreference;

public class AddTournamentFragment extends Fragment {

    private static final String admin = "https://football.bwadm.ru";

    private String tourneyId;

    public AddTournamentFragment() { tourneyId = ""; }
    public AddTournamentFragment(String tourneyId) { this.tourneyId = tourneyId; }


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("USER_TOURNAMENT", "loading web view...");
        final View view;
        view = inflater.inflate(R.layout.fragment_add_tournament, container, false);
        WebView webView = view.findViewById(R.id.webViewTournament1);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSaveFormData(false);
        webView.setVerticalScrollBarEnabled(true);

        String token = null;

        try {
            token = SaveSharedPreference.getObject().getToken();
        } catch (Exception ignored) {
            Log.e("AddTournamentFragment", " can't get token");
        }

        final String adminPath;

        if (!tourneyId.equals("")) adminPath = admin + "/tourney/" + tourneyId;
        else                       adminPath = admin;

        webView.evaluateJavascript("localStorage.setItem(\'"+ "auth" +"\',\'"+ token +"\');", null);

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setMinimumFontSize(1);
        webView.getSettings().setMinimumLogicalFontSize(1);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (adminPath.equals(Uri.parse(url).getHost())) {
                    return false;
                }
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }
        });
        webView.loadUrl(adminPath);

        return view;
    }
}
