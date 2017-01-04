package com.ihunter.taskee.interfaces;

import com.ihunter.taskee.data.Plan;

/**
 * Created by Master Bison on 12/26/2016.
 */

public interface TaskEditorInterface {

    void onPlanValidationError(String string);
    void onPlanSaveSuccessful();
    void onPlanForEditorMode(Plan plan);
    void onPlanDateSet(long time);

}
