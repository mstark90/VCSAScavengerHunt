package edu.unl.hive.scavengerhunt;

import android.*;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mstark on 8/4/13.
 */
public class SpinnerArrayAdapter extends ArrayAdapter<String> {

    private List<String> items;
    private View emptyView;

    public SpinnerArrayAdapter(Context context, List<String> options) {
        super(context, android.R.layout.simple_list_item_1);
        items = options;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public String getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view2 = convertView;
        if(view2 == null)
        {
            view2 = LayoutInflater.from(getContext()).inflate(R.layout.survey_question_item, null);
        }
        TextView textView = (TextView)view2.findViewById(R.id.surveyResponse);
        textView.setText(getItem(position));
        return view2;
    }
}
