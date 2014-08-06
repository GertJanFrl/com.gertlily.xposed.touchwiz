package com.gertlily.xposed.touchwiz.fragments;

import com.gertlily.xposed.touchwiz.R;
import com.gertlily.xposed.touchwiz.hooks.Common;

import de.robv.android.xposed.XposedBridge;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentGeneral extends Fragment {
	private Button restart_systemui;
	private Button restart_popupuireceiver;
	
	public FragmentGeneral(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_general, container, false);
        
        restart_systemui 		= (Button) rootView.findViewById(R.id.restart_systemui);
        restart_popupuireceiver	= (Button) rootView.findViewById(R.id.restart_popupuireceiver);
		
        restart_systemui.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				buttonRestartPackage(R.string.general_restart_systemui, Common.PACKAGE_SYSTEMUI);
			}
        });
        restart_popupuireceiver.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				buttonRestartPackage(R.string.general_restart_popupuireceiver, Common.PACKAGE_POPUPUIRECEIVER);
			}
        });
        
        return rootView;
    }
	
	private void buttonRestartPackage(int app_title, final String app_package) {
		new AlertDialog.Builder(getActivity())
			.setIconAttribute(android.R.attr.alertDialogIcon)
			.setTitle(app_title)
			.setMessage(R.string.areyousure)
			.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					try {
						Process proc = Runtime.getRuntime()
							.exec(
								new String[] { 
									"su",
									"-c", 
									"busybox killall " + app_package
								}
							);
						proc.waitFor();
					} catch (Exception e) { 
						XposedBridge.log(e);
					}
				}
			})
			.setNegativeButton(android.R.string.no, null)
			.create()
			.show();
	}
}
