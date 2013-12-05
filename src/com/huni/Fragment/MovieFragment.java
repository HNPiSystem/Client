package com.huni.Fragment;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.huni.R;
import com.huni.data.SharedPreference;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MovieFragment extends Fragment{

	private Button movieBtn;
	private MovieTask movieTask;
	private SharedPreference mPreference;
	private String accessToken;
	private String ip;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView=inflater.inflate(R.layout.movie_layout, container,false);

		mPreference = new SharedPreference(getActivity());

		ip = mPreference.getValue("ip", ""); //ip 주소
		accessToken = mPreference.getValue("accessToken", ""); //accessToken

		movieBtn = (Button)rootView.findViewById(R.id.movieBtn);

		movieBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				movieTask = new MovieTask(getActivity());
				//movieTask.execute();
				
				Uri uri = Uri.parse("http://www.wowza.com/_h264/BigBuckBunny_115k.mov");
				startActivity(new Intent(Intent.ACTION_VIEW,uri));
			}
		});

		return rootView;
	}

	class MovieTask extends AsyncTask<Object, String, JSONObject>{

		private Context mContext;
		private boolean started=true;
		private String JSON="";
		private ProgressDialog dialog;
		private JSONObject mJsonObject;


		public MovieTask(Context context)
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
			dialog.setTitle("동영상 로딩중");
			dialog.setMessage("Please wait while loading...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			dialog.show();


			super.onPreExecute();
		}

		@Override
		protected JSONObject doInBackground(Object... params) {
			// TODO Auto-generated method stub

			try {
				DefaultHttpClient mhttpClient =new DefaultHttpClient();
				HttpGet mHttpGet =new HttpGet("http://192.168.0.4:5000/askfor?accessToken=accessToken&order=movie");

				//HttpGet mHttpGet =new HttpGet("http://" + "192.168.0.4" + ":5000/askfor?accessToken=accessToken&order=movie/");
				ArrayList<NameValuePair> nameValuePairs =new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("order","picture"));	//전달할 인자들 설정
				HttpResponse mhttpHttpResponse = mhttpClient.execute(mHttpGet);

				HttpEntity mHttpEntity =mhttpHttpResponse.getEntity();
				if(mHttpEntity!=null)
				{
					JSON = EntityUtils.toString(mHttpEntity);
					Log.i("RESPONSE", JSON);

				}
				
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
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

			String url="";
			try {
				url = result.getString("result");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Uri uri = Uri.parse(url);
			startActivity(new Intent(Intent.ACTION_VIEW,uri));
			dialog.dismiss();

			super.onPostExecute(result);
		}
	}
}
