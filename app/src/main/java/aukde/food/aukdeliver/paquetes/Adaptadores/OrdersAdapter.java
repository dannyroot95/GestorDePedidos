package aukde.food.aukdeliver.paquetes.Adaptadores;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import aukde.food.aukdeliver.R;
import aukde.food.aukdeliver.paquetes.Actividades.Pedidos.DetailOrder;
import aukde.food.aukdeliver.paquetes.Modelos.Order;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.viewHolder>{

    List<Order> orders;
    Activity context;

    public OrdersAdapter(List<Order> ordersCTX , Activity context){
        this.orders = ordersCTX;
        this.context = context;
    }

    @NonNull
    @Override
    public OrdersAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_layout,parent,false);
        return new viewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.viewHolder holder, int position) {


        final Order ls = orders.get(position);
        String dateFormat = "dd MMM yyyy HH:mm";
        Date date = new Date(ls.getOrder_datetime());
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());


        holder.tv1.setText(ls.getTitle());
        holder.tv2.setText("S/"+ls.getTotal_amount());
        holder.tv3.setText(formatter.format(date));
        holder.tv4.setText(ls.getStatus().toString());

        if (holder.tv4.getText().toString().equals("0")){
            holder.tv4.setText("Pendiente");
            holder.tv4.setTextColor(Color.parseColor("#FC0000"));
        }
        else if(holder.tv4.getText().toString().equals("1")){
            holder.tv4.setText("Procesando");
            holder.tv4.setTextColor(Color.parseColor("#F1C40F"));
        }

        else if(holder.tv4.getText().toString().equals("2")){
            holder.tv4.setText("Recibido");
            holder.tv4.setTextColor(Color.parseColor("#154360"));
        }
        else if(holder.tv4.getText().toString().equals("3")){
            holder.tv4.setText("Completado");
            holder.tv4.setTextColor(Color.parseColor("#5BBD00"));
        }

        if (ls.getStatus() != 3){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailOrder.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("detail",ls);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    context.finish();
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        TextView tv1,tv2,tv3,tv4;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            tv1 = itemView.findViewById(R.id.tv_item_name);
            tv2 = itemView.findViewById(R.id.tv_item_price);
            tv3 = itemView.findViewById(R.id.tv_item_date_and_hour);
            tv4 = itemView.findViewById(R.id.tv_item_status);

        }
    }
}
