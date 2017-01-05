package com.ihunter.taskee;

import android.app.Application;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.Iconics;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Master Bison on 12/10/2016.
 */

public class TaskeeApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        Iconics.registerFont(new FontAwesome());
//        if(BuildConfig.DEBUG) {
//            TinyDancer.create()
//                    .show(this);
//        }
    }

    public static RealmConfiguration getRealmConfiugration(){
        return new RealmConfiguration.Builder()
                .name("taskmanager.realm")
                .schemaVersion(0)
                .build();
    }
}


