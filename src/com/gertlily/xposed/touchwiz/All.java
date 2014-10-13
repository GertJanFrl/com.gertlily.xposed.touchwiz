package com.gertlily.xposed.touchwiz;

import java.text.DateFormat;
import java.util.Calendar;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LayoutInflated.LayoutInflatedParam;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import com.gertlily.xposed.touchwiz.hooks.PreferencesHelper;

public class All implements IXposedHookInitPackageResources, IXposedHookLoadPackage  {
	
	// Thanks to Peter Gregus alias C3C076@xda for the log function @ GravityBox
	public static void log(String title, String message) {
		String timestamp = DateFormat.getTimeInstance(DateFormat.SHORT).format(Calendar.getInstance().getTime());
		XposedBridge.log("Xposed G-Touchwiz: (" + timestamp + ") " + title + ": " + message);
	}
	
	public static void logError(String pack, String title, Throwable e) {
		String timestamp = DateFormat.getTimeInstance(DateFormat.SHORT).format(Calendar.getInstance().getTime());
		XposedBridge.log("Xposed G-Touchwiz ERROR: (" + timestamp + ") " + pack + ": " + title + ": " + e);
	}
	
	@Override // Change methods
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		if (lpparam.packageName.equals("com.android.systemui")) {
//			findAndHookMethod("com.android.systemui.statusbar.policy.quicksetting.QuickSettingPanel", lpparam.classLoader, "setSingleLine", XC_MethodReplacement.DO_NOTHING);
			
			try {
				// Thanks to Pulser for the DisableBatteryFullAlert Xposed module 
				// https://github.com/pulser/xposedDisableBatteryFullAlert/blob/master/src/uk/co/villainrom/pulser/disablebatteryfullalert/DisableBatteryFullAlert.java#L18
	    		if(PreferencesHelper.NotificationBatteryFull) {
	    			findAndHookMethod("com.android.systemui.power.PowerUI", lpparam.classLoader, "notifyFullBatteryNotification", XC_MethodReplacement.DO_NOTHING);
	    		}
		    } catch (Exception e) { logError("SystemUI", "NotificationBatteryFull", e); }
		}
	};
	
	@Override // Change layouts
	public void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {
	    if (resparam.packageName.equals("com.zhixin.popupuireceiver")) {
	    	try {
	    		if(PreferencesHelper.PopupsUsbCover) {
				    resparam.res.hookLayout("com.zhixin.popupuireceiver", "layout", "usb_detached", new XC_LayoutInflated() {
						@Override
						public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {		        	
				        	TextView C = (TextView) liparam.view.findViewById(liparam.res.getIdentifier("water_damage_title", "id", "com.sec.android.app.popupuireceiver"));
				            ViewGroup parent = (LinearLayout) C.getParent();
				            parent.setVisibility(View.GONE);
				            log("PopupUiReceiver", "USB cover popup hidden");
						}
				    });
	    		}
		    } catch (Exception e) { logError("PopupUiReceiver", "Usb cover popup", e); }
	    	try {
	    		if(PreferencesHelper.PopupsBatteryCover) {
				    resparam.res.hookLayout("com.zhixin.popupuireceiver", "layout", "battery_cover", new XC_LayoutInflated() {
						@Override
						public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
				        	LinearLayout nCart = (LinearLayout) liparam.view.findViewById(liparam.res.getIdentifier("battery_cover_popup", "id", "com.sec.android.app.popupuireceiver"));
				        	nCart.setVisibility(View.GONE);
				            log("PopupUiReceiver", "Battery cover popup hidden");
						}
				    });
	    		}
		    } catch (Exception e) { logError("PopupUiReceiver", "Battery cover popup", e); }
	    }
	    if (resparam.packageName.equals("com.sec.android.app.popupuireceiver")) {
	    	try {
	    		if(PreferencesHelper.PopupsUsbCover) {
				    resparam.res.hookLayout("com.sec.android.app.popupuireceiver", "layout", "usb_detached", new XC_LayoutInflated() {
						@Override
						public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {		        	
				        	TextView C = (TextView) liparam.view.findViewById(liparam.res.getIdentifier("water_damage_title", "id", "com.sec.android.app.popupuireceiver"));
				            ViewGroup parent = (LinearLayout) C.getParent();
				            parent.setVisibility(View.GONE);
				            log("PopupUiReceiver", "USB cover popup hidden");
						}
				    });
	    		}
		    } catch (Exception e) { logError("PopupUiReceiver", "Usb cover popup", e); }
	    	try {
	    		if(PreferencesHelper.PopupsBatteryCover) {
				    resparam.res.hookLayout("com.sec.android.app.popupuireceiver", "layout", "battery_cover", new XC_LayoutInflated() {
						@Override
						public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
				        	LinearLayout nCart = (LinearLayout) liparam.view.findViewById(liparam.res.getIdentifier("battery_cover_popup", "id", "com.sec.android.app.popupuireceiver"));
				        	nCart.setVisibility(View.GONE);
				            log("PopupUiReceiver", "Battery cover popup hidden");
						}
				    });
	    		}
		    } catch (Exception e) { logError("PopupUiReceiver", "Battery cover popup", e); }
	    }
	    if (resparam.packageName.equals("com.android.systemui")) {
		    try {
		    	if(PreferencesHelper.SFinderTextField != "") {
		    		resparam.res.setReplacement("com.android.systemui", "string", "accessibility_sfinder_button", PreferencesHelper.SFinderTextField);	
		    	}
		    	if(PreferencesHelper.QConnectTextField != "") {
				    resparam.res.setReplacement("com.android.systemui", "string", "accessibility_qconnect_button", PreferencesHelper.QConnectTextField);	
		    	}
		    } catch (Exception e) { logError("SystemUI", "SFinder / QConnect textField", e); }
		    
		    resparam.res.setReplacement("com.android.systemui", "color", "toggle_slider_background_color", Color.parseColor("#005565"));	

		    // Testing (beta / alpha)
//		    resparam.res.hookLayout("com.android.systemui", "layout", "flip_settings", new XC_LayoutInflated() {
//				@Override
//		        public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
//					FrameLayout localViewGroup = (FrameLayout)liparam.view.findViewById(liparam.res.getIdentifier("quicksetting_scroller", "id", "com.android.systemui"));
////					ViewGroup.LayoutParams localLayoutParams1 = localViewGroup.getLayoutParams();
////					localLayoutParams1.height = 0;
//					
//					LinearLayout.LayoutParams SQButtonParams = new LinearLayout.LayoutParams(0, 0, 0);
//					SQButtonParams.setMargins(-30, -30, -30, -30);
//					localViewGroup.setLayoutParams(SQButtonParams);
//					localViewGroup.setVisibility(View.GONE);
////					FrameLayout.LayoutParams localLayoutParams1 = (LayoutParams) localViewGroup.getLayoutParams();
////					localLayoutParams1.height = 0;
//				}
//		    });
//		    resparam.res.hookLayout("com.android.systemui", "layout", "status_bar_expanded", new XC_LayoutInflated() {
//				@Override
//		        public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
//					ViewStub flipSettings = (ViewStub) liparam.view.findViewById(liparam.res.getIdentifier("flip_settings_stub", "id", "com.android.systemui"));
//					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, 0, 0);
//					params.setMargins(0, -3, 0, 0);
//					flipSettings.setLayoutParams(params);
//				}
//		    });
		    resparam.res.hookLayout("com.android.systemui", "layout", "status_bar_expanded_header", new XC_LayoutInflated() {
				@Override
				public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
					try {
						FrameLayout editButtonHolder = (FrameLayout) liparam.view.findViewById(liparam.res.getIdentifier("edit_button_holder", "id", "com.android.systemui"));
				    	android.view.ViewGroup.LayoutParams editButtonHolderParams = editButtonHolder.getLayoutParams();
				    	editButtonHolderParams.height = 0;
				    	editButtonHolder.setLayoutParams(editButtonHolderParams);
				    	
				    	ImageView expandedDivider3 = (ImageView) liparam.view.findViewById(liparam.res.getIdentifier("expanded_divider_3", "id", "com.android.systemui"));
				    	android.view.ViewGroup.LayoutParams expandedDivider3Params = expandedDivider3.getLayoutParams();
				    	expandedDivider3Params.height = 0;
				    	expandedDivider3.setLayoutParams(expandedDivider3Params);
				    	
//				    	FrameLayout settingsButtonHolder = (FrameLayout) liparam.view.findViewById(liparam.res.getIdentifier("settings_button_holder", "id", "com.android.systemui"));
//				    	android.view.ViewGroup.LayoutParams settingsButtonHolderParams = settingsButtonHolder.getLayoutParams();
//				    	settingsButtonHolderParams.height = 0;
//				    	settingsButtonHolder.setLayoutParams(settingsButtonHolderParams);
				    } catch (Exception e) { logError("SystemUI", "Statusbar icons (edit & toggles)", e); }
				}
		    });
		    
		    // Stable 
		    resparam.res.hookLayout("com.android.systemui", "layout", "status_bar", new XC_LayoutInflated() {
				@Override
				public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
					try {
						if(PreferencesHelper.StatusbarCarrier) {
							TextView carrierLabel = (TextView) liparam.view.findViewById(liparam.res.getIdentifier("carrierLabel", "id", "com.android.systemui"));
				        	carrierLabel.setVisibility(View.GONE);
				        	carrierLabel.setTextSize(0);
						}
				    } catch (Exception e) { logError("SystemUI", "StatusbarCarrier", e); }
				}
		    });
		    resparam.res.hookLayout("com.android.systemui", "layout", "status_bar_expanded", new XC_LayoutInflated() {
				@Override
		        public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {  
					
					try {
			    		if(PreferencesHelper.SQLayoutSFinder) {
			    			hideSQButton(liparam, "sfinder_button_big");
			    		}
			    		if(PreferencesHelper.SQLayoutQConnect) {
			    			hideSQButton(liparam, "qconnect_button_big");
			    		}
			    		if(PreferencesHelper.SQLayoutSFinder && PreferencesHelper.SQLayoutQConnect) {
			    			hideSQLayout(liparam);
			    		}
		    		} catch (Exception e) { logError("SystemUI", "SFinder / QConnect buttons and layout", e); }  	    
		    	    
		    		try {
			    		if(PreferencesHelper.NotificationbarHidden) {
				        	hideNotificationbar(liparam);
			    		}
		    		} catch (Exception e) { logError("SystemUI", "NotificationbarHidden", e); }   		
		    		
	
//		        	Button SFinderButton = (Button) liparam.view.findViewById(
//		        			liparam.res.getIdentifier("sfinder_button_big", "id", "com.android.systemui"));
//		        	SFinderButton.setOnTouchListener(new OnTouchListener() {
//						@Override
//						public boolean onTouch(View v, MotionEvent event) {
//							XposedBridge.log("XposedTouchwiz: SFinder button touched matey!!");
	//						ActivitySettings.launchNewActivityDetails("de.robv.android.xposed.installer");
	//						Intent LaunchIntent = Context.getPackageManager().getLaunchIntentForPackage("de.robv.android.xposed.installer");
	//						Intent LaunchIntent = new Intent(Context.getApplicationContext(), ActivitySettings.class);
	//						Context.startActivity(LaunchIntent);
//							BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();    
//							if (!mBluetoothAdapter.isEnabled()) {
//								mBluetoothAdapter.enable();
//							} else {
//							    mBluetoothAdapter.disable(); 
//							}
//							Intent i = new Intent(context, ActivitySettings.class);
//							context.startActivity(i);
//							Intent i = new Intent();
//					        i.setClassName("de.robv.android.xposed.installer", "de.robv.android.xposed.installer.XposedInstallerActivity");
//					        context.startActivity(i);
//							return false;
//						}
//		        	});
//		        	Button QconnectButton = (Button) liparam.view.findViewById(
//		        			liparam.res.getIdentifier("qconnect_button_big", "id", "com.android.systemui"));
//		        	QconnectButton.setOnTouchListener(new OnTouchListener() {
//						@Override
//						public boolean onTouch(View v, MotionEvent event) {
//							XposedBridge.log("XposedTouchwiz: QConnect button touched matey!");
//							return false;
//						}
//		        	});
		        }
		    });
		}
	}
	
	public void hideSQLayout(LayoutInflatedParam liparam) {
    	LinearLayout sqLayout = (LinearLayout) liparam.view.findViewById(liparam.res.getIdentifier("sfinder_qconnect_layout", "id", "com.android.systemui"));
		LinearLayout.LayoutParams sqParams = new LinearLayout.LayoutParams(0, 0, 0);
    	sqLayout.setLayoutParams(sqParams);

        log("SystemUI", "S-Finder & Q-Connect layout hidden");
	}
	public void hideSQButton(LayoutInflatedParam liparam, String key) {
		Button SQButton = (Button) liparam.view.findViewById(liparam.res.getIdentifier(key, "id", "com.android.systemui"));
		LinearLayout.LayoutParams SQButtonParams = new LinearLayout.LayoutParams(0, 0, 0);
		if(key == "sfinder_button_big") {
			SQButtonParams.setMargins(0, 0, -30, 0);
		} else if (key == "qconnect_button_big") {
			SQButtonParams.setMargins(-30, 0, 0, 0);
		} else {
			SQButtonParams.setMargins(0, 0, 0, 0);
		}
		SQButton.setLayoutParams(SQButtonParams);

        log("SystemUI", key + " hidden");
	}
	public void hideNotificationbar(LayoutInflatedParam liparam) {
		LinearLayout nCart = (LinearLayout) liparam.view.findViewById(liparam.res.getIdentifier("notificationCart", "id", "com.android.systemui"));
    	LinearLayout nTitle = (LinearLayout) liparam.view.findViewById(liparam.res.getIdentifier("noNotificationsTitle", "id", "com.android.systemui"));
    	android.view.ViewGroup.LayoutParams ncParams = nCart.getLayoutParams();
    	android.view.ViewGroup.LayoutParams ntParams = nTitle.getLayoutParams();
    	ncParams.height = 1;
    	ntParams.height = 1;
    	nCart.setLayoutParams(ncParams);
    	nTitle.setLayoutParams(ntParams);
    	
        log("SystemUI", "Notification bar hidden");
	}
}