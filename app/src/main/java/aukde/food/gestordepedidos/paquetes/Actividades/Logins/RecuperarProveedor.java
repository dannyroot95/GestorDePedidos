package aukde.food.gestordepedidos.paquetes.Actividades.Logins;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Actividades.Inicio;
import aukde.food.gestordepedidos.paquetes.Inclusiones.MiToolbar;
import es.dmoral.toasty.Toasty;

public class RecuperarProveedor extends AppCompatActivity {

    Button btnRecuperarproveedor;
    EditText CorreoelectronicoProveedor;
    private ProgressBar progressBarr;
    private FirebaseAuth auth;
    private Vibrator vibrator;
    long tiempo = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeRedCake);
        setContentView(R.layout.activity_recuperar_contrasena_proveedor);
        MiToolbar.Mostrar(this,"Recuperar Contraseña",true);
        btnRecuperarproveedor=findViewById(R.id.btnRecuperarProveedor);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        CorreoelectronicoProveedor=findViewById(R.id.RecuperarProveedor);
        progressBarr=findViewById(R.id.progressBar);
        auth=FirebaseAuth.getInstance();

        btnRecuperarproveedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(tiempo);
                RecuperarContraseñaProveedor();
            }
        });

    }

    private void RecuperarContraseñaProveedor(){
        //Obtener valor del editText
        String CorreoProveedor = CorreoelectronicoProveedor.getText().toString();

        //validar el ingreso del correo electronico
        if(TextUtils.isEmpty(CorreoProveedor)){
            Toasty.warning(getApplication(), "Ingrese su correo electronico registrado", Toast.LENGTH_SHORT,true).show();
            return;
        }

        //Mostrar progressbar
        progressBarr.setVisibility(View.VISIBLE);

        //cambiar mensaje a español
        auth.setLanguageCode("es");
        //Resetear contraseña
        auth.sendPasswordResetEmail(CorreoProveedor).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toasty.success(RecuperarProveedor.this, "Le hemos enviado instrucciones para restablecer su contraseñas", Toast.LENGTH_SHORT,true).show();
                }
                else{
                    Toasty.error(RecuperarProveedor.this,"Error al enviar correo electronico de reinicio ", Toast.LENGTH_SHORT,true).show();
                }
                progressBarr.setVisibility(View.GONE);
            }
        });


    }

}