package com.wuyou.user.mvp.home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.bean.response.CategoryChild;
import com.wuyou.user.bean.response.CategoryParent;
import com.wuyou.user.mvp.serve.ServeCategoryListActivity;
import com.wuyou.user.util.glide.GlideUtils;
import com.wuyou.user.view.widget.panel.HomePictureDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjn on 2018/2/8.
 */

public class MainServeAdapter extends BaseQuickAdapter<CategoryParent, BaseHolder> {
    private Context context;
    private ArrayList<String> emptyPicureList;

    public MainServeAdapter(int layoutResId, @Nullable List<CategoryParent> data, Context mCtx, ArrayList<String> list) {
        super(layoutResId, data);
        context = mCtx;
        emptyPicureList = list;
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
            CategoryChild categoryChild = item.sub.get(position);
            if (categoryChild.services == 0 && emptyPicureList.contains(categoryChild.id)) {
                showNoServeDialog(categoryChild);
                return;
            }
            Intent intent = new Intent(context, ServeCategoryListActivity.class);
            intent.putExtra(Constant.CATEGORY_ID, categoryChild.id);
            intent.putExtra(Constant.CATEGORY_NAME, categoryChild.name);
            context.startActivity(intent);
        });
    }

    private void showNoServeDialog(CategoryChild item) {
        HomePictureDialog dialog = new HomePictureDialog(mContext, item);
        dialog.show();
    }
}
