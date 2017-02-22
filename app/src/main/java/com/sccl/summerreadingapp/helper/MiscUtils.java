package com.sccl.summerreadingapp.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Html;
import android.widget.Toast;

public class MiscUtils {
	static String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

	public static  boolean isNetworkAvailable(Context c) {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	public static void displayToastMessage(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	public static boolean empty(String text) {
		return text == null || text.length() == 0;
	}

	public static void showAlertDialog(Activity activity, String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(title);
		builder.setMessage(Html.fromHtml(message));
		builder.setCancelable(false);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   dialog.dismiss();
	           }
	       });
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
	}

	public static ProgressDialog createProgressDialog(Activity parentActivity) {
		ProgressDialog pDialog = new ProgressDialog(parentActivity);
        pDialog.setMessage("Adding User...");
        pDialog.setCancelable(false);
        pDialog.show();
        return pDialog;
	}

	public static Date parseDateString(String dateString) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); 

		
		Date parsedDate = null;
		try {
			parsedDate = simpleDateFormat.parse(dateString);
		} catch (ParseException e) {
		}
		return parsedDate;
	}
	
	public static String convertDateToString(Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); 

		return simpleDateFormat.format(date); 
	}

	public static String getCurrentTimeInString() {
		long now = System.currentTimeMillis();
		return convertDateToString(new Date(now));
	}
}
