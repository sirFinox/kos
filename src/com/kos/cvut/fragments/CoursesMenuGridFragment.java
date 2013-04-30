package com.kos.cvut.fragments;

import java.util.Iterator;

import kos.cvut.getdata.SqlConnector;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.kos.R;

public class CoursesMenuGridFragment extends SherlockFragment {

	private int mPos = -1;
	private int mImgRes;
	private String title;
	private SqlConnector sql;

	public CoursesMenuGridFragment() {
	}

	public CoursesMenuGridFragment(int pos) {
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

//		LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.example,
//			null);
		//TextView tv = (TextView) ll.findViewById(R.id.editText1);
		sql = SqlConnector.getInstance(getSherlockActivity()
				.getApplicationContext());
		int count = sql.getCoursesCount();
		Iterator<String> it = sql.getCourses().iterator();
		String[] menu = new String[count];
		for (int i = 0; i < count; i++) {
			menu[i] = (String) it.next();
		}
		//tv.setText(menu[mPos]);
		getActivity().setTitle(menu[mPos]);
		SherlockFragment fragment = null;

		fragment = new CoursesFragment(mPos);
		getSherlockActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		return fragment.getView();

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
