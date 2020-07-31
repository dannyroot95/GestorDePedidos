package aukde.food.gestordepedidos.paquetes.Canales;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import aukde.food.gestordepedidos.R;

public class NotificationHelper extends ContextWrapper {

    private static final String CHANNEL_ID = "aukde.food.gestordepedidos";
    private static final String CHANNEL_NAME = "Gestordepedidos";
    private NotificationManager manager ;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel(){
        NotificationChannel notificationChannel = new NotificationChannel(
                CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH
        );
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setLightColor(Color.GRAY);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(notificationChannel);
    }

    public NotificationManager getManager (){
        if (manager == null){
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getNotification(String title, String body, PendingIntent intent , Uri sonidoUri){
           return new Notification.Builder(getApplicationContext(),CHANNEL_ID)
                   .setContentTitle(title)
                   .setContentText(body)
                   .setAutoCancel(true)
                   .setSound(sonidoUri)
                   .setContentIntent(intent)
                   .setOngoing(true)
                   .setSmallIcon(R.drawable.ic_notificacion)
                   .setStyle(new Notification.BigTextStyle().bigText(body).setBigContentTitle(title));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getNotificationActions(String title,
                                                       String body,
                                                       Uri sonidoUri,
                                                       Notification.Action acceptAction,
                                                       Notification.Action cancelAction){
        return new Notification.Builder(getApplicationContext(),CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(sonidoUri)
                .setOngoing(true)
                .addAction(acceptAction)
                .addAction(cancelAction)
                .setSmallIcon(R.drawable.ic_notificacion)
                .setStyle(new Notification.BigTextStyle().bigText(body).setBigContentTitle(title));
    }

    public NotificationCompat.Builder getNotificationOldApi(String title,
                                                            String body,
                                                            PendingIntent intent ,
                                                            Uri sonidoUri){
        return new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setOngoing(true)
                .setSound(sonidoUri)
                .setContentIntent(intent)
                .setSmallIcon(R.drawable.ic_notificacion)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body).setBigContentTitle(title));
    }

    public NotificationCompat.Builder getNotificationOldApiActions(String title,
                                                                   String body,
                                                                   Uri sonidoUri,
                                                                   NotificationCompat.Action acceptAction,
                                                                   NotificationCompat.Action cancelAction){
        return new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(sonidoUri)
                .setOngoing(true)
                .addAction(acceptAction)
                .addAction(cancelAction)
                .setSmallIcon(R.drawable.ic_notificacion)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body).setBigContentTitle(title));
    }



}
