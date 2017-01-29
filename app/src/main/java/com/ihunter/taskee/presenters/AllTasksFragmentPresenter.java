package com.ihunter.taskee.presenters;

import com.ihunter.taskee.interfaces.views.AllTasksFragmentView;
import com.ihunter.taskee.services.RealmService;

/**
 * Created by Master Bison on 1/12/2017.
 */

public class AllTasksFragmentPresenter {

    private AllTasksFragmentView allTasksFragmentView;
    private RealmService realmService;

    public AllTasksFragmentPresenter(AllTasksFragmentView allTasksFragmentView, RealmService realmService){
        this.allTasksFragmentView = allTasksFragmentView;
        this.realmService = realmService;
    }

    public void refreshAllTasks(){
        allTasksFragmentView.onRefreshAllTasks(realmService.getAllTasks());
    }

}
