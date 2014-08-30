package com.gertlily.xposed.touchwiz.xposed;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import android.graphics.Color;
import android.widget.TextView;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class PhoneStatusBar implements IXposedHookLoadPackage {
//    private static final String CLASS_PHONE_STATUSBAR = "com.android.systemui.statusbar.phone.PhoneStatusBar";
//    private static TextView[] mCarrierTextView;
//    
//	public static void init(final XSharedPreferences prefs, final ClassLoader lpparam) {
//		final Class<?> phoneStatusBarClass = XposedHelpers.findClass(CLASS_PHONE_STATUSBAR, lpparam);
//		
//        XposedHelpers.findAndHookMethod(phoneStatusBarClass, "makeStatusBarView", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                Object carrierTextView = XposedHelpers.getObjectField(param.thisObject, "mCarrierLabel");
//                if (carrierTextView instanceof TextView[]) {
//                    if (((TextView[])carrierTextView).length > 0) {
//                        mCarrierTextView = new TextView[] {
//                                (TextView) ((TextView[])carrierTextView)[0]
//                        };
//                    }
//                } else if (carrierTextView instanceof TextView) {
//                    mCarrierTextView = new TextView[] {
//                            (TextView) carrierTextView
//                    };
//                }
//                mCarrierTextView[0].setText("Gertlily");
//            }
//        });
//	}
	
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
//        if (!lpparam.packageName.equals("com.android.systemui"))
//            return;
//
//        findAndHookMethod("com.android.systemui.statusbar.policy.Clock", lpparam.classLoader, "updateClock", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                TextView tv = (TextView) param.thisObject;
//                String text = tv.getText().toString();
//                tv.setText(text + " :)");
//                tv.setTextColor(Color.RED);
//            }
//        });
    }
}
