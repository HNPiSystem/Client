package com.huni.data;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class ProgressTask extends AsyncTask<Object, String, Void>{

	
	private ProgressDialog dialog;
	private Context mContext;
	private boolean started=true;


	public ProgressTask(Context context)
	{
		mContext = context;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		//작업 완료후 하는 일;
		dialog.dismiss();
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
		dialog = new ProgressDialog(mContext);
		dialog.setTitle("Indeterminate");
		dialog.setMessage("Please wait while loading...");
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		dialog.show();
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(Object... params) {
		// TODO Auto-generated method stub

		while(started)
		{
			if (isCancelled())
			{
				dialog.dismiss();
				break;
			}
		}
		return null;
	}
}

