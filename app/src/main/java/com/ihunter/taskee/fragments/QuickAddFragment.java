package com.ihunter.taskee.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
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
import com.ihunter.taskee.TaskeeApplication;
import com.ihunter.taskee.animations.Animations;
import com.ihunter.taskee.data.Task;
import com.ihunter.taskee.dialogs.CustomTimeDialog;
import com.ihunter.taskee.interfaces.interactors.QuickAddInteractor;
import com.ihunter.taskee.interfaces.views.TaskEditorView;
import com.ihunter.taskee.presenters.QuickAddPresenter;
import com.ihunter.taskee.utils.ColorUtils;
import com.mikepenz.iconics.view.IconicsImageView;
import com.thebluealliance.spectrum.SpectrumDialog;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.ihunter.taskee.Constants.REVEAL_DURATION;
import static com.ihunter.taskee.R.id.edit_task_alarm;
import static com.ihunter.taskee.R.id.edit_task_date;
import static com.ihunter.taskee.R.id.edit_task_date_on;
import static com.ihunter.taskee.R.id.edit_task_date_wrapper;
import static com.ihunter.taskee.R.id.edit_task_note;
import static com.ihunter.taskee.R.id.edit_task_pick_color;
import static com.ihunter.taskee.R.id.edit_task_remove_alarm;
import static com.ihunter.taskee.R.id.edit_task_save_task;
import static com.ihunter.taskee.R.id.edit_task_title;
import static com.ihunter.taskee.R.id.main_view;
import static com.ihunter.taskee.R.id.reveal_layout;
import static com.ihunter.taskee.R.id.view_bubble;

public class QuickAddFragment extends Fragment implements ViewTreeObserver.OnGlobalLayoutListener, TaskEditorView {

    private QuickAddInteractor quickAddInteractor;
    private Animations animations;
    private QuickAddPresenter presenter;
    private Task task;
    private boolean shouldAnimate = true;
    private int[] androidColors;
    private int selectedColor;

    /* VIEWS */

    @BindView(reveal_layout)
    View container;

    @BindView(main_view)
    View mainView;

    @BindView(view_bubble)
    View bubble;

    @BindView(edit_task_date_wrapper)
    View editTaskDateWrapper;

    /* EDITTEXT */

    @BindView(edit_task_title)
    EditText editTaskTitle;

    @BindView(edit_task_note)
    EditText editTaskNote;

    /* QUICK ACTIONS */

    @BindView(edit_task_alarm)
    IconicsImageView editTaskAlarm;

    @BindView(edit_task_pick_color)
    IconicsImageView editTaskPickColor;

    @BindView(edit_task_save_task)
    IconicsImageView editTaskSave;

    @BindView(edit_task_remove_alarm)
    IconicsImageView editTaskRemoveAlarm;

    /* TEXTVIEWS */

    @BindView(edit_task_date_on)
    TextView editTaskDateOn;

    @BindView(edit_task_date)
    TextView editTaskDate;



    @OnTextChanged(edit_task_title)
    void onEditTaskTitleTextChange(SpannableStringBuilder builder) {
        task.setTitle(builder.toString());
        if (!task.getTitle().trim().isEmpty()) {
            editTaskSave.setAlpha(1f);
            editTaskSave.setEnabled(true);
        } else {
            editTaskSave.setAlpha(0.5f);
            editTaskSave.setEnabled(false);
        }
    }

    @OnTextChanged(edit_task_note)
    void onEditTaskNoteTextChange(SpannableStringBuilder builder) {
        task.setNote(builder.toString());
    }


    @OnClick(edit_task_date)
    void pickDateTime(){
        CustomTimeDialog timeDialog = new CustomTimeDialog(getActivity(), task.getTimestamp(), task.getColor());
        timeDialog.setTaskEditorPresenter(this);
        timeDialog.show();
    }

    @OnClick(edit_task_remove_alarm)
    void removeAlarm(){
        editTaskDateWrapper.setVisibility(GONE);
        editTaskAlarm.setColor(ContextCompat.getColor(getContext().getApplicationContext(), R.color.quick_action_color));
        task.setHasReminder(false);
    }

    @OnClick(edit_task_alarm)
    void addAlarm(){
        if(!task.hasReminder()) {
            editTaskDateWrapper.setVisibility(VISIBLE);
            editTaskAlarm.setColor(ColorUtils.getComplimentaryColor(Color.parseColor("#" + task.getColor())));
            task.setHasReminder(true);
        }
    }


    @OnClick(edit_task_pick_color)
    void pickColor(){
        new SpectrumDialog.Builder(getContext().getApplicationContext())
                .setColors(R.array.task_colors)
                .setSelectedColor(Color.parseColor("#" + task.getColor()))
                .setDismissOnColorSelected(false)
                .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                        if(positiveResult) {
                            task.setColor(Integer.toHexString(color));
                            if(task.hasReminder()) {
                                editTaskAlarm.setColor(ColorUtils.getComplimentaryColor(color));
                            }
                            setColors(color);
                        }
                    }
                }).build().show(getActivity().getSupportFragmentManager(), "color_picker");
    }
//    @OnClick(edit_task_set_alarm)
//    void onSetAlarmClick() {
//        CustomTimeDialog timeDialog = new CustomTimeDialog(getActivity(), task.getTimestamp());
//        timeDialog.setTaskEditorPresenter(this);
//        timeDialog.show();
//    }

    @OnClick(edit_task_save_task)
    void onSaveClick() {
        presenter.saveTask(((TaskeeApplication)getActivity().getApplication()).getRealm(),task);
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
        androidColors = getResources().getIntArray(R.array.task_colors);
        selectedColor = androidColors[new Random().nextInt(androidColors.length)];
        animations = new Animations();
        container.getViewTreeObserver().addOnGlobalLayoutListener(this);
        Bundle bundle = getArguments();
        if (bundle != null) {
//            presenter.editTask(bundle.getInt("item_id"));
//            setToolbarTitle(getString(R.string.line_edit_task));
        } else {
            task = new Task();
            bubble.setBackgroundColor(selectedColor);
            onNewTask();
        }
    }

    public void setQuickAddInteractor(QuickAddInteractor quickAddInteractor){
        this.quickAddInteractor = quickAddInteractor;
    }

    @Override
    public void onGlobalLayout() {
        container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        if (shouldAnimate) {
            circularReveal(true, 0);
        }
    }

    void circularReveal(boolean animateIn, int revealDelay) {
        final View view = mainView;
        bubble.setVisibility(VISIBLE);
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
            animator2.setStartDelay(revealDelay);
            animator2.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    animation.removeListener(this);
                    view.setVisibility(GONE);
                    internalClose();
                }
            });
            bubble.setVisibility(VISIBLE);
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
        quickAddInteractor.onQuickAddClose();
    }

    public void closeFragment() {
        shouldAnimate = false;
        circularReveal(false, 0);
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

        editTaskDate.setText(Constants.getFullDateTime(task.getTimestamp()));
        editTaskAlarm.setColor(ColorUtils.getComplimentaryColor(selectedColor));
        editTaskSave.setAlpha(0.5f);
        editTaskSave.setEnabled(false);
        task.setColor(Integer.toHexString(selectedColor));
        setColors(Color.parseColor("#" + task.getColor()));
    }

    private void setColors(int color) {
        int contrastColor = ColorUtils.isColorDark(color) ? Color.WHITE : Color.BLACK;
        int complimentaryColor = ColorUtils.getComplimentaryColor(color);
        int alphaContrast = Color.parseColor("#73" + Integer.toHexString(contrastColor).substring(2));

        editTaskTitle.setHintTextColor(alphaContrast);
        editTaskNote.setTextColor(contrastColor);
        editTaskNote.setHintTextColor(alphaContrast);
        editTaskDateOn.setTextColor(alphaContrast);
        editTaskDate.setTextColor(contrastColor);
        editTaskRemoveAlarm.setColor(contrastColor);
        editTaskPickColor.setColor(color);
        bubble.setBackgroundColor(color);
        setCursorColor(editTaskTitle, contrastColor);

        Drawable fragmentBackground = mainView.getBackground();
        if(fragmentBackground instanceof GradientDrawable){
            GradientDrawable shapeDrawable = (GradientDrawable) fragmentBackground;
            shapeDrawable.mutate().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        }
        Drawable editTaskSaveBackground = editTaskSave.getBackground();
        if(Build.VERSION.SDK_INT >= 21){
            if(editTaskSaveBackground instanceof RippleDrawable){
                RippleDrawable shapeDrawable2 = (RippleDrawable) editTaskSaveBackground;
                shapeDrawable2.getDrawable(0).mutate().setColorFilter(complimentaryColor, PorterDuff.Mode.MULTIPLY);
            }
        }else{
            if(editTaskSaveBackground instanceof GradientDrawable){
                GradientDrawable shapeDrawable = (GradientDrawable) editTaskSaveBackground;
                shapeDrawable.mutate().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
            }
        }
        editTaskSave.setColor(ColorUtils.isColorDark(complimentaryColor) ? Color.WHITE : Color.BLACK);
    }


    public static void setCursorColor(EditText view, @ColorInt int color) {
        try {
            // Get the cursor resource id
            Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
            field.setAccessible(true);
            int drawableResId = field.getInt(view);

            // Get the editor
            field = TextView.class.getDeclaredField("mEditor");
            field.setAccessible(true);
            Object editor = field.get(view);

            // Get the drawable and set a color filter
            Drawable drawable = ContextCompat.getDrawable(view.getContext(), drawableResId);
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            Drawable[] drawables = {drawable, drawable};

            // Set the drawables
            field = editor.getClass().getDeclaredField("mCursorDrawable");
            field.setAccessible(true);
            field.set(editor, drawables);
        } catch (Exception ignored) {
        }
    }


    @Override
    public void onTaskValidationError(Constants.ValidationError validationError) {

    }

    @Override
    public void onTaskSaveSuccessful(int id) {
        bubble.findViewById(R.id.task_saved).setVisibility(VISIBLE);
        circularReveal(false, REVEAL_DURATION * 3);
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
