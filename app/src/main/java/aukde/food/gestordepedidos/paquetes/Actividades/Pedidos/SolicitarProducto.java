package aukde.food.gestordepedidos.paquetes.Actividades.Pedidos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Inclusiones.MiToolbar;
import aukde.food.gestordepedidos.paquetes.Menus.MenuAdmin;
import aukde.food.gestordepedidos.paquetes.Modelos.SpinnerProduct;
import es.dmoral.toasty.Toasty;

public class SolicitarProducto extends AppCompatActivity {

    Spinner mSpinnerProducto, mSpinnerProveedor , mSpinnerAdicionales , mSpinnerBebidas;
    DatabaseReference mUsuarioProveedor , mProductoReference;
    EditText mProveedor , mProducto  , mPrecio ,mPrecioAdicional , mPrecioBebida,
            mAdicional, mBebidas , mNota;
    TextView mID,mIDProducto,mIDAdicional,mIDBebida, TxtStock ,TxtCant;
    Button   mBtnAddProduct , mBtnAddAdicional , mBtnAddBebidas , mbtnMin, mbtnMax,
            mBtnClean , mbtnMinAdicional , mbtnMaxAdicional
            , mbtnMinBebida , mbtnMaxBebida , mBtnDeleteElement , mBtnSolicitar;
    TextView txtProductoList, txtNotaList , txtPrecioUniList, txtCantidadList , txtCantidadAdicionalList
            , txtCantidadABebidaList , txtPrecioTotalList;
    TextView txtPrecioNeto;
    private Vibrator vibrator;
    long tiempo = 100;
    private ProgressDialog mDialog;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitar_producto);
        MiToolbar.Mostrar(SolicitarProducto.this,"Solicitar Producto",true);
        mDialog = new ProgressDialog(this, R.style.ThemeOverlay);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
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
        mNota = findViewById(R.id.edtSolicitarDescripcion);
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
        mBtnAddProduct = findViewById(R.id.add);
        mBtnAddAdicional = findViewById(R.id.add2);
        mBtnAddBebidas = findViewById(R.id.add3);
        mbtnMin = findViewById(R.id.btnMin);
        mbtnMax = findViewById(R.id.btnMax);
        mbtnMinAdicional = findViewById(R.id.btnMinAdicional);
        mbtnMaxAdicional = findViewById(R.id.btnMaxAdicional);
        mbtnMinBebida = findViewById(R.id.btnMinBebida);
        mbtnMaxBebida = findViewById(R.id.btnMaxBebida);
        mBtnClean = findViewById(R.id.btnClean);
        mBtnDeleteElement = findViewById(R.id.btnDeleteElement);
        mBtnSolicitar = findViewById(R.id.btnSolicitarProducto);
        txtProductoList = findViewById(R.id.lsProducto);
        txtNotaList = findViewById(R.id.lsDescripcion);
        txtPrecioUniList = findViewById(R.id.lsPUnitario);
        txtCantidadABebidaList = findViewById(R.id.txtSolicitarBebidaCantidad);
        txtCantidadList = findViewById(R.id.lsCant);
        txtCantidadAdicionalList = findViewById(R.id.txtSolicitarAdicionalCantidad);
        txtPrecioTotalList = findViewById(R.id.lsPTotal);
        txtPrecioNeto = findViewById(R.id.txtNeto);

        mbtnMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
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
                vibrator.vibrate(tiempo);
                String num = TxtCant.getText().toString();
                int Cc = Integer.parseInt(num);
                int res = Cc + 1;
                String Min = String.valueOf(res);
                TxtCant.setText(Min);
            }
        });

        mbtnMinAdicional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                String num = txtCantidadAdicionalList.getText().toString();
                if (num.equals("1")) {
                    TxtCant.setText("1");
                } else {
                    int Cc = Integer.parseInt(num);
                    int res = Cc - 1;
                    String Min = String.valueOf(res);
                    txtCantidadAdicionalList.setText(Min);
                }

            }
        });

        mbtnMaxAdicional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                String num = txtCantidadAdicionalList.getText().toString();
                int Cc = Integer.parseInt(num);
                int res = Cc + 1;
                String Min = String.valueOf(res);
                txtCantidadAdicionalList.setText(Min);
            }
        });

        mbtnMinBebida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                String num = txtCantidadABebidaList.getText().toString();
                if (num.equals("1")) {
                    TxtCant.setText("1");
                } else {
                    int Cc = Integer.parseInt(num);
                    int res = Cc - 1;
                    String Min = String.valueOf(res);
                    txtCantidadABebidaList.setText(Min);
                }

            }
        });

        mbtnMaxBebida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                String num = txtCantidadABebidaList.getText().toString();
                int Cc = Integer.parseInt(num);
                int res = Cc + 1;
                String Min = String.valueOf(res);
                txtCantidadABebidaList.setText(Min);
            }
        });

        mBtnDeleteElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                String desc [] = txtProductoList.getText().toString().split("\n");
                String desc2 [] = txtNotaList.getText().toString().split("\n");
                String desc3 [] = txtPrecioUniList.getText().toString().split("\n");
                String desc4 [] = txtCantidadList.getText().toString().split("\n");
                String desc5 [] = txtPrecioTotalList.getText().toString().split("\n");
                StringBuilder builder = new StringBuilder();
                StringBuilder builder2 = new StringBuilder();
                StringBuilder builder3 = new StringBuilder();
                StringBuilder builder4 = new StringBuilder();
                StringBuilder builder5 = new StringBuilder();
                for (int i = 0 ; i<desc.length - 1 ; i++){
                    builder.append(desc[i]+"\n");
                }
                for (int j = 0 ; j<desc2.length - 1 ; j++){
                    builder2.append(desc2[j]+"\n");
                }
                for (int k = 0 ; k<desc3.length - 1 ; k++){
                    builder3.append(desc3[k]+"\n");
                }
                for (int l = 0 ; l<desc4.length - 1 ; l++){
                    builder4.append(desc4[l]+"\n");
                }
                for (int m = 0 ; m<desc5.length - 1 ; m++){
                    builder5.append(desc5[m]+"\n");
                }
                String joined = builder.toString();
                String joined2 = builder2.toString();
                String joined3 = builder3.toString();
                String joined4 = builder4.toString();
                String joined5 = builder5.toString();
                txtProductoList.setText("");
                txtNotaList.setText("");
                txtPrecioUniList.setText("");
                txtCantidadList.setText("");
                txtPrecioTotalList.setText("");
                txtProductoList.append(joined);
                txtNotaList.append(joined2);
                txtPrecioUniList.append(joined3);
                txtCantidadList.append(joined4);
                txtPrecioTotalList.append(joined5);
                subtractPriceList();
                //Toast.makeText(SolicitarProducto.this, joined, Toast.LENGTH_SHORT).show();

            }
        });

        mBtnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                int stock = Integer.parseInt(TxtCant.getText().toString());
                int cant = Integer.parseInt(TxtStock.getText().toString());
                int res;
                if (stock>cant) {
                    Toasty.error(SolicitarProducto.this, "Stock Insuficiente", Toast.LENGTH_SHORT).show();
                }

                else{
                    res = cant - stock;
                    String newStock = String.valueOf(res);
                    TxtStock.setText(newStock);
                    AddProduct();
                }

            }
        });

        mBtnAddAdicional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                AddAditional();
            }
        });

        mBtnAddBebidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                AddBebida();
            }
        });

        mBtnSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                sendSolicitudeProvider();
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
                .addValueEventListener(new ValueEventListener() {
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

    private void AddProduct(){

        String stProduct = mProducto.getText().toString();
        String stNota = mNota.getText().toString();
        String stCantidad = TxtCant.getText().toString();
        String stPrecioUnitario = mPrecio.getText().toString();
        String netoValor = txtPrecioNeto.getText().toString();

        if(!stProduct.isEmpty() && !stCantidad.isEmpty() && !stPrecioUnitario.isEmpty()){
            txtProductoList.append(stProduct+"\n");
            txtNotaList.append(stNota+"\n");
            txtCantidadList.append(stCantidad+"\n");
            txtPrecioUniList.append(stPrecioUnitario+"\n");
            int Cantidad = Integer.parseInt(stCantidad);
            Double Precio = Double.parseDouble(stPrecioUnitario);
            Double total = Cantidad * Precio;
            String sTotal = String.valueOf(total);
            txtPrecioTotalList.append(sTotal + "\n");
            Double dNeto = Double.parseDouble(netoValor);
            Double finalNeto = total + dNeto;
            String stNeto = String.valueOf(finalNeto);
            txtPrecioNeto.setText(stNeto);

        }
        else {
            Toasty.error(SolicitarProducto.this, "Complete los Campos", Toast.LENGTH_SHORT).show();
        }

    }

    private void AddAditional(){

        String stProduct = mAdicional.getText().toString();
        String stNota = "ninguno";
        String stCantidad = txtCantidadAdicionalList.getText().toString();
        String stPrecioUnitario = mPrecioAdicional.getText().toString();
        String netoValor = txtPrecioNeto.getText().toString();

        if(!stProduct.isEmpty() && !stCantidad.isEmpty() && !stPrecioUnitario.isEmpty()){
            txtProductoList.append(stProduct+"\n");
            txtCantidadList.append(stCantidad+"\n");
            txtPrecioUniList.append(stPrecioUnitario+"\n");
            txtNotaList.append(stNota+"\n");
            int Cantidad = Integer.parseInt(stCantidad);
            Double Precio = Double.parseDouble(stPrecioUnitario);
            Double total = Cantidad * Precio;
            String sTotal = String.valueOf(total);
            txtPrecioTotalList.append(sTotal + "\n");
            Double dNeto = Double.parseDouble(netoValor);
            Double finalNeto = total + dNeto;
            String stNeto = String.valueOf(finalNeto);
            txtPrecioNeto.setText(stNeto);

        }
        else {
            Toasty.error(SolicitarProducto.this, "Complete los Campos", Toast.LENGTH_SHORT).show();
        }

    }

    private void AddBebida(){

        String stProduct = mBebidas.getText().toString();
        String stNota = "ninguno";
        String stCantidad = txtCantidadABebidaList.getText().toString();
        String stPrecioUnitario = mPrecioBebida.getText().toString();
        String netoValor = txtPrecioNeto.getText().toString();

        if(!stProduct.isEmpty() && !stCantidad.isEmpty() && !stPrecioUnitario.isEmpty()){
            txtProductoList.append(stProduct+"\n");
            txtCantidadList.append(stCantidad+"\n");
            txtPrecioUniList.append(stPrecioUnitario+"\n");
            txtNotaList.append(stNota+"\n");
            int Cantidad = Integer.parseInt(stCantidad);
            Double Precio = Double.parseDouble(stPrecioUnitario);
            Double total = Cantidad * Precio;
            String sTotal = String.valueOf(total);
            txtPrecioTotalList.append(sTotal + "\n");
            Double dNeto = Double.parseDouble(netoValor);
            Double finalNeto = total + dNeto;
            String stNeto = String.valueOf(finalNeto);
            txtPrecioNeto.setText(stNeto);

        }
        else {
            Toasty.error(SolicitarProducto.this, "Complete los Campos", Toast.LENGTH_SHORT).show();
        }
    }

    private void subtractPriceList(){
        String desc [] = txtPrecioTotalList.getText().toString().split("\n");
        double sub = 0;
        for (int i = 0 ; i<desc.length ; i++){
            if(!desc[i].isEmpty()){
            sub += Double.parseDouble(desc[i]);
            }
            else{
                txtPrecioNeto.setText("0.0");
            }
        }
        String stSub = String.valueOf(sub);
        txtPrecioNeto.setText(stSub);
    }

    private void sendSolicitudeProvider(){
        String producto = txtProductoList.getText().toString();
        String nota = txtNotaList.getText().toString();
        String cantidad = txtCantidadList.getText().toString();
        String precioTotal = txtPrecioTotalList.getText().toString();
        String neto = txtPrecioNeto.getText().toString();
        String precioUnitario = txtPrecioUniList.getText().toString();

        if (!producto.isEmpty() && !nota.isEmpty() && !cantidad.isEmpty() && !precioTotal.isEmpty() && !neto.isEmpty() && !precioUnitario.isEmpty()) {


            mDialog.show();
            mDialog.setMessage("Enviando Solicitud...");
            mDialog.setCancelable(false);
            final Map<String, Object> map = new HashMap<>();
            map.put("nombreProducto", producto);
            map.put("descripcion", nota);
            map.put("cantidad", cantidad);
            map.put("estado","Sin confirmar");
            map.put("IDsocio",mID.getText().toString());
            map.put("precioTotalPorProducto", precioTotal);
            map.put("totalACobrar", neto);
            map.put("nombreSocio", mProveedor.getText().toString());
            mDatabase.child("Usuarios").child("Proveedor")
                    .child(mID.getText().toString()).child("SolicitudDeProductos").push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    mDatabase2.child("SolicitudDeProductos").push().setValue(map);
                    UpdateStock();
                    mDialog.dismiss();
                    startActivity(new Intent(SolicitarProducto.this,MenuAdmin.class));
                    finish();
                    Toasty.success(SolicitarProducto.this, "Solicitud Enviada!", Toast.LENGTH_SHORT, true).show();
                }
            }).addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {
                    Toasty.error(SolicitarProducto.this, "Error!", Toast.LENGTH_SHORT, true).show();
                }
            });
        }
        else {
            Toasty.info(SolicitarProducto.this, "Agrege sus Productos", Toast.LENGTH_SHORT, true).show();
        }
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

    private void UpdateStock(){
        String idSocio = mID.getText().toString();
        String idProducto = mIDProducto.getText().toString();

        Map<String,Object> map = new HashMap<>();
        map.put("stock",TxtStock.getText().toString());

        mProductoReference.child("Usuarios").child("Proveedor").child(idSocio).child("Productos")
                .child(idProducto).updateChildren(map);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SolicitarProducto.this, MenuAdmin.class));
        finish();
    }
}