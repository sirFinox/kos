package com.kos.cvut.fragments;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import kos.cvut.getdata.Record;
import kos.cvut.getdata.SqlConnector;

import org.dom4j.Element;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.kos.R;
import com.kos.cvut.activity.SearchPeopleDetailsActivity;
import com.kos.cvut.adapters.RecordItemAdapter;

public class SearchPeopleResultsFragment extends SherlockFragment {

	private String username = "";
	private String surname = "";
	private String firstname = "";
	private SqlConnector sql;
	private LinkedList<String> titleList = new LinkedList<String>();
	private LinkedList<String> usernameList = new LinkedList<String>();
	private LinkedList<String> urlList = new LinkedList<String>();
	private ListView list_of_results;
	private LinearLayout ll;

	public SearchPeopleResultsFragment(String username,String surname, String firstname){
		this.username = username;
		this.surname = surname;
		this.firstname = firstname;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ll = (LinearLayout) inflater.inflate(R.layout.activity_search_people_results, null);
		// Show the Up button in the action bar.
		list_of_results = (ListView) ll.findViewById(R.id.listView1);
		sql = SqlConnector.getInstance(inflater.getContext());
		String url = "https://kosapi.feld.cvut.cz/api/3b/";
		if (!username.isEmpty() || !surname.isEmpty() || !firstname.isEmpty()) {
			url = url.concat("people/?detail=1&multilang=false&query=");
			if(!username.isEmpty()){
				url = url.concat("username='"+username+"'");
				if(!surname.isEmpty()){
					url = url.concat(";lastName='"+surname+"'");
				}
				if(!firstname.isEmpty()){
					url = url.concat(";firstName='"+firstname+"'");
				}
			}else if(!surname.isEmpty()){
				url = url.concat("lastName='"+surname+"'");
				if(!firstname.isEmpty()){
					url = url.concat(";firstName='"+firstname+"'");
				}
			}else if(!firstname.isEmpty()){
				url = url.concat(";firstName='"+firstname+"'");
			}
			Element xml_search_result = sql.GetFromUrl(url);
			titleList = sql.getElements(xml_search_result, "title");
			usernameList = sql.getElements(xml_search_result, "username");
			urlList = sql.getElements(xml_search_result, "link", "href");
			ArrayList<Record> records = new ArrayList<Record>();
			Record r;
			Iterator<String> i = titleList.iterator();
			Iterator<String> j = usernameList.iterator();
			Iterator<String> k = urlList.iterator();
			while (i.hasNext() && j.hasNext() && k.hasNext()) {
				r = new Record(i.next(), j.next());
				records.add(r);
			}

			RecordItemAdapter aR = new RecordItemAdapter(inflater.getContext(),
					android.R.layout.simple_list_item_1, records);
			list_of_results.setAdapter(aR);
			list_of_results.setOnItemClickListener(selectPerson);    
		}
		return ll;
	}

	private OnItemClickListener selectPerson = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			/*SherlockFragment fragment = null;
			String tit = titleList.get(arg2).toString();
			String ur = urlList.get(arg2).toString();
			String us = usernameList.get(arg2).toString();
	    	fragment = new SearchPeopleDetailsFragment(urlList.get(arg2).toString());
			getSherlockActivity().getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.content_frame, fragment)
			.commit();*/
			Intent i = new Intent(getSherlockActivity().getApplicationContext(), SearchPeopleDetailsActivity.class);
			i.putExtra("url", urlList.get(arg2).toString());
			startActivity(i);
		}                
	};

}
