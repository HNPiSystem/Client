package com.huni;

import com.huni.data.SharedPreference;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

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

		mActionBar = getActionBar();
		mActionBar.setTitle("제어할 기기 선택");
		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setIcon(R.drawable.ic_action_previous_item);

		mRadioGroup = (RadioGroup)findViewById(R.id.device);
		mButton = (Button)findViewById(R.id.choiceBtn);

		mPreference = new SharedPreference(this);
		accessToken= mPreference.getValue("accessToken", "");

		
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {


			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.camera:
					deviceNum = 0;
					break;
				case R.id.sensor:
					deviceNum = 1;
					break;
				default:
					break;
				}
			}
		});

		mButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(v.getId() == R.id.choiceBtn)
				{
					Intent intent = new Intent(SelectActivity.this,MainActivity.class);
					mPreference.put("device", deviceNum);
					intent.putExtra("device", deviceNum);
					startActivity(intent);
				}
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
