package com.sccl.summerreadingapp.model;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class Prize implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1148630055572789680L;
	private static String STATE = "state"; 
	private static String NOTES = "notes"; 
	private static String UPDATED_AT = "updatedAt"; 
	
	private int state;
	private String notes;
	private Date lastUpdated;
	
	
	static public Prize createPrize (JSONObject jsonObj)
	{
		try {
			int state = jsonObj.getInt(STATE);
			String notes = jsonObj.getString(NOTES);
			String dateString = ""; // jsonObj.getString(UPDATED_AT);
			Date d = null;
			if (dateString != null)
			{
//				d = new Date(dateString);
			}
	
			return new Prize(state, notes, d);
		} catch (JSONException e) {
			return null;
		}
	}
	
	public Prize (int state, String notes, Date lastUpdated)
	{
		this.state = state;
		this.notes = notes;
		//this.lastUpdated = lastUpdated;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String toJSON(){
	    JSONObject jsonObject = this.toJSONObject();
	    return jsonObject.toString();
	}	

	public JSONObject toJSONObject(){

	    JSONObject jsonObject= new JSONObject();
	    try {
	        jsonObject.put(STATE, getState());
	        jsonObject.put(NOTES, getNotes());
//	        jsonObject.put(UPDATED_AT, getLastUpdated());
	    } catch (JSONException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	    return jsonObject;
	}	
}
