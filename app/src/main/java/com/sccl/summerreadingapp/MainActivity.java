package com.sccl.summerreadingapp;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.sccl.summerreadingapp.adapter.SectionsPagerAdapter;
import com.sccl.summerreadingapp.clients.AccountClient;
import com.sccl.summerreadingapp.clients.ConfigClient;
import com.sccl.summerreadingapp.helper.MiscUtils;
import com.sccl.summerreadingapp.helper.SharedPreferenceHelper;
import com.sccl.summerreadingapp.model.Account;
import com.sccl.summerreadingapp.model.Login;
import com.sccl.summerreadingapp.model.User;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener, ConfigAsyncListener, AccountAsyncListener {
	private static final int REQUEST_CODE_LOGIN = 1;
	private static final int REQUEST_CODE_SELECT_USER = 2;
	private static final int REQUEST_CODE_ADD_USER = 3;

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link FragmentPagerAdapter} derivative, which will keep every
	 * loaded fragment in memory. If this becomes too memory intensive, it
	 * may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}

		});

		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(
					actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

		SummerReadingApplication summerReadingApplication = (SummerReadingApplication) getApplicationContext();
		boolean networkAvailable = MiscUtils.isNetworkAvailable(getApplicationContext());
		if (summerReadingApplication.getAccount() != null) {
    		if (networkAvailable) {
    			new AccountClient(MainActivity.this, MainActivity.this, summerReadingApplication.getAccount().getId()).execute();
    		}
    		else {
    			summerReadingApplication.setCurrentUserIndex(-1);
    			handleLoggedInUser(summerReadingApplication);
    		}
		}
		// TBD - Refactor the below condition. 
		else if (summerReadingApplication.getConfig() != null && summerReadingApplication.getConfig().getBranchesString() != null) {
			startLoginActivity();
		}
		else {
    		if (networkAvailable) {
    		    new ConfigClient(MainActivity.this, MainActivity.this, true).execute();
    		}
    		else {
    			MiscUtils.showAlertDialog(this, "Error", "Please enable network connection to use this application first time.");
    			// finish(); // can't do this here as execution comes here even before the dialog is shown.
    		}
		}

	}

	private void handleLoggedInUser(SummerReadingApplication summerReadingApplication) {
		// Account account = getAccountFromSharedPreferences(userDetails);
		Account account = summerReadingApplication.getAccount();
		if (account != null) {
			boolean started = startChooseUserActivity(account);
			if (!started) {
				startAddUserActivity(account);
			}
		}
	}

	private void startAddUserActivity(Account account) {
		Intent i = new Intent(getApplicationContext(), AddUserActivity.class);
		i.putExtra("accountId", account.getId());
		startActivityForResult(i, REQUEST_CODE_ADD_USER);
	}
	
	private boolean startChooseUserActivity(Account account) {
		User users[] = account.getUsers();
		if (users != null && users.length > 0) {
			String userNames[] = new String[users.length];
			String userTypes[] = new String[users.length];
			for (int i = 0; i < users.length; i++) {
				userNames[i] = users[i].getFirstName();
				userTypes[i] = users[i].getUserType();
			}
			Intent i = new Intent(getApplicationContext(), ChooseUserFromListActivity.class);
			i.putExtra("user", userNames);
			i.putExtra("userType", userTypes);
			i.putExtra("accountId", account.getId());
			startActivityForResult(i, REQUEST_CODE_SELECT_USER);
			return true;
		}
		return false;
	}

	private void startLoginActivity() {
		Intent i = new Intent(getApplicationContext(), LoginActivity.class);
		startActivityForResult(i, REQUEST_CODE_LOGIN);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK){
			if (requestCode == REQUEST_CODE_LOGIN && handleLoginResult(data))
				return;
			if (requestCode == REQUEST_CODE_SELECT_USER && handleChooseUserResult(data))
				return;
			if (requestCode == REQUEST_CODE_ADD_USER && handleAddUserResult(data))
				return;
		}
		finish();
	}//onActivityResult

	private boolean handleAddUserResult(Intent data) {
		ArrayList<User> userList = (ArrayList<User>)data.getSerializableExtra("user");
		// Get the account again and see if any users added. If only one added, may be start the actual activity. Otherwise, ask the user to select.
		SummerReadingApplication summerReadingApplication = (SummerReadingApplication) getApplicationContext();
		Account account = summerReadingApplication.getAccount();
		User users[] = account.getUsers();
		if (users == null || users.length == 0) {
			return handleAddingFirstTimeUsers(userList, summerReadingApplication, account);
		}
		else 
			return handleAddingNewUsers(userList, account, users);
	}

	private boolean handleAddingNewUsers(ArrayList<User> userList, Account account,
			User[] users) {
		if (userList.size() != 0) {
			User[] newUsers = new User[users.length + userList.size()];
			System.arraycopy(users, 0, newUsers, 0, users.length);
			System.arraycopy(userList.toArray(), 0, newUsers, users.length, userList.size());
			account.setUsers(newUsers);
			SharedPreferenceHelper.storeAccountDataIntoSharedPreferences(getApplicationContext(), null, account);
		}
		return startChooseUserActivity(account);
		// return true;
	}

	private boolean handleAddingFirstTimeUsers(ArrayList<User> userList,
			SummerReadingApplication summerReadingApplication, Account account) {
		if (userList.size() == 0)
			return false;
		User[] users = new User[userList.size()];
		System.arraycopy(userList.toArray(), 0, users, 0, userList.size());
		account.setUsers(users);
		SharedPreferenceHelper.storeAccountDataIntoSharedPreferences(getApplicationContext(), null, account);

/*		if (users.length == 1) {
			String user = users[0].getFirstName();
			displayToastMessage("User Selected "+ user);
			mSectionsPagerAdapter.setAccountAndSelectedUserIndex(getSupportFragmentManager(), account, 0);
			summerReadingApplication.setUser(users[0]);
			summerReadingApplication.setCurrentUserIndex(0);
			getSupportActionBar().setTitle(user);
			
			return true;
		}
*/		// Choose user from list
		return startChooseUserActivity(account);
	}

	private boolean handleChooseUserResult(Intent data) {
		SummerReadingApplication summerReadingApplication = (SummerReadingApplication) getApplicationContext();

		int restart = data.getIntExtra("restart", -1);
		if (restart == 1) {
			return handleAddUserResult(data);
		}
		
		int userIndex = data.getIntExtra("index", -1);
		int currentIndex = summerReadingApplication.getCurrentUserIndex();

		if (userIndex != -1) {
			if (userIndex == currentIndex)
				return true;
		}

		if (userIndex == -1) {
			// userIndex = currentIndex;
			if (currentIndex >= 0)
				return true;
			userIndex = 0;
		}
		
//		if (userIndex == -1) {
//			userIndex = 0;
//		}

		// I should optimize even when user preses the back button (userIndex -1) case.
		// Here if there is already a current index set, I should return true just like when selected index and current index are same
		// The problem is ApplicationContext object initializes ONLY once. Even when the app closes, context does not 
		// go away. So this can lead to a serious problem of app not starting at all when try to restart and if you hit the 
		// back button. MAY BE REST SummerReadingApplication.setCurrentUserIndex(-1) during onCreate???
		
		// if (userIndex == -1)
			//return false;
		
		// SharedPreferences userDetails = getApplicationContext().getSharedPreferences("Account", MODE_PRIVATE);
		// Account account = getAccountFromSharedPreferences(userDetails);

		Account account = summerReadingApplication.getAccount();
		if (account != null) {
   			User users[] = account.getUsers();
			if (users != null && userIndex < users.length) {
				String user = users[userIndex].getFirstName();
				if (!MiscUtils.empty(user)) {
					summerReadingApplication.setUser(users[userIndex]);
					summerReadingApplication.setCurrentUserIndex(userIndex);
					displayToastMessage("User Selected "+ user);
					getSupportActionBar().setTitle(account.getName() + "/" + user);
					// mSectionsPagerAdapter.setActivityGridData(users[userIndex].getGridActivities());
					// mSectionsPagerAdapter.setAccountAndSelectedUserIndex(getSupportFragmentManager(), users[userIndex], account, userIndex);
					mSectionsPagerAdapter.setAccountAndSelectedUserIndex(getSupportFragmentManager(), account, userIndex);
					// SummerReadingApplication summerReadingApplication = (SummerReadingApplication) getApplicationContext();
					// summerReadingApplication.setAccount(account);
					return true;
				}
			}
		}
		return false;
	}

	private boolean handleLoginResult(Intent data) {
		Login login = (Login)data.getSerializableExtra("user");
		if (login != null) {
			Account account = login.getAccount();				
			displayToastMessage("User signed in "+ account.getName());
			// storeDataIntoSharedPreferences(login, account);
			SharedPreferenceHelper.storeAccountDataIntoSharedPreferences(getApplicationContext(), login, account);
			SummerReadingApplication summerReadingApplication = (SummerReadingApplication) getApplicationContext();
			summerReadingApplication.setAccount(account);

			boolean started = startChooseUserActivity(account);
			if (!started) {
				startAddUserActivity(account);
			}
			return true;
		}
		return false;

/*			
			User users[] = account.getUsers();
			
			if (users != null && users.length > 0) {
				String userNames[] = new String[users.length];
				String userTypes[] = new String[users.length];
			
				for (int i = 0; i < users.length; i++) {
					userNames[i] = users[i].getFirstName();
					userTypes[i] = users[i].getUserType();
				}
				Intent i = new Intent(getApplicationContext(), ChooseUserFromListActivity.class);
				i.putExtra("user", userNames);
				i.putExtra("userType", userTypes);
				startActivityForResult(i, REQUEST_CODE_SELECT_USER);
				return true;
			}
			else {
				startAddUserActivity(account);
				return true;
			}
		}
		return false;
*/
	}

	private void displayToastMessage(String message) {
		MiscUtils.displayToastMessage(getApplicationContext(), message);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		SummerReadingApplication summerReadingApplication = (SummerReadingApplication) getApplicationContext();
		switch (item.getItemId()) {
/*		case R.id.miAddUser:
			displayToastMessage("Add selected");
			startAddUserActivity(summerReadingApplication.getAccount());
			break;

		case R.id.action_settings:
			break;

*/		case R.id.miChangeUser:
			startChooseUserActivity(summerReadingApplication.getAccount());
			break;
		default:
			super.onOptionsItemSelected(item);
			break;
		}
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onResult() {
		SummerReadingApplication summerReadingApplication = (SummerReadingApplication) getApplicationContext();
		if (summerReadingApplication.getConfig() != null && summerReadingApplication.getConfig().getBranchesString() != null) {
			startLoginActivity();
		}
		else {
			MiscUtils.showAlertDialog(this, "Error", "Error getting the configuration information. Please check your network connection..");
			finish();
		}
	}

	public void setCurrentPagerItem(int item) { 
		mViewPager.setCurrentItem(item); 
	} 

	public void refreshPager(int item) { 
		mSectionsPagerAdapter.refreshPager(getSupportFragmentManager());
	} 

	@Override
	public void onAccountClientResult() {
		// TODO Auto-generated method stub
	    new ConfigClient(MainActivity.this, MainActivity.this, false).execute();

		SummerReadingApplication summerReadingApplication = (SummerReadingApplication) getApplicationContext();
		summerReadingApplication.setCurrentUserIndex(-1);
		handleLoggedInUser(summerReadingApplication);
	}
}
