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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import aukde.food.aukdeliver.R;
import aukde.food.aukdeliver.paquetes.Adaptadores.CartItemsListAdapter;
import aukde.food.aukdeliver.paquetes.Mapas.MapClient;
import aukde.food.aukdeliver.paquetes.Modelos.Order;

public class DetailOrder extends AppCompatActivity {

    private ProgressDialog mDialog;
    TextView id , dates , status , type_address , name , detail_address , other_details , mobile,
             subtotal , shipping , total;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    MaterialButton btnMapClient , btnCompleteOrder;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.ColorButtonMaterial);
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

        btnCompleteOrder = findViewById(R.id.btn_complete_order);

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

        CartItemsListAdapter items = new CartItemsListAdapter(this,order.getItems(),order.getTitle());
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

        Double dSubTotal = Double.parseDouble(order.getSub_total_amount());
        Double dShipping = Double.parseDouble(order.getShipping_charge());
        double sum = dSubTotal+dShipping;
        subtotal.setText("S/"+order.getSub_total_amount());
        shipping.setText("S/"+order.getShipping_charge());
        total.setText("S/"+(sum));

        btnCompleteOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = order.getUser_id();
                Toast.makeText(DetailOrder.this, id, Toast.LENGTH_SHORT).show();
            }
        });

    }



}