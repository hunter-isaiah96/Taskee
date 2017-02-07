package com.ihunter.taskee.presenters;

import com.ihunter.taskee.data.Task;
import com.ihunter.taskee.interfaces.repositories.TaskRepository;
import com.ihunter.taskee.interfaces.views.ViewTaskView;

public class ViewTaskPresenter {

    private TaskRepository taskRepository;
    private ViewTaskView viewTaskView;

    public ViewTaskPresenter(TaskRepository taskRepository, ViewTaskView viewTaskView){
        this.taskRepository = taskRepository;
        this.viewTaskView = viewTaskView;
    }

    public void findTask(int id){
        Task task = taskRepository.getOneTask(id);
        viewTaskView.onTaskFound(task);
    }

}
