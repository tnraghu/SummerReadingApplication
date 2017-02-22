package com.sccl.summerreadingapp.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Branch {
	private static String ID = "id"; 
	private static String NAME = "name"; 
	private static String ADDRESS = "location"; 
	private static String HOURS = "hoursOfOperation"; 
	
	private String id;
	private String name;
	private String address;
	private String hoursOfOperation;
	
	static public Branch createBranch (JSONObject jsonObj)
	{
		try {
			String id = jsonObj.getString(ID);
			String name = jsonObj.getString(NAME);
			String address = jsonObj.getString(ADDRESS);
			String hours = jsonObj.getString(HOURS);
	
			return new Branch(id, name, address, hours);
		} catch (JSONException e) {
			return null;
		}
	}
	
	public Branch (String id, String name, String address, String hourOfOperation)
	{
		this.id = id;
		this.name = name;
		this.address = address;
		this.hoursOfOperation = hourOfOperation;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHoursOfOperation() {
		return hoursOfOperation;
	}

	public void setHoursOfOperation(String hoursOfOperation) {
		this.hoursOfOperation = hoursOfOperation;
	}

	public String toJSON(){

	    JSONObject jsonObject= new JSONObject();
	    try {
	        jsonObject.put(ID, getId());
	        jsonObject.put(NAME, getName());
	        jsonObject.put(ADDRESS, getAddress());
	        jsonObject.put(HOURS, getHoursOfOperation());

	        return jsonObject.toString();
	    } catch (JSONException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	        return "";
	    }
	}
}
