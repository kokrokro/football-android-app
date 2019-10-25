package baikal.web.footballapp.user.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import baikal.web.footballapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddTournamentFragment extends Fragment {


    public AddTournamentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view;
        view = inflater.inflate(R.layout.fragment_add_tournament, container, false);
        // Inflate the layout for this fragment

        WebView webView = view.findViewById(R.id.webViewTournament1);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("https://football.bwadm.ru");
        webView.setWebViewClient(new AddTournamentFragment.MyWebViewClient());
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true); //check the documentation for info about dbpath
        webSettings.setMinimumFontSize(1);
        webSettings.setMinimumLogicalFontSize(1);
        webSettings.setSupportZoom(false);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        return view;
    }
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if ("https://football.bwadm.ru".equals(Uri.parse(url).getHost())) {
                // This is my website, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }

}
