package aukde.food.aukdeliver.paquetes.Servicios;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import static aukde.food.aukdeliver.paquetes.Utils.ApplicationCronometro.CHANNEL_ID;
import java.util.Timer;
import java.util.TimerTask;

import aukde.food.aukdeliver.R;
import aukde.food.aukdeliver.paquetes.Actividades.Notificacion;

public class ForegroundServiceCronometro extends Service {

    private Timer temporizador = new Timer();
    private static final long INTERVALO_ACTUALIZACION = 10; // En ms
    public static Notificacion UPDATE_LISTENER;
    private double cronometro = 0;
    private Handler handler;

    public ForegroundServiceCronometro(){

    }

    public static void setUpdateListener(Notificacion CronometroAukde) {
        UPDATE_LISTENER = CronometroAukde;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                UPDATE_LISTENER.actualizarCronometro(cronometro);
            }
        };
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        iniciarCronometro();
        String input = intent.getStringExtra("inputExtra");
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
        pararCronometro();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void iniciarCronometro() {
        temporizador.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                cronometro += 0.01;
                handler.sendEmptyMessage(0);
            }
        }, 0, INTERVALO_ACTUALIZACION);
    }

    private void pararCronometro() {
        if (temporizador != null)
            temporizador.cancel();
    }
}
