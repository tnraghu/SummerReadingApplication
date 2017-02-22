package com.sccl.summerreadingapp.model;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Account implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2940990904607310934L;
	private static String ID = "id"; 
	private static String NAME = "accountName"; 
	private static String EMAIL_ADDRESS = "emailAddress"; 
	private static String BRANCH_ID = "branchId"; 
	private static String ROLE = "role";

	private static final String USERS = "users";
	
	private String id;
	private String name;
	private String emailAddress;
	private String branchId;
	private String role;
	private User[] users;
	
	static public Account createAccount (JSONObject jsonObj)
	{
		try {
			String id = jsonObj.getString(ID);
			String name = jsonObj.getString(NAME);
			String emailAddress = jsonObj.getString(EMAIL_ADDRESS);
			String branchId = jsonObj.getString(BRANCH_ID);
			String role = jsonObj.getString(ROLE);
			
			return new Account(id, name, emailAddress, branchId, role);
		} catch (JSONException e) {
			return null;
		}
	}
	
	public Account (String id, String name, String emailAddress, String branchId, String role)
	{
		this.id = id;
		this.name = name;
		this.emailAddress = emailAddress;
		this.branchId = branchId;
		this.role = role;
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

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public User[] getUsers() {
		return this.users;
	}

	public void setUsers(User[] users) {
		this.users = users;
	}
	
	public String toJSON(){

	    JSONObject jsonObject = new JSONObject();
	    try {
	        jsonObject.put(ID, getId());
	        jsonObject.put(NAME, getName());
	        jsonObject.put(EMAIL_ADDRESS, getEmailAddress());
	        jsonObject.put(BRANCH_ID, getBranchId());
	        jsonObject.put(ROLE, getRole());
	        
	        JSONArray jsonUserArray = new JSONArray();
	        User[] users = getUsers();
	        if (users != null) {
		        for (int i = 0; i < users.length; i++){
		        	jsonUserArray.put(users[i].toJSONObject(true));
		        }
	        }
	        jsonObject.put(USERS, jsonUserArray);

	        return jsonObject.toString();
	    } catch (JSONException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	        return "";
	    }
	}

	public String toJSONRegister(){

	    JSONObject jsonObject = new JSONObject();
	    try {
	        jsonObject.put(ID, "");
	        jsonObject.put(NAME, getName());
	        jsonObject.put(EMAIL_ADDRESS, getEmailAddress());
	        jsonObject.put(BRANCH_ID, getBranchId());
	        return jsonObject.toString();
	    } catch (JSONException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	        return "";
	    }
	}
}
