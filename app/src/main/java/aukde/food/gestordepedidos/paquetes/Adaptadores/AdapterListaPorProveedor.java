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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import aukde.food.gestordepedidos.paquetes.Actividades.Usuarios.DetalleProveedor;
import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Menus.MenuProveedor;
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
        holder.txtPosi7Proveedor.setText("Horario Apertura : ");
        holder.txtPosi8Proveedor.setText("Horario Cirre : ");
        holder.txtPosi9Proveedor.setText("Estado : ");

        holder.lineProveedor.setVisibility(View.VISIBLE);

        DateFormat df = new SimpleDateFormat("HH:mm");
        String time = df.format(Calendar.getInstance().getTime());
        /*String HoraApertura=ls.getHoraApertura();
        String HoraCierre=ls.getHoraCierre();*/
        DateFormat horarango1 = new SimpleDateFormat("HH:mm");
        DateFormat horarango2 = new SimpleDateFormat("HH:mm");

        holder.txtNombreProveedor.setText(ls.getNombres());
        holder.txtApellidosProveedor.setText(ls.getApellidos());
        holder.txtTelefonoProveedor.setText(ls.getTelefono());
        holder.txtEmailProveedor.setText(ls.getEmail());
        holder.txtDniProveedor.setText(ls.getDni());
        holder.txtRucProveedor.setText(ls.getRuc());
        holder.txtHorarioApertura.setText(ls.getHoraApertura());
        holder.txtHorarioCierre.setText(ls.getHoraCierre());
        try {
            Date horafinal1 = horarango1.parse(holder.txtHorarioApertura.getText().toString());
            Date horafinal2 = horarango2.parse(holder.txtHorarioCierre.getText().toString());
            Date TimeActual = df.parse(time);

            if(TimeActual.after(horafinal1) && TimeActual.before(horafinal2)){
                holder.txtHorarioEstado.setText("Abierto");
            }

            else{
                holder.txtHorarioEstado.setText("Cerrado");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

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
                ,txtEmailProveedor,txtDniProveedor,txtRucProveedor,
                txtNombreEmpresaProveedor,txtCategoriaProveedor,txtHorarioEstado,txtHorarioApertura,txtHorarioCierre;

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
            txtPosi7Proveedor=itemView.findViewById(R.id.posi7);
            txtPosi8Proveedor=itemView.findViewById(R.id.posi8);
            txtPosi9Proveedor=itemView.findViewById(R.id.posi9);

            txtNombreProveedor = itemView.findViewById(R.id.idNombreProveedor);
            txtApellidosProveedor=itemView.findViewById(R.id.idApellidoProveedor);
            txtTelefonoProveedor=itemView.findViewById(R.id.idTelefonoProveedor);
            txtEmailProveedor=itemView.findViewById(R.id.idEmailProveedor);
            txtDniProveedor=itemView.findViewById(R.id.idDniProveedor);
            txtRucProveedor=itemView.findViewById(R.id.idRucProveedor);

            txtfotoProveedor=itemView.findViewById(R.id.fotoProveedor);
            txtHorarioApertura=itemView.findViewById(R.id.idHorarioApertura);
            txtHorarioCierre=itemView.findViewById(R.id.idHorarioCierre);
            txtHorarioEstado=itemView.findViewById(R.id.idHorarioAtencion);

        }


    }

}
