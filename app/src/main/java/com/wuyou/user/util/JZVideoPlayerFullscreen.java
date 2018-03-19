package com.wuyou.user.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wuyou.user.R;
import com.wuyou.user.view.widget.panel.ShareBottomBoard;

import cn.jzvd.JZMediaManager;
import cn.jzvd.JZUserAction;
import cn.jzvd.JZUserActionStandard;
import cn.jzvd.JZUtils;
import cn.jzvd.JZVideoPlayerManager;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * 全屏状态播放完成，不退出全屏
 * Created by Nathen on 2016/11/26.
 */
public class JZVideoPlayerFullscreen extends JZVideoPlayerStandard {
    Context context;
    private ImageView upvoteView;
    private TextView soundView;
    private TextView title;
    private ImageView shareView;
    private boolean quite = false;
    private boolean liked = false;

    public JZVideoPlayerFullscreen(Context context) {
        super(context);
        this.context = context;
    }

    public JZVideoPlayerFullscreen(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.home_player;
    }

    @Override
    public void init(Context context) {
        super.init(context);
        upvoteView = findViewById(R.id.player_upvote);
        soundView = findViewById(R.id.player_sound);
        shareView = findViewById(R.id.player_share);
        title = findViewById(R.id.title);
        soundView.setOnClickListener(this);
        shareView.setOnClickListener(this);
        setLikeState();
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setFullState();
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setSmallState();
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == cn.jzvd.R.id.start) {
            Log.i(TAG, "onClick start [" + this.hashCode() + "] ");
            if (dataSourceObjects == null || JZUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex) == null) {
                Toast.makeText(getContext(), getResources().getString(cn.jzvd.R.string.no_url), Toast.LENGTH_SHORT).show();
                return;
            }
            if (currentState == CURRENT_STATE_NORMAL) {
                if (!JZUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex).toString().startsWith("file") && !
                        JZUtils.getCurrentFromDataSource(dataSourceObjects, currentUrlMapIndex).toString().startsWith("/") &&
                        !JZUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {
                    showWifiDialog();
                    return;
                }
                startVideo();
                onEvent(JZUserAction.ON_CLICK_START_ICON);
                startWindowFullscreen();
            } else if (currentState == CURRENT_STATE_PLAYING) {
                onEvent(JZUserAction.ON_CLICK_PAUSE);
                Log.d(TAG, "pauseVideo [" + this.hashCode() + "] ");
                JZMediaManager.pause();
                onStatePause();
            } else if (currentState == CURRENT_STATE_PAUSE) {
                onEvent(JZUserAction.ON_CLICK_RESUME);
                JZMediaManager.start();
                onStatePlaying();
                if (currentScreen != SCREEN_WINDOW_FULLSCREEN) {
                    startWindowFullscreen();
                }

            } else if (currentState == CURRENT_STATE_AUTO_COMPLETE) {
                onEvent(JZUserAction.ON_CLICK_START_AUTO_COMPLETE);
                startVideo();
                startWindowFullscreen();
            }
        } else if (i == cn.jzvd.R.id.fullscreen) {
            Log.i(TAG, "onClick fullscreen [" + this.hashCode() + "] ");
            if (currentState == CURRENT_STATE_AUTO_COMPLETE) return;
            if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
                //quit fullscreen
                backPress();
            } else {
                Log.d(TAG, "toFullscreenActivity [" + this.hashCode() + "] ");
                onEvent(JZUserAction.ON_ENTER_FULLSCREEN);
                startWindowFullscreen();
            }
        } else if (i == R.id.player_sound) {
            if (quite) {
                setVolume(1f);
                quite = false;
            } else {
                setVolume(0f);
                quite = true;
            }
        } else if (i == R.id.player_upvote) {
            liked = !liked;
            setLikeState();
        } else if (i == R.id.player_share) {
            doShare();
        }
    }

    private void doShare() {
        ShareBottomBoard bottomBoard = new ShareBottomBoard(context);
        bottomBoard.show();
    }

    private void setLikeState() {
        if (liked) {
//            upvoteView.setImageResource(R.mipmap.video_liked);
        } else {
//            upvoteView.setImageResource(R.mipmap.video_like);
        }

    }

    @Override
    public void startWindowFullscreen() {
        super.startWindowFullscreen();
        setFullState();
    }

    @Override
    public void onPrepared() {
        super.onPrepared();
        if (quite) {
            setVolume(0f);
        } else {
            setVolume(1f);
        }
    }

    private void setVolume(float v) {
        JZMediaManager.instance().jzMediaInterface.setVolume(v, v);
    }

    @Override
    public void playOnThisJzvd() {
        //退出全屏和小窗的方法
        Log.i(TAG, "playOnThisJzvd " + " [" + this.hashCode() + "] ");
        //1.清空全屏和小窗的jzvd.

        currentState = JZVideoPlayerManager.getSecondFloor().currentState;
        currentUrlMapIndex = JZVideoPlayerManager.getSecondFloor().currentUrlMapIndex;
        clearFloatScreen();
        //2.在本jzvd上播放,推出全屏要暂停
        if (currentState == CURRENT_STATE_PLAYING) {
            JZMediaManager.pause();
            onStatePause();
        }
        setState(currentState);
//        removeTextureView();
        addTextureView();
        setSmallState();
    }

    @Override
    public void showWifiDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getResources().getString(R.string.tips_not_wifi));
        builder.setPositiveButton(getResources().getString(R.string.tips_not_wifi_confirm), (dialog, which) -> {
            dialog.dismiss();
            onEvent(JZUserActionStandard.ON_CLICK_START_WIFIDIALOG);
            startVideo();
            WIFI_TIP_DIALOG_SHOWED = true;
            if (currentScreen != SCREEN_WINDOW_FULLSCREEN) {
                startWindowFullscreen();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.tips_not_wifi_cancel), (dialog, which) -> {
            dialog.dismiss();
            clearFloatScreen();
        });
        builder.setOnCancelListener(DialogInterface::dismiss);
        builder.create().show();
    }

    private void setSmallState() {
        upvoteView.setVisibility(GONE);
        soundView.setVisibility(GONE);
        shareView.setVisibility(GONE);
        title.setVisibility(GONE);
    }

    private void setFullState() {
        upvoteView.setVisibility(VISIBLE);
        soundView.setVisibility(VISIBLE);
        title.setVisibility(VISIBLE);
        shareView.setVisibility(VISIBLE);
    }
}
