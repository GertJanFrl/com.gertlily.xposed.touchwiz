package com.gertlily.xposed.touchwiz.fragments;

import com.gertlily.xposed.touchwiz.hooks.Common;
import com.gertlily.xposed.touchwiz.R;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;

public class FragmentSFinderQConnect extends Fragment {
	private Switch sfinder_switch;
	private EditText sfinder_textfield;
	private Switch qconnect_switch;
	private EditText qconnect_textfield;
	
	public FragmentSFinderQConnect(){}

	@SuppressLint("WorldReadableFiles")
	@SuppressWarnings("deprecation")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final SharedPreferences prefs = this.getActivity().getSharedPreferences(Common.PREFERENCES_NAME, Context.MODE_WORLD_READABLE);

        View rootView = inflater.inflate(R.layout.fragment_sfinder_qconnect, container, false);
        
		sfinder_switch 		= (Switch) rootView.findViewById(R.id.sfinder_switch);
		sfinder_textfield 	= (EditText) rootView.findViewById(R.id.sfinder_textfield);
		qconnect_switch 	= (Switch) rootView.findViewById(R.id.qconnect_switch);
		qconnect_textfield 	= (EditText) rootView.findViewById(R.id.qconnect_textfield);

		sfinder_switch.setChecked(this.getActivity().getSharedPreferences(Common.PREFERENCES_NAME, Context.MODE_WORLD_READABLE).getBoolean("sqlayoutsfinder", false));
		if(this.getActivity().getSharedPreferences(Common.PREFERENCES_NAME, android.content.Context.MODE_PRIVATE).getBoolean("sqlayoutsfinder", false)) {
			sfinder_textfield.setVisibility(View.GONE);	
		}
		sfinder_textfield.setText(this.getActivity().getSharedPreferences(Common.PREFERENCES_NAME, android.content.Context.MODE_PRIVATE).getString("sfindertextfield", ""));
		
		qconnect_switch.setChecked(this.getActivity().getSharedPreferences(Common.PREFERENCES_NAME, android.content.Context.MODE_PRIVATE).getBoolean("sqlayoutqconnect", false));
		if(this.getActivity().getSharedPreferences(Common.PREFERENCES_NAME, android.content.Context.MODE_PRIVATE).getBoolean("sqlayoutqconnect", false)) {
			qconnect_textfield.setVisibility(View.GONE);	
		}
		qconnect_textfield.setText(this.getActivity().getSharedPreferences(Common.PREFERENCES_NAME, android.content.Context.MODE_PRIVATE).getString("qconnecttextfield", ""));

		sfinder_switch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			  @Override
			  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					SharedPreferences.Editor editor = prefs.edit();
					if (isChecked) {
						editor.putBoolean("sqlayoutsfinder", true);
						sfinder_textfield.setVisibility(View.GONE);
					} else {
						editor.putBoolean("sqlayoutsfinder", false);
						sfinder_textfield.setVisibility(View.VISIBLE);
					}
					editor.apply();
			  }
		});
		sfinder_textfield.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) { }

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("sfindertextfield", s.toString());
				editor.apply();
			}
		});
		
		qconnect_switch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			  @Override
			  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					SharedPreferences.Editor editor = prefs.edit();
					if (isChecked) {
						editor.putBoolean("sqlayoutqconnect", true);
						qconnect_textfield.setVisibility(View.GONE);
					} else {
						editor.putBoolean("sqlayoutqconnect", false);
						qconnect_textfield.setVisibility(View.VISIBLE);
					}
					editor.apply();
			  }
		});
		qconnect_textfield.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) { }

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("qconnecttextfield", s.toString());
				editor.apply();
			}
		});
		
        return rootView;
    }
}
