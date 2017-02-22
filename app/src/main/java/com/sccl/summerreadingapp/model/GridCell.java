package com.sccl.summerreadingapp.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GridCell {
	private static String ID = "id"; 
	private static String USER_TYPE = "userType"; 
	private static String CELLS = "cells"; 
	private static String CELL_INDEX = "index"; 
	private static String CELL_DESC = "description"; 
	private static String GRID_ICON = "gridIcon"; 
	
	private String id;
	private String userType;
	
	private CellData cellData[]; 

	public class CellData {
		int index;
		String description;
		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getGridIcon() {
			return gridIcon;
		}

		public void setGridIcon(String gridIcon) {
			this.gridIcon = gridIcon;
		}

		String gridIcon;
		
		CellData(int index, String desc, String gridIcon) {
			this.index = index;
			this.description = desc;
			this.gridIcon = gridIcon;
		}
	}

	static public GridCell createGridCell (JSONObject jsonObj) {
		try {
			String id = jsonObj.getString(ID);
			String userType = jsonObj.getString(USER_TYPE);
			JSONArray cells = jsonObj.getJSONArray(CELLS);
	
			return new GridCell(id, userType, cells);
		} catch (JSONException e) {
			return null;
		}
	}
	
	public GridCell (String id, String name, JSONArray cells) {
		this.id = id;
		this.userType = name;
		cellData = new CellData[cells.length()];
		try {
			for (int i = 0; i < cells.length(); i ++) {
				JSONObject jsonObject = cells.getJSONObject(i);
				cellData[i] = new CellData(jsonObject.getInt(CELL_INDEX), jsonObject.getString(CELL_DESC), jsonObject.getString(GRID_ICON));
			}
		} catch (JSONException e) {
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String name) {
		this.userType = name;
	}

	public CellData[] getCellData() {
		return cellData;
	}

	public void setCellData(CellData[] cellData) {
		this.cellData = cellData;
	}


	public String toJSON(){

	    JSONObject jsonObject= new JSONObject();
	    try {
	        jsonObject.put(ID, getId());
	        return jsonObject.toString();
	    } catch (JSONException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	        return "";
	    }
	}
}
