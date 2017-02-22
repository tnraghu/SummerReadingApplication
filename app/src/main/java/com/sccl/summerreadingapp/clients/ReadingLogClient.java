package com.sccl.summerreadingapp.clients;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.sccl.summerreadingapp.SummerReadingApplication;
import com.sccl.summerreadingapp.helper.Constants;
import com.sccl.summerreadingapp.helper.ServiceInvoker;
import com.sccl.summerreadingapp.model.User;

/**
 * Async task class to get json by making HTTP call
 * */
public class ReadingLogClient extends AsyncTask<Void, Void, Void> {
	
	// private static final String GRID_ACTIVITY_REQUEST = "http://hackathon.ebaystratus.com/accounts/";
	private static final String READING_LOG_REQUEST = Constants.ACCOUNT_REQUEST;
	private Activity parent;
	private ProgressDialog pDialog;
	User user;
	int readingLog;
	
	// JSON Node names

//	public GridActivityClient(Activity parent, GridActivityAsyncListener listener, User user, GridActivity gridActivities) {
	public ReadingLogClient(Activity parent, User user, int readingLog) {
		super();
		this.parent = parent;
		this.user = user;
		this.readingLog = readingLog;
		// this.listener = listener;
	}

	@Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Showing progress dialog
        pDialog = new ProgressDialog(parent);
        pDialog.setMessage("Setting Reading Log...");
        pDialog.setCancelable(false);
        pDialog.show();

    }

    @Override
    protected Void doInBackground(Void... arg0) {
		SummerReadingApplication summerReadingApplication = (SummerReadingApplication) parent.getApplicationContext();
    	String url = READING_LOG_REQUEST + summerReadingApplication.getAccount().getId() + "/users/" + user.getId() + "/reading_log.json";
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
        nameValuePair.add(new BasicNameValuePair("readingLog", ""+readingLog));
        new ServiceInvoker().invoke(url, ServiceInvoker.PUT, nameValuePair);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        // Dismiss the progress dialog
        if (pDialog.isShowing())
            pDialog.dismiss();
//        listener.onResult(result);
    }

}
