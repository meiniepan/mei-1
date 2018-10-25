package com.wuyou.user.mvp.trace;

import com.wuyou.user.data.local.db.TraceIPFSBean;
import com.wuyou.user.mvp.BasePresenter;
import com.wuyou.user.mvp.IBaseView;

import java.util.List;

/**
 * Created by DELL on 2018/10/24.
 */

public interface TraceContract {

    interface View extends IBaseView {
        void getAuthDataSuccess(List<TraceIPFSBean> data);

        void approveAndExecSuccess(int position);
    }


    abstract class Presenter extends BasePresenter<View> {
        abstract void approveAndExec(int position, TraceIPFSBean data);

        abstract void getData(int position);
    }
}
