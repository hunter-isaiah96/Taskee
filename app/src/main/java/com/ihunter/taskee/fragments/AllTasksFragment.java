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
import com.ihunter.taskee.adapters.TaskItemAdapter;
import com.ihunter.taskee.data.Task;
import com.ihunter.taskee.interfaces.repositories.TasksRepositoryImpl;
import com.ihunter.taskee.interfaces.views.AllTasksFragmentView;
import com.ihunter.taskee.presenters.AllTasksFragmentPresenter;
import com.ihunter.taskee.ui.EmptyRecyclerView;
import com.ihunter.taskee.ui.SimpleDividerItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

import static com.ihunter.taskee.R.id.toolbar_title;

public class AllTasksFragment extends Fragment implements AllTasksFragmentView{

    AllTasksFragmentPresenter presenter;

    TaskItemAdapter adapter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(toolbar_title)
    AppCompatTextView toolbarTitle;

    @BindView(R.id.todo_list_empty_view)
    LinearLayout todoListEmptyView;

    @BindView(R.id.all_tasks_view)
    EmptyRecyclerView allTasks;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_tasks, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setupToolbar();
        setupTasksList();
        presenter = new AllTasksFragmentPresenter(this, new TasksRepositoryImpl(getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAllTasks();
    }

    public void loadAllTasks(){
        presenter.refreshAllTasks();
    }

    private void setupToolbar(){
        ((MainActivity)getActivity()).setToolbar(toolbar);
        setToolbarTitle(getString(R.string.line_all_tasks));
    }

    private void setupTasksList(){
        adapter = new TaskItemAdapter(getActivity());
        allTasks.setLayoutManager(new LinearLayoutManager(getActivity()));
        allTasks.setEmptyView(todoListEmptyView);
        allTasks.setHasFixedSize(true);
        allTasks.setAdapter(adapter);
        allTasks.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
    }

    private void setToolbarTitle(String title){
        toolbarTitle.setText(title);
    }

    @Override
    public void onTasksLoaded(RealmResults<Task> tasks) {
        adapter.replacePlansList(tasks);
    }
}
