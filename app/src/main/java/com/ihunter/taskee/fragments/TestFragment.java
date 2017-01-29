package com.ihunter.taskee.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.ihunter.taskee.R;
import com.ihunter.taskee.activities.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.codetail.animation.ViewAnimationUtils;

import static com.ihunter.taskee.Constants.REVEAL_DURATION;
import static com.ihunter.taskee.R.id.edit_task_title;
import static com.ihunter.taskee.R.id.main_view;
import static com.ihunter.taskee.R.id.root_view;
import static com.ihunter.taskee.R.id.view_bubble;

public class TestFragment extends Fragment implements ViewTreeObserver.OnGlobalLayoutListener {

    @BindView(main_view)
    View mainView;

    @BindView(root_view)
    View container;

    @BindView(view_bubble)
    View bubble;

    @BindView(edit_task_title)
    EditText editTaskTitle;

    private boolean shouldAnimate = true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quick_add, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        container.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }
    void reveal(boolean animateIn){
        final Animator animator;
        final View view = mainView;

        int cx = (view.getLeft() + view.getRight() / 2);
        int cy = (view.getTop() + view.getBottom() / 2);

        int dx = Math.max(cx, view.getWidth() - cx);
        int dy = Math.max(cy, view.getHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);

        if(animateIn){
            animator = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(REVEAL_DURATION);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    bubble.animate().alpha(0f).setDuration(REVEAL_DURATION).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            animation.removeListener(this);
                            bubble.setVisibility(View.GONE);
                            editTaskTitle.requestFocus();
                            InputMethodManager mgr = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            mgr.showSoftInput(editTaskTitle, InputMethodManager.SHOW_IMPLICIT);
                        }
                    });
                }
            });
            animator.start();
        }else{
            animator = ViewAnimationUtils.createCircularReveal(view, cx, cy, finalRadius, 0);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(REVEAL_DURATION);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animation.removeListener(this);
                    view.setVisibility(View.GONE);
                    internalClose();
                }
            });
            bubble.setVisibility(View.VISIBLE);
            bubble.animate().alpha(1f).setDuration(REVEAL_DURATION).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animation.removeListener(this);
                    animator.start();
                }
            });
        }
    }

    @Override
    public void onGlobalLayout() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            container.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        } else {
            container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
        if(shouldAnimate)
        reveal(true);
    }

    private void internalClose(){
        getActivity().getSupportFragmentManager().beginTransaction().remove(TestFragment.this).commit();
        getActivity().getSupportFragmentManager().popBackStack();
        ((MainActivity)getActivity()).quickAddClosed();
//        backgroundColorAnimator.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                getActivity().getSupportFragmentManager().beginTransaction().remove(TestFragment.this).commit();
//                getActivity().getSupportFragmentManager().popBackStack();
//                ((MainActivity)getActivity()).quickAddClosed();
//            }
//        });
//        backgroundColorAnimator.reverse();
    }

    public void closeFragment(){
        shouldAnimate = false;
        reveal(false);
    }

}
