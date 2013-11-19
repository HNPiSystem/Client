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
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

public class PictureFragment extends Fragment{

	private Button Picturebtn;
	private ImageView mImageView;
	private PictureTask mPictureTask;
	private SharedPreference mPreference;
	private String accessToken;
	private String ip;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView=inflater.inflate(R.layout.picture_layout, container,false);

		Picturebtn = (Button)rootView.findViewById(R.id.pictureBtn);
		mImageView = (ImageView)rootView.findViewById(R.id.resultImageView);

		mPreference = new SharedPreference(getActivity());

		mPreference.getValue("ip", ip); //ip 주소
		mPreference.getValue("accessToken", accessToken); //accessToken


		Picturebtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mPictureTask = new PictureTask(getActivity());
				mPictureTask.execute();		
			}
		});

		return rootView;
	}
	private void showPicture()
	{

	}

	class PictureTask extends AsyncTask<Object, String, JSONObject>{
		private ProgressDialog dialog;
		private Context mContext;
		private boolean started=true;
		private JSONObject mJsonObject=null;
		private String JSON="";
		private Bitmap mBitmap = null;

		public PictureTask(Context context)
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
				HttpGet mHttpGet =new HttpGet("http://" + "192.168.0.4" + ":5000/hardware");

				//ArrayList<NameValuePair> nameValuePairs =new ArrayList<NameValuePair>();
				//nameValuePairs.add(new BasicNameValuePair("order","picture"));	//전달할 인자들 설정
				//mHttpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

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
			String Url="";
			try {
				Url= result.getString("result");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			LoadPictureTask mLoadPictureTask = new LoadPictureTask();
			mLoadPictureTask.execute(Url);
			dialog.dismiss();

			
			
			super.onPostExecute(result);
		}
	}
	class LoadPictureTask extends AsyncTask<String, Void, Void>
	{

		private Bitmap mBitmap = null;

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			try {
				InputStream in = new java.net.URL(params[0]).openStream();
	            mBitmap= BitmapFactory.decodeStream(in);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			mImageView.setScaleType(ScaleType.FIT_CENTER);
			mImageView.setImageBitmap(mBitmap);
			mImageView.setVisibility(View.VISIBLE);		
			super.onPostExecute(result);
		}
		
		
	}
}
