package com.wuyou.user.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.bean.UserInfo;
import com.wuyou.user.util.glide.Glide4Engine;
import com.wuyou.user.util.glide.GlideUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.util.ConvertUtils;

/**
 * Created by DELL on 2018/3/14.
 */

public class InfoActivity extends BaseActivity {
    @BindView(R.id.info_head)
    ImageView infoHead;
    @BindView(R.id.tv_account_area)
    TextView tvAccountArea;
    @BindView(R.id.tv_phone_area)
    TextView tvPhoneArea;
    @BindView(R.id.tv_email_area)
    TextView tvEmailArea;
    @BindView(R.id.tv_sex_area)
    TextView tvSexArea;
    @BindView(R.id.tv_birthday_area)
    TextView tvBirthdayArea;
    private Uri imagePath;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        UserInfo userInfo = CarefreeDaoSession.getInstance().getUserInfo();
//        GlideUtils.loadImage(this, userInfo.getHead_image(), infoHead, true);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_user_info;
    }


    @OnClick({R.id.info_account_area, R.id.info_phone_area, R.id.info_email_area, R.id.info_sex_area, R.id.info_birthday_area, R.id.info_head})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.info_head:
                chosePhoto();
                break;
            case R.id.info_account_area:
                startActivityForResult(new Intent(getCtx(), ModifyNickActivity.class).putExtra(Constant.FROM, Constant.NICK), Constant.Intent.REQUEST_NICK);
                break;
            case R.id.info_phone_area:
                startActivityForResult(new Intent(getCtx(), ModifyNickActivity.class).putExtra(Constant.FROM, Constant.PHONE), Constant.Intent.REQUEST_PHONE);
                break;
            case R.id.info_email_area:
                startActivityForResult(new Intent(getCtx(), ModifyNickActivity.class).putExtra(Constant.FROM, Constant.EMAIL), Constant.Intent.REQUEST_EMAIL);
                break;
            case R.id.info_sex_area:
                startActivityForResult(new Intent(getCtx(), ModifyGenderActivity.class), Constant.Intent.REQUEST_GENDER);
                break;
            case R.id.info_birthday_area:
                chooseBirthday();
                break;
        }
    }

    private void chooseBirthday() {
        Calendar calendar = Calendar.getInstance();
        final DatePicker picker = new DatePicker(this);
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTopPadding(ConvertUtils.toPx(this, 10));
        picker.setRangeEnd(2111, 1, 11);
        calendar.setTime(new Date(System.currentTimeMillis() + 24 * 3600 * 1000));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        picker.setRangeStart(year, month, day);
        picker.setResetWhileWheel(false);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                calendar.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));
                tvBirthdayArea.setText(year + "-" + month + "-" + day);
            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.show();
    }

    private void chosePhoto() {
        Matisse.from(this)
                .choose(MimeType.ofImage())
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, "com.wuyou.user.FileProvider"))
                .showSingleMediaType(true)
                .theme(R.style.Matisse_Dracula)
                .countable(false)
                .maxSelectable(1)
                .imageEngine(new Glide4Engine())
                .forResult(Constant.REQUEST_CODE_CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constant.REQUEST_CODE_CHOOSE_IMAGE) {
                imagePath = Matisse.obtainResult(data).get(0);
                GlideUtils.loadImage(getCtx(), Matisse.obtainResult(data).get(0).toString(), infoHead, true);
            }else if (requestCode == Constant.Intent.REQUEST_NICK) {
                tvAccountArea.setText(data.getStringExtra("info"));
            }else if (requestCode == Constant.Intent.REQUEST_PHONE) {
                tvPhoneArea.setText(data.getStringExtra("info"));
            }else if (requestCode == Constant.Intent.REQUEST_EMAIL) {
                tvEmailArea.setText(data.getStringExtra("info"));
            }else if (requestCode == Constant.Intent.REQUEST_GENDER) {
                tvSexArea.setText(data.getStringExtra("info"));
            }
        }
    }
}
