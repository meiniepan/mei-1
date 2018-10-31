package com.wuyou.user.mvp.volunteer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wuyou.user.R;
import com.wuyou.user.adapter.MainPagerAdapter;
import com.wuyou.user.view.activity.BaseActivity;
import com.wuyou.user.view.activity.CaptureActivity;
import com.wuyou.user.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by DELL on 2018/10/26.
 */

public class TimeBankMainActivity extends BaseActivity {
    @BindView(R.id.time_main_pager)
    ViewPager timeMainPager;
    @BindView(R.id.tb_main_icon)
    ImageView tbMainIcon;
    @BindView(R.id.tb_main_text)
    TextView tbMainText;
    @BindView(R.id.tb_sign_icon)
    ImageView tbSignIcon;
    @BindView(R.id.tb_sign_text)
    TextView tbSignText;
    @BindView(R.id.tb_mine_icon)
    ImageView tbMineIcon;
    @BindView(R.id.tb_mine_text)
    TextView tbMineText;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_time_bank_main;
    }


    List<BaseFragment> fragments = new ArrayList<>();

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText(R.string.time_bank);
        fragments.add(new TimeBankMainFragment());
        fragments.add(new TimeBankMineFragment());
        timeMainPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), fragments));
    }


    @OnClick({R.id.tb_main_ll, R.id.tb_sign_ll, R.id.tb_mine_ll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tb_main_ll:
                setTabStatus(0);
                timeMainPager.setCurrentItem(0);
                break;
            case R.id.tb_sign_ll:
                Intent intent = new Intent(getCtx(), CaptureActivity.class);
                startActivity(intent);
                break;
            case R.id.tb_mine_ll:
                setTabStatus(1);
                timeMainPager.setCurrentItem(1);
                break;
        }
    }

    public void setTabStatus(int tabStatus) {
        tbMainText.setTextColor(tabStatus == 0 ? (getResources().getColor(R.color.night_blue)) : getResources().getColor(R.color.common_dark));
//        tbSignText.setTextColor(tabStatus == 1 ? (getResources().getColor(R.color.night_blue)) : getResources().getColor(R.color.common_dark));
        tbMineText.setTextColor(tabStatus == 1 ? (getResources().getColor(R.color.night_blue)) : getResources().getColor(R.color.common_dark));
        tbMainIcon.setImageResource(tabStatus == 0 ? R.mipmap.tb_main_selected : R.mipmap.tb_main);
//        tbSignIcon.setImageResource(tabStatus == 0 ? R.mipmap.tb_sign_selected : R.mipmap.tb_sign);
        tbMineIcon.setImageResource(tabStatus == 1 ? R.mipmap.tb_mine_selected : R.mipmap.tb_mine);
    }
}
