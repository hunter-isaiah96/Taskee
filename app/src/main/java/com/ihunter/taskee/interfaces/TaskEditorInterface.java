package com.ihunter.taskee.interfaces;

import com.ihunter.taskee.Constants;
import com.ihunter.taskee.data.Task;

/**
 * Created by Master Bison on 12/26/2016.
 */

public interface TaskEditorInterface {

    void onPlanValidationError(Constants.ValidationError validationError);
    void onPlanSaveSuccessful();
    void onPlanForEditorMode(Task plan);
    void onPlanDateSet(long time);

}
