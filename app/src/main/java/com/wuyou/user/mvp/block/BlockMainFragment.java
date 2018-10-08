package com.wuyou.user.mvp.block;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.gs.buluo.common.widget.StatusLayout;
import com.wanglu.lib.WPopup;
import com.wanglu.lib.WPopupDirection;
import com.wanglu.lib.WPopupModel;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.data.local.LinePoint;
import com.wuyou.user.view.fragment.BaseFragment;
import com.wuyou.user.view.widget.lineChart.Axis;
import com.wuyou.user.view.widget.lineChart.AxisValue;
import com.wuyou.user.view.widget.lineChart.Line;
import com.wuyou.user.view.widget.lineChart.LineChartData;
import com.wuyou.user.view.widget.lineChart.LineChartOnValueSelectListener;
import com.wuyou.user.view.widget.lineChart.LineChartView;
import com.wuyou.user.view.widget.lineChart.PointValue;
import com.wuyou.user.view.widget.lineChart.Viewport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by DELL on 2018/9/26.
 */

public class BlockMainFragment extends BaseFragment<BlockMainContract.View, BlockMainContract.Presenter> implements BlockMainContract.View {
    @BindView(R.id.block_main_search)
    EditText blockSearch;
    @BindView(R.id.sl_main_block)
    StatusLayout statusLayout;
    LineChartView chartBottom;
    private static LineChartData lineData;
    int numValues = 5;
    float maxY = 4;//Y坐标最大值
    private final float baseMaxY = 4;//Y坐标的最小范围
    @BindView(R.id.tv_main_block_height)
    TextView tvMainBlockHeight;
    @BindView(R.id.tv_main_block_account_num)
    TextView tvMainBlockAccountNum;
    @BindView(R.id.tv_main_block_transaction)
    TextView tvMainBlockTransaction;
    @BindView(R.id.tv_main_block_score_category)
    TextView tvMainBlockScoreCategory;
    private Disposable subscribe;
    private Disposable heighSubscibe;
    List<AxisValue> axisValues = new ArrayList<>();
    private int LAST_POSITION = 4;
    private WPopup wPopup;
    private TextView popTextView;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_main_block;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        statusLayout.showProgressView("数据获取中");
        chartBottom = getActivity().findViewById(R.id.chart_bottom);
        blockSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (blockSearch.length() == 0) return false;
                String searchText = blockSearch.getText().toString().trim();
                doSearch(searchText);
                return true;
            }
            return false;
        });
        mPresenter.getAccountAmount();
        mPresenter.getTransactionsAmount();
        subscribe = Observable.interval(5, TimeUnit.SECONDS).subscribe(aLong -> mPresenter.getTransactionsAmount());
        mPresenter.getBlockHeight();
        mPresenter.getPointTypeAmount();
        mPresenter.getOriginData();
        heighSubscibe = Observable.interval(5, TimeUnit.SECONDS).subscribe(aLong -> mPresenter.getBlockHeight());
    }

    @Override
    protected BlockMainContract.Presenter getPresenter() {
        return new BlockPresenter();
    }


    private void doSearch(String searchText) {
        if (searchText.length() == 0) return;
        Intent intent = new Intent(mCtx, BlockDetailActivity.class);
        intent.putExtra(Constant.SEARCH_TEXT, searchText);
        startActivity(intent);
    }

    private void generateInitialLineData(ArrayList<LinePoint> linePoints) {
        axisValues = new ArrayList<>();
        List<PointValue> values = new ArrayList<>();
        for (int i = 0; i < numValues; ++i) {
            float y = Float.parseFloat(linePoints.get(i).y);
            if (y > maxY) maxY = (float) (y * 1.2);
            values.add(new PointValue(i, Float.parseFloat(linePoints.get(i).y)));
            axisValues.add(new AxisValue(i).setLabel(linePoints.get(i).x));
        }

        Line line = new Line(values);
        line.setColor(0xFF3285FF).setCubic(true);
        List<Line> lines = new ArrayList<>();
        lines.add(line);
        lineData = new LineChartData(lines);
        lineData.setAxisXBottom(new Axis(axisValues));
        lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));

        chartBottom.setLineChartData(lineData);
        // For build-up animation you have to disable viewport recalculation.
        chartBottom.setViewportCalculationEnabled(false);
        // And set initial max viewport and current viewport- remember to set viewports after data.
        Viewport v = new Viewport(0, maxY, 4, 0);
        chartBottom.setMaximumViewport(v);
        chartBottom.setCurrentViewport(v);
        chartBottom.setZoomEnabled(false);
        List<WPopupModel> list = new ArrayList<>();
        list.add(new WPopupModel("", -1, "", -1));//必须的无意义代码
        wPopup = new WPopup.Builder(getActivity())
                .setData(list)
                .setPopupOrientation(WPopup.Builder.VERTICAL)
                .setClickView(chartBottom) // 点击的View，如果是RV/LV，则只需要传入RV/LV
                .create();
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.pop_line_chart, null);
        popTextView = rootView.findViewById(R.id.tv_pop_line_chart);

        chartBottom.setOnValueTouchListener(new LineChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
                popTextView.setText(new String(axisValues.get(pointIndex).getLabelAsChars()) + "\n交易数:" + value.getY());
                if (pointIndex == 0) {
                    popTextView.setBackgroundResource(R.mipmap.bac_pop_left);
                    wPopup.setContentView(rootView);
                    wPopup.showAtFingerLocation(WPopupDirection.RIGHT_TOP);
                } else if (pointIndex == LAST_POSITION) {
                    popTextView.setBackgroundResource(R.mipmap.bac_pop_right);
                    wPopup.setContentView(rootView);
                    wPopup.showAtFingerLocation(WPopupDirection.LEFT_TOP);
                } else {
                    popTextView.setBackgroundResource(R.mipmap.bac_pop_middle);
                    wPopup.setContentView(rootView);
                    wPopup.showAtFingerLocation(WPopupDirection.TOP);

                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (wPopup != null)
                            wPopup.dismiss();
                    }
                }, 2000);
            }

            @Override
            public void onValueDeselected() {

            }
        });
    }

    private void generateLineData(LinePoint linePoint) {
        // Cancel last animation if not finished.
        float curMaxY;//本轮Y坐标的最大值
        chartBottom.cancelDataAnimation();
        List<PointValue> values;
        // Modify data targets
        Line line = lineData.getLines().get(0);// For this example there is always only one line.
        line.setColor(getResources().getColor(R.color.night_blue));
        values = line.getValues();
        //设置X轴刻度
        for (int i = 0; i < axisValues.size() - 1; i++) {
            axisValues.set(i, axisValues.get(i).setLabel(new String(axisValues.get(i + 1).getLabelAsChars())));
        }
        axisValues.set(4, new AxisValue(4).setLabel(linePoint.x));
        lineData.setAxisXBottom(new Axis(axisValues));
        float y = Float.parseFloat(linePoint.y);//给新加入的点赋值
        curMaxY = y;
        for (int i = 0; i < values.size(); i++) {
            if (i < values.size() - 1) {
                float yy = values.get(i + 1).getY();
                if (yy > curMaxY) curMaxY = yy;//为本轮maxY赋值
                values.set(i, new PointValue(values.get(i).getX() + 1, yy));//把后一个点的值赋给当前点
            } else {
                values.set(i, new PointValue(values.get(i).getX() + 1, y));//为新加入的点赋值
            }
            values.get(i).setTarget(values.get(i).getX(), values.get(i).getY());
        }
        resetMaxY(curMaxY, y);
        // Start new data animation with 500ms duration;
        Viewport v = new Viewport(0, maxY, 4, 0);
        chartBottom.setMaximumViewport(v);
        chartBottom.setCurrentViewport(v);
        chartBottom.startDataAnimation(500);
    }

    private void resetMaxY(float curMaxY, float y) {
        //如果出现新的最大值，则将maxY调整为新的最大值的1.2倍。如果本轮最大值比上次最大值的0.6倍小，则缩小最大值0.8倍。
        if (y > maxY) maxY = (float) (y * 1.2);
        if (curMaxY < maxY * 0.6) maxY = (float) (maxY * 0.8);
        if (maxY < baseMaxY) maxY = baseMaxY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscribe != null) subscribe.dispose();
        if (heighSubscibe != null) heighSubscibe.dispose();
    }

    @Override
    public void getBlockHeightSuccess(String height) {
        tvMainBlockHeight.setText(height);
    }

    @Override
    public void getTransactionsAmountSuccess(String amount) {
        tvMainBlockTransaction.setText(amount);
    }

    @Override
    public void getAccountAmountSuccess(String amount) {
        tvMainBlockAccountNum.setText(amount);
    }

    @Override
    public void getPointTypeAmountSuccess(String amount) {
        tvMainBlockScoreCategory.setText(amount);
    }

    @Override
    public void getOriginDataSuccess(ArrayList<LinePoint> linePoints) {
        statusLayout.showContentView();
        subscribe = Observable.interval(5, TimeUnit.SECONDS).subscribe(aLong -> mPresenter.getLastFiveSecondsData());
        generateInitialLineData(linePoints);
    }

    @Override
    public void getLastFiveSecondsDataSuccess(LinePoint linePoint) {
        generateLineData(linePoint);
    }
}
