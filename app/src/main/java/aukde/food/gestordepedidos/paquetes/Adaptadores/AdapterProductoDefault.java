package aukde.food.gestordepedidos.paquetes.Adaptadores;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Actividades.Pedidos.DetallePedidoAukdeliver;
import aukde.food.gestordepedidos.paquetes.Modelos.ProductoDefault;
import aukde.food.gestordepedidos.paquetes.Productos.Default.DetalleProductoDefault;

public class AdapterProductoDefault extends RecyclerView.Adapter<AdapterProductoDefault.viewHolderProductoDefault> {


    List<ProductoDefault> productoDefaultList;
    Context c;

    public AdapterProductoDefault(List<ProductoDefault> productoDefaultList){
        this.productoDefaultList = productoDefaultList;
    }


    @NonNull
    @Override
    public viewHolderProductoDefault onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_productos,parent,false);
        AdapterProductoDefault.viewHolderProductoDefault holder = new viewHolderProductoDefault(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull viewHolderProductoDefault holder, int position) {

        final ProductoDefault ls = productoDefaultList.get(position);

        holder.txtPosProducto.setText("Producto : ");
        holder.txtPosStock.setText("Stock : ");
        holder.txtPosCodigo.setText("Código : ");
        holder.txtProducto.setText(ls.getNombreProducto());
        holder.txtStock.setText(ls.getStock());
        holder.txtCodigo.setText(ls.getCodigoINEA());
        Picasso
                .get()
                .load(ls.getUrlPhoto())
                .fit()
                .error(R.drawable.ic_error)
                .placeholder(R.drawable.loaderrosa)
                .centerCrop()
                .into(holder.photoProducto);

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
                Intent intent = new Intent(v.getContext(), DetalleProductoDefault.class);
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

    public class viewHolderProductoDefault extends RecyclerView.ViewHolder{

        TextView txtProducto , txtStock , txtCodigo , txtUrlPhoto , txtUrl;
        TextView txtPosProducto , txtPosStock , txtPosCodigo , txtPosUrlPhoto;
        ImageView photoProducto;

        public viewHolderProductoDefault(@NonNull final View itemView){
            super(itemView);
            txtPosProducto = itemView.findViewById(R.id.pos1);
            txtPosStock = itemView.findViewById(R.id.pos2);
            txtPosCodigo = itemView.findViewById(R.id.pos3);
            txtProducto = itemView.findViewById(R.id.listNombreProducto);
            txtStock = itemView.findViewById(R.id.listStock);
            txtCodigo = itemView.findViewById(R.id.listCodigoINEA);
            photoProducto = itemView.findViewById(R.id.imgProducto);
            txtUrl = itemView.findViewById(R.id.url);

        }

    }

}
