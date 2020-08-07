package aukde.food.gestordepedidos.paquetes.Actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Actividades.Pedidos.ListaPedidosAukdeliver;

public class Notificacion extends AppCompatActivity {

    private TextView ntfNumPedido,ntfNombre , ntfTelefono , ntfDirecion,ntfHora,ntfFecha , ntfGanancia;
    private Button btnListaPedido , btnCerrar;
    private String mExtraNumPedido;
    private String mExtraNombre;
    private String mExtraTelefono;
    private String mExtraDireccion;
    private String mExtraHora;
    private String mExtraFecha;
    private String mExtraGanancia;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacion);

        ntfNumPedido = findViewById(R.id.notifyNumPedido);
        ntfNombre = findViewById(R.id.notifyNombreCliente);
        ntfTelefono = findViewById(R.id.notifyTelefono);
        ntfDirecion = findViewById(R.id.notifyDireccion);
        ntfHora = findViewById(R.id.notifyHora);
        ntfFecha = findViewById(R.id.notifyFecha);
        ntfGanancia = findViewById(R.id.notifyGanancia);
        btnListaPedido = findViewById(R.id.btnVerLista);
        btnCerrar = findViewById(R.id.btnCerrar);

        mExtraNumPedido = getIntent().getStringExtra("numPedido");
        mExtraNombre = getIntent().getStringExtra("nombre");
        mExtraTelefono = getIntent().getStringExtra("telefono");
        mExtraDireccion = getIntent().getStringExtra("direccion");
        mExtraHora = getIntent().getStringExtra("hora");
        mExtraFecha = getIntent().getStringExtra("fecha");
        mExtraGanancia = getIntent().getStringExtra("ganancia");


        ntfNumPedido.setText("#"+mExtraNumPedido);
        ntfNombre.setText(mExtraNombre);
        ntfTelefono.setText(mExtraTelefono);
        ntfDirecion.setText(mExtraDireccion);
        ntfHora.setText(mExtraHora);
        ntfFecha.setText(mExtraFecha);
        ntfGanancia.setText(mExtraGanancia);

        String Delivery =  ntfGanancia.getText().toString();
        Double doubleDelivery = Double.parseDouble(Delivery);
        Double finalGananciaDelivery ;

        if(doubleDelivery < 4.00){
            finalGananciaDelivery = doubleDelivery * 0.5;
            String ganancia50P = String.valueOf(finalGananciaDelivery);
            ntfGanancia.setText(ganancia50P);
        }
        if(doubleDelivery >= 4.00 && doubleDelivery < 9.00){
            finalGananciaDelivery = doubleDelivery * 0.4;
            String ganancia40P = String.valueOf(finalGananciaDelivery);
            ntfGanancia.setText(ganancia40P);
        }
        if(doubleDelivery >= 9.00){
            finalGananciaDelivery = doubleDelivery * 0.3;
            String ganancia30P = String.valueOf(finalGananciaDelivery);
            ntfGanancia.setText(ganancia30P);
        }

        mediaPlayer = MediaPlayer.create(this,R.raw.ringtone);
        mediaPlayer.setLooping(true);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                             WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                             WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                             WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        btnListaPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verLista();
            }
        });

        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrar();
            }
        });

    }

    private void verLista() {
        Intent intent1 = new Intent();
        intent1.setClassName(this, ListaPedidosAukdeliver.class.getName());
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent1);
    }

    private void cerrar() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(2);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer !=null){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer !=null){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.release();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer !=null){
            if(!mediaPlayer.isPlaying()){
                mediaPlayer.start();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer !=null){
            if(mediaPlayer.isPlaying()){
                mediaPlayer.pause();
            }
        }
    }
}