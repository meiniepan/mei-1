package com.wuyou.user.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wuyou.user.view.activity.GuideActivity;

import java.util.List;

/**
 * Created by hjn on 2016/11/1.
 */
public class GuidePagerAdapter extends PagerAdapter {
    private List<Integer> lists;
    private GuideActivity mAct;
    private ImageView imgView;

    public GuidePagerAdapter(GuideActivity activity, List<Integer> mList) {
        lists = mList;
        this.mAct = activity;
    }

    /**
     * 获得页面的总数
     */
    public int getCount() {
        return lists.size();
    }

    /**
     * 获得相应位置上的view container view的容器
     */
    public Object instantiateItem(ViewGroup container, final int position) {
        // imgView.setOnClickListener(this);

        imgView = new ImageView(mAct);
        imgView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        imgView.setScaleType(ImageView.ScaleType.FIT_XY);
        if (lists.size() == 0) {

        } else {
            Integer integer = lists.get(position);
            imgView.setBackgroundResource(integer);
        }
        container.addView(imgView);
        return imgView;
    }

    /**
     * 判断 view和object的对应关系
     */
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 销毁对应位置上的object
     */
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        object = null;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
