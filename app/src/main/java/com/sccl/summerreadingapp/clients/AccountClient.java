package com.sccl.summerreadingapp.clients;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.sccl.summerreadingapp.AccountAsyncListener;
import com.sccl.summerreadingapp.SummerReadingApplication;
import com.sccl.summerreadingapp.helper.Constants;
import com.sccl.summerreadingapp.helper.JSONResultParser;
import com.sccl.summerreadingapp.helper.ServiceInvoker;
import com.sccl.summerreadingapp.helper.SharedPreferenceHelper;
import com.sccl.summerreadingapp.model.Account;
import com.sccl.summerreadingapp.model.GridActivity;
import com.sccl.summerreadingapp.model.User;

/**
 * Async task class to get json by making HTTP call
 * */
public class AccountClient extends AsyncTask<String, Void, Account> {

	private static final String ACCOUNT_REQUEST = Constants.BASE_URL + "/accounts/";
	
	
	private Activity parent;
	private ProgressDialog pDialog;
	private AccountAsyncListener listener;
	private String accountId;

	public AccountClient(Activity parent, AccountAsyncListener listener, String accountId) {
		super();
		this.parent = parent;
		this.accountId = accountId;
		this.listener = listener;
	}

	@Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Showing progress dialog
        pDialog = new ProgressDialog(parent);
        pDialog.setMessage("Getting account details...");
        pDialog.setCancelable(false);
        pDialog.show();

    }

    @Override
    protected Account doInBackground(String... arg0) {
        // Creating service handler class instance
        ServiceInvoker serviceInvoker = new ServiceInvoker();
        sendLatestAccountDataToServer(serviceInvoker);
        return getLatestAccountDataFromServer(serviceInvoker);
    }

	public Account getLatestAccountDataFromServer(ServiceInvoker serviceInvoker) {
		String url = ACCOUNT_REQUEST + accountId + ".json";

        // Making a request to url and getting response
       String jsonStr = serviceInvoker.invoke(url, ServiceInvoker.GET);
       Account account = null;

        Log.d("Response: ", "> " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                account = JSONResultParser.createAccount(jsonObj);

            	SummerReadingApplication summerReadingApplication = (SummerReadingApplication) parent.getApplicationContext();
        		summerReadingApplication.setAccount(account);
            	SharedPreferenceHelper.storeAccountDataIntoSharedPreferences(SummerReadingApplication.getContext(), null, account);
                
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get account data");
        }

        return account;
	}

	public void sendLatestAccountDataToServer(ServiceInvoker serviceInvoker) {
    	SummerReadingApplication summerReadingApplication = (SummerReadingApplication) parent.getApplicationContext();
		Account account = summerReadingApplication.getAccount();
		if (account == null) {
			return;
		}

		User users[] = account.getUsers();
		if (users == null) {
			return;
		}
		for (int i = 0; i < users.length; i++) {
			GridActivity[] gridActivities = users[i].getGridActivities();
			if (gridActivities != null) {
				for (int gridIndex = 0; gridIndex < gridActivities.length; gridIndex++) {
					GridActivity grid = gridActivities[gridIndex];
					if (grid != null && grid.getSaveToServer()) {
						if (sendGridData(serviceInvoker, grid, account.getId(), users[i].getId(), gridIndex)) {
							grid.saveToServer(false);
						}
					}
				}
			}
    	}
	}

	private boolean sendGridData(ServiceInvoker serviceInvoker, GridActivity grid, String accountId, String userId, int gridIndex) {
		String url = Constants.GRID_ACTIVITY_REQUEST + accountId + "/users/" + userId + "/activity_grid/" + gridIndex + ".json";
	    List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(3);
	    nameValuePair.add(new BasicNameValuePair("activity", ""+grid.getType()));
	    nameValuePair.add(new BasicNameValuePair("notes", grid.getNotes()));
	    nameValuePair.add(new BasicNameValuePair("updatedAt", grid.getLastUpdated()));
	    return serviceInvoker.invoke(url, ServiceInvoker.PUT, nameValuePair) != null;
}

    @Override
    protected void onPostExecute(Account result) {
        super.onPostExecute(result);
        // Dismiss the progress dialog
        if (pDialog.isShowing())
            pDialog.dismiss();
        listener.onAccountClientResult();
    }

}
