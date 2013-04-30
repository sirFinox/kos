package com.kos.cvut.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.kos.R;

public class SearchPeopleFragment extends SherlockFragment {

	private Button search_button;
	private TextView username;
	private TextView surname;
	private TextView firstname;
	private LinearLayout ll;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ll = (LinearLayout) inflater.inflate(R.layout.activity_search_people, null);
		// Show the Up button in the action bar.
		username = (TextView) ll.findViewById(R.id.editText1);
		surname = (TextView) ll.findViewById(R.id.editText2);
		firstname = (TextView) ll.findViewById(R.id.EditText01);
		search_button = (Button) ll.findViewById(R.id.button1);
		search_button.setOnClickListener(myhandler1);
		return ll;
	}

	View.OnClickListener myhandler1 = new View.OnClickListener() {
	    public void onClick(View v) {
	    	SherlockFragment fragment = null;
	    	fragment = new SearchPeopleResultsFragment(username.getText().toString(),surname.getText().toString(),firstname.getText().toString());
			getSherlockActivity().getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.content_frame, fragment)
			.commit();
	    }
	  };
	
}
