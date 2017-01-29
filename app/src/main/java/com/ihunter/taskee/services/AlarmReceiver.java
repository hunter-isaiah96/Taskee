package com.ihunter.taskee.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Master Bison on 1/9/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

//        RealmService realmService = new RealmService();
//
//        Task task = realmService.getTaskByID(intent.getIntExtra("item_id", 0));

//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
//                .setSmallIcon(R.drawable.zzz_view_list)
//                .setTicker("You have a task to complete!")
//                .setContentTitle(task.getTitle())
//                .setContentText(task.getNote())
//                .setAutoCancel(true);
//
//        Intent resultIntent = new Intent(context, MainActivity.class);
//        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_ONE_SHOT);
//        mBuilder.setContentIntent(resultPendingIntent);
//
//        NotificationManager mNotifyMgr =
//                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotifyMgr.notify(task.getId(), mBuilder.build());
    }

}
