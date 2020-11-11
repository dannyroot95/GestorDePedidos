package aukde.food.gestordepedidos.paquetes.Actividades.Pedidos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Inclusiones.MiToolbar;
import aukde.food.gestordepedidos.paquetes.Menus.MenuAdmin;
import aukde.food.gestordepedidos.paquetes.Modelos.SpinnerProduct;

public class SolicitarProducto extends AppCompatActivity {

    Spinner mSpinnerProducto, mSpinnerProveedor , mSpinnerAdicionales , mSpinnerBebidas;
    DatabaseReference mUsuarioProveedor , mProductoReference;
    EditText mProveedor , mProducto , mPrecio ,mPrecioAdicional , mPrecioBebida, mAdicional, mBebidas;
    TextView mID,mIDProducto,mIDAdicional,mIDBebida, TxtStock ,TxtCant;
    Button mBtnAdd, mbtnMin, mbtnMax, mBtnClean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitar_producto);
        MiToolbar.Mostrar(SolicitarProducto.this,"Solicitar Producto",true);
        mSpinnerProveedor = findViewById(R.id.spinnerProveedor);
        mSpinnerProducto = findViewById(R.id.spinnerProductosDisponibles);
        mSpinnerAdicionales = findViewById(R.id.spinnerAdicionalesDisponibles);
        mSpinnerBebidas  = findViewById(R.id.spinnerBebidasDisponibles);
        mUsuarioProveedor = FirebaseDatabase.getInstance().getReference();
        mProductoReference = FirebaseDatabase.getInstance().getReference();
        mProveedor = findViewById(R.id.edtSolicitarSocio);
        mProveedor.setEnabled(false);
        mProducto = findViewById(R.id.edtSolicitarProducto);
        mProducto.setEnabled(false);
        mAdicional = findViewById(R.id.edtSolicitarAdicional);
        mAdicional.setEnabled(false);
        mBebidas = findViewById(R.id.edtSolicitarBebidas);
        mBebidas.setEnabled(false);
        mID = findViewById(R.id.txtID);
        mIDProducto = findViewById(R.id.txtIDProducto);
        mIDAdicional = findViewById(R.id.txtIDAdicional);
        mIDBebida = findViewById(R.id.txtIDBebidas);
        TxtStock = findViewById(R.id.txtStockDisponible);
        TxtCant = findViewById(R.id.txtSolicitarCantidad);
        mPrecio = findViewById(R.id.edtSolicitarPrecUnitario);
        mPrecio.setEnabled(false);
        mPrecioAdicional = findViewById(R.id.edtSolicitarPrecAdicionalUnitario);
        mPrecioAdicional.setEnabled(false);
        mPrecioBebida = findViewById(R.id.edtSolicitarPrecBebidaUnitario);
        mPrecioBebida.setEnabled(false);
        mBtnAdd = findViewById(R.id.add);
        mbtnMin = findViewById(R.id.btnMin);
        mbtnMax = findViewById(R.id.btnMax);
        mBtnClean = findViewById(R.id.btnClean);

        mbtnMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = TxtCant.getText().toString();
                if (num.equals("1")) {
                    TxtCant.setText("1");
                } else {
                    int Cc = Integer.parseInt(num);
                    int res = Cc - 1;
                    String Min = String.valueOf(res);
                    TxtCant.setText(Min);
                }

            }
        });

        mbtnMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = TxtCant.getText().toString();
                int Cc = Integer.parseInt(num);
                int res = Cc + 1;
                String Min = String.valueOf(res);
                TxtCant.setText(Min);
            }
        });

        obtenerProveedor();

    }

    private void obtenerProveedor() {
        final List<aukde.food.gestordepedidos.paquetes.Modelos.Spinner> proveedor = new ArrayList<>();
        mUsuarioProveedor.child("Usuarios").child("Proveedor").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String id = ds.getKey();
                        String nombres = ds.child("nombre empresa").getValue().toString();
                        proveedor.add(new aukde.food.gestordepedidos.paquetes.Modelos.Spinner(id, nombres));
                    }

                    final ArrayAdapter<aukde.food.gestordepedidos.paquetes.Modelos.Spinner> arrayAdapter
                            = new ArrayAdapter<>(SolicitarProducto.this, R.layout.custom_spinner, proveedor);
                    mSpinnerProveedor.setAdapter(arrayAdapter);
                    arrayAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
                    mSpinnerProveedor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String stProveedor = parent.getItemAtPosition(position).toString();
                            cleaner();
                            mProveedor.setText(stProveedor);
                            mProveedor.setTextColor(0xff000000);
                            obtenerKeyProveedor();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            mProveedor.setText("");
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void obtenerKeyProveedor(){
        mUsuarioProveedor.child("Usuarios").child("Proveedor").orderByChild("nombre_empresa").equalTo(mProveedor.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                        String key = childSnapshot.getKey();
                        mID.setText(key);
                        obtenerProducto();
                        obtenerAdicionales();
                        obtenerBebidas();
                    }
                }
                else{
                    Toast.makeText(SolicitarProducto.this, "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void obtenerProducto() {
        final List<SpinnerProduct> producto= new ArrayList<>();
        mUsuarioProveedor.child("Usuarios").child("Proveedor").child(mID.getText().toString()).child("Productos")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String id = ds.getKey();
                        String nombreP = ds.child("nombreProducto").getValue().toString();
                        producto.add(new SpinnerProduct(id, nombreP));
                    }

                    final ArrayAdapter<SpinnerProduct> arrayAdapter
                            = new ArrayAdapter<>(SolicitarProducto.this, R.layout.custom_spinner, producto);
                    mSpinnerProducto.setAdapter(arrayAdapter);
                    arrayAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
                    mSpinnerProducto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String stProducto = parent.getItemAtPosition(position).toString();
                            mProducto.setText(stProducto);
                            mProducto.setTextColor(0xff000000);
                            obtenerProductoID();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            mProducto.setText("");
                        }
                    });
                }
                else {
                    mProducto.setText("");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void obtenerProductoID() {

        mUsuarioProveedor.child("Usuarios").child("Proveedor")
                .child(mID.getText().toString())
                .child("Productos")
                .orderByChild("nombreProducto")
                .equalTo(mProducto.getText().toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                        String key = childSnapshot.getKey();
                        mIDProducto.setText(key);
                        getAllDataProduct();
                    }
                }
                else{
                    Toast.makeText(SolicitarProducto.this, "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void obtenerAdicionales() {
        final List<SpinnerProduct> producto= new ArrayList<>();
        mUsuarioProveedor.child("Usuarios").child("Proveedor").child(mID.getText().toString()).child("Adicionales")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                String id = ds.getKey();
                                String nombreP = ds.child("nombreProducto").getValue().toString();
                                producto.add(new SpinnerProduct(id, nombreP));
                            }

                            final ArrayAdapter<SpinnerProduct> arrayAdapter
                                    = new ArrayAdapter<>(SolicitarProducto.this, R.layout.custom_spinner, producto);
                            mSpinnerAdicionales.setAdapter(arrayAdapter);
                            arrayAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
                            mSpinnerAdicionales.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    String stAdicional= parent.getItemAtPosition(position).toString();
                                    mAdicional.setText(stAdicional);
                                    mAdicional.setTextColor(0xff000000);
                                    obtenerAdicionalID();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    mAdicional.setText("");
                                }
                            });
                        }
                        else {
                            mAdicional.setText("");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

    }

    private void obtenerAdicionalID() {

        mUsuarioProveedor.child("Usuarios").child("Proveedor")
                .child(mID.getText().toString())
                .child("Adicionales")
                .orderByChild("nombreProducto")
                .equalTo(mAdicional.getText().toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                                String key = childSnapshot.getKey();
                                mIDAdicional.setText(key);
                                getAllDataAditional();
                            }
                        }
                        else{
                            Toast.makeText(SolicitarProducto.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void getAllDataAditional() {
        mUsuarioProveedor.child("Usuarios").child("Proveedor")
                .child(mID.getText().toString())
                .child("Adicionales")
                .child(mIDAdicional.getText().toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String disponibilidad = dataSnapshot.child("disponibilidad").getValue().toString();
                            String precio = dataSnapshot.child("tarifaConfidencial").getValue().toString();
                            mPrecioAdicional.setText(precio);
                            mPrecioAdicional.setTextColor(0xff000000);
                        }
                        else{
                            Toast.makeText(SolicitarProducto.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void obtenerBebidas() {
        final List<SpinnerProduct> producto= new ArrayList<>();
        mUsuarioProveedor.child("Usuarios").child("Proveedor").child(mID.getText().toString()).child("Bebidas")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                String id = ds.getKey();
                                String nombreP = ds.child("nombreProducto").getValue().toString();
                                producto.add(new SpinnerProduct(id, nombreP));
                            }

                            final ArrayAdapter<SpinnerProduct> arrayAdapter
                                    = new ArrayAdapter<>(SolicitarProducto.this, R.layout.custom_spinner, producto);
                            mSpinnerBebidas.setAdapter(arrayAdapter);
                            arrayAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
                            mSpinnerBebidas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    String stAdicional= parent.getItemAtPosition(position).toString();
                                    mBebidas.setText(stAdicional);
                                    mBebidas.setTextColor(0xff000000);
                                    obtenerBebidasID();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    mBebidas.setText("");
                                }
                            });
                        }
                        else {
                            mBebidas.setText("");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

    }

    private void obtenerBebidasID() {

        mUsuarioProveedor.child("Usuarios").child("Proveedor")
                .child(mID.getText().toString())
                .child("Bebidas")
                .orderByChild("nombreProducto")
                .equalTo(mBebidas.getText().toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                                String key = childSnapshot.getKey();
                                mIDBebida.setText(key);
                                getAllDataBebidas();
                            }
                        }
                        else{
                            Toast.makeText(SolicitarProducto.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void getAllDataBebidas() {
        mUsuarioProveedor.child("Usuarios").child("Proveedor")
                .child(mID.getText().toString())
                .child("Bebidas")
                .child(mIDBebida.getText().toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String disponibilidad = dataSnapshot.child("disponibilidad").getValue().toString();
                            String precio = dataSnapshot.child("tarifaConfidencial").getValue().toString();
                            mPrecioBebida.setText(precio);
                            mPrecioBebida.setTextColor(0xff000000);
                        }
                        else{
                            Toast.makeText(SolicitarProducto.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    private void getAllDataProduct() {
        mUsuarioProveedor.child("Usuarios").child("Proveedor")
                .child(mID.getText().toString())
                .child("Productos")
                .child(mIDProducto.getText().toString())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            String stock = dataSnapshot.child("stock").getValue().toString();
                            String precio = dataSnapshot.child("tarifaConfidencial").getValue().toString();
                            TxtStock.setText(stock);
                            TxtStock.setTextColor(0xff000000);
                            mPrecio.setText(precio);
                            mPrecio.setTextColor(0xff000000);
                        }
                        else{
                            Toast.makeText(SolicitarProducto.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void cleaner(){
        mSpinnerProducto.setAdapter(null);
        mSpinnerProducto.setOnItemSelectedListener(null);
        mSpinnerAdicionales.setAdapter(null);
        mSpinnerAdicionales.setOnItemSelectedListener(null);
        mSpinnerBebidas.setAdapter(null);
        mSpinnerBebidas.setOnItemSelectedListener(null);
        mIDProducto.setText("");
        mIDAdicional.setText("");
        mIDBebida.setText("");
        mProducto.setText("");
        mAdicional.setText("");
        mBebidas.setText("");
        mPrecio.setText("");
        mPrecioAdicional.setText("");
        mPrecioBebida.setText("");
        TxtStock.setText("");
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SolicitarProducto.this, MenuAdmin.class));
        finish();
    }
}