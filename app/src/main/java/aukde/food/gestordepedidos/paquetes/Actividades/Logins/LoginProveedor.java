package aukde.food.gestordepedidos.paquetes.Actividades.Logins;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
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
import aukde.food.gestordepedidos.paquetes.Menus.MenuAukdeliver;
import aukde.food.gestordepedidos.paquetes.Menus.MenuProveedor;
import aukde.food.gestordepedidos.paquetes.Providers.AuthProviders;

public class LoginProveedor extends AppCompatActivity {

    TextInputEditText edtEmail , edtPassword;
    Button btnlogin;
    private ProgressDialog mDialog;
    FirebaseAuth mAuth;
    DatabaseReference mDatabaseReference;
    SharedPreferences mSharedPreference;
    AuthProviders authProviders;
    TextView recuperarClave;
    private Vibrator vibrator;
    long tiempo = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeRedCake);
        setContentView(R.layout.activity_login_proveedor);
        MiToolbar.Mostrar(this,"Login Proveedor",true);
        edtEmail = findViewById(R.id.logcorreo);
        edtPassword = findViewById(R.id.logContrasena);
        recuperarClave = findViewById(R.id.txtOlvidar);
        btnlogin = findViewById(R.id.btnLogin);
        authProviders = new AuthProviders();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mSharedPreference = getApplicationContext().getSharedPreferences("tipoUsuario",MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDialog = new ProgressDialog(this);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                login();
            }
        });

        recuperarClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                startActivity(new Intent(LoginProveedor.this,RecuperarProveedor.class));

            }
        });

    }

    private void login() {
        final SharedPreferences.Editor editor = mSharedPreference.edit();
        String correo = edtEmail.getText().toString();
        String pass = edtPassword.getText().toString();

        if (!correo.isEmpty() && !pass.isEmpty()){
            mDialog.show();
            mDialog.setMessage("Iniciando sesión...");
            if (pass.length()>=6)
            {
                mAuth.signInWithEmailAndPassword(correo,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            mDatabaseReference.child("Usuarios").child("Proveedor").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        editor.putString("usuario","proveedor");
                                        editor.apply();
                                        Intent intent = new Intent(LoginProveedor.this, MenuProveedor.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                    else {
                                        editor.putString("","");
                                        editor.apply();
                                        mAuth.signOut();
                                        startActivity(new Intent(LoginProveedor.this, Inicio.class));
                                        finish();
                                        Toast.makeText(LoginProveedor.this, "No es un usuario permitido", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    mDialog.dismiss();
                                    Toast.makeText(LoginProveedor.this, "Error de servidor", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                        //task
                        else {
                            Toast.makeText(LoginProveedor.this,"El correo o la contraseña son incorrectos",Toast.LENGTH_LONG).show();
                            mDialog.dismiss();
                        }

                    }
                });
            }

            else {
                Toast.makeText(LoginProveedor.this,"La contraseña debe tener mas de 6 caracteres",Toast.LENGTH_LONG).show();
                mDialog.dismiss();
            }

        }

        else {
            Toast.makeText(LoginProveedor.this,"Complete los campos",Toast.LENGTH_LONG).show();
            mDialog.dismiss();
        }
    }
}