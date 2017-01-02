package com.ihunter.taskee.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;

import com.ihunter.taskee.R;

/**
 * Created by Master Bison on 12/23/2016.
 */

public class CustomRadioButton extends AppCompatRadioButton {

    private ColorStateList radioButtonDefaultColor;
    private TypedArray colors;

    public CustomRadioButton(Context context) {
        super(context);
    }

    public CustomRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs){
        radioButtonDefaultColor = ContextCompat.getColorStateList(context, R.color.light_gray);
        setSupportButtonTintList(radioButtonDefaultColor);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,R.styleable.CustomTextView, 0, 0);
        colors = context.getTheme().obtainStyledAttributes(attrs,R.styleable.CustomRadioButton, 0, 0);

        String typefaceName = a.getString(R.styleable.CustomTextView_fontName);

        if (typefaceName != null && !typefaceName.equals("")) {
            setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/" + typefaceName));
        }

        a.recycle();
    }

    public void toggleState(){
        if(isChecked()){
            setSupportButtonTintList(colors.getColorStateList(R.styleable.CustomRadioButton_checked_color));
            setTextColor(colors.getColor(R.styleable.CustomRadioButton_checked_color, ContextCompat.getColor(getContext(), R.color.light_gray)));
        }else{
            setSupportButtonTintList(radioButtonDefaultColor);
            setTextColor(ContextCompat.getColor(getContext(), R.color.light_gray));
        }
    }

}
