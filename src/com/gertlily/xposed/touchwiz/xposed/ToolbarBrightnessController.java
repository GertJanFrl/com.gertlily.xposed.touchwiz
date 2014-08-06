package com.gertlily.xposed.touchwiz.xposed;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import com.gertlily.xposed.touchwiz.R;
import com.gertlily.xposed.touchwiz.hooks.Common;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.database.ContentObserver;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import de.robv.android.xposed.XposedBridge;

public class ToolbarBrightnessController extends LinearLayout implements android.widget.CompoundButton.OnCheckedChangeListener, android.widget.SeekBar.OnSeekBarChangeListener{
	public static final String STATE_CHANGE = ToolbarBrightnessController.class.getName() + ".intent.action.STATE_CHANGE";
	public static final String STATE_EXTRA_SHOW_ICON = ToolbarBrightnessController.class.getName() + ".intent.extra.STATE_EXTRA_SHOW_ICON";
	private Context mContext;
	private int mCurrentState;
	private boolean mAutomatic;
	private boolean mShowAutoCheckBox = true;
	private int mBrightness;
	private int brightnessMode;
	ArrayList<Integer> brightnessArray = new ArrayList<Integer>();
	
	private SharedPreferences prefs;
	private ImageView icon;
	private CheckBox mAutoCheckBox;
	private Animation inAnimation;
	private Animation outAnimation;
	private SeekBar mSeekBar;
	
	protected SettingsContentObserver mSettingsContentObserver;
	
	private boolean isAttached;
	
	private Object mIPowerManager;
	private Method mSetBacklightBrightness;
	
	public ToolbarBrightnessController(Context context, AttributeSet attribute) {
		super(context, attribute);
		View.inflate(context, R.layout.toolbar_brightnessbar, this);
		mContext = context;
		mSettingsContentObserver = new SettingsContentObserver(new Handler());
		mCurrentState = getState();
		init();
		initOption();
	}

	public void init() {
		icon = (ImageView) findViewById(R.id.icon);
		icon.setOnClickListener(brightnessIconListener);
		try {			
			mAutomatic = android.provider.Settings.System.getInt(mContext.getContentResolver(), "screen_brightness_mode") == 1;
			mBrightness = android.provider.Settings.System.getInt(mContext.getContentResolver(), "screen_brightness", 80);
		} catch (android.provider.Settings.SettingNotFoundException e) {
			e.printStackTrace();
		}
		icon.setImageResource(mAutomatic ? R.drawable.ic_qs_brightness_auto_on : R.drawable.ic_qs_brightness_auto_off);
		mSeekBar = (SeekBar) findViewById(R.id.seekbar);
		mSeekBar.setMax(255);
		mSeekBar.setOnSeekBarChangeListener(this);
		reDrawSeekBar(mSeekBar);
		mSeekBar.setProgress(mBrightness);
		
		mAutoCheckBox = (CheckBox) findViewById(R.id.automatic_mode);
		mAutoCheckBox.setChecked(mAutomatic);
		mAutoCheckBox.setOnCheckedChangeListener(this);		
		
		brightnessArray.add(0);
		brightnessArray.add(0);
	}
	
	public void initOption(){
		try {
			Context regxmContext = mContext.createPackageContext(Common.PACKAGE_NAME, 3);
			if(regxmContext != null){
				prefs = PreferenceManager.getDefaultSharedPreferences(regxmContext);
				mShowAutoCheckBox = prefs.getBoolean(regxmContext.getString(R.string.show_auto_brightness_checkbox_key), true);
			}
		} catch (NameNotFoundException e) {
			XposedBridge.log(e);
		}
		
		inAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_right);
		inAnimation.setFillAfter(true);
		inAnimation.setAnimationListener(mAnimationListener);
		mAutoCheckBox.setAnimation(inAnimation);
		outAnimation = AnimationUtils.loadAnimation(mContext, R.anim.slide_out_left);
		outAnimation.setFillAfter(true);
		outAnimation.setAnimationListener(mAnimationListener);
		mAutoCheckBox.setAnimation(outAnimation);
		mAutoCheckBox.setVisibility(mShowAutoCheckBox ? View.VISIBLE : View.GONE);
	}
	
	private AnimationListener mAnimationListener = new AnimationListener(){
		@Override
		public void onAnimationEnd(Animation arg0) {
			setAutoCheckVisibility(mShowAutoCheckBox);
		}
		@Override
		public void onAnimationRepeat(Animation arg0) { }
		@Override
		public void onAnimationStart(Animation arg0) {
			prefs.edit().putBoolean(mContext.getString(R.string.show_auto_brightness_checkbox_key), !mShowAutoCheckBox).commit();
			mShowAutoCheckBox = prefs.getBoolean(mContext.getString(R.string.show_auto_brightness_checkbox_key), true);
		}
	};
	
	private OnClickListener brightnessIconListener = new OnClickListener() {		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.icon:
				mAutoCheckBox.startAnimation(mShowAutoCheckBox ? outAnimation : inAnimation);
				break;
			}
		}
	};
	
	private void setAutoCheckVisibility(boolean visibility){
		if(mAutoCheckBox != null){
			mAutoCheckBox.clearAnimation();
			mAutoCheckBox.setVisibility(visibility ? View.VISIBLE : View.GONE);
			mAutoCheckBox.invalidate();
			mAutoCheckBox.postInvalidate();
			mAutoCheckBox.requestLayout();

			mSeekBar.setVisibility(View.GONE);
			mSeekBar.destroyDrawingCache();
			mSeekBar.setVisibility(View.VISIBLE);
			mSeekBar.invalidate();
			mSeekBar.postInvalidate();
			mSeekBar.requestLayout();
			
			this.destroyDrawingCache();
			this.invalidate();
			this.postInvalidate();
		}
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		if(mSettingsContentObserver != null){
			mSettingsContentObserver.observe();
			mCurrentState = getState();
			reDrawIcon(icon, mCurrentState);
		}
		if(mIPowerManager == null)
			mIPowerManager = makeIPowerManager();
		if(!isAttached){
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(STATE_CHANGE);
			mContext.registerReceiver(toolBarBrightnessControllerReceiver, intentFilter);
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mSettingsContentObserver != null){
			mSettingsContentObserver.unobserve();
			mIPowerManager = null;
		}
		if(isAttached)
			mContext.unregisterReceiver(toolBarBrightnessControllerReceiver);
	}
	
	@Override
	public void onCheckedChanged(CompoundButton view, boolean flag) {
		mAutomatic = flag;
		icon.setImageResource(mAutomatic ? R.drawable.ic_qs_brightness_auto_on : R.drawable.ic_qs_brightness_auto_off);
		reDrawIcon(icon, mCurrentState);
		int i = mAutomatic ? 1 : 0 ;
		try{
			android.provider.Settings.System.putInt(mContext.getContentResolver(), "screen_brightness_mode", i);
		}catch(SecurityException e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
		if(fromUser){
			android.provider.Settings.System.putInt(mContext.getContentResolver(), "screen_brightness", progress);
			mBrightness = progress;
			setBrightness(mBrightness);
			int mState = getState();
			if (mState != mCurrentState) {
				mCurrentState = mState;
				reDrawIcon(icon, mCurrentState);
			}
		}		
	}
	
	private Object makeIPowerManager(){
		try {
			Class<?> mServiceManagerClass = Class.forName("android.os.ServiceManager");
			Class<?> mIPowerManagerClass = Class.forName("android.os.IPowerManager");
			Class<?> mIPowerManagerStubClass = Class.forName("android.os.IPowerManager$Stub");
						
			Method mGetService = mServiceManagerClass.getMethod("getService", new Class[]{String.class});
			Method mAsInterface = mIPowerManagerStubClass.getMethod("asInterface", new Class[]{IBinder.class});
			try{
				mSetBacklightBrightness = mIPowerManagerClass.getMethod("setBacklightBrightness", new Class[]{int.class});
			}catch(NoSuchMethodException e){
				mSetBacklightBrightness = mIPowerManagerClass.getMethod("setTemporaryScreenBrightnessSettingOverride", new Class[]{int.class});
			}			
			
			IBinder power = (IBinder) mGetService.invoke(null, "power");
			return mAsInterface.invoke(null, power);	
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void setBrightness(int brightness){
		try {
			if(mIPowerManager != null && mSetBacklightBrightness != null){
				mSetBacklightBrightness.invoke(mIPowerManager, brightness);
			}else{
				Intent intent = new Intent();
				intent.setAction(SilentBrightnessDialog.SILENTBRIGHTNESSDIALOG_STATE_CHANGE);
				intent.putExtra(SilentBrightnessDialog.SILENTBRIGHTNESSDIALOG_STATE_EXTRA_BRIGHTNESS, brightness);
				mContext.sendBroadcast(intent);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	private int getState() {
		return android.provider.Settings.System.getInt(mContext.getContentResolver(), "screen_brightness", 80) <= 150 ? 0 : 1;
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		try {
			brightnessMode = Settings.System.getInt(mContext.getContentResolver(),Settings.System.SCREEN_BRIGHTNESS_MODE);
			android.provider.Settings.System.putInt(mContext.getContentResolver(),Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}
		if(mIPowerManager == null){
			Intent intent = new Intent(mContext, SilentBrightnessDialog.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(intent);
		}
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		android.provider.Settings.System.putInt(mContext.getContentResolver(),Settings.System.SCREEN_BRIGHTNESS_MODE, brightnessMode);
		if(mIPowerManager == null){
			Intent intent = new Intent();
			intent.setAction(SilentBrightnessDialog.SILENTBRIGHTNESSDIALOG_STATE_FINISH);
			mContext.sendBroadcast(intent);
		}
	}
	
	private void reDrawSeekBar(SeekBar seekBar){
		if(mContext == null) return;
		Drawable particle = seekBar.getProgressDrawable();
		particle.clearColorFilter();
		try{
			particle.setColorFilter(android.R.color.darker_gray, Mode.SRC_ATOP);
		}catch(NotFoundException e){
			particle.setColorFilter(Color.WHITE, Mode.SRC_ATOP);
		}
		ClipDrawable progress = new ClipDrawable(particle, Gravity.LEFT, ClipDrawable.HORIZONTAL);
		seekBar.setProgressDrawable(null);
		seekBar.setProgressDrawable(progress);
	}
	
	private void reDrawIcon(ImageView icon, int state){
		if(mContext == null | icon == null) return;
		Drawable mDrawable = icon.getDrawable();
		mDrawable.clearColorFilter();
		if(state <= 0) return;
		try{					
			mDrawable.setColorFilter(android.R.color.darker_gray, Mode.SRC_ATOP);
		}catch(NotFoundException e){
			mDrawable.setColorFilter(Color.WHITE, Mode.SRC_ATOP);
		}
		icon.setImageDrawable(null);
		icon.setImageDrawable(mDrawable);
	}

	public class SettingsContentObserver extends ContentObserver {
		public SettingsContentObserver(Handler handler) {
			super(handler);
		}
		@Override
		public boolean deliverSelfNotifications() {
			return super.deliverSelfNotifications();
		}
		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			if(!selfChange){
				try {					
					mAutomatic = android.provider.Settings.System.getInt(mContext.getContentResolver(), "screen_brightness_mode") == 1;
					mBrightness = android.provider.Settings.System.getInt(mContext.getContentResolver(), "screen_brightness", 80);
					
					int mState = getState();
					icon.setImageResource(mAutomatic ? R.drawable.ic_qs_brightness_auto_on : R.drawable.ic_qs_brightness_auto_off);
					reDrawIcon(icon, mState);
					
					brightnessArray.add(mBrightness);
					if(mBrightness == 0 && brightnessArray.size() > 1){
						mBrightness = brightnessArray.get(brightnessArray.size()-2);
						android.provider.Settings.System.putInt(mContext.getContentResolver(), "screen_brightness", mBrightness);
					}
					
					mSeekBar.setProgress(mBrightness);
					mAutoCheckBox.setChecked(mAutomatic);					
					if(brightnessArray.size() > 2){
						for(int i = 0; i < brightnessArray.size()-2; i++){
							brightnessArray.remove(i);
						}
					}
				} catch (android.provider.Settings.SettingNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		
		public void observe() {
			ContentResolver contentresolver = mContext.getContentResolver();
			contentresolver.registerContentObserver(android.provider.Settings.System.getUriFor("screen_brightness"), false, this);
			contentresolver.registerContentObserver(android.provider.Settings.System.getUriFor("screen_brightness_mode"), false, this);
		}
		
		public void unobserve() {
			mContext.getContentResolver().unregisterContentObserver(this);
		}
	}
	
	private BroadcastReceiver toolBarBrightnessControllerReceiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(STATE_CHANGE)){
				mShowAutoCheckBox = intent.getBooleanExtra(STATE_EXTRA_SHOW_ICON, true);
			}
		}		
	};
}