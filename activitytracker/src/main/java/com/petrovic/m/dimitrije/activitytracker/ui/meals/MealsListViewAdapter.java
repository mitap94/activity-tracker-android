package com.petrovic.m.dimitrije.activitytracker.ui.meals;

import android.annotation.SuppressLint;
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
import com.petrovic.m.dimitrije.activitytracker.databinding.MealListRowBinding;

public class MealsListViewAdapter extends BaseAdapter {

    private Context context;
    private String[] dateIds;
    private String[] kcalIds;

    public MealsListViewAdapter(Context context, String[] dateIds, String[] kcalIds) {
        this.context = context;
        this.dateIds = dateIds;
        this.kcalIds = kcalIds;
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

//        if (position == 0) {
//            ((TextView) convertView.findViewById(R.id.list_kcal))
//                    .setText("1600 kcal");
//        } else if (position == 1) {
//            ((TextView) convertView.findViewById(R.id.list_kcal))
//                    .setText("1900 kcal");
//        } else if (position == 2) {
//            ((TextView) convertView.findViewById(R.id.list_kcal))
//                    .setText("1850 kcal");
//        } else if (position == 3) {
//            ((TextView) convertView.findViewById(R.id.list_kcal))
//                    .setText("2000 kcal");
//        } else if (position == 4) {
//            ((TextView) convertView.findViewById(R.id.list_kcal))
//                    .setText("1800 kcal");
//        } else if (position == 5) {
//            ((TextView) convertView.findViewById(R.id.list_kcal))
//                    .setText("1700 kcal");
//        } else if (position == 6) {
//            ((TextView) convertView.findViewById(R.id.list_kcal))
//                    .setText("1800 kcal");
//        }

        ((TextView) convertView.findViewById(R.id.list_date))
                .setText(dateIds[position]);
        ((TextView) convertView.findViewById(R.id.list_kcal))
                .setText(kcalIds[position]);

        return convertView;
    }
}
