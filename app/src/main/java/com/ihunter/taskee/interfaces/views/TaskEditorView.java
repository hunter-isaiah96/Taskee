package com.ihunter.taskee.interfaces.views;

import com.ihunter.taskee.Constants;
import com.ihunter.taskee.data.Task;

public interface TaskEditorView {

    void onTaskValidationError(Constants.ValidationError validationError);
    void onTaskSaveSuccessful(int id);
    void onTaskForEditorMode(Task plan);
    void onTaskDateSet(long time);
    void onSubTaskAdded();

}
