package com.gnway.bangwoba.widgets;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gnway.bangwoba.R;
import com.gnway.bangwoba.bean.FragmentLoadFinish;
import com.gnway.bangwoba.bean.RecentChatListItem;
import com.gnway.bangwoba.view.AudioRecordButton;
import com.gnway.bangwoba.widgets.tools.DImenUtil;
import com.gnway.bangwoba.widgets.tools.ResourceUtil;
import com.gnway.bangwoba.widgets.emotion.EmotionView;
import com.gnway.bangwoba.widgets.emotion.data.CustomEmoji;
import com.gnway.bangwoba.widgets.emotion.data.Emoji;
import com.gnway.bangwoba.widgets.emotion.data.Emoticon;
import com.gnway.bangwoba.widgets.emotion.data.EmotionData;
import com.gnway.bangwoba.widgets.emotion.data.UniqueEmoji;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChatInputFragment extends BaseFragment implements AudioRecordButton.AudioFinishRecorderListener {
    private static final int REQUEST_CODE_TAKE_PHOTO = 1;
    private static final int REQUEST_CODE_TAKE_VIDEO = 8;
    private static final int REQUEST_CODE_PICK_PHOTO = 2;
    private static final int REQUEST_CODE_TAKE_MEDIA = 9;
    private static final int REQUEST_CODE_CHOOSE_LOCATION = 3;
    private static final int REQUEST_CODE_PICK_FILE = 5;
    public static final int TAKE_PICTURE_TO = 4;
    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE1 = 7;

    protected LinearLayout rootView;
    protected LinearLayout buttonView;
    private OnlyView RecordOrEdit;
    public EditText editText;
    private OnlyView sendOrMoreFuntion;
    private Button sendBtn;
    public Button moreFuntion;
    public Button keyBoardChangeByMoreFuntion;
    private OnlyView voiceOrKeyBoard;
    public Button voiceBtn;
    public Button keyBoardBtnChangeByVoice;
    private OnlyView eMojiOrKeyBoard;
    public Button emojiBtn;
    public Button keyBoardBtnChangeByEmoji;
    protected OnlyView boxView;
    protected ChatToolBox toolBox;
    private int isFinish;

    private EmotionView emotionView;

    protected InputChatListener inputChatListener = null;
    private List<ChatToolBox.ChatToolItem> items = new ArrayList<ChatToolBox.ChatToolItem>();

    private boolean keyBoardShow = false;

    private int boxViewHeight;
    private AudioRecordButton voiceView;
    private AudioRecordButton recordButton;
    private String filePath;
    private File takePhotoFile;

    public void clearInput() {
        editText.setText("");
    }

    @Override
    public void onFinished(float seconds, String filePath) {
        inputChatListener.onSendVoiceMessage(seconds, filePath);
    }

    public interface InputChatListener {
        void onSendMessage(String msg);
        void onSendVoiceMessage(float seconds, String filePath);
        void onPickPhotoMessage(Intent data);
        void onTakePhotoMessage(String filePath);
        void onPickLocMessage(Intent data);
        void onPickFileMessage(Intent data);
    }

    //this will be called at first, so we must make initial data there to avoid initial data being reset. !!!
    public ChatInputFragment() {
        inputChatListener = null;
        items = new ArrayList<>();
        items.add(new ToolCamera());
        items.add(new ToolPhoto());
//        items.add(new ToolFile());
        //items.add(new ToolPosition());
    }
    public void setIsFinish(int isFinish){
        this.isFinish = isFinish;
    }

    public void setInputChatListener(InputChatListener listener) {
        this.inputChatListener = listener;
    }

    private int getBoxViewHeight() {
        if (0 == boxViewHeight) {
            // 保存键盘高度，隐藏toolbox
            SharedPreferences pref = getActivity().getSharedPreferences("sys_variable", Context.MODE_PRIVATE);
            boxViewHeight = pref.getInt("virtual_keyboard_height", 0);
            if (0 == boxViewHeight) {
                if (null != getActivity()) {
                    boxViewHeight = DImenUtil.dip2px(getActivity(), 230);
                } else {
                    boxViewHeight = 2 * 230;
                }
            }
        }
        return boxViewHeight;
    }

    //this will be called when fragment called to show the view, this will be called after creatView().
    @Override
    protected View onInitializeView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (LinearLayout) inflater.inflate(R.layout.inputchat_new, container, false);
        buttonView = (LinearLayout) rootView.findViewById(R.id.chat_button);
        toolBox = (ChatToolBox) rootView.findViewById(R.id.chat_tool_box);
        boxView = (OnlyView) rootView.findViewById(R.id.box_view);
        recordButton = (AudioRecordButton) rootView.findViewById(R.id.record_button);
        recordButton.setAudioFinishRecorderListener(this);

        // EmotionView 开始入口位置
        emotionView = (EmotionView) rootView.findViewById(R.id.emotion_view);
        // 1 构建需要显示的表情结构
        List<EmotionData> emotionList = new ArrayList<>();
        // 1.1 emoji emotion
        TypedArray icons = getContext().getResources().obtainTypedArray(R.array.emotion_array);
        List<Emoticon> emojiList = new ArrayList<>();
        String[] stringArray = getContext().getResources().getStringArray(R.array.emotion_code);
        for (int i = 0; i < icons.length(); ++i) {
            Emoticon emoji = new Emoji(icons.getResourceId(i, 0), stringArray[i]);
            emojiList.add(emoji);
        }
        icons.recycle();
        EmotionData data = new EmotionData(emojiList,
                ResourceUtil.getResourceUriString(getContext(), R.drawable.u1f004),
                EmotionData.EmotionCategory.emoji,
                new UniqueEmoji(R.drawable.bx_emotion_delete), 3, 7);
        emotionList.add(data);
        // 1.2 custom emotion
        String temp = ResourceUtil.getResourceUriString(getContext(), R.mipmap.ic_launcher);
        List<Emoticon> customList = new ArrayList<>();
        customList.add(new CustomEmoji(temp));
        customList.add(new CustomEmoji(temp));
        customList.add(new CustomEmoji(temp));

        data = new EmotionData(customList,
                ResourceUtil.getResourceUriString(getContext(), R.mipmap.ic_launcher),
                EmotionData.EmotionCategory.image,
                new UniqueEmoji(temp), 2, 4);
//                EmotionData.EmotionCategory.image, 2, 4);
        emotionList.add(data);
        // 以上即在填充emotionView前所需要完成的结构内容
        emotionView.setEmotionDataList(emotionList);
        // 2 设置相应的监听器
        emotionView.setEmotionClickListener(new EmotionView.EmotionClickListener() {
            @Override
            public void OnEmotionClick(Emoticon emotionData, View v, EmotionData.EmotionCategory category) {
                switch (category) {
                    case emoji:
                        //Emoji,UniqueEmoji和CustomEmoji都是Emoticon的实现类
                        String desc = emotionData.getDesc();
                        Integer drawableId = emotionData.getResourceId();
                        ImageSpan imageSpan = new ImageSpan(getActivity(), BitmapFactory.decodeResource(getResources(), drawableId));
                        SpannableString spannableString = new SpannableString(desc);  //图片所表示的文字
                        spannableString.setSpan(imageSpan, 0, spannableString.length(), SpannableString.SPAN_MARK_MARK);
                        editText.append(spannableString);
                        break;
                    case image:
                        Toast.makeText(getContext(),
                                "path:" + emotionData.getDesc(),
                                Toast.LENGTH_SHORT).show();
                    default:
                }
            }

            @Override
            public void OnUniqueEmotionClick(Emoticon uniqueItem, View v, EmotionData.EmotionCategory category) {
                switch (category) {
                    case emoji:
                        String text = editText.getText().toString();
                        if (text.isEmpty()) {
                            return;
                        }
                        if ("]".equals(text.substring(text.length() - 1, text.length()))) {
                            int index = text.lastIndexOf("[");
                            if (index == -1) {
                                int action = KeyEvent.ACTION_DOWN;
                                int code = KeyEvent.KEYCODE_DEL;
                                KeyEvent event = new KeyEvent(action, code);
                                editText.onKeyDown(KeyEvent.KEYCODE_DEL, event);
                                return;
                            }
                            editText.getText().delete(index, text.length());
                            return;
                        }
                        int action = KeyEvent.ACTION_DOWN;
                        int code = KeyEvent.KEYCODE_DEL;
                        KeyEvent event = new KeyEvent(action, code);
                        editText.onKeyDown(KeyEvent.KEYCODE_DEL, event);
                        break;
                    case image:
                        Toast.makeText(getContext(), "uniqueItem: " + "path:" + uniqueItem.getDesc(),
                                Toast.LENGTH_SHORT).show();

                        String temp = ResourceUtil.getResourceUriString(getContext(), R.mipmap.ic_launcher);
                        List<Emoticon> customList = emotionView.getEmotionDataList().get(1).getEmotionList();
                        customList.add(new CustomEmoji(temp));
                        EmotionData data = new EmotionData(customList,
                                ResourceUtil.getResourceUriString(getContext(), R.mipmap.ic_launcher),
                                EmotionData.EmotionCategory.image, new UniqueEmoji(temp), 2, 4);

                        emotionView.modifyEmotionDataList(data, 1);
                    default:
                }
            }
        });

        init();
        return rootView;
    }

    public void init() {
        initInputLayout();
        initToolBox();
    }

    private void initToolBox() {
        toolBox.setData(items);
    }

    private void initInputLayout() {
        voiceOrKeyBoard = (OnlyView) buttonView.findViewById(R.id.button_1);
        voiceBtn = (Button) buttonView.findViewById(R.id.voice_button);
        keyBoardBtnChangeByVoice = (Button) buttonView.findViewById(R.id.voice2chat_button);
        eMojiOrKeyBoard = (OnlyView) buttonView.findViewById(R.id.button_2);
        emojiBtn = (Button) buttonView.findViewById(R.id.emoji_button);
        keyBoardBtnChangeByEmoji = (Button) buttonView.findViewById(R.id.emoji2chat_button);
        sendOrMoreFuntion = (OnlyView) buttonView.findViewById(R.id.button_3);
        sendBtn = (Button) buttonView.findViewById(R.id.send_button);
        moreFuntion = (Button) buttonView.findViewById(R.id.send2tool_button);
        keyBoardChangeByMoreFuntion = (Button) buttonView.findViewById(R.id.send2toolOn_button);
        RecordOrEdit = (OnlyView) buttonView.findViewById(R.id.voice_view);
        voiceView = (AudioRecordButton) buttonView.findViewById(R.id.record_button);
        editText = (EditText) buttonView.findViewById(R.id.id_edit);
//        editText.setCursorVisible(true);
        //editText不获取焦点的话，语音逐帧动画播放就会卡在第一帧
        editText.requestFocus();

        initButtonListener();
        if(isFinish == RecentChatListItem.FINISH_SERVICE){
            EventBus.getDefault().post(new FragmentLoadFinish());
        }
    }

    private void requestAudioPermission(){
        int flag = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO);
        if (flag!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{ Manifest.permission.RECORD_AUDIO },1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
        }
    }

    private void initButtonListener() {
        voiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideAll();
                voiceOrKeyBoard.setChildView(keyBoardBtnChangeByVoice);
                eMojiOrKeyBoard.setChildView(emojiBtn);
                sendOrMoreFuntion.setChildView(moreFuntion);
                RecordOrEdit.setChildView(voiceView);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //如果api大于6.0就请求权限
                    requestAudioPermission();
                }
            }
        });
        keyBoardBtnChangeByVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSoftInput();
                voiceOrKeyBoard.setChildView(voiceBtn);
                eMojiOrKeyBoard.setChildView(emojiBtn);
                RecordOrEdit.setChildView(editText);
                editText.requestFocus();
            }
        });
        emojiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput(new Runnable() {
                    @Override
                    public void run() {
                        showBoxView();
                    }
                });
                voiceOrKeyBoard.setChildView(voiceBtn);
                eMojiOrKeyBoard.setChildView(keyBoardBtnChangeByEmoji);
                if (keyBoardChangeByMoreFuntion.getVisibility() == View.VISIBLE) {
                    sendOrMoreFuntion.setChildView(moreFuntion);
                }
                RecordOrEdit.setChildView(editText);
                boxView.setChildView(emotionView);
            }
        });
        keyBoardBtnChangeByEmoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSoftInput();
                voiceOrKeyBoard.setChildView(voiceBtn);
                eMojiOrKeyBoard.setChildView(emojiBtn);
                RecordOrEdit.setChildView(editText);
                editText.requestFocus();
            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != inputChatListener) {
                    inputChatListener.onSendMessage(editText.getEditableText().toString());
                }
                clearInput();
            }
        });
        moreFuntion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput(new Runnable() {
                    @Override
                    public void run() {
                        showBoxView();
                    }
                });
                voiceOrKeyBoard.setChildView(voiceBtn);
                eMojiOrKeyBoard.setChildView(emojiBtn);
                sendOrMoreFuntion.setChildView(keyBoardChangeByMoreFuntion);
                RecordOrEdit.setChildView(editText);
                boxView.setChildView(toolBox);
            }
        });
        keyBoardChangeByMoreFuntion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSoftInput();
                voiceOrKeyBoard.setChildView(voiceBtn);
                eMojiOrKeyBoard.setChildView(emojiBtn);
                RecordOrEdit.setChildView(editText);
                editText.requestFocus();
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    sendOrMoreFuntion.setChildView(moreFuntion);
                } else {
                    sendOrMoreFuntion.setChildView(sendBtn);
                }
                buttonView.postInvalidate();
            }
        });
    }

    private void showBoxView() {
        if (null == boxView) {
            return;
        }
        if (isBoxViewShow()) {
            return;
        }
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) boxView.getLayoutParams();
        params.height = getBoxViewHeight();
        boxView.setLayoutParams(params);
        boxView.setVisibility(View.VISIBLE);
    }

    public void addToolBoxData(ChatToolBox.ChatToolItem item) {
        this.items.add(item);
        if (null != toolBox) {
            toolBox.setData(items);
        }
    }

    public void addToolBoxData(List<ChatToolBox.ChatToolItem> items) {
        this.items.addAll(items);
        if (null != toolBox) {
            toolBox.setData(items);
        }
    }

    Runnable runOnKeyboardDismiss;

    protected boolean hideSoftInput(Runnable runnable) {
        if (keyBoardShow) {
            runOnKeyboardDismiss = runnable;
            super.hideSoftKeyboard();
            return true;
        }
        if (null != runnable) {
            runnable.run();
        }
        runOnKeyboardDismiss = null;
        return false;
    }

    public void onKeyboardShow(int height) {
        //  保存键盘高度，隐藏toolbox
        SharedPreferences pref = getActivity().getSharedPreferences("sys_variable", Context.MODE_PRIVATE);
        pref.edit().putInt("virtual_keyboard_height", height).apply();
        boxViewHeight = height;
        keyBoardShow = true;
        hideBoxView();
        voiceOrKeyBoard.setChildView(voiceBtn);
        eMojiOrKeyBoard.setChildView(emojiBtn);

        if (!editText.getText().toString().equals("")) {
            sendOrMoreFuntion.setChildView(sendBtn);
        } else {
            sendOrMoreFuntion.setChildView(moreFuntion);
        }
    }

    public void onKeyboardDismiss() {
        keyBoardShow = false;
        if (null != runOnKeyboardDismiss) {
            runOnKeyboardDismiss.run();
            runOnKeyboardDismiss = null;
        }
    }

    public void hideBoxViewMode() {
        if (null == rootView) {
            return;
        }
        this.hideAll();
        voiceOrKeyBoard.setChildView(voiceBtn);
        eMojiOrKeyBoard.setChildView(emojiBtn);
        sendOrMoreFuntion.setChildView(moreFuntion);
        RecordOrEdit.setChildView(editText);
    }

    @Override
    public boolean handleBack() {
        return hideAll();
    }

    public boolean hideAll() {
        return hideBoxView() || hideSoftInput(null);
    }

    public boolean hideBoxView() {
        if (isBoxViewShow()) {
            boxView.setVisibility(View.GONE);
            return true;
        } else {
            return false;
        }
    }

    private void showSoftInput() {
        if (keyBoardShow) {
            return;
        }
        InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.toggleSoftInput(0, 0);
    }

    private boolean isBoxViewShow() {
        if (null == boxView) {
            return false;
        }
        return View.VISIBLE == boxView.getVisibility();
    }

    public boolean isBoxShow() {
        return isBoxViewShow() || keyBoardShow;
    }

    private class ToolPhoto implements ChatToolBox.ChatToolItem {
        @Override
        public int getIcon() {
            return R.drawable.app_panel_gallery_selector;//app_panel_pic_icon
        }

        @Override
        public String getName() {
            return "照片";
        }

        @Override
        public void onItemSelected() {
            startPhotoPicker();
        }
    }

    private class ToolCamera implements ChatToolBox.ChatToolItem {
        @Override
        public int getIcon() {
            return R.drawable.app_panel_video_selector;
        }

        @Override
        public String getName() {
            return "拍摄";
        }

        @Override
        public void onItemSelected() {
            startCameraDlg();
        }
    }

    /*private class ToolPosition implements ChatToolBox.ChatToolItem {
        @Override
        public int getIcon() {
            return R.drawable.app_panel_location_selector;
        }

        @Override
        public String getName() {
            return "位置";
        }

        @Override
        public void onItemSelected() {
            startLocationChooser();
        }
    }*/

    private class ToolFile implements ChatToolBox.ChatToolItem {
        @Override
        public int getIcon() {
            return R.drawable.app_panel_ads_selector;
        }

        @Override
        public String getName() {
            return "文件";
        }

        @Override
        public void onItemSelected() {
            startFileChooser();
        }
    }

    private void startFileChooser() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CALL_PHONE1);
        } else {
            Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
            intent1.setType("*/*");
            intent1.addCategory(Intent.CATEGORY_OPENABLE);
            try {
                startActivityForResult(Intent.createChooser(intent1, "选择文件"), REQUEST_CODE_PICK_FILE);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(getActivity(), "Please install a File Manager.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void startCameraDlg() {
        //以下是调用系统相机的逻辑，保留
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(getContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    TAKE_PICTURE_TO);
        } else{
            filePath = getFilePath(System.currentTimeMillis() + ".jpg");
            takePhotoFile = new File(filePath);
            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //判断版本是否大于7.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, takePhotoFile.getAbsolutePath());
                Uri uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(openCameraIntent, REQUEST_CODE_TAKE_PHOTO);
            } else {
                Uri imageUri = Uri.fromFile(takePhotoFile);
                openCameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(openCameraIntent, REQUEST_CODE_TAKE_PHOTO);
            }
//            filePath = getFilePath(System.currentTimeMillis() + ".jpg");
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(takePhotoFile));
//            startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
        }
    }

    protected void startPhotoPicker() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"), REQUEST_CODE_PICK_PHOTO);
        }
    }

    protected void startLocationChooser() {
        Toast.makeText(getActivity(),"未实现",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        System.out.println("onActivityResult");
        if (data == null && requestCode != REQUEST_CODE_TAKE_PHOTO) {
            return;
        }
        if (REQUEST_CODE_TAKE_PHOTO == requestCode) {
            if (null != inputChatListener) {
                if(takePhotoFile.length() != 0){
                    inputChatListener.onTakePhotoMessage(filePath);
                }
            }
        } else if (REQUEST_CODE_PICK_PHOTO == requestCode) {
            if (null != inputChatListener) {
                inputChatListener.onPickPhotoMessage(data);
            }
        } else if (REQUEST_CODE_TAKE_MEDIA == requestCode) {
            if (null != inputChatListener) {
                inputChatListener.onPickLocMessage(data);
            }
        } else if (REQUEST_CODE_PICK_FILE == requestCode) {
            if (null != inputChatListener) {
                System.out.println("data  "+ data);
                inputChatListener.onPickFileMessage(data);
            }
        }
        this.hideBoxViewMode();
    }

    private String getFilePath(String filename) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(path + "/gnway/xmppIm/allfiles");
        if (!file.exists()) {
            file.mkdirs();
        }
        String resultPath = file.getAbsolutePath() + "/" + filename;
        return resultPath;
    }
}
