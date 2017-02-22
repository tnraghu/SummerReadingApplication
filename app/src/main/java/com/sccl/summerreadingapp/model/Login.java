package com.sccl.summerreadingapp.model;

import java.io.Serializable;

public class Login implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6657996738770000046L;
	private String authToken;
	private Account account;
	private String loginJSONResponse;
	
	public Login (String authToken, Account account)
	{
		this.authToken = authToken;
		this.account = account;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getLoginJSONResponse() {
		return this.loginJSONResponse;
	}
	public void setLoginJSONResponse(String jsonStr) {
		this.loginJSONResponse = jsonStr;
	}
}
