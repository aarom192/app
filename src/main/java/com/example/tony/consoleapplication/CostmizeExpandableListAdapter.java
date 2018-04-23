package com.example.tony.consoleapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Tony on 2018/1/15.
 */

public class CostmizeExpandableListAdapter extends BaseExpandableListAdapter {
    private List<String> groups;
    private List<List<ListItem>> children;
    private Context context = null;
    private int[] rowId;
    private Object object;

    /**
     * Constructor
     */
    public CostmizeExpandableListAdapter(Context ctx, int[] rowId, List<String> groups, List<List<ListItem>> children) {
        this.context = ctx;
        this.groups = groups;
        this.children = children;
        this.rowId = rowId;
    }

    /**
     *
     * @return
     */
    public View getGenericView() {
        // xmlをinflateしてViewを作成する
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, null);
        return view;
    }


    public TextView getGroupGenericView() {
        AbsListView.LayoutParams param = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 150);

        TextView textView = new TextView(context);
        textView.setLayoutParams(param);

        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        textView.setPadding(64, 0, 0, 0);

        return textView;
    }

    public int getRowId(int groupPosition) {
        return rowId[groupPosition];
    }


    @Override
    public Object getChild(int arg0, int arg1) {
        object = children.get(arg0).get(arg1);
        return children.get(arg0).get(arg1);
    }


    @Override
    public long getChildId(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return arg1;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {
        // 子供のViewオブジェクトを作成
        View childView = getGenericView();
        TextView textView = (TextView)childView.findViewById(R.id.name);
        ListItem item  = children.get(groupPosition).get(childPosition);
        textView.setText(item.getmName());

        TextView calorieView = (TextView)childView.findViewById(R.id.calorie);
        calorieView.setText(item.getmCalories());

        TextView storeView = (TextView)childView.findViewById(R.id.store);
        storeView.setText(item.getmStore());

        return childView;
    }

    @Override
    public int getChildrenCount(int arg0) {
        return children.get(arg0).size();
    }

    @Override
    public Object getGroup(int arg0) {
        return groups.get(arg0);
    }

    @Override
    public int getGroupCount() {
        return children.size();
    }

    @Override
    public long getGroupId(int arg0) {
        return arg0;
    }

    @Override
    public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
        TextView textView = getGroupGenericView();
        textView.setText(getGroup(arg0).toString());
        textView.setTextColor(Color.parseColor("#000000"));
        textView.setTextSize(18);
        TextPaint tp = textView.getPaint();
        tp.setFakeBoldText(true);
        return textView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }
}
