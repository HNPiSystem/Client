package com.huni;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SplashActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);
		
		Handler mHandler = new Handler()
		{
			public void handleMessage(Message msg)
			{
				finish();
			}
		};
		
		mHandler.sendEmptyMessageDelayed(0,1700);
	}
	
	

}
