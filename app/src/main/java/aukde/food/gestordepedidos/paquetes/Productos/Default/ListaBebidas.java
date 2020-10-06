package aukde.food.gestordepedidos.paquetes.Productos.Default;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Adaptadores.AdapterAdicionales;
import aukde.food.gestordepedidos.paquetes.Adaptadores.AdapterBebidas;
import aukde.food.gestordepedidos.paquetes.Adaptadores.AdapterProductoDefault;
import aukde.food.gestordepedidos.paquetes.Inclusiones.MiToolbar;
import aukde.food.gestordepedidos.paquetes.Modelos.ProductoDefault;
import es.dmoral.toasty.Toasty;

public class ListaBebidas extends AppCompatActivity {

    private ProgressDialog mDialogActualizeData;
    private FirebaseAuth mAuth;
    DatabaseReference mDatabaseReference;
    ArrayList<ProductoDefault> listProductos;
    RecyclerView recyclerViewProducto;
    SearchView searchViewProducto;
    AdapterBebidas adapterProductoDefault;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeRedCake);
        setContentView(R.layout.activity_list_bebidas);
        MiToolbar.Mostrar(this,"Lista de bebidas",false);
        mAuth = FirebaseAuth.getInstance();
        mDialogActualizeData = new ProgressDialog(this,R.style.MyAlertDialogData);
        mDialogActualizeData.setCancelable(false);
        mDialogActualizeData.show();
        mDialogActualizeData.setContentView(R.layout.dialog_data);
        mDialogActualizeData.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        String id = mAuth.getCurrentUser().getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Proveedor").child(id).child("Bebidas");
        recyclerViewProducto = findViewById(R.id.recyclerProductoDefault);
        searchViewProducto = findViewById(R.id.searchProductoDefault);
        searchViewProducto.setBackgroundColor(Color.WHITE);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewProducto.setLayoutManager(linearLayoutManager);
        listProductos = new ArrayList<>();
        adapterProductoDefault = new AdapterBebidas(listProductos);
        recyclerViewProducto.setAdapter(adapterProductoDefault);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    listProductos.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        ProductoDefault pd = snapshot.getValue(ProductoDefault.class);
                        listProductos.add(pd);
                    }

                    Collections.reverse(listProductos);
                    adapterProductoDefault.notifyDataSetChanged();
                    mDialogActualizeData.dismiss();
                }
                else {
                    Toasty.info(ListaBebidas.this, "Sin Productos", Toast.LENGTH_SHORT,true).show();
                    mDialogActualizeData.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(ListaBebidas.this, "Error de base de datos", Toast.LENGTH_SHORT,true).show();
            }
        });

        searchViewProducto.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String texto) {
                buscar(texto);
                return true;
            }
        });

    }

    private void buscar(String texto) {
        ArrayList<ProductoDefault> lista = new ArrayList<>();
        for(ProductoDefault object : listProductos){
            if (object.getNombreProducto().toLowerCase().contains(texto.toLowerCase()) ||
                    object.getCodigoINEA().toLowerCase().contains(texto.toLowerCase()))
            {
                lista.add(object);
            }
            else {
            }
        }
        AdapterProductoDefault adapter = new AdapterProductoDefault(lista);
        recyclerViewProducto.setAdapter(adapter);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
