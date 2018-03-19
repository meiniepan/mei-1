package com.wuyou.user.view.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.bean.OrderBean;

import butterknife.BindView;
import io.techery.properratingbar.ProperRatingBar;

/**
 * Created by DELL on 2018/3/14.
 */

public class CommentActivity extends BaseActivity {
    @BindView(R.id.comment_)
    TextView comment;
    @BindView(R.id.comment_star)
    ProperRatingBar commentStar;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        OrderBean orderBean = getIntent().getParcelableExtra(Constant.ORDER_BEAN);
        comment.setText(orderBean.service.service_name);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_comment;
    }
}
