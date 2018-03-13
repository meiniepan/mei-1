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
public class GoodsLevel1Adapter2 extends RecyclerView.Adapter<GoodsLevel1Adapter2.Level1Holder> {

    private List<String> mDatas;
    private final Context mCtx;
    private int nowPos = -1;
    private OnLevelClickListener onLevelClickListener;
    private ArrayList<String> unClickGoodsList = new ArrayList<>();

    public GoodsLevel1Adapter2(Context context, List<String> datas) {
        mCtx = context;
        mDatas = datas;
    }

    public void initData(List<String> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public Level1Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.goods_level_item, parent, false);
        return new Level1Holder(view);
    }

    @Override
    public void onBindViewHolder(final Level1Holder holder, final int position) {
        if (mDatas.size() == 0) return;
        final String s = mDatas.get(position);
        holder.text.setText(s);
        if (unClickGoodsList.contains(s)) {
            holder.text.setBackgroundResource(R.drawable.button_not_choosealbe);
            holder.text.setTextColor(Color.WHITE);
        } else if (position == nowPos) {
            holder.text.setBackgroundResource(R.drawable.button_normal);
            holder.text.setTextColor(Color.WHITE);
        } else {
            holder.text.setBackgroundResource(R.drawable.night_blue_border);
            holder.text.setTextColor(Color.BLACK);
        }
        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (unClickGoodsList.contains(s)) return;
                if (nowPos == position) {
                    nowPos = -1;
                    notifyDataSetChanged();
                    onLevelClickListener.onClick("");
                } else {
                    nowPos = position;
                    notifyDataSetChanged();
                    onLevelClickListener.onClick(s);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void setUnClickableList(ArrayList<String> unClickGoods) {
        this.unClickGoodsList=(unClickGoods);
        notifyDataSetChanged();
    }

    public class Level1Holder extends RecyclerView.ViewHolder {
        public TextView text;

        public Level1Holder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.goods_level_item_text);
        }
    }

    public interface OnLevelClickListener {
        void onClick(String s);
    }

    public void setOnLevelClickListener(OnLevelClickListener onLevelClickListener) {
        this.onLevelClickListener = onLevelClickListener;
    }
}
