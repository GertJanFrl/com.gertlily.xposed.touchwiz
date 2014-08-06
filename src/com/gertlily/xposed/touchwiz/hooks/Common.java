package com.gertlily.xposed.touchwiz.hooks;

public final class Common {
	public static final String PACKAGE_NAME 				= Common.class.getPackage().getName().replace(".hooks", "");
	
	// the name of the settings file
	public static final String PREFERENCES_NAME 			= Common.PACKAGE_NAME + "_preferences";

	// For the restart buttons - Static packages
	public static final String PACKAGE_SYSTEMUI 			= "com.android.systemui";
	public static final String PACKAGE_POPUPUIRECEIVER 		= "com.sec.android.app.popupuireceiver";
}