package aukde.food.aukdeliver.paquetes.Servicios;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static aukde.food.aukdeliver.paquetes.Utils.ApplicationCronometro.CHANNEL_ID;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import aukde.food.aukdeliver.R;
import aukde.food.aukdeliver.paquetes.Actividades.Notificacion;

public class ForegroundServiceCronometro extends Service {

    public static Notificacion UPDATE_LISTENER;

    public ForegroundServiceCronometro(){
    }

    public static void setUpdateListener(Notificacion CronometroAukde) {
        UPDATE_LISTENER = CronometroAukde;
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification= new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle("Pedido en proceso")
                .setContentText("El pedido se está procesando")
                .setUsesChronometer(true)
                .setSmallIcon(R.drawable.ic_notificacion)
                .setColor(getResources().getColor(R.color.errorColor))
                .setOngoing(true)
                .setAutoCancel(false)
                .build();
        startForeground(1,notification);
        //return START_NOT_STICKY;
        return  Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
