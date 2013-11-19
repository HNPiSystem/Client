package com.huni.Fragment;

import com.huni.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SettingFragment extends Fragment{
	private String[] list;
	private ListView listView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView=inflater.inflate(R.layout.setting_layout, container,false);

		listView=(ListView)rootView.findViewById(R.id.settingListview);

		list = getResources().getStringArray(R.array.setting);
		listView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,list));
		listView.setOnItemClickListener(new ListListener());

		return rootView;
	}
	public class ListListener implements OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
		}		
	}

	private void sensorHandle()
	{
		//인체감지센서
	}

	private void dayNinght()
	{
		//주야간 설정
	}

	private void notice()
	{
		//신고기능
	}
}
