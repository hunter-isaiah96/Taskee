package com.ihunter.taskee.presenters;

import com.ihunter.taskee.interfaces.AllTasksFragmentView;
import com.ihunter.taskee.services.RealmService;

/**
 * Created by Master Bison on 1/12/2017.
 */

public class AllTasksFragmentPresenter {

    AllTasksFragmentView allTasksFragmentView;
    RealmService realmService;

    public AllTasksFragmentPresenter(AllTasksFragmentView allTasksFragmentView){
        this.allTasksFragmentView = allTasksFragmentView;
        realmService = new RealmService();
    }

    public void refreshAllTasks(){
        allTasksFragmentView.onRefreshAllTasks(realmService.getAllTasks());
    }

}
