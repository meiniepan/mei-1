package com.wuyou.user.mvp.vote;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.gs.buluo.common.network.BaseSubscriber;
import com.gs.buluo.common.network.ErrorBody;
import com.gs.buluo.common.utils.ToastUtils;
import com.wuyou.user.CarefreeDaoSession;
import com.wuyou.user.Constant;
import com.wuyou.user.R;
import com.wuyou.user.data.EoscDataManager;
import com.wuyou.user.data.api.VoteOption;
import com.wuyou.user.util.RxUtil;
import com.wuyou.user.view.activity.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Solang on 2018/10/15.
 */

public class VotePledgeActivity extends BaseActivity {
    @BindView(R.id.tv_vote_pledge_score_num)
    TextView tvVotePledgeScoreNum;
    @BindView(R.id.et_vote_pledge_vote_num)
    EditText etVotePledgeVoteNum;
    private float scoreInt;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_vote_pledge;
    }

    @Override
    protected void bindView(Bundle savedInstanceState) {
        setTitleText(getString(R.string.vote));
        getAccountScore();
    }


    @OnClick(R.id.tv_vote_pledge_confirm)
    public void onViewClicked() {
        int input = Integer.parseInt(etVotePledgeVoteNum.getText().toString());
        if (input > scoreInt || input == 0) {
            ToastUtils.ToastMessage(getCtx(), "请检查输入！");
            return;
        }
        ArrayList<VoteOption> list = (ArrayList<VoteOption>) getIntent().getSerializableExtra(Constant.VOTE_OPT_LIST);
        String id = getIntent().getStringExtra(Constant.VOTE_ID);
        doVote(list, id, input);
    }

    public void getAccountScore() {
        showLoadingDialog();
        EoscDataManager.getIns().getCurrencyBalance(Constant.EOSIO_TOKEN_CONTRACT, CarefreeDaoSession.getInstance().getMainAccount().getName(), "EOS").compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<JsonArray>() {
                    @Override
                    public void onSuccess(JsonArray eosAccountInfo) {
                        if (eosAccountInfo.size() > 0) {
                            String scoreAmount = eosAccountInfo.get(0).toString().replace("EOS", "").replaceAll("\"", "");
                            scoreInt = Float.parseFloat(scoreAmount);
                            tvVotePledgeScoreNum.setText(scoreInt + "");
                            etVotePledgeVoteNum.setText(scoreInt + "");
                            etVotePledgeVoteNum.setSelection(etVotePledgeVoteNum.length());
                            etVotePledgeVoteNum.requestFocus();
                        }
                    }
                });
    }

    private void doVote(ArrayList<VoteOption> list, String id, int scoreNum) {
        showLoadingDialog();
        EoscDataManager.getIns().doVote(id, list, scoreNum)
                .compose(RxUtil.switchSchedulers())
                .subscribe(new BaseSubscriber<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jsonObject) {
                        ToastUtils.ToastMessage(getCtx(), "投票成功！");
                        finish();
                    }


                    @Override
                    protected void onNodeFail(int code, ErrorBody.DetailErrorBean message) {
                        if (message.message.contains("You have voted")) {
                            ToastUtils.ToastMessage(getCtx(), "您已经投过票了");
                        } else {
                            ToastUtils.ToastMessage(getCtx(), message.message);
                        }
                    }
                });
    }
}
