package com.ihunter.taskee.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.ihunter.taskee.Constants;
import com.ihunter.taskee.R;
import com.ihunter.taskee.activities.MainActivity;
import com.ihunter.taskee.animations.Animations;
import com.ihunter.taskee.data.Task;
import com.ihunter.taskee.dialogs.CustomTimeDialog;
import com.ihunter.taskee.interfaces.views.TaskEditorView;
import com.ihunter.taskee.presenters.QuickAddPresenter;
import com.mikepenz.iconics.view.IconicsImageView;

import java.util.Calendar;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static android.view.View.GONE;
import static com.ihunter.taskee.Constants.REVEAL_DURATION;
import static com.ihunter.taskee.R.id.edit_task_alarm;
import static com.ihunter.taskee.R.id.edit_task_date;
import static com.ihunter.taskee.R.id.edit_task_date_wrapper;
import static com.ihunter.taskee.R.id.edit_task_note;
import static com.ihunter.taskee.R.id.edit_task_remove_alarm;
import static com.ihunter.taskee.R.id.edit_task_save_task;
import static com.ihunter.taskee.R.id.edit_task_title;
import static com.ihunter.taskee.R.id.main_view;
import static com.ihunter.taskee.R.id.reveal_layout;
import static com.ihunter.taskee.R.id.view_bubble;

public class QuickAddFragment extends Fragment implements ViewTreeObserver.OnGlobalLayoutListener, TaskEditorView {

    private Animations animations;
    private QuickAddPresenter presenter;
    private Task task;
    private boolean shouldAnimate = true;

    @BindView(reveal_layout)
    View container;

    @BindView(main_view)
    View mainView;

    @BindView(view_bubble)
    View bubble;

    @BindView(edit_task_date_wrapper)
    View editTaskDateWrapper;

    @BindView(edit_task_save_task)
    View editTaskSave;

    @BindView(edit_task_title)
    EditText editTaskTitle;

    @BindView(edit_task_note)
    EditText editTaskNote;

    @BindView(edit_task_alarm)
    IconicsImageView editTaskAlarm;

    @BindView(edit_task_date)
    TextView editTaskDate;

    @OnTextChanged(edit_task_title)
    void onEditTaskTitleTextChange(SpannableStringBuilder builder) {
        task.setTitle(builder.toString());
        if (!task.getTitle().trim().isEmpty()) {
            editTaskSave.setAlpha(1f);
        } else {
            editTaskSave.setAlpha(0.5f);
        }
    }

    @OnTextChanged(edit_task_note)
    void onEditTaskNoteTextChange(SpannableStringBuilder builder) {
        task.setNote(builder.toString());
    }


    @OnClick(edit_task_date)
    void pickDateTime(){
        CustomTimeDialog timeDialog = new CustomTimeDialog(getActivity(), task.getTimestamp());
        timeDialog.setTaskEditorPresenter(this);
        timeDialog.show();
    }

    @OnClick(edit_task_remove_alarm)
    void removeAlarm(){
        editTaskDateWrapper.setVisibility(GONE);
        editTaskAlarm.setColor(ContextCompat.getColor(getContext().getApplicationContext(), R.color.quick_action_color));
    }


//    @OnClick(edit_task_set_alarm)
//    void onSetAlarmClick() {
//        CustomTimeDialog timeDialog = new CustomTimeDialog(getActivity(), task.getTimestamp());
//        timeDialog.setTaskEditorPresenter(this);
//        timeDialog.show();
//    }

    @OnClick(edit_task_save_task)
    void onSaveClick() {
        presenter.saveTask(task);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quick_add, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new QuickAddPresenter(this);
        ButterKnife.bind(this, view);
        animations = new Animations();
        container.getViewTreeObserver().addOnGlobalLayoutListener(this);
        Bundle bundle = getArguments();
        if (bundle != null) {
//            presenter.editTask(bundle.getInt("item_id"));
//            setToolbarTitle(getString(R.string.line_edit_task));
        } else {
            task = new Task();
            onNewTask();
        }

    }

    @Override
    public void onGlobalLayout() {
        container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        if (shouldAnimate) {
            circularReveal(true);
        }
    }

    void circularReveal(boolean animateIn) {
        final View view = mainView;
        bubble.setVisibility(View.VISIBLE);
        float[] centerPosition = animations.getCenterOfView(view);
        float finalRadius = animations.getCircularRevalRadius(view);
        if (animateIn) {
            final Animator animator = animations.circularReveal(view, centerPosition[0], centerPosition[1], finalRadius, true);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animator.removeListener(this);
                    bubble.animate()
                            .alpha(0f)
                            .setDuration(REVEAL_DURATION)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    bubble.animate().setListener(null);
                                    bubble.setVisibility(GONE);
                                    editTaskTitle.requestFocus();
                                    InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    mgr.showSoftInput(editTaskTitle, InputMethodManager.SHOW_IMPLICIT);
                                }
                            });
                }
            });
            animator.start();
        } else {
            final Animator animator2 = animations.circularReveal(view, centerPosition[0], centerPosition[1], finalRadius, false);
            animator2.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animation.removeListener(this);
                    view.setVisibility(GONE);
                    internalClose();
                }
            });
            bubble.setVisibility(View.VISIBLE);
            bubble.animate()
                    .alpha(1f)
                    .setDuration(REVEAL_DURATION)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            bubble.animate().setListener(null);
                            animator2.start();
                        }
                    });
        }
    }

    private void internalClose() {
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        getActivity().getSupportFragmentManager().popBackStack();
        ((MainActivity) getActivity()).quickAddClosed();
    }

    public void closeFragment() {
        shouldAnimate = false;
        circularReveal(false);
    }

    private boolean isKitKatOrLower() {
        return Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT;
    }

    public void onNewTask() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.HOUR, 12);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        task.setTimestamp(calendar.getTimeInMillis());
        task.setHasReminder(true);

        int[] androidColors = getResources().getIntArray(R.array.task_colors);
        int selectedColor = androidColors[new Random().nextInt(androidColors.length)];
        task.setColor(Integer.toHexString(selectedColor));
        Drawable background = mainView.getBackground();
        if(background instanceof GradientDrawable){
            GradientDrawable shapeDrawable = (GradientDrawable) background;
            shapeDrawable.setColor(selectedColor);
        }
        Drawable background2 = editTaskSave.getBackground();
        if(background instanceof GradientDrawable){

        }
        editTaskDate.setText(Constants.getFullDateTime(task.getTimestamp()));
        editTaskAlarm.setColor(ContextCompat.getColor(getContext().getApplicationContext(), R.color.colorAccent));
        editTaskSave.setAlpha(0.5f);
    }

    public static int getContrastColor(int color) {
        double y = (299 * Color.red(color) + 587 * Color.green(color) + 114 * Color.blue(color)) / 1000;
        return y >= 128 ? Color.BLACK : Color.WHITE;
    }


    @Override
    public void onTaskValidationError(Constants.ValidationError validationError) {

    }

    @Override
    public void onTaskSaveSuccessful(int id) {

    }

    @Override
    public void onTaskForEditorMode(Task plan) {

    }

    @Override
    public void onTaskDateSet(long time) {
        task.setTimestamp(time);
        editTaskDate.setText(Constants.getFullDateTime(time));
    }

    @Override
    public void onSubTaskAdded() {

    }
}
