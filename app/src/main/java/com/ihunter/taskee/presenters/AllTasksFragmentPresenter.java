package com.ihunter.taskee.presenters;

import com.ihunter.taskee.interfaces.repositories.TaskRepository;
import com.ihunter.taskee.interfaces.views.AllTasksFragmentView;

public class AllTasksFragmentPresenter {

    private AllTasksFragmentView allTasksFragmentView;
    private TaskRepository taskRepository;

    public AllTasksFragmentPresenter(AllTasksFragmentView allTasksFragmentView, TaskRepository taskRepository){
        this.allTasksFragmentView = allTasksFragmentView;
        this.taskRepository = taskRepository;
    }

    public void refreshAllTasks(){
        allTasksFragmentView.onTasksLoaded(taskRepository.getAllTasks());
    }

}
