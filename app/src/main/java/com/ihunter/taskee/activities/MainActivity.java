package com.ihunter.taskee.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ihunter.taskee.R;
import com.ihunter.taskee.animations.Animations;
import com.ihunter.taskee.fragments.AllTasksFragment;
import com.ihunter.taskee.fragments.CalendarTasksFragment;
import com.ihunter.taskee.fragments.QuickAddFragment;
import com.ihunter.taskee.ui.DrawerHeader;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.ihunter.taskee.Constants.REVEAL_DURATION;
import static com.ihunter.taskee.R.id.plan;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.content_container)
    FrameLayout view;

    @BindView(R.id.root_view)
    FrameLayout view2;

    @BindView(plan)
    ImageView newPlan;

    Fragment currentFragment;
    private Animations animations;
    private Drawer mDrawer;
    private QuickAddFragment quickAddFragment;
    private boolean closingQuickAdd = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        animations = new Animations();
        changeCurrentFragment(new AllTasksFragment());
        setUpDrawer();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void setUpDrawer() {
        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .withDisplayBelowStatusBar(true)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        (IDrawerItem) new DrawerHeader().withSelectable(false),
                        new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_all_tasks).withIcon(new IconicsDrawable(this).icon(FontAwesome.Icon.faw_list_ul)).withIconTintingEnabled(true),
                        new PrimaryDrawerItem().withIdentifier(2).withName(R.string.drawer_calendar).withIcon(new IconicsDrawable(this).icon(FontAwesome.Icon.faw_calendar_o)).withIconTintingEnabled(true)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch ((int) drawerItem.getIdentifier()) {
                            case 1:
                                changeCurrentFragment(new AllTasksFragment());
                                break;
                            case 2:
                                changeCurrentFragment(new CalendarTasksFragment());
                                break;
                        }
                        return false;
                    }
                })
                .build();
        mDrawer.setSelection(1);
    }

    @OnClick(plan)
    protected void planButtonClick() {
        if (Build.VERSION.SDK_INT >= 19) {
            animateWithArch(true);
        } else {
            animateWithoutArc(true);
        }
    }

    private void animateWithoutArc(final boolean animateIn) {
        float[] center = animations.transformCenter(view, newPlan);
        if (animateIn) {
            newPlan.animate()
                    .translationX(center[0])
                    .translationY(center[1])
                    .setDuration(REVEAL_DURATION)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            newPlan.animate()
                                    .scaleX(0f)
                                    .scaleY(0f)
                                    .setDuration(REVEAL_DURATION)
                                    .setListener(null)
                                    .setInterpolator(new OvershootInterpolator())
                                    .start();
                            openQuickAddFragment();
                        }
                    })
                    .start();
        } else {
            newPlan.animate()
                    .translationX(0f)
                    .translationY(0f)
                    .setDuration(REVEAL_DURATION)
                    .start();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void animateWithArch(final boolean animateIn) {
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
                if (animateIn) {
                    newPlan.animate()
                            .scaleX(0f)
                            .scaleY(0f)
                            .setDuration(REVEAL_DURATION)
                            .setInterpolator(new OvershootInterpolator())
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    newPlan.animate().setListener(null);
                                }
                            })
                            .start();
                    openQuickAddFragment();
                }
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
        TransitionManager.beginDelayedTransition(view2, transition);

        if (animateIn) {
            layoutParams.gravity = Gravity.CENTER;
        } else {
            int margin = (int) getResources().getDimension(R.dimen.fab_margin);
            layoutParams.gravity = Gravity.BOTTOM | Gravity.END;
            layoutParams.setMargins(margin, margin, margin, margin);
        }
        newPlan.setLayoutParams(layoutParams);
    }

    private void changeCurrentFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_container, fragment).commit();
        currentFragment = fragment;
        getSupportFragmentManager().executePendingTransactions();

    }

    private void openQuickAddActivity(){
        Intent i = new Intent(this, TestActivity.class);
        startActivity(i);
        overridePendingTransition(0, 0);
    }

    private void openQuickAddFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (quickAddFragment == null) {
            quickAddFragment = new QuickAddFragment();

            ft.add(R.id.content_container, quickAddFragment);
            ft.commit();
        }
    }

    public void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);
        mDrawer.setToolbar(this, toolbar);
        mDrawer.setActionBarDrawerToggle(new ActionBarDrawerToggle(this, mDrawer.getDrawerLayout(), toolbar, com.mikepenz.materialdrawer.R.string.material_drawer_open, com.mikepenz.materialdrawer.R.string.material_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }
        });
        mDrawer.getActionBarDrawerToggle().syncState();
    }

    @Override
    public void onBackPressed() {
        if (quickAddFragment != null && !closingQuickAdd) {
            closingQuickAdd = true;
            quickAddFragment.closeFragment();
            quickAddFragment = null;
        } else {
            super.onBackPressed();
        }
    }

    public void quickAddClosed() {
        closingQuickAdd = false;
        quickAddFragment = null;
        newPlan.setVisibility(View.VISIBLE);
        newPlan.animate()
                .scaleX(1f)
                .scaleY(1f)
                .translationX(1f)
                .translationY(1f)
                .setDuration(REVEAL_DURATION)
                .setListener(null)
                .setInterpolator(new OvershootInterpolator())
                .start();
        if (Build.VERSION.SDK_INT >= 19) {
            animateWithArch(false);
        } else {
            animateWithoutArc(false);
        }
    }
}