package com.kos.cvut.fragments;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import org.dom4j.Element;

import kos.cvut.getdata.Course;
import kos.cvut.getdata.Record;
import kos.cvut.getdata.SqlConnector;
import kos.cvut.getdata.TimetableSlot;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.kos.R;
import com.kos.authentification.authenticator.AuthenticatorActivity;
import com.kos.cvut.Constants;
import com.kos.cvut.activity.CoursesListActivity;
import com.kos.cvut.activity.MainActivity;
import com.kos.cvut.adapters.CourseItemAdapter;
import com.kos.cvut.adapters.RecordItemAdapter;

public class CoursesListFragment extends SherlockFragment {

	private TextView search;
	private SqlConnector sql;
	private static ListView listView;
	private LinearLayout ll;

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		getSherlockActivity().getSupportFragmentManager().putFragment(outState,
				"mContent", this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getSherlockActivity().getSupportFragmentManager().putFragment(savedInstanceState,
		// "mContent", this);
		ll = new LinearLayout(inflater.getContext());
		Button bt = new Button(ll.getContext());
		bt.setText("Log in");
		bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(getSherlockActivity().getApplicationContext(), AuthenticatorActivity.class);
				startActivity(myIntent);
			}
		});
		ll.addView(bt);
		
		if (MainActivity.getSystemStatus()) {
			sql = SqlConnector.getInstance(getSherlockActivity()
					.getApplicationContext());
			sql = SqlConnector.getInstance(inflater.getContext());
			listView = new ListView(inflater.getContext());
			ArrayList<Course> records = new ArrayList<Course>();
			LinkedList<String> name_list = sql.getCourses();
			LinkedList<String> codes = sql.getCodes();
			LinkedList<String> completionList = new LinkedList<String>();
			LinkedList<String> creditsList = new LinkedList<String>();
			LinkedList<String> urlList = new LinkedList<String>();
			String url = Constants.getBase();
			if (sql.getClassification().matches("students")) {
				url = url + sql.getClassification() + "/" + sql.getUsername()
						+ "/enrolledCourses/";
				Element root = sql.GetFromUrl(url);
				urlList = sql.getElements(root, "course", "href");
				Iterator<String> url_it = urlList.iterator();
				while (url_it.hasNext()) {
					url = Constants.getBase();
					url += url_it.next() + "?detail=1&multilang=false";
					root = sql.GetFromUrl(url);
					completionList.add(sql.getElement(root, "completion"));
					creditsList.add(sql.getElement(root, "credits"));
				}
			}
			Iterator<String> it = name_list.iterator();
			Iterator<String> it1 = codes.iterator();
			Iterator<String> it2 = completionList.iterator();
			Iterator it3 = creditsList.iterator();
			Iterator<String> it4 = urlList.iterator();
			url = Constants.getBase();
			url += sql.getClassification() + "/" + sql.getUsername()
					+ "/parallels/?multilang=false";
			Element root = sql.GetFromUrl(url);
			LinkedList<String> parallel_name = sql.getElements(root, "course");
			LinkedList<String> day = sql.getElements(root, "day");
			LinkedList<String> duration = sql.getElements(root, "duration");
			LinkedList<String> firstHour = sql.getElements(root, "firstHour");
			LinkedList<String> parity = sql.getElements(root, "parity");
			LinkedList<String> room = sql.getElements(root, "room");
			Iterator<String> parallel_names = parallel_name.iterator();
			while (it.hasNext() && it1.hasNext() && it2.hasNext()
					&& it3.hasNext()) {
				Course r = null;
				String name = it.next();
				String code = it1.next();
				String completion = it2.next();
				int credits = Integer.valueOf((String) it3.next());
				String course_url = it4.next();
				int i = 0;
				while (parallel_names.hasNext()) {
					i++;
					String par_name = parallel_names.next();
					if (par_name.equalsIgnoreCase(name)) {
						TimetableSlot slot = new TimetableSlot(
								Integer.valueOf(day.get(i)),
								Integer.valueOf(duration.get(i)),
								Integer.valueOf(firstHour.get(i)),
								parity.get(i), room.get(i));
						r = new Course(name, code, completion, credits,
								course_url, slot);
						break;
					}
				}
				if (r == null) {
					r = new Course(name, code, completion, credits, course_url);
				}
				records.add(r);

			}
			CourseItemAdapter aR = new CourseItemAdapter(inflater.getContext(),
					R.layout.courses_list_adapter, records);
			listView.setAdapter(aR);
			listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					/*
					 * SherlockFragment fragment = null; fragment = new
					 * CoursesFragment(arg2);
					 * getSherlockActivity().getSupportFragmentManager()
					 * .beginTransaction().replace(R.id.content_frame, fragment)
					 * .commit();
					 */
					Intent i = new Intent(getSherlockActivity()
							.getApplicationContext(), CoursesListActivity.class);
					i.putExtra("pos", arg2);
					startActivity(i);
				}

			});
			return listView;
		}
		return ll;

	}

	View.OnClickListener myhandler1 = new View.OnClickListener() {
		public void onClick(View v) {

			SherlockFragment fragment = null;
			fragment = new SearchCourseResultsFragment(search.getText()
					.toString());
			getSherlockActivity().getSupportFragmentManager()
					.beginTransaction().replace(R.id.content_frame, fragment)
					.commit();

		}
	};

	public void onBackPressed() {

	};

}
