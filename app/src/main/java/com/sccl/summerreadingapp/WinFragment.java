package com.sccl.summerreadingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sccl.summerreadingapp.model.Account;
import com.sccl.summerreadingapp.model.Config;
import com.sccl.summerreadingapp.model.GridActivity;
import com.sccl.summerreadingapp.model.GridCell;
import com.sccl.summerreadingapp.model.Prize;
import com.sccl.summerreadingapp.model.User;

public class WinFragment extends Fragment {
	private static final int TOTAL_ROWS = 4;
	private static final int TOTAL_COLUMNS = 4;
	private static final int TOTAL_PRIZES = 3;
	
	View rootView;
	User user;
	int selectedIndex = -1;
	private Account account;
	GridCell gridCell;
	private boolean refresh;
	String userTypeName;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        rootView = inflater.inflate(R.layout.fragment_summer_win, container, false);

		SummerReadingApplication summerReadingApplication = (SummerReadingApplication) getActivity().getApplicationContext();
		Config config = summerReadingApplication.getConfig();

        if (user != null) {
        	userTypeName = config.getUserTypeById(user.getUserType()).getName();
            new PrizeImageHandler(getActivity(), userTypeName).setImagesAndAssignClickHandler(rootView, user.getPrizes());
        	gridCell = config.getGridCell(user.getUserType());
        	setHoursAndMinutesTextViewAndBadgeImage();        	
        }
		
        return rootView;
    }

	public void setAccountAndSelectedUserIndex(Account account, int userIndex) {
    	// this.user = user;
    	this.account = account;
    	
    	if (account != null && userIndex >=0) {
    		User users[] = account.getUsers();
    		this.user = users[userIndex];
    	}
    	
    	refreshImageAndPrizes();
    }

	public void refreshImageAndPrizes() {
		if (user != null && rootView != null) {
    		SummerReadingApplication summerReadingApplication = (SummerReadingApplication) getActivity().getApplicationContext();
    		Config config = summerReadingApplication.getConfig();
        	gridCell = config.getGridCell(user.getUserType());
        	userTypeName = config.getUserTypeById(user.getUserType()).getName();
        	prizeWon(getActivity(), userTypeName, rootView);
        	setHoursAndMinutesTextViewAndBadgeImage();        	
    	}
	}

	public void setRefreshImageAndPrizes(boolean refresh) {
		this.refresh = refresh;
	}

	public boolean getRefreshImageAndPrizes() {
		return this.refresh;
	}

	private void setHoursAndMinutesTextViewAndBadgeImage() {
		String hoursAndMinutes = "";
		int readingLog = user.getReadingLog();
		if (readingLog > 0) {
			int numberOfHours = readingLog / 60;
			int numberOfMinutes = readingLog % 60;
			if (numberOfHours > 0) {
				hoursAndMinutes = numberOfHours + " Hours";
			}
			
			if (numberOfHours > 0 && numberOfMinutes > 0) {
				hoursAndMinutes += " & ";
			}
			if (numberOfMinutes > 0) {
				hoursAndMinutes += numberOfMinutes + " Minutes";
			}
		}
		else {
			hoursAndMinutes = "Have Not Started";			
		}
		TextView hoursAndMinutesTextView = (TextView)rootView.findViewById(R.id.textReadHoursAndMins);
		hoursAndMinutesTextView.setText(hoursAndMinutes);
		setBingoImage(rootView, readingLog);
	}

	private void prizeWon(Context context, String userTypeName, View rootView)
    {
    	if (user == null)
    		return;

    	GridActivity[] userGrid = user.getGridActivities();
    	if (userGrid.length < TOTAL_ROWS * TOTAL_COLUMNS)
    		return;

        Prize[] prizes = user.getPrizes();
        if (prizes.length < TOTAL_PRIZES)
        	return;
    	
    	int total = countOfCompletedRows(userGrid);
    			
    	total += countOfCompletedColumns(userGrid);
    	
		if (isLeftDiagonalCompleted(userGrid)) {
			total++;
		}

		if (isRightDiagonalCompleted(userGrid)) {
			total++;
		}

        setPrizes(prizes, total, user.getReadingLog());

        new PrizeImageHandler(getActivity(), userTypeName).setImagesAndAssignClickHandler(rootView, prizes);
        
        // MiscUtils.displayToastMessage(context, "Prizes="+total);
    }

	private void setPrizes(Prize[] prizes, int total, int readingLog) {
		if (total > 0 && prizes[0].getState() == 0)
        	prizes[0].setState(1);

/*        if (total > 1 && prizes[1].getState() == 0)
        	prizes[1].setState(1);

        if (total > 2 && prizes[2].getState() == 0)
        	prizes[2].setState(1);

        if (total > 3 && prizes[3].getState() == 0)
        	prizes[3].setState(1);

        if (total > 11 && prizes[4].getState() == 0)
        	prizes[4].setState(1);
*/
        if (total > 9 && prizes[1].getState() == 0)
        	prizes[1].setState(1);
        
        if (readingLog / 600 > 0) {
        	prizes[2].setState(1);
        }
	}

	private boolean isRightDiagonalCompleted(GridActivity[] userGrid) {
		boolean completed = true;
    	for (int i = TOTAL_COLUMNS - 1, totalIterations = 0; totalIterations < TOTAL_ROWS; i+= (TOTAL_COLUMNS - 1), totalIterations++) {
    		if (userGrid[i].getType() != 1) {
				completed = false;
				break;
    		}
    	}
		return completed;
	}

	private boolean isLeftDiagonalCompleted(GridActivity[] userGrid) {
		boolean completed = true;
    	for (int i = 0; i < TOTAL_ROWS * TOTAL_COLUMNS; i += (TOTAL_COLUMNS + 1)) {
    		if (userGrid[i].getType() != 1) {
    			completed = false;
				break;
    		}
    	}
		return completed;
	}

	private int countOfCompletedColumns(GridActivity[] userGrid) {
		int total = 0;
		for (int i = 0; i < TOTAL_COLUMNS; i++) {
    		boolean good = true;
    		for (int j = i, totalIterations = 0;  totalIterations < TOTAL_ROWS; j += TOTAL_COLUMNS, totalIterations++) {
    			if (userGrid[j].getType() != 1) {
    				good = false;
    				break;
    			}
    		}

    		if (good) {
    			total++;
    		}
    			
    	}
		return total;
	}

	private int countOfCompletedRows(GridActivity[] userGrid) {
		int total = 0;
		for (int i = 0; i < TOTAL_ROWS; i++) {
    		int firstIndex = i * TOTAL_COLUMNS;
    		boolean good = true;
    		for (int j = firstIndex; j < firstIndex + TOTAL_COLUMNS; j++) {
    			if (userGrid[j].getType() != 1) {
    				good = false;
    				break;
    			}
    		}

    		if (good) {
    			total++;
    		}
    			
    	}
		return total;
	}

//	int badgeIds[] = {R.id.badge1, R.id.badge2, R.id.badge3, R.id.badge1};
	private void setBingoImage(View rootView, int readingLogMinutes) {
		int readingLogMinutesInTenHours = readingLogMinutes / 600;
		for (int i = 0; i < 5; i++) {
			setReadingBadgeImage(rootView, R.id.badge1 + i, 
					(i < readingLogMinutesInTenHours) ? R.drawable.design1 + i : R.drawable.designoff);
		}

/*		if (readingLogMinutesInTenHours == 0) {
			for (int i = 0; i < readingLogMinutesInTenHours && i < 5; i++) {
				setReadingBadgeImage(rootView, R.id.badge1 + i, R.drawable.designoff);
			}
		}
		else {
			for (int i = 0; i < readingLogMinutesInTenHours && i < 5; i++) {
				setReadingBadgeImage(rootView, R.id.badge1 + i, R.drawable.design1 + i);
			}
		}
*/	}

	private void setReadingBadgeImage(View rootView, int badgeResourceId, int imageId) {
		ImageView prizeView = (ImageView) rootView.findViewById(badgeResourceId);
		prizeView.setImageResource(imageId);
	}
}