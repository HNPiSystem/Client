package com.huni;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.huni.Fragment.FragmentAdapter;

import com.huni.data.SharedPreference;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {


	private ViewPager mViewPager;
	private FragmentPagerAdapter mFragmentPagerAdapter;
	private FragmentManager mFragmentManager;	
	private String []mTitles;
	private ActionBar actionbar = null;
	private SharedPreference mPreference;
	private String accessToken = "";
	private String ip = "";
	private int deviceNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mPreference = new SharedPreference(this);

		Intent intent = getIntent();
		deviceNum = intent.getIntExtra("device", 0);

		ip = mPreference.getValue(SharedPreference.ip, ""); //접속시 사용했던 ip 가져오기 	
		accessToken = mPreference.getValue(SharedPreference.accessToken, "");//accessToken 가져오기 
		Toast.makeText(getBaseContext(), accessToken, Toast.LENGTH_SHORT).show();

		mViewPager = (ViewPager)findViewById(R.id.viewpager1);

		setActionBar(deviceNum);
		setViewPager(deviceNum);

	}

	private void setActionBar(int menu)
	{
		//actionBar Setting method
		actionbar = getActionBar();
		mTitles = getResources().getStringArray(R.array.tab);
		actionbar.setTitle(mTitles[menu]);
		actionbar.setHomeButtonEnabled(true);
		actionbar.setIcon(R.drawable.btn_back);

	}


	private void setViewPager(int menu)
	{
		//viewPager Setting method
		mFragmentManager = getSupportFragmentManager();
		mFragmentPagerAdapter = new FragmentAdapter(mFragmentManager,deviceNum);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
		{
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub

				actionbar.setTitle(mTitles[position]);
			}
		});
		mViewPager.setAdapter(mFragmentPagerAdapter);
		
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


	private void notice()
	{
		//신고기능 112에 전화 
		AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
		ab.setMessage("112에 신고하시겠습니까?");
		ab.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:112"));
				startActivity(intent);
			}
		});
		ab.setNegativeButton("No", null);
		ab.show();

	}
}
