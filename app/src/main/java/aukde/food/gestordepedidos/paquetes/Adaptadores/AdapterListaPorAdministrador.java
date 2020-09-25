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

import aukde.food.gestordepedidos.paquetes.Actividades.Usuarios.Detalle_Administrador;
import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Modelos.ListaAdministrador;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterListaPorAdministrador extends RecyclerView.Adapter<AdapterListaPorAdministrador.viewHolderUsuarios> {

    List<ListaAdministrador> AdministradorLlamadaList;
    Context c;
    private final int limit = 30;


    public AdapterListaPorAdministrador(List<ListaAdministrador> usuariosLlamadaList) {
        this.AdministradorLlamadaList = usuariosLlamadaList;
    }

    @NonNull
    @Override
    public viewHolderUsuarios onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_lista_administrador,parent,false);
        viewHolderUsuarios holder = new viewHolderUsuarios(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolderUsuarios holder, final int position) {

            final ListaAdministrador ls = AdministradorLlamadaList.get(position);

            holder.txtPos1.setText("Nombre : ");
            holder.txtPos2.setText("Apellidos : ");
            holder.txtPos3.setText("Dni : ");
            holder.txtPos4.setText("Email : ");
            holder.txtPos5.setText("Telefono : ");

            holder.line.setVisibility(View.VISIBLE);

            holder.txtNombre.setText(ls.getNombres());
            holder.txtApellidos.setText(ls.getApellidos());
            holder.txtDni.setText(ls.getDni());
            holder.txtEmail.setText(ls.getEmail());
            holder.txtTelefono.setText(ls.getTelefono());
            Picasso
                .get()
                .load(ls.getFoto())
                .fit()
                .error(R.drawable.ic_error)
                .placeholder(R.drawable.loaderrosa)
                .centerCrop()
                .into(holder.txtfoto);

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
                    Intent intent = new Intent(v.getContext(), Detalle_Administrador.class);
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
        return Math.min(AdministradorLlamadaList.size(), limit);
    }

    public class viewHolderUsuarios extends RecyclerView.ViewHolder {

        TextView txtNombre,txtApellidos,txtDni,txtEmail,txtTelefono ,txtPos1, txtPos2,txtPos3,txtPos4,txtPos5,txtPos6;
        CircleImageView txtfoto;

        LinearLayout line,LinearUsuarios;

        public viewHolderUsuarios(@NonNull final View itemView) {
            super(itemView);
            line = itemView.findViewById(R.id.linearButtons);
            txtPos1 = itemView.findViewById(R.id.pos1);
            txtPos2 = itemView.findViewById(R.id.pos2);
            txtPos3 = itemView.findViewById(R.id.pos3);
            txtPos4 = itemView.findViewById(R.id.pos4);
            txtPos5 = itemView.findViewById(R.id.pos5);
            txtNombre = itemView.findViewById(R.id.idNombre);
            txtApellidos=itemView.findViewById(R.id.idApellido);
            txtDni=itemView.findViewById(R.id.idDni);
            txtEmail=itemView.findViewById(R.id.idEmail);
            txtTelefono=itemView.findViewById(R.id.idTelefono);
            txtfoto=itemView.findViewById(R.id.fotoAdministrador);


            /*LinearUsuarios = itemView.findViewById(R.id.linearContenedorExtra);
            int alto = 0;
            int ancho = 0;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ancho,alto);
            LinearUsuarios.setLayoutParams(params);*/


        }
    }

}
