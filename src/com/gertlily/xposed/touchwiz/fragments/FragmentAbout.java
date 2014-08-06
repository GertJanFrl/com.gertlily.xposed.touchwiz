package com.gertlily.xposed.touchwiz.fragments;

import com.gertlily.xposed.touchwiz.R;
import com.android.vending.billing.util.IabHelper;
import com.android.vending.billing.util.IabResult;
import com.android.vending.billing.util.Purchase;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentAbout extends Fragment {

	IabHelper mHelper;
	static final String ITEM_SKU = "android.test.purchased";

	private Button clickButton;
	private Button buyButton;

	public FragmentAbout(){}
		
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        getActivity().setTitle(R.string.app_name);

		buyButton = (Button) rootView.findViewById(R.id.buyButton);
		clickButton = (Button) rootView.findViewById(R.id.clickButton);	
		clickButton.setEnabled(false);
		
		buyButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				buyClick(getActivity());
			}
		});
		
		clickButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				buttonClicked(getActivity());
			}
		});
	
		String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgqo5jchvlzHsld8dxGPAY4UzbgDx/wHN2yoG3hEnhdBCteaRf3fPBrKOAmPpj9axT80c0zxiuNxKnbwK2SQR6e2dtl9E2EMMRIJXQEu7I87aGLTKXQRdHo2GMhOnEnfkh7XvIgkN8kTLuZCEhNTWXy1XWF/AdjRR5Vb7eRTdjwaUYF4G6yEdCScsOcr216nmFGIptmSfVB4kaB33qykbUT8qOHgBhkSr9hL+Sm9q9Mt2PWhjEG0hLFkHsKmKXJ0+Ll6zqwNqQgKMG0LNLNYTg4mOQFdQOMfosF12UH5v4lVrFbLwwX8HI4BuS0sPTW6LucCjxA2JxXWR2fthjj0uGQIDAQAB";
        
    	mHelper = new IabHelper(getActivity(), base64EncodedPublicKey);
    
    	mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				if (!result.isSuccess()) {
//						Log.d(TAG, "In-app Billing setup failed: " + result);
				} else {             
//						Log.d(TAG, "In-app Billing is set up OK");
				}
			}
		});
		
        try {
			String packageName = getActivity().getPackageName();
			String version = getActivity().getPackageManager().getPackageInfo(packageName, 0).versionName;
			((TextView) rootView.findViewById(R.id.version)).setText(version);
		} catch (NameNotFoundException e) { }
		
		((TextView) rootView.findViewById(R.id.developedby)).setMovementMethod(LinkMovementMethod.getInstance());
		((TextView) rootView.findViewById(R.id.support)).setMovementMethod(LinkMovementMethod.getInstance());

		String translatedby = getResources().getString(R.string.about_translatedby);
		if (translatedby.isEmpty()) {
			rootView.findViewById(R.id.translatedby_title).setVisibility(View.GONE);
			rootView.findViewById(R.id.translatedby).setVisibility(View.GONE);
			rootView.findViewById(R.id.translatedby_break).setVisibility(View.GONE);
		} else {
			((TextView) rootView.findViewById(R.id.translatedby)).setMovementMethod(LinkMovementMethod.getInstance());
		}
		
        return rootView;
    }
	
	public void buttonClicked(Activity activity) {
		clickButton.setEnabled(false);
		buyButton.setEnabled(true);
	}
	
	public void buyClick(Activity activity) {
	     mHelper.launchPurchaseFlow(getActivity(), "", 10001, mPurchaseFinishedListener);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {     
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
//			if (result.isFailure()) {
				// Handle error
				Toast.makeText(getActivity(), "A error  - " + result + " : " + purchase, Toast.LENGTH_SHORT).show();
//				return;
//			} else if (purchase.getSku().equals(ITEM_SKU)) {
				buyButton.setEnabled(false);
//			}   
		}
	};
}
