package com.sccl.summerreadingapp.model;

import org.json.JSONException;
import org.json.JSONObject;

public class UserType {

/*	static Map<String, String> idToDescription = new HashMap<String, String>();
	static {
		idToDescription.put("C2747BE4-E0C9-45AC-9E50-549F43B49D31", "Reader");
		idToDescription.put("3E7CAEB5-90BF-4109-8A1E-5703D8FDC063", "Pre-Reader");
		idToDescription.put("FA97845D-7C8D-4C2C-8B80-C46B5DB6A03C", "Teen");
		idToDescription.put("49DABF22-C5BE-48F3-9119-5C8DDFF781B8", "Adult");
		// idToDescription.put(null, "Reader");
	}
	
	static public String getDescription(String id) {
		return idToDescription.get(id);
	}
	
	static public String getId(String desc) {
	    for (Entry<String, String> entry : idToDescription.entrySet()) {
	        if (desc.equals(entry.getValue())) {
	            return entry.getKey();
	        }
	    }
		return "C2747BE4-E0C9-45AC-9E50-549F43B49D31";
	}
	
*/	private static String ID = "id"; 
	private static String NAME = "name"; 
	private static String DESCRIPTION = "description"; 
	private static String MIN_AGE = "minAge"; 
	private static String MAX_AGE = "maxAge"; 
	
	private String id;
	private String name;
	private String description;
	private int minAge;
	private int maxAge;
	private String[] prizeDescriptionArray; // TBD -- remove this. Check below for more comments
	
	static public UserType createUserType (JSONObject jsonObj)
	{
		try {
			String id = jsonObj.getString(ID);
			String name = jsonObj.getString(NAME);
			String desc = jsonObj.getString(DESCRIPTION);
			int minAge = jsonObj.getInt(MIN_AGE);
			int maxAge = jsonObj.getInt(MAX_AGE);
			
			// TBD
			// This should be completely refactored. The prize description should come from the
			// backend and parsed and PrizeDescription object should be created and then used.
			// REMOVE THIS
			String prizeDescriptionArray [] = new String[5];
	
			return new UserType(id, name, desc, minAge, maxAge);
		} catch (JSONException e) {
			return null;
		}
	}
	
	public UserType(String id, String name, String desc, int minAge, int maxAge) {
		this.id = id;
		this.name = name;
		this.description = desc;
		this.setMinAge(minAge);
		this.setMaxAge(maxAge);
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getMinAge() {
		return minAge;
	}

	public void setMinAge(int minAge) {
		this.minAge = minAge;
	}

	public int getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
	}

}
