package aukde.food.aukdeliver.paquetes.Adaptadores;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import aukde.food.aukdeliver.R;
import aukde.food.aukdeliver.paquetes.Firestore.FirestoreClass;
import aukde.food.aukdeliver.paquetes.Modelos.Cart;
import aukde.food.aukdeliver.paquetes.Modelos.Order;

public class CartItemsListAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private ArrayList<Cart> list;
    private String title;
    Dialog dialog;

    public CartItemsListAdapter(Context context , ArrayList<Cart> list, String title){
        this.context = context;
        this.list = list;
        this.title = title;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_layout,parent,false);
        return new MyView(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        final Cart model = list.get(position);

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_item_cart);

        if (holder instanceof MyView){
        Glide.with(context).load(model.getImage()).centerCrop().fitCenter().into(((MyView) holder).imageView);
        ((MyView) holder).tv1.setText(model.getTitle());
        ((MyView) holder).tv2.setText("S/"+model.getPrice());
        ((MyView) holder).tv3.setText(model.getCart_quantity());

        if (model.getStatus() == 0){
            ((MyView) holder).tv4.setText("Pendiente");
            ((MyView) holder).tv4.setTextColor(Color.parseColor("#fc0000"));
        }
        else if (model.getStatus() == 1){
            ((MyView) holder).tv4.setText("Procesando");
            ((MyView) holder).tv4.setTextColor(Color.parseColor("#F1C40F"));
        }

        else if (model.getStatus() == -1){
            ((MyView) holder).tv4.setText("Cancelado");
            ((MyView) holder).tv4.setTextColor(Color.parseColor("#fc0000"));
        }

        else if (model.getStatus() == 2){
            ((MyView) holder).tv4.setText("Recibido");
            ((MyView) holder).tv4.setTextColor(Color.parseColor("#5BBD00"));
        }

        else{
                ((MyView) holder).tv4.setText("Completado");
            ((MyView) holder).tv4.setTextColor(Color.parseColor("#5bbd00"));
            }

        if (model.getStatus() == 1){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView name_item = dialog.findViewById(R.id.dialog_name_item);
                    ImageView image_item = dialog.findViewById(R.id.dialog_image_item);
                    ImageView close_dialog = dialog.findViewById(R.id.closeDialog);
                    MaterialButton btnReceiveItem = dialog.findViewById(R.id.btn_receive_item);
                    name_item.setText(model.getTitle());
                    Glide.with(context).load(model.getImage()).centerCrop().fitCenter().into(image_item);
                    dialog.show();
                    close_dialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    btnReceiveItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirestoreClass data = new FirestoreClass();
                            data.updateStatusItem(context,title,position);
                            dialog.dismiss();
                            //Toast.makeText(context,"pos : "+position+" "+model.getProvider_id(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });
        }


        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyView extends RecyclerView.ViewHolder {

        TextView tv1,tv2,tv3,tv4;
        ImageView imageView;

        public MyView(View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tv_cart_item_title);
            tv2 = itemView.findViewById(R.id.tv_cart_item_price);
            tv3 = itemView.findViewById(R.id.tv_cart_quantity);
            tv4 = itemView.findViewById(R.id.tv_cart_item_status);
            imageView = itemView.findViewById(R.id.iv_cart_item_image);


        }
    }
}
