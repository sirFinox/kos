package com.kos.cvut;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kos.R;

public class MainMenuAdapter extends ArrayAdapter {
	private ArrayList<String> menu_items;

	public MainMenuAdapter(Context context, int textViewResourceId,
			ArrayList<String> menu_items) {
		super(context, textViewResourceId, menu_items);
		this.menu_items = menu_items;
	}

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
    if (v == null) {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.listitem, null);
    }
           
    String menu = menu_items.get(position);
    if (menu != null) {
            ImageView icon = (ImageView) v.findViewById(R.id.icon);
            TextView menu_item = (TextView) v.findViewById(R.id.menu_item);

        if (icon != null) {
        	TypedArray imgs = getContext().getResources().obtainTypedArray(R.array.main_menu_img_dark);
    		int resId = imgs.getResourceId(position, -1); 
            icon.setImageResource(resId);
        }

        if(menu_item != null) {
            menu_item.setText(menu);
        }
    }
    return v;
    }
}
