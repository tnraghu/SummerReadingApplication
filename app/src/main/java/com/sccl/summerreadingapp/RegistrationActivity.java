package com.sccl.summerreadingapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.sccl.summerreadingapp.clients.RegistrationClient;
import com.sccl.summerreadingapp.helper.MiscUtils;
import com.sccl.summerreadingapp.model.Branch;
import com.sccl.summerreadingapp.model.Config;
import com.sccl.summerreadingapp.model.Login;
 
public class RegistrationActivity extends Activity implements RegistrationAsyncListener{
    private static final int REQUEST_CODE_REGISTER = 1;
    Config config;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set View to register.xml
        setContentView(R.layout.register);
 
        TextView loginScreen = (TextView) findViewById(R.id.link_to_login);
        Button button = (Button) findViewById(R.id.btnRegister);

		SummerReadingApplication summerReadingApplication = (SummerReadingApplication) getApplicationContext();
		config = summerReadingApplication.getConfig();
		
	    populateBranches(config.getBranches());
        
		button.setOnClickListener(new View.OnClickListener() {
 
			@Override
			public void onClick(View v) {
			    EditText userNameEdit = (EditText) findViewById(R.id.reg_username);
			    String userName = userNameEdit.getText().toString();
			    
			    EditText passwordEdit = (EditText) findViewById(R.id.reg_password);
			    String password = passwordEdit.getText().toString();


			    if (MiscUtils.empty(userName) || MiscUtils.empty(password)) {
					MiscUtils.showAlertDialog(RegistrationActivity.this, "Error", "User Name and Password cannot be empty!");
					return;
			    }
			    
			    
			    EditText emailEdit = (EditText) findViewById(R.id.reg_email);
			    String email = emailEdit.getText().toString();

			    EditText phoneEdit = (EditText) findViewById(R.id.reg_phone);
			    String phone = phoneEdit.getText().toString();
			    
			    // EditText fullNameEdit = (EditText) findViewById(R.id.reg_fullname);
			    // String fullName = fullNameEdit.getText().toString();
			    
				Spinner branchSpinner = (Spinner) findViewById(R.id.branch);
			    String branchSpinnerText = String.valueOf(branchSpinner.getSelectedItem()); 
			    
			    Branch branch = config.getBranch(branchSpinnerText);
			    String branchId = branch.getId();
			    //String branch = "C36F2906-1173-459D-B5BC-73AD058673A3";
			    
				Spinner languageSpinner = (Spinner) findViewById(R.id.language);
			    String languageSpinnerText = String.valueOf(languageSpinner.getSelectedItem()); 

			    if (MiscUtils.isNetworkAvailable(getApplicationContext())) {
				    new RegistrationClient(RegistrationActivity.this, RegistrationActivity.this).execute(userName, password, email, branchId, languageSpinnerText, phone);
				    // new RegistrationClient(RegistrationActivity.this, RegistrationActivity.this).execute(userName, password, email, fullName, branchId);
				}
				else {
					MiscUtils.showAlertDialog(RegistrationActivity.this, "Network Error", "User not registered. You need to enable network to login.");
				}
			}
 
		});
 
        // Listening to Login Screen link
        loginScreen.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View arg0) {
                // Switching to Register screen
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                // startActivity(i);
        		startActivityForResult(i, REQUEST_CODE_REGISTER);
            }
        });
    }

	private void populateBranches(ArrayList<Branch> branchList) {
		 
		Spinner branchSpinner = (Spinner) findViewById(R.id.branch);
		List<String> branchNames = new ArrayList<String>();
		for (Branch branch:branchList) {
			branchNames.add(branch.getName());
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, branchNames);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		branchSpinner.setAdapter(dataAdapter);
	  }
	
	@Override
	public void onResult(Login account) {
		 Intent returnIntent = new Intent();
		 returnIntent.putExtra("login", account);
		 setResult(RESULT_OK,returnIntent);     
		 finish();			
		
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK){
			if (requestCode == REQUEST_CODE_REGISTER)
				return;
		}
	}//onActivityResult
}