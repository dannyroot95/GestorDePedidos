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
import aukde.food.gestordepedidos.paquetes.Menus.ListaUsuarios.DetalleAukdeliver;
import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Modelos.ListaAukdelivery;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterListaPorAukdeliver extends RecyclerView.Adapter<AdapterListaPorAukdeliver.viewHolderAukdeDelivery > {

    List<ListaAukdelivery> AukdeDeliveryLlamadaList;
    Context c;
    private final int limit = 30;


    public AdapterListaPorAukdeliver(List<ListaAukdelivery> AukdeDeliveryLlamadaList) {
        this.AukdeDeliveryLlamadaList = AukdeDeliveryLlamadaList;
    }

    @NonNull
    @Override
    public viewHolderAukdeDelivery onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_lista_aukdelivery,parent,false);
        viewHolderAukdeDelivery holder = new viewHolderAukdeDelivery(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final viewHolderAukdeDelivery holder, final int position) {

            final ListaAukdelivery ls = AukdeDeliveryLlamadaList.get(position);

            holder.txtPosi1AukdeDelivery.setText("Nombres : ");
            holder.txtPosi2AukdeDelivery.setText("Apellidos : ");
            holder.txtPosi3AukdeDelivery.setText("NombreUsuario : ");
            holder.txtPosi4AukdeDelivery.setText("Telefono : ");
            holder.txtPosi5AukdeDelivery.setText("Email : ");
            holder.txtPosi6AukdeDelivery.setText("Categotia Licencia : ");

            holder.lineAukdeDelivery.setVisibility(View.VISIBLE);

            holder.txtNombreAukdeDelivery.setText(ls.getNombres());
            holder.txtApellidosAukdeDelivery.setText(ls.getApellidos());
            holder.txtUsuarioNombreAukdeDelivery.setText(ls.getUsername());
            holder.txtTelefonoAukdeDelivery.setText(ls.getTelefono());
            holder.txtEmailAukdeDelivery.setText(ls.getEmail());
            holder.txtCategoriaLicencia.setText(ls.getCategoria_licencia());

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
                    Intent intent = new Intent(v.getContext(), DetalleAukdeliver.class);
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
        return Math.min(AukdeDeliveryLlamadaList.size(), limit);
    }

    public class viewHolderAukdeDelivery extends RecyclerView.ViewHolder {


       TextView txtNombreAukdeDelivery,txtApellidosAukdeDelivery,txtUsuarioNombreAukdeDelivery,txtTelefonoAukdeDelivery
        ,txtEmailAukdeDelivery,txtCategoriaLicencia;

       TextView txtPosi1AukdeDelivery, txtPosi2AukdeDelivery,txtPosi3AukdeDelivery,
               txtPosi4AukdeDelivery,txtPosi5AukdeDelivery,txtPosi6AukdeDelivery;

        CircleImageView txtfoto;



        LinearLayout lineAukdeDelivery,LinearAukdeDelivery;

        public viewHolderAukdeDelivery(@NonNull final View itemView) {
            super(itemView);
            lineAukdeDelivery = itemView.findViewById(R.id.linearButtonsAukdeDelivery);
            txtPosi1AukdeDelivery = itemView.findViewById(R.id.posi1);
            txtPosi2AukdeDelivery = itemView.findViewById(R.id.posi2);
            txtPosi3AukdeDelivery = itemView.findViewById(R.id.posi3);
            txtPosi4AukdeDelivery = itemView.findViewById(R.id.posi4);
            txtPosi5AukdeDelivery = itemView.findViewById(R.id.posi5);
            txtPosi6AukdeDelivery=itemView.findViewById(R.id.posi6);


            txtNombreAukdeDelivery = itemView.findViewById(R.id.idNombreAukdeDelivery);
            txtApellidosAukdeDelivery=itemView.findViewById(R.id.idApellidoAukdeDelivery);
            txtUsuarioNombreAukdeDelivery=itemView.findViewById(R.id.idUsuarioNombreAukdeDelivery);
            txtTelefonoAukdeDelivery=itemView.findViewById(R.id.idTelefonoAukdeDelivery);
            txtEmailAukdeDelivery=itemView.findViewById(R.id.idEmailAukdeDelivery);
            txtCategoriaLicencia=itemView.findViewById(R.id.idCategoriaLicAukdeliver);

            txtfoto=itemView.findViewById(R.id.fotoAukdeliver);


            /*LinearAukdeDelivery = itemView.findViewById(R.id.linearContenedorExtraAukdeDelivery);
            int alto = 0;
            int ancho = 0;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearAukdeDelivery.setLayoutParams(params);*/



        }
    }

}
