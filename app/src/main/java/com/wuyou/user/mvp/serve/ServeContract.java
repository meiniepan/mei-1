package com.wuyou.user.mvp.serve;

import com.wuyou.user.bean.response.ServeListResponse;
import com.wuyou.user.mvp.BasePresenter;
import com.wuyou.user.mvp.IBaseView;

/**
 * Created by Administrator on 2018\1\29 0029.
 */

public interface ServeContract {
    interface View extends IBaseView {
        void getServeSuccess(ServeListResponse list);

        void loadMore(ServeListResponse data);

        void loadMoreFail(String displayMessage, int code);
    }

    abstract class Presenter extends BasePresenter<View> {
        abstract void getServe(String serveId,int key ,int sort);

        abstract void getServeMore();
    }
}
