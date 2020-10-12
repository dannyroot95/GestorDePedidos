package aukde.food.gestordepedidos.paquetes.Productos.Solicitud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Actividades.Pedidos.RealizarPedido;
import es.dmoral.toasty.Toasty;

public class FichaDeSolicitud extends AppCompatActivity {

    Spinner mSpinner;
    DatabaseReference mProducto;
    EditText edtProducto , edtPrecio;
    TextView edtCantidad;
    FirebaseAuth mAuth;
    private Button mBtnAdd, mbtnMin, mbtnMax, mBtnClean;
    public EditText  edtNombreCliente, edtTelefono, edtDireccion, edtReferencia;
    private TextView txtNumSolicitud,txtCantidad,txtProducto, txtPrecioUnitario,txtPtotal , txtNeto;
    private Vibrator vibrator;
    long tiempo = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha_de_solicitud);
        mAuth = FirebaseAuth.getInstance();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        txtNumSolicitud = findViewById(R.id.numSolicitud);

        edtProducto = findViewById(R.id.edtProductoSolicitud);
        edtProducto.setEnabled(false);
        edtPrecio = findViewById(R.id.edtPrecUnitarioSolicitud);
        edtCantidad = findViewById(R.id.edtCant);

        edtPrecio.setEnabled(false);
        mProducto = FirebaseDatabase.getInstance().getReference();
        mSpinner = findViewById(R.id.spinnerProducto);

        mBtnAdd = findViewById(R.id.add);
        mbtnMin = findViewById(R.id.btnMin);
        mbtnMax = findViewById(R.id.btnMax);
        mBtnClean = findViewById(R.id.btnClean);

        txtProducto = findViewById(R.id.lsProducto);
        txtPrecioUnitario = findViewById(R.id.lsPUnitario);
        txtCantidad = findViewById(R.id.lsCant);
        txtPtotal = findViewById(R.id.lsPTotal);
        txtNeto = findViewById(R.id.txtTotalProductoSolicitud);

        checkUltimaSolicitud();
        getProduct();

        mbtnMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                String num = edtCantidad.getText().toString();
                if (num.equals("1")) {
                    edtCantidad.setText("1");
                } else {
                    int Cc = Integer.parseInt(num);
                    int res = Cc - 1;
                    String Min = String.valueOf(res);
                    edtCantidad.setText(Min);
                }

            }
        });

        mbtnMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                String num = edtCantidad.getText().toString();
                int Cc = Integer.parseInt(num);
                int res = Cc + 1;
                String Min = String.valueOf(res);
                edtCantidad.setText(Min);
            }
        });

        mBtnClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                txtProducto.setText("");
                txtPrecioUnitario.setText("");
                txtCantidad.setText("");
                txtPtotal.setText("");
                txtNeto.setText("0");

            }
        });

        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                String P = edtProducto.getText().toString();
                String Pu = edtPrecio.getText().toString();
                String Cant = edtCantidad.getText().toString();
                String netoValor = txtNeto.getText().toString();
                Double dNeto = Double.parseDouble(netoValor);
                if ( P.isEmpty() || Pu.isEmpty()) {
                    Toasty.error(FichaDeSolicitud.this, "Complete los Campos", Toast.LENGTH_SHORT).show();
                } else {
                    txtProducto.append(P + "\n");
                    txtPrecioUnitario.append(Pu + "\n");
                    txtCantidad.append(Cant + "\n");
                    int Cantidad = Integer.parseInt(Cant);
                    Double Precio = Double.parseDouble(Pu);
                    Double total = Cantidad * Precio;
                    String sTotal = String.valueOf(total);
                    txtPtotal.append(sTotal + "\n");

                    Double finalNeto = total + dNeto;
                    String stNeto = String.valueOf(finalNeto);
                    txtNeto.setText(stNeto);

                }
            }
        });

    }

    public void getProduct(){
        final List<aukde.food.gestordepedidos.paquetes.Modelos.Spinner> proveedor = new ArrayList<>();
        mProducto.child("Usuarios").child("Proveedor").child(mAuth.getUid()).child("Productos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String id = ds.getKey();
                        String nombres = ds.child("nombreProducto").getValue().toString();
                        proveedor.add(new aukde.food.gestordepedidos.paquetes.Modelos.Spinner(id, nombres));
                    }

                    final ArrayAdapter<aukde.food.gestordepedidos.paquetes.Modelos.Spinner> arrayAdapter
                            = new ArrayAdapter<>(FichaDeSolicitud.this, R.layout.custom_spinner, proveedor);
                    mSpinner.setAdapter(arrayAdapter);
                    arrayAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
                    mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String stProducto = parent.getItemAtPosition(position).toString();
                            edtProducto.setText(stProducto);
                            getDataProduct();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            edtProducto.setText("");
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getDataProduct(){
        Query ultimoDato = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Proveedor")
                .child(mAuth.getUid()).child("Productos").orderByChild("nombreProducto").equalTo(edtProducto.getText().toString());
        ultimoDato.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String num = childSnapshot.child("tarifaPublicada").getValue().toString();
                    edtPrecio.setText(num);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void checkUltimaSolicitud(){
        Query ultimoDato = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Proveedor")
                .child(mAuth.getUid()).child("Solicitudes").orderByKey().limitToLast(1);
        ultimoDato.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String num = childSnapshot.child("numPedido").getValue().toString();
                        int numToString = Integer.parseInt(num);
                        int newNumPedido = numToString + 1;
                        String stNewNumPedido = String.valueOf(newNumPedido);
                        txtNumSolicitud.setText(stNewNumPedido);
                    }
                }
                else {
                    txtNumSolicitud.setText("0");
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}