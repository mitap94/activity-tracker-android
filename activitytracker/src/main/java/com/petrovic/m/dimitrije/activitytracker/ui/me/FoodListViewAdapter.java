package com.petrovic.m.dimitrije.activitytracker.ui.me;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.petrovic.m.dimitrije.activitytracker.R;
import com.petrovic.m.dimitrije.activitytracker.databinding.FoodRowBinding;
import com.petrovic.m.dimitrije.activitytracker.databinding.MealListRowBinding;

import de.hdodenhof.circleimageview.CircleImageView;

public class FoodListViewAdapter extends BaseAdapter {

    private Context context;
    private String[] textIds;
    private int[] iconIds;
    private String[] sizeIds;
    private String[] kcalIds;

    public FoodListViewAdapter(Context context, String[] textIds, int[] iconIds, String[] sizeIds,  String[] kcalIds) {
        this.context = context;
        this.textIds = textIds;
        this.iconIds = iconIds;
        this.sizeIds = sizeIds;
        this.kcalIds = kcalIds;
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
            FoodRowBinding binding = FoodRowBinding.inflate(LayoutInflater.from(context), container, false);
            convertView = binding.getRoot();
        }

        ((CircleImageView) convertView.findViewById(R.id.food_icon))
                .setImageResource(iconIds[position]);
        ((TextView) convertView.findViewById(R.id.name))
                .setText(textIds[position]);
        ((TextView) convertView.findViewById(R.id.size))
                .setText(sizeIds[position]);
        ((TextView) convertView.findViewById(R.id.kcal))
                .setText(kcalIds[position]);

        return convertView;
    }
}
