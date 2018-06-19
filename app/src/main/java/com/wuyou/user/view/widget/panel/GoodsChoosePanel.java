package com.wuyou.user.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.adapter.GoodsLevel1Adapter1;
import com.wuyou.user.bean.ServeDetailBean;
import com.wuyou.user.bean.ServeStandard;
import com.wuyou.user.mvp.serve.NewOrderActivity;
import com.wuyou.user.util.CommonUtil;
import com.wuyou.user.util.glide.GlideUtils;
import com.wuyou.user.util.layoutmanager.AutoLineFeedLayoutManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hjn on 2016/11/17.
 */
public class GoodsChoosePanel extends Dialog implements View.OnClickListener {
    @BindView(R.id.goods_level1)
    RecyclerView leve1View1;
    @BindView(R.id.goods_board_number)
    TextView mNumber;
    @BindView(R.id.goods_board_choose_remain)
    TextView mRemainNumber;
    @BindView(R.id.goods_choose_icon)
    ImageView mIcon;
    @BindView(R.id.choose_board_price)
    TextView boardPrice;
    @BindView(R.id.choose_board_title)
    TextView boardTitle;

    private int nowNum = 1;
    private ServeStandard selectedStandard;
    private ServeDetailBean defaultEntity;

    public GoodsChoosePanel(Context context) {
        super(context, R.style.my_dialog);
        initView();
    }

    public void setData(ServeDetailBean entity, String price) {
        defaultEntity = entity;
        GlideUtils.loadRoundCornerImage(getContext(), entity.photo, mIcon);
        boardTitle.setText(entity.title);
        mRemainNumber.setText(entity.stock + "");
        boardPrice.setText("¥ " + price + "/" + defaultEntity.unit);
        if (defaultEntity.has_specification == 1) {
            findViewById(R.id.choose_board_level_layout).setVisibility(View.VISIBLE);
            setLevelOneData(entity.specification);
        }
    }

    private void setLevelOneData(List<ServeStandard> standards) {
        ServeStandard serveStandard = standards.get(0).clone();
        serveStandard.name = "氨基酸的理解ask来";
        serveStandard.stock = 0;
        standards.add(serveStandard);
        ServeStandard serveStandard1 = serveStandard.clone();
        serveStandard1.name = "i基金切忌琼文炯气温为哦亲我姐哦";
        serveStandard1.stock = 10;
        standards.add(serveStandard1);
        ServeStandard serveStandard2 = serveStandard.clone();
        serveStandard2.name = "飒飒的让他恢复和投放广告风格风格的歌";
        standards.add(serveStandard2);
        final GoodsLevel1Adapter1 adapter1 = new GoodsLevel1Adapter1(R.layout.goods_level_item, standards);
        leve1View1.setAdapter(adapter1);
        adapter1.setOnItemClickListener((baseQuickAdapter, view, i) -> {
            ServeStandard chooseData = standards.get(i);
            if (chooseData.stock == 0) return;
            selectedStandard = chooseData;
            adapter1.selectedPos = i;
            adapter1.notifyDataSetChanged();
            onShowInDetailListener.onShow(chooseData);
            setChooseData(chooseData);
        });
    }

    public void setChooseData(ServeStandard chooseData) {
        GlideUtils.loadRoundCornerImage(getContext(), chooseData.photo, mIcon, 10);
        boardTitle.setText(chooseData.name);
        boardPrice.setText("¥ " + CommonUtil.formatPrice(chooseData.price) + "/" + defaultEntity.unit);
        mRemainNumber.setText(chooseData.stock + "");
    }

    private void initView() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.choose_board, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        leve1View1.setLayoutManager(new AutoLineFeedLayoutManager(true));
        findViewById(R.id.goods_board_add).setOnClickListener(this);
        findViewById(R.id.goods_board_reduce).setOnClickListener(this);
        findViewById(R.id.goods_board_finish).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goods_board_add:
                if (selectedStandard == null) return;
                if (nowNum >= selectedStandard.stock) {
                    ToastUtils.ToastMessage(getContext(), getContext().getString(R.string.not_enough_goods));
                    return;
                }
                nowNum += 1;
                mNumber.setText(nowNum + "");
                break;
            case R.id.goods_board_reduce:
                if (nowNum > 1) {
                    nowNum -= 1;
                    mNumber.setText(nowNum + "");
                }
                break;
            case R.id.goods_board_finish:
                if (defaultEntity.has_specification == 1) {
                    if (selectedStandard == null) {
                        ToastUtils.ToastMessage(getContext(), "请选择规格");
                        return;
                    }
                    if (nowNum > selectedStandard.stock) {
                        ToastUtils.ToastMessage(getContext(), getContext().getString(R.string.not_enough_goods));
                        return;
                    }
                } else {
                    if (nowNum > defaultEntity.stock) {
                        ToastUtils.ToastMessage(getContext(), getContext().getString(R.string.not_enough_goods));
                        return;
                    }
                }
                accountOrder();
                dismiss();
                break;

        }
    }

    private OnShowInDetailListener onShowInDetailListener;

    public interface OnShowInDetailListener {
        void onShow(ServeStandard goodsStandard);
    }

    public void addShowInDetailListener(OnShowInDetailListener onShowInDetailListener) {
        this.onShowInDetailListener = onShowInDetailListener;
    }

    private void accountOrder() {
        defaultEntity.number = nowNum;
        if (selectedStandard != null) defaultEntity.standardId = selectedStandard.id;
        Intent intent = new Intent(getContext(), NewOrderActivity.class);
        intent.putExtra(Constant.SERVE_BEAN, defaultEntity);
        getContext().startActivity(intent);
    }
}
