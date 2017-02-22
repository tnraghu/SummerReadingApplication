package com.sccl.summerreadingapp.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.sccl.summerreadingapp.R;
import com.sccl.summerreadingapp.helper.Constants;

public class PrizeDescription implements Serializable {
	
private String[] descriptions;
private String userType;
private static final Map<String, String[]> DESCRIPTION_MAP;
static {
	Map<String, String[]>tempMap = new HashMap<String, String[]>();
	tempMap.put("Adult", new String[]{"Tote Bag", "Entry in Prize Drawing", "Entry in Prize Drawing"});
	tempMap.put("Teen", new String[]{"Spiral Notebook","Entry in Prize Drawing","Book"});
	tempMap.put("Reader", new String[]{"Pencil Pouch","Entry in Prize Drawing","Book"});
	tempMap.put("Pre-Reader", new String[]{"Coloring Book and Crayons","Entry in Prize Drawing","Book  plus Children's Discovery Museum Ticket"});
	tempMap.put("STAFF SJP", new String[]{"Entry in Prize Drawing","Entry in Prize Drawing","Entry in Prize Drawing"});
	DESCRIPTION_MAP = Collections.unmodifiableMap(tempMap);
}	


	static public PrizeDescription createPrizeDescription (JSONObject jsonObj) {
//		try {
			String userType = "";
			String descriptions[] = new String [Constants.TOTAL_PRIZES];
			String dateString = ""; // jsonObj.getString(UPDATED_AT);
			Date d = null;
			if (dateString != null) {
//				d = new Date(dateString);
			}
	
			return new PrizeDescription(userType, descriptions,d);
/*		} catch (JSONException e) {
			return null;
		}
*/	}
	
	static public PrizeDescription createPrizeDescription (String userType) {
//		try {
			String dateString = ""; // jsonObj.getString(UPDATED_AT);
			Date d = null;
			if (dateString != null) {
//				d = new Date(dateString);
			}
	
			return new PrizeDescription(userType, DESCRIPTION_MAP.get(userType),d);
/*		} catch (JSONException e) {
			return null;
		}
*/	}
	

	public PrizeDescription (String userType, String[] descriptions, Date lastUpdated) {
		this.descriptions = descriptions;
		this.userType = userType;
		//this.lastUpdated = lastUpdated;
	}

	public String[] getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(String[] descriptions) {
		this.descriptions = descriptions;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
/*	public void setLastUpdated(Date lastUpdated) {
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
*/
}
