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
import aukde.food.gestordepedidos.paquetes.Menus.MenuProveedor;
import aukde.food.gestordepedidos.paquetes.Productos.Default.Adicionales;
import aukde.food.gestordepedidos.paquetes.Productos.Default.AgregarProductoPorDefecto;
import aukde.food.gestordepedidos.paquetes.Productos.Default.Bebidas;
import aukde.food.gestordepedidos.paquetes.Productos.Pizza.AgregarProductoPizza;
import aukde.food.gestordepedidos.paquetes.Productos.Pollos.AgregarProductoPollos;
import aukde.food.gestordepedidos.paquetes.Productos.Tortas.AgregarProductoTortas;

public class MenuAddProduct extends AppCompatActivity {

    LinearLayout btnProducto , btnAdicional , btnBebidas;
    private Vibrator vibrator;
    long tiempo = 100;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeRedCake);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_productos);
        final String id = getIntent().getStringExtra("keyProduct");

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        btnAdicional = findViewById(R.id.btnAddAdicional);
        btnBebidas = findViewById(R.id.btnAddBebidas);

        btnProducto = findViewById(R.id.btnAddProduct);
        btnProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(tiempo);
                if(id.equals("Pizzas")){
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

        btnAdicional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuAddProduct.this, Adicionales.class);
                intent.putExtra("keyProduct",id);
                startActivity(intent);
                finish();
            }
        });

        btnBebidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuAddProduct.this, Bebidas.class);
                intent.putExtra("keyProduct",id);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MenuAddProduct.this, MenuProveedor.class));
        finish();
    }
}
