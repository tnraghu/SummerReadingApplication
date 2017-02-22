package com.sccl.summerreadingapp.clients;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.sccl.summerreadingapp.AddUserAsyncListener;
import com.sccl.summerreadingapp.helper.Constants;
import com.sccl.summerreadingapp.helper.JSONResultParser;
import com.sccl.summerreadingapp.helper.MiscUtils;
import com.sccl.summerreadingapp.helper.ServiceInvoker;
import com.sccl.summerreadingapp.model.User;

/**
 * Async task class to get json by making HTTP call
 * */
public class AddUserClient extends AsyncTask<String, Void, User> {
	
	// private static final String ADD_USER_REQUEST = "http://hackathon.ebaystratus.com/accounts/";
	private static final String ADD_USER_REQUEST = Constants.ACCOUNT_REQUEST;
	private Activity parent;
	private AddUserAsyncListener listener;
	private ProgressDialog pDialog;
	
	// JSON Node names

	public AddUserClient(Activity parent, AddUserAsyncListener listener) {
		super();
		this.parent = parent;
		this.listener = listener;
	}

	@Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Showing progress dialog
        pDialog = MiscUtils.createProgressDialog(parent);

    }

    @Override
    protected User doInBackground(String... arg0) {
        // Creating service handler class instance
        if (arg0.length < 5)
        	return null;
        
        String accountId = arg0[3];
    	String url = ADD_USER_REQUEST + accountId + "/users.json";
    	
    	JSONObject jsonObject = new JSONObject();
    	
    	try {
/*
        	JSONObject jsonUserObject = new JSONObject();
        	// jsonUserObject.accumulate("id", -1);
			jsonUserObject.accumulate("firstName", arg0[0]);
			jsonUserObject.accumulate("lastName", arg0[1]);
			jsonUserObject.accumulate("userType", arg0[2]);
			jsonObject.put("user", jsonUserObject);
*/			
            String ageStr = arg0[4];
            int age = Integer.parseInt(ageStr);

            User user = new User("-1", arg0[0], arg0[1], arg0[2], 0, age);
			jsonObject.put("user", user.toJSONObject(false));
			String newUser = new ServiceInvoker().invokeJson(url, ServiceInvoker.POST, jsonObject);
			JSONObject userResponse = new JSONObject(newUser);
	        JSONObject jsonUserObj = userResponse.getJSONObject("user");

			return JSONResultParser.createUser(jsonUserObj);
			// return user;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }

    @Override
    protected void onPostExecute(User result) {
        super.onPostExecute(result);
        // Dismiss the progress dialog
        if (pDialog.isShowing())
            pDialog.dismiss();
        listener.onResult(result);
    }

}
