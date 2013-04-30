package com.kos.cvut.adapters;

import java.util.ArrayList;

import kos.cvut.getdata.News;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kos.R;

public class NewsItemAdapter extends ArrayAdapter<News>{

	 
	    private ArrayList<News> records;

	    public NewsItemAdapter(Context context, int textViewResourceId, ArrayList<News> items) {
	        super(context, textViewResourceId, items);
	  this.records = items;
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View v = convertView;
	  if (v == null) {
	            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            v = vi.inflate(R.layout.rss_news, null);
	  }
	      
	  News record = records.get(position);
	  if (record != null) {
	            TextView title = (TextView) v.findViewById(R.id.textView1);
	            TextView desc = (TextView) v.findViewById(R.id.textView3);
	            TextView pubDate = (TextView) v.findViewById(R.id.textView2);
	      if (title != null) {
	    	  title.setText(record.getTitle());
	      }

	      if(desc != null) {
	    	  desc.setText(record.getDescription() );
	      }
	      
	      if(pubDate != null) {
	    	  pubDate.setText(record.getPubDate() );
	      }
	  }
	  return v;
	    }
	}