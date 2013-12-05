package com.huni;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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

import com.huni.data.SharedPreference;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class FirstActivity extends Activity{

	private EditText ipText;
	private EditText passText;
	private Button connectButton;
	private CheckBox passwdCheckBox;
	private OnClickListener myOnclickListener;
	public String ip;
	private String passWd;
	private String hashPasswd;
	private ProgressTask mProgressTask;
	private boolean FlagCancelled = false;
	private boolean isStored = false;
	private boolean isBackButtonTouched = false;
	private SharedPreference mPreference;	
	private OnCheckedChangeListener mChangeListener;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.firstactivity_main);

		ipText= (EditText)findViewById(R.id.idText);
		passText= (EditText)findViewById(R.id.passwordText);
		connectButton = (Button)findViewById(R.id.connectButton);
		passwdCheckBox = (CheckBox)findViewById(R.id.passwdCheckBox);

		ipText.setText("192.168.0.4");
		mPreference = new SharedPreference(this);
		isStored = mPreference.getValue("isStored",false);
		
		
		setPasswd(isStored);
		
		myOnclickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				ip = ipText.getText().toString();
				mPreference.put("ip", ip);
				//ip를 sharePreference 에 저장
				passWd = passText.getText().toString();
				mPreference.put("passWd", passWd);
				hashPasswd = getMD5Hash(passWd); //md5로 passwd를 hashcode로 변환
				mPreference.put("hashPasswd", hashPasswd);
				//passwd를 sharePreference 에 저장
				Intent intent = new Intent(FirstActivity.this,SelectActivity.class);
				startActivity(intent);
//				mProgressTask= new ProgressTask(FirstActivity.this); //쓰레드
//				mProgressTask.execute(); //서버에 접속하는 Thread

			}
		};
		
		mChangeListener = new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(buttonView.getId() == R.id.passwdCheckBox)
				{
					if(isChecked)
					{
						mPreference.put("isStored", true);
					}
					else
					{
						mPreference.put("isStored", false);
					}
				}
				
			}
		};

		connectButton.setOnClickListener(myOnclickListener);
		passwdCheckBox.setOnCheckedChangeListener(mChangeListener);
	}



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{

			if(isBackButtonTouched == false)
			{
				isBackButtonTouched = true;
				Toast.makeText(this, "뒤로 버튼을 한번 더 누르면 종료됩니다", Toast.LENGTH_SHORT).show();
				return true;
			}
			else if (isBackButtonTouched ==true)
			{
				finish();
				return super.onKeyDown(keyCode, event);
			}
		}
		return true;
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
				HttpPost mHttpPost =new HttpPost("http://" +ip + ":5000/login"); //서버 ip 주소
				ArrayList<NameValuePair> nameValuePairs =new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("passwd", hashPasswd));	//전달할 인자들 설정
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
				Intent intent = new Intent(FirstActivity.this,SelectActivity.class);
				mPreference.put("accessToken", accessToken); //accessToken을 저장
				intent.putExtra("ip", ip);
				startActivity(intent);
			}
			super.onPostExecute(result);
		}

	}
	
	public String getMD5Hash(String passWd)
	{
		//비밀번호 MD5로 hashCode로 만듬 
		MessageDigest mDigest = null;
		String hash = null;
		
		try
		{
			mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(passWd.getBytes(),0,passWd.length());
			hash = new BigInteger(1,mDigest.digest()).toString(16);
		}
		catch(NoSuchAlgorithmException e)
		{
			e.getStackTrace();
		}
		
		return hash;
	}
	private void setPasswd(boolean isChecked)
	{
		if(isChecked)
		{
			passwdCheckBox.setChecked(isStored);
			passText.setText(mPreference.getValue("passWd", ""));
		}
	}


}