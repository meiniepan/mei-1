package com.wuyou.user.mvp;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by hjn on 2016/11/3.
 */
public abstract class BasePresenter<T extends IBaseView> {
    public T mView;

    public void attach(T mView) {
        this.mView = mView;
    }

    public void detachView() {
        if (null != compositeDisposable) {
            compositeDisposable.clear();
        }
        if (mView != null) {
            mView = null;
        }
    }

    public boolean isAttach() {
        return mView != null;
    }

    private CompositeDisposable compositeDisposable;

    protected void addDisposable(Disposable disposable) {
        if (null == compositeDisposable) {
            compositeDisposable = new CompositeDisposable();
        }
        if (compositeDisposable.isDisposed()) {
            compositeDisposable.add(disposable);
        }
    }
}