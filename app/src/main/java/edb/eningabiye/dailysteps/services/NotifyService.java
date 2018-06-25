package edb.eningabiye.dailysteps.services;

import android.app.IntentService;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.couchbase.lite.CouchbaseLiteException;

import edb.eningabiye.dailysteps.Messages;
import edb.eningabiye.dailysteps.R;
import edb.eningabiye.dailysteps.database.CouchData;

public class NotifyService extends IntentService {

    public NotifyService() {
        super("NotifyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if ("NOTIFY".equals(action)) {
                String[] data = intent.getStringExtra("msg").split("##");
                String msg = data[0];
                String nom = data[1];
                String steps = data[2];
                saveMsg(nom, steps, msg);
                notification(nom, steps, msg);
            }
        }
    }
    public void notification(String nom, String steps, String msg){
        Intent intent = new Intent(this, Messages.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_NO_CREATE);
        //PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "1020")
                .setSmallIcon(R.drawable.baseline_directions_walk_white_18)
                .setContentTitle(nom)
                .setContentText(msg)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(msg+" ... "+steps))
                //.setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, mBuilder.build());
    }

    public void saveMsg(String nom, String steps, String msg){
        try {
            CouchData db = new CouchData(getApplicationContext());
            db.saveMessages(nom, steps, msg);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }

    }
}
