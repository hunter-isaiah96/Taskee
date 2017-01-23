package com.ihunter.taskee.interfaces;

import com.ihunter.taskee.Constants;
import com.ihunter.taskee.data.Task;

/**
 * Created by Master Bison on 12/26/2016.
 */

public interface TaskEditorView {

    void onTaskValidationError(Constants.ValidationError validationError);
    void onTaskSaveSuccessful(int id);
    void onTaskForEditorMode(Task plan);
    void onTaskDateSet(long time);

}
