package com.ihunter.taskee;

import android.app.Application;
import android.content.Context;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.Iconics;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Master Bison on 12/10/2016.
 */

public class TaskeeApplication extends Application{

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        Iconics.registerFont(new FontAwesome());
        context = getApplicationContext();
    }

    public static RealmConfiguration getRealmConfiugration(){
        return new RealmConfiguration.Builder()
                .name("taskmanager.realm")
                .schemaVersion(0)
                .build();
    }

    public static Context getContext() {
        return context;
    }
}


