package com.ihunter.taskee.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.ihunter.taskee.R;
import com.ihunter.taskee.fragments.AllTasksFragment;
import com.ihunter.taskee.fragments.CalendarTasksFragment;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Master Bison on 12/21/2016.
 */

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.content_container)
    View view;

    @BindView(R.id.plan)
    ImageView plan;

    Fragment currentFragment;
    Drawer mDrawer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
        ButterKnife.bind(this);
        changeCurrentFragment(new AllTasksFragment());
        setUpDrawer();
    }

    public void setUpDrawer(){
        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .withDisplayBelowStatusBar(true)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_all_tasks).withIcon(R.drawable.zzz_view_list).withIconTintingEnabled(true),
                        new PrimaryDrawerItem().withIdentifier(2).withName(R.string.drawer_calendar).withIcon(R.drawable.zzz_calendar).withIconTintingEnabled(true)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch(position){
                            case 0:
                                changeCurrentFragment(new AllTasksFragment());
                                break;
                            case 1:
                                changeCurrentFragment(new CalendarTasksFragment());
                                break;
                        }
                        return false;
                    }
                })
                .build();
    }

    @OnClick(R.id.plan)
    protected void planButtonClick() {
        startActivity(new Intent(MainActivity.this, TaskEditorActivity.class));
    }

    private void changeCurrentFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_container, fragment).commit();
        currentFragment = fragment;
        getSupportFragmentManager().executePendingTransactions();

    }

    public void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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

}
