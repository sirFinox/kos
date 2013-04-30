package com.kos.cvut.fragments;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import kos.cvut.getdata.Record;
import kos.cvut.getdata.RecordItemAdapter;
import kos.cvut.getdata.SqlConnector;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.kos.R;

public class SearchCourseDetailsFragment extends SherlockFragment {
	private String url;
	private SqlConnector sql;
	ListView course;
	private LinearLayout ll;

	public SearchCourseDetailsFragment(String url){
		this.url = url;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ll = (LinearLayout) inflater.inflate(
				R.layout.activity_search_course_details, null);
		sql = SqlConnector.getInstance(inflater.getContext());
		LinkedList<String[]> details = sql.getInfoFromUrl(url);
		course = (ListView) ll.findViewById(R.id.listView1);
		Iterator<String[]> it = details.iterator();
		Record r;
		ArrayList<Record> records = new ArrayList<Record>();
		while (it.hasNext()) {
			String[] info = it.next();
			r = new Record(info[0], info[1]);
			records.add(r);
		}
		RecordItemAdapter aR = new RecordItemAdapter(inflater.getContext(),
				android.R.layout.simple_list_item_1, records);
		course.setAdapter(aR);
		return ll;
	}
}
