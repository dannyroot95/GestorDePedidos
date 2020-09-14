package aukde.food.gestordepedidos.paquetes.Adaptadores;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Actividades.Pedidos.DetallePedidoAukdeliver;
import aukde.food.gestordepedidos.paquetes.Modelos.PedidoLlamada;

public class AdapterPedidoPorLlamadaAukdeliver extends RecyclerView.Adapter<AdapterPedidoPorLlamadaAukdeliver.viewHolderPedidos> {

    List<PedidoLlamada> pedidoLlamadaList;
    Context c;
    private final int limit = 20;

    public AdapterPedidoPorLlamadaAukdeliver(List<PedidoLlamada> pedidoLlamadaList) {
        this.pedidoLlamadaList = pedidoLlamadaList;
    }

    @NonNull
    @Override
    public viewHolderPedidos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pedidos,parent,false);
        viewHolderPedidos holder = new viewHolderPedidos(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolderPedidos holder, int position) {

        final PedidoLlamada ls = pedidoLlamadaList.get(position);

        holder.txtPos1.setText("Número de pedido : ");
        holder.txtPos2.setText("Entregar Pedido a las : ");
        holder.txtPos3.setText("Fecha de entrega : ");
        holder.txtPos4.setText("Cliente : ");
        holder.txtPos5.setText("Dirección : ");
        holder.txtPos6.setText("Estado : ");

        holder.line.setVisibility(View.VISIBLE);

        holder.txtTotalProducto.setText(ls.getTotalPagoProducto());
        holder.txtDireccion.setText(ls.getDireccion());
        holder.txtFechaList.setText(ls.getFechaEntrega());
        holder.txtHoraList.setText(ls.getHoraEntrega());
        holder.txtHoraRegistro.setText(ls.getHoraPedido());
        holder.txtFechaRegistro.setText(ls.getFechaPedido());
        holder.txtClienteList.setText(ls.getNombreCliente());
        holder.txtNumeroPedido.setText(ls.getNumPedido());
        holder.txtListEstado.setText(ls.getEstado());
        holder.txttelefono.setText(ls.getTelefono());
        holder.txtClientePaga.setText(ls.getConCuantoVaAPagar());
        holder.txtTotalACobrar.setText(ls.getTotalCobro());
        holder.txtVuelto.setText(ls.getVuelto());
        holder.txtRepartidor.setText(ls.getEncargado());
        holder.txtProveedor.setText(ls.getProveedores());
        holder.txtProductos.setText(ls.getProductos());
        holder.txtProductDescripcion.setText(ls.getDescripcion());
        holder.txtLatitud.setText(ls.getLatitud());
        holder.txtLongitud.setText(ls.getLongitud());
        holder.txtPreciosUnitarios.setText(ls.getPrecioUnitario());
        holder.txtCantidad.setText(ls.getCantidad());
        holder.txtPrecioTotalXproducto.setText(ls.getPrecioTotalXProducto());
        holder.txtComision.setText(ls.getComision());
        holder.txtTotalDelivery.setText(ls.getTotalDelivery());
        holder.txtGananciaDelivery.setText(ls.getGananciaDelivery());
        holder.txtGananciaComision.setText(ls.getGananciaComision());


        if ( holder.txtListEstado.getText().toString().equals("En espera")){
            holder.txtListEstado.setTextColor(Color.parseColor("#2E86C1"));
        }

        if ( holder.txtListEstado.getText().toString().equals("Completado")){
            holder.txtListEstado.setTextColor(Color.parseColor("#5bbd00"));
        }

        if ( holder.txtListEstado.getText().toString().equals("Cancelado")){
            holder.txtListEstado.setTextColor(Color.parseColor("#E74C3C"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog mDialog;
                mDialog = new ProgressDialog(v.getContext(),R.style.ThemeOverlay);
                mDialog.show();
                mDialog.setCancelable(false);
                mDialog.setMessage("Cargando...");
                final Vibrator vibrator;
                final long tiempo = 100;
                vibrator = (Vibrator) v.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(tiempo);
                Intent intent = new Intent(v.getContext(), DetallePedidoAukdeliver.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("key",ls);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        //Limitar el numero de pedidos en la lista de pedidos
        // mostrar todos los pedidos ->  return pedidoLlamadaList.size();
        return Math.min(pedidoLlamadaList.size(), limit);
    }

    public class viewHolderPedidos extends RecyclerView.ViewHolder {
        TextView txtNumeroPedido,txtHoraList,txtFechaList,txtClienteList,txtDireccion,txtPos1,
                txtPos2,txtPos3,txtPos4,txtPos5,txtPos6,txtListEstado,txttelefono,txtHoraRegistro,
                txtFechaRegistro , txtTotalProducto , txtClientePaga , txtTotalACobrar , txtVuelto
                , txtRepartidor , txtProveedor , txtProductos , txtProductDescripcion ,txtLatitud,txtLongitud
                ,txtPreciosUnitarios , txtCantidad , txtPrecioTotalXproducto , txtComision ,
                txtTotalDelivery ,txtGananciaDelivery,txtGananciaComision;


        LinearLayout line,LinearPedidos;

        public viewHolderPedidos(@NonNull final View itemView) {
            super(itemView);
            line = itemView.findViewById(R.id.linearButtons);
            txtPos1 = itemView.findViewById(R.id.pos1);
            txtPos2 = itemView.findViewById(R.id.pos2);
            txtPos3 = itemView.findViewById(R.id.pos3);
            txtPos4 = itemView.findViewById(R.id.pos4);
            txtPos5 = itemView.findViewById(R.id.pos5);
            txtPos6 = itemView.findViewById(R.id.pos6);
            txtTotalProducto = itemView.findViewById(R.id.idTotalPagoProducto);
            txtDireccion = itemView.findViewById(R.id.listDireccion);
            txtFechaList = itemView.findViewById(R.id.listFecha);
            txtHoraList = itemView.findViewById(R.id.listHora);
            txtHoraRegistro = itemView.findViewById(R.id.idHoraRegistro);
            txtFechaRegistro = itemView.findViewById(R.id.idFechaRegistro);
            txtClienteList = itemView.findViewById(R.id.listCliente);
            txtNumeroPedido = itemView.findViewById(R.id.numPedido);
            txtListEstado = itemView.findViewById(R.id.listEstado);
            txttelefono = itemView.findViewById(R.id.idTelefonoList);
            txtClientePaga = itemView.findViewById(R.id.idClientePaga);
            txtTotalACobrar = itemView.findViewById(R.id.idTotalCobro);
            txtVuelto = itemView.findViewById(R.id.idVuelto);
            txtProveedor = itemView.findViewById(R.id.idProveedor);
            txtProductos = itemView.findViewById(R.id.idProducto);
            txtProductDescripcion = itemView.findViewById(R.id.idProductoDescripcion);
            txtRepartidor = itemView.findViewById(R.id.idRepartidor);
            txtPreciosUnitarios = itemView.findViewById(R.id.idPreciosUnitarios);
            txtCantidad = itemView.findViewById(R.id.idCantidad);
            txtPrecioTotalXproducto = itemView.findViewById(R.id.idPrecioTotalPorProducto);
            txtComision = itemView.findViewById(R.id.idComision);
            txtTotalDelivery = itemView.findViewById(R.id.idTotalDelivery);
            txtGananciaDelivery = itemView.findViewById(R.id.idGananciaDelivery);
            txtGananciaComision = itemView.findViewById(R.id.idGananciaComision);
            LinearPedidos = itemView.findViewById(R.id.linearContenedorExtra);
            txtLatitud = itemView.findViewById(R.id.idlatitud);
            txtLongitud = itemView.findViewById(R.id.idlongitud);
            LinearPedidos = itemView.findViewById(R.id.linearContenedorExtra);
            int alto = 0;
            int ancho = 0;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ancho,alto);
            LinearPedidos.setLayoutParams(params);

        }
    }

}