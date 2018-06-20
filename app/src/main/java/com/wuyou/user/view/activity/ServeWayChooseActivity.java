package com.wuyou.user.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.bean.ServeMode;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by DELL on 2018/5/14.
 */

public class ServeWayChooseActivity extends BaseActivity {
    @BindView(R.id.serve_mode_recycler)
    RecyclerView serveModeRecycler;
    private ArrayList<ServeMode> list;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        list = getIntent().getParcelableArrayListExtra(Constant.SERVE_MODES);
        serveModeRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        BaseQuickAdapter<ServeMode, BaseHolder> adapter = new BaseQuickAdapter<ServeMode, BaseHolder>(R.layout.item_serve_mode, list) {
            @Override
            protected void convert(BaseHolder baseHolder, ServeMode serveMode) {
                baseHolder.setText(R.id.item_serve_mode_name, serveMode.name);
                baseHolder.setChecked(R.id.item_serve_mode_radio, baseHolder.getAdapterPosition() == selectPos);
            }
        };
        serveModeRecycler.setAdapter(adapter);
        adapter.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            selectPos = i;
            adapter.notifyDataSetChanged();
        });
    }

    private int selectPos = 0;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_serve_choose;
    }

    public void confirmWay(View view) {
        Intent intent =  new Intent();
        intent.putExtra(Constant.SERVE_MODE,list.get(selectPos));
        setResult(RESULT_OK,intent);
        finish();
    }
}
