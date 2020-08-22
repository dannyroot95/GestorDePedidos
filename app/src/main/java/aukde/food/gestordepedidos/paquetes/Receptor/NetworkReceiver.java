package aukde.food.gestordepedidos.paquetes.Receptor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connec.equals(context.getSystemService(Context.CONNECTIVITY_SERVICE))) {
            boolean noConnectivity = intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY, false
            );
            if (noConnectivity) {
                //enviar un extra y obtenerlo en Menu aukdeliver
                Toast.makeText(context, "Disconectado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Connectado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}