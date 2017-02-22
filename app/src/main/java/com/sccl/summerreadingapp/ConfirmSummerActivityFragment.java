package com.sccl.summerreadingapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sccl.summerreadingapp.helper.MiscUtils;
import com.sccl.summerreadingapp.model.GridActivity;

public class ConfirmSummerActivityFragment  extends DialogFragment {
	GridActivity grid;
	
    public ConfirmSummerActivityFragment() {
          // Empty constructor required for DialogFragment
    }

    public static ConfirmSummerActivityFragment newInstance(String title, GridActivity grid) {
    	ConfirmSummerActivityFragment frag = new ConfirmSummerActivityFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putSerializable("grid", grid);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, 0);        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirm_summer_activity, container);
        String title = getArguments().getString("title");
        this.grid = (GridActivity)getArguments().getSerializable("grid");
        
        getDialog().setTitle("Confirm Activity");
        // Show soft keyboard automatically
        
        String[] parts = title.split(" ");
        if (parts.length > 1)
        {
        	StringBuffer sb = new StringBuffer();
        	for (int i = 0; i < parts.length; i++) {
        		if (parts[i].startsWith("http:")) {
        			String url = "<a href=\""+parts[i]+"\">" + parts[i] + "</a>";
        			
        			// <a href="http://www.sjpl.org/">let us know
            		sb.append(url);
        		}
        		else
        			sb.append(parts[i]);
    			sb.append(" ");
        	}
        	title = sb.toString();
        }
        
        final TextView titleText = (TextView) view.findViewById(R.id.textCinfirmSummerActivityTitle);
        titleText.setText(Html.fromHtml(title));
        
        final EditText noteText = (EditText) view.findViewById(R.id.txt_name);
        noteText.setText(grid.getNotes());

        Button okButton = (Button) view.findViewById(R.id.btn_ok);
        Button cancelButton = (Button) view.findViewById(R.id.btn_cancel);
        Button donebutton = (Button) view.findViewById(R.id.btnDone);

        if (grid.getType() == 1) {
        	noteText.setEnabled(false);
        	noteText.setInputType(InputType.TYPE_NULL);
        	
            okButton.setVisibility(View.INVISIBLE);            
            cancelButton.setVisibility(View.INVISIBLE);            
            
            donebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                	dismiss();
                }
               });
        }
        else {
            donebutton.setVisibility(View.INVISIBLE);

        	okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleButtonClick(noteText, v);
                }
               });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleButtonClick(noteText, v);
                }
               });

            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	        noteText.requestFocus();
        }
      return view;
    }
    
	private void handleButtonClick(final EditText noteText, View view) {
		
		if (grid.getType() != 1) {
	    	grid.setNotes(noteText.getText().toString());
	    	int type = 1;
	    	if (view.getId() == R.id.btn_cancel) {
	    		type = 0;
	    	}
	        grid.setType(type);
	        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
		}

    	dismiss();
	}
    
/*    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
         String title = getArguments().getString("title");
         AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
         alertDialogBuilder.setTitle(title);
         alertDialogBuilder.setMessage("Are you sure?");
         alertDialogBuilder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                   // on success
             }
         });
         alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 dialog.dismiss();
             }
         });

         return alertDialogBuilder.create();
    }
*/    
}