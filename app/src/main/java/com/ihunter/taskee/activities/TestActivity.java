package com.ihunter.taskee.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import com.ihunter.taskee.R;
import com.ihunter.taskee.animations.Animations;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.codetail.widget.RevealFrameLayout;

import static com.ihunter.taskee.R.id.main_view;
import static com.ihunter.taskee.R.id.root_view;

public class TestActivity extends Activity implements ViewTreeObserver.OnGlobalLayoutListener {

    @BindView(root_view)
    RevealFrameLayout container;

    @BindView(main_view)
    FrameLayout layout;

    @BindView(R.id.plan)
    View planView;

    private Animations animations;
    boolean shouldAnimate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        ButterKnife.bind(this);
//        container.getViewTreeObserver().addOnGlobalLayoutListener(this);
        animations = new Animations();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void circularReveal(boolean animateIn){
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.width = (int) getResources().getDimension(R.dimen.fab_size);
        layoutParams.height = (int) getResources().getDimension(R.dimen.fab_size);
        int transitionID = R.transition.element_transition;
        TransitionInflater inflater = TransitionInflater.from(this);
        Transition transition = inflater.inflateTransition(transitionID);
        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
            }

            @Override
            public void onTransitionEnd(Transition transition) {
//                if (animateIn) {
//                    newPlan.animate()
//                            .scaleX(0f)
//                            .scaleY(0f)
//                            .setDuration(REVEAL_DURATION)
//                            .setInterpolator(new OvershootInterpolator())
//                            .setListener(new AnimatorListenerAdapter() {
//                                @Override
//                                public void onAnimationEnd(Animator animation) {
//                                    super.onAnimationEnd(animation);
//                                    newPlan.animate().setListener(null);
//                                    newPlan.setVisibility(GONE);
//                                }
//                            })
//                            .start();
//                    openQuickAddActivity();
//                }
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
        TransitionManager.beginDelayedTransition(container, transition);

        if (animateIn) {
            layoutParams.gravity = Gravity.CENTER;
        } else {
            int margin = (int) getResources().getDimension(R.dimen.fab_margin);
            layoutParams.gravity = Gravity.BOTTOM | Gravity.END;
            layoutParams.setMargins(margin, margin, margin, margin);
        }
        planView.setLayoutParams(layoutParams);
//        float[] centerPosition = animations.getCenterOfView(layout);
//        float finalRadius = animations.getCircularRevalRadius(layout);
//        if (animateIn) {
//            final Animator animator = animations.circularReveal(layout, centerPosition[0], centerPosition[1], finalRadius, true);
//            animator.addListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    super.onAnimationEnd(animation);
//                    animator.removeListener(this);
////                    bubble.animate()
////                            .alpha(0f)
////                            .setDuration(REVEAL_DURATION)
////                            .setListener(new AnimatorListenerAdapter() {
////                                @Override
////                                public void onAnimationEnd(Animator animation) {
////                                    super.onAnimationEnd(animation);
////                                    bubble.animate().setListener(null);
////                                    bubble.setVisibility(View.GONE);
////                                    editTaskTitle.requestFocus();
////                                    InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
////                                    mgr.showSoftInput(editTaskTitle, InputMethodManager.SHOW_IMPLICIT);
////                                }
////                            });
//                }
//            });
//            animator.start();
        }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (shouldAnimate) {
            circularReveal(true);
        }
    }

    @Override
    public void onGlobalLayout() {
        if (shouldAnimate) {
            circularReveal(true);
        }
    }
}
