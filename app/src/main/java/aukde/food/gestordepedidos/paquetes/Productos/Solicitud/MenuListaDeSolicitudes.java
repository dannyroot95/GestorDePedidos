package aukde.food.gestordepedidos.paquetes.Productos.Solicitud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Inclusiones.MiToolbar;
import aukde.food.gestordepedidos.paquetes.Menus.MenuAdmin;
import aukde.food.gestordepedidos.paquetes.Menus.MenuProveedor;

public class MenuListaDeSolicitudes extends AppCompatActivity {

    Button mbtnSolicitudProducto , mbtnSolicitudDelivery;
    private Vibrator vibrator;
    long tiempo = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeRedCake);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_lista_de_solicitudes);
        MiToolbar.Mostrar(this,"Lista de solicitudes",false);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mbtnSolicitudProducto = findViewById(R.id.btnListaSolicitudProducto);
        mbtnSolicitudDelivery = findViewById(R.id.btnListaSolicitudDelivery);

        mbtnSolicitudProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                startActivity(new Intent(MenuListaDeSolicitudes.this,ListaSolicitudProductoParaProveedor.class));
                finish();
            }
        });

    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(MenuListaDeSolicitudes.this, MenuProveedor.class));
        finish();
    }
}