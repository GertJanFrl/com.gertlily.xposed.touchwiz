package com.gertlily.xposed.touchwiz.fragments;

import com.gertlily.xposed.touchwiz.R;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FragmentHidden extends Fragment {
	private Button torchSettingsButton;

	public FragmentHidden(){}
		
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
        View rootView = inflater.inflate(R.layout.fragment_hidden, container, false);
        getActivity().setTitle(R.string.app_name);

        torchSettingsButton = (Button) rootView.findViewById(R.id.torch_settings_button);
        torchSettingsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.setClassName("com.android.settings", "com.android.settings.torchlight.TorchlightSettings");
				startActivity(intent);
			}
        });
		
        return rootView;
    }
}
