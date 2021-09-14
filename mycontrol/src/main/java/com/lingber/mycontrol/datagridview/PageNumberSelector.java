package com.lingber.mycontrol.datagridview;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lingber.mycontrol.R;

import java.lang.reflect.Method;

/**
 * @Author :ShuboLin
 * @CreatTime: 2021-03-02 9:43.
 * @Description: 页码选择器
 */

public class PageNumberSelector extends DialogFragment implements View.OnClickListener{

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

    private EditText mEditText;
    private TextView mTvLastPageNumber;
    private ImageButton mBtnReturn;
    private ImageButton mBtnOk;

    private Integer lastPageNumber;
    private Integer currentPageNumber;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog mDialog = new Dialog(getActivity());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.lingber_pagenumber_selector);

        // 允许点击其它空白地方消失弹窗
        mDialog.setCanceledOnTouchOutside(true);

        Window window = mDialog.getWindow();
        WindowManager.LayoutParams layoutParams;
        if (window != null) {
            window.getDecorView().setPadding(0, 0, 0, 0);
            layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.CENTER;
            layoutParams.height = 500;
            layoutParams.width = 850;
            window.setAttributes(layoutParams);
            // 必须
            window.getDecorView().setBackgroundColor(Color.GREEN);
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }

        // 获取参数
        lastPageNumber = getArguments().getInt("lastPageNumber");
        currentPageNumber = getArguments().getInt("currentPageNumber");
        if(lastPageNumber == null){
            lastPageNumber = 0;
        }
        if(currentPageNumber == null){
            currentPageNumber = 0;
        }

        doBinding(mDialog);

        // 绑定EditText键盘
        bindKeyBoard();

        return mDialog;
    }

    /** 绑定控件**/
    private void doBinding(Dialog mDialog){
        mEditText = (EditText) mDialog.findViewById(R.id.et_barcode_input);
        mTvLastPageNumber = (TextView) mDialog.findViewById(R.id.tv_last_page_number);
        mBtnReturn = (ImageButton) mDialog.findViewById(R.id.btn_return);
        mBtnOk = (ImageButton) mDialog.findViewById(R.id.btn_ok);
        mTvKey1 = (TextView) mDialog.findViewById(R.id.tv_key_1);
        mTvKey2 = (TextView) mDialog.findViewById(R.id.tv_key_2);
        mTvKey3 = (TextView) mDialog.findViewById(R.id.tv_key_3);
        mTvKey4 = (TextView) mDialog.findViewById(R.id.tv_key_4);
        mTvKey5 = (TextView) mDialog.findViewById(R.id.tv_key_5);
        mTvKey6 = (TextView) mDialog.findViewById(R.id.tv_key_6);
        mTvKey7 = (TextView) mDialog.findViewById(R.id.tv_key_7);
        mTvKey8 = (TextView) mDialog.findViewById(R.id.tv_key_8);
        mTvKey9 = (TextView) mDialog.findViewById(R.id.tv_key_9);
        mTvKey0 = (TextView) mDialog.findViewById(R.id.tv_key_0);
        mTvKeyBackspacing = (ImageView) mDialog.findViewById(R.id.iv_backspace);

        mBtnReturn.setOnClickListener(this);
        mBtnOk.setOnClickListener(this);
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
        mTvKeyBackspacing.setOnClickListener(this);
        mBtnReturn.setTag("return");
        mBtnOk.setTag("ok");
        mTvKeyBackspacing.setTag("backspacing");
        mTvLastPageNumber.setText(lastPageNumber+"");
        mEditText.setText(currentPageNumber+"");

        // 回退长按事件
        mTvKeyBackspacing.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(mEditText!=null){
                    mEditText.setText("");
                    mEditText.setSelection(mEditText.getText().length());
                }
                return false;
            }
        });

        // 页码最大输入值限制
        if(lastPageNumber == 0){
            mEditText.setEnabled(false);
            mBtnOk.setVisibility(View.GONE);
        }

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String text = mEditText.getText().toString();

                if(text.isEmpty()){
                    return;
                }

                if(Integer.valueOf(text)>lastPageNumber){
                    mEditText.setText(lastPageNumber+"");
                }

                if(Integer.valueOf(text) == 0){
                    mEditText.setText("");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if("backspacing".equals(v.getTag())){
            String text = mEditText.getText().toString();
            if(text.length()==0){
                return;
            }
            mEditText.setText(text.substring(0, text.length()-1));
            mEditText.setSelection(mEditText.getText().length());
            return;
        }

        if("ok".equals(v.getTag())){
            String text = mEditText.getText().toString();
            if(text.isEmpty()){
                return;
            }

            currentPageNumber = Integer.valueOf(text);

            if(mOnGetPageNumberListener!=null){
                mOnGetPageNumberListener.getPageNumber(currentPageNumber);
            }

            dismiss();

            return;
        }

        if("return".equals(v.getTag())){
            dismiss();
            return;
        }

        if(lastPageNumber == 0){
            return;
        }

        String text = mEditText.getText().toString();
        TextView textView = (TextView) v;
        mEditText.setText(text+textView.getText().toString());
        mEditText.setSelection(mEditText.getText().length());
    }

    /** 绑定小键盘**/
    private void bindKeyBoard(){

        // 禁止系统自带键盘弹出
        Class<EditText> cls = EditText.class;
        Method method;
        try {
            method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            method.setAccessible(true);
            method.invoke(mEditText, false);
        } catch (Exception e) {
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /** 选择页码回调**/
    private OnGetPageNumberListener mOnGetPageNumberListener;

    public void setOnGetPageNumberListener(OnGetPageNumberListener getPageNumberListener){
        this.mOnGetPageNumberListener = getPageNumberListener;
    }

    public interface OnGetPageNumberListener {
        void getPageNumber(int pageNumber);
    }

}
