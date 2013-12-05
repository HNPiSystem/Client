package com.huni.Fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class SecondFragmentAdapter extends FragmentAdapter{

	public SecondFragmentAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		Fragment fragment = new SettingFragment();
	
		return fragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub

		return 1;
	}

}
