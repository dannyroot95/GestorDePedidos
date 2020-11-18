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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Actividades.Usuarios.DetalleAdministrador;
import aukde.food.gestordepedidos.paquetes.Modelos.ListaSolicitudProducto;
import aukde.food.gestordepedidos.paquetes.Productos.Solicitud.DetalleSolicitudDeProductoParaProveedor;

public class AdapterListaSolicitudProductoProveedor  extends RecyclerView.Adapter<AdapterListaSolicitudProductoProveedor.viewHolderListaSolicitudProducto> {
    List<ListaSolicitudProducto> listaSolicitudProductoList;
    Context c;
    private final int limit = 30;

    public AdapterListaSolicitudProductoProveedor(List<ListaSolicitudProducto> listaSolicitudProductoList) {
        this.listaSolicitudProductoList = listaSolicitudProductoList;
    }

    public AdapterListaSolicitudProductoProveedor.viewHolderListaSolicitudProducto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_lista_solicitud_producto, parent, false);
        AdapterListaSolicitudProductoProveedor.viewHolderListaSolicitudProducto holder = new AdapterListaSolicitudProductoProveedor.viewHolderListaSolicitudProducto(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterListaSolicitudProductoProveedor.viewHolderListaSolicitudProducto holder, final int position) {

        final ListaSolicitudProducto ls = listaSolicitudProductoList.get(position);

        holder.txtPos1.setText("Proveedor : ");
        holder.txtProveedor.setText(ls.getNombreSocio());
        holder.txtPos2.setText("Productos : ");

        String desc [] = ls.getNombreProducto().split("\n");
        StringBuilder builder = new StringBuilder();
        for (int i = 0 ; i<desc.length - 1 ; i++){
            builder.append(desc[i]+", ");
        }
        String joined = builder.toString();
        holder.txtNombreProductos.setText(joined);
        holder.txtPos3.setText("Estado : ");
        holder.txtEstado.setText(ls.getEstado());


        if ( holder.txtEstado.getText().toString().equals("Sin confirmar")){
            holder.txtEstado.setTextColor(Color.parseColor("#FC0000"));
        }
        else {
            holder.txtEstado.setTextColor(Color.parseColor("#5bbd00"));
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
                Intent intent = new Intent(v.getContext(), DetalleSolicitudDeProductoParaProveedor.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("key",ls);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return Math.min(listaSolicitudProductoList.size(), limit);
    }

    public class viewHolderListaSolicitudProducto extends RecyclerView.ViewHolder{

        TextView txtPos1 , txtPos2 , txtPos3 , txtProveedor , txtNombreProductos , txtEstado;

        public viewHolderListaSolicitudProducto(@NonNull final View itemView){
            super(itemView);

            txtPos1 = itemView.findViewById(R.id.pos1);
            txtPos2 = itemView.findViewById(R.id.pos2);
            txtPos3 = itemView.findViewById(R.id.pos3);
            txtProveedor = itemView.findViewById(R.id.listNombreProveedor);
            txtNombreProductos = itemView.findViewById(R.id.listNombreProducto);
            txtEstado = itemView.findViewById(R.id.listEstado);

        }

    }

}

