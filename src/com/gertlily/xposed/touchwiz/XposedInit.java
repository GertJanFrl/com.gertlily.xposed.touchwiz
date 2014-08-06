package com.gertlily.xposed.touchwiz;

import com.gertlily.xposed.touchwiz.xposed.PhoneStatusBar;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import com.gertlily.xposed.touchwiz.hooks.Common;

public class XposedInit implements IXposedHookZygoteInit, IXposedHookInitPackageResources, IXposedHookLoadPackage {
	private static XSharedPreferences prefs;
    
	@Override
	public void initZygote(StartupParam startupParam) throws Throwable {
		prefs = new XSharedPreferences(Common.PACKAGE_NAME);
		prefs.makeWorldReadable();
	}

	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		if (lpparam.packageName.equals("com.android.systemui")) {
			PhoneStatusBar.init(prefs, lpparam.classLoader);
		}
	}

	@Override
	public void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {
		
	}
}
