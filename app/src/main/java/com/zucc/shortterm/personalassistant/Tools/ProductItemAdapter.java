package com.zucc.shortterm.personalassistant.Tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zucc.shortterm.personalassistant.Bean.Product;
import com.zucc.shortterm.personalassistant.R;

import java.util.List;

public class ProductItemAdapter extends BaseAdapter {

    private List<Product> list;
    private LayoutInflater inflater;

    public ProductItemAdapter(Context context,List<Product> list){
        this.list = list;
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
        return list.size();
    }

    public class ViewHolder{
        private TextView name;
        private TextView rate;
        private TextView term;
        private TextView des;
        private TextView point;
        private TextView risk;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ProductItemAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.product_item,null);
            holder.name = convertView.findViewById(R.id.company_name);
            holder.rate = convertView.findViewById(R.id.rate);
            holder.term = convertView.findViewById(R.id.term);
            holder.des = convertView.findViewById(R.id.des);
            holder.point = convertView.findViewById(R.id.start_point);
            holder.risk = convertView.findViewById(R.id.risk);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Product p = list.get(position);
        holder.name.setText(p.getCompanyName());
        holder.rate.setText(p.getRate());
        holder.term.setText(p.getTerm()+"天");
        holder.des.setText(p.getDes());
        holder.point.setText(p.getStartPoint()+"元起购");
        holder.risk.setText(p.getRisk());
        return convertView;

    }
}
