package aukde.food.gestordepedidos.paquetes.Receptor;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;

import aukde.food.gestordepedidos.R;


public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connec.equals(context.getSystemService(Context.CONNECTIVITY_SERVICE))) {
            boolean noConnectivity = intent.getBooleanExtra(
                    ConnectivityManager.EXTRA_NO_CONNECTIVITY, false
            );
            if (noConnectivity) {
                //enviar un extra y obtenerlo en Menu aukdeliver
                //Toast.makeText(context, "Desconectado", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Error");
                builder.setCancelable(false);
                builder.setIcon(R.drawable.ic_nowifi);
                builder.setMessage("Sin conexión a internet\nActívelo para recibir sus pedidos!");
                builder.setNegativeButton("Reintentar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent();
                        i.setClassName("aukde.food.gestordepedidos", "aukde.food.gestordepedidos.paquetes.Menus.MenuAukdeliver");
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(i);
                    }
                });
                builder.create();
                builder.show();

            } else {
                //Toast.makeText(context, "Conectado", Toast.LENGTH_SHORT).show();
            }
        }
    }

}