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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Modelos.ProductoDefault;
import aukde.food.gestordepedidos.paquetes.Productos.Default.DetalleAdicionales;
import aukde.food.gestordepedidos.paquetes.Productos.Default.DetalleBebidas;

public class AdapterBebidas  extends RecyclerView.Adapter<AdapterBebidas.viewHolderBebidas> {


    List<ProductoDefault> productoDefaultList;
    Context c;

    public AdapterBebidas(List<ProductoDefault> productoDefaultList){
        this.productoDefaultList = productoDefaultList;
    }


    @NonNull
    @Override
    public AdapterBebidas.viewHolderBebidas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_productos,parent,false);
        AdapterBebidas.viewHolderBebidas holder = new AdapterBebidas.viewHolderBebidas(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull AdapterBebidas.viewHolderBebidas holder, int position) {

        final ProductoDefault ls = productoDefaultList.get(position);

        holder.txtPosProducto.setText("Producto : ");
        holder.txtPosStock.setText("Disponibilidad : ");
        holder.txtPosCodigo.setText("Código : ");
        holder.txtProducto.setText(ls.getNombreProducto());
        holder.txtEstado.setText(ls.getDisponibilidad());
        holder.txtCodigo.setText(ls.getCodigoINEA());
        Picasso
                .get()
                .load(ls.getUrlPhoto())
                .fit()
                .error(R.drawable.ic_error)
                .placeholder(R.drawable.loaderrosa)
                .centerCrop()
                .into(holder.photoProducto);

        if ( holder.txtEstado.getText().toString().equals("Disponible")){
            holder.txtEstado.setTextColor(Color.parseColor("#5bbd00"));
        }

        if ( holder.txtEstado.getText().toString().equals("No disponible")){
            holder.txtEstado.setTextColor(Color.parseColor("#E74C3C"));
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
                Intent intent = new Intent(v.getContext(), DetalleBebidas.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("key",ls);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });


    }


    @Override
    public int getItemCount() {
        return productoDefaultList.size();
    }

    public class viewHolderBebidas extends RecyclerView.ViewHolder

    {

        TextView txtProducto , txtEstado , txtCodigo , txtUrl ;
        TextView txtPosProducto , txtPosStock , txtPosCodigo ;
        ImageView photoProducto;

        public viewHolderBebidas(@NonNull final View itemView){
            super(itemView);
            txtPosProducto = itemView.findViewById(R.id.pos1);
            txtPosStock = itemView.findViewById(R.id.pos2);
            txtPosCodigo = itemView.findViewById(R.id.pos3);
            txtProducto = itemView.findViewById(R.id.listNombreProducto);
            txtEstado = itemView.findViewById(R.id.listStock);
            txtCodigo = itemView.findViewById(R.id.listCodigoINEA);
            photoProducto = itemView.findViewById(R.id.imgProducto);
            txtUrl = itemView.findViewById(R.id.url);

        }

    }

}

