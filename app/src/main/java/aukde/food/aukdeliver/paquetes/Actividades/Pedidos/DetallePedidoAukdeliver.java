package aukde.food.aukdeliver.paquetes.Actividades.Pedidos;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import aukde.food.aukdeliver.R;
import aukde.food.aukdeliver.paquetes.Actividades.Notificacion;
import aukde.food.aukdeliver.paquetes.Mapas.MapaClientePorLlamada;
import aukde.food.aukdeliver.paquetes.Modelos.FCMBody;
import aukde.food.aukdeliver.paquetes.Modelos.FCMResponse;
import aukde.food.aukdeliver.paquetes.Modelos.PedidoLlamada;
import aukde.food.aukdeliver.paquetes.Providers.NotificationProvider;
import aukde.food.aukdeliver.paquetes.Providers.TokenProvider;
import aukde.food.aukdeliver.paquetes.Receptor.GpsReceiver;
import aukde.food.aukdeliver.paquetes.Receptor.NetworkReceiver;
import aukde.food.aukdeliver.paquetes.Servicios.ForegroundServiceCronometro;
import aukde.food.aukdeliver.paquetes.Utils.CalculoHoras;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetallePedidoAukdeliver extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    TextView listNumPedido, listNumPedido2, listNombreCliente, listTelefonoCliente, listHoraRegistro,
            listFechaRegistro, listHoraEntrega, listFechaEntrega, listTotalPagoProducto, listDireccion,
            listPagoCliente, listTotalACobrar, listVuelto, listRepartidor, listProveedores, listProducto, listDescripcion,

    listPrecioUnitario , listCantidad ,listPrecioTotalPorProducto , listComision , listTotalDelivery ,
            listGananciaDelivery , listGananciaComision , listEstado ,listLatitud , listLongitud;

    Button mButtonShow;
    Button mButtonShow2;
    Button bottonEstado, btnError;
    Button mMapa;

    TextView mGanasteComision , mGanasteDelivery , txtDetallePTotal , txtDetalleReferencia;

    private DatabaseReference mDatabase;
    private LinearLayout mLinearProductos, mLinearCliente , mLinearTelefono , mLinearDireccion , mLinearReferencia;
    private FirebaseAuth mAuth;

    private TokenProvider tokenProvider;
    private NotificationProvider notificationProvider;

    private TextView photo;
    private String defaultPhoto = "https://firebasestorage.googleapis.com/v0/b/gestor-de-pedidos-aukdefood.appspot.com/o/fotoDefault.jpg?alt=media&token=f74486bf-432e-4af6-b114-baa523e1f801";

    public static final int SETTINGS_REQUEST_CODE = 2;
    public static final int LOCATION_REQUEST_CODE = 1;
    NetworkReceiver networkReceiver = new NetworkReceiver();
    GpsReceiver gpsReceiver = new GpsReceiver();
    private Vibrator vibrator;
    long tiempo = 100;
    TextView txtTiempo;
    SimpleDateFormat simpleDateFormatHora = new SimpleDateFormat("HH:mm:ss");
    String formatoHora = simpleDateFormatHora.format(new Date());
    private ProgressDialog mDialog;
    CalculoHoras calculoHoras;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeDark);
        setContentView(R.layout.activity_detalle_pedido_aukdeliver);
        startLocacion();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this, R.style.ThemeOverlay);
        tokenProvider = new TokenProvider();
        notificationProvider = new NotificationProvider();
        calculoHoras = new CalculoHoras();
        listHoraRegistro = findViewById(R.id.detalleHoraRegistro);
        listFechaRegistro = findViewById(R.id.detalleFechaRegistro);
        listHoraEntrega = findViewById(R.id.detalleHoraEntrega);
        listFechaEntrega = findViewById(R.id.detalleFechaEntrega);
        listTotalPagoProducto = findViewById(R.id.detalleTotalPago);
        listNumPedido = findViewById(R.id.detalleNumPedido);
        listNumPedido2 = findViewById(R.id.detalleNumPedido2);
        listNombreCliente = findViewById(R.id.detalleNombreCliente);
        listTelefonoCliente = findViewById(R.id.detalleTelefonoCliente);
        listDireccion = findViewById(R.id.detalleDireccionCliente);
        listPagoCliente = findViewById(R.id.detallePagoCliente);
        listTotalACobrar = findViewById(R.id.detalleTotalCobro);
        listVuelto = findViewById(R.id.detalleVuelto);
        listRepartidor = findViewById(R.id.detallevRepartidor);
        listProveedores = findViewById(R.id.detalleSocio);
        listProducto = findViewById(R.id.detalleProductos);
        listDescripcion = findViewById(R.id.detalleProductosDescripcion);
        //
        listPrecioUnitario = findViewById(R.id.detallePrecioUnitario);
        listCantidad = findViewById(R.id.detalleCantidad);
        listPrecioTotalPorProducto = findViewById(R.id.detallePreciosTotalesDelproducto);
        listGananciaDelivery = findViewById(R.id.detalleGananciaDelivery);
        //
        listEstado = findViewById(R.id.detalleEstado);
        listLatitud = findViewById(R.id.detalleLatitud);
        listLongitud = findViewById(R.id.detalleLongitudd);
        txtTiempo = findViewById(R.id.detalleTiempo);
        mGanasteDelivery = findViewById(R.id.txtGanasteDelivery);
        txtDetallePTotal = findViewById(R.id.DetallePTotal);

        mButtonShow = findViewById(R.id.showProducto);
        mButtonShow2 = findViewById(R.id.showDetalle);
        bottonEstado = findViewById(R.id.btnEstado);
        btnError = findViewById(R.id.btnReporte);
        mMapa = findViewById(R.id.showMapa);

        photo = findViewById(R.id.pathPhoto);

        int alto = 0;
        mLinearProductos = findViewById(R.id.linearProductos);
        mLinearCliente = findViewById(R.id.linearCliente);
        mLinearTelefono = findViewById(R.id.linearTelefono);
        mLinearDireccion = findViewById(R.id.linearDireccion);
        mLinearReferencia = findViewById(R.id.linearReferencia);
        txtDetalleReferencia = findViewById(R.id.detalleReferencia);
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, alto);
        mLinearProductos.setLayoutParams(params);

        mButtonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mLinearProductos.setLayoutParams(params);
            }
        });

        mButtonShow2.setOnClickListener(new View.OnClickListener() {
            int alto1 = 0;
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, alto1);
                mLinearProductos.setLayoutParams(params);
            }
        });

        mMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                mDialog.show();
                mDialog.setCancelable(false);
                mDialog.setMessage("Cargando...");
                Intent intent = new Intent(DetallePedidoAukdeliver.this, MapaClientePorLlamada.class);
                intent.putExtra("latitud",listLatitud.getText().toString());
                intent.putExtra("longitud",listLongitud.getText().toString());
                intent.putExtra("nombre",listNombreCliente.getText().toString());
                intent.putExtra("telefono",listTelefonoCliente.getText().toString());
                intent.putExtra("proveedores",listProveedores.getText().toString());
                intent.putExtra("referencia",txtDetalleReferencia.getText().toString());
                startActivity(intent);
            }
        });

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        PedidoLlamada pedidoLlamada = (PedidoLlamada) bundle.getSerializable("key");
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(pedidoLlamada.getNumPedido());
        arrayList.add(pedidoLlamada.getHoraPedido());
        arrayList.add(pedidoLlamada.getFechaPedido());
        arrayList.add(pedidoLlamada.getHoraEntrega());
        arrayList.add(pedidoLlamada.getFechaEntrega());
        arrayList.add(pedidoLlamada.getTotalPagoProducto());
        arrayList.add(pedidoLlamada.getNombreCliente());
        arrayList.add(pedidoLlamada.getTelefono());
        arrayList.add("S/" + pedidoLlamada.getConCuantoVaAPagar());
        arrayList.add("S/" + pedidoLlamada.getTotalCobro());
        arrayList.add(pedidoLlamada.getVuelto());
        arrayList.add(pedidoLlamada.getEncargado());
        arrayList.add(pedidoLlamada.getDireccion());

        arrayList.add(pedidoLlamada.getProveedores());
        arrayList.add(pedidoLlamada.getProductos());
        arrayList.add(pedidoLlamada.getDescripcion());

        arrayList.add(pedidoLlamada.getPrecioUnitario());
        arrayList.add(pedidoLlamada.getCantidad());
        arrayList.add(pedidoLlamada.getPrecioTotalXProducto());
        arrayList.add(pedidoLlamada.getGananciaDelivery());

        arrayList.add(pedidoLlamada.getEstado());
        arrayList.add(pedidoLlamada.getLatitud());
        arrayList.add(pedidoLlamada.getLongitud());


        listNumPedido.setText(arrayList.get(0));
        listNumPedido2.setText(arrayList.get(0));
        listHoraRegistro.setText(arrayList.get(1));
        listFechaRegistro.setText(arrayList.get(2));
        listHoraEntrega.setText(arrayList.get(3));
        listFechaEntrega.setText(arrayList.get(4));
        listTotalPagoProducto.setText(arrayList.get(5));
        txtDetallePTotal.setText(arrayList.get(5));
        listNombreCliente.setText(arrayList.get(6));
        listTelefonoCliente.setText(arrayList.get(7));
        listPagoCliente.setText(arrayList.get(8));
        listTotalACobrar.setText(arrayList.get(9));
        listVuelto.setText(arrayList.get(10));
        listRepartidor.setText(arrayList.get(11));
        listDireccion.setText(arrayList.get(12));
        listProveedores.setText(arrayList.get(13));
        listProducto.setText(arrayList.get(14));
        listDescripcion.setText(arrayList.get(15));
        listPrecioUnitario.setText(arrayList.get(16));
        listCantidad.setText(arrayList.get(17));
        listPrecioTotalPorProducto.setText(arrayList.get(18));
        listGananciaDelivery.setText(arrayList.get(19));
        listEstado.setText(arrayList.get(20));
        listLatitud.setText(arrayList.get(21));
        listLongitud.setText(arrayList.get(22));
        checkReference();
        tiempoEntrega();
        checkStatus();

        mDatabase.child("Usuarios").child("Aukdeliver").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Aukdeliver = dataSnapshot.child("nombres").getValue().toString();
                listRepartidor.setText(Aukdeliver);
                if (dataSnapshot.hasChild("foto")){
                    String Foto = dataSnapshot.child("foto").getValue().toString();
                    photo.setText(Foto);
                }
                else {
                    photo.setText(defaultPhoto);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        String stEstado = listEstado.getText().toString();

        String Delivery = listGananciaDelivery.getText().toString();
        Double doubleDelivery = Double.parseDouble(Delivery);
        Double finalGananciaDelivery ;

        if(doubleDelivery < 4.00){
            finalGananciaDelivery = doubleDelivery * 0.5;
            listGananciaDelivery.setText(obtieneDosDecimales(finalGananciaDelivery));
        }
        if(doubleDelivery >= 4.00 && doubleDelivery < 9.00){
            finalGananciaDelivery = doubleDelivery * 0.4;
            listGananciaDelivery.setText(obtieneDosDecimales(finalGananciaDelivery));
        }
        if(doubleDelivery >= 9.00){
            finalGananciaDelivery = doubleDelivery * 0.3;
            listGananciaDelivery.setText(obtieneDosDecimales(finalGananciaDelivery));
        }

        if (stEstado.equals("Completado")) {
            mGanasteDelivery.setText("Ganancia delívery : ");
            listEstado.setTextColor(Color.parseColor("#000000"));
            bottonEstado.setVisibility(View.INVISIBLE);
            btnError.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, alto);
            mLinearCliente.setLayoutParams(params4);
            mLinearTelefono.setLayoutParams(params4);
            mLinearDireccion.setLayoutParams(params4);
            mLinearReferencia.setLayoutParams(params4);
            mMapa.setVisibility(View.INVISIBLE);
        }
        if (stEstado.equals("En espera")) {
            listEstado.setTextColor(Color.parseColor("#232C9B"));
            btnError.setVisibility(View.INVISIBLE);
        }

        if (stEstado.equals("En proceso")) {
            listEstado.setTextColor(Color.parseColor("#FFC300"));
            btnError.setVisibility(View.INVISIBLE);
        }

    }


    private String obtieneDosDecimales(double valor) {
        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(2); //Define 2 decimales.
        return format.format(valor);
    }

    public void showPopupEstado(View view) {
        vibrator.vibrate(tiempo);
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu_estado_aukdeliver);
        popupMenu.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                mDialog.show();
                mDialog.getWindow().setBackgroundDrawableResource(android.R.color.holo_green_dark);
                mDialog.setCancelable(false);
                mDialog.setMessage(Html.fromHtml("<font color='#FFFFFF'>Completando pedido...</font>"));
                detenerCronometro();
                estadoCompletadoAdmin();
                estadoCompletadoAukdeliver();
                sendCompletedNotification();
                hora();
                horaAdmin();
                finish();
                return true;

            case R.id.item2:
                //resetearTiempo();
                confirmarRechazo();
                return true;

            default:
                return false;
        }
    }

    // Crear un query para su usuario Con firebase Auth
    // ocultar el boton al decir si el pedido esta completo

    private void estadoCompletadoAdmin() {
        String dataNumPedido = listNumPedido.getText().toString();
        Query reference = FirebaseDatabase.getInstance().getReference().child("PedidosPorLlamada").child("pedidos").orderByChild("numPedido").equalTo(dataNumPedido);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    Map<String, Object> map = new HashMap<>();
                    map.put("estado", "Completado");
                    mDatabase.child("PedidosPorLlamada").child("pedidos").child(key).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void estadoCompletadoAukdeliver() {
        String dataNumPedido = listNumPedido.getText().toString();
        Query reference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Aukdeliver").child(mAuth.getUid()).child("pedidos").orderByChild("numPedido").equalTo(dataNumPedido);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    Map<String, Object> map = new HashMap<>();
                    map.put("estado", "Completado");
                    mDatabase.child("Usuarios").child("Aukdeliver").child(mAuth.getUid()).child("pedidos").child(key).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toasty.success(DetallePedidoAukdeliver.this, "PEDIDO COMPLETADO!", Toast.LENGTH_LONG, true).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.info(DetallePedidoAukdeliver.this, "Error al actualizar estado", Toast.LENGTH_LONG, true).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void estadoCanceladoAdmin() {
        String dataNumPedido = listNumPedido.getText().toString();
        Query reference = FirebaseDatabase.getInstance().getReference().child("PedidosPorLlamada").child("pedidos").orderByChild("numPedido").equalTo(dataNumPedido);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    //Toast.makeText(DetallePedido.this, "Id : "+key, Toast.LENGTH_SHORT).show();
                    Map<String, Object> map = new HashMap<>();
                    map.put("estado", "Rechazado");
                    mDatabase.child("PedidosPorLlamada").child("pedidos").child(key).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void estadoCanceladoAukdeliver() {
        String dataNumPedido = listNumPedido.getText().toString();
        Query reference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Aukdeliver").child(mAuth.getUid()).child("pedidos").orderByChild("numPedido").equalTo(dataNumPedido);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    mDatabase.child("Usuarios").child("Aukdeliver").child(mAuth.getUid()).child("pedidos").child(key).removeValue();
                    Toasty.error(DetallePedidoAukdeliver.this, "PEDIDO RECHAZADO!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void onClickLlamada(View v) {
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:992997223"));
        startActivity(i);
    }

    public void onClickLlamadaCliente(View v) {
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+listTelefonoCliente.getText().toString()));
        startActivity(i);
    }


    private void confirmarRechazo(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DetallePedidoAukdeliver.this, R.style.ThemeOverlay);
        builder.setTitle("Alerta!");
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_error);
        builder.setMessage("Esta seguro de rechazar este pedido..? ");
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                detenerCronometro();
                estadoCanceladoAdmin();
                estadoCanceladoAukdeliver();
                sendCancelNotification();
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                startActivity(getIntent());
            }
        });
        builder.create();
        builder.show();
    }

    private void sendCancelNotification(){
        final String numPedNotify = listNumPedido.getText().toString();
        final String dataRepartidor = listRepartidor.getText().toString();
        final String admin1 = "9sjTQMmowxWYJGTDUY98rAR2jzB3";
        final String admin2 = "UnwAmhwRzmRLn8aozWjnYFOxYat2";
        final String admin3 = "nS8J0zEj53OcXSugQsXIdMKUi5r1";
        tokenProvider.getToken(admin2).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String token = dataSnapshot.child("token").getValue().toString();
                    String ruta = photo.getText().toString();
                    Map<String,String> map = new HashMap<>();
                    map.put("title","Pedido #"+numPedNotify);
                    map.put("body","El repartidor "+dataRepartidor+"\nha RECHAZADO el pedido!");
                    map.put("path",ruta);
                    FCMBody fcmBody = new FCMBody(token,"high",map);
                    notificationProvider.sendNotificacion(fcmBody).enqueue(new Callback<FCMResponse>() {
                        @Override
                        public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                            if(response.body() !=null){
                                if(response.body().getSuccess() == 1){
                                    //Toast.makeText(RealizarPedido.this, "Notificación enviada", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toasty.error(DetallePedidoAukdeliver.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toasty.error(DetallePedidoAukdeliver.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<FCMResponse> call, Throwable t) {
                            Log.d("Error","Error encontrado"+ t.getMessage());
                        }
                    });
                }

                else {
                    Toast.makeText(DetallePedidoAukdeliver.this, "No existe token se sesión", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        tokenProvider.getToken(admin1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String token = dataSnapshot.child("token").getValue().toString();
                    String ruta = photo.getText().toString();
                    Map<String,String> map = new HashMap<>();
                    map.put("title","Pedido #"+numPedNotify);
                    map.put("body","El repartidor "+dataRepartidor+"\nha RECHAZADO el pedido!");
                    map.put("path",ruta);
                    FCMBody fcmBody = new FCMBody(token,"high",map);
                    notificationProvider.sendNotificacion(fcmBody).enqueue(new Callback<FCMResponse>() {
                        @Override
                        public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                            if(response.body() !=null){
                                if(response.body().getSuccess() == 1){
                                    //Toast.makeText(RealizarPedido.this, "Notificación enviada", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toasty.error(DetallePedidoAukdeliver.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toasty.error(DetallePedidoAukdeliver.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<FCMResponse> call, Throwable t) {
                            Log.d("Error","Error encontrado"+ t.getMessage());
                        }
                    });
                }

                else {
                    Toast.makeText(DetallePedidoAukdeliver.this, "No existe token se sesión", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        tokenProvider.getToken(admin3).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String token = dataSnapshot.child("token").getValue().toString();
                    String ruta = photo.getText().toString();
                    Map<String,String> map = new HashMap<>();
                    map.put("title","Pedido #"+numPedNotify);
                    map.put("body","El repartidor "+dataRepartidor+"\nha RECHAZADO el pedido!");
                    map.put("path",ruta);
                    FCMBody fcmBody = new FCMBody(token,"high",map);
                    notificationProvider.sendNotificacion(fcmBody).enqueue(new Callback<FCMResponse>() {
                        @Override
                        public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                            if(response.body() !=null){
                                if(response.body().getSuccess() == 1){
                                    //Toast.makeText(RealizarPedido.this, "Notificación enviada", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toasty.error(DetallePedidoAukdeliver.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toasty.error(DetallePedidoAukdeliver.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<FCMResponse> call, Throwable t) {
                            Log.d("Error","Error encontrado"+ t.getMessage());
                        }
                    });
                }

                else {
                    Toast.makeText(DetallePedidoAukdeliver.this, "No existe token se sesión", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendCompletedNotification(){
        final String numPedNotify = listNumPedido.getText().toString();
        final String dataRepartidor = listRepartidor.getText().toString();
        final String admin1 = "9sjTQMmowxWYJGTDUY98rAR2jzB3";
        final String admin2 = "UnwAmhwRzmRLn8aozWjnYFOxYat2";
        final String admin3 = "nS8J0zEj53OcXSugQsXIdMKUi5r1";
        tokenProvider.getToken(admin2).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String token = dataSnapshot.child("token").getValue().toString();
                    String ruta = photo.getText().toString();
                    Map<String,String> map = new HashMap<>();
                    map.put("title","Pedido #"+numPedNotify);
                    map.put("body","El repartidor "+dataRepartidor+"\nha COMPLETADO el pedido!");
                    map.put("path",ruta);
                    FCMBody fcmBody = new FCMBody(token,"high",map);
                    notificationProvider.sendNotificacion(fcmBody).enqueue(new Callback<FCMResponse>() {
                        @Override
                        public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                            if(response.body() !=null){
                                if(response.body().getSuccess() == 1){
                                    //Toast.makeText(RealizarPedido.this, "Notificación enviada", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toasty.error(DetallePedidoAukdeliver.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toasty.error(DetallePedidoAukdeliver.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<FCMResponse> call, Throwable t) {
                            Log.d("Error","Error encontrado"+ t.getMessage());
                        }
                    });
                }

                else {
                    Toast.makeText(DetallePedidoAukdeliver.this, "No existe token se sesión", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        tokenProvider.getToken(admin1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String token = dataSnapshot.child("token").getValue().toString();
                    String ruta = photo.getText().toString();
                    Map<String,String> map = new HashMap<>();
                    map.put("title","Pedido #"+numPedNotify);
                    map.put("body","El repartidor "+dataRepartidor+"\nha COMPLETADO el pedido!");
                    map.put("path",ruta);
                    FCMBody fcmBody = new FCMBody(token,"high",map);
                    notificationProvider.sendNotificacion(fcmBody).enqueue(new Callback<FCMResponse>() {
                        @Override
                        public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                            if(response.body() !=null){
                                if(response.body().getSuccess() == 1){
                                    //Toast.makeText(RealizarPedido.this, "Notificación enviada", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toasty.error(DetallePedidoAukdeliver.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toasty.error(DetallePedidoAukdeliver.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<FCMResponse> call, Throwable t) {
                            Log.d("Error","Error encontrado"+ t.getMessage());
                        }
                    });
                }

                else {
                    Toast.makeText(DetallePedidoAukdeliver.this, "No existe token se sesión", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        tokenProvider.getToken(admin3).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String token = dataSnapshot.child("token").getValue().toString();
                    String ruta = photo.getText().toString();
                    Map<String,String> map = new HashMap<>();
                    map.put("title","Pedido #"+numPedNotify);
                    map.put("body","El repartidor "+dataRepartidor+"\nha COMPLETADO el pedido!");
                    map.put("path",ruta);
                    FCMBody fcmBody = new FCMBody(token,"high",map);
                    notificationProvider.sendNotificacion(fcmBody).enqueue(new Callback<FCMResponse>() {
                        @Override
                        public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                            if(response.body() !=null){
                                if(response.body().getSuccess() == 1){
                                    //Toast.makeText(RealizarPedido.this, "Notificación enviada", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toasty.error(DetallePedidoAukdeliver.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toasty.error(DetallePedidoAukdeliver.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<FCMResponse> call, Throwable t) {
                            Log.d("Error","Error encontrado"+ t.getMessage());
                        }
                    });
                }

                else {
                    Toast.makeText(DetallePedidoAukdeliver.this, "No existe token se sesión", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void startLocacion(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (gpsActive()) {
                } else {
                    showAlertDialog();
                }

            } else {
                checkLocationPermision();
            }
        } else {
            if (gpsActive()) {
            } else {
                showAlertDialog();
            }

        }
    }

    private boolean gpsActive() {
        boolean isActive = false;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            isActive = true;
        }

        return isActive;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SETTINGS_REQUEST_CODE && gpsActive()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

        } else {
            showAlertDialog();
        }
    }

    public void checkLocationPermision() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Proporciona los permisos para continuar")
                        .setCancelable(false)
                        .setMessage("Esta aplicación requiere los permisos para continuar")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(DetallePedidoAukdeliver.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(DetallePedidoAukdeliver.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Por favor activa tu ubicación para continuar")
                .setCancelable(false)
                .setPositiveButton("Configurar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), SETTINGS_REQUEST_CODE);
                    }
                }).create().show();
    }

    private void checkReference(){
        String dataNumPedido = listNumPedido.getText().toString();
        Query reference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Aukdeliver").child(mAuth.getUid()).child("pedidos").orderByChild("numPedido").equalTo(dataNumPedido);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String key=childSnapshot.getKey();
                    mDatabase.child("Usuarios").child("Aukdeliver").child(mAuth.getUid()).child("pedidos").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild("referencia")){
                                String referencia = dataSnapshot.child("referencia").getValue().toString();
                                txtDetalleReferencia.setText(referencia);
                            }
                            else{
                                txtDetalleReferencia.setText("Ninguna");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void iniciarCronometro(){
        Intent service = new Intent(this, ForegroundServiceCronometro.class);
        startService(service);
    }

    private void detenerCronometro(){
        Intent service = new Intent(this, ForegroundServiceCronometro.class);
        stopService(service);
    }

    private void hora(){
        String dataNumPedido = listNumPedido.getText().toString();
        Query reference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Aukdeliver").child(mAuth.getUid()).child("pedidos").orderByChild("numPedido").equalTo(dataNumPedido);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    final String key = childSnapshot.getKey();
                    mDatabase.child("Usuarios").child("Aukdeliver").child(mAuth.getUid()).child("pedidos").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map<String , Object> map = new HashMap<>();
                            map.put("tiempo2",formatoHora);
                            mDatabase.child("Usuarios").child("Aukdeliver").child(mAuth.getUid()).child("pedidos").child(key).child("tiempo").updateChildren(map);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void horaAccept(){
        String dataNumPedido = listNumPedido.getText().toString();
        Query reference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Aukdeliver").child(mAuth.getUid()).child("pedidos").orderByChild("numPedido").equalTo(dataNumPedido);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    final String key = childSnapshot.getKey();
                    mDatabase.child("Usuarios").child("Aukdeliver").child(mAuth.getUid()).child("pedidos").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map<String , Object> map = new HashMap<>();
                            map.put("tiempo1",formatoHora);
                            mDatabase.child("Usuarios").child("Aukdeliver").child(mAuth.getUid()).child("pedidos").child(key).child("tiempo").updateChildren(map);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void horaAdmin(){
        String dataNumPedido = listNumPedido.getText().toString();
        Query reference = FirebaseDatabase.getInstance().getReference().child("PedidosPorLlamada").child("pedidos").orderByChild("numPedido").equalTo(dataNumPedido);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    final String key = childSnapshot.getKey();
                    //Toast.makeText(DetallePedido.this, "Id : "+key, Toast.LENGTH_SHORT).show();
                    mDatabase.child("PedidosPorLlamada").child("pedidos").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("tiempo2",formatoHora);
                            mDatabase.child("PedidosPorLlamada").child("pedidos").child(key).child("tiempo").updateChildren(map);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void horaAdminAccept(){
        String dataNumPedido = listNumPedido.getText().toString();
        Query reference = FirebaseDatabase.getInstance().getReference().child("PedidosPorLlamada").child("pedidos").orderByChild("numPedido").equalTo(dataNumPedido);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    final String key = childSnapshot.getKey();
                    //Toast.makeText(DetallePedido.this, "Id : "+key, Toast.LENGTH_SHORT).show();
                    mDatabase.child("PedidosPorLlamada").child("pedidos").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("tiempo1",formatoHora);
                            mDatabase.child("PedidosPorLlamada").child("pedidos").child(key).child("tiempo").updateChildren(map);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void tiempoEntrega(){
        String dataNumPedido = listNumPedido.getText().toString();
        Query reference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Aukdeliver").child(mAuth.getUid()).child("pedidos").orderByChild("numPedido").equalTo(dataNumPedido);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String key=childSnapshot.getKey();
                    mDatabase.child("Usuarios").child("Aukdeliver").child(mAuth.getUid()).child("pedidos").child(key).child("tiempo").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild("tiempo2") && dataSnapshot.hasChild("tiempo1")) {
                                String time1 = dataSnapshot.child("tiempo1").getValue().toString();
                                String time2 = dataSnapshot.child("tiempo2").getValue().toString();
                                try {
                                    Date date1 = simpleDateFormatHora.parse(time1);
                                    Date date2 = simpleDateFormatHora.parse(time2);
                                    Date difference = calculoHoras.getDiferenciaHoras(date1,date2);
                                    txtTiempo.setText(simpleDateFormatHora.format(difference));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                //Toast.makeText(DetallePedidoAukdeliver.this, "Tiempo Ini : "+time1+" Tiempo Fin : "+time2, Toast.LENGTH_SHORT).show();
                            }
                            else{
                                txtTiempo.setText("No disponible!");
                                //Toast.makeText(DetallePedidoAukdeliver.this, "Sin tiempo 2", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void checkStatus(){
        String status = listEstado.getText().toString();
        if (status.equals("En espera"))
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(DetallePedidoAukdeliver.this, R.style.ThemeOverlay);
            builder.setTitle("Advertencia!");
            builder.setCancelable(false);
            builder.setIcon(R.drawable.ic_error);
            builder.setMessage("Debe aceptar el pedido para ver los datos");
            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //finish();
                    estadoEnProcesoAdmin();
                    estadoEnProcesoAukdeliver();
                    horaAccept();
                    horaAdminAccept();
                    iniciarCronometro();
                    Toasty.success(DetallePedidoAukdeliver.this, "Pedido Aceptado!", Toast.LENGTH_SHORT,true).show();
                    dialog.cancel();
                    listEstado.setText("En proceso");
                    listEstado.setTextColor(Color.parseColor("#FFC300"));
                }
            });
            builder.setNegativeButton("Rechazar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    confirmarRechazo();
                }
            });
            builder.create();
            builder.show();
        }
    }

    private void estadoEnProcesoAdmin(){
        String dataNumPedido = listNumPedido.getText().toString();
        Query reference = FirebaseDatabase.getInstance().getReference().child("PedidosPorLlamada").child("pedidos").orderByChild("numPedido").equalTo(dataNumPedido);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    //Toast.makeText(DetallePedido.this, "Id : "+key, Toast.LENGTH_SHORT).show();
                    Map<String, Object> map = new HashMap<>();
                    map.put("estado", "En proceso");
                    mDatabase.child("PedidosPorLlamada").child("pedidos").child(key).updateChildren(map);
                    sendInProcessNotification();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void estadoEnProcesoAukdeliver() {
        String dataNumPedido = listNumPedido.getText().toString();
        Query reference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Aukdeliver").child(mAuth.getUid()).child("pedidos").orderByChild("numPedido").equalTo(dataNumPedido);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    Map<String, Object> map = new HashMap<>();
                    map.put("estado", "En proceso");
                    mDatabase.child("Usuarios").child("Aukdeliver").child(mAuth.getUid()).child("pedidos").child(key).updateChildren(map);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendInProcessNotification(){
        final String numPedNotify = listNumPedido.getText().toString();
        final String dataRepartidor = listRepartidor.getText().toString();
        String[] admins = {"nS8J0zEj53OcXSugQsXIdMKUi5r1", "UnwAmhwRzmRLn8aozWjnYFOxYat2",
                "9sjTQMmowxWYJGTDUY98rAR2jzB3"};

        for (int i = 0; i < admins.length; i++) {
            tokenProvider.getToken(admins[i]).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String token = dataSnapshot.child("token").getValue().toString();
                        String ruta = photo.getText().toString();
                        Map<String, String> map = new HashMap<>();
                        map.put("title", "Pedido #" + numPedNotify);
                        map.put("body", "El repartidor " + dataRepartidor + "\nha ACEPTADO el pedido!");
                        map.put("path", ruta);
                        FCMBody fcmBody = new FCMBody(token, "high", map);
                        notificationProvider.sendNotificacion(fcmBody).enqueue(new Callback<FCMResponse>() {
                            @Override
                            public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                                if (response.body() != null) {
                                    if (response.body().getSuccess() == 1) {
                                        //Toast.makeText(RealizarPedido.this, "Notificación enviada", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toasty.error(DetallePedidoAukdeliver.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toasty.error(DetallePedidoAukdeliver.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<FCMResponse> call, Throwable t) {
                                Log.d("Error", "Error encontrado" + t.getMessage());
                            }
                        });
                    } else {
                        Toast.makeText(DetallePedidoAukdeliver.this, "No existe token se sesión", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetallePedidoAukdeliver.this, R.style.ThemeOverlay);
        builder.setTitle("Confirmar");
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_atras);
        builder.setMessage("Deseas volver a la lista de pedidos? ");
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create();
        builder.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        startLocacion();
        unregisterReceiver(networkReceiver);
        unregisterReceiver(gpsReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDialog.dismiss();
        startLocacion();
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);
        registerReceiver(gpsReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
        super.onStart();
    }

}