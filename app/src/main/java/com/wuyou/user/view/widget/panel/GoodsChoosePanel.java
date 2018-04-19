package com.wuyou.user.view.widget.panel;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.wuyou.user.adapter.GoodsLevel1Adapter2;
import com.wuyou.user.bean.GoodsStandard;
import com.wuyou.user.bean.ServeDetailBean;
import com.wuyou.user.mvp.serve.NewOrderActivity;
import com.wuyou.user.util.glide.GlideUtils;
import com.wuyou.user.util.layoutmanager.AutoLineFeedLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hjn on 2016/11/17.
 */
public class GoodsChoosePanel extends Dialog implements View.OnClickListener, DialogInterface.OnDismissListener {
    private OnShowInDetail onShowInDetail;
    @BindView(R.id.goods_level1)
    RecyclerView leve1View1;
    @BindView(R.id.goods_level2)
    RecyclerView leve1View2;

    @BindView(R.id.goods_board_number)
    TextView mNumber;
    @BindView(R.id.goods_board_choose_remain)
    TextView mRemainNumber;
    @BindView(R.id.goods_choose_icon)
    ImageView mIcon;
    @BindView(R.id.goods_standard_type2)
    TextView type2;
    @BindView(R.id.goods_standard_type1)
    TextView type1;
    @BindView(R.id.choose_board_price)
    TextView boardPrice;
    @BindView(R.id.choose_board_title)
    TextView boardTitle;

    Context mCtx;

    private int nowNum = 1;
    private String leve11Key;
    private String level2Key;
    private ServeDetailBean defaultEntity;
    private GoodsLevel1Adapter2 adapter2;
    private GoodsLevel1Adapter1 adapter1;
    private View car;
    //    private View buy;
    private View finish;
    private OnSelectFinish selectFinish;
    private AddCartListener addCartListener;
    private String originId;
    private int type = 0;  // 1 加入购物车 ，2.立即购买
    private View buyView;
    private View limitSaleView;

    public GoodsChoosePanel(Context context, OnShowInDetail onShowInDetail) {
        super(context, R.style.my_dialog);
        this.onShowInDetail = onShowInDetail;
        mCtx = context;
        initView();
    }

    public void setData(ServeDetailBean entity) {
        defaultEntity = entity;
//        if (entity == null) {   //一级都没有
//            findViewById(R.id.goods_board_repertory).setVisibility(View.GONE);
//            findViewById(R.id.empty).setVisibility(View.VISIBLE);
//        } else if (entity.descriptions.secondary == null) {    //只有一级
//            setLevelOneData(entity);
//            findViewById(R.id.level2_view).setVisibility(View.GONE);
//        } else {  //两级
//            setLevelTwoData(entity);
//        }
        GlideUtils.loadRoundCornerImage(mCtx, defaultEntity.photo, mIcon);
        boardTitle.setText(defaultEntity.title);
        boardPrice.setText(defaultEntity.price == 0 ? "免费" : "¥ " + defaultEntity.price);
        setRepertoryGoodsData(entity);
    }

    public void setRepertoryGoodsData(ServeDetailBean goodsDetail) {
        defaultEntity = goodsDetail;
        if (defaultEntity == null) return;
        originId = defaultEntity.id;

        GlideUtils.loadRoundCornerImage(mCtx, defaultEntity.photo, mIcon, 10);
        boardTitle.setText(goodsDetail.title);
        boardPrice.setText(defaultEntity.price == 0 ? "免费" : "¥ " + defaultEntity.price);
        mRemainNumber.setText(defaultEntity.recorded + "");
    }

    private void setLevelOneData(GoodsStandard entity) {
//        GoodsStandard.GoodsType type = entity.descriptions.primary;
//        type1.setText(type.label);
//        final GoodsLevel1Adapter1 adapter1 = new GoodsLevel1Adapter1(mCtx, type.types);
//        leve1View1.setAdapter(adapter1);
//
//        ArrayList<String> unClickList = new ArrayList<>();
//        final Map<String, goodb> goodsMap = entity.goodsIndexes;
//        for (String s : type.types) {
//            if (goodsMap.get(s) == null) {
//                unClickList.add(s);
//            }
//        }
//        adapter1.setUnClickableList(unClickList);
//        adapter1.setOnLevelClickListener(new GoodsLevel1Adapter1.OnLevelClickListener() {
//            @Override
//            public void onClick(String s) {
//                mKind.setText(s);
//                leve11Key = s;
//                defaultEntity = goodsMap.get(leve11Key);
//                if (TextUtils.isEmpty(s) || defaultEntity == null) return;
//                setRepertoryGoodsData(defaultEntity);
//            }
//        });
    }

    private void setLevelTwoData(GoodsStandard entity) {
//        final GoodsStandard.GoodsType t1 = entity.descriptions.primary;
//        type1.setText(t1.label);
//        adapter1 = new GoodsLevel1Adapter1(mCtx, t1.types);
//        leve1View1.setAdapter(adapter1);
//        final GoodsStandard.GoodsType t2 = entity.descriptions.secondary;
//        type2.setText(t2.label);
//        adapter2 = new GoodsLevel1Adapter2(mCtx, t2.types);
//        leve1View2.setAdapter(adapter2);
//        final Map<String, ListGoodsDetail> goodsMap = entity.goodsIndexes;
//        adapter1.setOnLevelClickListener(new GoodsLevel1Adapter1.OnLevelClickListener() {
//            @Override
//            public void onClick(String s) {
//                mKind.setText(s);
//                leve11Key = s;
//                changeSelectAdapter2(goodsMap, adapter2, t2.types, s);
//
//            }
//        });
//        adapter2.setOnLevelClickListener(new GoodsLevel1Adapter2.OnLevelClickListener() {
//            @Override
//            public void onClick(String s) {
//                level2Key = s;
//                changeSelectAdapter1(goodsMap, adapter1, t1.types, s);
//            }
//        });
    }

    //点击第一级，改变第二级的状态
    private void changeSelectAdapter2(Map<String, ServeDetailBean> goodsIndexes, GoodsLevel1Adapter2 adapter2, List<String> types, String key2) {
        ArrayList<String> unClickTypeList = new ArrayList<>();
        if (TextUtils.isEmpty(key2)) {
            defaultEntity = null;
            adapter2.setUnClickableList(unClickTypeList);
            return;
        }
        for (String s : types) {
            if (goodsIndexes.get(leve11Key + "^" + s) == null) {
                unClickTypeList.add(s);
            } else {
                unClickTypeList.remove(s);
            }
        }
        adapter2.setUnClickableList(unClickTypeList);
        defaultEntity = goodsIndexes.get(leve11Key + "^" + level2Key);
        setRepertoryGoodsData(defaultEntity);
    }

    private void changeSelectAdapter1(Map<String, ServeDetailBean> goodsIndexes, GoodsLevel1Adapter1 adapter1, List<String> types, String key1) {
        ArrayList<String> unClickTypeList = new ArrayList<>();
        if (TextUtils.isEmpty(key1)) {
            defaultEntity = null;
            adapter1.setUnClickableList(unClickTypeList);
            return;
        }
        for (String s : types) {
            if (goodsIndexes.get(s + "^" + level2Key) == null) {
                unClickTypeList.add(s);
            } else {
                unClickTypeList.remove(s);
            }
        }
        adapter1.setUnClickableList(unClickTypeList);

        defaultEntity = goodsIndexes.get(leve11Key + "^" + level2Key);
        setRepertoryGoodsData(defaultEntity);
    }

    private void initView() {
        View rootView = LayoutInflater.from(mCtx).inflate(R.layout.choose_board, null);
        setContentView(rootView);
        setOnDismissListener(this);
        ButterKnife.bind(this);

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        leve1View1.setLayoutManager(new AutoLineFeedLayoutManager(true));
        leve1View2.setLayoutManager(new AutoLineFeedLayoutManager(true));
        findViewById(R.id.goods_board_add).setOnClickListener(this);
        findViewById(R.id.goods_board_reduce).setOnClickListener(this);
        finish = findViewById(R.id.goods_board_finish);
        finish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goods_board_add:
                if (defaultEntity == null) return;
//                if (nowNum >= defaultEntity.recorded) {
//                    ToastUtils.ToastMessage(mCtx, mCtx.getString(R.string.not_enough_goods));
//                    return;
//                }
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
                if (defaultEntity == null) {
                    ToastUtils.ToastMessage(mCtx, "请选择商品");
                    return;
                }
                accountOrder();
                dismiss();
                break;

        }
    }

    private void addCartItem() {
        addCartListener.onAddCart(defaultEntity.id, nowNum);
    }

    public void setFromShoppingCar(OnSelectFinish onSelectedFinished) {
        selectFinish = onSelectedFinished;
        buyView.setVisibility(View.GONE);
        car.setVisibility(View.GONE);
        finish.setVisibility(View.VISIBLE);
    }

    public void setAddCartListener(AddCartListener addCartListener) {
        this.addCartListener = addCartListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
//        if (onShowInDetail == null || defaultEntity == null || TextUtils.equals(defaultEntity.id, originId))
//            return;
//        onShowInDetail.onShow(defaultEntity, nowNum);
    }

    public void setAmount(int amount) {
        mNumber.setText(amount + "");
        nowNum = amount;
    }

    public interface OnSelectFinish {
        void onSelected(String newId, int nowNum);
    }

    public interface AddCartListener {
        void onAddCart(String id, int nowNum);
    }

    public interface OnShowInDetail {
        void onShow(ServeDetailBean goodsDetail, int num);
    }

    private void accountOrder() {
        defaultEntity.number = nowNum;
        Intent intent = new Intent(getContext(), NewOrderActivity.class);
        intent.putExtra(Constant.SERVE_BEAN, defaultEntity);
        mCtx.startActivity(intent);
    }
}
