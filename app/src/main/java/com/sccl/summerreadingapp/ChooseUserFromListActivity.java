package com.sccl.summerreadingapp;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.sccl.summerreadingapp.adapter.ChooseUserAdapter;
import com.sccl.summerreadingapp.model.User;

public class ChooseUserFromListActivity extends Activity {

	private static final int REQUEST_CODE_ADD_USER = 3;
	private String accountId;

	ChooseUserAdapter c;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_user_list);

		final String users[] = (String[]) getIntent().getSerializableExtra(
				"user");
		final String usersTypes[] = (String[]) getIntent()
				.getSerializableExtra("userType");
		accountId = (String) getIntent().getStringExtra("accountId");

		ListView lv = (ListView) findViewById(R.id.user_list);

		// Adding items to listview
		c = new ChooseUserAdapter(this, users, usersTypes);
		lv.setAdapter(c);
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		// listening to single list item on click
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				c.setSelectedPosition(position);
				closeAndSendResult(position);
			}
		});

	}

	@Override
	public void onBackPressed() {
		closeAndSendResult(-1);
	}

	public void closeAndSendResult(int index) {
		Intent returnIntent = new Intent();
		returnIntent.putExtra("index", index);
		returnIntent.putExtra("restart", -1);
		setResult(RESULT_OK, returnIntent);
		finish();
	}

	public void onButtonClickHandler(View v) {
		if (v.getId() == R.id.btnAdd)
			startAddUserActivity(accountId);
		else {
			int index = c.getSelectedPosition();

			if (index == -1) {
				index = 0;
			}
			closeAndSendResult(index);
		}
	}

	private void startAddUserActivity(String accountId) {
		Intent i = new Intent(getApplicationContext(), AddUserActivity.class);
		i.putExtra("accountId", accountId);
		startActivityForResult(i, REQUEST_CODE_ADD_USER);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CODE_ADD_USER) {
				ArrayList<User> userList = (ArrayList<User>) data.getSerializableExtra("user");
				Intent returnIntent = new Intent();
				returnIntent.putExtra("index", 0);
				returnIntent.putExtra("restart", 1);
				returnIntent.putExtra("user", userList);
				setResult(RESULT_OK, returnIntent);
			}
		}
		finish();
	}// onActivityResult
}