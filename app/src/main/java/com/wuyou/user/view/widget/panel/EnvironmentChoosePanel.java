package com.wuyou.user.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.gs.buluo.common.network.TokenEvent;
import com.gs.buluo.common.utils.DensityUtils;
import com.gs.buluo.common.utils.SharePreferenceManager;
import com.wuyou.user.CarefreeApplication;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.event.LoginEvent;
import com.wuyou.user.view.widget.CustomNestRadioGroup;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

/**
 * Created by hjn on 2017/1/3.
 */

public class EnvironmentChoosePanel extends Dialog {
    public EnvironmentChoosePanel(Context context) {
        super(context, R.style.bottom_dialog);
        initView();
    }

    private void initView() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.env_choose_board, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = DensityUtils.dip2px(getContext(), 300);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);

        CustomNestRadioGroup radioGroup = rootView.findViewById(R.id.env_group);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> setEnv(checkedId));

        findViewById(R.id.env_login).setOnClickListener(v -> EventBus.getDefault().post(new TokenEvent()));
    }


    public void setEnv(int env) {
        switch (env) {
            case R.id.env_dev:
                Constant.WEB_URL = "https://dev.activity.iwantmei.com/";
                Constant.BASE_URL = "https://develop.api.iwantmei.com/customer/v1/";
                break;
            case R.id.env_test:
                Constant.WEB_URL = "https://stage.activity.iwantmei.com/";
                Constant.BASE_URL = "https://stage.api.iwantmei.com/customer/v1/";
                break;
            case R.id.env_online:
                Constant.WEB_URL = "https://activities.iwantmei.com/";
                Constant.BASE_URL = "https://api.iwantmei.com/customer/v1/";
                break;
        }
        SharePreferenceManager.getInstance(getContext()).setValue(Constant.SP_BASE_URL, Constant.BASE_URL);
        SharePreferenceManager.getInstance(CarefreeApplication.getInstance().getApplicationContext()).setValue(Constant.SP_WEB_URL, Constant.WEB_URL);
    }
}
