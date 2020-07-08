package com.zucc.shortterm.personalassistant.Tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.zucc.shortterm.personalassistant.Bean.BeanRecord;
import com.zucc.shortterm.personalassistant.R;

import java.util.ArrayList;

public class RecordItemAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    ArrayList<BeanRecord> itemList;

    public RecordItemAdapter(Context context,ArrayList<BeanRecord> itemList){
        this.inflater = LayoutInflater.from(context);
        this.itemList = itemList;
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
        return itemList.get(position).getId();
    }

    static class ViewHolder{
        public ImageView img;
        public TextView title;
        public TextView text;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.record_item, null);
            holder.img = (ImageView) convertView.findViewById(R.id.ItemImage);
            holder.title = (TextView) convertView.findViewById(R.id.ItemTitle);
            holder.text = (TextView) convertView.findViewById(R.id.ItemText);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.img.setImageResource(R.drawable.icon_record_1);
        holder.img.setColorFilter(R.color.colorTheme_2);
        holder.title.setText(String.valueOf(itemList.get(position).getSum()));
        holder.text.setText(("test"));
         return convertView;

    }
}
