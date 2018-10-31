package com.wuyou.user.mvp.trace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.ErrorBody;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.utils.TribeDateUtils;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.wuyou.user.CarefreeApplication;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.data.EoscDataManager;
import com.wuyou.user.data.local.db.TraceIPFSBean;
import com.wuyou.user.network.ipfs.ChainIPFS;
import com.wuyou.user.network.ipfs.ChainNamedStreamable;
import com.wuyou.user.util.RxUtil;
import com.wuyou.user.view.activity.BaseActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

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
    @BindView(R.id.tv_trace_keyword)
    TextView tvTraceKeyword;
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
        keyword = getIntent().getStringExtra(Constant.TRACE_KEY_WORD);
        tvTraceKeyword.setText(keyword);
        setTitleIcon(R.mipmap.trace_list, v -> negativeTiList());
        data.add(addBtn);
        setTitleText(getString(R.string.trace_auth));
        recyclerView.setLayoutManager(new GridLayoutManager(getCtx(), 3));
        adapter = new TraceUploadImgAdapter(R.layout.item_trace_auth_img, data);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.iv_item_delete) {
                data.remove(position);
                pictureCodeList.remove(position);
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

    private void negativeTiList() {
        Intent intent = new Intent(getCtx(), TraceUploadRecordActivity.class);
        startActivity(intent);
    }


    @OnClick({R.id.btn_trace_upload, R.id.tv_trace_plus, R.id.tv_trace_minus})
    public void onViewClicked(View view) {
        int score = Integer.parseInt(etTraceScoreNum.getText().toString());
        switch (view.getId()) {
            case R.id.btn_trace_upload:
                if (pictureCodeList.size() == 0 || etTraceSpec.length() == 0) {
                    return;
                }
                if (score == 0) {
                    ToastUtils.ToastMessage(getCtx(), "质押积分不能为0");
                    return;
                }
                uploadTrace(etTraceSpec.getText().toString().trim(), pictureCodeList, score);
                break;
            case R.id.tv_trace_plus:
                etTraceScoreNum.setText(score + 1 + "");
                break;
            case R.id.tv_trace_minus:
                if (score <= 1) {
                    return;
                }
                etTraceScoreNum.setText(score - 1 + "");
                break;
        }
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
                uploadPicture(path);
            }
        }
    }


    ArrayList<String> pictureCodeList = new ArrayList<>();

    private void uploadPicture(String desFilePath) {
        File desFile = new File(desFilePath);
        showLoadingDialog();
        Observable.create((ObservableOnSubscribe<String>) e -> {
            ChainIPFS ipfs = new ChainIPFS(Constant.IPFS_URL.contains(Constant.BASE_CHAIN_URL) ? Constant.BASE_CHAIN_URL : Constant.DEV_BASE_CHAIN_URL, 5001);
            ipfs.local();
            ChainNamedStreamable.FileWrapper file = new ChainNamedStreamable.FileWrapper(desFile);
            e.onNext(ipfs.addFile(Collections.singletonList(file)));
        }).compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<String>() {
                    @Override
                    public void onSuccess(String hashCode) {
                        data.add(data.size() - 1, desFilePath);
                        if (data.size() > 6) {
                            data.remove(6);
                        }
                        adapter.notifyDataSetChanged();
                        tvHint.setVisibility(View.GONE);
                        Log.e("Carefree", "uploadFileToIpfs onSuccess: " + hashCode);//QmV2V7MBRAxZub8931MB5LMRT6SwJQQfmqstmqz5dXLCbJ
                        pictureCodeList.add(hashCode);
                        dismissDialog();
                    }
                });
    }

    private void chosePhoto() {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .maxSelectNum(1)// 最大图片选择数量 int
                .imageSpanCount(4)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .isCamera(true)// 是否显示拍照按钮 true or false
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                .enableCrop(false)// 是否裁剪 true or false
                .compress(true)// 是否压缩 true or false
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .compressSavePath(CarefreeApplication.getInstance().getApplicationContext().getFilesDir().getAbsolutePath())//压缩图片保存地址
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(true)// 是否圆形裁剪 true or false
                .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(false)// 是否开启点击声音 true or false
                .cropCompressQuality(50)// 裁剪压缩质量 默认90 int
                .minimumCompressSize(50)// 小于100kb的图片不压缩
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .isDragFrame(false)// 是否可拖动裁剪框(固定)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    private String keyword = "溯源认证";

    public void uploadTrace(String content, List<String> pictureHashList, int amount) {
        showLoadingDialog();
        TraceIPFSBean bean = new TraceIPFSBean();
        bean.account_name = CarefreeDaoSession.getInstance().getMainAccount().getName();
        bean.timestamp = TribeDateUtils.dateFormat7(new Date(System.currentTimeMillis()));
        bean.content = content;
        bean.phone = CarefreeDaoSession.getInstance().getUserInfo().getMobile();
        bean.picture = pictureHashList;
        bean.node_name = "庄胜广场";
        bean.keyword = keyword;
        bean.setStatus(-1);
        try {
            uploadFileToIpfs(bean, amount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadFileToIpfs(TraceIPFSBean bean, int amount) throws IOException {
        String desString = new GsonBuilder().create().toJson(bean);
        Observable.create((ObservableOnSubscribe<String>) e -> {
            ChainIPFS ipfs = new ChainIPFS(Constant.IPFS_URL.contains(Constant.BASE_CHAIN_URL) ? Constant.BASE_CHAIN_URL : Constant.DEV_BASE_CHAIN_URL, 5001);
            ipfs.local();
            ChainNamedStreamable file = new ChainNamedStreamable.ByteArrayWrapper(desString.getBytes());
            e.onNext(ipfs.addFile(Collections.singletonList(file)));
        }).flatMap(hashcode -> EoscDataManager.getIns().transfer(CarefreeDaoSession.getInstance().getMainAccount().getName(), Constant.EOSIO_TRACE_PC, amount, hashcode))
                .doOnNext(jsonObject -> CarefreeDaoSession.getInstance().addTraceRecord(bean))
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jsonObject) {
                        ToastUtils.ToastMessage(getCtx(), "溯源认证提交成功");
                        Intent intent = new Intent(getCtx(), TraceUploadRecordActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    protected void onNodeFail(int code, ErrorBody.DetailErrorBean message) {
                        super.onNodeFail(code, message);
                        if (message.message.contains("overdrawn balance")) {
                            ToastUtils.ToastMessage(getCtx(), "积分余额不足");
                        }
                    }
                });
    }
}
