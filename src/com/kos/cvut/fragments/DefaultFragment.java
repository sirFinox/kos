package com.kos.cvut.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.actionbarsherlock.app.SherlockFragment;

public class DefaultFragment extends SherlockFragment{

	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSherlockActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	
}

