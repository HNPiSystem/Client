package com.huni.data;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huni.R;

public class ImageAdapter extends BaseAdapter{

	private Context mContext = null;
	private LayoutInflater mInflater;
	private ArrayList<String> devices;
	private boolean [] available = {true,false,true,false};
	private Integer [] mThumbIds =
		{
			R.drawable.ic_cctv, R.drawable.ic_temp,R.drawable.ic_bulb,R.drawable.btn_commingsoon
		};
	private Integer [] disable =
		{
			R.drawable.btn_cctv_disable,R.drawable.btn_temp_disable,R.drawable.btn_light_disable,R.drawable.btn_commingsoon
		};
	private String []mTitle;
	
	public ImageAdapter (Context mContext,ArrayList<String>temp){
		super();
		this.mContext = mContext;
		mInflater = LayoutInflater.from(this.mContext);
		devices = temp;
		mTitle  = mContext.getResources().getStringArray(R.array.tab);
			
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mThumbIds.length;
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
	public View getView(int position, View convertView, ViewGroup parent) 
	{     
		View view = mInflater.inflate(R.layout.image_text_button,null);
		ImageButton icon = (ImageButton)view.findViewById(R.id.imageButton);
		TextView mTextView = (TextView)view.findViewById(R.id.nameText);

		

		if(available[position] == true)
		{
			icon.setBackground(null);
			icon.setImageResource(mThumbIds[position]);
			icon.setClickable(false);
			icon.setFocusable(false);
			
		}
		else if(available[position] == false)
		{
			icon.setImageResource(disable[position]);
			icon.setClickable(false);
		}
		
		icon.setLayoutParams(new RelativeLayout.LayoutParams(170, 170));
		icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
		icon.setPadding(1, 1, 1, 1); 
		//icon.setId(position);
		
		
		mTextView.setText(mTitle[position]);
	       
		return view;    
	}  
	
}
