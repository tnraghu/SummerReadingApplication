package com.sccl.summerreadingapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.sccl.summerreadingapp.clients.AddUserClient;
import com.sccl.summerreadingapp.helper.MiscUtils;
import com.sccl.summerreadingapp.model.Config;
import com.sccl.summerreadingapp.model.User;
import com.sccl.summerreadingapp.model.UserType;
 
public class AddUserActivity extends Activity implements AddUserAsyncListener {
	String accountId;
	ArrayList<User> userList = new ArrayList<User>();
	private ArrayList<UserType> readerTypeList;
	Config config;
	private ArrayList<UserType> listWithoutStaff = new ArrayList<UserType>();

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_user);
        accountId = (String)getIntent().getStringExtra("accountId");
        
		SummerReadingApplication summerReadingApplication = (SummerReadingApplication) getApplicationContext();
		config = summerReadingApplication.getConfig();
		
		readerTypeList = config.getUserTypes();
        
		populateUserTypes();
    }

	private void populateUserTypes() {
		 
		Spinner readerTypeSpinner = (Spinner) findViewById(R.id.reader_type);
		List<String> readerTypeNames = new ArrayList<String>();
		for (UserType userType:readerTypeList) {
			if (userType.getName().indexOf("STAFF") < 0) {
				listWithoutStaff.add(userType);
				String withAgeRange = userType.getName() + 
						" <Age " + userType.getMinAge() + " - " + userType.getMaxAge() + ">";
				readerTypeNames.add(withAgeRange);
			}
				// readerTypeNames.add(userType.getName());
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, readerTypeNames);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		readerTypeSpinner.setAdapter(dataAdapter);
	  }
	
	
    public void onButtonClickHandler(View v) {
        if( v.getId() == R.id.btnAdd )
          handleAdd();
        else // if( v.getId() == R.id.btnDone )
            handleDone();
      }
    
    @Override
    public void onBackPressed() {
        handleDone();
    }
    
    private void handleAdd() {
	    EditText firstNameEdit = (EditText) findViewById(R.id.add_fname);
	    String firstName = firstNameEdit.getText().toString();
	    
	    EditText lastNameEdit = (EditText) findViewById(R.id.add_lname);
	    String lastName = lastNameEdit.getText().toString();
	    
	    if (MiscUtils.empty(firstName) || MiscUtils.empty(lastName)) {
			MiscUtils.showAlertDialog(AddUserActivity.this, "Error", "First Name and Last Name cannot be empty!");
			return;
	    }

	    Spinner readerTypeSpinner = (Spinner) findViewById(R.id.reader_type);
//	    String readerType = String.valueOf(readerTypeSpinner.getSelectedItem());
	    
	    UserType userType = listWithoutStaff.get(readerTypeSpinner.getSelectedItemPosition());
//	    UserType userType = config.getUserType(readerType);
	    String userTypeId = userType.getId();

//	    String id = UserType.getId(readerType);

	    EditText ageEdit = (EditText) findViewById(R.id.add_lage);
	    String age = ageEdit.getText().toString();
	    if (MiscUtils.empty(age)) {
	    	age = "-1";
	    }

	    if (MiscUtils.isNetworkAvailable(getApplicationContext())) {
		    new AddUserClient(AddUserActivity.this, AddUserActivity.this).execute(firstName, lastName, userTypeId, accountId, age);
		}
		else {
			MiscUtils.showAlertDialog(AddUserActivity.this, "Network Error", "User not added. You need to enable network to login.");
		}
	}

    private void handleDone() {
		 Intent returnIntent = new Intent();
		 returnIntent.putExtra("user", userList);
		 setResult(RESULT_OK,returnIntent);     
		 finish();			
	}

	@Override
	public void onResult(User user) {
		// TODO Auto-generated method stub
		userList.add(user);
		Intent returnIntent = new Intent();
		returnIntent.putExtra("user", userList);
		setResult(RESULT_OK,returnIntent);     
		finish();			
	}
}