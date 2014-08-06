package com.gertlily.xposed.touchwiz.model;

public class NavDrawerItem {
	
	private String title;
	private String count = "0";
	private boolean isCounterVisible = false; // boolean to set visiblity of the counter
	
	public NavDrawerItem(){}

	public NavDrawerItem(String title){
		this.title = title;
	}
	
	public NavDrawerItem(String title, boolean isCounterVisible, String count){
		this.title = title;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public String getCount(){
		return this.count;
	}
	
	public boolean getCounterVisibility(){
		return this.isCounterVisible;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setCount(String count){
		this.count = count;
	}
	
	public void setCounterVisibility(boolean isCounterVisible){
		this.isCounterVisible = isCounterVisible;
	}
}
