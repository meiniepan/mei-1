package com.wuyou.user.mvp.block;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.wuyou.user.CarefreeApplication;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.view.fragment.BaseFragment;
import com.wuyou.user.view.widget.lineChart.Axis;
import com.wuyou.user.view.widget.lineChart.AxisValue;
import com.wuyou.user.view.widget.lineChart.ChartUtils;
import com.wuyou.user.view.widget.lineChart.Line;
import com.wuyou.user.view.widget.lineChart.LineChartData;
import com.wuyou.user.view.widget.lineChart.LineChartView;
import com.wuyou.user.view.widget.lineChart.PointValue;
import com.wuyou.user.view.widget.lineChart.Viewport;
import com.wuyou.user.view.widget.lineChart.ZoomType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by DELL on 2018/9/26.
 */

public class BlockMainFragment extends BaseFragment {
    @BindView(R.id.block_main_search)
    EditText blockSearch;
    @BindView(R.id.chart_bottom)
    LineChartView chartTop;
    public final static String[] days = new String[]{"1", "2", "3", "4", "5",};
    private LineChartData lineData;
    int numValues = 5;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (getContext()!=null) {
                generateLineData(getContext().getResources().getColor(R.color.night_blue), 4);
            }
        }
    };

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_main_block;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        CarefreeApplication.getInstance().setThreadIsStop(false);
        blockSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (blockSearch.length() == 0) return false;
                String searchText = blockSearch.getText().toString().trim();
                doSearch(searchText);
                return true;
            }
            return false;
        });
        initChart();
    }

    private void initChart() {
        generateInitialLineData();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!CarefreeApplication.getInstance().getThreadIsStop()) {
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            handler.sendEmptyMessage(2);
                        }
                    }
                }).start();
    }

    private void doSearch(String searchText) {
        if (searchText.length() == 0) return;
        Intent intent = new Intent(mCtx, BlockDetailActivity.class);
        intent.putExtra(Constant.SEARCH_TEXT, searchText);
        startActivity(intent);
    }

    private void generateInitialLineData() {
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<AxisValue> axisValuesY = new ArrayList<AxisValue>();
        List<PointValue> values = new ArrayList<PointValue>();
        int axisYnums = 5;
        for (int i = 0; i < axisYnums; i++) {
            axisValuesY.add(new AxisValue(i).setLabel(i * 5 + ""));
        }
        for (int i = 0; i < numValues; ++i) {
            float y = (float) (Math.random() * 4);
            values.add(new PointValue(i, y));
            axisValues.add(new AxisValue(i).setLabel(days[i]));
        }

        Line line = new Line(values);
        line.setColor(getContext().getResources().getColor(R.color.night_blue)).setCubic(true);

        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        lineData = new LineChartData(lines);
        lineData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        lineData.setAxisYLeft(new Axis(axisValuesY).setHasLines(true).setMaxLabelChars(3));

        chartTop.setLineChartData(lineData);

        // For build-up animation you have to disable viewport recalculation.
        chartTop.setViewportCalculationEnabled(false);

        // And set initial max viewport and current viewport- remember to set viewports after data.
        Viewport v = new Viewport(0, 5, 4, 0);
        chartTop.setMaximumViewport(v);
        chartTop.setCurrentViewport(v);

        chartTop.setZoomType(ZoomType.HORIZONTAL);
    }

    private void generateLineData(int color, float range) {
        // Cancel last animation if not finished.
        chartTop.cancelDataAnimation();

        // Modify data targets
        Line line = lineData.getLines().get(0);// For this example there is always only one line.
        line.setColor(color);
        List<PointValue> values = new ArrayList<PointValue>();
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        values = line.getValues();
        for (int i = 0; i < numValues; ++i) {
            axisValues.add(new AxisValue(i).setLabel(i + ""));
        }
        lineData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
//        values.remove(0);
        float y = (float) (Math.random() * range);
//        values.add(new PointValue())
        for (int i = 0; i < values.size(); i++) {
            if (i < values.size() - 1) {
                values.set(i, new PointValue(values.get(i).getX() + 1, values.get(i + 1).getY()));
            } else {
                values.set(i, new PointValue(values.get(i).getX() + 1, y));
            }
            values.get(i).setTarget(values.get(i).getX(), values.get(i).getY());
        }
        // Start new data animation with 300ms duration;
        chartTop.startDataAnimation(500);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CarefreeApplication.getInstance().setThreadIsStop(true);
    }
}
