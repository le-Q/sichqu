package no.hiof.sichqu.sichqu;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class NotificationActivity extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
      /* long when = System.currentTimeMillis();
       NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

       Intent mintent = new Intent(context,HvisListeneActivity.class);
       mintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

       PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mintent, PendingIntent.FLAG_UPDATE_CURRENT);

       NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
               .setContentTitle("")
               .setContentText("")
               .setAutoCancel(true)
               .setWhen(when)
               .setContentIntent(pendingIntent);
        notificationManager.notify(0, builder.build());*/


    }
}
