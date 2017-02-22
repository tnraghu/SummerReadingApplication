package com.sccl.summerreadingapp.helper;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.sccl.summerreadingapp.model.Account;
import com.sccl.summerreadingapp.model.Config;
import com.sccl.summerreadingapp.model.Login;

public class SharedPreferenceHelper {

	public static void storeAccountDataIntoSharedPreferences(Context c, Login login, Account account) {
		SharedPreferences userDetails = c.getSharedPreferences("Account", Context.MODE_PRIVATE);
		Editor edit = userDetails.edit();
		if (login != null) {
			edit.putString("Token", login.getAuthToken());
		}
		edit.putString("Name", account.getName());
		edit.putString("Account", account.toJSON());
		edit.commit();
	}
	
	public static Account getAccountFromSharedPreferences(Context c) {
		SharedPreferences userDetails = c.getSharedPreferences("Account", Context.MODE_PRIVATE);
		Account account = null;
		try {
            String accountStr = userDetails.getString("Account", "");
            if (!MiscUtils.empty(accountStr)) {
            	account = JSONResultParser.createAccount(new JSONObject(accountStr));
            }
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return account;
	}

	public static void storeConfigDataIntoSharedPreferences(Context c, Config config) {
		SharedPreferences userDetails = c.getSharedPreferences("Config", Context.MODE_PRIVATE);
		Editor edit = userDetails.edit();
		edit.putString("Branches", config.getBranchesString());
		edit.putString("UserTypes", config.getUserTypesString());
		edit.putString("GridCells", config.getGridCellsString());
		edit.commit();
	}
	
	public static Config getConfigFromSharedPreferences(Context c) {
		SharedPreferences userDetails = c.getSharedPreferences("Config", Context.MODE_PRIVATE);
		Config config = new Config();;
        String branchesStr = userDetails.getString("Branches", "");
        if (!MiscUtils.empty(branchesStr)) {
        	config.setBranchesString(branchesStr);
        	config.setBranches(JSONResultParser.createBranches(branchesStr));
        }

        String userTypesStr = userDetails.getString("UserTypes", "");
        if (!MiscUtils.empty(userTypesStr)) {
        	config.setUserTypesString(userTypesStr);
        	config.setUserTypes(JSONResultParser.createUserTypes(userTypesStr));
        }

        String gridCellsStr = userDetails.getString("GridCells", "");
        if (!MiscUtils.empty(gridCellsStr)) {
        	config.setGridCellsString(gridCellsStr);
        	config.setGridCells(JSONResultParser.createGridCells(gridCellsStr));
        }

        // TBD
    	// Should use service version
/*        String prizeDescriptionsStr = userDetails.getString("PrizeDescriptions", "");
        if (!MiscUtils.empty(prizeDescriptions)) {
        	config.createPrizeDescriptios(prizeDescriptions);
        	config.createPrizeDescriptios(JSONResultParser.createPrizeDescriptios(prizeDescriptions));
        }
*/
    	if (config.getUserTypes() != null) {
    		config.setPrizeDescriptions(JSONResultParser.createPrizeDescriptios(config.getUserTypes()));
    	}

    	return config;
	}
}
