package me.jack.moodlighting;

import android.content.Context;
import android.graphics.Color;
import android.transition.Fade;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jackp on 02/04/2017.
 */

public class SideDrawListAdapter extends BaseAdapter {
    private Context mContext;

    public SideDrawListAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return FadePreset.presets.size() + 2;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = null;
        if (convertView == null) {
            textView = new TextView(mContext);
            if (position == 0) {
                textView.setText("Presets:");
                textView.setTextSize(28);
                textView.setTextColor(Color.GRAY);
                textView.setPadding(0, 10, 0, 10);
            } else if (position == getCount() - 1) {
                textView.setText("Settings");
                textView.setTextSize(28);
                textView.setTextColor(Color.GRAY);
                textView.setPadding(0, 10, 0, 10);
            } else {
                textView.setText("-" + FadePreset.presets.get(position - 1).toString());
                textView.setTextColor(Color.WHITE);
                textView.setPadding(50, 5, 0, 5);
            }
        } else {
            textView = (TextView) convertView;
        }
        return textView;
    }
}