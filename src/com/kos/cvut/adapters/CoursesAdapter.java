package kos.cvut.getdata;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kos.R;

public class CoursesAdapter extends ArrayAdapter<Courses>{

    Context context; 
    int layoutResourceId;    
    Courses data[] = null;
    
    public CoursesAdapter(Context context, int layoutResourceId, Courses[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CoursesHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new CoursesHolder();
            holder.type = (TextView)row.findViewById(R.id.txtType);
            holder.text = (TextView)row.findViewById(R.id.txtText);
            
            row.setTag(holder);
        }
        else
        {
            holder = (CoursesHolder)row.getTag();
        }
        
        Courses courses = data[position];
        holder.text.setText(courses.text);
        holder.type.setText(courses.type);
        
        return row;
    }
    
    static class CoursesHolder
    {
        TextView type;
        TextView text;
    }
}