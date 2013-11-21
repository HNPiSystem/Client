package com.huni.Fragment;

import com.huni.R;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MovieFragment extends Fragment{
	
	private Button movieBtn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView=inflater.inflate(R.layout.movie_layout, container,false);
		
		
		movieBtn = (Button)rootView.findViewById(R.id.movieBtn);
		
		movieBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_VIEW);
				Uri uri = Uri.parse("http://www.archive.org/download/Unexpect2001/Unexpect2001_512kb.mp4");
				intent.setDataAndType(uri, "video/*");
				startActivity(intent);
			}
		});

		return rootView;
	}

	public void showMovie()
	{

	}

	class MovieTask extends AsyncTask<Object, String, Void>{

		private Context mContext;
		private boolean started=true;


		public MovieTask(Context context)
		{
			mContext = context;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			//작업 완료후 하는 일;

			super.onPostExecute(result);
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

			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Object... params) {
			// TODO Auto-generated method stub

			while(started)
			{
				if (isCancelled())
				{
					break;
				}
			}
			return null;
		}
	}
	public void errorMessage()
	{

	}
}
