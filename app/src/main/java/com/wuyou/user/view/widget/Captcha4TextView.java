package com.wuyou.user.view.widget;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wuyou.user.R;

/**
 * Created by hjn on 2017/8/10.
 */

public class Captcha4TextView extends RelativeLayout {
    private EditText editText;
    private TextView[] textViews;
    private StringBuffer stringBuffer;
    private int count;
    private String strPassword;
    private Captcha4TextView.InputCompleteListener inputCompleteListener;

    public Captcha4TextView(Context context) {
        this(context, null);
    }

    public Captcha4TextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Captcha4TextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.stringBuffer = new StringBuffer();
        this.count = 4;
        this.textViews = new TextView[6];
        View.inflate(context, R.layout.custom_captcha_4, this);
        this.editText = this.findViewById(R.id.item_edittext);
        this.textViews[0] = this.findViewById(R.id.item_password_iv1);
        this.textViews[1] = this.findViewById(R.id.item_password_iv2);
        this.textViews[2] = this.findViewById(R.id.item_password_iv3);
        this.textViews[3] = this.findViewById(R.id.item_password_iv4);
        this.editText.setCursorVisible(false);
        this.setListener();
    }

    public void showKeyBoard() {
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                InputMethodManager m = (InputMethodManager) Captcha4TextView.this.editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                m.showSoftInput(Captcha4TextView.this.editText, 2);
            }
        }, 500L);
    }

    public void dismissKeyBoard() {
        InputMethodManager m = (InputMethodManager) this.editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        m.hideSoftInputFromWindow(this.editText.getWindowToken(), 0);
    }

    private void setListener() {
        this.editText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    if (stringBuffer.length() > 3) {
                        editText.setText("");
                        return;
                    }

                    stringBuffer.append(editable);
                    editText.setText("");
                    count = stringBuffer.length();
                    strPassword = stringBuffer.toString();
                    if (stringBuffer.length() == 4 && inputCompleteListener != null) {
                        inputCompleteListener.inputComplete();
                    }

                    for (int i = 0; i < stringBuffer.length(); ++i) {
                        textViews[i].setText(stringBuffer.charAt(i) + "");
                    }
                }

            }
        });
        this.editText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return (keyCode == 67 && event.getAction() == 0) && (Captcha4TextView.this.onKeyDelete() ? true : true);
            }
        });
    }

    public boolean onKeyDelete() {
        if (this.count == 0) {
            this.count = 4;
            return true;
        } else {
            if (this.stringBuffer.length() > 0) {
                this.stringBuffer.delete(this.count - 1, this.count);
                --this.count;
                this.strPassword = this.stringBuffer.toString();
                this.textViews[this.stringBuffer.length()].setText("");
            }
            return false;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    public void setInputCompleteListener(Captcha4TextView.InputCompleteListener inputCompleteListener) {
        this.inputCompleteListener = inputCompleteListener;
    }

    public EditText getEditText() {
        return this.editText;
    }

    public String getStrPassword() {
        return this.strPassword;
    }

    public void setContent(String content) {
        this.editText.setText(content);
    }

    public void clear() {
        for (int i = 0; i < 4; ++i) {
            this.textViews[i].setText("");
        }

        this.stringBuffer = new StringBuffer();
        this.strPassword = "";
    }

    public interface InputCompleteListener {
        void inputComplete();
    }
}
