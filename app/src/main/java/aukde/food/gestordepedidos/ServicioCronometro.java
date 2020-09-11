package aukde.food.gestordepedidos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ServicioCronometro extends AppCompatActivity {
    private TextView textoCronometro;
    DatabaseReference mrefsCronometro;
    private final static int ID_SERVICIO = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicio_cronometro);

        textoCronometro = (TextView) findViewById(R.id.cronometro);
        mrefsCronometro = FirebaseDatabase.getInstance().getReference();

        Button startButton = (Button) findViewById(R.id.btn_iniciar);


        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                iniciarCronometro();
            }
        });

        Button stopButton = (Button) findViewById(R.id.btn_finalizar);
        stopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pararCronometro();
                sendRatingConducta();
            }
        });

        ImplServicioCronometro.setUpdateListener(this);

    }

    @Override
    protected void onDestroy() {
        // Antes de cerrar la aplicacion se para el servicio (el cronometro)
        pararCronometro();
        super.onDestroy();
    }

    //iniciar servicio
    private void iniciarCronometro() {
        Intent service = new Intent(this, ImplServicioCronometro.class);
        //prueba
        service.putExtra("inputExtra",textoCronometro.getText().toString());
        startService(service);
    }
    //finalizar el servicio
    private void pararCronometro() {
        Intent service = new Intent(this, ImplServicioCronometro.class);
        stopService(service);
    }

    public void actualizarCronometro(double tiempo) {
        textoCronometro.setText(String.format("%.2f", tiempo) + "s");
        sendRatingConducta();
    }
    //envio a base de datos
    private void sendRatingConducta(){
        mrefsCronometro.child("Tiempo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String , Object> dataconduta = new HashMap<>();
                dataconduta.put("Tiempo2",textoCronometro.getText().toString());
                mrefsCronometro.child("Tiempo").child("Cronometro").updateChildren(dataconduta);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}