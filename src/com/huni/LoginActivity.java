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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.huni.data.BaseActivity;
import com.huni.data.ProgressTask;
import com.huni.data.SharedPreference;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends BaseActivity{
	private EditText prevoiusEdittext;
	private EditText newPassEdittext;
	private EditText againPassEdittext;
	private Button changeBtn;
	private SharedPreference mPreference;
	private PassChangeTask  mChangePassTask;
	private String ip;
	private String newPasswd;
	private String newHashPasswd;
	private String previuosHashPasswd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);

		prevoiusEdittext = (EditText)findViewById(R.id.previousPass);
		newPassEdittext = (EditText)findViewById(R.id.newPass);
		againPassEdittext = (EditText)findViewById(R.id.againPass);
		changeBtn = (Button)findViewById(R.id.changeBtn);

		mPreference = new SharedPreference(this);
		ip = mPreference.getValue(SharedPreference.ip, ""); //ip 값 가져오기 
		previuosHashPasswd = mPreference.getValue(SharedPreference.hassPassWd, ""); //이전에 저장한 hashpasswd
		
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setIcon(R.drawable.btn_back);

		changeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(v.getId() == R.id.changeBtn)
				{
					checkPass(newPassEdittext.getText().toString(), againPassEdittext.getText().toString());

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

	private void checkPass(String newPass,String againPass)
	{
		//password의 정확도를 검사하는 메소드.
		if(mPreference.getValue("passWd", "").equals(prevoiusEdittext.getText().toString()))
		{
			if(newPass.equals("") || againPass.equals(""))
			{
				Toast.makeText(this, "비밀번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
			}
			else if(newPass.equals(againPass))
			{
				newHashPasswd = getMD5Hash(newPass);
				newPasswd = newPass;
				mChangePassTask = new PassChangeTask(LoginActivity.this);
				mChangePassTask.execute();
			}
			else
			{
				Toast.makeText(this, "다시 입력한 비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
			}
		}
		
	}

	public String getMD5Hash(String passWd)
	{
		//비밀번호를 MD5로 hashCode로 변환  
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




	class PassChangeTask extends AsyncTask<Object, String, JSONObject>{

		private ProgressDialog dialog;
		private Context mContext;
		private boolean started=true;
		private String JSON="";
		private String accessToken="";
		private JSONObject mJsonObject;

		public PassChangeTask(Context context)
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
				HttpPost mHttpPost =new HttpPost("http://" +ip + ":5000/password"); //서버 ip 주소
				ArrayList<NameValuePair> nameValuePairs =new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("passwd", previuosHashPasswd));	//전달할 인자들 설정
				nameValuePairs.add(new BasicNameValuePair("new_pw", newHashPasswd));
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
			String receiveMessage="";
			try {
				receiveMessage = result.getString("result");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dialog.dismiss();

			if(receiveMessage.equals("PW is not correct"))
			{
				Toast.makeText(getBaseContext(), "비밀번호가 맞지 않습니다.", Toast.LENGTH_LONG).show();
				Log.d("receiveMessage", receiveMessage);
				Log.d("hashCode", newHashPasswd);
			}
			else if(receiveMessage.equals("Success"))
			{
				Toast.makeText(mContext, "성공적으로 비밀번호를 변경하였습니다",Toast.LENGTH_SHORT).show();

				mPreference.put(SharedPreference.hassPassWd, newHashPasswd);
				mPreference.put(SharedPreference.passWd, newPasswd);
				//새로운 비밀번호와 md5로 변환된 해시코드를 저장하고 다시 로그인으로 간다.
				//				Intent intent = new Intent(LoginActivity.this,FirstActivity.class);
				//				startActivity(intent);
				finish();
			}
			super.onPostExecute(result);
		}

	}
}
