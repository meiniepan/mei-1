package com.wuyou.user.mvp.block;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.view.fragment.BaseFragment;

import butterknife.BindView;

/**
 * Created by DELL on 2018/9/26.
 */

public class BlockMainFragment extends BaseFragment {
    @BindView(R.id.block_main_search)
    EditText blockSearch;


    @Override
    protected int getContentLayout() {
        return R.layout.fragment_main_block;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        blockSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (blockSearch.length() == 0) return false;
                String searchText = blockSearch.getText().toString().trim();
                doSearch(searchText);
                return true;
            }
            return false;
        });
    }

    private void doSearch(String searchText) {
        if (searchText.length() == 0) return;
        Intent intent = new Intent(mCtx, BlockDetailActivity.class);
        intent.putExtra(Constant.SEARCH_TEXT, searchText);
        startActivity(intent);
    }
}
