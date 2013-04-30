package com.kos.cvut.fragments;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import kos.cvut.getdata.Record;
import kos.cvut.getdata.SqlConnector;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.kos.R;
import com.kos.cvut.activity.SearchCourseResultsActivity;
import com.kos.cvut.adapters.RecordItemAdapter;

public class CoursesFragment extends SherlockFragment {

	private Button search_button;
	private TextView search;
	private Spinner programme;
	private Spinner language;
	private int position;
	private SqlConnector sql;
	private static ListView listView;
	
	public CoursesFragment(int mPos) {
		this.position = mPos;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		getSherlockActivity().getSupportFragmentManager().putFragment(outState, "mContent", this);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//getSherlockActivity().getSupportFragmentManager().putFragment(savedInstanceState, "mContent", this);
		sql = SqlConnector.getInstance(getSherlockActivity()
				.getApplicationContext());
		sql = SqlConnector.getInstance(inflater.getContext());
		listView = new ListView(inflater.getContext());
		ArrayList<Record> records = new ArrayList<Record>();
		LinkedList<String> name_list = sql.getCourses();
		LinkedList<String> codes = sql.getCodes();
		LinkedList<String[]> courseInfo = sql.getInfo(name_list.get(position),
				codes.get(position),inflater.getContext());
		getActivity().setTitle(name_list.get(position));
		Iterator<String[]> it = courseInfo.iterator();
		Record r;
		while (it.hasNext()) {
			String[] info = it.next();
			r = new Record(info[0], info[1]);
			records.add(r);
		}
		RecordItemAdapter aR = new RecordItemAdapter(inflater.getContext(),
				android.R.layout.simple_list_item_1, records);
		listView.setAdapter(aR);

		return listView;
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */

	View.OnClickListener myhandler1 = new View.OnClickListener() {
	    public void onClick(View v) {
	    	/*SherlockFragment fragment = null;
	    	fragment = new SearchCourseResultsFragment(search.getText().toString());
			getSherlockActivity().getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.content_frame, fragment)
			.commit();*/
	    	Intent i = new Intent(getSherlockActivity().getApplicationContext(), SearchCourseResultsActivity.class);
	    	i.putExtra("search", search.getText().toString());
	    	startActivity(i);
	    }
	  };
	  
//	  @Override
//	  public void onBackPressed() {
//		  SherlockFragment fragment = new CoursesListFragment();
//			getSherlockActivity().getSupportFragmentManager()
//					.beginTransaction().replace(R.id.content_frame, fragment)
//					.commit();
//	  }
}
