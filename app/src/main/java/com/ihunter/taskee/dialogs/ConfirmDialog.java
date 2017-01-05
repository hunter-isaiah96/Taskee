package com.ihunter.taskee.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.ihunter.taskee.R;
import com.ihunter.taskee.ui.CustomTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Master Bison on 12/7/2016.
 */

public class ConfirmDialog extends Dialog {

    @BindView(R.id.top_dialog)
    LinearLayout topDialog;

    @BindView(R.id.negative_dialog)
    CustomTextView negativeDialog;

    @BindView(R.id.positive_dialog)
    CustomTextView positiveDialog;

    @BindView(R.id.dialog_title)
    CustomTextView dialogTitle;

    @BindView(R.id.dialog_icon)
    AppCompatImageView dialogIcon;

    Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public ConfirmDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_confirm);
        this.context = context;
        ButterKnife.bind(this);
    }

    public ConfirmDialog setIcon(int icon){
        dialogIcon.setImageResource(icon);
        return this;
    }

    public ConfirmDialog setIconColor(int color){
        dialogIcon.setColorFilter(color);
        return this;
    }

    public ConfirmDialog setTitle(String text){
        dialogTitle.setText(text);
        dialogTitle.setVisibility(View.VISIBLE);
        return this;
    }

    public ConfirmDialog setNegativeButton(String text, OnNegativeButton listener){
        negativeDialog.setVisibility(View.VISIBLE);
        negativeDialog.setText(text);
        negativeDialog.setOnClickListener(new NegativeButtonListener(listener));
        return this;
    }

    public ConfirmDialog setPositiveButtonColor(int textColor){
        return this;
    }

    public ConfirmDialog setPositiveButton(String text, OnPositiveButton listener) {
        positiveDialog.setText(text);
        positiveDialog.setOnClickListener(new PositiveButtonListener(listener));
        return this;
    }

    public ConfirmDialog setTopColorRes(int color){
        topDialog.setBackgroundColor(color);
        return this;
    }

    public class NegativeButtonListener implements View.OnClickListener{

         OnNegativeButton wrapped;

        public NegativeButtonListener(OnNegativeButton wrapped){
            this.wrapped = wrapped;
        }

        @Override
        public void onClick(View v) {
            wrapped.onClick(v, ConfirmDialog.this);
        }
    }

    public class PositiveButtonListener implements View.OnClickListener{

        private OnPositiveButton wrapped;

        public PositiveButtonListener(OnPositiveButton wrapped){
            this.wrapped = wrapped;
        }


        @Override
        public void onClick(View v) {
            wrapped.onClick(v, ConfirmDialog.this);
        }
    }

    public interface OnNegativeButton{
        void onClick(View v, Dialog dialog);
    }

    public interface OnPositiveButton {
        void onClick(View v, Dialog dialog);
    }

}
