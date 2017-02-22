package com.sccl.summerreadingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sccl.summerreadingapp.clients.LoginClient;
import com.sccl.summerreadingapp.helper.MiscUtils;
import com.sccl.summerreadingapp.model.Login;
 
public class LoginActivity extends Activity implements LoginAsyncListener {
    protected static final int REQUEST_CODE_REGISTRATION = 0;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setting default screen to login.xml
        setContentView(R.layout.login);
 
        Button loginButton = (Button) findViewById(R.id.btnLogin);
 		loginButton.setOnClickListener(new View.OnClickListener() {
 			@Override
			public void onClick(View v) {
				if (MiscUtils.isNetworkAvailable(getApplicationContext())) {
				    EditText userNameEdit = (EditText) findViewById(R.id.username);
				    String userName = userNameEdit.getText().toString();
				    
				    EditText passwordEdit = (EditText) findViewById(R.id.password);
				    String password = passwordEdit.getText().toString();

				    if (MiscUtils.empty(userName) || MiscUtils.empty(password)) {
						MiscUtils.showAlertDialog(LoginActivity.this, "Error", "User Name and Password cannot be empty!");
						return;
				    }
				    
				    new LoginClient(LoginActivity.this, LoginActivity.this).execute(userName, password);
				}
				else {
					MiscUtils.showAlertDialog(LoginActivity.this, "Network Error", "User not logged in. You need to enable network to login.");
				}
			}
 
		});
 
        TextView registerButton = (TextView) findViewById(R.id.link_to_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View v) {
                // Switching to Register screen
                Intent i = new Intent(getApplicationContext(), RegistrationActivity.class);
                // startActivity(i);
                startActivityForResult(i, REQUEST_CODE_REGISTRATION);
            }
        });
        
    }
    
    public void onResult(Login login) {
    	if (login != null) {
			 returnLoginResult(login);
    	}
    	else {
    		MiscUtils.showAlertDialog(this, "Login Error", "Unable to login. Check the credentials");
    	}
    }

	private void returnLoginResult(Login login) {
		Intent returnIntent = new Intent();
		 returnIntent.putExtra("user", login);
		 setResult(RESULT_OK,returnIntent);     
		 finish();
	}
    
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK){
			if (requestCode == REQUEST_CODE_REGISTRATION) {
				Login login = (Login)data.getSerializableExtra("login");
				if (login != null) {
					 returnLoginResult(login);
				}
				else {
					// Error???
				}
			}
		}
		// finish();
	}//onActivityResult

    
}