package com.kos.cvut.fragments;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.client.HttpClient;

import net.qmsource.android.cvut.kos.auth.KosHttpClient;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.kos.R;
import com.kos.authentification.authenticator.AuthenticatorActivity;
import com.kos.cvut.Constants;

public class MenuGridFragment extends SherlockFragment {

	private int mPos = -1;
	private int mImgRes;
	private String title;

	public MenuGridFragment() {
	}

	public MenuGridFragment(int pos) {
		mPos = pos;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mPos == -1 && savedInstanceState != null)
			mPos = savedInstanceState.getInt("mPos");
		// TypedArray imgs = getResources().obtainTypedArray(R.array.birds_img);
		// mImgRes = imgs.getResourceId(mPos, -1);
		// GridView gv = (GridView) inflater.inflate(R.layout.example, null);
		// gv.setBackgroundResource(android.R.color.black);
		// gv.setBackgroundResource(0);
		// gv.setAdapter(new GridAdapter());

		LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.example,
				null);
		TextView tv = (TextView) ll.findViewById(R.id.editText1);

		String[] menu = getResources().getStringArray(R.array.main_menu);
		tv.setText(menu[mPos]);
		getActivity().setTitle(menu[mPos]);
		TypedArray imgs = getResources()
				.obtainTypedArray(R.array.main_menu_img);
		int resId = imgs.getResourceId(mPos, -1);
		getSherlockActivity().getSupportActionBar().setIcon(resId);
		SherlockFragment fragment = null;
		if (menu[mPos].equals("Home")) {
			fragment = new NewsFragment();
			getSherlockActivity().getSupportFragmentManager()
					.beginTransaction().replace(R.id.content_frame, fragment)
					.commit();
			return fragment.getView();
		} else if (menu[mPos].equals("Schedule")) {
			fragment = new SyncCalFragment();
			getSherlockActivity().getSupportFragmentManager()
					.beginTransaction().replace(R.id.content_frame, fragment)
					.commit();
			return fragment.getView();
		} else if (menu[mPos].equals("Find Courses")) {
			fragment = new SearchCourseFragment();
			getSherlockActivity().getSupportFragmentManager()
					.beginTransaction().replace(R.id.content_frame, fragment)
					.commit();
			return fragment.getView();
		} else if (menu[mPos].equals("Find People")) {
			fragment = new SearchPeopleFragment();
			getSherlockActivity().getSupportFragmentManager()
					.beginTransaction().replace(R.id.content_frame, fragment)
					.commit();
			return fragment.getView();
		} else if (menu[mPos].equals("Courses")) {
			/*
			 * Intent i = new
			 * Intent(getSherlockActivity().getApplicationContext(),
			 * CoursesActivity.class); startActivity(i); fragment = new
			 * NewsFragment(); getSherlockActivity().getSupportFragmentManager()
			 * .beginTransaction() .replace(R.id.content_frame, fragment)
			 * .commit();
			 */
			fragment = new CoursesListFragment();
			getSherlockActivity().getSupportFragmentManager()
					.beginTransaction().replace(R.id.content_frame, fragment)
					.commit();
			return fragment.getView();
		}

		return ll;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("mPos", mPos);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private class GridAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 30;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.grid_item, null);
			}
			// ImageView img = (ImageView)
			// convertView.findViewById(R.id.grid_item_img);
			// img.setImageResource(mImgRes);
			return convertView;
		}

	}

	
}
