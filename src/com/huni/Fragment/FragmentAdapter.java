package com.huni.Fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter{

	private int deviceNum;
	public FragmentAdapter(FragmentManager fm, int device) {
		super(fm);
		deviceNum = device;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		Fragment fragment = null;


		switch(deviceNum)
		{
		case 0: fragment = new MovieFragment(); break;
		case 1: fragment = new TempFragment(); break;
		case 2: fragment = new LightFragment(); break;
		}


		return fragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub

		return 1;
	}

}
