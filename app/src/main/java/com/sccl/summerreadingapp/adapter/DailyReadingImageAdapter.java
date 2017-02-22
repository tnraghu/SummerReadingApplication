package com.sccl.summerreadingapp.adapter;

import java.io.Serializable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.sccl.summerreadingapp.R;
import com.sccl.summerreadingapp.helper.MiscUtils;
import com.sccl.summerreadingapp.model.Account;
import com.sccl.summerreadingapp.model.User;

public class DailyReadingImageAdapter extends BaseAdapter implements Serializable{
    private static final int IMAGE_COUNT = 30;

	/**
	 * 
	 */
	private static final long serialVersionUID = -6698923475338075042L;
	
	private Context mContext;
    int batteryWidth, batteryHeight, imageSize;
	Account account;
	User user;


    public DailyReadingImageAdapter(Context c, Account account, User user) {
        mContext = c;
        this.account = account;
        this.user = user;
        // imageSize = (int) mContext.getResources().getDimension(R.dimen.image_size);
        batteryWidth = (int) mContext.getResources().getDimension(R.dimen.battery_image_width);
        batteryHeight = (int) mContext.getResources().getDimension(R.dimen.battery_image_height);
        imageSize = (int) mContext.getResources().getDimension(R.dimen.image_size);
    }

    public int getCount() {
        // return 45; //mThumbIds.length;
        return IMAGE_COUNT; //mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }
    
    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

    	ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(imageSize, imageSize));

            // MiscUtils.displayToastMessage(mContext, "size="+imageSize);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //imageView.setPadding(2, 2, 2, 2);
        } else {
            imageView = (ImageView) convertView;
        }
        
        int id = android.R.color.transparent;
        
        int modSixHunderd = user != null ? user.getReadingLog() % 600 : 0;
        int twentyAdded = modSixHunderd / 20;
//        if (position >= 45 - twentyAdded) {
//        if (position >= IMAGE_COUNT - twentyAdded) {
        if (position <= twentyAdded - 1) {
        	// id = R.drawable.robotactivated01 + position;
        	// id = R.drawable.batteryon01 + position;

            // Last best version
        	// id = R.drawable.appbatteryon01 + position;

        	// id = R.drawable.appbatteryon201 + position;
        	id = R.drawable.read_checkmark;
        }
        imageView.setImageResource(id);
        
        return imageView;
    	}


    public View getViewOld(int position, View convertView, ViewGroup parent) {

    	ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            // imageView.setLayoutParams(new GridView.LayoutParams(370, 100));
            // MiscUtils.displayToastMessage(mContext, "w="+batteryWidth + "h="+batteryHeight);
            // imageView.setLayoutParams(new GridView.LayoutParams(batteryWidth, batteryHeight));
            // imageView.setLayoutParams(new GridView.LayoutParams(370, 200));
            // imageView.setLayoutParams(new GridView.LayoutParams(260, 200));
            // imageView.setLayoutParams(new GridView.LayoutParams(208, 160));

            // Good for 9 by 5
            // imageView.setLayoutParams(new GridView.LayoutParams(192, 146));
            imageView.setLayoutParams(new GridView.LayoutParams(batteryWidth, batteryHeight));
            
            // below for 15 by 3
            // imageView.setLayoutParams(new GridView.LayoutParams(122, 94));

            // imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            
/*            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            int width = display.getWidth();  // deprecated
            int height = display.getHeight();  // deprecated
            MiscUtils.displayToastMessage(mContext, "w="+width + "h="+height);
*/            
        } else {
            imageView = (ImageView) convertView;
        }
        
//        int id = R.drawable.appbatteryoff201 + position;
        // int id = R.drawable.read_checkmark_transparent;
        int id = android.R.color.transparent;
        
        int modSixHunderd = user != null ? user.getReadingLog() % 600 : 0;
        // int twentyAdded = user != null ? user.getReadingLog() / 20 : 0;
        int twentyAdded = modSixHunderd / 20;
//        if (position >= 45 - twentyAdded) {
        if (position >= 30 - twentyAdded) {
        	// id = R.drawable.robotactivated01 + position;
        	// id = R.drawable.batteryon01 + position;

            // Last best version
        	// id = R.drawable.appbatteryon01 + position;

        	// id = R.drawable.appbatteryon201 + position;
        	id = R.drawable.read_checkmark;
        }
        // imageView.setImageResource(id);
        
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), id);
        if (bitmap != null) {
        	Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
        	imageView.setImageBitmap(resizedBitmap);
        }

        // int id = R.drawable.robotactivated01;
        // imageView.setImageResource(position + id);
        return imageView;
    	}

    public void addTwenty() {
		if (user != null) {
			user.addTwentyToReadingLog();
			this.notifyDataSetChanged();
    	}
	}

    public void removeTwenty() {
		if (user != null) {
			user.removeTwentyToReadingLog();
			this.notifyDataSetChanged();
    	}
	}

	public void setAccountAndUser(Account account, User user) {
		// TODO Auto-generated method stub
		this.account = account;
		this.user = user;
    	this.notifyDataSetChanged();
	}
}