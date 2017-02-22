package com.sccl.summerreadingapp.clients;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.sccl.summerreadingapp.ConfigAsyncListener;
import com.sccl.summerreadingapp.SummerReadingApplication;
import com.sccl.summerreadingapp.helper.Constants;
import com.sccl.summerreadingapp.helper.JSONResultParser;
import com.sccl.summerreadingapp.helper.ServiceInvoker;
import com.sccl.summerreadingapp.helper.SharedPreferenceHelper;
import com.sccl.summerreadingapp.model.Config;
import com.sccl.summerreadingapp.model.UserType;

/**
 * Async task class to get json by making HTTP call
 * */
public class ConfigClient extends AsyncTask<Void, Void, Void> {
	
	private static final String BRANCH_REQUEST = Constants.BRANCH_REQUEST; // "http://hackathon.ebaystratus.com/branches.json";
	private static final String USER_TYPE_REQUEST = Constants.USER_TYPE_REQUEST; // "http://hackathon.ebaystratus.com/user_types.json";
	private static final String GRID_CELL_REQUEST = Constants.GRID_CELL_REQUEST; // "http://hackathon.ebaystratus.com/grids.json";
	private Activity parent;
	private ProgressDialog pDialog;
	private ConfigAsyncListener listener;
	private boolean sendResult;
	
	// JSON Node names

	public ConfigClient(Activity parent, ConfigAsyncListener listener, boolean sendResult) {
		super();
		this.parent = parent;
		this.listener = listener;
		this.sendResult = sendResult;
	}

	@Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Showing progress dialog
        pDialog = new ProgressDialog(parent);
        pDialog.setMessage("Getting Configuration Data...");
        pDialog.setCancelable(false);
        pDialog.show();

    }

    @Override
    protected Void doInBackground(Void... arg0) {
    	Config config = new Config();
    	ServiceInvoker serviceInvoker = new ServiceInvoker();
    	String branchesStr = getBranchFromUrl(serviceInvoker); 
    	config.setBranchesString(branchesStr);
    	config.setBranches(JSONResultParser.createBranches(branchesStr));
    	
    	String userTypesStr = getUserTypesFromUrl(serviceInvoker);
    	config.setUserTypesString(userTypesStr);
    	config.setUserTypes(JSONResultParser.createUserTypes(userTypesStr));
    	
    	String gridCellsStr = getGridCellFromUrl(serviceInvoker); 
    	config.setGridCellsString(gridCellsStr);
    	config.setGridCells(JSONResultParser.createGridCells(gridCellsStr));
    	
        // TBD
    	// Should use service version
    	// String prizeDescriptionString = getPrizeDescriptionsFromUrl(serviceInvoker); 
    	// config.setPrizeDescriptionsString(prizeDescriptionString);
    	// config.setPrizeDescriptions(JSONResultParser.createPrizeDescriptios(prizeDescriptionString)));
    	if (config.getUserTypes() != null) {
    		config.setPrizeDescriptions(JSONResultParser.createPrizeDescriptios(config.getUserTypes()));
    	}
    	
    	SummerReadingApplication summerReadingApplication = (SummerReadingApplication) parent.getApplicationContext();
    	summerReadingApplication.setConfig(config);

    	SharedPreferenceHelper.storeConfigDataIntoSharedPreferences(SummerReadingApplication.getContext(), config);
    	return null;
    }

	private String getBranchFromUrl(ServiceInvoker serviceInvoker) {
		String jsonStr = serviceInvoker.invoke(BRANCH_REQUEST, ServiceInvoker.GET);
    	return jsonStr;
	}

	private String getUserTypesFromUrl(ServiceInvoker serviceInvoker) {
		String jsonStr = serviceInvoker.invoke(USER_TYPE_REQUEST, ServiceInvoker.GET);
    	return jsonStr;
	}

	private String getGridCellFromUrl(ServiceInvoker serviceInvoker) {
		String jsonStr = serviceInvoker.invoke(GRID_CELL_REQUEST, ServiceInvoker.GET);
    	return jsonStr;
	}

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        // Dismiss the progress dialog
        if (pDialog.isShowing())
            pDialog.dismiss();
        if (sendResult) {
        	listener.onResult();
        }
    }

}
