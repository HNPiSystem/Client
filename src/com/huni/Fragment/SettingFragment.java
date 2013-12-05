package com.huni.Fragment;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.huni.FirstActivity;
import com.huni.MainActivity;
import com.huni.R;
import com.huni.data.SharedPreference;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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
	private SharedPreference mPreference;
	private String accessToken = "";
	private String ip = "";
	private String passWd = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// RelativeLayout으로 고정 

		mPreference = new SharedPreference(getActivity());
		ip = mPreference.getValue("ip", ""); //ip 주소
		accessToken = mPreference.getValue("accessToken", ""); //accessToken

		View rootView=inflater.inflate(R.layout.setting_layout, container,false);


		infraredSwitch = (Switch)rootView.findViewById(R.id.infraredSwitch);//적외선 센서 
		soundSwitch = (Switch)rootView.findViewById(R.id.soundSwitch); //사운드 센서 

		list = getResources().getStringArray(R.array.setting);

		infraredSwitch.setText(list[0]);
		soundSwitch.setText(list[1]);

		mChangeListener = new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(buttonView.getId()==R.id.infraredSwitch && isChecked ==true)
				{
					infraredSensorOn();
					Toast.makeText(getActivity(), "적외선 센서 on", Toast.LENGTH_SHORT).show();
				}
				else if (buttonView.getId()==R.id.infraredSwitch && isChecked ==false)
				{
					infraredSensorOff();
					Toast.makeText(getActivity(), "적외선 센서 off", Toast.LENGTH_SHORT).show();
				}

				if(buttonView.getId()==R.id.soundSwitch && isChecked ==true)
				{
					soundSensorOn();
					Toast.makeText(getActivity(), "사운드 센서 on", Toast.LENGTH_SHORT).show();
				}
				else if(buttonView.getId()==R.id.soundSwitch && isChecked ==false)
				{
					soundSensorOff();
					Toast.makeText(getActivity(), "사운드 센서 off", Toast.LENGTH_SHORT).show();
				}
			}
		};

		infraredSwitch.setOnCheckedChangeListener(mChangeListener);
		soundSwitch.setOnCheckedChangeListener(mChangeListener);
		return rootView;
	}
	private void infraredSensorOn()
	{
		//적외선 센서 on
	}
	private void infraredSensorOff()
	{
		//적외선 센서 off
	}

	private void soundSensorOn()
	{
		//사운드 센서 on
	}
	private void soundSensorOff()
	{
		//사운드 센서 off
	}

	class ProgressTask extends AsyncTask<String, String, JSONObject>{

		private ProgressDialog dialog;
		private String url;
		private boolean started=true;
		private String JSON="";
		private String accessToken="";
		private JSONObject mJsonObject;

		public ProgressTask(String mode)
		{
			if(mode.equals("적외선on"))
			{
				this.url = "";
			}
			else if (mode.equals("적외선off"))
			{
				this.url = "";
			}
			else if (mode.equals("사운드on"))
			{
				this.url = "";
			}
			else if (mode.equals("사운드off"))
			{
				this.url = "";
			}
		}
		public ProgressTask()
		{
			this("적외선on");
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
			dialog = new ProgressDialog(getActivity());
			dialog.setTitle("서버 연결중");
			dialog.setMessage("Please wait while loading...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub

			try
			{
				// HTTP request 를 보낼 때 POST 방식은 보통 List<NameValuePair> 를 만들어서 Entity 로 전달을 하곤 합니다.
				DefaultHttpClient mhttpClient =new DefaultHttpClient();
				HttpPost mHttpPost =new HttpPost(url); //서버 ip 주소
				//HttpPost mHttpPost =new HttpPost("http://" +ip + ":5000/login"); 원래 코드
				ArrayList<NameValuePair> nameValuePairs =new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("passwd", passWd));	//전달할 인자들 설정
				mHttpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse mhttpHttpResponse= mhttpClient.execute(mHttpPost); //서버에 연결 요청	
				HttpEntity mHttpEntity =mhttpHttpResponse.getEntity(); //서버에서 값 가져오기.

				JSON = EntityUtils.toString(mHttpEntity);

			}
			catch (UnsupportedEncodingException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			catch (ClientProtocolException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			catch (IOException e) {
				// TODO: handle exception
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
			try {
				accessToken = result.getString("result");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dialog.dismiss();

			if(accessToken.equals("Invalid Password"))
			{
				Toast.makeText(getActivity(), "연결 오류입니다.", Toast.LENGTH_LONG).show();
			}
			else
			{

			}
			super.onPostExecute(result);
		}

	}

}
