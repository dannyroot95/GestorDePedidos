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
import com.squareup.picasso.Picasso;
import java.util.List;
import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Actividades.Pedidos.DetalleSolicitudProveedor;
import aukde.food.gestordepedidos.paquetes.Modelos.ListaSolicitud;
import aukde.food.gestordepedidos.paquetes.Productos.Solicitud.DetalleSolicitudDeProductoParaProveedor;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterListaSolicitud extends RecyclerView.Adapter<AdapterListaSolicitud.viewHolderListaSolicitud> {

    List<ListaSolicitud> listaSolicitudList;
    Context c;
    private final int limit = 30;

    public AdapterListaSolicitud(List<ListaSolicitud> listaSolicitudList) {
        this.listaSolicitudList = listaSolicitudList;
    }

    public AdapterListaSolicitud.viewHolderListaSolicitud onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_lista_solicitud, parent, false);
        AdapterListaSolicitud.viewHolderListaSolicitud holder = new AdapterListaSolicitud.viewHolderListaSolicitud(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolderListaSolicitud holder, final int position) {

        final ListaSolicitud ls = listaSolicitudList.get(position);

        holder.txtPos1.setText("Proveedor : ");
        holder.txtProveedor.setText(ls.getProveedor());
        holder.txtPos2.setText("Cliente : ");
        holder.txtNombreCliente.setText(ls.getNombreCliente());
        holder.txtPos3.setText("Dirección : ");
        holder.txtDireccion.setText(ls.getDireccionCliente());
        holder.txtPos4.setText("Estado : ");
        holder.txtEstado.setText(ls.getEstado());

        Picasso
                .get()
                .load(ls.getPhoto())
                .fit()
                .error(R.drawable.ic_error)
                .centerCrop()
                .into(holder.circleImgProveedor);

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
                Intent intent = new Intent(v.getContext(), DetalleSolicitudProveedor.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("key",ls);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return Math.min(listaSolicitudList.size(), limit);
    }

    public class viewHolderListaSolicitud extends RecyclerView.ViewHolder{

        TextView txtPos1 , txtPos2 , txtPos3,txtPos4 , txtProveedor , txtNombreCliente , txtDireccion, txtEstado;
        CircleImageView circleImgProveedor;

        public viewHolderListaSolicitud(@NonNull final View itemView){
            super(itemView);

            txtPos1 = itemView.findViewById(R.id.pos1);
            txtPos2 = itemView.findViewById(R.id.pos2);
            txtPos3 = itemView.findViewById(R.id.pos3);
            txtPos4 = itemView.findViewById(R.id.pos4);
            txtProveedor = itemView.findViewById(R.id.listNombreProveedor);
            txtNombreCliente = itemView.findViewById(R.id.listNombreCliente);
            txtDireccion = itemView.findViewById(R.id.listDireccionCliente);
            txtEstado = itemView.findViewById(R.id.listEstadoSolicitud);
            circleImgProveedor = itemView.findViewById(R.id.imgProveedor);


        }

    }

}