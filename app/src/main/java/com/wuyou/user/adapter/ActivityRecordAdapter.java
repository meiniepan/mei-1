package com.wuyou.user.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.gs.buluo.common.network.ApiException;
import com.gs.buluo.common.network.BaseResponse;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.QueryMapBuilder;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.common.utils.TribeDateUtils;
import com.gs.buluo.common.widget.LoadingDialog;
import com.gs.buluo.common.widget.recyclerHelper.BaseHolder;
import com.gs.buluo.common.widget.recyclerHelper.BaseQuickAdapter;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.data.EoscDataManager;
import com.wuyou.user.data.remote.ActivityRecordBean;
import com.wuyou.user.network.CarefreeRetrofit;
import com.wuyou.user.network.apis.ActivityApis;
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
        if (activityRecordBean.points_is_received == 1) {
            tvStatus.setText("已领取");
            tvStatus.setTextColor(mContext.getResources().getColor(R.color.common_dark));
            tvObtain.setVisibility(View.GONE);
            tvTicket.setVisibility(View.VISIBLE);
        } else {
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
        }

        baseHolder.setText(R.id.activity_record_time, "活动时间：" + TribeDateUtils.dateFormat9(new Date(activityRecordBean.activity.start_at * 1000))
                + "-" + TribeDateUtils.dateFormat9(new Date(activityRecordBean.activity.end_at)).split(" ")[1])
                .setText(R.id.activity_record_price, activityRecordBean.price == 0 ? "免费" : CommonUtil.formatPrice(activityRecordBean.price))
                .setText(R.id.activity_record_title, activityRecordBean.activity.title);

        GlideUtils.loadRoundCornerImage(mContext, activityRecordBean.activity.image.get(0), baseHolder.getView(R.id.activity_record_picture));

        tvObtain.setOnClickListener(v -> getActivityRewards(activityRecordBean.activity.title, activityRecordBean.activity.points, activityRecordBean.order_id, baseHolder.getAdapterPosition()));
        tvTicket.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, WebActivity.class);
            intent.putExtra(Constant.WEB_INTENT, Constant.WEB_URL + "activity_proof?user_id=" + CarefreeDaoSession.getInstance().getUserId() + "&Authorization=" + CarefreeDaoSession.getInstance().getUserInfo().getToken() + "&order_id=" + activityRecordBean.order_id + "&type=1");
            mContext.startActivity(intent);
        });
    }

    private void getActivityRewards(String topic, String points, String order_id, int position) {
        LoadingDialog.getInstance().show(mContext, "", false);
        EoscDataManager.getIns().getActivityRewards(topic, points).compose(RxUtil.switchSchedulers())
                .subscribeWith(new BaseSubscriber<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jsonObject) {
                        String transactionId = jsonObject.get("transaction_id").toString().trim();
                        updateActivityStatus(order_id, transactionId, position);
                    }

                    @Override
                    protected void onFail(ApiException e) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Log.e("Carefree", "onError: " + e.toString());
                        if (e.toString().contains("Internal Server Error")) {
                            updateActivityStatus(order_id, CommonUtil.getRandomString(10), position);
                        } else {
                            ToastUtils.ToastMessage(mContext, R.string.connect_fail);
                        }
                    }
                });
    }

    private void updateActivityStatus(String orderId, String transactionId, int position) {
        CarefreeRetrofit.getInstance().createApi(ActivityApis.class).updateActivityStatus(orderId,
                QueryMapBuilder.getIns().put("user_id", CarefreeDaoSession.getInstance().getUserId()).put("transaction_id", transactionId).buildPost())
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        ToastUtils.ToastMessage(mContext, R.string.get_record_success);
                        getData().get(position).points_is_received = 1;
                        notifyItemChanged(position);
                    }
                });
    }
}
