package aukde.food.aukdeliver.paquetes.Actividades.Pedidos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import aukde.food.aukdeliver.R;
import aukde.food.aukdeliver.paquetes.Adaptadores.CartItemsListAdapter;
import aukde.food.aukdeliver.paquetes.Mapas.MapClient;
import aukde.food.aukdeliver.paquetes.Modelos.Order;
import aukde.food.aukdeliver.paquetes.Modelos.PedidoLlamada;

public class DetailOrder extends AppCompatActivity {

    private ProgressDialog mDialog;
    TextView id , dates , status , type_address , name , detail_address , other_details , mobile,
             subtotal , shipping , total;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    Button btnMapClient;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme2);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        recyclerView = findViewById(R.id.rv_my_order_items_list);
        mDialog = new ProgressDialog(this, R.style.ThemeOverlay);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        id = findViewById(R.id.tv_order_details_id);
        dates = findViewById(R.id.tv_order_details_date);
        status = findViewById(R.id.tv_order_status);
        type_address = findViewById(R.id.tv_my_order_details_address_type);
        name = findViewById(R.id.tv_my_order_details_full_name);
        detail_address = findViewById(R.id.tv_my_order_details_address);
        other_details = findViewById(R.id.tv_my_order_details_other_details);
        mobile = findViewById(R.id.tv_my_order_details_mobile_number);
        btnMapClient = findViewById(R.id.btn_map_client);
        subtotal = findViewById(R.id.tv_order_details_sub_total);
        shipping = findViewById(R.id.tv_order_details_shipping_charge);
        total = findViewById(R.id.tv_order_details_total_amount);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        final Order order = (Order) bundle.getSerializable("detail");
        String dateFormat = "dd MMM yyyy HH:mm";
        Date date = new Date(order.getOrder_datetime());
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());

        id.setText(order.getTitle());
        dates.setText(formatter.format(date));

        if (order.getStatus() == 0){
            status.setText("Pendiente");
            status.setTextColor(Color.parseColor("#FC0000"));
        }
        else if (order.getStatus() == 1){
            status.setText("Procesando");
            status.setTextColor(Color.parseColor("#F1C40F"));
        }

        CartItemsListAdapter items = new CartItemsListAdapter(this,order.getItems());
        recyclerView.setAdapter(items);

        type_address.setText(order.getAddress().getType());
        name.setText(order.getAddress().getName());
        detail_address.setText(order.getAddress().getAddress()+" "+order.getAddress().getZipCode());

        if (!order.getAddress().getOtherDetails().isEmpty()){
            other_details.setVisibility(View.VISIBLE);
            other_details.setText(order.getAddress().getOtherDetails());
        }
        else{
            other_details.setVisibility(View.GONE);
        }

        mobile.setText(order.getAddress().getMobileNumber());
        btnMapClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailOrder.this,MapClient.class);
                intent.putExtra("latitude",order.getAddress().getLatitude());
                intent.putExtra("longitude",order.getAddress().getLongitude());
                startActivity(intent);
            }
        });

        subtotal.setText(order.getSub_total_amount());
        shipping.setText(order.getShipping_charge());
        total.setText(order.getSub_total_amount());



    }



}