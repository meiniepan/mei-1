package com.wuyou.user.mvp.serve;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.R;
import com.wuyou.user.view.activity.BaseActivity;
import com.wuyou.user.view.widget.CustomNestRadioGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hjn on 2018/2/6.
 */

public class FastCreateActivity extends BaseActivity {
    @BindView(R.id.fast_create_group)
    CustomNestRadioGroup group;
    @BindView(R.id.fast_radio_high)
    RadioButton radioButton;
    @Override
    protected void bindView(Bundle savedInstanceState) {
        group.setOnCheckedChangeListener((group, checkedId) -> {
            ToastUtils.ToastMessage(getCtx(),checkedId+"............"+group.getCheckedRadioButtonId());
        });
        Log.e("Test", "bindView: "+radioButton.getId());
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
