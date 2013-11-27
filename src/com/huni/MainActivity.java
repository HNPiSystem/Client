package com.huni;

import com.huni.Fragment.FragmentAdapter;
import com.huni.data.SharedPreference;

import android.net.Uri;
import android.os.Bundle;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements TabListener{


	private ViewPager mViewPager;
	private FragmentPagerAdapter mFragmentPagerAdapter;
	private FragmentManager mFragmentManager;	
	private String []mTitles;
	private ActionBar actionbar = null;
	private SharedPreference mPreference;
	private String accessToken = "";
	private String ip = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Intent intent= getIntent();
		//accessToken = intent.getExtras().getString("accessToken"); //accessToken을 전달받아 저장한다.
		//ip = intent.getExtras().getString("ip"); //연결한 ip주소를 받아서 저장
		
		mPreference = new SharedPreference(this);
		
		mPreference.getValue("ip", ip);
		mPreference.getValue("accessToken", accessToken);

		mViewPager = (ViewPager)findViewById(R.id.viewpager1);

		mFragmentManager = getSupportFragmentManager();
		mFragmentPagerAdapter = new FragmentAdapter(mFragmentManager);

		mViewPager.setAdapter(mFragmentPagerAdapter);

		actionbar = getActionBar();
		
		mTitles = getResources().getStringArray(R.array.tab);
		actionbar.setTitle(mTitles[0]);
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS); 
		actionbar.setHomeButtonEnabled(true);
		actionbar.setIcon(R.drawable.ic_action_previous_item);
	
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
		{
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				actionbar.setSelectedNavigationItem(position);
				actionbar.setTitle(mTitles[position]);
			}
		});
		makeTabmenu();
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    if (item.getItemId() == android.R.id.home) {
	        finish();
	        return true;
	    }
	    else if(item.getItemId() == R.id.alert)
	    {
	    	notice();
	    }
	    return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		mViewPager.setCurrentItem(tab.getPosition());		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}
	private void makeTabmenu()
	{
		for (int i = 0; i < mTitles.length; i++) {
			actionbar.addTab(
					actionbar.newTab()
					.setText(mTitles[i])
					.setTabListener(this));
		}
	}
	
	private void notice()
	{
		//신고기능
		Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:112"));
		startActivity(intent);
	}

}
