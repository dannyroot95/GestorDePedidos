package aukde.food.gestordepedidos.paquetes.Canales;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import aukde.food.gestordepedidos.R;

public class NotificationHelper extends ContextWrapper {

    private static final String CHANNEL_ID = "aukde.food.gestordepedidos";
    private static final String CHANNEL_NAME = "Gestor de pedidos";
    private NotificationManager manager;

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
    public Notification.Builder getNotification(String title, String body , PendingIntent intent , Uri sonidoUri , String path){

        Bitmap bmp = null;
        try {
            InputStream in = new URL(path).openStream();
            bmp = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Notification.Builder(getApplicationContext(),CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setLargeIcon(bmp)
                .setAutoCancel(true)
                .setStyle(new Notification.BigPictureStyle()
                .bigPicture(bmp).bigLargeIcon(bmp))
                .setSound(sonidoUri)
                .setContentIntent(intent)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_notificacion)
                .setStyle(new Notification.BigTextStyle().bigText(body).setBigContentTitle(title));

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getNotificationActions(String title,
                                                       String body,
                                                       Uri sonidoUri,String path,
                                                       Notification.Action acceptAction,
                                                       Notification.Action cancelAction){
        Bitmap bmp = null;
        try {
            InputStream in = new URL(path).openStream();
            bmp = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Notification.Builder(getApplicationContext(),CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setLargeIcon(bmp)
                .setStyle(new Notification.BigPictureStyle()
                        .bigPicture(bmp).bigLargeIcon(bmp))
                .setSound(sonidoUri)
                .setOngoing(true)
                .addAction(acceptAction)
                .addAction(cancelAction)
                .setSmallIcon(R.drawable.ic_notificacion)
                .setStyle(new Notification.BigTextStyle().bigText(body).setBigContentTitle(title));
    }

    public NotificationCompat.Builder getNotificationOldApi(String title,
                                                            String body,String path,
                                                            PendingIntent intent ,
                                                            Uri sonidoUri){
        Bitmap bmp = null;
        try {
            InputStream in =
                    new URL(path).openStream();
            bmp = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setOngoing(true)
                .setLargeIcon(bmp)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bmp).bigLargeIcon(bmp))
                .setSound(sonidoUri)
                .setContentIntent(intent)
                .setSmallIcon(R.drawable.ic_notificacion)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body).setBigContentTitle(title));
    }

    public NotificationCompat.Builder getNotificationOldApiActions(String title,
                                                                   String body,String path,
                                                                   Uri sonidoUri,
                                                                   NotificationCompat.Action acceptAction,
                                                                   NotificationCompat.Action cancelAction){
        Bitmap bmp = null;
        try {
            InputStream in = new URL(path).openStream();
            bmp = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(sonidoUri)
                .setOngoing(true)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bmp).bigLargeIcon(bmp))
                .setLargeIcon(bmp)
                .addAction(acceptAction)
                .addAction(cancelAction)
                .setSmallIcon(R.drawable.ic_notificacion)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body).setBigContentTitle(title));
    }



}