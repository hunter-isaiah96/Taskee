package com.ihunter.taskee.service;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Master Bison on 12/28/2016.
 */

public class RealmService {

    private final Realm mRealm;

    public RealmService(Realm realm) {
        mRealm = realm;
    }

    public static RealmConfiguration getRealmConfiugration(){
        return new RealmConfiguration.Builder()
                .name("taskmanager.realm")
                .schemaVersion(0)
                .build();
    }

}
