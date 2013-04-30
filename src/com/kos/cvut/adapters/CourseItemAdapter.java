package com.kos.cvut.adapters;

import java.util.ArrayList;

import kos.cvut.getdata.Course;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kos.R;

public class CourseItemAdapter extends ArrayAdapter<Course> {

	private ArrayList<Course> records;

	public CourseItemAdapter(Context context, int textViewResourceId,
			ArrayList<Course> record) {
		super(context, textViewResourceId, record);
		this.records = record;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.courses_list_adapter, null);
		}

		Course record = records.get(position);
		if (record != null) {
			TextView completion = (TextView) v.findViewById(R.id.textView1);
			TextView credits = (TextView) v.findViewById(R.id.textView2);
			TextView name = (TextView) v.findViewById(R.id.textView3);
			TextView code = (TextView) v.findViewById(R.id.textView4);
			TextView time = (TextView) v.findViewById(R.id.textView5);

			if (completion != null) {
				completion.setText(completion(record.getCompletion(),v.getContext()));
			}
			if (credits != null) {
				credits.setText(v.getContext().getResources().getString(R.string.credits) + String.valueOf(record.getCredits()));
			}
			if (name != null) {
				name.setText(record.getNameCz());
			}
			if (code != null) {
				code.setText(record.getCode());
			}
			if (time != null) {
				if(record.getTimetableSlot().toString().length() > 0){
					time.setText(record.getTimetableSlot().toString(v.getContext()));
				}
			}
		}
		return v;
	}
	
	public String completion(String compl,Context ctx){
		if(compl.equalsIgnoreCase("CREDIT")){
			return ctx.getResources().getString(R.string.credit);
		}else if(compl.equalsIgnoreCase("CLFD_CREDIT")){
			return ctx.getResources().getString(R.string.clfd_credit);
		}else if(compl.equalsIgnoreCase("CREDIT_EXAM")){
			return ctx.getResources().getString(R.string.credit_exam);
		}else if(compl.equalsIgnoreCase("DEFENCE")){
			return ctx.getResources().getString(R.string.defence);
		}else if(compl.equalsIgnoreCase("EXAM")){
			return ctx.getResources().getString(R.string.exam);
		}else if(compl.equalsIgnoreCase("NOTHING")){
			return ctx.getResources().getString(R.string.nothing);
		}else if(compl.equalsIgnoreCase("UNDEFINED")){
			return ctx.getResources().getString(R.string.undefined);
		} 
		return "";
	}
	
	
}