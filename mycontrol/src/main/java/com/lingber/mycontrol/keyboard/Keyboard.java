package com.lingber.mycontrol.keyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.lingber.mycontrol.R;

import java.lang.reflect.Method;

/**
 * @Author :ShuboLin
 * @CreatTime: 2020-06-04 10:34.
 * @Description: 键盘
 */

public class Keyboard extends LinearLayout implements View.OnClickListener{

    private Context mContext;
    private View mView;

    private EditText mEditText = null;

    private EditText[] arrEditText = new EditText[]{};

    private TextView mTvKey1;
    private TextView mTvKey2;
    private TextView mTvKey3;
    private TextView mTvKey4;
    private TextView mTvKey5;
    private TextView mTvKey6;
    private TextView mTvKey7;
    private TextView mTvKey8;
    private TextView mTvKey9;
    private TextView mTvKey0;
    private ImageView mTvKeyBackspacing;
    private TextView mTvKeyHash;

    public Keyboard(Context context) {
        super(context);
    }

    public Keyboard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Keyboard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        this.mContext = context;
        this.mView = LayoutInflater.from(mContext).inflate(R.layout.lingber_keyboard_template, this);
        // 获取控件属性
        //TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.NumberController);
        // 设置配置属性
        //setmRang(a.getInt(R.styleable.NumberController_range, 1));
        // 绑定控件
        doBinding();
    }

    /** 绑定控件**/
    private void doBinding(){
        mTvKey1 = (TextView) mView.findViewById(R.id.tv_key_1);
        mTvKey2 = (TextView) mView.findViewById(R.id.tv_key_2);
        mTvKey3 = (TextView) mView.findViewById(R.id.tv_key_3);
        mTvKey4 = (TextView) mView.findViewById(R.id.tv_key_4);
        mTvKey5 = (TextView) mView.findViewById(R.id.tv_key_5);
        mTvKey6 = (TextView) mView.findViewById(R.id.tv_key_6);
        mTvKey7 = (TextView) mView.findViewById(R.id.tv_key_7);
        mTvKey8 = (TextView) mView.findViewById(R.id.tv_key_8);
        mTvKey9 = (TextView) mView.findViewById(R.id.tv_key_9);
        mTvKey0 = (TextView) mView.findViewById(R.id.tv_key_0);
        mTvKeyBackspacing = (ImageView) mView.findViewById(R.id.iv_backspace);
        mTvKeyHash = (TextView) mView.findViewById(R.id.tv_key_hash);

        mTvKey1.setOnClickListener(this);
        mTvKey2.setOnClickListener(this);
        mTvKey3.setOnClickListener(this);
        mTvKey4.setOnClickListener(this);
        mTvKey5.setOnClickListener(this);
        mTvKey6.setOnClickListener(this);
        mTvKey7.setOnClickListener(this);
        mTvKey8.setOnClickListener(this);
        mTvKey9.setOnClickListener(this);
        mTvKey0.setOnClickListener(this);
        mTvKeyHash.setOnClickListener(this);
        mTvKeyBackspacing.setOnClickListener(this);
        mTvKeyBackspacing.setTag("backspacing");
        // 回退长按事件
        mTvKeyBackspacing.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(mEditText!=null){
                    mEditText.setText("");
                    mEditText.setSelection(mEditText.getText().length());
                }
                return false;
            }
        });

    }

    /** 设置当前mEditText**/
    public void setEditText(EditText editText){
        this.mEditText = editText;
    }

    /** 设置绑定小键盘的EditText**/
    public void setArrEditText(EditText[] arrEditText){
        if(arrEditText==null){
            return;
        }
        this.arrEditText = arrEditText;
        // 绑定EditText键盘
        bindKeyBoard();
    }

    @Override
    public void onClick(View v) {
        if(mEditText!=null){
            if("backspacing".equals(v.getTag())){
                String text = mEditText.getText().toString();
                if(text.length()==0){
                    return;
                }
                mEditText.setText(text.substring(0, text.length()-1));
                mEditText.setSelection(mEditText.getText().length());
                return;
            }
            String text = mEditText.getText().toString();
            TextView textView = (TextView) v;
            mEditText.setText(text+textView.getText().toString());
            mEditText.setSelection(mEditText.getText().length());
        }
    }

    /** 绑定小键盘**/
    private void bindKeyBoard(){
        // 监听焦点状态改变
        for(int i=0;i<arrEditText.length;i++){
            arrEditText[i].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        mEditText = (EditText) v;
                    }
                }
            });
        }

        // 禁止系统自带键盘弹出
        Class<EditText> cls = EditText.class;
        Method method;
        try {
            method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            method.setAccessible(true);
            for(int i=0;i<arrEditText.length;i++){
                method.invoke(arrEditText[i], false);
            }
        } catch (Exception e) {
        }
    }
}
