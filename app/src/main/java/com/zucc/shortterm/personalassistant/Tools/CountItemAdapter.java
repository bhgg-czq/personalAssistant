package com.zucc.shortterm.personalassistant.Tools;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.github.mikephil.charting.data.PieEntry;
import com.zucc.shortterm.personalassistant.Bean.BeanRecordType;
import com.zucc.shortterm.personalassistant.R;

import java.math.BigDecimal;
import java.util.List;
import java.util.zip.Inflater;

public class CountItemAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<PieEntry> pieList;
    private List<BeanRecordType> list;

    public List<PieEntry> getPieList() {
        return pieList;
    }

    public void setPieList(List<PieEntry> pieList) {
        this.pieList = pieList;
    }

    public List<BeanRecordType> getList() {
        return list;
    }

    public void setList(List<BeanRecordType> list) {
        this.list = list;
    }

    public CountItemAdapter(Context context, List<PieEntry> pieList, List<BeanRecordType> list){
        this.list = list;
        this.pieList = pieList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    public class ViewHolder{
        private TextView count1;
        private TextView count2;
        private TextView count3;
        private ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.count_item,null);
            holder.count1 = convertView.findViewById(R.id.count_1);
            holder.count2 = convertView.findViewById(R.id.count_2);
            holder.count3 = convertView.findViewById(R.id.count_3);
            holder.imageView = convertView.findViewById(R.id.count_image);
            convertView.setTag(holder);
        }else{
            holder =(ViewHolder) convertView.getTag();
        }
        holder.imageView.setImageBitmap(list.get(position).getIcon());
        holder.imageView.setColorFilter(list.get(position).getColor(), PorterDuff.Mode.MULTIPLY);
        holder.count1.setText(list.get(position).getName());
        float f = pieList.get(position).getValue()*100;
        BigDecimal   b  =   new BigDecimal(f);
        f = b.setScale(2,BigDecimal.ROUND_UP).floatValue();
        holder.count2.setText(String.valueOf(f)+"%");
        holder.count3.setText(String.valueOf(list.get(position).getSum()));

        return convertView;
    }
}
