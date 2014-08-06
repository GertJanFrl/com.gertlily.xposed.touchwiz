package com.gertlily.xposed.touchwiz;

import com.gertlily.xposed.touchwiz.R;

import java.util.ArrayList;

import com.gertlily.xposed.touchwiz.adapter.NavDrawerListAdapter;
import com.gertlily.xposed.touchwiz.fragments.FragmentChangelog;
import com.gertlily.xposed.touchwiz.fragments.FragmentGeneral;
import com.gertlily.xposed.touchwiz.fragments.FragmentSFinderQConnect;
import com.gertlily.xposed.touchwiz.fragments.FragmentStatusbar;
import com.gertlily.xposed.touchwiz.fragments.FragmentPopups;
import com.gertlily.xposed.touchwiz.fragments.FragmentAbout;
import com.gertlily.xposed.touchwiz.model.NavDrawerItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
//import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] navMenuTitles;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        boolean installed = appInstalledOrNot("de.robv.android.xposed.installer");  
        if(!installed) {
        	new AlertDialog.Builder(this)
	    		.setTitle(R.string.general_alert_xposed_title)
	    		.setMessage(R.string.general_alert_xposed)
	    		.setIconAttribute(android.R.attr.alertDialogIcon)
	    		.setPositiveButton(R.string.general_alert_download, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						Uri uri = Uri.parse("http://dl.xposed.info/latest.apk");
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(intent);
						finish();
					}
				  })
	    		.setNegativeButton(R.string.general_alert_exit, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						finish();
					}
				  })
				.setCancelable(false)
	    		.create()
	    		.show();
        }
        
		setContentView(R.layout.activity_main);

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0])); // General
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1])); // S-Finder & Q-Connect
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2])); // Notification Bar
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3])); // Popups
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4])); // Changelog
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5])); // About

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		if (savedInstanceState == null) {
			displayView(0);
		}
	}

	/*
	 * Check if Xposed Framework is installed or not;
	 * Return the value
	 */
    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	/*
	 * Settings (three-dots)
	 */
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//
//	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
//		// Handle action bar actions click
//		switch (item.getItemId()) {
//		case R.id.action_settings:
//			return true;
//		default:
//			return super.onOptionsItemSelected(item);
//		}
		return false;
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
//	@Override
//	public boolean onPrepareOptionsMenu(Menu menu) {
//		// if nav drawer is opened, hide the action items
//		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
//		return super.onPrepareOptionsMenu(menu);
//	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new FragmentGeneral();
			break;
		case 1:
			fragment = new FragmentSFinderQConnect();
			break;
		case 2:
			fragment = new FragmentStatusbar();
			break;
		case 3:
			fragment = new FragmentPopups();
			break;
		case 4:
			fragment = new FragmentChangelog();
			break;
		case 5:
			fragment = new FragmentAbout();
			break;

		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

}
