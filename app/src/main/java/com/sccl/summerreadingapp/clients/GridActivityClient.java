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
import com.sccl.summerreadingapp.model.GridActivity;
import com.sccl.summerreadingapp.model.User;

/**
 * Async task class to get json by making HTTP call
 * */
public class GridActivityClient extends AsyncTask<Void, Void, Void> {
	
	// private static final String GRID_ACTIVITY_REQUEST = "http://hackathon.ebaystratus.com/accounts/";
	private static final String GRID_ACTIVITY_REQUEST = Constants.ACCOUNT_REQUEST;
	private Activity parent;
// 	private GridActivityAsyncListener listener;
	private ProgressDialog pDialog;
	User user;
	GridActivity gridActivities;
	int index;
	
	// JSON Node names

//	public GridActivityClient(Activity parent, GridActivityAsyncListener listener, User user, GridActivity gridActivities) {
	public GridActivityClient(Activity parent, User user, int index) {
		super();
		this.parent = parent;
		this.user = user;
		this.index = index;
		this.gridActivities = user.getGridActivities()[index];
		// this.listener = listener;
	}

	@Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Showing progress dialog
        pDialog = new ProgressDialog(parent);
        pDialog.setMessage("Setting Grid result...");
        pDialog.setCancelable(false);
        pDialog.show();

    }

    @Override
    protected Void doInBackground(Void... arg0) {
		SummerReadingApplication summerReadingApplication = (SummerReadingApplication) parent.getApplicationContext();
    	String url = GRID_ACTIVITY_REQUEST + summerReadingApplication.getAccount().getId() + "/users/" + user.getId() + "/activity_grid/" + index + ".json";
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);
        nameValuePair.add(new BasicNameValuePair("activity", ""+gridActivities.getType()));
        nameValuePair.add(new BasicNameValuePair("notes", gridActivities.getNotes()));
        nameValuePair.add(new BasicNameValuePair("updatedAt", gridActivities.getLastUpdated()));
        new ServiceInvoker().invoke(url, ServiceInvoker.PUT, nameValuePair);
        // gridActivities.saveToServer(false); 
        // cannot do it here because it is on a different thread and when we write to sharedpreference which is happening main 
       // thread, it will still be true!!!
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
