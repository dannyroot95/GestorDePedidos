package aukde.food.aukdeliver.paquetes.Servicios;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Map;
import aukde.food.aukdeliver.R;
import aukde.food.aukdeliver.paquetes.Actividades.Notificacion;
import aukde.food.aukdeliver.paquetes.Canales.NotificationHelper;
import aukde.food.aukdeliver.paquetes.Receptor.AcceptReceiver;
import aukde.food.aukdeliver.paquetes.Receptor.CancelReceiver;

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
        String path = data.get("path");

        if (title != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                if (title.contains("Nuevo pedido #")){
                    String idClient = data.get("idClient");
                    String numPedido = data.get("numPedido");
                    String nombre = data.get("nombre");
                    String telefono = data.get("telefono");
                    String direccion = data.get("direccion");
                    String hora = data.get("hora");
                    String fecha = data.get("fecha");
                    String ganancia = data.get("ganancia");
                    String repartidor = data.get("repartidor");
                    //showNotificationApiOreoActions(title,body,path,idClient);
                    showNotificationApiOreoActivity(numPedido,nombre,telefono,direccion,hora,fecha,ganancia,repartidor);
                }
                else
                {
                    showNotificationApiOreo(title,body,path);
                }
            }
            else {
                if (title.contains("Nuevo pedido #")){
                    String idClient = data.get("idClient");
                    String numPedido = data.get("numPedido");
                    String nombre = data.get("nombre");
                    String telefono = data.get("telefono");
                    String direccion = data.get("direccion");
                    String hora = data.get("hora");
                    String fecha = data.get("fecha");
                    String ganancia = data.get("ganancia");
                    String repartidor = data.get("repartidor");
                    //showNotificationActions(title,body,path,idClient);
                    showNotificationApiOreoActivity(numPedido,nombre,telefono,direccion,hora,fecha,ganancia,repartidor);
                }
                else {
                    showNotification(title,body,path);
                }
            }
        }

    }

    private void showNotificationApiOreoActivity(String numPedido, String nombre, String telefono, String direccion, String hora, String fecha, String ganancia, String repartidor) {

        PowerManager pm = (PowerManager) getBaseContext().getSystemService(Context.POWER_SERVICE);
        boolean screenOn = pm.isScreenOn();
        if(!screenOn){
            PowerManager.WakeLock wakeLock = pm.newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK |
                            PowerManager.ACQUIRE_CAUSES_WAKEUP |
                            PowerManager.ON_AFTER_RELEASE,"AppName:Mylock"
            );
            wakeLock.acquire(10000);
        }
        Intent intent = new Intent(getBaseContext(), Notificacion.class);
        intent.putExtra("numPedido",numPedido);
        intent.putExtra("nombre",nombre);
        intent.putExtra("telefono",telefono);
        intent.putExtra("direccion",direccion);
        intent.putExtra("hora",hora);
        intent.putExtra("fecha",fecha);
        intent.putExtra("ganancia",ganancia);
        intent.putExtra("repartidor",repartidor);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotificationApiOreo(String title , String body , String path) {
        PendingIntent intent = PendingIntent.getActivity(getBaseContext(),0,new Intent(),PendingIntent .FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        Notification.Builder builder = notificationHelper.getNotification(title,body,intent,sound,path);
        notificationHelper.getManager().notify(1,builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotificationApiOreoActions(String title , String body ,String path ,  String idClient) {
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
        Notification.Builder builder = notificationHelper.getNotificationActions(title,body,sound,path,acceptAction,cancelAction);
        notificationHelper.getManager().notify(2,builder.build());
    }


    private void showNotification(String title , String body,String path) {
        PendingIntent intent = PendingIntent.getActivity(getBaseContext(),0,new Intent(),PendingIntent .FLAG_ONE_SHOT);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationHelper notificationHelper = new NotificationHelper(getBaseContext());
        NotificationCompat.Builder builder = notificationHelper.getNotificationOldApi(title,body,path,intent,sound);
        notificationHelper.getManager().notify(1,builder.build());
    }

    private void showNotificationActions(String title , String body,String path ,String idClient) {
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
        NotificationCompat.Builder builder = notificationHelper.getNotificationOldApiActions(title,body,path,sound,acceptAction,cancelAction);
        notificationHelper.getManager().notify(2,builder.build());
    }

}