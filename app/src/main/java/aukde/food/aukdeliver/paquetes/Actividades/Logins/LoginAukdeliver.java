package aukde.food.aukdeliver.paquetes.Actividades.Logins;

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
import aukde.food.aukdeliver.R;
import aukde.food.aukdeliver.paquetes.Inclusiones.MiToolbar;
import aukde.food.aukdeliver.paquetes.Menus.MenuAukdeliver;
import aukde.food.aukdeliver.paquetes.Providers.AuthProviders;
import es.dmoral.toasty.Toasty;

public class LoginAukdeliver extends AppCompatActivity {

  TextInputEditText edtEmail , edtPassword;
  Button btnlogin;
  private ProgressDialog mDialog;
  FirebaseAuth mAuth;
  DatabaseReference mDatabaseReference;
  AuthProviders authProviders;
  TextView recuperarClave;
  private Vibrator vibrator;
  long tiempo = 100;
  SharedPreferences mSharedPreference;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setTheme(R.style.AppTheme2);
    setContentView(R.layout.activity_login_aukdeliver);
    MiToolbar.Mostrar(this,"Iniciar Sesión",false);
    edtEmail = findViewById(R.id.logcorreo);
    edtPassword = findViewById(R.id.logContrasena);
    btnlogin =  findViewById(R.id.btnLogin);
    recuperarClave = findViewById(R.id.txtOlvidar);
    vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    authProviders = new AuthProviders();
    mAuth = FirebaseAuth.getInstance();
    mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    mDialog = new ProgressDialog(LoginAukdeliver.this,R.style.ThemeOverlay);
    mSharedPreference = getApplicationContext().getSharedPreferences("tipoUsuario",MODE_PRIVATE);

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
        mDialog.setCancelable(false);
        mDialog.show();
        mDialog.setMessage("Cargando...");
        startActivity(new Intent(LoginAukdeliver.this,RecuperarAukdelivery.class));
      }
    });

  }

  private void login() {
    final SharedPreferences.Editor editor = mSharedPreference.edit();
    String correo = edtEmail.getText().toString();
    String pass = edtPassword.getText().toString();

    if (!correo.isEmpty() && !pass.isEmpty()){
      mDialog.show();
      mDialog.setCancelable(false);
      mDialog.setMessage("Iniciando sesión...");
      if (pass.length()>=6)
      {
        mAuth.signInWithEmailAndPassword(correo,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()) {
              mDatabaseReference.child("Usuarios").child("Aukdeliver").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  if(dataSnapshot.exists()){
                    editor.putString("usuario","aukdeliver");
                    editor.apply();
                    Intent intent = new Intent(LoginAukdeliver.this, MenuAukdeliver.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                  }
                  else {
                    mDialog.dismiss();
                    editor.putString("","");
                    editor.apply();
                    mAuth.signOut();
                    Toasty.error(LoginAukdeliver.this, "No es un usuario permitido", Toast.LENGTH_SHORT, true).show();
                  }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                  mDialog.dismiss();
                  Toasty.error(LoginAukdeliver.this, "Error de servidor", Toast.LENGTH_SHORT, true).show();
                }
              });

            }
            //task
            else {
              Toasty.error(LoginAukdeliver.this, "El correo o la contraseña son incorrectos", Toast.LENGTH_SHORT, true).show();
              mDialog.dismiss();
            }

          }
        });
      }

      else {
        Toasty.error(LoginAukdeliver.this, "La contraseña debe tener más de 6 caracteres", Toast.LENGTH_SHORT, true).show();
        mDialog.dismiss();
      }

    }

    else {
      Toasty.error(LoginAukdeliver.this, "Complete los campos", Toast.LENGTH_SHORT, true).show();
      mDialog.dismiss();
    }
  }


  protected void onStart() {
    super.onStart();
    if (FirebaseAuth.getInstance().getCurrentUser() != null){
          Intent intent = new Intent(LoginAukdeliver.this, MenuAukdeliver.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
          startActivity(intent);
        }
        else {
          /*escribir algun error si falla*/
        }
  }


  @Override
  protected void onResume() {
    super.onResume();
    mDialog.dismiss();
  }

}