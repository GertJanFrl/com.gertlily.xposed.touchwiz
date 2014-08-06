package com.gertlily.xposed.touchwiz.xposed;

import java.math.BigDecimal;
import java.math.RoundingMode;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SilentBrightnessDialog extends Activity {
	public static final String SILENTBRIGHTNESSDIALOG_STATE_CHANGE = SilentBrightnessDialog.class.getName() + ".intent.action.STATE_CHANGE";
	public static final String SILENTBRIGHTNESSDIALOG_STATE_FINISH = SilentBrightnessDialog.class.getName() + ".intent.action.STATE_FINISH";
	public static final String SILENTBRIGHTNESSDIALOG_STATE_EXTRA_BRIGHTNESS = SilentBrightnessDialog.class.getName() + ".intent.action.STATE_EXTRA_BRIGHTNESS";
	private Window window;
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		try{
			Activity parent = (Activity) this;
		    window = parent.getWindow();
		}catch(ClassCastException e){
			e.printStackTrace();
		}
	}
	
	private void setBrightness(int i) {
		if(window != null){
			WindowManager.LayoutParams params = window.getAttributes();
			float brightnessF = (float) ((i * 100 / 255) / 100.0);
			BigDecimal brightnessBd = new BigDecimal(brightnessF).setScale(1, RoundingMode.HALF_EVEN);
			float setVal = brightnessBd.floatValue();
			if(setVal <= 0.0f){
				setVal = 0.1f;
			}
			params.screenBrightness = setVal;
			window.setAttributes(params);			
		}
	}
	
	BroadcastReceiver receiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(SILENTBRIGHTNESSDIALOG_STATE_CHANGE)){
				int brightness = intent.getIntExtra(SILENTBRIGHTNESSDIALOG_STATE_EXTRA_BRIGHTNESS, 0);
				setBrightness(brightness);
			}else if(intent.getAction().equals(SILENTBRIGHTNESSDIALOG_STATE_FINISH)){
				SilentBrightnessDialog.this.finish();	
			}
		}		
	};
	
	@Override
	protected void onStart() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(SILENTBRIGHTNESSDIALOG_STATE_CHANGE);
		filter.addAction(SILENTBRIGHTNESSDIALOG_STATE_FINISH);
		mContext.registerReceiver(receiver, filter);
		super.onStart();
	}

	@Override
	protected void onStop() {
		mContext.unregisterReceiver(receiver);
		super.onStop();
	}
}