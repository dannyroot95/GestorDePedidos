package aukde.food.gestordepedidos.paquetes.Actividades.Registros;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Actividades.Pedidos.RealizarPedido;
import aukde.food.gestordepedidos.paquetes.Inclusiones.MiToolbar;
import aukde.food.gestordepedidos.paquetes.Menus.MenuAdmin;

public class MenuRegistros extends AppCompatActivity {

    private ProgressDialog mDialog;
    private Button btnRegistroAdmin,btnRegistroAukdeliver,btnRegistroProveedor;
    private Vibrator vibrator;
    long tiempo = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeDark);
        setContentView(R.layout.activity_menu_registros);
        MiToolbar.Mostrar(this,"Registro de usuarios",false);

        btnRegistroAdmin = findViewById(R.id.btnRegistroAdmin);
        btnRegistroAukdeliver = findViewById(R.id.btnRegistroAukdeliver);
        btnRegistroProveedor = findViewById(R.id.btnRegistroProveedor);
        mDialog = new ProgressDialog(this);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        btnRegistroAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                mDialog.show();
                mDialog.setMessage("Cargando...");
                startActivity(new Intent(MenuRegistros.this,RegistroAdmin.class));
            }
        });

        btnRegistroAukdeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                mDialog.show();
                mDialog.setMessage("Cargando...");
                startActivity(new Intent(MenuRegistros.this,RegistroAukdeliver.class));
            }
        });

        btnRegistroProveedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                mDialog.show();
                mDialog.setMessage("Cargando...");
                startActivity(new Intent(MenuRegistros.this,RegistroProveedor.class));
            }
        });

    }

// actualizar activity
    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}