package com.wuyou.user.mvp.volunteer;

import com.wuyou.user.data.api.VolunteerProjectBean;
import com.wuyou.user.mvp.BasePresenter;
import com.wuyou.user.mvp.IBaseView;

import java.util.List;

/**
 * Created by DELL on 2018/10/29.
 */

public interface TimeBankRecordContract {
    interface View extends IBaseView {
        void registerSuccess(int position);

        void rewardSuccess(int position);

        void getAttendingDataSuccess(List<VolunteerProjectBean> data);

        void getFinishAttendDataSuccess(List<VolunteerProjectBean> data);
    }

    abstract class Presenter extends BasePresenter<View> {
        abstract void registerProject(int position ,VolunteerProjectBean bean);

        abstract void rewardProject(int position,VolunteerProjectBean bean);

        abstract void getRecordData();

    }
}
