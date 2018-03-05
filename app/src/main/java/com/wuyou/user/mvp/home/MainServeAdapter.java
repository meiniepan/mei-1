package com.wuyou.user.mvp.home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.bean.response.CategoryParent;
import com.wuyou.user.mvp.serve.ServeCategoryListActivity;
import com.wuyou.user.util.glide.GlideUtils;
import com.wuyou.user.view.widget.recyclerHelper.BaseHolder;
import com.wuyou.user.view.widget.recyclerHelper.BaseQuickAdapter;

import java.util.List;

/**
 * Created by hjn on 2018/2/8.
 */

public class MainServeAdapter extends BaseQuickAdapter<CategoryParent, BaseHolder> {
    Context context;

    public MainServeAdapter(int layoutResId, @Nullable List<CategoryParent> data, Context mCtx) {
        super(layoutResId, data);
        context = mCtx;
    }

    @Override
    protected void convert(BaseHolder helper, CategoryParent item) {
        RecyclerView recyclerView = helper.getView(R.id.main_serve_children);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        MainServeChildrenAdapter adapter = new MainServeChildrenAdapter(R.layout.item_main_serve_child, item.sub);
        recyclerView.setAdapter(adapter);
        helper.setText(R.id.main_serve_parent, item.name);
        ImageView imageView = helper.getView(R.id.main_serve_parent_picture);
        GlideUtils.loadImage(context, item.icon, imageView);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            Intent intent = new Intent(context, ServeCategoryListActivity.class);
            intent.putExtra(Constant.CATEGORY_ID, item.sub.get(position).id);
            intent.putExtra(Constant.CATEGORY_NAME, item.sub.get(position).name);
            context.startActivity(intent);
        });
    }
}
