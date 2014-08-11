package com.gertlily.xposed.touchwiz.fragments;

import com.gertlily.xposed.touchwiz.R;

import android.app.Fragment;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentAbout extends Fragment {
	
	public FragmentAbout(){}
		
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        getActivity().setTitle(R.string.app_name);

        try {
			String packageName = getActivity().getPackageName();
			String version = getActivity().getPackageManager().getPackageInfo(packageName, 0).versionName;
			((TextView) rootView.findViewById(R.id.version)).setText(version);
		} catch (NameNotFoundException e) { }
		
		((TextView) rootView.findViewById(R.id.developedby)).setMovementMethod(LinkMovementMethod.getInstance());
		((TextView) rootView.findViewById(R.id.support)).setMovementMethod(LinkMovementMethod.getInstance());

		String translatedby = getResources().getString(R.string.about_translatedby);
		if (translatedby.isEmpty()) {
			rootView.findViewById(R.id.translatedby_title).setVisibility(View.GONE);
			rootView.findViewById(R.id.translatedby).setVisibility(View.GONE);
			rootView.findViewById(R.id.translatedby_break).setVisibility(View.GONE);
		} else {
			((TextView) rootView.findViewById(R.id.translatedby)).setMovementMethod(LinkMovementMethod.getInstance());
		}
		
        return rootView;
    }
}
