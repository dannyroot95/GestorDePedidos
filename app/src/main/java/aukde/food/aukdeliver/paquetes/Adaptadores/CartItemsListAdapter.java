package aukde.food.aukdeliver.paquetes.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import aukde.food.aukdeliver.R;
import aukde.food.aukdeliver.paquetes.Modelos.Cart;
import aukde.food.aukdeliver.paquetes.Modelos.Order;

public class CartItemsListAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private ArrayList<Cart> list;

    public CartItemsListAdapter(Context context , ArrayList<Cart> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyView(LayoutInflater.from(context).inflate(R.layout.item_cart_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final Cart model = list.get(position);
        if (holder instanceof MyView){
        Glide.with(context).load(model.getImage()).centerCrop().fitCenter().into(((MyView) holder).imageView);
        ((MyView) holder).tv1.setText(model.getTitle());
        ((MyView) holder).tv2.setText(model.getPrice());
        ((MyView) holder).tv3.setText(model.getCart_quantity());
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyView extends RecyclerView.ViewHolder {

        TextView tv1,tv2,tv3;
        ImageView imageView;

        public MyView(View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tv_cart_item_title);
            tv2 = itemView.findViewById(R.id.tv_cart_item_price);
            tv3 = itemView.findViewById(R.id.tv_cart_quantity);
            imageView = itemView.findViewById(R.id.iv_cart_item_image);

        }
    }
}
