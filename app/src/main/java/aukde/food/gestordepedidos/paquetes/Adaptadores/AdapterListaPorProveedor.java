package aukde.food.gestordepedidos.paquetes.Adaptadores;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;
import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Menus.ListaUsuarios.DetalleProveedor;
import aukde.food.gestordepedidos.paquetes.Modelos.ListaProveedor;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterListaPorProveedor extends RecyclerView.Adapter<AdapterListaPorProveedor.viewHolderProveedor > {

    List<ListaProveedor> ProveedorLlamadaList;
    Context c;
    private final int limit = 30;

    public AdapterListaPorProveedor(List<ListaProveedor> ProveedorLlamadaList) {
        this.ProveedorLlamadaList = ProveedorLlamadaList;
    }

    @NonNull
    @Override
    public viewHolderProveedor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_lista_proveedor,parent,false);
        AdapterListaPorProveedor.viewHolderProveedor holder = new viewHolderProveedor(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterListaPorProveedor.viewHolderProveedor holder, final int position) {


        final ListaProveedor ls = ProveedorLlamadaList.get(position);

        holder.txtPosi1Proveedor.setText("Nombres : ");
        holder.txtPosi2Proveedor.setText("Apellidos : ");
        holder.txtPosi3Proveedor.setText("Telefono : ");
        holder.txtPosi4Proveedor.setText("Email : ");
        holder.txtPosi5Proveedor.setText("Dni : ");
        holder.txtPosi6Proveedor.setText("Ruc : ");


        holder.lineProveedor.setVisibility(View.VISIBLE);

        holder.txtNombreProveedor.setText(ls.getNombres());
        holder.txtApellidosProveedor.setText(ls.getApellidos());
        holder.txtTelefonoProveedor.setText(ls.getTelefono());
        holder.txtEmailProveedor.setText(ls.getEmail());
        holder.txtDniProveedor.setText(ls.getDni());
        holder.txtRucProveedor.setText(ls.getRuc());

        Picasso
                .get()
                .load(ls.getFoto())
                .fit()
                .error(R.drawable.ic_error)
                .placeholder(R.drawable.loaderrosa)
                .centerCrop()
                .into(holder.txtfotoProveedor);


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
                Intent intent = new Intent(v.getContext(), DetalleProveedor.class);
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
        return Math.min(ProveedorLlamadaList.size(), limit);
    }

    public class viewHolderProveedor extends RecyclerView.ViewHolder {

        TextView txtNombreProveedor,txtApellidosProveedor,txtUsuarioNombreProveedor,txtTelefonoProveedor
                ,txtEmailProveedor,txtDniProveedor,txtRucProveedor,txtNombreEmpresaProveedor,txtCategoriaProveedor;

        TextView txtPosi1Proveedor, txtPosi2Proveedor,txtPosi3Proveedor,
                txtPosi4Proveedor,txtPosi5Proveedor,txtPosi6Proveedor,txtPosi7Proveedor,txtPosi8Proveedor,txtPosi9Proveedor;

        CircleImageView txtfotoProveedor;

        LinearLayout lineProveedor;

        public viewHolderProveedor(@NonNull final View itemView) {
            super(itemView);
            lineProveedor = itemView.findViewById(R.id.linearButtonsProveedor);
            txtPosi1Proveedor = itemView.findViewById(R.id.posi1);
            txtPosi2Proveedor = itemView.findViewById(R.id.posi2);
            txtPosi3Proveedor = itemView.findViewById(R.id.posi3);
            txtPosi4Proveedor = itemView.findViewById(R.id.posi4);
            txtPosi5Proveedor = itemView.findViewById(R.id.posi5);
            txtPosi6Proveedor=itemView.findViewById(R.id.posi6);

            txtNombreProveedor = itemView.findViewById(R.id.idNombreProveedor);
            txtApellidosProveedor=itemView.findViewById(R.id.idApellidoProveedor);
            txtTelefonoProveedor=itemView.findViewById(R.id.idTelefonoProveedor);
            txtEmailProveedor=itemView.findViewById(R.id.idEmailProveedor);
            txtDniProveedor=itemView.findViewById(R.id.idDniProveedor);
            txtRucProveedor=itemView.findViewById(R.id.idRucProveedor);

            txtfotoProveedor=itemView.findViewById(R.id.fotoProveedor);

        }


    }

}
