package com.ihunter.taskee.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ihunter.taskee.R;
import com.ihunter.taskee.fragments.AllTasksFragment;
import com.ihunter.taskee.fragments.CalendarTasksFragment;
import com.ihunter.taskee.fragments.TestFragment;
import com.ihunter.taskee.ui.DrawerHeader;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.multidots.fingerprintauth.FingerPrintAuthHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ihunter.taskee.Constants.REVEAL_DURATION;
import static com.ihunter.taskee.R.id.plan;

public class MainActivity extends AppCompatActivity{

    @BindView(R.id.content_container)
    FrameLayout view;
    @BindView(R.id.root_view)
    FrameLayout view2;

    @BindView(plan)
    ImageView newPlan;

    Fragment currentFragment;
    private Drawer mDrawer;
    private FingerPrintAuthHelper mFingerPrintAuthHelper;
    private TestFragment testFragment;
    private boolean closingQuickAdd = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        mFingerPrintAuthHelper = FingerPrintAuthHelper.getHelper(this, this);
        changeCurrentFragment(new AllTasksFragment());
        setUpDrawer();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mFingerPrintAuthHelper.startAuth();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mFingerPrintAuthHelper.stopAuth();
    }

    public void setUpDrawer(){
        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .withDisplayBelowStatusBar(true)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        (IDrawerItem)new DrawerHeader().withSelectable(false),
                        new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_all_tasks).withIcon(new IconicsDrawable(this).icon(FontAwesome.Icon.faw_list_ul)).withIconTintingEnabled(true),
                        new PrimaryDrawerItem().withIdentifier(2).withName(R.string.drawer_calendar).withIcon(new IconicsDrawable(this).icon(FontAwesome.Icon.faw_calendar_o)).withIconTintingEnabled(true)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch((int)drawerItem.getIdentifier()){
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
    protected void planButtonClick(View v) {
        if(Build.VERSION.SDK_INT >= 19) {
            animateWithArch(true);
        }else{
            animateWithoutArc(true);
        }
    }

    private void animateWithoutArc(final boolean animateIn){
        float parentCenterX = view.getX() + view.getWidth()/2;
        float parentCenterY = view.getY() + view.getHeight()/2;

        if(animateIn){
            newPlan.animate().translationX(parentCenterX - newPlan.getWidth()/2 - newPlan.getX())
                    .translationY(parentCenterY - newPlan.getHeight()/2 - newPlan.getY())
                    .setDuration(REVEAL_DURATION)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animation.removeListener(this);
                            newPlan.animate().scaleX(0f).scaleY(0f).setDuration(REVEAL_DURATION).setInterpolator(new OvershootInterpolator()).start();
                            openAddFragment();
                        }
                    })
                    .start();
        }else{
            newPlan.animate().translationX(0f)
                    .translationY(0f)
                    .setDuration(REVEAL_DURATION)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animation.removeListener(this);
                        }
                    })
                    .start();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void animateWithArch(final boolean animateIn){
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
                transition.removeListener(this);
                if(animateIn){
                    newPlan.animate()
                            .scaleX(0f)
                            .scaleY(0f)
                            .setDuration(REVEAL_DURATION)
                            .setInterpolator(new OvershootInterpolator())
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    animation.removeListener(this);
                                    newPlan.animate();
                                }
                            })
                            .start();
                    openAddFragment();
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

        if(animateIn) {
            layoutParams.gravity = Gravity.CENTER;
        }else{
            int margin = (int)getResources().getDimension(R.dimen.fab_margin);
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

    private void openAddFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (testFragment == null) {
            testFragment = new TestFragment();
            ft.add(R.id.content_container, testFragment, TestFragment.class.getName()).commit();
        }
    }

    public void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);
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
//        super.onBackPressed();
        if(testFragment != null && !closingQuickAdd){
            closingQuickAdd = true;
            testFragment.closeFragment();
            testFragment = null;
        }else{
            super.onBackPressed();
        }
    }

    public void quickAddClosed(){
        closingQuickAdd = false;
        testFragment = null;
        newPlan.setVisibility(View.VISIBLE);
        newPlan.animate().scaleX(1f).scaleY(1f).translationX(1f).translationY(1f).setDuration(REVEAL_DURATION).setInterpolator(new OvershootInterpolator()).start();
        if(Build.VERSION.SDK_INT >= 19){
            animateWithArch(false);
        }else{
            animateWithoutArc(false);
        }
    }

    private static AnimationSet getAnimation(int x, int y)  {

        AnimationSet animSet = new AnimationSet(false);
        animSet.addAnimation(new TranslateAnimation(0, 0, x, y));
        animSet.setDuration(REVEAL_DURATION);
        return animSet;
    }

    //    @Override
//    public void onNoFingerPrintHardwareFound() {
//
//    }
//
//    @Override
//    public void onNoFingerPrintRegistered() {
//
//    }
//
//    @Override
//    public void onBelowMarshmallow() {
//
//    }
//
//    @Override
//    public void onAuthSuccess(FingerprintManager.CryptoObject cryptoObject) {
//        System.out.println("FingerPrint: " + cryptoObject);
//    }
//
//    @Override
//    public void onAuthFailed(int errorCode, String errorMessage) {
//        switch (errorCode) {    //Parse the error code for recoverable/non recoverable error.
//            case AuthErrorCodes.CANNOT_RECOGNIZE_ERROR:
//                //Cannot recognize the fingerprint scanned.
//                break;
//            case AuthErrorCodes.NON_RECOVERABLE_ERROR:
//                //This is not recoverable error. Try other options for user authentication. like pin, password.
//                break;
//            case AuthErrorCodes.RECOVERABLE_ERROR:
//                //Any recoverable error. Display message to the user.
//                break;
//        }
//    }
}
