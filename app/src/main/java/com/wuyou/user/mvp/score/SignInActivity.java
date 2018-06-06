package com.wuyou.user.mvp.score;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wuyou.user.R;
import com.wuyou.user.adapter.MainPagerAdapter;
import com.wuyou.user.view.activity.BaseActivity;
import com.wuyou.user.view.fragment.BaseFragment;
import com.wuyou.user.view.widget.UnScrollViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by DELL on 2018/6/5.
 */

public class SignInActivity extends BaseActivity {
    @BindView(R.id.sign_pager)
    UnScrollViewPager signPager;
    @BindView(R.id.sign_icon)
    ImageView signIcon;
    @BindView(R.id.sign_text)
    TextView signText;
    @BindView(R.id.sign_record_icon)
    ImageView signRecordIcon;
    @BindView(R.id.sign_record)
    TextView signRecord;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        ArrayList<BaseFragment> fragments = new ArrayList<>();
        fragments.add(new SignInFragment());
        fragments.add(new SignInRecordFragment());
        signPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), fragments));
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_sign_up;
    }


    private boolean showFirst = true;

    @OnClick({R.id.sign_in_first, R.id.sign_in_second})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sign_in_first:
                showFirst = true;
                signPager.setCurrentItem(0);
                switchStatus();
                break;
            case R.id.sign_in_second:
                showFirst = false;
                signPager.setCurrentItem(1);
                switchStatus();
                break;
        }
    }

    private void switchStatus() {
        if (showFirst) {
            signIcon.setImageResource(R.mipmap.sign_in);
            signText.setTextColor(getResources().getColor(R.color.night_blue));
            signRecordIcon.setImageResource(R.mipmap.sign_record_negative);
            signRecord.setTextColor(getResources().getColor(R.color.common_gray));
        } else {
            signIcon.setImageResource(R.mipmap.sign_icon_negative);
            signText.setTextColor(getResources().getColor(R.color.common_gray));
            signRecordIcon.setImageResource(R.mipmap.sign_record);
            signRecord.setTextColor(getResources().getColor(R.color.night_blue));
        }
    }

}
