package aukde.food.gestordepedidos.paquetes.Productos.Tortas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Inclusiones.MiToolbar;

public class AgregarProductoTortas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto_tortas);
        MiToolbar.Mostrar(this,"Tortas",false);
    }
}