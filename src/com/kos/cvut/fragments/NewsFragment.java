package com.kos.cvut.fragments;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import org.dom4j.Element;

import kos.cvut.getdata.News;
import kos.cvut.getdata.Record;
import kos.cvut.getdata.SqlConnector;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.kos.R;
import com.kos.cvut.Constants;
import com.kos.cvut.adapters.NewsItemAdapter;
import com.kos.cvut.adapters.RecordItemAdapter;

public class NewsFragment extends DefaultFragment {

	ListView lv = null;
	SqlConnector sql;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lv = new ListView(inflater.getContext());
		if (isOnline()) {
			sql = SqlConnector.getInstance(inflater.getContext());
			Element rss = sql.GetFromUrl(Constants.getRssNews());
			LinkedList<String> titles = sql.getAtomElements(rss, "title",
					new String[] { "channel", "item", "title" });
			LinkedList<String> link = sql.getAtomElements(rss, "link",
					new String[] { "channel", "item", "link" });
			LinkedList<String> description = sql.getAtomElements(rss,
					"description", new String[] { "channel", "item",
							"description" });
			LinkedList<String> category = sql.getAtomElements(rss, "category",
					new String[] { "channel", "item", "category" });
			LinkedList<String> pubDate = sql.getAtomElements(rss, "pubDate",
					new String[] { "channel", "item", "pubDate" });
			titles.remove(0);
			description.remove(0);
			link.remove(0);
			String[] title = new String[titles.size()];
			Iterator<String> it = titles.iterator();
			Iterator<String> it1 = description.iterator();
			Iterator<String> it2 = pubDate.iterator();
			ArrayList<News> list = new ArrayList<News>();
			while (it.hasNext() && it1.hasNext() && it2.hasNext()) {
				News n = new News(it.next(),checkText(it1.next()),getDate(it2.next()));
				list.add(n);
			}
			NewsItemAdapter adapter = new NewsItemAdapter(
					getSherlockActivity().getApplicationContext(),
					R.layout.rss_news, list);
			lv.setAdapter(adapter);
		}
		return lv;
	}
	
	
	//datum ve formatu DD mes RRRR hh:mm:ss timezone
	public String getDate(String pubDate){
		String[] date = pubDate.split(" ");
		return date[0] + " " + date[1] + " " + date[2];
	}
	
	public String checkText(String text){
		text = text.replaceAll("&#160;", " ");
		text = text.replace("<p>", "");
		text = text.replace("</p>", "");
		text = text.replace("<em>", "");
		text = text.replace("</em>", "");
		text = text.replace("<strong>", "");
		text = text.replace("</strong>", "");
		while (text.contains("<")){
			String rem = text.substring(text.indexOf("<"),text.indexOf(">")+1);
			text = text.replace(rem, "");
		}
		return text;
	}

}
