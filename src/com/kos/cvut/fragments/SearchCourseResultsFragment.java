package com.kos.cvut.fragments;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import kos.cvut.getdata.SearchCourse;
import kos.cvut.getdata.SqlConnector;

import org.dom4j.Element;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.kos.R;
import com.kos.cvut.activity.SearchCourseDetailsActivity;
import com.kos.cvut.adapters.SearchCourseItemAdapter;

public class SearchCourseResultsFragment extends SherlockFragment {
	private String search;
	private String programme;
	private String language;
	private SqlConnector sql;
	private LinkedList<String> nameList = new LinkedList<String>();
	private LinkedList<String> codeList = new LinkedList<String>();
	private LinkedList<String> urlList = new LinkedList<String>();
	private ListView list_of_results;
	private LinearLayout ll;

	public SearchCourseResultsFragment(String search){
		this.search = search; 
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ll = (LinearLayout) inflater.inflate(R.layout.activity_search_course_results, null);
		// Show the Up button in the action bar.

		sql = SqlConnector.getInstance(inflater.getContext());
		String url = "https://kosapi.feld.cvut.cz/api/3b/";
		url = url.concat("courses/?detail=1&multilang=false&query=name=='*" + search + "*',code=='*" + search
				+ "*',keywords=='*" + search + "*'");
		Element xml_search_result = sql.GetFromUrl(url);
		nameList = sql.getAtomElements(xml_search_result, "name", new String[] {"entry","content","name"});
		codeList = sql.getElements(xml_search_result, "code");
		urlList = sql.getElements(xml_search_result, "link", "href");

		ArrayList<SearchCourse> records = new ArrayList<SearchCourse>();
		SearchCourse r;
		Iterator<String> i = nameList.iterator();
		Iterator<String> j = codeList.iterator();
		Iterator<String> k = urlList.iterator();
		while (i.hasNext() && j.hasNext() && k.hasNext()) {
			r = new SearchCourse(i.next(), j.next(), k.next());
			records.add(r);
		}

		SearchCourseItemAdapter results = new SearchCourseItemAdapter(inflater.getContext(),
				android.R.layout.simple_list_item_1, records);
		list_of_results = (ListView) ll.findViewById(R.id.listView1);
		list_of_results.setAdapter(results);
		list_of_results.setOnItemClickListener(selectCourse);    
		
		return ll;
	}

	private OnItemClickListener selectCourse = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			/*SherlockFragment fragment = null;
	    	fragment = new SearchCourseDetailsFragment( urlList.get(arg2).toString());
			getSherlockActivity().getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.content_frame, fragment)
			.commit();*/
			Intent i = new Intent(getSherlockActivity().getApplicationContext(), SearchCourseDetailsActivity.class);
			i.putExtra("url", urlList.get(arg2).toString());
	    	startActivity(i);
		}                
	};

}
