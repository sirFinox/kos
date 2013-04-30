package com.kos.cvut.fragments;

import java.util.ArrayList;
import java.util.Iterator;

import kos.cvut.getdata.SqlConnector;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.kos.R;
import com.kos.cvut.activity.CoursesActivity;
import com.kos.cvut.adapters.MainMenuAdapter;

public class CoursesMainMenuFragment extends SherlockListFragment {

	private SqlConnector sql;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		sql = SqlConnector.getInstance(getSherlockActivity()
				.getApplicationContext());
		int count = sql.getCoursesCount();
		Iterator<String> it = sql.getCourses().iterator();
		String[] menu = new String[count];
		for (int i = 0; i < count; i++) {
			menu[i] = (String) it.next();
		}
		ArrayList<String> menu_array = new ArrayList<String>();
		for (int i = 0; i < menu.length; i++) {
			menu_array.add(menu[i]);
		}
		ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(getSherlockActivity().getApplicationContext(),
				R.layout.menu_list, menu); //android.R.layout.simple_list_item_1
		// MainMenuAdapter menuAdapter = new MainMenuAdapter(getActivity(),
		// R.layout.menu_list, menu_array);
		setListAdapter(menuAdapter);
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContent = new CoursesMenuGridFragment(position);
		if (newContent != null)
			switchFragment(newContent);
	}

	// the meat of switching the above fragment
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof CoursesActivity) {
			CoursesActivity ra = (CoursesActivity) getActivity();
			ra.switchContent(fragment);
		}
	}
}
