package com.ihunter.taskee.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ihunter.taskee.R;

/**
 * Created by Master Bison on 1/1/2017.
 */

public class PriorityAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    public PriorityAdapter(Context context) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return getCustomView(i, view, viewGroup);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        View row = inflater.inflate(R.layout.item_spinner_priority, parent, false);
        View v = row.findViewById(R.id.view);
        if(position == 0){
            v.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.low_priority));
        }else if(position == 1){
            v.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.medium_priority));
        }else if(position == 2){
            v.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.high_priority));
        }
        return row;
    }

}
