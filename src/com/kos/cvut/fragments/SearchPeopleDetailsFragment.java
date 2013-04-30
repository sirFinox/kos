package com.kos.cvut.fragments;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import kos.cvut.getdata.Record;
import kos.cvut.getdata.SqlConnector;

import org.dom4j.Element;

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
import com.kos.cvut.adapters.RecordItemAdapter;

public class SearchPeopleDetailsFragment extends SherlockFragment {
	private String url;
	private SqlConnector sql;
	ListView person;
	private LinearLayout ll;

	public SearchPeopleDetailsFragment(String url) {
		this.url = url;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ll = (LinearLayout) inflater.inflate(
				R.layout.activity_search_people_details, null);
		String full_url = "https://kosapi.feld.cvut.cz/api/3b/";
		full_url = full_url.concat(url);
		sql = SqlConnector.getInstance(inflater.getContext());
		Element details = sql.GetFromUrl(full_url);
		LinkedList<String> title = sql.getElements(details, "title");
		LinkedList<String> username = sql.getElements(details, "username");
		person = (ListView) ll.findViewById(R.id.listView1);
		LinkedList<String[]> roles = sql.getRoles(details);
		Record r;
		ArrayList<Record> records = new ArrayList<Record>();
		Iterator<String[]> i = roles.iterator();
		// while (i.hasNext()){
		// String[] info = i.next();
		// r = new Record(info[0], info[1]);
		// records.add(r);
		// }
		r = new Record("Title", title.get(0));
		records.add(r);
		r = new Record("Username", username.get(0));
		records.add(r);
		if (i.hasNext()) {
			String[] info = i.next();
			full_url = "https://kosapi.feld.cvut.cz/api/3b/";
			full_url = full_url.concat(info[1]);
			details = sql.GetFromUrl(full_url);

			LinkedList<String> email = sql.getElements(details, "email");
			if (email.get(0).length() > 0) {
				r = new Record("E-mail", email.get(0));
				records.add(r);
			}
		}
		RecordItemAdapter aR = new RecordItemAdapter(inflater.getContext(),
				android.R.layout.simple_list_item_1, records);
		person.setAdapter(aR);
		return ll;
	}

}
