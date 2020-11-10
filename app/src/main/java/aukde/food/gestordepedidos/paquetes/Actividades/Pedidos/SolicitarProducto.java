package aukde.food.gestordepedidos.paquetes.Actividades.Pedidos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Inclusiones.MiToolbar;

public class SolicitarProducto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitar_producto);
        MiToolbar.Mostrar(SolicitarProducto.this,"Solicitar Producto",true);
    }

}