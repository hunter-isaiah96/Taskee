package com.ihunter.taskee.presenters;

import com.ihunter.taskee.data.Task;
import com.ihunter.taskee.interfaces.views.TaskEditorView;

public class QuickAddPresenter {

    private TaskEditorView taskEditorView;

    public QuickAddPresenter(TaskEditorView taskEditorView) {
        this.taskEditorView = taskEditorView;
    }

    public void saveTask(Task task) {
        taskEditorView.onTaskSaveSuccessful(task.getId());
    }

}
