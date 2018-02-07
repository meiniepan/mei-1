package com.wuyou.user.mvp.serve;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.wuyou.user.R;
import com.wuyou.user.view.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hjn on 2018/2/6.
 */

public class FastCreateActivity extends BaseActivity {
    @Override
    protected void bindView(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_fast_create;
    }

    public void createOrder(View view) {
        Intent intent = new Intent(getCtx(), NewOrderActivity.class);
        startActivity(intent);
    }

}
