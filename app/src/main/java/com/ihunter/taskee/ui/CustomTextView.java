package com.ihunter.taskee.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.ihunter.taskee.R;

/**
 * Created by Master Bison on 12/18/2016.
 */

public class CustomTextView extends AppCompatTextView {

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs){
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CustomTextView, 0, 0);

        String typefaceName = a.getString(R.styleable.CustomTextView_fontName);

        if (typefaceName != null && !typefaceName.equals("")) {
            setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/" + typefaceName));
        }

        a.recycle();
    }

}
