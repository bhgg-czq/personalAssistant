package com.zucc.shortterm.personalassistant.Tools;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.zucc.shortterm.personalassistant.Bean.BeanTodo;
import com.zucc.shortterm.personalassistant.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TodoItemAdapter extends RecyclerView.Adapter{
    private static final int LIST_HEADER=1;
    private static final int LIST_ITEM=0;
    private OnitemClick onitemClick;   //定义点击事件接口
    private OnLongClick onLongClick;  //定义长按事件接口
    private List<BeanTodo> mList;
    private DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public TodoItemAdapter(List<BeanTodo> mList)
    {
        this.mList=mList;
    }
    //定义设置点击事件监听的方法
    public void setOnitemClickLintener (OnitemClick onitemClick) {
        this.onitemClick = onitemClick;
    }
    //定义设置长按事件监听的方法
    public void setOnLongClickListener (OnLongClick onLongClick) {
        this.onLongClick = onLongClick;
    }

    //定义一个点击事件的接口
    public interface OnitemClick {
        void onItemClick(BeanTodo.Content item);
    }
    //定义一个长按事件的接口
    public interface OnLongClick {
        void onLongClick(BeanTodo.Content item);
    }

    static class ContentViewHolder extends RecyclerView.ViewHolder {
        AppCompatCheckBox checkBox;
        TextView text;
        TextView time;
        View itemview;

        ContentViewHolder(View view) {
            super(view);
            itemview=view;
            checkBox = view.findViewById(R.id.checkbox);
            text=view.findViewById(R.id.text);
            time = view.findViewById(R.id.time);
        }


    }

    static class TitleViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        TitleViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.list_header);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == LIST_ITEM)
            return new ContentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_listview_item, parent, false));
        else
            return new TitleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_listview_header, parent, false));







    }
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {



        if (getItemViewType(position) == LIST_ITEM) {
            final ContentViewHolder contentViewHolder = (ContentViewHolder) holder;

            BeanTodo.Content content = (BeanTodo.Content) mList.get(position);

            contentViewHolder.checkBox.setChecked((content.getHaveDown()==1)? true:false);
            contentViewHolder.text.setText(content.getName());
            contentViewHolder.time.setText(format.format(content.getDate()));

            switch(content.getHaveDown()){
                case 0:
                    contentViewHolder.text.setTextColor(0xff000000);
                    contentViewHolder.time.setTextColor(0xff000000);
                    break;
                case 1:
                    contentViewHolder.text.setTextColor(0xffbebebe);
                    contentViewHolder.time.setTextColor(0xffbebebe);
                    break;
            }
            contentViewHolder.checkBox.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){


                    if (contentViewHolder.checkBox.isChecked())
                    {
                        ((BeanTodo.Content) mList.get(position)).setHaveDown(1);
                        Log.d("aa","aaaa");

                    }

                    else
                    {
                        ((BeanTodo.Content) mList.get(position)).setHaveDown(0);
                        Log.d("bbbbb","aaaa");
                    }

                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            notifyDataSetChanged();
                        }
                    });
                    sort();
                }
            });



        } else {
            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
            BeanTodo.Title header = (BeanTodo.Title) mList.get(position);
            titleViewHolder.title.setText(header.getName());
        }



        if (onitemClick != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//进行监听点击事件，并且实现接口
                    int position=holder.getAdapterPosition();

                    BeanTodo.Content item=(BeanTodo.Content)mList.get(position);
                    onitemClick.onItemClick(item);
                }
            });
        }

        if (onLongClick != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position=holder.getAdapterPosition();

                    BeanTodo.Content item=(BeanTodo.Content)mList.get(position);
                    onLongClick.onLongClick(item);
                    return true;
                }
            });
        }


    }
    @Override
    public int getItemCount(){
        return mList.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override

    public int getItemViewType(int position) {
        return isDone(position) ? LIST_ITEM : LIST_HEADER;
    }


    private boolean isDone(int position) {
        return mList.get(position) instanceof BeanTodo.Content;
    }


    public void sort(){
        BeanTodo haveNotTitle=new BeanTodo.Title("未完成");
        BeanTodo haveDoneTitle=new BeanTodo.Title("已完成");
        List<BeanTodo.Content> notHaveDone =new ArrayList<>();
        List<BeanTodo.Content> haveDone =new ArrayList<>();


        for(int i=0;i<mList.size();i++){
            if (getItemViewType(i)==LIST_ITEM){
                System.out.println("未"+mList.get(i).getName());
                BeanTodo.Content a=(BeanTodo.Content)mList.get(i);
                if (a.getHaveDown()==0)
                    notHaveDone.add((BeanTodo.Content)mList.get(i));
                else{
                    System.out.println("已经"+mList.get(i).getName());
                    haveDone.add((BeanTodo.Content)mList.get(i));
                }

            }

        }

        mList.clear();
        System.out.println("1"+mList.size());
        System.out.println("1"+haveDone.size());
        System.out.println("1"+notHaveDone.size());

        if (notHaveDone.size()>0){
            mList.add(haveNotTitle);
            mList.addAll(notHaveDone);

        }

        if (haveDone.size()>0){
            mList.add(haveDoneTitle);
            mList.addAll(haveDone);

        }


        haveDone.clear();
        notHaveDone.clear();
    }
}
