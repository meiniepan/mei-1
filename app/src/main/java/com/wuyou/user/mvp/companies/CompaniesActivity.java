/*
 *  Copyright(c) 2017 lizhaotailang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wuyou.user.mvp.companies;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.wuyou.user.R;
import com.wuyou.user.data.remote.Company;
import com.wuyou.user.view.activity.BaseActivity;

import java.util.List;

/**
 * Created by hjn on 2017/2/10.
 */

public class CompaniesActivity extends BaseActivity<CompaniesContract.View, CompaniesContract.Presenter> implements CompaniesContract.View {
    private RecyclerView recyclerView;
    private CompaniesAdapter adapter;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected CompaniesContract.Presenter getPresenter() {
        return new CompaniesPresenter();
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mPresenter.getCompanyInfo();
    }

    @Override
    public void showCompanies(List<Company> list) {

    }

    @Override
    public void showError(String message, int res) {

    }

}
