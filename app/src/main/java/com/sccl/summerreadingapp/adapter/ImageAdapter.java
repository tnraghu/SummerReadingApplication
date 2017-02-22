package com.sccl.summerreadingapp.adapter;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.sccl.summerreadingapp.R;
import com.sccl.summerreadingapp.model.GridActivity;
import com.sccl.summerreadingapp.model.GridCell;

public class ImageAdapter extends BaseAdapter implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -3395985378568915487L;
	private Context mContext;
    private GridActivity[] gridData;
    int imageSize;
	private GridCell gridCell;
	
	private static final Map<String, Integer> IMAGE_MAP;
	static
	{
		Map<String, Integer>tempMap = new HashMap<String, Integer>();
		tempMap.put("READ", R.drawable.grid_read_disabled);
		tempMap.put("LEARN", R.drawable.grid_learn_disabled);
		tempMap.put("DISCOVER", R.drawable.grid_explore_disabled);
		//tempMap.put("EXPLORE", R.drawable.grid_explore_disabled);
		tempMap.put("ROBOT", R.drawable.grid_robot_disabled);
		tempMap.put("ENERGIZE", R.drawable.grid_energize_disabled);
		tempMap.put("READ_DONE", R.drawable.grid_read_enabled);
		tempMap.put("LEARN_DONE", R.drawable.grid_learn_enabled);
//		tempMap.put("EXPLORE_DONE", R.drawable.grid_explore_enabled);
		tempMap.put("DISCOVER_DONE", R.drawable.grid_explore_enabled);
		tempMap.put("ROBOT_DONE", R.drawable.grid_robot_enabled);
		tempMap.put("ENERGIZE_DONE", R.drawable.grid_energize_enabled);
		IMAGE_MAP = Collections.unmodifiableMap(tempMap);
	}	

    public ImageAdapter(Context c, GridActivity[] gridData, GridCell gridCell) {
        mContext = c;
        this.gridData = gridData;
        this.gridCell = gridCell;
        imageSize = (int) mContext.getResources().getDimension(R.dimen.image_size);

    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }
    
    public void setGridData(GridActivity[] data, GridCell gridCell)
    {
    	gridData = data;
    	this.gridCell = gridCell;
    	super.notifyDataSetChanged();
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

    	ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(imageSize, imageSize));

            // MiscUtils.displayToastMessage(mContext, "size="+imageSize);
            //imageView.setLayoutParams(new GridView.LayoutParams(180, 180));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(2, 2, 2, 2);
        } else {
            imageView = (ImageView) convertView;
        }

        int resId = mThumbIdsEnabled[position];
        
        if (gridData != null && position < gridData.length) {
        	GridCell.CellData cellDataArray[] = gridCell.getCellData();
        	
        	GridCell.CellData cellData = cellDataArray[position];
        	String icon = cellData.getGridIcon();
        	
        	GridActivity activity = gridData[position];
        	if (activity != null && activity.getType() == 1)
        	{
        		resId = mThumbIds[position];
        		icon = icon + "_DONE";
        	}
        	resId = IMAGE_MAP.get(icon);
        	
        }
        imageView.setImageResource(resId);
        return imageView;
    
/*
    	ImageView imageView = (ImageView) convertView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(2, 2, 2, 2);
        } else {
            imageView = (ImageView) convertView;
        }
    	Picasso.with(mContext).load("http://i.imgur.com/DvpvklR.png")
    		.placeholder(R.drawable.grid_0_0_d)
    		.error(R.drawable.grid_0_0_e)
    		.resize(175, 175)
    		//.resize(imageView.getMeasuredWidth(), imageView.getMeasuredWidth())
    		.centerCrop().into(imageView);
    	return imageView;
*/    	
    	}

    private Integer[] mThumbIds = {
            R.drawable.grid_energize_enabled, R.drawable.grid_explore_enabled, R.drawable.grid_learn_enabled, R.drawable.grid_read_enabled, 
            R.drawable.grid_energize_enabled, R.drawable.grid_explore_enabled, R.drawable.grid_learn_enabled, R.drawable.grid_read_enabled, 
            R.drawable.grid_energize_enabled, R.drawable.grid_explore_enabled, R.drawable.grid_learn_enabled, R.drawable.grid_read_enabled, 
            R.drawable.grid_energize_enabled, R.drawable.grid_explore_enabled, R.drawable.grid_learn_enabled, R.drawable.grid_read_enabled 
    };
    // references to our images
    private Integer[] mThumbIdsEnabled = {
            R.drawable.grid_energize_disabled, R.drawable.grid_explore_disabled, R.drawable.grid_learn_disabled, R.drawable.grid_read_disabled, 
            R.drawable.grid_energize_disabled, R.drawable.grid_explore_disabled, R.drawable.grid_learn_disabled, R.drawable.grid_read_disabled, 
            R.drawable.grid_energize_disabled, R.drawable.grid_explore_disabled, R.drawable.grid_learn_disabled, R.drawable.grid_read_disabled, 
            R.drawable.grid_energize_disabled, R.drawable.grid_explore_disabled, R.drawable.grid_learn_disabled, R.drawable.grid_read_disabled 
    };

/*    private Integer[] mThumbIds = {
            R.drawable.grid_robot_enabled, R.drawable.grid_energize_enabled,
            R.drawable.grid_explore_enabled, R.drawable.grid_learn_enabled,
            R.drawable.grid_read_enabled, 
            R.drawable.grid_robot_enabled, R.drawable.grid_energize_enabled,
            R.drawable.grid_explore_enabled, R.drawable.grid_learn_enabled,
            R.drawable.grid_read_enabled, 
            R.drawable.grid_robot_enabled, R.drawable.grid_energize_enabled,
            R.drawable.grid_explore_enabled, R.drawable.grid_learn_enabled,
            R.drawable.grid_read_enabled, 
            R.drawable.grid_robot_enabled, R.drawable.grid_energize_enabled,
            R.drawable.grid_explore_enabled, R.drawable.grid_learn_enabled,
            R.drawable.grid_read_enabled, 
            R.drawable.grid_robot_enabled, R.drawable.grid_energize_enabled,
            R.drawable.grid_explore_enabled, R.drawable.grid_learn_enabled,
            R.drawable.grid_read_enabled 
    };
    // references to our images
    private Integer[] mThumbIdsEnabled = {
            R.drawable.grid_robot_disabled, R.drawable.grid_energize_disabled,
            R.drawable.grid_explore_disabled, R.drawable.grid_learn_disabled,
            R.drawable.grid_read_disabled, 
            R.drawable.grid_robot_disabled, R.drawable.grid_energize_disabled,
            R.drawable.grid_explore_disabled, R.drawable.grid_learn_disabled,
            R.drawable.grid_read_disabled, 
            R.drawable.grid_robot_disabled, R.drawable.grid_energize_disabled,
            R.drawable.grid_explore_disabled, R.drawable.grid_learn_disabled,
            R.drawable.grid_read_disabled, 
            R.drawable.grid_robot_disabled, R.drawable.grid_energize_disabled,
            R.drawable.grid_explore_disabled, R.drawable.grid_learn_disabled,
            R.drawable.grid_read_disabled, 
            R.drawable.grid_robot_disabled, R.drawable.grid_energize_disabled,
            R.drawable.grid_explore_disabled, R.drawable.grid_learn_disabled,
            R.drawable.grid_read_disabled 
    };
*/
}