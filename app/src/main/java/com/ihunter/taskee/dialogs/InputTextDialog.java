package com.ihunter.taskee.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.ihunter.taskee.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Master Bison on 12/7/2016.
 */

public class InputTextDialog extends Dialog {

    @BindView(R.id.top_dialog)
    LinearLayout topDialog;

    @BindView(R.id.input_dialog_text)
    AppCompatEditText inputDialogText;

    @BindView(R.id.negative_dialog)
    AppCompatTextView negativeDialog;

    @BindView(R.id.positive_dialog)
    AppCompatTextView positiveDialog;

    @BindView(R.id.dialog_title)
    AppCompatTextView dialogTitle;

    @BindView(R.id.dialog_icon)
    AppCompatImageView dialogIcon;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public InputTextDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_input);
        this.context = context;
        ButterKnife.bind(this);
    }

    public InputTextDialog setIcon(int icon){
        dialogIcon.setImageResource(icon);
        return this;
    }

    public InputTextDialog setIconColor(int color){
        dialogIcon.setColorFilter(color);
        return this;
    }

    public InputTextDialog setTitle(String text){
        dialogTitle.setText(text);
        dialogTitle.setVisibility(View.VISIBLE);
        return this;
    }

    public InputTextDialog setInputTextColor(int textColor){
        inputDialogText.setTextColor(textColor);
        return this;
    }

    public InputTextDialog setInputHintColor(int textColor){
        inputDialogText.setHintTextColor(textColor);
        return this;
    }

    public InputTextDialog setInputHint(String hint){
        inputDialogText.setHint(hint);
        return this;
    }

    public InputTextDialog setNegativeButton(String text, DialogInterface dialogReader){
        negativeDialog.setVisibility(View.VISIBLE);
        negativeDialog.setText(text);
        negativeDialog.setOnClickListener(new NegativeButtonListener(dialogReader));
        return this;
    }

    public InputTextDialog setPositiveButtonColor(int textColor){

        return this;
    }

    public InputTextDialog setPositiveButton(String text, OnTextInputConfirmListener listener) {
        positiveDialog.setText(text);
        positiveDialog.setOnClickListener(new TextInputListener(listener));
        return this;
    }

    public InputTextDialog setTopColorRes(int color){
        topDialog.setBackgroundColor(color);
        return this;
    }

    public class NegativeButtonListener implements View.OnClickListener{

        DialogInterface dialogReader;

        public NegativeButtonListener(DialogInterface dialogReader){
            this.dialogReader = dialogReader;
        }

        @Override
        public void onClick(View v) {
            dialogReader.onDialogInterface(InputTextDialog.this);
        }
    }

    public class TextInputListener implements View.OnClickListener{

        private OnTextInputConfirmListener wrapped;

        public TextInputListener(OnTextInputConfirmListener wrapped){
            this.wrapped = wrapped;
        }


        @Override
        public void onClick(View v) {
            String text = inputDialogText.getText().toString();
            if(wrapped != null){
                wrapped.onTextInputConfirmed(text, InputTextDialog.this);
            }
        }
    }

    public interface DialogInterface{
        void onDialogInterface(Dialog dialog);
    }

    public interface OnTextInputConfirmListener {
        void onTextInputConfirmed(String text, Dialog dialog);
    }

}
