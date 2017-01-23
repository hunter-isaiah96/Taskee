package com.ihunter.taskee.interfaces;

/**
 * Created by Master Bison on 1/5/2017.
 */

public interface SubTaskItemAdapterInteractor {
    void onRemoveSubTask(int position);
    void onSubTaskCompletionChange(int position, boolean checked);
}
