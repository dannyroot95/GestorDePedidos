package aukde.food.gestordepedidos.paquetes.Productos;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Inclusiones.MiToolbar;

public class AgregarProductoPorDefecto extends AppCompatActivity {

    Spinner spEmbalaje;
    TextInputEditText edtEmbalaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeRedCake);
        setContentView(R.layout.activity_agregar_producto_por_defecto);
        MiToolbar.Mostrar(this,"Agregar producto",false);
        edtEmbalaje = findViewById(R.id.embalaje);
        edtEmbalaje.setEnabled(false);

        spEmbalaje = findViewById(R.id.spinnerEmbalaje);
        ArrayAdapter<CharSequence> adapterSpinnerEmbalaje = ArrayAdapter.createFromResource(this,R.
                array.categoriaEmbalaje,R.layout.custom_spinner);
        spEmbalaje.setAdapter(adapterSpinnerEmbalaje);
        spEmbalaje.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                edtEmbalaje.setText(parent.getItemAtPosition(position).toString());
                edtEmbalaje.setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }



}
