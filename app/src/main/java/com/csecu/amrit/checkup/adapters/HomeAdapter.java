package com.csecu.amrit.checkup.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.csecu.amrit.checkup.R;
import com.csecu.amrit.checkup.models.Icon;

import java.util.ArrayList;

/**
 * Created by Amrit on 2/20/2018.
 */

public class HomeAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Icon> list;

    public HomeAdapter(Context context, ArrayList<Icon> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.home_item, null);
        }

        TextView textView = v.findViewById(R.id.home_tvName);
        textView.setText(list.get(i).getName());
        textView.setCompoundDrawablesWithIntrinsicBounds(0, list.get(i).getImage(), 0, 0);
        return v;
    }
}
