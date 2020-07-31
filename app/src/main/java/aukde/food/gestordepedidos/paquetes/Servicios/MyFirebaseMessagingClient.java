package aukde.food.gestordepedidos.paquetes.Servicios;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Canales.NotificationHelper;
import aukde.food.gestordepedidos.paquetes.Receptor.AcceptReceiver;
import aukde.food.gestordepedidos.paquetes.Receptor.CancelReceiver;

public class MyFirebaseMessagingClient extends FirebaseMessagingService {

    private static final int NOTIFICATION_CODE = 100;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Map<String,String> data = remoteMessage.getData();
        String title = data.get("title");
        String body = data.get("body");

        if (title != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                if (title.contains("Nuevo pedido #")){
                    String idClient = data.get("idClient");
                    showNotificationApiOreoActions(title,body,idClient);
                }
                else
                {
                    showNotificationApiOreo(title,body);
                }
            }
            else {
                if (title.contains("Nuevo pedido #")){
                    String idClient = data.get("idClient");
                    showNotificationActions(title,body,idClient);
                }
                else {
                    showNotification(title,body);
                }
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotificationApiOreo(String title , String body) {
        PendingIntent intent = PendingIntent.getActivity(getBaseContext(),0,new Intent(),PendingIntent .FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        Notification.Builder builder = notificationHelper.getNotification(title,body,intent,sound);
        notificationHelper.getManager().notify(1,builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotificationApiOreoActions(String title , String body , String idClient) {
        //ver lista
        Intent assetIntent = new Intent(this, AcceptReceiver.class);
        assetIntent.putExtra("idClient",idClient);
        PendingIntent acceptPendingIntent = PendingIntent.getBroadcast(this,NOTIFICATION_CODE,assetIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Action acceptAction = new Notification.Action.Builder(
                R.mipmap.ic_launcher,"Ver lista",acceptPendingIntent
        ).build();

        //Cerrar
        Intent cancelIntent = new Intent(this, CancelReceiver.class);
        cancelIntent.putExtra("idClient",idClient);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(this,NOTIFICATION_CODE,cancelIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Action cancelAction = new Notification.Action.Builder(
                R.mipmap.ic_launcher,"Cerrar",cancelPendingIntent
        ).build();

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        Notification.Builder builder = notificationHelper.getNotificationActions(title,body,sound,acceptAction,cancelAction);
        notificationHelper.getManager().notify(2,builder.build());
    }


    private void showNotification(String title , String body) {
        PendingIntent intent = PendingIntent.getActivity(getBaseContext(),0,new Intent(),PendingIntent .FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        NotificationCompat.Builder builder = notificationHelper.getNotificationOldApi(title,body,intent,sound);
        notificationHelper.getManager().notify(1,builder.build());
    }

    private void showNotificationActions(String title , String body,String idClient) {
        //ver lista
        Intent assetIntent = new Intent(this, AcceptReceiver.class);
        assetIntent.putExtra("idClient",idClient);
        PendingIntent acceptPendingIntent = PendingIntent.getBroadcast(this,NOTIFICATION_CODE,assetIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action acceptAction = new NotificationCompat.Action.Builder(
                R.mipmap.ic_launcher,"Ver lista",acceptPendingIntent
        ).build();
        //cerrar

        Intent cancelIntent = new Intent(this, CancelReceiver.class);
        cancelIntent.putExtra("idClient",idClient);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(this,NOTIFICATION_CODE,cancelIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action cancelAction = new NotificationCompat.Action.Builder(
                R.mipmap.ic_launcher,"Cerrar",cancelPendingIntent
        ).build();

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        NotificationCompat.Builder builder = notificationHelper.getNotificationOldApiActions(title,body,sound,acceptAction,cancelAction);
        notificationHelper.getManager().notify(2,builder.build());
    }

}
