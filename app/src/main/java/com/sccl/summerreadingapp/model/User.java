package com.sccl.summerreadingapp.model;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 820256012561578272L;
	private static String ID = "id"; 
	private static String FIRST_NAME = "firstName"; 
	private static String LAST_NAME = "lastName"; 
	private static String AGE = "age"; 
	private static String USER_TYPE_ID = "userType"; 
	private static String READING_LOG = "readingLog"; 
	
	private String id;
	private String firstName;
	private String lastName;
	private int age;
	private String userType;
	private GridActivity[] activities;
	private Prize[] prizes;
	private int readingLog;
	private String[] createPrizeDescriptionArray;
	private boolean readingLogSync;
	
	static public User createUser (JSONObject jsonObj)
	{
		try {
			String id = jsonObj.getString(ID);
			String firstName = jsonObj.getString(FIRST_NAME);
			String lastName = jsonObj.getString(LAST_NAME);
			int age = -1;
			if (jsonObj.has(AGE))
				try {
					age = jsonObj.getInt(AGE);
				} catch (JSONException e) {
				}
			String userType = jsonObj.getString(USER_TYPE_ID);
			int readingLog = jsonObj.getInt(READING_LOG);
	
			return new User(id, firstName, lastName, userType, readingLog, age);
		} catch (JSONException e) {
			return null;
		}
	}
	
	public User (String id, String firstName, String lastName, String userType, int readingLog, int age)
	{
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userType = userType;
		this.readingLog = readingLog;
		this.age = age;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public GridActivity[] getGridActivities() {
		return this.activities;
	}

	public void setGridActivities(GridActivity[] activity) {
		this.activities = activity;
	}

	public Prize[] getPrizes() {
		return this.prizes;
	}

	public void setPrizes(Prize[] prizes) {
		this.prizes = prizes;
	}
	
	public int getReadingLog() {
		return readingLog;
	}

	public void setReadingLog(int readingLog) {
		this.readingLog = readingLog;
	}

	public void addTwentyToReadingLog() {
		this.readingLog += 20;
	}

	public void removeTwentyToReadingLog() {
		if (this.readingLog > 0) {
			this.readingLog -= 20;
		}
	}

	public String toJSON(){

	    JSONObject jsonObject = this.toJSONObject(true);
	    return jsonObject.toString();
	}	

	public JSONObject toJSONObject(boolean addChildren){
	    JSONObject jsonObject = new JSONObject();
	    try {
	        jsonObject.put(ID, getId());
	        jsonObject.put(FIRST_NAME, getFirstName());
	        jsonObject.put(LAST_NAME, getLastName());
	        int age = getAge();
	        if (age != -1)
	        	jsonObject.put(AGE, age);
	        jsonObject.put(USER_TYPE_ID, getUserType());

	        if (addChildren) {
		        JSONArray jsonGridArray = new JSONArray();
		        GridActivity[] grid = getGridActivities();
		        for (int i = 0; grid != null && i < grid.length; i++){
		        	jsonGridArray.put(grid[i].toJSONObject());
		        }
		        jsonObject.put("activityGrid", jsonGridArray);
	
		        JSONArray jsonPrizeArray = new JSONArray();
		        Prize[] prizes = getPrizes();
		        for (int i = 0; prizes != null && i < prizes.length; i++){
		        	jsonPrizeArray.put(prizes[i].toJSONObject());
		        }
		        jsonObject.put("prizes", jsonPrizeArray);

		        jsonObject.put(READING_LOG, getReadingLog());
		        
		        if (createPrizeDescriptionArray != null) {
	        		JSONObject prizeDescription = new JSONObject(); 
		        	for (int i = 0; i < createPrizeDescriptionArray.length; i++) {
		        		prizeDescription.put("prize"+i+1, createPrizeDescriptionArray[i]);
		        	}
		        	jsonObject.put("prize", prizeDescription);
		        }
	        }
	    } catch (JSONException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	        return null;
	    }
	    return jsonObject;	    
	}

	public void setPrizeDesscription(String[] createPrizeDescriptionArray) {
		this.createPrizeDescriptionArray = createPrizeDescriptionArray;
	}

	public void setReadingLogSync(boolean b) {
		this.readingLogSync = b;
	}	

	public boolean getReadingLogSync() {
		return this.readingLogSync;
	}	
}
