package aukde.food.gestordepedidos.paquetes.Actividades.Registros;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Actividades.Inicio;
import aukde.food.gestordepedidos.paquetes.Actividades.Logins.LoginAdmin;
import aukde.food.gestordepedidos.paquetes.Inclusiones.MiToolbar;
import aukde.food.gestordepedidos.paquetes.Menus.MenuAdmin;
import aukde.food.gestordepedidos.paquetes.Modelos.Administrador;
import aukde.food.gestordepedidos.paquetes.Modelos.Aukdeliver;
import aukde.food.gestordepedidos.paquetes.Providers.AdminProvider;
import aukde.food.gestordepedidos.paquetes.Providers.AukdeliverProvider;
import aukde.food.gestordepedidos.paquetes.Providers.AuthProviders;

public class RegistroAukdeliver extends AppCompatActivity {

    private TextInputEditText edtNombres, edtApellidos,edtUsername,edtDni, edtTelefono,
            edtMarcaMoto,edtPlacaMoto,edtCategoriaLic,edtNumLicencia,edtEmail, edtPassword,
            edtRepetirPass, edtClaveAuth , edtSoat;

    private ProgressDialog mDialog;
    Button mButtonRegistro;
    AuthProviders mAuthProviders;
    AukdeliverProvider mAukdeliverProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_aukdeliver);
        MiToolbar.Mostrar(this,"Registro Aukdeliver",true);

        mAuthProviders = new AuthProviders();
        mAukdeliverProvider = new AukdeliverProvider();
        mButtonRegistro = findViewById(R.id.btnRegistrarse);
        mDialog = new ProgressDialog(this);

        edtNombres = findViewById(R.id.AukdeliverNombres);
        edtApellidos = findViewById(R.id.AukdeliverApellidos);
        edtUsername = findViewById(R.id.AukdeliverUsername);
        edtDni = findViewById(R.id.AukdeliverDNI);
        edtTelefono = findViewById(R.id.AukdeliverTeléfono);
        edtMarcaMoto = findViewById(R.id.AukdeliverMarcaMoto);
        edtPlacaMoto = findViewById(R.id.AukdeliverPlaca);
        edtCategoriaLic = findViewById(R.id.AukdeliverCategoriaLic);
        edtNumLicencia =  findViewById(R.id.AukdeliverNumLicencia);
        edtEmail = findViewById(R.id.AukdeliverEmail);
        edtPassword =  findViewById(R.id.AukdeliverEdtPassword);
        edtRepetirPass = findViewById(R.id.AukdeliverRepetirContrasena);
        edtClaveAuth = findViewById(R.id.AukdeliverClaveAutorización);
        edtSoat = findViewById(R.id.AukdeliverNumSoat);

        mButtonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickRegistro();
                Intent intent = new Intent(getApplicationContext(), LoginAdmin.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("dato","valor");
                startActivity(intent);
            }
        });


    }

    private void ClickRegistro() {

        final String nombres = edtNombres.getText().toString();
        final String apellidos = edtApellidos.getText().toString();
        final String username = edtUsername.getText().toString();
        final String dni = edtDni.getText().toString();
        final String telefono = edtTelefono.getText().toString();
        final String marcaMoto = edtMarcaMoto.getText().toString();
        final String placaMoto = edtPlacaMoto.getText().toString();
        final String categoriaLic = edtCategoriaLic.getText().toString();
        final String numLicencia = edtNumLicencia.getText().toString();
        final String email = edtEmail.getText().toString();
        final String password = edtPassword.getText().toString();
        final String repetirPass = edtRepetirPass.getText().toString();
        final String ClaveAuth = edtClaveAuth.getText().toString();
        final String soat = edtSoat.getText().toString();

        if(!nombres.isEmpty() && !apellidos.isEmpty() &&!username.isEmpty() && !dni.isEmpty() && !telefono.isEmpty()
                && !marcaMoto.isEmpty() && !placaMoto.isEmpty() && !categoriaLic.isEmpty() &&
                !numLicencia.isEmpty() && !email.isEmpty() && !password.isEmpty()
                && !repetirPass.isEmpty() && !ClaveAuth.isEmpty() && !soat.isEmpty()){

            mDialog.show();
            mDialog.setMessage("Registrando usuario...");
            if(password.length()>=6){
                if(password.equals(repetirPass)){
                    if(ClaveAuth.equals("AUK2020+*") || ClaveAuth.equals("WRZ20@") || ClaveAuth.equals("GOGOOL*")){
                        registrar(nombres,apellidos,username,dni,telefono,marcaMoto,placaMoto,categoriaLic,numLicencia,email,soat,password);
                    }
                    else {
                        mDialog.dismiss();
                        Toast.makeText(RegistroAukdeliver.this,"Clave de autorización inválida",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    mDialog.dismiss();
                    Toast.makeText(RegistroAukdeliver.this,"Las contraseñas no coinciden",Toast.LENGTH_LONG).show();
                }
            }
            else {
                mDialog.dismiss();
                Toast.makeText(RegistroAukdeliver.this,"La contraseña debe ser mayor a 6 caracteres",Toast.LENGTH_LONG).show();
            }
        }

        else {
            Toast.makeText(RegistroAukdeliver.this,"Complete los todos campos",Toast.LENGTH_LONG).show();
        }
    }

    private void registrar(final String nombres, final String apellidos,final String username, final String dni, final String telefono, final String marcaMoto, final String placaMoto, final String categoriaLic, final String numLicencia, final String email,final String soat, String password) {

        mAuthProviders.Registro(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Aukdeliver aukdeliver = new Aukdeliver(id,nombres,apellidos,username,dni,telefono,marcaMoto,placaMoto,categoriaLic,numLicencia,email,soat);
                mapear(aukdeliver);
            }
        });

    }

    void mapear(Aukdeliver aukdeliver){

        mAukdeliverProvider.Mapear(aukdeliver).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    mDialog.dismiss();
                    logout();
                }

                else {
                    mDialog.dismiss();
                    Toast.makeText(RegistroAukdeliver.this, "No se pudo crear un nuevo usuario", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    void logout() {
        mAuthProviders.Logout();
        finish();
    }

}