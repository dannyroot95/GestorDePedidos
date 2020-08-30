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
    map.put("precioUnitario",pedidoLlamada.getPrecioUnitario());
    map.put("cantidad",pedidoLlamada.getCantidad());
    map.put("precioTotalXProducto",pedidoLlamada.getPrecioTotalXProducto());
    map.put("comision",pedidoLlamada.getComision());
    map.put("totalDelivery",pedidoLlamada.getTotalDelivery());
    map.put("gananciaDelivery",pedidoLlamada.getGananciaDelivery());
    map.put("gananciaComision",pedidoLlamada.getGananciaComision());
    map.put("totalPagoProducto",pedidoLlamada.getTotalPagoProducto());
    map.put("nombreCliente",pedidoLlamada.getNombreCliente());
    map.put("telefono",pedidoLlamada.getTelefono());
    map.put("conCuantoVaAPagar",pedidoLlamada.getConCuantoVaAPagar());
    map.put("totalCobro",pedidoLlamada.getTotalCobro());
    map.put("vuelto",pedidoLlamada.getVuelto());
    map.put("direccion",pedidoLlamada.getDireccion());
    map.put("referencia",pedidoLlamada.getReferencia());
    map.put("encargado",pedidoLlamada.getEncargado());
    map.put("estado",pedidoLlamada.getEstado());
    map.put("latitud",pedidoLlamada.getLatitud());
    map.put("longitud",pedidoLlamada.getLongitud());
    
    return mDatabaseReference.child("pedidos").push().setValue(map);
  }

}