package kos.cvut.getdata;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kos.R;

public class SearchCourseItemAdapter extends ArrayAdapter<SearchCourse>{

	 
	    private ArrayList<SearchCourse> records;

	    public SearchCourseItemAdapter(Context context, int textViewResourceId, ArrayList<SearchCourse> users) {
	        super(context, textViewResourceId, users);
	        this.records = users;
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View v = convertView;
	  if (v == null) {
	            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            v = vi.inflate(R.layout.listlayout, null);
	  }
	      
	  SearchCourse record = records.get(position);
	  if (record != null) {
	            TextView tag = (TextView) v.findViewById(R.id.TAG);
	            TextView desc = (TextView) v.findViewById(R.id.DESC);

	      if (tag != null) {
	    	  tag.setText(record.getTAG());
	      }

	      if(desc != null) {
	    	  desc.setText(record.getDESC() );
	      }
	  }
	  return v;
	    }
	    
	    
	}