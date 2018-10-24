package com.wuyou.user.mvp.trace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.wuyou.user.CarefreeApplication;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.data.remote.OrderBean;
import com.wuyou.user.mvp.home.MainServeChildrenAdapter;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.util.glide.GlideUtils;
import com.wuyou.user.view.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Solang on 2018/10/22.
 */

public class TraceAuthActivity extends BaseActivity {
    @BindView(R.id.tv_trace_spec)
    EditText etTraceSpec;
    @BindView(R.id.tv_trace_minus)
    TextView tvTraceMinus;
    @BindView(R.id.et_trace_score_num)
    EditText etTraceScoreNum;
    @BindView(R.id.tv_trace_plus)
    TextView tvTracePlus;
    @BindView(R.id.tv_trace_hint)
    TextView tvHint;
    @BindView(R.id.rv_trace_img)
    RecyclerView recyclerView;
    List<String> data = new ArrayList<>();
    String addBtn = "00";
    private TraceUploadImgAdapter adapter;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_trace_auth;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        data.add(addBtn);
        setTitleText(getString(R.string.trace_auth));
        recyclerView.setLayoutManager(new GridLayoutManager(getCtx(), 3));
        adapter = new TraceUploadImgAdapter(R.layout.item_trace_auth_img, data);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.iv_item_delete) {
                data.remove(position);
                if (!data.get(data.size() - 1).equals("00")) {
                    data.add(data.size(), "00");
                }
                if (data.size() == 1) {
                    tvHint.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (data.get(i).equals("00")) {
                    chosePhoto();
                }
            }
        });
    }


    @OnClick({R.id.btn_trace_upload, R.id.tv_trace_plus, R.id.tv_trace_minus})
    public void onViewClicked(View view) {
        int score = Integer.parseInt(etTraceScoreNum.getText().toString());
        switch (view.getId()) {
            case R.id.btn_trace_upload:
                upload();
                break;
            case R.id.tv_trace_plus:
                etTraceScoreNum.setText(score + 1 + "");
                break;
            case R.id.tv_trace_minus:
                if (score < 1) {
                    return;
                }
                etTraceScoreNum.setText(score - 1 + "");
                break;
        }
    }

    private void upload() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent datas) {
        super.onActivityResult(requestCode, resultCode, datas);
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(datas);
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
                data.add(data.size()-1, path);
                if (data.size() > 6) {
                    data.remove(6);
                }
                adapter.notifyDataSetChanged();
                tvHint.setVisibility(View.GONE);
            }
        }
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
                .enableCrop(false)// 是否裁剪 true or false
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
}
