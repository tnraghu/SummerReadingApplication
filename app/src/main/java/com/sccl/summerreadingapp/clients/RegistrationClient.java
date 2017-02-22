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

import com.sccl.summerreadingapp.RegistrationAsyncListener;
import com.sccl.summerreadingapp.helper.Constants;
import com.sccl.summerreadingapp.helper.JSONResultParser;
import com.sccl.summerreadingapp.helper.ServiceInvoker;
import com.sccl.summerreadingapp.model.Login;

/**
 * Async task class to get json by making HTTP call
 * */
public class RegistrationClient extends AsyncTask<String, Void, Login> {
	
	// private static final String REGISTRATION_REQUEST = "http://hackathon.ebaystratus.com/accounts.json";
	private static final String REGISTRATION_REQUEST = Constants.BASE_URL + "/accounts.json";
	private Activity parent;
	private RegistrationAsyncListener listener;
	private ProgressDialog pDialog;
	
	// JSON Node names

	public RegistrationClient(Activity parent, RegistrationAsyncListener listener) {
		super();
		this.parent = parent;
		this.listener = listener;
	}

	@Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Showing progress dialog
        pDialog = new ProgressDialog(parent);
        pDialog.setMessage("Registering User ...");
        pDialog.setCancelable(false);
        pDialog.show();

    }

    @Override
    protected Login doInBackground(String... arg0) {
        // Creating service handler class instance
        if (arg0.length < 4)
        	return null;
        
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(4);
        nameValuePair.add(new BasicNameValuePair("accountName", arg0[0]));
        nameValuePair.add(new BasicNameValuePair("passcode", arg0[1]));
        nameValuePair.add(new BasicNameValuePair("emailAddress", arg0[2]));
        nameValuePair.add(new BasicNameValuePair("branchId", arg0[3]));
        nameValuePair.add(new BasicNameValuePair("languageSpoken", arg0[4]));
        nameValuePair.add(new BasicNameValuePair("phone", arg0[5]));

        // Making a request to url and getting response
       String jsonStr = new ServiceInvoker().invoke(REGISTRATION_REQUEST, ServiceInvoker.POST, nameValuePair);
       // String jsonStr = "";
//       Account account = null;
       Login login = null;

        Log.d("Response: ", "> " + jsonStr);

        if (jsonStr != null) {
            try {
                // JSONObject jsonLoginObject = new JSONObject(jsonStr);
                // return JSONResultParser.createAccount(jsonLoginObject);

                JSONObject jsonLoginObject = new JSONObject(jsonStr);
                login = JSONResultParser.createLogin(jsonLoginObject);
                login.setLoginJSONResponse(jsonStr);
            
            	} catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("ServiceHandler", "Couldn't get any data from the url");
        }

        // return account;
        return login;
    }

    @Override
//    protected void onPostExecute(Account result) {
    protected void onPostExecute(Login result) {
        super.onPostExecute(result);
        // Dismiss the progress dialog
        if (pDialog.isShowing())
            pDialog.dismiss();
        listener.onResult(result);
    }

}
