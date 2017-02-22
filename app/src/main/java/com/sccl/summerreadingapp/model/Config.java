package com.sccl.summerreadingapp.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Config implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8083517946400541784L;
	private ArrayList<Branch> branches;
	private ArrayList<GridCell> gridCells;
	private ArrayList<UserType> userTypes;
	private String branchesString;
	private String userTypesString;
	private String gridCellsString;
	private ArrayList<PrizeDescription> prizeDescriptions;
	private String prizeDescriptionsString;
	
	public ArrayList<Branch> getBranches() {
		return branches;
	}
	public void setBranches(ArrayList<Branch> branches) {
		this.branches = branches;
	}
	public ArrayList<GridCell> getGridCells() {
		return gridCells;
	}
	public void setGridCells(ArrayList<GridCell> gridCells) {
		this.gridCells = gridCells;
	}
		
	public GridCell getGridCell(String userType)
	{
		for (GridCell gridCell:this.gridCells) {
			if (userType.equalsIgnoreCase(gridCell.getUserType()))
				return gridCell;
		}
		return null;
	}

	public Branch getBranch(String branchName)
	{
		for (Branch branch:this.branches) {
			if (branchName.equalsIgnoreCase(branch.getName()))
				return branch;
		}
		return null;
	}

	public UserType getUserType(String userTypeName)
	{
		for (UserType userType:this.userTypes) {
			if (userTypeName.equalsIgnoreCase(userType.getDescription()))
				return userType;
		}
		return null;
	}

	public UserType getUserTypeById(String id)
	{
		for (UserType userType:this.userTypes) {
			if (id.equalsIgnoreCase(userType.getId()))
				return userType;
		}
		return null;
	}

	public ArrayList<UserType> getUserTypes() {
		return userTypes;
	}
	public void setUserTypes(ArrayList<UserType> userTypes) {
		this.userTypes = userTypes;
	}
	public void setBranchesString(String branchesStr) {
		this.branchesString = branchesStr;
	}
	public void setUserTypesString(String userTypesStr) {
		this.userTypesString = userTypesStr;
	}
	public void setGridCellsString(String gridCellsStr) {
		this.gridCellsString = gridCellsStr;
	}
	public void setPrizeDescriptionsString(String prizeDescriptionsString) {
		this.prizeDescriptionsString = prizeDescriptionsString;
	}
	public String getBranchesString() {
		return branchesString;
	}
	public String getUserTypesString() {
		return userTypesString;
	}
	public String getGridCellsString() {
		return gridCellsString;
	}
	public String getPrizeDescriptionsString() {
		return this.prizeDescriptionsString;
	}
	public void setPrizeDescriptions(ArrayList<PrizeDescription> prizeDescriptions) {
		this.prizeDescriptions = prizeDescriptions;
	}
	public ArrayList<PrizeDescription> getPrizeDescriptions() {
		return this.prizeDescriptions;
	}

	public PrizeDescription getPrizeDescription(String userType)
	{
		for (PrizeDescription prizeDescription:this.prizeDescriptions) {
			if (userType.equalsIgnoreCase(prizeDescription.getUserType()))
				return prizeDescription;
		}
		return null;
	}

}
