package aukde.food.gestordepedidos.paquetes.Productos.Solicitud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Actividades.Pedidos.DetallePedido;
import aukde.food.gestordepedidos.paquetes.Actividades.Pedidos.DetallePedidoAukdeliver;
import aukde.food.gestordepedidos.paquetes.Actividades.Pedidos.RealizarPedido;
import aukde.food.gestordepedidos.paquetes.Menus.MenuAdmin;
import aukde.food.gestordepedidos.paquetes.Menus.MenuProveedor;
import aukde.food.gestordepedidos.paquetes.Modelos.FCMBody;
import aukde.food.gestordepedidos.paquetes.Modelos.FCMResponse;
import aukde.food.gestordepedidos.paquetes.Modelos.ListaSolicitudProducto;
import aukde.food.gestordepedidos.paquetes.Modelos.PedidoLlamada;
import aukde.food.gestordepedidos.paquetes.Providers.NotificationProvider;
import aukde.food.gestordepedidos.paquetes.Providers.TokenProvider;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleSolicitudDeProductoParaProveedor extends AppCompatActivity {

    TextView mTxtProductos , mTxtNota , mTxtCantidad , mTxtPrecioTotal , mTxtTotalACobrar,
            mTxtEstado , txtNumSolicitudProducto;
    FloatingActionButton floatRotate;
    Button mBtnConfirmar , mBtnRechazar , mBtnCancelar;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Vibrator vibrator;
    LinearLayout lnLinearConfirm , lnLinearCancel;
    long tiempo = 100;
    private TokenProvider tokenProvider;
    private NotificationProvider notificationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_solicitud_de_producto_para_proveedor);
        tokenProvider = new TokenProvider();
        notificationProvider = new NotificationProvider();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mTxtProductos = findViewById(R.id.detalleProductos);
        mTxtNota = findViewById(R.id.detalleProductosDescripcion);
        mTxtCantidad = findViewById(R.id.detalleCantidad);
        mTxtPrecioTotal = findViewById(R.id.detallePreciosTotalesDelproducto);
        mTxtTotalACobrar = findViewById(R.id.DetallePTotal);
        mTxtEstado = findViewById(R.id.txtEstadoProducto);
        floatRotate = findViewById(R.id.floatBtnRotate);
        floatRotate.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.quantum_black_100)));
        mBtnConfirmar = findViewById(R.id.btnConfirmar);
        mBtnRechazar = findViewById(R.id.btnRechazar);
        mBtnCancelar = findViewById(R.id.btnCancelarSoli);
        txtNumSolicitudProducto = findViewById(R.id.numSolicitudProducto);
        lnLinearConfirm = findViewById(R.id.linearConfirm);
        lnLinearCancel = findViewById(R.id.linearCancel);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        ListaSolicitudProducto mListaSoliicitud = (ListaSolicitudProducto) bundle.getSerializable("key");
        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add(mListaSoliicitud.getNombreProducto());
        arrayList.add(mListaSoliicitud.getDescripcion());
        arrayList.add(mListaSoliicitud.getCantidad());
        arrayList.add(mListaSoliicitud.getPrecioTotalPorProducto());
        arrayList.add(mListaSoliicitud.getEstado());
        arrayList.add(mListaSoliicitud.getTotalACobrar());
        arrayList.add(mListaSoliicitud.getNumSolicitud());

        mTxtProductos.setText(arrayList.get(0));
        mTxtNota.setText(arrayList.get(1));
        mTxtCantidad.setText(arrayList.get(2));
        mTxtPrecioTotal.setText(arrayList.get(3));
        mTxtEstado.setText(arrayList.get(4));
        if (mTxtEstado.getText().toString().equals("Sin confirmar") || mTxtEstado.getText().toString().equals("Rechazado")
                || mTxtEstado.getText().toString().equals("Cancelado")){
            mTxtEstado.setTextColor(Color.parseColor("#FC0000"));
        }
        else
        {
            mTxtEstado.setTextColor(Color.parseColor("#14A72A"));
        }
        mTxtTotalACobrar.setText(arrayList.get(5));
        txtNumSolicitudProducto.setText(arrayList.get(6));
        statusButtom();

        floatRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeScreenOrientation();
            }
        });

        mBtnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                AlertDialog.Builder builder = new AlertDialog.Builder(DetalleSolicitudDeProductoParaProveedor.this,R.style.ThemeOverlay);
                builder.setTitle("Confirme!");
                builder.setIcon(R.drawable.ic_alerta_producto);
                builder.setCancelable(false);
                builder.setMessage("Esta seguro de CONFIRMAR esta solicitud?");
                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirm();
                        startActivity(new Intent(DetalleSolicitudDeProductoParaProveedor.this, MenuProveedor.class));
                        finish();
                        Toasty.success(DetalleSolicitudDeProductoParaProveedor.this, "Solicitud Confirmada", Toast.LENGTH_SHORT,true).show();
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
        });

        mBtnRechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                AlertDialog.Builder builder = new AlertDialog.Builder(DetalleSolicitudDeProductoParaProveedor.this,R.style.ThemeOverlay);
                builder.setTitle("Confirme!");
                builder.setIcon(R.drawable.ic_error);
                builder.setCancelable(false);
                builder.setMessage("Esta seguro de RECHAZAR esta solicitud?");
                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nulled();
                        startActivity(new Intent(DetalleSolicitudDeProductoParaProveedor.this, MenuProveedor.class));
                        finish();
                        Toasty.error(DetalleSolicitudDeProductoParaProveedor.this, "Solicitud Rechazada", Toast.LENGTH_SHORT,true).show();
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
        });

        mBtnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                AlertDialog.Builder builder = new AlertDialog.Builder(DetalleSolicitudDeProductoParaProveedor.this,R.style.ThemeOverlay);
                builder.setTitle("Confirme!");
                builder.setIcon(R.drawable.ic_error);
                builder.setCancelable(false);
                builder.setMessage("Esta seguro de CANCELAR esta solicitud?");
                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelSolicitude();
                        startActivity(new Intent(DetalleSolicitudDeProductoParaProveedor.this, MenuProveedor.class));
                        finish();
                        Toasty.error(DetalleSolicitudDeProductoParaProveedor.this, "Solicitud Cancelada", Toast.LENGTH_SHORT,true).show();
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
        });

    }

    private void statusButtom(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        LinearLayout.LayoutParams paramsMatch = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (mTxtEstado.getText().toString().equals("Confirmado")){
            lnLinearConfirm.setLayoutParams(params);
        }
        else if (mTxtEstado.getText().toString().equals("Cancelado")){
            lnLinearConfirm.setLayoutParams(paramsMatch);
            lnLinearCancel.setLayoutParams(params);
        }
        else{
            lnLinearCancel.setLayoutParams(params);
        }
    }

    private void changeScreenOrientation(){
        int orientation = DetalleSolicitudDeProductoParaProveedor.this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        if (Settings.System.getInt(getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                }
            }, 4000);
        }
    }

    private void confirm() {
        mDatabase.child("SolicitudDeProductos").orderByChild("numSolicitud")
                .equalTo(txtNumSolicitudProducto.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String key = childSnapshot.getKey();
                        Map<String , Object> map = new HashMap<>();
                        map.put("estado","Confirmado");
                        mDatabase.child("SolicitudDeProductos").child(key).updateChildren(map);
                        sendConfirmNotification();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.child("Usuarios").child("Proveedor").child(mAuth.getUid()).child("SolicitudDeProductos").orderByChild("numSolicitud")
                .equalTo(txtNumSolicitudProducto.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String key = childSnapshot.getKey();
                        Map<String , Object> map = new HashMap<>();
                        map.put("estado","Confirmado");
                        mDatabase.child("Usuarios").child("Proveedor").child(mAuth.getUid()).child("SolicitudDeProductos").child(key).updateChildren(map);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void nulled(){
        mDatabase.child("SolicitudDeProductos").orderByChild("numSolicitud")
                .equalTo(txtNumSolicitudProducto.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String key = childSnapshot.getKey();
                        Map<String , Object> map = new HashMap<>();
                        map.put("estado","Rechazado");
                        mDatabase.child("SolicitudDeProductos").child(key).updateChildren(map);
                        sendNulledNotification();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.child("Usuarios").child("Proveedor").child(mAuth.getUid()).child("SolicitudDeProductos").orderByChild("numSolicitud")
                .equalTo(txtNumSolicitudProducto.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String key = childSnapshot.getKey();
                        Map<String , Object> map = new HashMap<>();
                        map.put("estado","Rechazado");
                        mDatabase.child("Usuarios").child("Proveedor").child(mAuth.getUid()).child("SolicitudDeProductos").child(key).updateChildren(map);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void cancelSolicitude(){
        mDatabase.child("SolicitudDeProductos").orderByChild("numSolicitud")
                .equalTo(txtNumSolicitudProducto.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String key = childSnapshot.getKey();
                        Map<String , Object> map = new HashMap<>();
                        map.put("estado","Cancelado");
                        mDatabase.child("SolicitudDeProductos").child(key).updateChildren(map);
                        sendCancelNotification();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.child("Usuarios").child("Proveedor").child(mAuth.getUid()).child("SolicitudDeProductos").orderByChild("numSolicitud")
                .equalTo(txtNumSolicitudProducto.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String key = childSnapshot.getKey();
                        Map<String , Object> map = new HashMap<>();
                        map.put("estado","Cancelado");
                        mDatabase.child("Usuarios").child("Proveedor").child(mAuth.getUid()).child("SolicitudDeProductos").child(key).updateChildren(map);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendConfirmNotification(){

        mDatabase.child("Usuarios").child("Proveedor").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    final String nameBussiness = dataSnapshot.child("nombre_empresa").getValue().toString();
                    final String photo = dataSnapshot.child("foto").getValue().toString();
                    String[] admins = {"nS8J0zEj53OcXSugQsXIdMKUi5r1", "UnwAmhwRzmRLn8aozWjnYFOxYat2",
                            "9sjTQMmowxWYJGTDUY98rAR2jzB3"};
                    for (int i = 0; i < admins.length; i++) {
                        tokenProvider.getToken(admins[i]).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String token = dataSnapshot.child("token").getValue().toString();
                                    Map<String, String> map = new HashMap<>();
                                    map.put("title", "Solicitud de producto CONFIRMADA!");
                                    map.put("body",  "Del PRODUCTO\ndel SOCIO : " + nameBussiness);
                                    map.put("path", photo);
                                    FCMBody fcmBody = new FCMBody(token, "high", map);
                                    notificationProvider.sendNotificacion(fcmBody).enqueue(new Callback<FCMResponse>() {
                                        @Override
                                        public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                                            if (response.body() != null) {
                                                if (response.body().getSuccess() == 1) {
                                                    //Toast.makeText(RealizarPedido.this, "Notificación enviada", Toast.LENGTH_LONG).show();
                                                } else {
                                                    Toasty.error(DetalleSolicitudDeProductoParaProveedor.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toasty.error(DetalleSolicitudDeProductoParaProveedor.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        @Override
                                        public void onFailure(Call<FCMResponse> call, Throwable t) {
                                            Log.d("Error", "Error encontrado" + t.getMessage());
                                        }
                                    });
                                } else {
                                    Toast.makeText(DetalleSolicitudDeProductoParaProveedor.this, "No existe token se sesión", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                }
                else {
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void sendNulledNotification(){

        mDatabase.child("Usuarios").child("Proveedor").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    final String nameBussiness = dataSnapshot.child("nombre_empresa").getValue().toString();
                    final String photo = dataSnapshot.child("foto").getValue().toString();

                    String[] admins = {"nS8J0zEj53OcXSugQsXIdMKUi5r1", "UnwAmhwRzmRLn8aozWjnYFOxYat2",
                            "9sjTQMmowxWYJGTDUY98rAR2jzB3"};

                    for (int i = 0; i < admins.length; i++) {
                        tokenProvider.getToken(admins[i]).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String token = dataSnapshot.child("token").getValue().toString();
                                    Map<String, String> map = new HashMap<>();
                                    map.put("title", "Solicitud de producto RECHAZADA!");
                                    map.put("body",  "Del SOCIO : "+nameBussiness);
                                    map.put("path", photo);
                                    FCMBody fcmBody = new FCMBody(token, "high", map);
                                    notificationProvider.sendNotificacion(fcmBody).enqueue(new Callback<FCMResponse>() {
                                        @Override
                                        public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                                            if (response.body() != null) {
                                                if (response.body().getSuccess() == 1) {
                                                    //Toast.makeText(RealizarPedido.this, "Notificación enviada", Toast.LENGTH_LONG).show();
                                                } else {
                                                    Toasty.error(DetalleSolicitudDeProductoParaProveedor.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toasty.error(DetalleSolicitudDeProductoParaProveedor.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<FCMResponse> call, Throwable t) {
                                            Log.d("Error", "Error encontrado" + t.getMessage());
                                        }
                                    });
                                } else {
                                    Toast.makeText(DetalleSolicitudDeProductoParaProveedor.this, "No existe token se sesión", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }
                else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendCancelNotification(){

        mDatabase.child("Usuarios").child("Proveedor").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    final String nameBussiness = dataSnapshot.child("nombre_empresa").getValue().toString();
                    final String photo = dataSnapshot.child("foto").getValue().toString();

                    String[] admins = {"nS8J0zEj53OcXSugQsXIdMKUi5r1", "UnwAmhwRzmRLn8aozWjnYFOxYat2",
                            "9sjTQMmowxWYJGTDUY98rAR2jzB3"};

                    for (int i = 0; i < admins.length; i++) {
                        tokenProvider.getToken(admins[i]).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String token = dataSnapshot.child("token").getValue().toString();
                                    Map<String, String> map = new HashMap<>();
                                    map.put("title", "Solicitud de producto CANCELADA!");
                                    map.put("body",  "Del SOCIO : "+nameBussiness);
                                    map.put("path", photo);
                                    FCMBody fcmBody = new FCMBody(token, "high", map);
                                    notificationProvider.sendNotificacion(fcmBody).enqueue(new Callback<FCMResponse>() {
                                        @Override
                                        public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                                            if (response.body() != null) {
                                                if (response.body().getSuccess() == 1) {
                                                    //Toast.makeText(RealizarPedido.this, "Notificación enviada", Toast.LENGTH_LONG).show();
                                                } else {
                                                    Toasty.error(DetalleSolicitudDeProductoParaProveedor.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toasty.error(DetalleSolicitudDeProductoParaProveedor.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<FCMResponse> call, Throwable t) {
                                            Log.d("Error", "Error encontrado" + t.getMessage());
                                        }
                                    });
                                } else {
                                    Toast.makeText(DetalleSolicitudDeProductoParaProveedor.this, "No existe token se sesión", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }
                else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

}