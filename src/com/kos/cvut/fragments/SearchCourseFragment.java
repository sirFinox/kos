package com.kos.cvut.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.kos.R;

public class SearchCourseFragment extends SherlockFragment {

	private Button search_button;
	private SearchView search;
	private Spinner programme;
	private Spinner language;
	private LinearLayout ll;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ll = (LinearLayout) inflater.inflate(R.layout.activity_search_course,
				null);
		search = (SearchView) ll.findViewById(R.id.textView1);
		search.setSubmitButtonEnabled(true);
		search.setOnQueryTextListener(new OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String arg0) {
                // TODO Auto-generated method stub
            	startActivity();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String arg0) {
                return false;
            }
        });
		search.setOnKeyListener(new OnKeyListener() {
			@Override
		    public boolean onKey(View v, int keyCode, KeyEvent event) {
		        // TODO Auto-generated method stub

		        Log.d("SEARCH", "Search onkey");
		        return false;
		    }
		});
		// programme = (Spinner) ll.findViewById(R.id.Spinner01);
		// language = (Spinner) ll.findViewById(R.id.Spinner02);

		return ll;
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */

	View.OnClickListener myhandler1 = new View.OnClickListener() {
		public void onClick(View v) {
			/*
			 * SherlockFragment fragment = null; fragment = new
			 * SearchCourseResultsFragment(search.getText().toString());
			 * getSherlockActivity().getSupportFragmentManager()
			 * .beginTransaction() .replace(R.id.content_frame, fragment)
			 * .commit();
			 */
			startActivity();
			
		}
	};

	private void startActivity() {
		if (search.getQuery().toString().length() >= 3){
			Intent i = new Intent(getSherlockActivity().getApplicationContext(),
					SearchCourseResultsActivity.class);
			i.putExtra("search", search.getQuery().toString());
			startActivity(i);
		}else{
			Context context = getSherlockActivity().getApplicationContext();
			CharSequence text = "Search text length must be longer than 2 characters.";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		
	}

}
