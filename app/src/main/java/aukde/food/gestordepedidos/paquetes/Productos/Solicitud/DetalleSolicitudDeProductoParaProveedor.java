package aukde.food.gestordepedidos.paquetes.Productos.Solicitud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Modelos.ListaSolicitudProducto;
import aukde.food.gestordepedidos.paquetes.Modelos.PedidoLlamada;
import es.dmoral.toasty.Toasty;

public class DetalleSolicitudDeProductoParaProveedor extends AppCompatActivity {

    TextView mTxtProductos , mTxtNota , mTxtCantidad , mTxtPrecioTotal , mTxtTotalACobrar,
            mTxtEstado , txtNumSolicitudProducto;
    FloatingActionButton floatRotate;
    Button mBtnConfirmar , mBtnRechazar;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Vibrator vibrator;
    long tiempo = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_solicitud_de_producto_para_proveedor);
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
        txtNumSolicitudProducto = findViewById(R.id.numSolicitudProducto);

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
        if (mTxtEstado.getText().toString().equals("Sin confirmar")){
            mTxtEstado.setTextColor(Color.parseColor("#FC0000"));
        }
        else
        {
            mTxtEstado.setTextColor(Color.parseColor("#14A72A"));
        }
        mTxtTotalACobrar.setText(arrayList.get(5));
        txtNumSolicitudProducto.setText(arrayList.get(6));

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
                confirm();
            }
        });

        mBtnRechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nulled();
            }
        });

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
                        Toasty.success(DetalleSolicitudDeProductoParaProveedor.this, "Solicitud Confirmada", Toast.LENGTH_SHORT,true).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        finish();
    }

    private void nulled(){

    }

}