package aukde.food.gestordepedidos.paquetes.Providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import aukde.food.gestordepedidos.paquetes.Modelos.PedidoLlamada;

public class PedidoProvider {

    DatabaseReference mDatabaseReference;

    public PedidoProvider(){
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("PedidosPorLlamada");
    }

    public Task<Void> Mapear (PedidoLlamada pedidoLlamada){

        Map<String , Object> map = new HashMap<>();
        map.put("numPedido",pedidoLlamada.getNumPedido());
        map.put("horaPedido" , pedidoLlamada.getHoraPedido());
        map.put("fechaPedido" , pedidoLlamada.getFechaPedido());
        map.put("horaEntrega" , pedidoLlamada.getHoraEntrega());
        map.put("fechaEntrega" , pedidoLlamada.getFechaEntrega());
        map.put("proveedores",pedidoLlamada.getProveedores());
        map.put("productos",pedidoLlamada.getProductos());
        map.put("descripcion",pedidoLlamada.getDescripcion());
        map.put("precio1",pedidoLlamada.getPrecio1());
        map.put("precio2",pedidoLlamada.getPrecio2());
        map.put("precio3",pedidoLlamada.getPrecio3());
        map.put("delivery1",pedidoLlamada.getDelivery1());
        map.put("delivery2",pedidoLlamada.getDelivery2());
        map.put("delivery3",pedidoLlamada.getDelivery3());
        map.put("totalPagoProducto",pedidoLlamada.getTotalPagoProducto());
        map.put("nombreCliente",pedidoLlamada.getNombreCliente());
        map.put("telefono",pedidoLlamada.getTelefono());
        map.put("conCuantoVaAPagar",pedidoLlamada.getConCuantoVaAPagar());
        map.put("totalCobro",pedidoLlamada.getTotalCobro());
        map.put("vuelto",pedidoLlamada.getVuelto());
        map.put("direccion",pedidoLlamada.getDireccion());
        map.put("encargado",pedidoLlamada.getEncargado());
        map.put("estado",pedidoLlamada.getEstado());

        return mDatabaseReference.child("pedidos").push().setValue(map);
    }

}
