package com.huni.Fragment;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import com.huni.MainActivity;
import com.huni.R;
import com.huni.data.SharedPreference;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView.ScaleType;
import android.widget.Switch;
import android.widget.Toast;

public class LightFragment extends Fragment{

	private Switch autobright;
	private Switch turnPower;
	private SharedPreference mPreference;
	private String accessToken;
	private String ip;
	private OnCheckedChangeListener mChangeListener;
	private LightTask mLightTask;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView=inflater.inflate(R.layout.light_layout, container,false);
		autobright = (Switch)rootView.findViewById(R.id.brightSwitch);
		turnPower = (Switch)rootView.findViewById(R.id.PowerSwitch);

		mPreference = new SharedPreference(getActivity());
		ip = mPreference.getValue("ip", ""); //ip 주소
		accessToken = mPreference.getValue("accessToken", ""); //accessToken
		
		

		mChangeListener = new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(buttonView.getId() == R.id.brightSwitch && isChecked ==true)
				{
					mLightTask = new LightTask(getActivity(), 0);
					Toast.makeText(getActivity(), "Auto mode on", Toast.LENGTH_SHORT).show();
				}
				else if (buttonView.getId() == R.id.brightSwitch && isChecked ==false)
				{
					mLightTask = new LightTask(getActivity(), 1);
					Toast.makeText(getActivity(), "Auto mode off", Toast.LENGTH_SHORT).show();
				}

				if(buttonView.getId() == R.id.PowerSwitch && isChecked ==true)
				{
					mLightTask = new LightTask(getActivity(), 2);
					Toast.makeText(getActivity(), "Turn on", Toast.LENGTH_SHORT).show();
				}
				else if(buttonView.getId() == R.id.PowerSwitch && isChecked ==false)
				{
					mLightTask = new LightTask(getActivity(), 3);
					Toast.makeText(getActivity(), "Turn off", Toast.LENGTH_SHORT).show();
				}
				
				mLightTask.execute();
			}
		};

		autobright.setOnCheckedChangeListener(mChangeListener);
		turnPower.setOnCheckedChangeListener(mChangeListener);
		return rootView;
	}

	class LightTask extends AsyncTask<Object, String, JSONObject>{
		private ProgressDialog dialog;
		private Context mContext;
		private boolean started=true;
		private JSONObject mJsonObject=null;
		private String JSON="";
		private Bitmap mBitmap = null;
		private String url;

		public LightTask(Context context,int mode)
		{
			mContext = context;
			
			switch (mode) {
			case 0:
				url = ":5000/askfor?accessToken=accessToken&order=light_auto";
				break;
			case 1:
				url = ":5000/askfor?accessToken=accessToken&order=light_auto";
				break;
			case 2:
				url = ":5000/askfor?accessToken=accessToken&order=light_on";
				break;
			case 3:
				url = ":5000/askfor?accessToken=accessToken&order=light_off";
				break;
			default:
				break;
			}
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
			dialog.setTitle("사진 로딩중");
			dialog.setMessage("Please wait while loading...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			dialog.show();

			super.onPreExecute();
		}

		@Override
		protected JSONObject doInBackground(Object... params) {
			// TODO Auto-generated method stub
			//쓰레드 도는 중에 하는 일
			try
			{
				DefaultHttpClient mhttpClient =new DefaultHttpClient();
				HttpGet mHttpGet =new HttpGet("http://" + ip + url);

				//ArrayList<NameValuePair> nameValuePairs =new ArrayList<NameValuePair>();
				//nameValuePairs.add(new BasicNameValuePair("order","picture"));	//전달할 인자들 설정
				//mHttpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				//Post방식에서 헤더에 값 설정하는 부분인데 get으로 해서 지금 주석처리중

				HttpResponse mhttpHttpResponse= mhttpClient.execute(mHttpGet);
				HttpEntity mHttpEntity =mhttpHttpResponse.getEntity();

				JSON = EntityUtils.toString(mHttpEntity);
				//mInputStream=mHttpEntity.getContent();

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
			//사진 불러오기;
			String temp="";
			try {
				temp= result.getString("result");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d("error", url);
				Toast.makeText(getActivity(), "Connectting Fail", Toast.LENGTH_SHORT).show();
			}
			Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
			dialog.dismiss();
			super.onPostExecute(result);
		}
	}
}

