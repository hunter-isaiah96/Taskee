package com.ihunter.taskee.activities;

import android.net.Uri;

/**
 * Created by Master Bison on 12/26/2016.
 */

public interface TaskEditorView {

    // If Plan Validation is Unsuccessful, call onPlanValidationError and Pass a Message
    void onPlanValidationError(String string);

    // If Plan Validation is Successful, save the Plan
    void onPlanSaveSuccessful();

    // IF User Adds A SubTask
    void onAddSubTask(String string);

    // Set Toolbar Title
    void onTitleSet(String string);

    // If inEditMode
    void onPlanForEditorMode();

    void onPlanDateSet(String string);

    void onImageSet(Uri uri);

}
