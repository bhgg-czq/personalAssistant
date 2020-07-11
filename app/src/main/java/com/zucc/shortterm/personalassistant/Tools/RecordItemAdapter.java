package com.zucc.shortterm.personalassistant.Tools;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.zucc.shortterm.personalassistant.Bean.BeanRecord;
import com.zucc.shortterm.personalassistant.Bean.BeanRecordGroup;
import com.zucc.shortterm.personalassistant.Bean.BeanRecordType;
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
        public TextView memo;
        public TextView price;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.record_item, null);
            holder.img = (ImageView) convertView.findViewById(R.id.ItemImage);
            holder.title = (TextView) convertView.findViewById(R.id.ItemTitle);
            holder.memo = (TextView) convertView.findViewById(R.id.ItemMemo);
            holder.price = (TextView) convertView.findViewById(R.id.price);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        int typeid = itemList.get(position).getType();
        if(itemList.get(position).getIn() == 0){
            for(int i = 0;i<BeanRecordType.outlist.size();i++){
                if(BeanRecordType.outlist.get(i).getId() == typeid){
                    holder.img.setImageBitmap(BeanRecordType.outlist.get(i).getIcon());
                    holder.title.setText(BeanRecordType.outlist.get(i).getName());
                    holder.title.setText(BeanRecordType.outlist.get(i).getName());
                    break;
                }
            }
            holder.img.setColorFilter(Color.parseColor("#e75e58"),PorterDuff.Mode.MULTIPLY);
            holder.price.setText("-"+String.valueOf(itemList.get(position).getSum()));
        }else {
            for (int i = 0; i < BeanRecordType.inlist.size(); i++) {
                if (BeanRecordType.inlist.get(i).getId() == typeid) {
                    holder.img.setImageBitmap(BeanRecordType.inlist.get(i).getIcon());
                    holder.title.setText(BeanRecordType.inlist.get(i).getName());
                    holder.title.setText(BeanRecordType.inlist.get(i).getName());
                    break;
                }
            }
            holder.img.setColorFilter(Color.parseColor("#9AACEC"), PorterDuff.Mode.MULTIPLY);
            holder.price.setText(String.valueOf(itemList.get(position).getSum()));
        }
        holder.memo.setText(itemList.get(position).getMemo());
         return convertView;

    }
}
