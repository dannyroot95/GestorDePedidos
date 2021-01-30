package aukde.food.aukdeliver.paquetes.Receptor;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;

import aukde.food.aukdeliver.R;
import aukde.food.aukdeliver.paquetes.Menus.MenuAukdeliver;

public class GpsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Toast.makeText(context, "Activo", Toast.LENGTH_SHORT).show();
            Intent pushIntent = new Intent(context, MenuAukdeliver.class);
            context.startService(pushIntent);
        }
        else {
            //Toast.makeText(context, "Apagado", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Error");
            builder.setCancelable(false);
            builder.setIcon(R.drawable.ic_nogps);
            builder.setMessage("Sin acceso a GPS\nActívelo para recibir sus pedidos!");
            builder.setNegativeButton("Reintentar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent();
                    i.setClassName("aukde.food.aukdeliver", "aukde.food.aukdeliver.paquetes.Menus.MenuAukdeliver");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    context.startActivity(i);
                }
            });
            builder.create();
            builder.show();
        }

    }

}
