package com.wuyou.user.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.gnway.bangwo8sdk.Bangwo8SdkManager;
import com.gnway.bangwo8sdk.bean.ChatMessage;
import com.gnway.bangwo8sdk.listener.Bangwo8MessageListener;
import com.gnway.bangwoba.activity.ChatActivity;
import com.gnway.bangwoba.bean.NewMsgCome;
import com.gnway.bangwoba.global.Variable;
import com.gnway.bangwoba.utils.MD5Encoder;
import com.wuyou.user.R;
import com.wuyou.user.view.activity.MainActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by DELL on 2018/3/15.
 */

public class HelpChatService extends Service implements Bangwo8MessageListener {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    @Override
    public void messageReceive(ChatMessage chatMessage) {
        EventBus.getDefault().post(new NewMsgCome(chatMessage));
        Bangwo8SdkManager.getInstance().updateNotification(this,chatMessage, R.mipmap.kehuduan, ChatActivity.class,MainActivity.class);
        Bangwo8SdkManager.getInstance().saveChatMessageToDb(this, chatMessage);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //根据规则生成密码
        String passWord = getPassWordByLoginUser(Variable.loginUser);
        //用户名与密码登录帮我吧服务器
        Bangwo8SdkManager.getInstance().login(Variable.loginUser , passWord);
        //开启服务后添加接收消息的监听器
        Bangwo8SdkManager.getInstance().addMessageReceiveListener(this);
    }

    private String getPassWordByLoginUser(String loginUserName) {
        try {
            return MD5Encoder.encode(loginUserName + "var sRand=0x1E9D8F7B;");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
