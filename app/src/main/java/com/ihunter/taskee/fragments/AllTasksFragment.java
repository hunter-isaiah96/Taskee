package com.ihunter.taskee.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ihunter.taskee.R;
import com.ihunter.taskee.activities.MainActivity;
import com.ihunter.taskee.adapters.PlanItemAdapter;
import com.ihunter.taskee.services.RealmService;
import com.ihunter.taskee.ui.EmptyRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ihunter.taskee.R.id.toolbar_title;

/**
 * Created by Master Bison on 12/21/2016.
 */

public class AllTasksFragment extends Fragment {

    PlanItemAdapter adapter;
    RealmService realmService;

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
        realmService = new RealmService();
        View view = inflater.inflate(R.layout.fragment_all_tasks, container, false);
        ButterKnife.bind(this, view);
        ((MainActivity)getActivity()).setToolbar(toolbar);
        setToolbarTitle(getString(R.string.line_all_tasks));
        setAdapterByDate();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }

    public void refreshList(){
        adapter.replacePlansList(realmService.getAllTasks());
    }

    public void setAdapterByDate(){
        adapter = new PlanItemAdapter(getActivity());
        allTasksView.setLayoutManager(new LinearLayoutManager(getActivity()));
        allTasksView.setEmptyView(todoListEmptyView);
        allTasksView.setHasFixedSize(true);
        allTasksView.setAdapter(adapter);
    }

    public void setToolbarTitle(String title){
        toolbarTitle.setText(title);
    }

}
