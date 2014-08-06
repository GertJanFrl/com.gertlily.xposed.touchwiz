package com.gertlily.xposed.touchwiz.hooks;

import de.robv.android.xposed.XSharedPreferences;

public class PreferencesHelper {
	public static XSharedPreferences prefs 			= new XSharedPreferences(Common.PACKAGE_NAME);
	public static boolean Debug 					= prefs.getBoolean("debug", true);

	public static boolean SQLayoutSFinder 			= prefs.getBoolean("sqlayoutsfinder", false);  
	public static String  SFinderTextField 			= prefs.getString("sfindertextfield", "S-Finder"); 
	
	public static boolean SQLayoutQConnect 			= prefs.getBoolean("sqlayoutqconnect", false);  
	public static String  QConnectTextField 		= prefs.getString("qconnecttextfield", "Quick Connect"); 

	public static boolean NotificationbarHidden 	= prefs.getBoolean("notificationbarhidden", false);
	public static boolean NotificationBatteryFull 	= prefs.getBoolean("notificationbatteryfull", false);
	
	public static boolean StatusbarCarrier			= prefs.getBoolean("statusbarcarrierhidden", false);
	
	public static boolean PopupsUsbCover 			= prefs.getBoolean("popupsusbcoverhidden", false);  
	public static boolean PopupsBatteryCover 		= prefs.getBoolean("popupsbatterycoverhidden", false);  
}