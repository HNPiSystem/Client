package com.huni;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.http.Header;
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

import com.huni.data.SharedPreference;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FirstActivity extends Activity{

	private EditText ipText;
	private EditText passText;
	private Button connectButton;
	private OnClickListener myOnclickListener;
	public String ip;
	private String passWd;
	private ProgressDialog connectDialog;
	private ProgressTask mProgressTask;
	private boolean FlagCancelled = false;
	private SharedPreference mPreference;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.firstactivity_main);

		ipText= (EditText)findViewById(R.id.idText);
		passText= (EditText)findViewById(R.id.passwordText);
		connectButton = (Button)findViewById(R.id.connectButton);
		getActionBar().setIcon(R.drawable.ic_action_cast);
		
		mPreference = new SharedPreference(this);

		myOnclickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ip = ipText.getText().toString();
				mPreference.put("ip", ip);
				//ip를 sharePreference 에 저장
				passWd = passText.getText().toString();
				mPreference.put("passwd", passWd);
				//passwd를 sharePreference 에 저장
				mProgressTask= new ProgressTask(FirstActivity.this);
						
				Intent intent = new Intent(FirstActivity.this,MainActivity.class);
				startActivity(intent);
				//mProgressTask.execute(); 서버에 접속하는 Thread
			
			}
		};

		connectButton.setOnClickListener(myOnclickListener);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		FlagCancelled = true;
		//mProgressTask.cancel(true);
		super.onPause();
	}
	class ProgressTask extends AsyncTask<Object, String, JSONObject>{

		private ProgressDialog dialog;
		private Context mContext;
		private boolean started=true;
		private String JSON="";
		private String accessToken="";
		private JSONObject mJsonObject;

		public ProgressTask(Context context)
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
			dialog.setTitle("서버 연결중");
			dialog.setMessage("Please wait while loading...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected JSONObject doInBackground(Object... params) {
			// TODO Auto-generated method stub

			try
			{
				// HTTP request 를 보낼 때 POST 방식은 보통 List<NameValuePair> 를 만들어서 Entity 로 전달을 하곤 합니다.
				DefaultHttpClient mhttpClient =new DefaultHttpClient();
				HttpPost mHttpPost =new HttpPost("http://" + "192.168.0.4" + ":5000/login"); //서버 ip 주소
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
/*
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						mInputStream, "UTF-8"));
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				mInputStream.close();
				JSON = sb.toString();

			} catch (Exception e) {
				Log.e("Buffer Error", "Error converting result " + e.toString());
			}

			// try parse the string to a JSON object
			try {
				mJsonObject = new JSONObject(JSON);
			} catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing data " + e.toString());
			}
			// return JSON String
			 * */
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
				Toast.makeText(getBaseContext(), "연결 오류입니다.", Toast.LENGTH_LONG).show();
			}
			else
			{
				Intent intent = new Intent(FirstActivity.this,MainActivity.class);
				mPreference.put("accessToken", accessToken); //accessToken을 저장
				intent.putExtra("ip", ip);
				startActivity(intent);
			}
			super.onPostExecute(result);
		}

	}
}