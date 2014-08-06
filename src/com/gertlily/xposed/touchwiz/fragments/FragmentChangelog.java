package com.gertlily.xposed.touchwiz.fragments;

import com.gertlily.xposed.touchwiz.R;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class FragmentChangelog extends Fragment {
	private WebView browser;
	
	public FragmentChangelog(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
        View rootView = inflater.inflate(R.layout.fragment_changelog, container, false);
        this.browserLoad(rootView);
        return rootView;
    }
    @SuppressLint("SetJavaScriptEnabled")
	public void browserLoad(View rootView){
		browser = (WebView) rootView.findViewById(R.id.webview);
		browser.setBackgroundColor(0x00000000);		
        browser.loadUrl("file:///android_res/raw/index.html");
    }
}
