package com.sccl.summerreadingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sccl.summerreadingapp.adapter.DailyReadingImageAdapter;
import com.sccl.summerreadingapp.clients.ReadingLogClient;
import com.sccl.summerreadingapp.helper.MiscUtils;
import com.sccl.summerreadingapp.helper.SharedPreferenceHelper;
import com.sccl.summerreadingapp.model.Account;
import com.sccl.summerreadingapp.model.Config;
import com.sccl.summerreadingapp.model.Prize;
import com.sccl.summerreadingapp.model.PrizeDescription;
import com.sccl.summerreadingapp.model.User;

public class DailyReadingFragment extends Fragment {
	private Account account;
	private User user;
	private DailyReadingImageAdapter imageAdapter;
	LinearLayout batteryLayout, robotImageLayout;
	Button addTwentyButton, removeTwentyButton;
	ImageView readImageView;
	private String userTypeName;
	private PrizeDescription prizeDescription;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_daily_reading, container, false);
         
        batteryLayout= (LinearLayout) rootView.findViewById(R.id.batteryLayout);
        robotImageLayout= (LinearLayout) rootView.findViewById(R.id.robotFullImageLayout);
        addTwentyButton = (Button) rootView.findViewById(R.id.btn_ok);
        removeTwentyButton = (Button) rootView.findViewById(R.id.btn_remove_20);
        
        GridView gridview = (GridView) rootView.findViewById(R.id.gridviewDailyReading);
        imageAdapter = new DailyReadingImageAdapter(container.getContext(), account, user);
		gridview.setAdapter(imageAdapter);

		readImageView = (ImageView) rootView.findViewById(R.id.readimage);

        addTwentyButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handleAddRemoveButtonClick(true);
			}
		});

        removeTwentyButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handleAddRemoveButtonClick(false);
			}
		});

/*		if (user!= null && user.getReadingLog() >= 45 * 20) {
			if (!isCentetGridStateToDone()) {
				setCentetGridStateToDone();
				updateAccountInSharedPreferences();
			}
			hideBatteryAndShowRobot(true);
			return rootView;
		}
*/        
		
        if (user!= null) {

			SummerReadingApplication summerReadingApplication = (SummerReadingApplication) getActivity().getApplicationContext();
			Config config = summerReadingApplication.getConfig();
	    	userTypeName = config.getUserTypeById(user.getUserType()).getName();
			prizeDescription = config.getPrizeDescription(userTypeName);

	    	int modSixHunderd = user.getReadingLog() % 600;
            int numberOfTenHours = user.getReadingLog() / 600;
// 			hideBatteryAndShowRobot(true);
			hideBatteryAndShowTenHourImages (modSixHunderd, numberOfTenHours);
			return rootView;
        }
  
/*        gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				handleAddTwenty();
			}

        });
*/
        return rootView;
    }
    
	private void handleAddRemoveButtonClick(boolean add) {
		if (add) {
			imageAdapter.addTwenty();
		}
		else {
			imageAdapter.removeTwenty();
		}
		if (MiscUtils.isNetworkAvailable(getActivity().getApplicationContext())) {
			// new GridActivityClient(getActivity(), user, selectedIndex).execute();
			new ReadingLogClient(getActivity(), user, user.getReadingLog()).execute();
		}
		else
			user.setReadingLogSync(true);
		
/*		if (user.getReadingLog() >= 900) {
			if (!isCentetGridStateToDone()) {
				setCentetGridStateToDone();
			}
		}
*/		
        Prize[] prizes = user.getPrizes();
        if (user.getReadingLog() / 600 > 0 && prizes[2].getState() != 1) {
        	prizes[2].setState(1);
            new PrizeImageHandler(getActivity(), userTypeName).displayPrizeMessage(prizeDescription, 2);
        }
    	updateAccountInSharedPreferences();
    	displayProgressMessage(getActivity(), user.getReadingLog() / 20);
	}

	private void displayProgressMessage(FragmentActivity activity, int readingLogDividedBy20) {
/*		if (readingLogDividedBy20 == 45) {
			hideBatteryAndShowRobot(true);
		}
*/		int readingLogMod30 = readingLogDividedBy20 % 30;
		if (readingLogMod30 == 0 || readingLogMod30 == 1 || readingLogMod30 == 29) {
		    int modSixHunderd = user.getReadingLog() % 600;
		    int numberOfTenHours = user.getReadingLog() / 600;
			hideBatteryAndShowTenHourImages (modSixHunderd, numberOfTenHours);
		}

		if (readingLogDividedBy20 % 9 == 0) {
			int byNine = readingLogDividedBy20 / 9;
			String message = "You have read " + byNine;
			
			if (byNine == 1)
				message += (" hour!");
			else
				message += (" hours!");
				
			// MiscUtils.showAlertDialog(getActivity(), "Great Job!", message);
		}
	}

	private void setCentetGridStateToDone() {
		// TODO Auto-generated method stub
		user.getGridActivities()[12].setType(1);
		((MainActivity)getActivity()).refreshPager(0);

		
		// save the grid in preferences
		// refresh the activity fragment
	}

	private boolean isCentetGridStateToDone() {
		return user.getGridActivities()[12].getType() == 1;
	}

	private void hideBatteryAndShowRobot(boolean readingFinished) {
		batteryLayout.setVisibility(readingFinished ? View.INVISIBLE : View.VISIBLE);
		robotImageLayout.setVisibility(readingFinished ? View.VISIBLE : View.INVISIBLE);
		addTwentyButton.setVisibility(readingFinished ? View.INVISIBLE : View.VISIBLE);
	}

	private void hideBatteryAndShowTenHourImages(int modSixHunderd, int numberOfTenHours) {
		
		if (modSixHunderd > 0 || numberOfTenHours == 0) {
			batteryLayout.setVisibility(View.VISIBLE);
			robotImageLayout.setVisibility(View.INVISIBLE);
		}
		else if (numberOfTenHours >= 1) {
			robotImageLayout.setVisibility(View.VISIBLE);
			batteryLayout.setVisibility(View.INVISIBLE);

			readImageView.setImageResource(R.drawable.read_design1 + numberOfTenHours - 1);			
		}
	}

	private void updateAccountInSharedPreferences() {
    	Context c = getActivity().getApplicationContext();
    	SharedPreferenceHelper.storeAccountDataIntoSharedPreferences(c, null, account);
    }

    public void setAccountAndSelectedUserIndex(Account account, int userIndex) {
    	// this.user = user;
    	this.account = account;
    	
    	if (account != null && userIndex >=0) {
    		User users[] = account.getUsers();
    		this.user = users[userIndex];
    	}
    	
    	if (user != null && imageAdapter != null) {
    		imageAdapter.setAccountAndUser(this.account, this.user);
    		
/*    		boolean readingLogDone = user.getReadingLog() >= 45 * 20;
			hideBatteryAndShowRobot(readingLogDone);

    		if (readingLogDone) {
				if (!isCentetGridStateToDone()) {
					setCentetGridStateToDone();
					updateAccountInSharedPreferences();
				}
    		}    		
*/
			SummerReadingApplication summerReadingApplication = (SummerReadingApplication) getActivity().getApplicationContext();
			Config config = summerReadingApplication.getConfig();
	    	userTypeName = config.getUserTypeById(user.getUserType()).getName();
			prizeDescription = config.getPrizeDescription(userTypeName);

			int modSixHunderd = user.getReadingLog() % 600;
            int numberOfTenHours = user.getReadingLog() / 600;
			hideBatteryAndShowTenHourImages (modSixHunderd, numberOfTenHours);
			
    	}
    }

    
}