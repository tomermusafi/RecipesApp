package com.musafi.recipesapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

import com.musafi.recipesapp.CallBack_Activity;
import com.musafi.recipesapp.R;

public class WebPageFragment extends Fragment {
    private CallBack_Activity callBack_activity;
    private WebView webView;
    private View view;
    private  Context context;
    private String url;
    public WebPageFragment(){

    }
    public WebPageFragment(Context context) {
        this.context = context;

    }
    public void setCallBack(CallBack_Activity _callBack_activity) {
        this.callBack_activity = _callBack_activity;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);
        Log.d("pttt", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("pttt", "onCreateView");
        if(view==null){
            view = inflater.inflate(R.layout.fragment_web_view, container, false);
        }
        findViews(view);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        return view;
    }

    public boolean isWebCanGoBack(){
        boolean isWebCanGoBack = webView.canGoBack();
        if(isWebCanGoBack){
            webView.goBack();
        }
        return isWebCanGoBack;
    }

    private void findViews(View view) {
        webView = view.findViewById(R.id.web_view_WV);
    }



}
