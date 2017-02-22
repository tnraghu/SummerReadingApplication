package com.sccl.summerreadingapp.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.sccl.summerreadingapp.clients.GridActivityClient;
import com.sccl.summerreadingapp.helper.MiscUtils;

public class GridActivity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5574204293047294079L;
	private static final String SAVE_TO_SERVER = "saveToServer";
	private static String TYPE = "activity"; 
	private static String NOTES = "notes"; 
	private static String UPDATED_AT = "updatedAt"; 
	
	private int type;
	private String notes;
	private String lastUpdated;
	private boolean saveToServer;
	
	
	static public GridActivity createGridActivity (JSONObject jsonObj)
	{
		try {
			int type = jsonObj.getInt(TYPE);
			String notes = jsonObj.getString(NOTES);
			String dateString = "";
			boolean saveToServer = false;
			try {
				if (jsonObj.has(UPDATED_AT))
					dateString = jsonObj.getString(UPDATED_AT);
				if (jsonObj.has(SAVE_TO_SERVER))
					saveToServer = jsonObj.getBoolean(SAVE_TO_SERVER);
				} catch (JSONException e) {
			}
			
			// If notes has text or type is 1, date will be empty ONLY if it was saved from the older version of app.
			// In that case, lets sync it once just to be sure.
        	if ((!MiscUtils.empty(notes) || type == 1) && MiscUtils.empty(dateString)) {
    			dateString = MiscUtils.getCurrentTimeInString();
    			saveToServer = true;
			}
        	
			return new GridActivity(type, notes, dateString, saveToServer);
		} catch (JSONException e) {
			return null;
		}
	}
	
	private GridActivity (int type, String notes, String lastUpdated, boolean saveToServer)
	{
		this.type = type;
		this.notes = notes;
		this.lastUpdated = lastUpdated;
		this.saveToServer = saveToServer;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public void saveToServer(boolean b) {
		this.saveToServer = b;
	}	
	
	public boolean getSaveToServer() {
		return this.saveToServer;
	}	
	

	public String toJSON(){

	    JSONObject jsonObject = this.toJSONObject();
	    return jsonObject.toString();
	}	

	public JSONObject toJSONObject(){

	    JSONObject jsonObject= new JSONObject();
	    try {
	        jsonObject.put(TYPE, getType());
	        jsonObject.put(NOTES, getNotes());
	        jsonObject.put(UPDATED_AT, getLastUpdated());
	        jsonObject.put(SAVE_TO_SERVER, getSaveToServer());
	    } catch (JSONException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
        return jsonObject;
	}
}
