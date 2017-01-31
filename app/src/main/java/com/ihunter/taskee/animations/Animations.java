package com.ihunter.taskee.animations;

import android.animation.Animator;
import android.os.Build;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import io.codetail.animation.ViewAnimationUtils;

import static com.ihunter.taskee.Constants.REVEAL_DURATION;

public class Animations {

    public float[] getCenterOfView(View v){
        float[] positions = new float[2];
        float x = v.getX() + v.getWidth() / 2;
        float y = v.getY() + v.getHeight() / 2;
        positions[0] = x;
        positions[1] = y;
        // index 0 = x
        // index 1 = y
        return positions;
    }

    public float getCircularRevalRadius(View v){
        int cx = (v.getLeft() + v.getRight()) / 2;
        int cy = (v.getTop() + v.getBottom()) / 2;
        int dx = Math.max(cx, v.getWidth() - cx);
        int dy = Math.max(cy, v.getWidth() - cy);
        return (float) Math.hypot(dx, dy);
    }

    public Animator circularReveal(View v, float x, float y, float radius, boolean animateIn){
        Animator animator;
        animator = ViewAnimationUtils.createCircularReveal(v, (int)x, (int)y, animateIn ? 0 : radius, animateIn ? radius : 0);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(REVEAL_DURATION);
        return animator;
    }

    public float[] transformCenter(View parentView, View objectView){
        final float[] centerOfParentViewXY = getCenterOfView(parentView);
        float objectX = centerOfParentViewXY[0] - objectView.getWidth() / 2 - objectView.getX();
        float objectY = centerOfParentViewXY[1] - objectView.getHeight() / 2 - objectView.getY();
        return new float[]{objectX, objectY};
    }

    private boolean isKitKatOrLower(){
        return Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT;
    }

}
