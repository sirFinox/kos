package com.kos.cvut.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.kos.R;
import com.kos.cvut.MainMenuAdapter;

public class MainMenuFragment extends ListFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		String[] menu = getResources().getStringArray(R.array.main_menu);
		ArrayList<String> menu_array = new ArrayList<String>();
		for(int i=0;i<menu.length;i++){
			menu_array.add(menu[i]);
		}
		MainMenuAdapter menuAdapter = new MainMenuAdapter(getActivity(), 
				R.layout.menu_list, menu_array);
		setListAdapter(menuAdapter);
	}
	
	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		SherlockFragment newContent = new MenuGridFragment(position);
		if (newContent != null)
			switchFragment(newContent);
	}
	
	// the meat of switching the above fragment
	private void switchFragment(SherlockFragment fragment) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof MainActivity) {
			MainActivity ra = (MainActivity) getActivity();
			ra.switchContent(fragment);
		}
	}
}
