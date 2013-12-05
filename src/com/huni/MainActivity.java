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
import com.huni.Fragment.SecondFragmentAdapter;
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

public class MainActivity extends FragmentActivity implements TabListener{


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

		ip = mPreference.getValue("ip", "");
		accessToken = mPreference.getValue("accessToken", "");
		Toast.makeText(getBaseContext(), accessToken, Toast.LENGTH_SHORT).show();

		mViewPager = (ViewPager)findViewById(R.id.viewpager1);

		setActionBar(deviceNum);
		setViewPager(deviceNum);

	}

	private void setActionBar(int menu)
	{
		//actionBar Setting method
		actionbar = getActionBar();
		if(menu == 0)
		{
			mTitles = getResources().getStringArray(R.array.tab);
		}
		else if(menu == 1)
		{
			mTitles = getResources().getStringArray(R.array.sensorTab);
		}
		actionbar.setTitle(mTitles[0]);
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS); 
		actionbar.setHomeButtonEnabled(true);
		actionbar.setIcon(R.drawable.ic_action_previous_item);

	}


	private void setViewPager(int menu)
	{
		//viewPager Setting method

		mFragmentManager = getSupportFragmentManager();

		if(menu == 0)
		{
			mFragmentPagerAdapter = new FragmentAdapter(mFragmentManager);
			mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
			{
				@Override
				public void onPageSelected(int position) {
					// TODO Auto-generated method stub
					actionbar.setSelectedNavigationItem(position);
					actionbar.setTitle(mTitles[position]);
				}
			});

		}
		else if (menu ==1)
		{
			mFragmentPagerAdapter = new SecondFragmentAdapter(mFragmentManager);
		}
		mViewPager.setAdapter(mFragmentPagerAdapter);

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
		//신고기능 112에 전화 
		AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
		ab.setMessage("112에 신고하시겠습니까?");
		ab.setPositiveButton("Yes", null);
		ab.setNegativeButton("No", null);
		ab.show();
		//		Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:112"));
		//		startActivity(intent);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		//intent된 후에 호출되는 메소드 
		super.onRestart();
		Toast.makeText(this, "재시작",Toast.LENGTH_SHORT).show();
	}

	class MovieStopTask extends AsyncTask<Object, String, JSONObject>{

		private Context mContext;
		private boolean started=true;
		private String JSON="";
		private ProgressDialog dialog;
		private JSONObject mJsonObject;


		public MovieStopTask(Context context)
		{
			mContext = context;
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			started = false;
			super.onCancelled();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			//작업을 시작전 하는 일 

			dialog = new ProgressDialog(mContext);
			dialog.setTitle("동영상 스트리밍 종료중");
			dialog.setMessage("Please wait while loading...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			dialog.show();


			super.onPreExecute();
		}

		@Override
		protected JSONObject doInBackground(Object... params) {
			// TODO Auto-generated method stub

			try {
				DefaultHttpClient mhttpClient =new DefaultHttpClient();
				HttpGet mHttpGet =new HttpGet("http://192.168.0.4:5000/askfor?accessToken=accessToken&order=movie");
				//종료하는 url에 접속하게 된다.
				//HttpGet mHttpGet =new HttpGet("http://" + "192.168.0.4" + ":5000/askfor?accessToken=accessToken&order=movie/");
				ArrayList<NameValuePair> nameValuePairs =new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("order","picture"));	//전달할 인자들 설정
				HttpResponse mhttpHttpResponse = mhttpClient.execute(mHttpGet);

				HttpEntity mHttpEntity =mhttpHttpResponse.getEntity();
				if(mHttpEntity!=null)
				{
					JSON = EntityUtils.toString(mHttpEntity);
					Log.i("RESPONSE", JSON);

				}

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				mJsonObject = new JSONObject(JSON);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return mJsonObject;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			//작업 완료후 하는 일;

			String url="";
			try {
				url = result.getString("result");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dialog.dismiss();

			super.onPostExecute(result);
		}
	}

}
