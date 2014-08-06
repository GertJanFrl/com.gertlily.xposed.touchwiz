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

 public class FragmentPopups extends Fragment {
	private Switch popups_usbcover;
	private Switch popups_batterycover;
	
	public FragmentPopups(){}

	@SuppressLint("WorldReadableFiles")
	@SuppressWarnings("deprecation")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final SharedPreferences prefs = this.getActivity().getSharedPreferences(Common.PREFERENCES_NAME, Context.MODE_WORLD_READABLE);
		 
        View rootView = inflater.inflate(R.layout.fragment_popups, container, false);
        
		popups_usbcover 		= (Switch) rootView.findViewById(R.id.popups_usbcover);
		popups_batterycover		= (Switch) rootView.findViewById(R.id.popups_batterycover);

		popups_usbcover.setChecked(this.getActivity().getSharedPreferences(Common.PREFERENCES_NAME, Context.MODE_WORLD_READABLE).getBoolean("popupsusbcoverhidden", false));
		popups_batterycover.setChecked(this.getActivity().getSharedPreferences(Common.PREFERENCES_NAME, Context.MODE_WORLD_READABLE).getBoolean("popupsbatterycoverhidden", false));

		popups_usbcover.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			  @Override
			  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					SharedPreferences.Editor editor = prefs.edit();
					if (isChecked) {
						editor.putBoolean("popupsusbcoverhidden", true);
					} else {
						editor.putBoolean("popupsusbcoverhidden", false);
					}
					editor.apply();
			  }
		});
		popups_batterycover.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			  @Override
			  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					SharedPreferences.Editor editor = prefs.edit();
					if (isChecked) {
						editor.putBoolean("popupsbatterycoverhidden", true);
					} else {
						editor.putBoolean("popupsbatterycoverhidden", false);
					}
					editor.apply();
			  }
		});
		
        return rootView;
    }
}
