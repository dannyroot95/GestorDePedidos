package aukde.food.gestordepedidos.paquetes.Actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Actividades.Logins.LoginAdmin;
import aukde.food.gestordepedidos.paquetes.Actividades.Logins.LoginAukdeliver;
import aukde.food.gestordepedidos.paquetes.Actividades.Logins.LoginProveedor;
import aukde.food.gestordepedidos.paquetes.Mapas.MapaClientePorLlamada;
import aukde.food.gestordepedidos.paquetes.Menus.MenuAdmin;
import aukde.food.gestordepedidos.paquetes.Menus.MenuAukdeliver;
import aukde.food.gestordepedidos.paquetes.Menus.MenuProveedor;

public class Inicio extends AppCompatActivity {

    private Button btnAdminLogin,btnAukdeliverLogin,btnProveedorLogin;
    SharedPreferences mPreference;
    private Vibrator vibrator;
    private ProgressDialog mDialog;
    long tiempo = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme2);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAdminLogin = findViewById(R.id.btnAdmin);
        btnAukdeliverLogin = findViewById(R.id.btnAukdeliver);
        mDialog = new ProgressDialog(Inicio.this,R.style.ThemeOverlay);
        btnProveedorLogin = findViewById(R.id.btnProveedor);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mPreference = getApplicationContext().getSharedPreferences("tipoUsuario",MODE_PRIVATE);

        btnAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.show();
                mDialog.setCancelable(false);
                mDialog.setMessage("Cargando...");
                vibrator.vibrate(tiempo);
                startActivity(new Intent(Inicio.this, LoginAdmin.class));
            }
        });

        btnAukdeliverLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.show();
                mDialog.setCancelable(false);
                mDialog.setMessage("Cargando...");
                vibrator.vibrate(tiempo);
                startActivity(new Intent(Inicio.this, LoginAukdeliver.class));
            }
        });
        btnProveedorLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.show();
                mDialog.setCancelable(false);
                mDialog.setMessage("Cargando...");
                vibrator.vibrate(tiempo);
                startActivity(new Intent(Inicio.this, LoginProveedor.class));
            }
        });

    }

    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            String admin = mPreference.getString("usuario","");
            if(admin.equals("administrador")){
                Intent intent = new Intent(Inicio.this, MenuAdmin.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            else if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                String aukdeliver = mPreference.getString("usuario","");
                if(aukdeliver.equals("aukdeliver")){
                    Intent intent = new Intent(Inicio.this, MenuAukdeliver.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                       }
                else {
                    /*escribir algun error si falla*/
                }
            }
            else {
                /*escribir algun error si falla*/
            }
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    String proveedor = mPreference.getString("usuario","");
                    if(proveedor.equals("proveedor")){
                    Intent intent = new Intent(Inicio.this, MenuProveedor.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    }
                    else {
                        /*escribir algun error si falla*/
                    }
                }
                else {
                    /*escribir algun error si falla*/
            }
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