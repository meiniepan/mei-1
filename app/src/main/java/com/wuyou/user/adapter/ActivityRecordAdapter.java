package com.wuyou.user.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.utils.TribeDateUtils;
import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.data.EoscDataManager;
import com.wuyou.user.data.remote.ActivityRecordBean;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.util.RxUtil;
import com.wuyou.user.util.glide.GlideUtils;
import com.wuyou.user.view.activity.WebActivity;

import java.util.Date;

/**
 * Created by DELL on 2018/9/17.
 */

public class ActivityRecordAdapter extends BaseQuickAdapter<ActivityRecordBean, BaseHolder> {
    public ActivityRecordAdapter() {
        super(R.layout.item_activity_record);
    }

    @Override
    protected void convert(BaseHolder baseHolder, ActivityRecordBean activityRecordBean) {
        TextView tvStatus = baseHolder.getView(R.id.activity_record_status);
        TextView tvObtain = baseHolder.getView(R.id.activity_record_obtain);
        TextView tvTicket = baseHolder.getView(R.id.activity_record_ticket);
        switch (activityRecordBean.status) {
            case 0:
                tvStatus.setText("待参加");
                tvStatus.setTextColor(mContext.getResources().getColor(R.color.common_red));
                tvObtain.setVisibility(View.GONE);
                tvTicket.setVisibility(View.VISIBLE);
                break;
            case 1:
                tvStatus.setText("已参加");
                tvStatus.setTextColor(0xff666666);
                tvObtain.setVisibility(View.VISIBLE);
                tvTicket.setVisibility(View.VISIBLE);
                break;
            case 2:
                tvStatus.setText("未参加");
                tvStatus.setTextColor(mContext.getResources().getColor(R.color.common_gray));
                tvObtain.setVisibility(View.GONE);
                tvTicket.setVisibility(View.VISIBLE);
                break;
            case 3:
                tvStatus.setText("未支付");
                tvStatus.setTextColor(mContext.getResources().getColor(R.color.common_red));
                tvObtain.setVisibility(View.GONE);
                tvTicket.setVisibility(View.GONE);
                break;
        }

        baseHolder.setText(R.id.activity_record_time, "活动时间：" + TribeDateUtils.dateFormat9(new Date(activityRecordBean.activity.start_at * 1000))
                + "-" + TribeDateUtils.dateFormat9(new Date(activityRecordBean.activity.end_at)).split(" ")[1])
                .setText(R.id.activity_record_price, activityRecordBean.price == 0 ? "免费" : CommonUtil.formatPrice(activityRecordBean.price))
                .setText(R.id.activity_record_title, activityRecordBean.activity.title);

        GlideUtils.loadRoundCornerImage(mContext, activityRecordBean.activity.image.get(0), baseHolder.getView(R.id.activity_record_picture));

        tvObtain.setOnClickListener(v -> getActivityRewards(activityRecordBean.activity.title, activityRecordBean.activity.points));
        tvTicket.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, WebActivity.class);
            intent.putExtra(Constant.WEB_INTENT, Constant.WEB_URL + "activity_proof?user_id=" + CarefreeDaoSession.getInstance().getUserId() + "&Authorization=" + CarefreeDaoSession.getInstance().getUserInfo().getToken() + "&order_id=" + activityRecordBean.order_id + "&type=1");
            mContext.startActivity(intent);
        });
    }

    private void getActivityRewards(String topic, String points) {
        EoscDataManager.getIns().getActivityRewards(topic, points).compose(RxUtil.switchSchedulers())
                .subscribeWith(new BaseSubscriber<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jsonObject) {
                        ToastUtils.ToastMessage(mContext, "领取成功");
                    }
                });
    }
}
