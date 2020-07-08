package com.zucc.shortterm.personalassistant.Tools;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.zucc.shortterm.personalassistant.Bean.BeanRecord;
import com.zucc.shortterm.personalassistant.Bean.BeanRecordGroup;
import com.zucc.shortterm.personalassistant.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class RecordGroupAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    ArrayList<BeanRecordGroup> itemList;
    private Context context;

    public RecordGroupAdapter(Context context, ArrayList<BeanRecordGroup> itemList){
        this.inflater = LayoutInflater.from(context);
        this.itemList = itemList;
        this.context = context;
    }


    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder{
        public SwipeMenuListView listView;
        public TextView date;
        public TextView inSum;
        public TextView outSum;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.record_group, null);
            holder.listView = (SwipeMenuListView) convertView.findViewById(R.id.recordList_2);
            holder.date = convertView.findViewById(R.id.date);
            holder.inSum = convertView.findViewById(R.id.in_sum);
            holder.outSum = convertView.findViewById(R.id.out_sum);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        initRecordList(holder.listView,position);
        setListViewHeightBasedOnChildren(holder.listView);
        holder.date.setText(formatDate(itemList.get(position).getDate()));
        this.notifyDataSetChanged();
        return convertView;
    }


    /**
     * @param listView 此方法是本次listview嵌套listview的核心方法：计算parentlistview item的高度。
     *                 如果不使用此方法，无论innerlistview有多少个item，则只会显示一个item。
     **/
    public void setListViewHeightBasedOnChildren(SwipeMenuListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private void initRecordList(SwipeMenuListView recordList_2,int position){
        //record
        ArrayList<BeanRecord> list = itemList.get(position).getList() ;
        final RecordItemAdapter recordItemAdapter = new RecordItemAdapter(context,list);
        recordList_2.setAdapter(recordItemAdapter);
        recordList_2.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0,View arg1,int arg2,long arg3){
                Log.i("recordList","你点击了"+ arg2 +"行");
            }
        });
        SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem swipeMenuItem = new SwipeMenuItem(context);
                swipeMenuItem.setBackground(R.color.app_red);
                swipeMenuItem.setTitleColor(Color.WHITE);
                swipeMenuItem.setWidth(200);
                swipeMenuItem.setTitle(R.string.delete);
                swipeMenuItem.setTitleSize(18);
                menu.addMenuItem(swipeMenuItem);
            }
        };
        recordList_2.setMenuCreator(swipeMenuCreator);
        recordList_2.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                itemList.remove(position);
                recordItemAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    public String formatDate(Date date){
        SimpleDateFormat sdf =   new SimpleDateFormat( " yyyy-MM-dd" );
        return sdf.format(date);
    }
}
