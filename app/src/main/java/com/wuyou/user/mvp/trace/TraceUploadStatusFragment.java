package com.wuyou.user.mvp.trace;

import android.os.Bundle;

import com.gs.buluo.common.widget.recyclerHelper.RefreshRecyclerView;
import com.wuyou.user.R;
import com.wuyou.user.adapter.TraceRecordAdapter;
import com.wuyou.user.data.api.TraceRecordEntity;
import com.wuyou.user.data.local.db.TraceIPFSBean;
import com.wuyou.user.data.remote.OrderBean;
import com.wuyou.user.view.activity.BaseActivity;
import com.wuyou.user.view.fragment.BaseFragment;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Solang on 2018/10/22.
 */

public class TraceUploadStatusFragment extends BaseFragment {


    @BindView(R.id.rv_trace_upload_record)
    RefreshRecyclerView recyclerView;
    TraceRecordAdapter adapter;
    List<TraceIPFSBean> data;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_trace_upload_status;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        adapter = new TraceRecordAdapter(R.layout.item_trace_record, data);
        recyclerView.setAdapter(adapter);
        recyclerView.setRefreshAction(this::refreshData);
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            //todo
            if (view.getId() == R.id.order_item_orange) {
                dealWithOrangeButtonClick(position, (OrderBean) adapter.getData().get(position));
            }
        });
        adapter.setOnItemClickListener((adapter, view, position) -> {
            //todo
//            OrderBean bean = (OrderBean) adapter.getData().get(position);
//            Intent intent = new Intent(mCtx, OrderDetailActivity.class);
//            intent.putExtra(Constant.ORDER_ID, bean.order_id);
//            startActivity(intent);
        });
        adapter.setOnLoadMoreListener(() -> getMore(), recyclerView.getRecyclerView());
        adapter.disableLoadMoreIfNotFullPage();
    }

    private void getMore() {
        //todo
    }

    private void dealWithOrangeButtonClick(int position, OrderBean orderBean) {
        //todo
    }

    private void refreshData() {
        //todo
    }


}
