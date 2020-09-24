package aukde.food.gestordepedidos.paquetes.Productos.Pizza;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Inclusiones.MiToolbar;

public class AgregarProductoPizza extends AppCompatActivity {

    @Override
    public void onCreate( Bundle savedInstanceState) {
        setTheme(R.style.AppThemeRedCake);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto_pizza);
        MiToolbar.Mostrar(this,"Pizza",false);
    }
}
