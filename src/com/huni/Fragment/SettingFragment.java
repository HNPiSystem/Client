package com.huni.Fragment;

import com.huni.R;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SettingFragment extends Fragment{
	private String[] list;
	private Switch infraredSwitch; //적외선 센서 스위치
	private Switch soundSwitch; //사운드 센서 스위치
	private OnCheckedChangeListener mChangeListener;
	private boolean isInfrared = false;
	private boolean isSound = false;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//listView가 아닌 정해진 걸로 만들어버리는게 나을듯.
		View rootView=inflater.inflate(R.layout.setting_layout, container,false);
		
		
		infraredSwitch = (Switch)rootView.findViewById(R.id.infraredSwitch);
		soundSwitch = (Switch)rootView.findViewById(R.id.soundSwitch);
		
		list = getResources().getStringArray(R.array.setting);
		
		infraredSwitch.setText(list[0]);
		soundSwitch.setText(list[1]);
		
		mChangeListener = new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(buttonView.getId()==R.id.infraredSwitch && isInfrared ==false)
				{
					infraredSensor();
					Toast.makeText(getActivity(), "적외선", Toast.LENGTH_SHORT).show();
					isInfrared = true;
				}
				
				else if(buttonView.getId()==R.id.soundSwitch && isSound ==false)
				{
					soundSensor();
					Toast.makeText(getActivity(), "사운드", Toast.LENGTH_SHORT).show();
					isSound = true;
				}
			}
		};
		
		infraredSwitch.setOnCheckedChangeListener(mChangeListener);
		soundSwitch.setOnCheckedChangeListener(mChangeListener);
//		listView=(ListView)rootView.findViewById(R.id.settingListview);
//
//		list = getResources().getStringArray(R.array.setting);
//		
//		ArrayList<String> tempList = new ArrayList<String>();
//		for(int i = 0; i<list.length; i++)
//		{
//			tempList.add(list[i]);
//		}
//		listView.setAdapter(new CheckBoxAdapter(getActivity(), R.layout.checkbox_listview,tempList));
//		listView.setOnItemClickListener(new ListListener());

		return rootView;
	}
	private void infraredSensor()
	{
		//적외선 센서 제어
	}

	private void soundSensor()
	{
		//사운드 센서 제어
	}

	
}
