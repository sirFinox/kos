package com.kos.cvut.activity;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;

import kos.cvut.getdata.SqlConnector;
import net.qmsource.android.cvut.kos.auth.KosHttpClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;
import com.kos.R;
import com.kos.authentification.authenticator.AuthenticatorActivity;
import com.kos.cvut.Constants;
import com.kos.cvut.fragments.MainMenuFragment;
import com.kos.cvut.fragments.MenuGridFragment;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * This activity is an example of a responsive Android UI. On phones, the
 * SlidingMenu will be enabled only in portrait mode. In landscape mode, it will
 * present itself as a dual pane layout. On tablets, it will will do the same
 * general thing. In portrait mode, it will enable the SlidingMenu, and in
 * landscape mode, it will be a dual pane layout.
 * 
 * @author jeremy
 * 
 */
public class MainActivity extends SlidingFragmentActivity {

	private SherlockFragment mContent;
	private static boolean status = false;
	private String username;
	private Constants constant = new Constants();
	static HttpClient client;
	private static String classification = null;
	private SqlConnector sql;
	public final static int REQUEST_CODE_B = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setTitle(R.string.responsive_ui);

		setContentView(R.layout.responsive_content_frame);
		// connect();
		sql = SqlConnector.getInstance(this);
		if(!MainActivity.getSystemStatus()){
			if(sql.getUsername().length() > 0){
				setOnline();
			}
		}
		/*
		 * if (sql.getClassification().length() <= 0) { getStudentOrTeacher(); }
		 */
		// check if the content frame contains the menu frame
		if (findViewById(R.id.menu_frame) == null) {
			setBehindContentView(R.layout.menu_frame);
			getSlidingMenu().setSlidingEnabled(true);
			getSlidingMenu()
					.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			// show home as up so we can toggle
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		} else {
			// add a dummy view
			View v = new View(this);
			setBehindContentView(v);
			getSlidingMenu().setSlidingEnabled(false);
			getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}

		// set the Above View Fragment
		if (savedInstanceState != null)
			mContent = (SherlockFragment) getSupportFragmentManager()
					.getFragment(savedInstanceState, "mContent");
		if (mContent == null)
			mContent = new MenuGridFragment(0);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mContent).commit();

		// set the Behind View Fragment
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new MainMenuFragment()).commit();

		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindScrollScale(0.25f);
		sm.setFadeDegree(0.25f);

		// show the explanation dialog
		/*
		 * if (savedInstanceState == null) new AlertDialog.Builder(this)
		 * .setTitle(R.string.what_is_this)
		 * .setMessage(R.string.responsive_explanation) .show();
		 */

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
			/*getSupportFragmentManager().putFragment(outState, "mContent",
					mContent);*/
	}

	public void switchContent(final SherlockFragment fragment) {
		mContent = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				getSlidingMenu().showContent();
			}
		}, 50);
	}

	public void onMenuPressed(int pos) {
		Intent intent = MenuActivity.newInstance(this, pos);
		startActivity(intent);
	}

	public static void setOnline() {
		status = true;
	}

	public static boolean getSystemStatus() {
		return status;
	}

	private void connect() {
		// TODO Auto-generated method stub
		Intent myIntent = new Intent(this, AuthenticatorActivity.class);
		startActivity(myIntent);
		String usr = ""; // = myIntent.getStringExtra("mUsername");
		File f = new File(getCacheDir(), "username.dat");
		if (f.isFile()) {
			FileInputStream fstream;
			try {
				fstream = new FileInputStream(f);
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(
						new InputStreamReader(in));
				// Read File Line By Line
				usr = br.readLine();
				in.close();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Log.i("GetKosData - connect", "usr:" + usr);
		if (usr != null && usr.length() > 0) {
			username = usr;
			setOnline();
		}
		client = new KosHttpClient(constant.getUsername(),
				constant.getPassword());

	}

	public String getClassification() {
		if (classification != null && !(classification.length() == 0)) {
			return classification;
		} else {
			getStudentOrTeacher();
			return classification;
		}

	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public void getStudentOrTeacher() {
		connect();
		HttpUriRequest request;

		String url = "https://kosapi.feld.cvut.cz/api/3b/";
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		url = url + "students/" + username;
		request = new HttpGet(url);
		// Log.i("GetKosData", url);
		request.setHeader("Content-Type", "application/atom+xml");
		InputStream data = null;
		Reader r = null;
		try {
			Log.i("testing student/teacher", "starting request...");
			HttpResponse response;
			response = client.execute(request);
			data = response.getEntity().getContent();
			java.util.Scanner s = new java.util.Scanner(data)
					.useDelimiter("\\A");
			String str;
			if (s.hasNext()) {
				str = s.next();
			} else
				str = "";
			SAXReader reader = new SAXReader();
			Document document = DocumentHelper.parseText(str);
			Element root = document.getRootElement();
			String clasification = parseXml(root);
			SqlConnector sql = SqlConnector.getInstance(MainActivity.this);
			if (clasification.contains("exception")) {
				setClassification("teachers");
				sql.setClassification(false, true);
			} else if (clasification.contains("entry")
					|| clasification.contains("title")
					|| clasification.contains("atom")) {
				setClassification("students");
				sql.setClassification(true, false);
			}
			data.close();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String parseXml(Element el) {
		Iterator i = el.elements().iterator();
		String ent = "";
		while (i.hasNext()) {
			Element elem = (Element) i.next();
			ent = ent + elem.getName();
			ent = ent + " " + elem.getData() + "\n";
			if (elem.elements().size() > 0) {
				ent = ent + parseXml(elem);
			}

		}
		return ent;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		// if(!getSupportFragmentManager().popBackStackImmediate()){
		// finish();
		// }
	}

}
