package com.ihunter.taskee.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.ihunter.taskee.R;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewTaskActivity extends AppCompatActivity{

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        ButterKnife.bind(this);
        setUpToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.view_task_menu, menu);
        MenuItem setTaskComplete = menu.findItem(R.id.menu_complete);
        MenuItem editTask = menu.findItem(R.id.menu_edit_task);
        MenuItem deleteTask = menu.findItem(R.id.menu_trash);
        deleteTask.setIcon(new IconicsDrawable(this).icon(MaterialDesignIconic.Icon.gmi_delete).actionBar().paddingDp(4).color(Color.WHITE));
        editTask.setIcon(new IconicsDrawable(this).icon(MaterialDesignIconic.Icon.gmi_edit).actionBar().paddingDp(4).color(Color.WHITE));
        return true;
    }

    public void setUpToolbar(){
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    public void setToolbarTitle(String title){
        toolbarTitle.setText(title);
    }
}
