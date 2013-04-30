package com.kos.cvut.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.kos.R;

public class MenuActivity extends SherlockActivity {

	private Handler mHandler;
	
	public static Intent newInstance(Activity activity, int pos) {
		Intent intent = new Intent(activity, MenuActivity.class);
		intent.putExtra("pos", pos);
		return intent;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int pos = 0;
		if (getIntent().getExtras() != null) {
			pos = getIntent().getExtras().getInt("pos");
		}
		
		String[] menu = getResources().getStringArray(R.array.main_menu);
		//TypedArray imgs = getResources().obtainTypedArray(R.array.birds_img);
		//int resId = imgs.getResourceId(pos, -1);
		
		setTitle(menu[pos]);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		ColorDrawable color = new ColorDrawable(Color.BLACK);
		color.setAlpha(128);
		getSupportActionBar().setBackgroundDrawable(color);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mHandler = new Handler();
		
		//Obsah
		/*TextView t = new TextView(this);
		t.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getSupportActionBar().show();
				hideActionBarDelayed(mHandler);
			}
		});
		t.setText(menu[pos]);*/
		/*ImageView imageView = new ImageView(this);
		imageView.setScaleType(ScaleType.CENTER_INSIDE);
		imageView.setImageResource(resId);
		imageView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getSupportActionBar().show();
				hideActionBarDelayed(mHandler);
			}
		});
		setContentView(imageView);*/
		//setContentView(t);
		this.getWindow().setBackgroundDrawableResource(android.R.color.black);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getSupportActionBar().show();
		hideActionBarDelayed(mHandler);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void hideActionBarDelayed(Handler handler) {
		handler.postDelayed(new Runnable() {
			public void run() {
				getSupportActionBar().hide();
			}
		}, 2000);
	}
	
}
