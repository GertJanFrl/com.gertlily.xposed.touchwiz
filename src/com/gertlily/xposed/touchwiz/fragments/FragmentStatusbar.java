package com.gertlily.xposed.touchwiz.fragments;

import com.gertlily.xposed.touchwiz.hooks.Common;
import com.gertlily.xposed.touchwiz.R;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

public class FragmentStatusbar extends Fragment {
	private Switch notificationbar_hidden;
	private Switch notification_batteryfull;
	private Switch statusbar_carrier;
	
	public FragmentStatusbar(){}
	
	@SuppressLint("WorldReadableFiles")
	@SuppressWarnings("deprecation")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final SharedPreferences prefs = this.getActivity().getSharedPreferences(Common.PREFERENCES_NAME, Context.MODE_WORLD_READABLE);
 
        View rootView = inflater.inflate(R.layout.fragment_statusbar, container, false);

        notificationbar_hidden = (Switch) rootView.findViewById(R.id.notificationbar_hidden);
        notification_batteryfull = (Switch) rootView.findViewById(R.id.notification_batteryfull);
        statusbar_carrier = (Switch) rootView.findViewById(R.id.statusbar_carrier);

        notificationbar_hidden.setChecked(this.getActivity().getSharedPreferences(Common.PREFERENCES_NAME, Context.MODE_WORLD_READABLE).getBoolean("notificationbarhidden", false));
        notification_batteryfull.setChecked(this.getActivity().getSharedPreferences(Common.PREFERENCES_NAME, Context.MODE_WORLD_READABLE).getBoolean("notificationbatteryfull", false));
        statusbar_carrier.setChecked(this.getActivity().getSharedPreferences(Common.PREFERENCES_NAME, Context.MODE_WORLD_READABLE).getBoolean("statusbarcarrierhidden", false));

        notificationbar_hidden.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			  @Override
			  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					SharedPreferences.Editor editor = prefs.edit();
					if (isChecked) {
						editor.putBoolean("notificationbarhidden", true);
					} else {
						editor.putBoolean("notificationbarhidden", false);
					}
					editor.apply();
			  }
		});
        notification_batteryfull.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			  @Override
			  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					SharedPreferences.Editor editor = prefs.edit();
					if (isChecked) {
						editor.putBoolean("notificationbatteryfull", true);
					} else {
						editor.putBoolean("notificationbatteryfull", false);
					}
					editor.apply();
			  }
		});
        statusbar_carrier.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			  @Override
			  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					SharedPreferences.Editor editor = prefs.edit();
					if (isChecked) {
						editor.putBoolean("statusbarcarrierhidden", true);
					} else {
						editor.putBoolean("statusbarcarrierhidden", false);
					}
					editor.apply();
			  }
		});
        
        return rootView;
    }
}
