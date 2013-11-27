package com.huni.data;

import java.util.ArrayList;
import com.huni.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class CheckBoxAdapter extends BaseAdapter{
	
	private Context mContext;
	private LayoutInflater mInflater;
	private int layout;
	ArrayList<String> list =new ArrayList<String>();
	TextView mTextView;
	CheckBox mCheckBox;

	public CheckBoxAdapter (Context context, int layout, ArrayList<String> arrayList)
	{
		this.mContext = context;
		this.list = arrayList;
		mInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		this.layout = layout;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//모든 처리는 getView에서 진행된다.
		if(convertView ==null)
		{
			convertView = mInflater.inflate(layout, parent,false);
		}
		
		mTextView = (TextView)convertView.findViewById(R.id.titleText);
		mCheckBox = (CheckBox)convertView.findViewById(R.id.checkBox1);
		
		mTextView.setText(list.get(position));
		return convertView;
	}
	

}
