package aukde.food.gestordepedidos.paquetes.Utils;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class ApplicationCronometro extends Application {
    public static final String CHANNEL_ID = "cronometroServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();

    }
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID,"EL pedido se está procesando", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager =getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);

        }
    }
}
