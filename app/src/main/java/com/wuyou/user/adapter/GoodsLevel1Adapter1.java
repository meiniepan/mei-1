package com.wuyou.user.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.wuyou.user.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjn on 2016/11/18.
 */
public class GoodsLevel1Adapter1 extends RecyclerView.Adapter<GoodsLevel1Adapter1.Level1Holder>{

    private List<String> mDatas=new ArrayList<>();
    private final Context mCtx;
    private int nowPos =-1;
    private OnLevelClickListener onLevelClickListener;
    private ArrayList<String> unClickList =new ArrayList<>();

    public GoodsLevel1Adapter1(Context context, List<String> datas){
        mCtx = context;
        mDatas = datas;
    }

    @Override
    public Level1Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.goods_level_item, parent,false);
        return new Level1Holder(view);
    }

    @Override
    public void onBindViewHolder(final Level1Holder holder, final int position) {
        if (mDatas.size()==0)return;
        final String text = mDatas.get(position);
        holder.text.setText(text);
        if (unClickList.contains(text)){
            holder.text.setBackgroundResource(R.drawable.button_not_choosealbe);
            holder.text.setTextColor(Color.WHITE);
        }else if (position==nowPos){
            holder.text.setBackgroundResource(R.drawable.button_normal);
            holder.text.setTextColor(Color.WHITE);
        }else {
            holder.text.setBackgroundResource(R.drawable.night_blue_border);
            holder.text.setTextColor(0xff627db9);
        }
        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (unClickList.contains(text))return;
                if (nowPos==position){
                    nowPos=-1;
                    notifyDataSetChanged();
                    onLevelClickListener.onClick("");
                }else {
                    nowPos =position;
                    notifyDataSetChanged();
                    onLevelClickListener.onClick(text);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setUnClickableList(ArrayList<String> unClick) {
        this.unClickList = unClick;
        notifyDataSetChanged();
    }

    public class Level1Holder extends RecyclerView.ViewHolder {
        public TextView text;
        public Level1Holder(View itemView) {
            super(itemView);
            text= (TextView) itemView.findViewById(R.id.goods_level_item_text);
        }
    }

    public interface OnLevelClickListener {
        void onClick(String s);
    }

    public void setOnLevelClickListener(OnLevelClickListener onLevelClickListener){
        this.onLevelClickListener=onLevelClickListener;
    }
}
