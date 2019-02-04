package com.nepali.nepali_app.nepali_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter2 extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    private List<NepaliWords> nepaliWordsList = null;
    private ArrayList<NepaliWords> arraylist;


    public ListViewAdapter2(Context context, List<NepaliWords> nepaliWords) {
        mContext = context;
        this.nepaliWordsList = nepaliWords;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<NepaliWords>();
        this.arraylist.addAll(nepaliWords);
    }

    public class ViewHolder {
        TextView words_id;
        TextView sound;
        TextView meaning;
        TextView origin;
        TextView role;
    }

    @Override
    public int getCount() {
        return nepaliWordsList.size();
    }

    @Override
    public NepaliWords getItem(int position) {
        return nepaliWordsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ListViewAdapter2.ViewHolder holder;
        if (view == null) {
            holder = new ListViewAdapter2.ViewHolder();
            view = inflater.inflate(R.layout.list_view_items, null);
            // Locate the TextViews in listview_item.xml
            holder.words_id = (TextView) view.findViewById(R.id.words_id);
            holder.sound = (TextView) view.findViewById(R.id.sound);
            holder.origin = (TextView) view.findViewById(R.id.origin);
            holder.meaning = (TextView) view.findViewById(R.id.meaning);
            holder.role = (TextView) view.findViewById(R.id.role);
            view.setTag(holder);
        } else {
            holder = (ListViewAdapter2.ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.words_id.setText(Integer.toString(nepaliWordsList.get(position).getId()));
        holder.origin.setText(nepaliWordsList.get(position).getOrigin());
        holder.meaning.setText(nepaliWordsList.get(position).getMeaning());
        holder.sound.setText(nepaliWordsList.get(position).getSound());
        holder.role.setText(nepaliWordsList.get(position).getRole());
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        nepaliWordsList.clear();
        if (charText.length() == 0) {
                nepaliWordsList.addAll(arraylist);
            } else {
                for (NepaliWords wp : arraylist) {
                    if (wp.getSound().toLowerCase(Locale.getDefault()).contains(charText) || wp.getOrigin().toLowerCase(Locale.getDefault()).contains(charText) || wp.getMeaning().toLowerCase(Locale.getDefault()).contains(charText) ) {
                        nepaliWordsList.add(wp);
                    }
                }
        }
        notifyDataSetChanged();
    }
}
