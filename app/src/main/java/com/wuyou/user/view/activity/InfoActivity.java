package com.wuyou.user.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.TribeDateUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.wuyou.user.CarefreeApplication;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.data.local.db.UserInfo;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.UserApis;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.util.RxUtil;
import com.wuyou.user.util.glide.GlideUtils;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.util.ConvertUtils;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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

    private int gender;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        UserInfo userInfo = CarefreeDaoSession.getInstance().getUserInfo();
        if (CarefreeDaoSession.getAvatar(userInfo) != null)
            GlideUtils.loadImage(this, CarefreeDaoSession.getAvatar(userInfo), infoHead, true);
        tvPhoneArea.setText(CommonUtil.getPhoneWithStar(userInfo.getMobile()));
        showLoadingDialog();
        CarefreeRetrofit.getInstance().createApi(UserApis.class)
                .getUserInfo(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns().buildGet())
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<BaseResponse<UserInfo>>() {
                    @Override
                    public void onSuccess(BaseResponse<UserInfo> userInfoBaseResponse) {
                        setUserData(userInfoBaseResponse.data);
                    }
                });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_user_info;
    }


    @OnClick({R.id.info_account_area, R.id.info_phone_area, R.id.info_email_area, R.id.info_sex_area, R.id.info_birthday_area, R.id.info_head})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.info_head:
                chosePhoto();
                break;
            case R.id.info_account_area:
                intent.setClass(getCtx(), ModifyNickActivity.class);
                startActivityForResult(intent.putExtra(Constant.FROM, Constant.NICK), Constant.REQUEST_NICK);
                break;
            case R.id.info_phone_area:
                intent.setClass(getCtx(), ModifyPhoneActivity.class);
                startActivityForResult(intent.putExtra(Constant.FROM, Constant.PHONE), Constant.REQUEST_PHONE);
                break;
            case R.id.info_email_area:
                intent.setClass(getCtx(), ModifyNickActivity.class);
                startActivityForResult(intent.putExtra(Constant.FROM, Constant.EMAIL), Constant.REQUEST_EMAIL);
                break;
            case R.id.info_sex_area:
                intent.setClass(getCtx(), ModifyGenderActivity.class);
                intent.putExtra(Constant.GENDER, gender);
                startActivityForResult(intent, Constant.REQUEST_GENDER);
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
        picker.setRangeStart(1940, 1, 1);
        picker.setSelectedItem(1980, 1, 1);
        picker.setTextColor(getResources().getColor(R.color.night_blue));
        picker.setSubmitTextColor(getResources().getColor(R.color.night_blue));
        picker.setCancelTextColor(getResources().getColor(R.color.common_gray));
        calendar.setTime(new Date(System.currentTimeMillis() + 24 * 3600 * 1000));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        picker.setRangeEnd(year, month, day);
        picker.setResetWhileWheel(false);
        picker.setOnDatePickListener((DatePicker.OnYearMonthDayPickListener) (year1, month1, day1) -> updateBirthday(year1 + "-" + month1 + "-" + day1));
        picker.show();
    }

    private void updateBirthday(String birth) {
        showLoadingDialog();
        CarefreeRetrofit.getInstance().createApi(UserApis.class)
                .updateUserInfo(CarefreeDaoSession.getInstance().getUserId(), QueryMapBuilder.getIns()
                        .put("field", "birthday")
                        .put("value", birth)
                        .buildPost())
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        tvBirthdayArea.setText(birth);
                    }
                });
    }

    private void chosePhoto() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
//                .theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .maxSelectNum(1)// 最大图片选择数量 int
//                .minSelectNum()// 最小选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
//                .previewImage()// 是否可预览图片 true or false
//                .previewVideo()// 是否可预览视频 true or false
//                .enablePreviewAudio() // 是否可播放音频 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
//                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .enableCrop(true)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
//                .glideOverride()// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                .hideBottomControls()// 是否显示uCrop工具栏，默认不显示 true or false
//                .isGif()// 是否显示gif图片 true or false
                .compressSavePath(CarefreeApplication.getInstance().getApplicationContext().getFilesDir().getAbsolutePath())//压缩图片保存地址
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(true)// 是否圆形裁剪 true or false
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(false)// 是否开启点击声音 true or false
//                .selectionMedia()// 是否传入已选图片 List<LocalMedia> list
//                .previewEggs()// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                .cropCompressQuality(50)// 裁剪压缩质量 默认90 int
                .minimumCompressSize(50)// 小于100kb的图片不压缩
//                .synOrAsy(true)//同步true或异步false 压缩 默认同步
//                .cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
//                .rotateEnabled() // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
//                .videoQuality()// 视频录制质量 0 or 1 int
//                .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
//                .videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
//                .recordVideoSecond()//视频秒数录制 默认60s int
                .isDragFrame(false)// 是否可拖动裁剪框(固定)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                String path = "";
                if (selectList != null && selectList.size() > 0) {
                    LocalMedia localMedia = selectList.get(0);
                    if (localMedia.isCompressed()) {
                        path = localMedia.getCompressPath();
                    } else if (localMedia.isCut()) {
                        path = localMedia.getCutPath();
                    } else {
                        path = localMedia.getPath();
                    }
                }
                GlideUtils.loadImageNoHolder(getCtx(), path, infoHead, true);
                uploadAvatar(path);
            } else if (requestCode == Constant.REQUEST_NICK) {
                tvAccountArea.setText(data.getStringExtra("info"));
            } else if (requestCode == Constant.REQUEST_PHONE) {
                tvPhoneArea.setText(CommonUtil.getPhoneWithStar(data.getStringExtra("info")));
            } else if (requestCode == Constant.REQUEST_EMAIL) {
                tvEmailArea.setText(data.getStringExtra("info"));
            } else if (requestCode == Constant.REQUEST_GENDER) {
                gender = data.getIntExtra("info", 0);
                tvSexArea.setText(getGenderString(gender));
            }
        }
    }

    private static final long MAX_NUM_PIXELS_THUMBNAIL = 64 * 64;

    private void uploadAvatar(String path) {
        CarefreeDaoSession.tempAvatar = path;
        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);
        CarefreeRetrofit.getInstance().createApi(UserApis.class).updateAvatar(CarefreeDaoSession.getInstance().getUserId(), body, QueryMapBuilder.getIns().buildPost())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        CarefreeDaoSession.tempAvatar = null;
                    }
                });
    }

    private String getGenderString(int gender) {
        if (gender == 0) return "男";
        else if (gender == 1) return "女";
        else return "保密";
    }

    public void setUserData(UserInfo userInfo) {
        if (userInfo.getNickname() != null) tvAccountArea.setText(userInfo.getNickname());
        if (userInfo.getGender() != null) {
            gender = Integer.parseInt(userInfo.getGender());
            tvSexArea.setText(getGenderString(gender));
        }
        if (userInfo.getBirthday() != null)
            tvBirthdayArea.setText(TribeDateUtils.dateFormat5(new Date(Long.parseLong(userInfo.getBirthday()) * 1000)));
        if (userInfo.getEmail() != null) tvEmailArea.setText(userInfo.getEmail());
        if (userInfo.getAvatar() != null)
            GlideUtils.loadImageNoHolder(this, CarefreeDaoSession.getAvatar(userInfo), infoHead, true);
    }
}
