package com.sccl.summerreadingapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class InformationFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_information, container, false);
        //TextView tvInfoBody = (TextView) rootView.findViewById(R.id.infoBody);
        //tvInfoBody.setMovementMethod(LinkMovementMethod.getInstance());
        // tvInfoBody.setText(getString(R.string.info_body));
        //tvInfoBody.setText(Html.fromHtml(getString(R.string.info_body)));
        
        WebView webView = (WebView)rootView.findViewById(R.id.informationWebView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadDataWithBaseURL("file:///android_asset/", getString(R.string.info_body), "text/html", "UTF-8", "");
        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null && url.startsWith("http://")) {
                    view.getContext().startActivity(
                        new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                } else {
                    return false;
                }
            }
        });        
        return rootView;
    }
}