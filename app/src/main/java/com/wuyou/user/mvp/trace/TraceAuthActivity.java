package com.wuyou.user.mvp.trace;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wuyou.user.R;
import com.wuyou.user.view.activity.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Solang on 2018/10/22.
 */

public class TraceAuthActivity extends BaseActivity {
    @BindView(R.id.tv_trace_spec)
    TextView tvTraceSpec;
    @BindView(R.id.iv_trace_upload)
    ImageView ivTraceUpload;
    @BindView(R.id.tv_trace_minus)
    TextView tvTraceMinus;
    @BindView(R.id.et_trace_score_num)
    EditText etTraceScoreNum;
    @BindView(R.id.tv_trace_plus)
    TextView tvTracePlus;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_trace_auth;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText(getString(R.string.trace_auth));
    }


    @OnClick({R.id.iv_trace_upload, R.id.btn_trace_upload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_trace_upload:
                break;
            case R.id.btn_trace_upload:
                break;
        }
    }
}
