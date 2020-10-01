package com.petrovic.m.dimitrije.activitytracker.ui.me;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.petrovic.m.dimitrije.activitytracker.R;
import com.petrovic.m.dimitrije.activitytracker.databinding.MealListRowBinding;

public class InfoListViewAdapter extends BaseAdapter {

    private Context context;
    private String[] dateIds;
    private String[] weightIds;

    public InfoListViewAdapter(Context context, String[] dateIds, String[] weightIds) {
        this.context = context;
        this.dateIds = dateIds;
        this.weightIds = weightIds;
    }

    @Override
    public int getCount() {
        return dateIds.length;
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
            MealListRowBinding binding = MealListRowBinding.inflate(LayoutInflater.from(context), container, false);
            convertView = binding.getRoot();
        }

        ((TextView) convertView.findViewById(R.id.list_date))
                .setText(dateIds[position]);
        ((TextView) convertView.findViewById(R.id.list_kcal))
                .setText(weightIds[position]);

        return convertView;
    }
}
