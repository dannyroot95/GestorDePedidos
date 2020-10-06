package aukde.food.gestordepedidos.paquetes.Productos.Default;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Modelos.FCMBody;
import aukde.food.gestordepedidos.paquetes.Modelos.FCMResponse;
import aukde.food.gestordepedidos.paquetes.Modelos.ProductoDefault;
import aukde.food.gestordepedidos.paquetes.Providers.NotificationProvider;
import aukde.food.gestordepedidos.paquetes.Providers.TokenProvider;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleBebidas extends AppCompatActivity {

    TextView txtNombreProducto , txtDecripcion ,
            txtDisponibilidad , txtCodigo , txtStock ;
    CircleImageView clrPhotoDetalleProducto;
    Button mButtonMax , mButtomMin , mButtonEditarProductoDefault  , mButtonActualizarStockDefault;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    private ProgressDialog mDialog;
    private Vibrator vibrator;
    long tiempo = 100;
    Switch estSwitch;
    private NotificationProvider notificationProvider;
    private TokenProvider tokenProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeRedCake);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_bebidas);

        tokenProvider = new TokenProvider();
        mDialog = new ProgressDialog(this,R.style.ThemeOverlay);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        estSwitch =  findViewById(R.id.estadoSwitch);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        clrPhotoDetalleProducto = findViewById(R.id.photoDetalleProductoDefault);
        txtNombreProducto = findViewById(R.id.detalleNombreProductoDefault);
        txtDecripcion = findViewById(R.id.detalleDefaultDescripcion);
        txtStock = findViewById(R.id.detalleStockDefault);
        txtCodigo = findViewById(R.id.detalleCodigoDefault);
        txtDisponibilidad = findViewById(R.id.detalleDisponibilidad);
        notificationProvider = new NotificationProvider();
        mButtomMin = findViewById(R.id.btnMinDefault);
        mButtonMax = findViewById(R.id.btnMaxDefault);
        mButtonEditarProductoDefault = findViewById(R.id.btnEditarProductoDefault);
        mButtonActualizarStockDefault = findViewById(R.id.btnActualizarStockDefault);


        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        ProductoDefault productoDefault = (ProductoDefault) bundle.getSerializable("key");
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(productoDefault.getUrlPhoto());
        arrayList.add(productoDefault.getNombreProducto());
        arrayList.add(productoDefault.getDescripcionProducto());
        arrayList.add(productoDefault.getCodigoINEA());
        arrayList.add(productoDefault.getDisponibilidad());
        arrayList.add(productoDefault.getStock());

        Picasso
                .get()
                .load(arrayList.get(0))
                .fit()
                .error(R.drawable.ic_error)
                .placeholder(R.drawable.loaderrosa)
                .centerCrop()
                .into(clrPhotoDetalleProducto);

        txtNombreProducto.setText(arrayList.get(1));
        txtDecripcion.setText(arrayList.get(2));
        txtCodigo.setText(arrayList.get(3));
        txtDisponibilidad.setText(arrayList.get(4));
        txtStock.setText(arrayList.get(5));

        if(txtDisponibilidad.getText().toString().equals("Disponible")){
            estSwitch.setChecked(true);
        }
        else {
            estSwitch.setChecked(false);
            txtDisponibilidad.setText("No disponible");
        }

        estSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (estSwitch.isChecked()){
                    vibrator.vibrate(tiempo);
                    txtDisponibilidad.setText("Disponible");
                    actualizarEstado();
                }
                else {
                    vibrator.vibrate(tiempo);
                    txtDisponibilidad.setText("No disponible");
                    actualizarEstado();
                }
            }
        });

        mButtomMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = txtStock.getText().toString();
                if (num.equals("1")){
                    txtStock.setText("1");
                }
                else {
                    int Cc = Integer.parseInt(num);
                    int res = Cc-1;
                    String Min = String.valueOf(res);
                    txtStock.setText(Min);
                }

            }
        });

        mButtonMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = txtStock.getText().toString();
                int Cc = Integer.parseInt(num);
                int res = Cc+1;
                String Min = String.valueOf(res);
                txtStock.setText(Min);
            }
        });

        mButtonActualizarStockDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                vibrator.vibrate(tiempo);
                actualizarStock();
            }
        });

        mButtonEditarProductoDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                Toasty.success(DetalleBebidas.this, "Activity", Toast.LENGTH_SHORT,true).show();
            }
        });


    }

    private void actualizarEstado() {
        final String dataEstado = txtDisponibilidad.getText().toString();
        final String dataCodigoINEA = txtCodigo.getText().toString();
        Query reference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Proveedor").child(mAuth.getUid()).child("Bebidas").orderByChild("codigoINEA").equalTo(dataCodigoINEA);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    Map<String, Object> map = new HashMap<>();
                    map.put("disponibilidad", dataEstado);
                    mDatabase.child("Usuarios").child("Proveedor").child(mAuth.getUid()).child("Bebidas").child(key).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toasty.success(DetalleBebidas.this, "Estado Actualizado!", Toast.LENGTH_LONG, true).show();
                            sendActualizeEstadoNotification();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.error(DetalleBebidas.this, "Error al actualizar estado", Toast.LENGTH_LONG, true).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void actualizarStock() {
        final String dataStockDefault = txtStock.getText().toString();
        final String dataCodigoINEA = txtCodigo.getText().toString();
        Query reference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Proveedor").child(mAuth.getUid()).child("Bebidas").orderByChild("codigoINEA").equalTo(dataCodigoINEA);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    Map<String, Object> map = new HashMap<>();
                    map.put("stock", dataStockDefault);
                    mDatabase.child("Usuarios").child("Proveedor").child(mAuth.getUid()).child("Bebidas").child(key).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toasty.success(DetalleBebidas.this, "Stock Actualizado!", Toast.LENGTH_LONG, true).show();
                            sendActualizeStockNotification();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.error(DetalleBebidas.this, "Error al actualizar Stock", Toast.LENGTH_LONG, true).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendActualizeStockNotification(){
        //obtener nombre del socio y nombre del producto
        final String admin1 = "9sjTQMmowxWYJGTDUY98rAR2jzB3";
        final String admin2 = "UnwAmhwRzmRLn8aozWjnYFOxYat2";
        final String admin3 = "nS8J0zEj53OcXSugQsXIdMKUi5r1";
        final String stStock = txtStock.getText().toString();
        final String stNombreProducto = txtNombreProducto.getText().toString();
        tokenProvider.getToken(admin2).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String token = dataSnapshot.child("token").getValue().toString();
                    String ruta = "https://firebasestorage.googleapis.com/v0/b/gestor-de-pedidos-aukdefood.appspot.com/o/PhotoProveedor%2FH1HZlKfKCjRWIR4ttx15pRKhFKM2.jpg?alt=media&token=dbf15a43-829c-4c69-8f15-f16e221a2f70";
                    Map<String,String> map = new HashMap<>();
                    map.put("title","Socio "+"Pizzeria Croissant");
                    map.put("body","ha actualizado su stock a : "+stStock+"\ndel producto : "+stNombreProducto);
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
                                    Toasty.error(DetalleBebidas.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toasty.error(DetalleBebidas.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<FCMResponse> call, Throwable t) {
                            Log.d("Error","Error encontrado"+ t.getMessage());
                        }
                    });
                }

                else {
                    Toast.makeText(DetalleBebidas.this, "No existe token se sesión", Toast.LENGTH_SHORT).show();
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
                    String ruta = "https://firebasestorage.googleapis.com/v0/b/gestor-de-pedidos-aukdefood.appspot.com/o/PhotoProveedor%2FH1HZlKfKCjRWIR4ttx15pRKhFKM2.jpg?alt=media&token=dbf15a43-829c-4c69-8f15-f16e221a2f70";
                    Map<String,String> map = new HashMap<>();
                    map.put("title","Socio "+"Pizzeria Croissant");
                    map.put("body","ha actualizado su stock a : "+stStock+"\ndel producto : "+stNombreProducto);
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
                                    Toasty.error(DetalleBebidas.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toasty.error(DetalleBebidas.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<FCMResponse> call, Throwable t) {
                            Log.d("Error","Error encontrado"+ t.getMessage());
                        }
                    });
                }

                else {
                    Toast.makeText(DetalleBebidas.this, "No existe token se sesión", Toast.LENGTH_SHORT).show();
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
                    String ruta = "https://firebasestorage.googleapis.com/v0/b/gestor-de-pedidos-aukdefood.appspot.com/o/PhotoProveedor%2FH1HZlKfKCjRWIR4ttx15pRKhFKM2.jpg?alt=media&token=dbf15a43-829c-4c69-8f15-f16e221a2f70";
                    Map<String,String> map = new HashMap<>();
                    map.put("title","Socio "+"Pizzeria Croissant");
                    map.put("body","ha actualizado su stock a : "+stStock+"\ndel producto : "+stNombreProducto);
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
                                    Toasty.error(DetalleBebidas.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toasty.error(DetalleBebidas.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<FCMResponse> call, Throwable t) {
                            Log.d("Error","Error encontrado"+ t.getMessage());
                        }
                    });
                }

                else {
                    Toast.makeText(DetalleBebidas.this, "No existe token se sesión", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void sendActualizeEstadoNotification(){
        //obtener nombre del socio y nombre del producto
        final String admin1 = "9sjTQMmowxWYJGTDUY98rAR2jzB3";
        final String admin2 = "UnwAmhwRzmRLn8aozWjnYFOxYat2";
        final String admin3 = "nS8J0zEj53OcXSugQsXIdMKUi5r1";
        final String stEstado = txtDisponibilidad.getText().toString();
        final String stNombreProducto = txtNombreProducto.getText().toString();
        tokenProvider.getToken(admin2).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String token = dataSnapshot.child("token").getValue().toString();
                    String ruta = "https://firebasestorage.googleapis.com/v0/b/gestor-de-pedidos-aukdefood.appspot.com/o/PhotoProveedor%2FH1HZlKfKCjRWIR4ttx15pRKhFKM2.jpg?alt=media&token=dbf15a43-829c-4c69-8f15-f16e221a2f70";
                    Map<String,String> map = new HashMap<>();
                    map.put("title","Socio "+"Pizzeria Croissant");
                    map.put("body","ha actualizado el producto "+stNombreProducto+" a "+stEstado);
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
                                    Toasty.error(DetalleBebidas.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toasty.error(DetalleBebidas.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<FCMResponse> call, Throwable t) {
                            Log.d("Error","Error encontrado"+ t.getMessage());
                        }
                    });
                }

                else {
                    Toast.makeText(DetalleBebidas.this, "No existe token se sesión", Toast.LENGTH_SHORT).show();
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
                    String ruta = "https://firebasestorage.googleapis.com/v0/b/gestor-de-pedidos-aukdefood.appspot.com/o/PhotoProveedor%2FH1HZlKfKCjRWIR4ttx15pRKhFKM2.jpg?alt=media&token=dbf15a43-829c-4c69-8f15-f16e221a2f70";
                    Map<String,String> map = new HashMap<>();
                    map.put("title","Socio "+"Pizzeria Croissant");
                    map.put("body","ha actualizado el producto "+stNombreProducto+" a:\n"+stEstado);
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
                                    Toasty.error(DetalleBebidas.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toasty.error(DetalleBebidas.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<FCMResponse> call, Throwable t) {
                            Log.d("Error","Error encontrado"+ t.getMessage());
                        }
                    });
                }

                else {
                    Toast.makeText(DetalleBebidas.this, "No existe token se sesión", Toast.LENGTH_SHORT).show();
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
                    String ruta = "https://firebasestorage.googleapis.com/v0/b/gestor-de-pedidos-aukdefood.appspot.com/o/PhotoProveedor%2FH1HZlKfKCjRWIR4ttx15pRKhFKM2.jpg?alt=media&token=dbf15a43-829c-4c69-8f15-f16e221a2f70";
                    Map<String,String> map = new HashMap<>();
                    map.put("title","Socio "+"Pizzeria Croissant");
                    map.put("body","ha actualizado el producto "+stNombreProducto+" a:\n"+stEstado);
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
                                    Toasty.error(DetalleBebidas.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toasty.error(DetalleBebidas.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<FCMResponse> call, Throwable t) {
                            Log.d("Error","Error encontrado"+ t.getMessage());
                        }
                    });
                }

                else {
                    Toast.makeText(DetalleBebidas.this, "No existe token se sesión", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mDialog.dismiss();
    }
}
