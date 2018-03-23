package com.gnway.bangwoba.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;


import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.gnway.bangwo8sdk.Bangwo8OpenHelper;
import com.gnway.bangwo8sdk.listener.Bangwo8ChatConnectListener;
import com.gnway.bangwo8sdk.listener.Bangwo8FileSendListener;
import com.gnway.bangwo8sdk.listener.Bangwo8IsFinishServiceListener;
import com.gnway.bangwo8sdk.listener.Bangwo8IsServicedListener;
import com.gnway.bangwo8sdk.listener.Bangwo8ResponseListener;
import com.gnway.bangwo8sdk.Bangwo8SdkManager;

import com.gnway.bangwo8sdk.bean.ChatMessage;
import com.gnway.bangwoba.R;
import com.gnway.bangwoba.bean.ConnectState;
import com.gnway.bangwoba.bean.FragmentLoadFinish;
import com.gnway.bangwoba.bean.NewMsgCome;
import com.gnway.bangwoba.bean.OnSdkResponse;
import com.gnway.bangwoba.bean.RecentChatListItem;
import com.gnway.bangwoba.adapter.ChatAdapter;
import com.gnway.bangwoba.bean.UpFileSend;
import com.gnway.bangwoba.callback.OnChatItemClickListener;
import com.gnway.bangwoba.global.Variable;
import com.gnway.bangwoba.utils.FileUtil;
import com.gnway.bangwoba.utils.ImageUtil;
import com.gnway.bangwoba.utils.MediaManager;

import com.gnway.bangwoba.widgets.ChatInputFragment;
import com.gnway.bangwoba.widgets.KeyboardDetectorRelativeLayout;
import com.gnway.bangwoba.widgets.tools.DImenUtil;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;


public class ChatActivity extends AppCompatActivity implements View.OnClickListener, OnChatItemClickListener, OnRefreshListener,
        KeyboardDetectorRelativeLayout.OnSoftKeyboardListener, ChatInputFragment.InputChatListener, View.OnTouchListener,
        Bangwo8FileSendListener, Bangwo8IsServicedListener, Bangwo8IsFinishServiceListener, Bangwo8ChatConnectListener {
    private ArrayList<ChatMessage> messageList;
    private SmartRefreshLayout refresh;
    private String chatWithJid;
    private int dbCurrentTotalCount;
    private int limitCount;
    private RecyclerView recycle;
    private ChatAdapter chatAdapter;
    private int isFinish;
    private int currentSelectPosition;
    private ArrayList<ChatMessage> reverseMessageList;
    private ChatInputFragment inputChat;
    private int chatType;
    private AlertDialog searchServiceDialog;
    private AlertDialog leaveMessageDialog;
    private AlertDialog evaluateDialog;
    private String agentId;
    private LinearLayout normalEvaluate;
    private LinearLayout satisfyEvaluate;
    private LinearLayout unsatisfyEvaluate;
    private Object serviceState;
    private SharedPreferences.Editor editor;
    private LinearLayout connectStateLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        EventBus.getDefault().register(this);
        initViewAndData();
        Bangwo8SdkManager.getInstance().addBangwo8ChatConnectListener(this);
        //添加文件发送成功或失败的监听器
        Bangwo8SdkManager.getInstance().addBangwo8FileSendListener(this);
        //添加是否被客服成功接待的监听器
        Bangwo8SdkManager.getInstance().addBangwo8IsServicedListener(this);
        //添加客服结束会话的监听器
        Bangwo8SdkManager.getInstance().addBangwo8IsFinishServiceListener(this);
    }

    //当界面的fragment完全绑定到activity上并且结束服务时会触发此方法
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMessage(FragmentLoadFinish msg) {
        //禁用所有发送功能
        disableAllSend();
    }

    private void initViewAndData() {
//        chatWithJid = getIntent().getStringExtra("chatWithJid");
        chatWithJid = Variable.loginUser;
//        agentId = getIntent().getStringExtra("agentId");
        agentId = Variable.AgentId;
        isFinish = getIntent().getIntExtra("isFinish", RecentChatListItem.NOT_FINISH_SERVICE);
        boolean isRequestHistory = getIntent().getBooleanExtra("isRequestHistory", false);
        chatType = getIntent().getIntExtra("chatType", RecentChatListItem.SINGLE_CHAT);
        String chatId = getIntent().getStringExtra("chatId");

        KeyboardDetectorRelativeLayout rootView = (KeyboardDetectorRelativeLayout) findViewById(R.id.chat_roots);
        connectStateLayout = (LinearLayout) findViewById(R.id.connect_state);
        TextView title = (TextView) findViewById(R.id.tv_title);
        recycle = (RecyclerView) findViewById(R.id.recylerView);
        refresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        Button back = (Button) findViewById(R.id.chat_back);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        inputChat = new ChatInputFragment();
        inputChat.setIsFinish(isFinish);
        //向activity绑定fragment是异步的,写在前面不见得就先执行完
        transaction.replace(R.id.bottom_input, inputChat).commit();
        back.setOnClickListener(this);
        inputChat.setInputChatListener(this);
        rootView.setOnSoftKeyboardListener(this);
        recycle.setOnTouchListener(this);
        refresh.setRefreshHeader(new WaterDropHeader(this));
        initPopUpWindow();
        title.setText(chatWithJid);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        recycle.setLayoutManager(layoutManager);
        messageList = new ArrayList<>();
        reverseMessageList = new ArrayList<>();

        //创建与某人的聊天表
        Bangwo8SdkManager.getInstance().createChatTable(this, chatWithJid);

        //每当activity重新创建的时候就从数据库中读取聊天记录
        readDataFromDbAddToMessageList("[" + chatWithJid + "]");

        chatAdapter = new ChatAdapter(messageList, this);
        chatAdapter.setOnChatItemClickListener(this);

        //封装一个下拉刷新，以便刷新聊天记录,设置下拉刷新的监听器
        refresh.setOnRefreshListener(this);
        recycle.setAdapter(chatAdapter);
        recycle.scrollToPosition(0);
        if(Bangwo8SdkManager.getInstance().checkIsConnect()){
            connectStateLayout.setVisibility(View.GONE);
        }else {
            connectStateLayout.setVisibility(View.VISIBLE);
        }
        //initServiceState();
    }

    private void initServiceState() {
        SharedPreferences sp = getSharedPreferences("serviceState", Context.MODE_PRIVATE);
        editor = sp.edit();
        isServiced = sp.getBoolean("isServiced", false);
    }


    private void disableAllSend() {
        inputChat.voiceBtn.setClickable(false);
        inputChat.emojiBtn.setClickable(false);
        inputChat.moreFuntion.setClickable(false);
        inputChat.keyBoardBtnChangeByVoice.setClickable(false);
        inputChat.keyBoardBtnChangeByEmoji.setClickable(false);
        inputChat.keyBoardChangeByMoreFuntion.setClickable(false);
        inputChat.editText.setClickable(false);
        inputChat.editText.setFocusable(false);
        if (inputChat.isBoxShow()) {
            inputChat.hideAll();
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        if (canRefresh) {
            reverseMessageList.clear();
            readMoreDataFromDbAddToMessageList("[" + chatWithJid + "]");
            Collections.reverse(reverseMessageList);
            messageList.addAll(reverseMessageList);
            chatAdapter.notifyDataSetChanged();
        }
        //查询出来了之后停止刷新
        refresh.finishRefresh();
    }

    private boolean canRefresh = true;

    private void readDataFromDbAddToMessageList(String dbName) {
        Bangwo8OpenHelper openHelper = Bangwo8OpenHelper.getInstance(this);
        SQLiteDatabase db = openHelper.getWritableDatabase();
        Cursor cursor = db.query(dbName, null, null, null, null, null, null);
        dbCurrentTotalCount = cursor.getCount();
        limitCount = dbCurrentTotalCount - 8;
        Cursor cursorLimit = null;
        if (limitCount < 0) {
            //证明数据库的消息数量小于8条，就全查询出来
            canRefresh = false;
            cursorLimit = db.query(dbName, null, null, null, null, null, null);
        } else {
            //证明数据库的消息数量大于8条，只查询8条出来
            cursorLimit = db.query(dbName, null, null, null, null, null, null, limitCount + ",8");
        }
        if (cursorLimit != null) {
            while (cursorLimit.moveToNext()) {
                ChatMessage chatMessage = addToChatMessage(cursorLimit);
                messageList.add(chatMessage);
            }
            // 释放资源
            cursor.close();
            cursorLimit.close();
            db.close();
            Collections.reverse(messageList);
        }
    }

    private void readMoreDataFromDbAddToMessageList(String titleName1) {
        Bangwo8OpenHelper openHelper = Bangwo8OpenHelper.getInstance(this);
        SQLiteDatabase database = openHelper.getWritableDatabase();
        int queNumber = 10;
        limitCount = limitCount - 10;
        if (limitCount < 0) {
            queNumber = limitCount + 10;
            limitCount = 0;
            canRefresh = false;
        }
        //刷新的话每次展示多一点，展示十条，想看更多的聊天记录就下拉刷新
        Cursor cursorLimit = database.query(titleName1, null, null, null, null, null, null, limitCount + "," + queNumber);
        if (cursorLimit != null) {
            while (cursorLimit.moveToNext()) {
                ChatMessage chatMessage = addToChatMessage(cursorLimit);
                reverseMessageList.add(chatMessage);
            }
            // 释放资源
            cursorLimit.close();
            database.close();
        }
    }

    private ChatMessage addToChatMessage(Cursor cursorLimit) {
        //String id = cursor.getString(0);
        String fromJid = cursorLimit.getString(1);
        String message = cursorLimit.getString(2);
        long time = cursorLimit.getLong(3);
        int messageType = cursorLimit.getInt(4);
        String filePath = cursorLimit.getString(5);
        Float voiceTime = cursorLimit.getFloat(6);
        String fileName = cursorLimit.getString(7);
        String fileSize = cursorLimit.getString(8);
        String fileUrl = cursorLimit.getString(9);
        int fileLoadState = cursorLimit.getInt(10);
        int audioReadState = cursorLimit.getInt(11);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatWithJid(fromJid);
        chatMessage.setMessage(message);
        chatMessage.setTime(time);
        chatMessage.setMessageType(messageType);
        chatMessage.setFilepath(filePath);
        chatMessage.setVoiceTime(voiceTime);
        chatMessage.setFileName(fileName);
        chatMessage.setFileSize(fileSize);
        chatMessage.setDownloadurl(fileUrl);
        chatMessage.setFileLoadState(fileLoadState);
        chatMessage.setAudioReadState(audioReadState);
        return chatMessage;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.chat_back) {
            if(isServiced && evaluateDialog == null){
                //如果被服务了，退出activity的时候才弹出评价界面
                showEvaluateDialog();
                //结束会话
                Bangwo8SdkManager.getInstance().finishSession(chatWithJid, chatWithJid);
            }else {
                finish();
                overridePendingTransition(R.anim.exit_in, R.anim.exit_out);
            }
        } else if (v.getId() == R.id.copy_message) {
            String copyMessage = messageList.get(currentSelectPosition).getMessage();
            //获取剪贴板管理器：
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", copyMessage);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            popWindow.dismiss();
            Toast.makeText(this, "复制成功", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.cancel_leave_message) {
            closeLeaveMessageDialog();
        } else if (v.getId() == R.id.leave_message) {
            //开启留言
            Intent intent=new Intent();
            intent.setClass(this,Leaving_message.class);
            startActivity(intent);
            finish();
        } else if (v.getId() == R.id.evaluate_normal) {
            normalEvaluate.setBackgroundResource(R.drawable.evaluate_press_shape);
            satisfyEvaluate.setBackgroundResource(R.drawable.evaluate_select_shape);
            unsatisfyEvaluate.setBackgroundResource(R.drawable.evaluate_select_shape);
        } else if (v.getId() == R.id.evaluate_satisfy) {
            normalEvaluate.setBackgroundResource(R.drawable.evaluate_select_shape);
            satisfyEvaluate.setBackgroundResource(R.drawable.evaluate_press_shape);
            unsatisfyEvaluate.setBackgroundResource(R.drawable.evaluate_select_shape);
        } else if (v.getId() == R.id.evaluate_unsatisfy) {
            normalEvaluate.setBackgroundResource(R.drawable.evaluate_select_shape);
            satisfyEvaluate.setBackgroundResource(R.drawable.evaluate_select_shape);
            unsatisfyEvaluate.setBackgroundResource(R.drawable.evaluate_press_shape);
        } else if (v.getId() == R.id.commit_evaluate) {
            //提交评价
            closeEvaluateDialog();
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaManager.pause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        closeSearchDialog();
        closeLeaveMessageDialog();
        if (inputChat.isBoxShow()) {
            inputChat.hideAll();
        } else if (isServiced && evaluateDialog == null) {
            //如果被服务了，退出activity的时候才弹出评价界面
            showEvaluateDialog();
            //结束会话
            Bangwo8SdkManager.getInstance().finishSession(chatWithJid, chatWithJid);
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.exit_in, R.anim.exit_out);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaManager.resume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除监听器，否则会收到多条重复消息
//        Bangwo8XmppManager.getInstance().removeMessageReceiveListener();
        EventBus.getDefault().unregister(this);
        MediaManager.release();
    }

    private int prePosition = -1;

    @Override
    public void onChatItemClick(final int position, View view) {
        if (view.getId() == R.id.image_content) {
            String imagePath = messageList.get(position).getFilepath();
            Intent intent = new Intent(this, PhotoLookActivity.class);
            intent.putExtra("path", imagePath);

            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(this, view, "image");
            ActivityCompat.startActivity(this, intent, options.toBundle());
        } else if (view.getId() == R.id.audio_item) {
            if (prePosition != -1) {
                messageList.get(prePosition).setRecordPlayState(ChatMessage.RECORD_PLAY_COMPLETE);
            }
            prePosition = position;
            messageList.get(position).setRecordPlayState(ChatMessage.RECORD_PLAYING);
            messageList.get(position).setAudioReadState(ChatMessage.AUDIO_READ);
            chatAdapter.setData(messageList);
            chatAdapter.notifyDataSetChanged();
            MediaManager.playSound(messageList.get(position).getFilepath(),
                    new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            messageList.get(position).setRecordPlayState(ChatMessage.RECORD_PLAY_COMPLETE);
                            chatAdapter.setData(messageList);
                            chatAdapter.notifyDataSetChanged();
                        }
                    });
            int dbId = dbCurrentTotalCount - position;
//            DbHelper.upChatMessageAudioReadStateToDb(this,dbId, chatWithJid,ChatMessage.AUDIO_READ);
        }
    }

    private PopupWindow popWindow;

    private void initPopUpWindow() {
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout popWindowView = (LinearLayout) inflater.inflate(R.layout.delete_or_resend, null);

        TextView deleteMessage = (TextView) popWindowView.findViewById(R.id.delete_message);
        TextView resendMessage = (TextView) popWindowView.findViewById(R.id.resend_message);
        TextView copyMessage = (TextView) popWindowView.findViewById(R.id.copy_message);
        TextView sendToOther = (TextView) popWindowView.findViewById(R.id.send_to_other);
        copyMessage.setOnClickListener(this);
        sendToOther.setOnClickListener(this);
        deleteMessage.setOnClickListener(this);
        resendMessage.setOnClickListener(this);
        //int width = DImenUtil.dip2px(this, 233);
        int height = DImenUtil.dip2px(this, 43);
        popWindow = new PopupWindow(popWindowView, ViewGroup.LayoutParams.WRAP_CONTENT, height, false);
        // 需要设置一下此参数，点击外边可消失
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //只有把恢复透明度的代码写在这里，才会执行
                WindowManager.LayoutParams attribute = getWindow().getAttributes();
                attribute.alpha = 1f;
                getWindow().setAttributes(attribute);
            }
        });
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置点击窗口外边窗口消失
        popWindow.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        popWindow.setFocusable(true);
    }

    @Override
    public void onChatItemLongClick(int position, View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        //要想此代码生效,popupWindow必须有具体高度 new PopupWindow的时候指定高度
        popWindow.showAtLocation(view, Gravity.NO_GRAVITY,
                location[0], location[1] - popWindow.getHeight() - 20);

        currentSelectPosition = position;
    }

    @Override
    public void onShown(int keyboardHeight) {
        inputChat.onKeyboardShow(keyboardHeight);
    }

    @Override
    public void onHidden() {
        inputChat.onKeyboardDismiss();
    }

    @Override
    public void onMeasureFinished() {
    }

    private void showSearchDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.search_servicer_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        searchServiceDialog = builder.setView(view).show();
        Window dialogWindow = searchServiceDialog.getWindow();
        WindowManager m = getWindowManager();
        // 获取屏幕宽、高度
        Display d = m.getDefaultDisplay();
        // 获取对话框当前的参数值
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        p.height = (int) (d.getHeight() * 0.2);
        // 高度设置为屏幕的0.6，根据实际情况调整,宽度设置为屏幕的0.65，根据实际情况调整
        p.width = (int) (d.getWidth() * 0.6);
        dialogWindow.setAttributes(p);
        //弹框以外不允许点击 但是返回键没有禁用
        searchServiceDialog.setCanceledOnTouchOutside(false);
        //弹框以外不允许点击 返回键也被禁用
//        dialog.setCancelable(false);
        searchServiceDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_two);
    }


    private void showLeaveMessageDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.leave_message_dialog, null);
        Button cancelLeaveMessage = (Button) view.findViewById(R.id.cancel_leave_message);
        Button leaveMessage = (Button) view.findViewById(R.id.leave_message);
        cancelLeaveMessage.setOnClickListener(this);
        leaveMessage.setOnClickListener(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        leaveMessageDialog = builder.setView(view).show();
        Window dialogWindow = leaveMessageDialog.getWindow();
        WindowManager m = getWindowManager();
        // 获取屏幕宽、高度
        Display d = m.getDefaultDisplay();
        // 获取对话框当前的参数值
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        p.height = (int) (d.getHeight() * 0.3);
        // 高度设置为屏幕的0.6，根据实际情况调整,宽度设置为屏幕的0.65，根据实际情况调整
        p.width = (int) (d.getWidth() * 0.7);
        dialogWindow.setAttributes(p);
        //弹框以外不允许点击 但是返回键没有禁用
        leaveMessageDialog.setCanceledOnTouchOutside(false);
        //弹框以外不允许点击 返回键也被禁用
//        dialog.setCancelable(false);
    }

    private void showEvaluateDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.evaluate_dialog, null);
        normalEvaluate = (LinearLayout) view.findViewById(R.id.evaluate_normal);
        satisfyEvaluate = (LinearLayout) view.findViewById(R.id.evaluate_satisfy);
        unsatisfyEvaluate = (LinearLayout) view.findViewById(R.id.evaluate_unsatisfy);
        Button evaluateCommit = (Button) view.findViewById(R.id.commit_evaluate);
        normalEvaluate.setOnClickListener(this);
        satisfyEvaluate.setOnClickListener(this);
        unsatisfyEvaluate.setOnClickListener(this);
        evaluateCommit.setOnClickListener(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        evaluateDialog = builder.setView(view).show();
        Window dialogWindow = this.evaluateDialog.getWindow();
        WindowManager m = getWindowManager();
        // 获取屏幕宽、高度
        Display d = m.getDefaultDisplay();
        // 获取对话框当前的参数值
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        p.height = (int) (d.getHeight() * 0.5);
        // 高度设置为屏幕的0.6，根据实际情况调整,宽度设置为屏幕的0.65，根据实际情况调整
        p.width = (int) (d.getWidth() * 0.9);
        dialogWindow.setAttributes(p);
        //弹框以外不允许点击 但是返回键没有禁用
        this.evaluateDialog.setCanceledOnTouchOutside(false);
    }

    private void closeSearchDialog() {
        if (searchServiceDialog != null && searchServiceDialog.isShowing()) {
            searchServiceDialog.dismiss();
        }
    }

    private void closeLeaveMessageDialog() {
        if (leaveMessageDialog != null && leaveMessageDialog.isShowing()) {
            leaveMessageDialog.dismiss();
        }
    }

    private void closeEvaluateDialog() {
        if (evaluateDialog != null && evaluateDialog.isShowing()) {
            evaluateDialog.dismiss();
        }
    }



    private boolean isServiced;

    private void requestService(final String chatMessage, final int messageType, final String filePath) {
        //发送消息的时候展示正在寻找客服
        showSearchDialog();
        Bangwo8SdkManager.getInstance().requestService(chatWithJid, agentId, new Bangwo8ResponseListener() {
            @Override
            public void requestServiceSuccess(String chatWithJid) {
                //进入不进入聊天队列是由客服触发决定的，不是客户端请求的
                //请求成功(进入聊天队列)了，再发消息就不需要再请求了
                isServiced = true;
                //post到主线程更新ui
                EventBus.getDefault().post(new OnSdkResponse(-1, chatMessage, messageType, filePath, OnSdkResponse.REQUEST_SUCCESS, null));
            }

            @Override
            public void requestServiceFailed(int queueLocation, String reason) {
                //没有进入聊天队列，找不到客服或在排队队列里,再发消息需要再请求
                isServiced = false;
                //post到主线程更新ui
                EventBus.getDefault().post(new OnSdkResponse(queueLocation, chatMessage, messageType, filePath, OnSdkResponse.REQUEST_FAILED, reason));
            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1){
                timer.cancel();
                showEvaluateDialog();
                Bangwo8SdkManager.getInstance().finishSession(chatWithJid, chatWithJid);
            }
            super.handleMessage(msg);
        }
    };

    private Timer timer;
    private void startTiming() {
        timer = new Timer();
        TimerTask timerTask = new TimerTask(){
            int i = 0;
            @Override
            public void run() {
                i++;
                if(i == 120){
                    Message message = new Message();
                    message.what = 1;
                    handler.sendMessage(message);
                }
            }
        };
        timer.schedule(timerTask,1000,1000);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMessage(OnSdkResponse msg) {
        int requestResult = msg.getRequestResult();
        if (requestResult == OnSdkResponse.REQUEST_SUCCESS) {
            //请求接口发的第一条消息
            firstSendMessage(msg);
            closeSearchDialog();
        } else {
            //请求服务失败
            closeSearchDialog();
            //先发送消息更新ui
            firstSendMessage(msg);
            String failedReason = msg.getFailedReason();
            if (failedReason.equals("正在排队等待服务")) {
                int queueLocation = msg.getQueueLocation();
                //只要还在排队队列中，就证明没有被客户接待
                if (queueLocation == 0) {
                    Toast.makeText(this,"您正在排队中,请耐心等待...",Toast.LENGTH_SHORT).show();
//                    upUiAndDbAfterSend("您正在排队中,请耐心等待...", null, ChatMessage.QUEUE_WAIT);
                } else {
                    Toast.makeText(this,"您前面还有" + queueLocation + "个客户正在排队,请耐心等待...",Toast.LENGTH_SHORT).show();
//                    upUiAndDbAfterSend("您前面还有" + queueLocation + "个客户正在排队,请耐心等待...", null, ChatMessage.QUEUE_WAIT);
                }
            } else if (failedReason.equals("用户在排队队列中")) {
                Toast.makeText(this,"客服正在努力接待中,请耐心等待...",Toast.LENGTH_SHORT).show();
//                upUiAndDbAfterSend("客服正在努力接待中,请耐心等待...", null, ChatMessage.QUEUE_WAIT);
            } else if (failedReason.equals("没有找到可分配的客服")) {
                showLeaveMessageDialog();
            }
        }
    }

    private void firstSendMessage(OnSdkResponse msg) {
        int messageType = msg.getMessageType();
        if (messageType == ChatMessage.TEXT_TO) {
            String chatMessage = msg.getChatMessage();
            if(Bangwo8SdkManager.getInstance().checkIsConnect()){
                upUiAndDbAfterSend(chatMessage, null, ChatMessage.TEXT_TO,ConnectState.CONNECTED);
            }else {
                upUiAndDbAfterSend(chatMessage, null, ChatMessage.TEXT_TO,ConnectState.CLOSE_ON_ERROR);
            }
            Bangwo8SdkManager.getInstance().sendTextMessage(chatMessage, chatWithJid, chatWithJid);
        } else if (messageType == ChatMessage.IMAGE_TO) {
            String filePath = msg.getFilePath();
            if(Bangwo8SdkManager.getInstance().checkIsConnect()){
                upUiAndDbAfterSend(null, filePath, ChatMessage.IMAGE_TO,ConnectState.CONNECTED);
            }else {
                upUiAndDbAfterSend(null, filePath, ChatMessage.IMAGE_TO,ConnectState.CLOSE_ON_ERROR);
            }
            Bangwo8SdkManager.getInstance().sendImageMessage(this, filePath, chatWithJid, chatWithJid, messageList.size(), dbCurrentTotalCount);
        } else if (messageType == ChatMessage.AUDIO_TO) {
            String filePath = msg.getFilePath();
            if(Bangwo8SdkManager.getInstance().checkIsConnect()){
                upUiAndDbAfterSend(null, filePath, ChatMessage.AUDIO_TO,ConnectState.CONNECTED);
            }else {
                upUiAndDbAfterSend(null, filePath, ChatMessage.AUDIO_TO,ConnectState.CLOSE_ON_ERROR);
            }
            //更新完界面和数据库后,messageList的size加1,数据库的数量加1
            Bangwo8SdkManager.getInstance().sendAudioMessage(this, filePath, chatWithJid, chatWithJid, messageList.size(), dbCurrentTotalCount);
        }
    }

    @Override
    public void onSendMessage(String msg) {
        if (isFinish == RecentChatListItem.FINISH_SERVICE) {
            disableAllSend();
            Toast.makeText(this, "会话已结束!", Toast.LENGTH_SHORT).show();
        } else {
            if (!isServiced) {
                requestService(msg, ChatMessage.TEXT_TO, null);
            } else {
                if(Bangwo8SdkManager.getInstance().checkIsConnect()){
                    upUiAndDbAfterSend(msg, null, ChatMessage.TEXT_TO,ConnectState.CONNECTED);
                }else {
                    upUiAndDbAfterSend(msg, null, ChatMessage.TEXT_TO,ConnectState.CLOSE_ON_ERROR);
                }
                Bangwo8SdkManager.getInstance().sendTextMessage(msg, chatWithJid, chatWithJid);
            }
        }
    }


    @Override
    public void onSendVoiceMessage(float seconds, String filePath) {
        if (!isServiced) {
            requestService(null, ChatMessage.AUDIO_TO, filePath);
        } else {

            if(Bangwo8SdkManager.getInstance().checkIsConnect()){
                upUiAndDbAfterSend(null, filePath, ChatMessage.AUDIO_TO,ConnectState.CONNECTED);
            }else {
                upUiAndDbAfterSend(null, filePath, ChatMessage.AUDIO_TO,ConnectState.CLOSE_ON_ERROR);
            }
            //更新完界面和数据库后,messageList的size加1,数据库的数量加1
            Bangwo8SdkManager.getInstance().sendAudioMessage(this, filePath, chatWithJid, chatWithJid, messageList.size(), dbCurrentTotalCount);
        }
    }

    @Override
    public void onPickPhotoMessage(Intent data) {
        Uri dataUri = data.getData();
        String pathFromURI = FileUtil.getRealPathFromURI(this, dataUri);
        String[] split = pathFromURI.split("/");
        String fileName = split[split.length - 1];
        Bitmap large = ImageUtil.ratio(pathFromURI, 2400, 2400);
        String imagePath = ImageUtil.saveBitmap2file(large, fileName, 30);

        if (!isServiced) {
            requestService(null, ChatMessage.IMAGE_TO, imagePath);
        } else {
            if(Bangwo8SdkManager.getInstance().checkIsConnect()){
                upUiAndDbAfterSend(null, imagePath, ChatMessage.IMAGE_TO,ConnectState.CONNECTED);
            }else {
                upUiAndDbAfterSend(null, imagePath, ChatMessage.IMAGE_TO,ConnectState.CLOSE_ON_ERROR);
            }
            //更新完界面和数据库后,messageList的size加1,数据库的数量加1
            Bangwo8SdkManager.getInstance().sendImageMessage(this, imagePath, chatWithJid, chatWithJid, messageList.size(), dbCurrentTotalCount);
        }
    }

    @Override
    public void onTakePhotoMessage(String filePath) {
        Bitmap large = ImageUtil.ratio(filePath, 2400, 2400);
        String[] split = filePath.split("/");
        String fileName = split[split.length - 1];
        String imagePath = ImageUtil.saveBitmap2file(large, fileName, 30);

        if (!isServiced) {
            requestService(null, ChatMessage.IMAGE_TO, imagePath);
        } else {
            if(Bangwo8SdkManager.getInstance().checkIsConnect()){
                upUiAndDbAfterSend(null, imagePath, ChatMessage.IMAGE_TO,ConnectState.CONNECTED);
            }else {
                upUiAndDbAfterSend(null, imagePath, ChatMessage.IMAGE_TO,ConnectState.CLOSE_ON_ERROR);
            }
            //更新完界面和数据库后,messageList的size加1,数据库的数量加1
            Bangwo8SdkManager.getInstance().sendImageMessage(this, imagePath, chatWithJid, chatWithJid, messageList.size(), dbCurrentTotalCount);
        }
    }

    @Override
    public void onPickFileMessage(Intent data) {
        Uri uri = data.getData();
        String path = FileUtil.getImagePathFromUriAfter19(this, uri);
        assert path != null;
        File file = new File(path);
        String fileSize = FileUtil.FormetFileSize(file.length());
        String[] split = path.split("/");
        String fileName = split[split.length - 1];
    }

    private float getAudioDuration(String filePath) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            int duration = mediaPlayer.getDuration();
            float v = Float.parseFloat(duration + "");
            return v / 1000;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0.0f;
    }

    private void upUiAndDbAfterSend(String message, String filePath, int messageType,int connectState) {
        long messageTime = System.currentTimeMillis();
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setTime(messageTime);
        chatMessage.setChatWithJid(chatWithJid);
        chatMessage.setMessageType(messageType);
        if (messageType == ChatMessage.TEXT_TO) {
            if(connectState == ConnectState.CONNECTED){
                chatMessage.setFileLoadState(ChatMessage.LOAD_SUCCESS);
            }else {
                chatMessage.setFileLoadState(ChatMessage.LOAD_FAILED);
            }
            chatMessage.setMessage(message);
        } else if (messageType == ChatMessage.IMAGE_TO) {
            if(connectState == ConnectState.CONNECTED){
                chatMessage.setFileLoadState(ChatMessage.LOADING);
            }else {
                chatMessage.setFileLoadState(ChatMessage.LOAD_FAILED);
            }
            chatMessage.setFilepath(filePath);
        } else if (messageType == ChatMessage.AUDIO_TO) {
            if(connectState == ConnectState.CONNECTED){
                chatMessage.setFileLoadState(ChatMessage.LOADING);
            }else {
                chatMessage.setFileLoadState(ChatMessage.LOAD_FAILED);
            }
            chatMessage.setFilepath(filePath);
            float duration = getAudioDuration(filePath);
            chatMessage.setVoiceTime(duration);
            chatMessage.setAudioReadState(ChatMessage.AUDIO_READ);
        } else if (messageType == ChatMessage.QUEUE_WAIT) {
            chatMessage.setMessage(message);
        }
        messageList.add(0, chatMessage);
        chatAdapter.setData(messageList);
        chatAdapter.notifyDataSetChanged();
        recycle.scrollToPosition(0);
        dbCurrentTotalCount++;
        if (messageType != ChatMessage.QUEUE_WAIT) {
            //展示需要等待的条目不需要往数据库存
            Bangwo8SdkManager.getInstance().saveChatMessageToDb(this, chatMessage);
        }

    }

    @Override
    public void onPickLocMessage(Intent data) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        //点击recyclerView使软键盘消失，设置setOnclickListener不管用，只能设置seOnTouchListener
        if (v.getId() == R.id.recylerView) {
            if (action == MotionEvent.ACTION_UP) {
                if (inputChat.isBoxShow()) {
                    inputChat.hideAll();
                }
            }
        }
        return false;
    }

    @Override
    public void fileSendSuccess(int listPosition, int dbPosition) {
        //文件发送成功的回调，post到主线程更新ui
        EventBus.getDefault().post(new UpFileSend(ChatMessage.LOAD_SUCCESS, listPosition, dbPosition));
    }

    @Override
    public void fileSendFailed(int listPosition, int dbPosition) {
        //文件发送失败的回调，post到主线程更新ui
        EventBus.getDefault().post(new UpFileSend(ChatMessage.LOAD_FAILED, listPosition, dbPosition));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMessage(UpFileSend msg) {
        int fileSendStatus = msg.getFileSendStatus();
        //由于集合是倒着排序的，所以实际上没有用上listPosition
        int dbOldPosition = msg.getDbPosition();
        //集合中对应的position是动态计算出来的
        int position = dbCurrentTotalCount - dbOldPosition;
        messageList.get(position).setFileLoadState(fileSendStatus);
        chatAdapter.notifyDataSetChanged();
        Bangwo8SdkManager.getInstance().upChatMessageFileLoadingStateToDb(this, dbOldPosition, chatWithJid, fileSendStatus);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveMessage(NewMsgCome msg) {
        ChatMessage chatMessage = msg.getChatMessage();
        messageList.add(0, chatMessage);
        chatAdapter.setData(messageList);
        chatAdapter.notifyDataSetChanged();
        recycle.scrollToPosition(0);
        dbCurrentTotalCount++;
    }

    @Override
    public void servicedSuccess() {
        //有客服接待了，再发消息不用再请求接口了
        isServiced = true;
        //再进来不需要再请求接口了
//        editor.putBoolean("isServiced",true);
//        editor.commit();
    }

    @Override
    public void finishService() {
        //再进来需要重新请求接口
//        editor.putBoolean("isServiced",false);
//        editor.commit();
        //客服结束服务了，弹出评价对话框
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println("客服结束服务了");
                showEvaluateDialog();
            }
        });
    }


    @Override
    public void connectSuccess() {
        //可以更新界面
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ChatActivity.this,"连接成功",Toast.LENGTH_SHORT).show();
                connectStateLayout.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void connectionClosed() {
        //可以更新界面
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ChatActivity.this,"连接异常断开",Toast.LENGTH_SHORT).show();
                connectStateLayout.setVisibility(View.VISIBLE);
            }
        });
    }
}
