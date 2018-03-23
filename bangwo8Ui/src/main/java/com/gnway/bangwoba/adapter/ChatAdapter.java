package com.gnway.bangwoba.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.gnway.bangwo8sdk.bean.ChatMessage;
import com.gnway.bangwoba.global.ChatColor;
import com.gnway.bangwoba.R;
import com.gnway.bangwoba.callback.OnChatItemClickListener;
import com.gnway.bangwoba.utils.ChatTimeUtil;
import com.gnway.bangwoba.utils.EmojiUtil;
import com.gnway.bangwoba.utils.GlideApp;
import com.gnway.bangwoba.view.BoundTextView;
import com.gnway.bangwoba.view.BubbleImageView;
import com.gnway.bangwoba.view.BubbleLinearLayout;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by luzhan on 2017/6/17.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ChatMessage> list;
    private Activity activity;
    private int mMinItemWith;// 设置对话框的最大宽度和最小宽度
    private int mMaxItemWith;
    private int current = -1;

    public ChatAdapter(ArrayList<ChatMessage> list, Activity activity) {
        this.list = list;
        this.activity = activity;
        // 获取系统宽度
        WindowManager wManager = (WindowManager) activity
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wManager.getDefaultDisplay().getMetrics(outMetrics);
        mMaxItemWith = (int) (outMetrics.widthPixels * 0.7f);
        mMinItemWith = (int) (outMetrics.widthPixels * 0.15f);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == ChatMessage.TEXT_FROM) {
            //说明是收到的文本消息
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_messgae_text_left, parent, false);
            return new TextHolder(view);
        } else if (viewType == ChatMessage.TEXT_TO) {
            //说明是发送的文本消息
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_messgae_text_right, parent, false);
            return new TextHolder(view);
        } else if (viewType == ChatMessage.AUDIO_FROM) {
            //说明是收到的语音消息
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_messgae_audio_left, parent, false);
            return new AudioHolder(view);
        } else if (viewType == ChatMessage.AUDIO_TO) {
            //说明是发送的语音消息
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_messgae_audio_right, parent, false);
            return new AudioHolder(view);
        } else if (viewType == ChatMessage.IMAGE_FROM) {
            //说明是收到的图片消息
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_messgae_image_left, parent, false);
            return new ImageHolder(view);
        } else if (viewType == ChatMessage.IMAGE_TO) {
            //说明是发送的图片消息
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_messgae_image_right, parent, false);
            return new ImageHolder(view);
        } else if (viewType == ChatMessage.QUEUE_WAIT) {
            //说明是队列等待消息
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.queue_wait, parent, false);
            return new QueueWaitHolder(view);
        } else {
            //展示结束会话条目
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.finish_call_layout, parent, false);
            return new FinishHolder(view);
        }

    }

    public ArrayList<ChatMessage> getMessageList() {
        return list;
    }

    @Override
    public int getItemViewType(int position) {
        //此行代码决定集合中的每条数据用哪种布局展示
        return list.get(position).getMessageType();
    }

    private OnChatItemClickListener onChatItemClickListener;

    public void setOnChatItemClickListener(OnChatItemClickListener onChatItemClickListener) {
        this.onChatItemClickListener = onChatItemClickListener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int itemViewType = getItemViewType(position);
        if(holder instanceof TextHolder){
            final TextHolder textHolder = (TextHolder)holder;
            if (itemViewType == ChatMessage.TEXT_FROM) {
                textHolder.textContent.setBubbleColor(Color.parseColor(ChatColor.chatLeftColor));
            } else {
                textHolder.textContent.setBubbleColor(Color.parseColor(ChatColor.chatRightColor));
            }
            textHolder.textContent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onChatItemClickListener.onChatItemLongClick(textHolder.getAdapterPosition(),v);
                    return true;
                }
            });
            String friendlyTime = ChatTimeUtil.getFriendlyTimeSpanByNow(list.get(position).getTime());
            String textMessage = list.get(position).getMessage();
            textHolder.textChatTime.setText(friendlyTime);

            EmojiUtil.handlerEmojiText(textHolder.textContent,textMessage,activity, 20);

            int fileLoadState = list.get(position).getFileLoadState();
            if (fileLoadState == ChatMessage.LOADING) {
                textHolder.textChatLoading.setVisibility(View.VISIBLE);
                textHolder.textChatLoading.setBackgroundResource(R.drawable.loading_img);
                AnimationDrawable animationDrawable = (AnimationDrawable) textHolder.textChatLoading.getBackground();
                animationDrawable.start();
            } else if (fileLoadState == ChatMessage.LOAD_SUCCESS) {
                //不能gone掉，因为是相对布局，以它为基准
                textHolder.textChatLoading.setVisibility(View.INVISIBLE);
            } else if (fileLoadState == ChatMessage.LOAD_FAILED) {
                textHolder.textChatLoading.setVisibility(View.VISIBLE);
                textHolder.textChatLoading.setBackgroundResource(R.drawable.file_load_fail);
            }
            //执行动画
            animateHolder(textHolder,position);

        }else if(holder instanceof AudioHolder){
            final AudioHolder audioHolder = (AudioHolder)holder;
            if (itemViewType == ChatMessage.AUDIO_FROM) {
                audioHolder.audioItem.setBubbleColor(Color.parseColor(ChatColor.chatLeftColor));
            } else {
                audioHolder.audioItem.setBubbleColor(Color.parseColor(ChatColor.chatRightColor));
            }
            audioHolder.audioItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onChatItemClickListener.onChatItemClick(audioHolder.getAdapterPosition(), v);
                }
            });
            String friendlyTime = ChatTimeUtil.getFriendlyTimeSpanByNow(list.get(position).getTime());
            audioHolder.audioChatTime.setText(friendlyTime);
            if (list.get(position).getRecordPlayState() == ChatMessage.RECORD_PLAYING) {
                if (itemViewType == ChatMessage.AUDIO_TO) {
                    audioHolder.audioAnimal.setBackgroundResource(R.drawable.play_right);
                    AnimationDrawable drawable = (AnimationDrawable) audioHolder.audioAnimal
                            .getBackground();
                    drawable.start();
                } else {
                    audioHolder.audioAnimal.setBackgroundResource(R.drawable.play_left);
                    AnimationDrawable drawable = (AnimationDrawable) audioHolder.audioAnimal
                            .getBackground();
                    drawable.start();
                }
            } else {
                if (itemViewType == ChatMessage.AUDIO_TO) {
                    audioHolder.audioAnimal.setBackgroundResource(R.drawable.chat_to_voice_playing);
                } else {
                    audioHolder.audioAnimal.setBackgroundResource(R.drawable.chatfrom_voice_playing);
                }
            }
            audioHolder.audioRecordTime.setText(Math.round(list.get(position).getVoiceTime()) + "\"");
            ViewGroup.LayoutParams lParams = audioHolder.audioItem.getLayoutParams();
            lParams.width = (int) (mMinItemWith + mMaxItemWith / 60f * list.get(position).getVoiceTime());
            audioHolder.audioItem.setLayoutParams(lParams);
            int fileLoadState = list.get(position).getFileLoadState();
            if (fileLoadState == ChatMessage.LOADING) {
                audioHolder.audioChatLoading.setVisibility(View.VISIBLE);
                audioHolder.audioChatLoading.setBackgroundResource(R.drawable.loading_img);
                AnimationDrawable animationDrawable = (AnimationDrawable) audioHolder.audioChatLoading.getBackground();
                animationDrawable.start();
            } else if (fileLoadState == ChatMessage.LOAD_SUCCESS) {
                //不能gone掉，因为是相对布局，以它为基准
                audioHolder.audioChatLoading.setVisibility(View.INVISIBLE);
            } else if (fileLoadState == ChatMessage.LOAD_FAILED) {
                audioHolder.audioChatLoading.setVisibility(View.VISIBLE);
                audioHolder.audioChatLoading.setBackgroundResource(R.drawable.file_load_fail);
            }
            //执行动画
            animateHolder(audioHolder,position);
        }else if(holder instanceof ImageHolder){
            final ImageHolder imageHolder = (ImageHolder)holder;
            imageHolder.imageContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onChatItemClickListener.onChatItemClick(imageHolder.getAdapterPosition(), v);
                }
            });
            String friendlyTime = ChatTimeUtil.getFriendlyTimeSpanByNow(list.get(position).getTime());
            imageHolder.imageChatTime.setText(friendlyTime);
            String imagePath = list.get(position).getFilepath();
            GlideApp.with(activity).load(imagePath).placeholder(R.drawable.default_picture).into(imageHolder.imageContent);
            int fileLoadState = list.get(position).getFileLoadState();
            if (fileLoadState == ChatMessage.LOADING) {
                imageHolder.imageChatLoading.setVisibility(View.VISIBLE);
                imageHolder.imageChatLoading.setBackgroundResource(R.drawable.loading_img);
                AnimationDrawable animationDrawable = (AnimationDrawable) imageHolder.imageChatLoading.getBackground();
                animationDrawable.start();
            } else if (fileLoadState == ChatMessage.LOAD_SUCCESS) {
                //不能gone掉，因为是相对布局，以它为基准
                imageHolder.imageChatLoading.setVisibility(View.INVISIBLE);
            } else if (fileLoadState == ChatMessage.LOAD_FAILED) {
                imageHolder.imageChatLoading.setVisibility(View.VISIBLE);
                imageHolder.imageChatLoading.setBackgroundResource(R.drawable.file_load_fail);
            }
            //执行动画
            animateHolder(imageHolder,position);
        }else if(holder instanceof QueueWaitHolder){
            QueueWaitHolder needWaitHolder = (QueueWaitHolder)holder;
            needWaitHolder.queueWait.setText(list.get(position).getMessage());
        }
    }

    private long lastMessageTime = 0;

    private void animateHolder(RecyclerView.ViewHolder holder, int position) {
        long newMessageTime = list.get(0).getTime();
        int itemViewType = getItemViewType(0);
        if (lastMessageTime == 0) {
            lastMessageTime = newMessageTime;
        }
        //用时间戳去判断position == 0的条目该不该执行动画
        if (position == 0 && newMessageTime > lastMessageTime) {
            lastMessageTime = newMessageTime;
            if (itemViewType == ChatMessage.TEXT_FROM || itemViewType == ChatMessage.AUDIO_FROM || itemViewType == ChatMessage.IMAGE_FROM) {
                holder.itemView.setTranslationX(-130f);
            } else if (itemViewType == ChatMessage.TEXT_TO || itemViewType == ChatMessage.AUDIO_TO || itemViewType == ChatMessage.IMAGE_TO) {
                holder.itemView.setTranslationX(130f);
            }
            holder.itemView.setAlpha(0f);
            ViewCompat.animate(holder.itemView)
                    .translationX(0)
                    .alpha(1f)
                    .setDuration(300)
                    .start();
        }

    }

    private void audioAnimal(ImageView view){
        ScaleAnimation sa = new ScaleAnimation(1.0f, 1.3f, 1.0f, 1.3f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //[2]设置动画执行的时间
        sa.setDuration(300);
        //[3]设置动画执行的次数
        sa.setRepeatCount(30);
        //[4]设置动画执行模式
        sa.setRepeatMode(Animation.REVERSE);
        view.startAnimation(sa);
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(ArrayList<ChatMessage> messageList) {
        this.list = messageList;
    }

    private class TextHolder extends RecyclerView.ViewHolder {

        private TextView textChatTime;
        private CircleImageView textChatLeftAvatar;
        private CircleImageView textChatRightAvatar;
        private ImageView textChatLoading;
        private ImageView textChatLoadingRight;
        private BoundTextView textContent;
        private LinearLayout textChatLeftLayout;
        private LinearLayout textChatRightLayout;

        private TextHolder(View itemView) {
            super(itemView);
            textChatTime = (TextView) itemView.findViewById(R.id.text_chat_time);
            textChatLeftLayout = (LinearLayout) itemView.findViewById(R.id.text_chat_left_layout);
            textChatRightLayout = (LinearLayout) itemView.findViewById(R.id.text_chat_right_layout);
            textChatLeftAvatar = (CircleImageView) itemView.findViewById(R.id.text_chat_left_avatar);
            textChatRightAvatar = (CircleImageView) itemView.findViewById(R.id.text_chat_right_avatar);
            textChatLoading = (ImageView) itemView.findViewById(R.id.text_chat_loading);

            textContent = (BoundTextView) itemView.findViewById(R.id.text_content);
        }
    }

    private class AudioHolder extends RecyclerView.ViewHolder {

        private TextView audioChatTime;
        private CircleImageView audioChatLeftAvatar;
        private CircleImageView audioChatRightAvatar;
        private ImageView audioChatLoading;
        private BubbleLinearLayout audioItem;
        private ImageView audioAnimal;
        private LinearLayout audioChatLeftLayout;
        private LinearLayout audioChatRightLayout;
        private TextView audioRecordTime;


        private AudioHolder(View itemView) {
            super(itemView);
            audioChatTime = (TextView) itemView.findViewById(R.id.audio_chat_time);
            audioChatLeftLayout = (LinearLayout) itemView.findViewById(R.id.audio_chat_left_layout);
            audioChatRightLayout = (LinearLayout) itemView.findViewById(R.id.audio_chat_right_layout);
            audioChatLeftAvatar = (CircleImageView) itemView.findViewById(R.id.audio_chat_left_avatar);
            audioChatRightAvatar = (CircleImageView) itemView.findViewById(R.id.audio_chat_right_avatar);
            audioChatLoading = (ImageView) itemView.findViewById(R.id.audio_chat_loading);

            audioItem = (BubbleLinearLayout) itemView.findViewById(R.id.audio_item);
            audioAnimal = (ImageView) itemView.findViewById(R.id.audio_animal);
            audioRecordTime = (TextView) itemView.findViewById(R.id.audio_recorder_time);
        }
    }

    private class ImageHolder extends RecyclerView.ViewHolder {

        private TextView imageChatTime;
        private CircleImageView imageChatLeftAvatar;
        private CircleImageView imageChatRightAvatar;
        private ImageView imageChatLoading;
        private BubbleImageView imageContent;
        private LinearLayout imageChatLeftLayout;
        private LinearLayout imageChatRightLayout;

        private ImageHolder(View itemView) {
            super(itemView);
            imageChatTime = (TextView) itemView.findViewById(R.id.image_chat_time);
            imageChatLeftLayout = (LinearLayout) itemView.findViewById(R.id.image_chat_left_layout);
            imageChatRightLayout = (LinearLayout) itemView.findViewById(R.id.image_chat_right_layout);
            imageChatLeftAvatar = (CircleImageView) itemView.findViewById(R.id.image_chat_left_avatar);
            imageChatRightAvatar = (CircleImageView) itemView.findViewById(R.id.image_chat_right_avatar);
            imageChatLoading = (ImageView) itemView.findViewById(R.id.image_chat_loading);

            imageContent = (BubbleImageView) itemView.findViewById(R.id.image_content);
        }
    }

    private class FinishHolder extends RecyclerView.ViewHolder {

        FinishHolder(View itemView) {
            super(itemView);
        }
    }

    private class QueueWaitHolder extends RecyclerView.ViewHolder {

        private TextView queueWait;

        QueueWaitHolder(View itemView) {
            super(itemView);
            queueWait = (TextView) itemView.findViewById(R.id.queue_need_wait);
        }
    }
}
