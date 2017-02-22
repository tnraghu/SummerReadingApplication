package com.sccl.summerreadingapp.helper;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.sccl.summerreadingapp.model.Account;
import com.sccl.summerreadingapp.model.Branch;
import com.sccl.summerreadingapp.model.GridActivity;
import com.sccl.summerreadingapp.model.GridCell;
import com.sccl.summerreadingapp.model.Login;
import com.sccl.summerreadingapp.model.Prize;
import com.sccl.summerreadingapp.model.PrizeDescription;
import com.sccl.summerreadingapp.model.User;
import com.sccl.summerreadingapp.model.UserType;

public class JSONResultParser {
    private static final String PRIZE_DESCRIPTION = "prize";
	private static final String ACCOUNT = "account";
    private static final String USERS = "users";
    private static final String USER_GRIDS = "activityGrid";
    private static final String USER_PRIZES = "prizes";
	private static String AUTH_TOKEN = "authToken"; 

	static public Login createLogin(JSONObject jsonLoginObject) throws JSONException {
        String authToken = jsonLoginObject.getString(AUTH_TOKEN);
        JSONObject jsonAccountObj = jsonLoginObject.getJSONObject(ACCOUNT);
        Account account = createAccount(jsonAccountObj);
        
        return new Login(authToken, account);
	}

	static public Account createAccount(JSONObject jsonAccountObject) throws JSONException {
		Account account = Account.createAccount(jsonAccountObject);
		
		// Getting JSON User Array node
		JSONArray jsonUserArray = jsonAccountObject.getJSONArray(USERS);
		if (jsonUserArray.length() > 0)
			account.setUsers(createUserArray(jsonUserArray));
		return account;
	}

	static private User[] createUserArray(JSONArray jsonUserArray)
			throws JSONException {
		User users[] = new User[jsonUserArray.length()];

		// looping through All Users
		for (int i = 0; i < jsonUserArray.length(); i++) {
		    JSONObject userJson = jsonUserArray.getJSONObject(i);
		    users[i] = createUser(userJson);
		}
		return users;
	}

	static public User createUser(JSONObject jsonObj) throws JSONException {
		User user = User.createUser(jsonObj);
		
	    JSONArray activityArray = jsonObj.getJSONArray(USER_GRIDS);
	    if (activityArray.length() > 0)
	    	user.setGridActivities(createGridActivityArray(activityArray));

	    JSONArray prizesArray = jsonObj.getJSONArray(USER_PRIZES);
	    if (prizesArray.length() > 0)
	    	user.setPrizes(createPrizeArray(prizesArray));

	    if (jsonObj.has(PRIZE_DESCRIPTION)) {
	    	JSONObject jsonPrizeDescription = jsonObj.getJSONObject(PRIZE_DESCRIPTION);
	    	user.setPrizeDesscription(createPrizeDescriptionArray(jsonPrizeDescription));
	    }
	    
	    return user;
	}

	static private GridActivity[] createGridActivityArray(JSONArray activityArray)
			throws JSONException {
		GridActivity activity[] = new GridActivity[activityArray.length()];
	    // looping through All Grids
	    for (int j = 0; j < activityArray.length(); j++) {
	        JSONObject activityJson = activityArray.getJSONObject(j);
	        activity[j] = GridActivity.createGridActivity(activityJson);
	    }
		return activity;
	}

	static private Prize[] createPrizeArray(JSONArray prizeArray)
			throws JSONException {
		Prize prize[] = new Prize[prizeArray.length()];
	    // looping through All Grids
	    for (int j = 0; j < prizeArray.length(); j++) {
	        JSONObject prizeJson = prizeArray.getJSONObject(j);
	        prize[j] = Prize.createPrize(prizeJson);
	    }
		return prize;
	}
	
	static private String[] createPrizeDescriptionArray(JSONObject prizeDescriptionJson) {
		String prizeDescription[] = new String[5];
		try {
			for (int i = 0; i < prizeDescription.length; i++) {
				prizeDescription[i] = prizeDescriptionJson.getString(PRIZE_DESCRIPTION+(i+1));
			}
		} 
		catch (Exception e) {}
		
		return prizeDescription;
	}
	
	public static ArrayList<Branch> createBranches(String jsonStr) {
		ArrayList<Branch> branches = new ArrayList<Branch>();

    	Log.d("Response: ", "> " + jsonStr);

    	if (jsonStr != null) {
    		try {
    			JSONArray jsonArray = new JSONArray(jsonStr);
    			for (int i = 0; i < jsonArray.length(); i++) {
	    			JSONObject jsonObj = jsonArray.getJSONObject(i);
	    			branches.add(Branch.createBranch(jsonObj));
    			}
    		} catch (JSONException e) {
    			e.printStackTrace();
    		}
    	} else {
    		Log.e("ServiceHandler", "Couldn't get any data from the url");
    	}

    	return branches;
	}

	public static ArrayList<UserType> createUserTypes(String jsonStr) {
		ArrayList<UserType> userTypes = new ArrayList<UserType>();
		UserType userType = null;

    	Log.d("Response: ", "> " + jsonStr);

    	if (jsonStr != null) {
    		try {
    			JSONArray jsonArray = new JSONArray(jsonStr);
    			for (int i = 0; i < jsonArray.length(); i++) {
	    			JSONObject jsonObj = jsonArray.getJSONObject(i);
	    			userType = UserType.createUserType(jsonObj);
	    			userTypes.add(userType);
    			}
    		} catch (JSONException e) {
    			e.printStackTrace();
    		}
    	} else {
    		Log.e("ServiceHandler", "Couldn't get any data from the url");
    	}

		return userTypes;
	}

	
	public static ArrayList<GridCell> createGridCells(String jsonStr) {
		ArrayList<GridCell> gridCells = new ArrayList<GridCell>();
    	GridCell gridCell = null;

    	Log.d("Response: ", "> " + jsonStr);

    	if (jsonStr != null) {
    		try {
    			JSONObject jsonGrid = new JSONObject(jsonStr);
    			JSONArray jsonArray = jsonGrid.getJSONArray("grids");
    			for (int i = 0; i < jsonArray.length(); i++) {
	    			JSONObject jsonObj = jsonArray.getJSONObject(i);
	    			gridCell = GridCell.createGridCell(jsonObj);
	    			if (gridCell != null)
	    				gridCells.add(gridCell);
    			}
    		} catch (JSONException e) {
    			e.printStackTrace();
    		}
    	} else {
    		Log.e("ServiceHandler", "Couldn't get any data from the url");
    	}

		return gridCells;
	}

	public static ArrayList<PrizeDescription> createPrizeDescriptios(String jsonStr) {
		ArrayList<PrizeDescription> prizeDescriptions = new ArrayList<PrizeDescription>();

    	Log.d("Response: ", "> " + jsonStr);

    	if (jsonStr != null) {
    		try {
    			JSONArray jsonArray = new JSONArray(jsonStr);
    			for (int i = 0; i < jsonArray.length(); i++) {
	    			JSONObject jsonObj = jsonArray.getJSONObject(i);
	    			prizeDescriptions.add(PrizeDescription.createPrizeDescription(jsonObj));
    			}
    		} catch (JSONException e) {
    			e.printStackTrace();
    		}
    	} else {
    		Log.e("ServiceHandler", "Couldn't get any data from the url");
    	}

    	return prizeDescriptions;
	}

	public static ArrayList<PrizeDescription> createPrizeDescriptios(ArrayList<UserType> userType) {
		ArrayList<PrizeDescription> prizeDescriptions = new ArrayList<PrizeDescription>();

		for (int i = 0; i < userType.size(); i++) {
			prizeDescriptions.add(PrizeDescription.createPrizeDescription(userType.get(i).getName()));
		}
    	return prizeDescriptions;
	}

}
