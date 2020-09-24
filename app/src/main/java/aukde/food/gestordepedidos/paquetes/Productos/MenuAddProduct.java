package aukde.food.gestordepedidos.paquetes.Productos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Productos.Default.AgregarProductoPorDefecto;
import aukde.food.gestordepedidos.paquetes.Productos.Pizza.AgregarProductoPizza;
import aukde.food.gestordepedidos.paquetes.Productos.Pollos.AgregarProductoPollos;
import aukde.food.gestordepedidos.paquetes.Productos.Tortas.AgregarProductoTortas;

public class MenuAddProduct extends AppCompatActivity {

    LinearLayout btnProducto;
    private Vibrator vibrator;
    long tiempo = 100;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeRedCake);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_productos);
        final String id = getIntent().getStringExtra("keyProduct");

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        btnProducto = findViewById(R.id.btnAddProduct);
        btnProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(tiempo);
                if(id.equals("Pizza")){
                    startActivity(new Intent(MenuAddProduct.this, AgregarProductoPizza.class));
                }
                else if (id.equals("Pollos y Parrillas")){
                    startActivity(new Intent(MenuAddProduct.this, AgregarProductoPollos.class));
                }
                else if (id.equals("Pastelería y Repostería")){
                    startActivity(new Intent(MenuAddProduct.this, AgregarProductoTortas.class));
                }
                else{
                startActivity(new Intent(MenuAddProduct.this, AgregarProductoPorDefecto.class));
                }
            }
        });




    }
}
