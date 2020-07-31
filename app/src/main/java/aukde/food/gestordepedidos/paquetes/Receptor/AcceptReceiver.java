package aukde.food.gestordepedidos.paquetes.Receptor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import aukde.food.gestordepedidos.paquetes.Actividades.Pedidos.ListaPedidosAukdeliver;

public class AcceptReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent();
        intent1.setClassName(context.getPackageName(), ListaPedidosAukdeliver.class.getName());
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
    }
}
