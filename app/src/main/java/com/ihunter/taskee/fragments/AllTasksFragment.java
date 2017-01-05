package com.ihunter.taskee.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ihunter.taskee.R;
import com.ihunter.taskee.TaskeeApplication;
import com.ihunter.taskee.activities.MainActivity;
import com.ihunter.taskee.adapters.PlanItemAdapter;
import com.ihunter.taskee.data.Plan;
import com.ihunter.taskee.ui.EmptyRecyclerView;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.ihunter.taskee.R.id.toolbar_title;

/**
 * Created by Master Bison on 12/21/2016.
 */

public class AllTasksFragment extends Fragment {

    Realm realm;
    PlanItemAdapter adapter;
    boolean performOnResume = false;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(toolbar_title)
    AppCompatTextView toolbarTitle;

    @BindView(R.id.todo_list_empty_view)
    LinearLayout todoListEmptyView;

    @BindView(R.id.all_tasks_view)
    EmptyRecyclerView allTasksView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_tasks, container, false);
        ButterKnife.bind(this, view);
        realm = Realm.getInstance(TaskeeApplication.getRealmConfiugration());
        ((MainActivity)getActivity()).setToolbar(toolbar);
        setToolbarTitle(getString(R.string.line_all_tasks));
        adapter = new PlanItemAdapter(getActivity(), getAllTasks());
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            allTasksView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        }else{
            allTasksView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        allTasksView.setEmptyView(todoListEmptyView);
        allTasksView.setHasFixedSize(true);
        allTasksView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(performOnResume){
            refreshList();
        }else{
            performOnResume = true;
        }
    }

    public void refreshList(){
        adapter.replacePlansList(getAllTasks());
    }

    public void setToolbarTitle(String title){
        toolbarTitle.setText(title);
    }

    public static Date toDate(Calendar calendar){
        return calendar.getTime();
    }

    public RealmResults<Plan> getAllTasks(){
        return realm.where(Plan.class).findAllSorted("id", Sort.DESCENDING);
    }

}
