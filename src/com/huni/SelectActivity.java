package com.huni;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.huni.data.ImageAdapter;
import com.huni.data.SharedPreference;

public class SelectActivity extends Activity{

	private ActionBar mActionBar;
	private RadioGroup mRadioGroup;
	private Button mButton;
	private int deviceNum = 0;
	private SharedPreference mPreference;
	private String accessToken;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_layout);
		
		ArrayList<String> tempList = (ArrayList<String>)getIntent().getStringArrayListExtra("devices");
		GridView mGridView = (GridView)findViewById(R.id.select_gridview);
		
		mGridView.setAdapter(new ImageAdapter(this,tempList));
		mActionBar = getActionBar();
		mActionBar.setTitle("Selct Device");
		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setIcon(R.drawable.btn_back);
		
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Log.d("position", ""+position);
				Intent intent = new Intent(SelectActivity.this,MainActivity.class);
				intent.putExtra("device", position);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    if (item.getItemId() == android.R.id.home) {
	        finish();
	        return true;
	    }
	    
	    return super.onOptionsItemSelected(item);
	}


}
