package com.sccl.summerreadingapp.adapter;

import java.util.Locale;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sccl.summerreadingapp.DailyReadingFragment;
import com.sccl.summerreadingapp.InformationFragment;
import com.sccl.summerreadingapp.R;
import com.sccl.summerreadingapp.SummerActivityFragment;
import com.sccl.summerreadingapp.WinFragment;
import com.sccl.summerreadingapp.model.Account;
import com.sccl.summerreadingapp.model.GridActivity;


/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    GridActivity[] data = null;
	// private User user;
	SummerActivityFragment summer;
	WinFragment win;
	private Account account;
	private int userIndex = -1;

    public SectionsPagerAdapter(FragmentManager fm, Context c) {
        super(fm);
        mContext = c;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
        case 0:
        	DailyReadingFragment daily =  new DailyReadingFragment();
        	daily.setAccountAndSelectedUserIndex(account, userIndex);
        	return daily;
        case 1:
        	summer =  new SummerActivityFragment();
        	summer.setAccountAndSelectedUserIndex(account, userIndex);
        	return summer;
        case 2:
//            return new InformationFragment();
        	win =  new WinFragment();
        	win.setAccountAndSelectedUserIndex(account, userIndex);
        	return win;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
          case 0:
              return mContext.getString(R.string.title_daily_reading);
          case 1:
              return mContext.getString(R.string.title_summer_activity);
          case 2:
              return mContext.getString(R.string.title_daily_win);
        }
        return null;
    }
    
	// public void setAccountAndSelectedUserIndex(FragmentManager fm, User user, Account account, int userIndex) {
	public void setAccountAndSelectedUserIndex(FragmentManager fm, Account account, int userIndex) {
    	// this.user = user;
    	this.account = account;
    	this.userIndex  = userIndex;
    	
    	// User users[] = account.getUsers();
    	// this.user = users[userIndex];
    	
    	SummerActivityFragment fragment = (SummerActivityFragment) fm.findFragmentByTag(
  	                       "android:switcher:"+R.id.pager+":1");
	  	if(fragment != null)  {
	  		fragment.setAccountAndSelectedUserIndex(account, userIndex);
	  	}

    	DailyReadingFragment dailyFragment = (DailyReadingFragment) fm.findFragmentByTag(
                  "android:switcher:"+R.id.pager+":0");
		if(dailyFragment != null)  {
			dailyFragment.setAccountAndSelectedUserIndex(account, userIndex);
		}

    	WinFragment winFragment = (WinFragment) fm.findFragmentByTag(
                  "android:switcher:"+R.id.pager+":2");
    	if(winFragment != null)  {
    		winFragment.setAccountAndSelectedUserIndex(account, userIndex);
    	}

	}

	public void refreshPager(FragmentManager fm) {
    	SummerActivityFragment fragment = (SummerActivityFragment) fm.findFragmentByTag(
                  "android:switcher:"+R.id.pager+":1");
    	if(fragment != null)  {
    		// fragment.setAccountAndSelectedUserIndex(user, account, userIndex);
    		fragment.refreshImageAndPrizes();
    	}

    	WinFragment winFragment = (WinFragment) fm.findFragmentByTag(
                "android:switcher:"+R.id.pager+":2");
    	if(winFragment != null)  {
    		winFragment.refreshImageAndPrizes();
    	}

	}
	

}

