package aukde.food.gestordepedidos.paquetes.Productos;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Inclusiones.MiToolbar;

public class AgregarProductoPorDefecto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeRedCake);
        setContentView(R.layout.activity_agregar_producto_por_defecto);
        MiToolbar.Mostrar(this,"Agregar producto",false);
    }



}
