package com.huni.data;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class BaseActivity extends Activity{


	private static Typeface mTypeface;
	private static Typeface mBoldTypeface;


	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);

		if (BaseActivity.mTypeface == null)
			BaseActivity.mTypeface = Typeface.createFromAsset(getAssets(), "Dosis-Medium.ttf");

		if (BaseActivity.mBoldTypeface == null)
			BaseActivity.mBoldTypeface = Typeface.createFromAsset(getAssets(), "Dosis-Bold.ttf");

		ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
		setGlobalFont(root);
		
	}


	void setGlobalFont(ViewGroup root) {
		for (int i = 0; i < root.getChildCount(); i++) {
			View child = root.getChildAt(i);
			if (child instanceof TextView){
				((TextView)child).setTypeface(mTypeface);
				//((TextView)child).setTypeface(mBoldTypeface,Typeface.BOLD);
			}
			
			else if (child instanceof ViewGroup)
				setGlobalFont((ViewGroup)child);
		}
	}

}
