package no.hiof.sichqu.sichqu;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

class NotificationActivity extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
       /* Intent repeatingintent = new Intent(context, HvisListeneActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(HvisListeneActivity.class);
        stackBuilder.addNextIntent(repeatingintent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(100, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setContentTitle("Noe");
        builder.setContentText("Husk å legge til varer!");
        builder.setTicker("");
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent).build();


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, builder.build());*/
    }
}
