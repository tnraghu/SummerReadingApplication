package com.sccl.summerreadingapp;
import android.app.Application;
import android.content.Context;

import com.sccl.summerreadingapp.helper.SharedPreferenceHelper;
import com.sccl.summerreadingapp.model.Account;
import com.sccl.summerreadingapp.model.Config;
import com.sccl.summerreadingapp.model.User;
 
public class SummerReadingApplication extends Application {
 
    /**
     * Keeps a reference of the application context
     */
    private static Context sContext;
    
    private Account account;
    private User user;

	private Config config;

	private int userIndex = -1;
 
    public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
		// SharedPreferences userDetails = sContext.getSharedPreferences("Account", MODE_PRIVATE);
		// Account account = getAccountFromSharedPreferences(userDetails);
		Account account = SharedPreferenceHelper.getAccountFromSharedPreferences(sContext);
		if (account != null) {
			this.setAccount(account);
		}
		Config config = SharedPreferenceHelper.getConfigFromSharedPreferences(sContext);
		if (config != null) {
			this.setConfig(config);
		}
   }
 
    /**
     * Returns the application context
     *
     * @return application context
     */
    public static Context getContext() {
        return sContext;
    }
 
	public void setConfig(Config config) {
		this.config = config;
	}

	public Config getConfig() {
		return this.config;
	}

	public void setCurrentUserIndex(int userIndex) {
		this.userIndex = userIndex;
	}

	public int getCurrentUserIndex() {
		return this.userIndex;
	}

}
