package aukde.food.gestordepedidos.paquetes.Actividades.Logins;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Inclusiones.MiToolbar;
import es.dmoral.toasty.Toasty;

public class RecuperarAdmin extends AppCompatActivity {
    Button btnRecuperar;
    EditText Correoelectronico;
    private ProgressBar progressBarr;
    private FirebaseAuth auth;
    private Vibrator vibrator;
    long tiempo = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeDark);
        setContentView(R.layout.activity_recuperar_contrasena_admin);
        MiToolbar.Mostrar(this,"Recuperar Contraseña",true);
        btnRecuperar=findViewById(R.id.btnRecuperar);
        Correoelectronico=findViewById(R.id.correoelectronico);
        progressBarr=findViewById(R.id.progressBar);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        auth=FirebaseAuth.getInstance();

        btnRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(tiempo);
                RecuperarContraseña();
            }
        });
    }

    private void RecuperarContraseña(){
        //Obtener valor del editText
        String Correo = Correoelectronico.getText().toString();

        //validar el ingreso del correo electronico
        if(TextUtils.isEmpty(Correo)){
            Toasty.warning(getApplication(), "Ingrese correo electronico registrado", Toast.LENGTH_SHORT,true).show();
            return;
        }

        //Mostrar progressbar
        progressBarr.setVisibility(View.VISIBLE);

        //cambiar mensaje a español
        auth.setLanguageCode("es");
        //Resetear contraseña
        auth.sendPasswordResetEmail(Correo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toasty.success(RecuperarAdmin.this,"Le hemos enviado instrucciones para restablecer su contraseñas", Toast.LENGTH_SHORT,true).show();
                }
                else{
                    Toasty.error(RecuperarAdmin.this, "Error al enviar correo electronico de reinicio ", Toast.LENGTH_SHORT,true).show();
                }
                progressBarr.setVisibility(View.GONE);
            }
        });


    }


}