package com.petrovic.m.dimitrije.activitytracker.ui.me;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.petrovic.m.dimitrije.activitytracker.R;
import com.petrovic.m.dimitrije.activitytracker.databinding.ListViewRowBinding;

public class ListViewAdapter extends BaseAdapter {

    private Context context;
    @StringRes
    private int[] textIds;
    @DrawableRes
    private int[] iconIds;

    public ListViewAdapter(Context context, int[] textIds, int[] iconIds) {
        this.context = context;
        this.textIds = textIds;
        this.iconIds = iconIds;
    }

    @Override
    public int getCount() {
        return textIds.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {

        if (convertView == null) {
            ListViewRowBinding binding = ListViewRowBinding.inflate(LayoutInflater.from(context), container, false);
            convertView = binding.getRoot();
        }

        ((TextView) convertView.findViewById(R.id.list_text))
                .setText(textIds[position]);

        ((ImageView) convertView.findViewById(R.id.list_icon))
                .setImageResource(iconIds[position]);

        return convertView;
    }
}
