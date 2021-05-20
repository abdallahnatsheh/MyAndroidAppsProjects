package com.abdallahn_ameerh.ex3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ListAdapter extends BaseAdapter {
    Context context;
    private final String[] titles;
    private final String[] descriptions;
    private final String[] dates;
    private final String[] times;


    public ListAdapter(Context context, String[] titles, String[] descriptions, String[] dates, String[] times){
        //super(context, R.layout.single_list_app_item, utilsArrayList);
        this.context = context;
        this.titles = titles;
        this.descriptions = descriptions;
        this.dates = dates;
        this.times = times;

    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.todo_items, parent, false);
            viewHolder.todoTitleName = (TextView) convertView.findViewById(R.id.todoTitleName);
            viewHolder.todoDateName = (TextView) convertView.findViewById(R.id.todoDateName);
            viewHolder.todoTimeName = (TextView) convertView.findViewById(R.id.todoTimeName);
            viewHolder.todoDescriptionName = (TextView) convertView.findViewById(R.id.todoDescriptionName);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        viewHolder.todoTitleName.setText(titles[position]);
        viewHolder.todoDescriptionName.setText(descriptions[position]);
        viewHolder.todoDateName.setText(dates[position]);
        viewHolder.todoTimeName.setText(times[position]);

        return convertView;
    }

    private static class ViewHolder {

        TextView todoTitleName;
        TextView todoDateName;
        TextView todoTimeName;
        TextView todoDescriptionName;

    }
}

