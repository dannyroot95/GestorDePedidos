package aukde.food.aukdeliver.paquetes.Actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import aukde.food.aukdeliver.R;
import aukde.food.aukdeliver.paquetes.Actividades.Pedidos.ListaPedidosAukdeliver;
import aukde.food.aukdeliver.paquetes.Modelos.FCMBody;
import aukde.food.aukdeliver.paquetes.Modelos.FCMResponse;
import aukde.food.aukdeliver.paquetes.Providers.NotificationProvider;
import aukde.food.aukdeliver.paquetes.Providers.TokenProvider;
import aukde.food.aukdeliver.paquetes.Servicios.ForegroundServiceCronometro;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Notificacion extends AppCompatActivity {

    private String mExtraNumPedido;
    private String mExtraNombre;
    private String mExtraID;
    private MediaPlayer mediaPlayer;
    private FirebaseAuth mAuth;
    private final static int ID_SERVICIO = 99;
    private DatabaseReference mDatabase;
    private TokenProvider tokenProvider;
    private NotificationProvider notificationProvider;
    private Vibrator vibrator;
    TextView photo;
    String defaultPhoto = "https://firebasestorage.googleapis.com/v0/b/gestor-de-pedidos-aukdefood.appspot.com/o/fotoDefault.jpg?alt=media&token=f74486bf-432e-4af6-b114-baa523e1f801";
    long[] pattern = {400, 600, 100,300,100,150,100,75};
    private FloatingActionButton mButtonAccept , mButtonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacion);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        tokenProvider = new TokenProvider();
        notificationProvider = new NotificationProvider();
        mButtonAccept = findViewById(R.id.btnFloatAccept);
        mButtonCancel = findViewById(R.id.btnFloatCancel);



        mExtraNumPedido = getIntent().getStringExtra("numPedido");
        mExtraNombre = getIntent().getStringExtra("nombre");
        mExtraID = getIntent().getStringExtra("idClient");

        //

        mediaPlayer = MediaPlayer.create(this,R.raw.ringtone);
        mediaPlayer.setLooping(true);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                             WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                             WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                             WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        mButtonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptOrder();
            }
        });

        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelOrder();
            }
        });

        vibrator.vibrate(pattern, 0);


    }

    private void acceptOrder() {

        Map<String , Object> map = new HashMap<>();
        map.put("status","accept");
        mDatabase.child("ClientBooking").child(mExtraNombre).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                cerrar();
                verLista();
            }
        });
    }

    private void cancelOrder() {
        Map<String , Object> map = new HashMap<>();
        map.put("status","cancel");
        mDatabase.child("ClientBooking").child(mExtraNombre).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
                finishAndRemoveTask();
            }
        });

    }


    private void verLista() {
        Intent intent = new Intent(getApplicationContext(), ListaPedidosAukdeliver.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void cerrar() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(2);
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