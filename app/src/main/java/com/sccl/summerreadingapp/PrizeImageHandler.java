package com.sccl.summerreadingapp;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sccl.summerreadingapp.helper.MiscUtils;
import com.sccl.summerreadingapp.model.Config;
import com.sccl.summerreadingapp.model.Prize;
import com.sccl.summerreadingapp.model.PrizeDescription;
import com.sccl.summerreadingapp.model.User;
 
public class PrizeImageHandler {
    private Activity activity;
    String userType;
	private static final Map<String, Integer> IMAGE_MAP;
	static {
		Map<String, Integer>tempMap = new HashMap<String, Integer>();
		tempMap.put("Adult_bingo", R.drawable.win_adult_bingo);
		tempMap.put("Adult_drawing", R.drawable.win_adult_drawing);
		tempMap.put("Teen_bingo", R.drawable.win_teen_bingo);
		tempMap.put("Teen_drawing", R.drawable.win_teen_drawing);
		tempMap.put("Reader_bingo", R.drawable.win_reader_bingo);
		tempMap.put("Reader_drawing", R.drawable.win_reader_drawing);
		tempMap.put("Pre-Reader_bingo", R.drawable.win_prereader_bingo);
		tempMap.put("Pre-Reader_drawing", R.drawable.win_prereader_drawing);
		tempMap.put("STAFF SJPL_bingo", R.drawable.win_staff_bingo);
		tempMap.put("STAFF SJPL_drawing", R.drawable.win_staff_drawing);

		tempMap.put("reading", R.drawable.win_reading_log);

		tempMap.put("bingo_received", R.drawable.win_ready_to_receive);
		tempMap.put("drawing_received", R.drawable.win_ready_to_receive);
		tempMap.put("reading_received", R.drawable.win_ready_to_receive);

		tempMap.put("bingo_collected", R.drawable.win_bingo_collected);
		tempMap.put("drawing_collected", R.drawable.win_drawing_collected);
		tempMap.put("reading_collected", R.drawable.win_reading_log_collected);
		IMAGE_MAP = Collections.unmodifiableMap(tempMap);
	}	
	
	public PrizeImageHandler(Activity activity, String userType) {
		super();
		this.activity = activity;
		this.userType = userType;
	}

	public void setImagesAndAssignClickHandler(View rootView, Prize[] prizes) {
        setPrizeImages(rootView, prizes);
        setPrizeImageText(rootView, prizes);
        handlePrizeImageClick(rootView, prizes);
	}

	
	private void setPrizeImageText(View rootView, Prize[] prizes) {
		if (prizes == null || prizes.length < 2) {
			return;
		}
		setPrizeImageTextBasedOnState(rootView, prizes[0], R.id.textPrizeTitle1);
		setPrizeImageTextBasedOnState(rootView, prizes[1], R.id.textPrizeTitle2);
		setPrizeImageTextBasedOnState(rootView, prizes[2], R.id.textPrizeTitle3);
	}

	private void setPrizeImageTextBasedOnState(View rootView, Prize prize, int prizeTextId) {
		String prizeText = "";
		if (prize.getState() == 1) {
			prizeText += "Ready to Receive";
		}
		else if (prize.getState() == 2) {
			prizeText += "Collected!";
		}
		if (prizeText.length() != 0) {
			TextView prizeTextView = (TextView)rootView.findViewById(prizeTextId);
			prizeTextView.setText(prizeText);
		}
	}

	public void handlePrizeImageClick(View rootView, Prize[] prizes) {
		// int[] resourceIds = {R.id.prize1, R.id.prize2, R.id.prize3, R.id.prize4, R.id.prize5};
		int[] resourceIds = {R.id.prize1, R.id.prize2, R.id.prize3};
		
		for (int i = 0; i < resourceIds.length; i++) {
			setPrizeImageClickListener((ImageView) rootView.findViewById(resourceIds[i]), 
				getPrizeTitle(prizes[i]), getPrizeMessage(prizes[i]));
		}

	}

	public void setPrizeImages(View rootView, Prize[] prizes) {
		// if (prizes != null && prizes.length > 3) {
		if (prizes != null && prizes.length > 2) {
			setPrizeImageBasedOnState(rootView, prizes[0], R.id.prize1);
			setPrizeImageBasedOnState(rootView, prizes[1], R.id.prize2);
			setPrizeImageBasedOnState(rootView, prizes[2], R.id.prize3);
/*			setPrizeImageBasedOnState(rootView, prizes[3], R.id.prize4);
			setPrizeImageBasedOnState(rootView, prizes[4], R.id.prize5);
*/		}
	}

	private String getPrizeTitle(Prize prize) {
		String title = prize.getState() == 1 ? "Great Job" : "Good Luck";
		return title;
	}

	private String getPrizeMessage(Prize prize) {
    	String message = prize.getState() == 1 ? 
        	"Congratulations! You have won a prize. Visit your local San Jose Public Library to pick up prizes from June 2 to July 31." :
        	"Complete 5 squares in a row (vertical, horizontal, or diagonal) to win a prize. Visit your local library to pick up prizes from June 2 to July 31.";
/*    	String message = "Complete 5 squares in a row (vertical, horizontal, or diagonal) to win a prize. Visit your local library to pick up prizes from June 2 to July 31.";
    	if (prize.getState() == 1)
    		message = "Congratulations! You have won a prize. Visit your local San Jose Public Library to pick up prizes from June 2 to July 31.";
*/    	return message;
	}

	private void setPrizeImageClickListener(ImageView prizeView, String title, String message) {
    	final String finalTitle = title;
		final String finalMessage = message;
		prizeView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (v.getId() == R.id.prize1 || v.getId() == R.id.prize2)
				{
					((MainActivity)activity).setCurrentPagerItem(1);
				}
				else
				{
					((MainActivity)activity).setCurrentPagerItem(0);
				}
				// MiscUtils.showAlertDialog(activity, finalTitle, finalMessage);
            }
        });
	}

	private void setPrizeImageBasedOnState(View rootView, Prize prize, int prizeResourceId) {
		String suffix = "bingo";

		if (prizeResourceId == R.id.prize2) {
			suffix = "drawing";
		}
		if (prizeResourceId == R.id.prize3) {
			suffix = "reading";
		}

		if (prize.getState() == 1) {
			suffix += "_received";
		}
		else if (prize.getState() == 2) {
			suffix += "_collected";
		}
		else if (prizeResourceId != R.id.prize3) {
			suffix = userType + "_" + suffix;
		}
		
/*		int imageId = R.drawable.prize_not_ready;
		if (prize.getState() == 1) {
			imageId = R.drawable.prize_1_ready + prizeResourceId - R.id.prize1;
		}
		else if (prize.getState() == 2) {
			imageId = R.drawable.prize_claimed_badge;
		}
*/		int imageId = IMAGE_MAP.get(suffix);
		ImageView prizeView = (ImageView) rootView.findViewById(prizeResourceId);
		prizeView.setImageResource(imageId);
	}

	private String[][] prizePrefixAndSuffix = {
							{"You've won the following prize for completing 4 activity squares in a row: ", " Please visit your local San Jose Public Library to claim your prize. Complete the entire activity grid for a chance to win more prizes!"},
							{"For completing all activities, you've won: ", " The library will contact you if you win. Please make sure you have a valid phone number or email listed on your account."},
							{"You've won the following prize for completing 10 hours of reading: ", " Keep tracking your reading to earn more reading badges!"}
						};
	public void displayPrizeMessage(PrizeDescription prizeDescription, int i) {
		if (prizeDescription != null) {
			String message = prizePrefixAndSuffix[i][0] + "<b>" + prizeDescription.getDescriptions()[i] + ".</b>" + prizePrefixAndSuffix[i][1];
			MiscUtils.showAlertDialog(activity, "Great Job!", message);
		}
	}

}
/*
You've won the following prize for completing 4 activity squares in a row: Coloring Book and Crayons.
Please visit your local San Jose Public Library to claim your prize. 
Complete the entire activity grid for a chance to win more prizes! */