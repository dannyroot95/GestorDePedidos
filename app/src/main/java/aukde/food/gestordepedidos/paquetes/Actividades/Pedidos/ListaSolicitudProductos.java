package aukde.food.gestordepedidos.paquetes.Actividades.Pedidos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Adaptadores.AdapterListaSolicitudProducto;
import aukde.food.gestordepedidos.paquetes.Inclusiones.MiToolbar;
import aukde.food.gestordepedidos.paquetes.Menus.MenuAdmin;
import aukde.food.gestordepedidos.paquetes.Modelos.ListaSolicitudProducto;
import es.dmoral.toasty.Toasty;

public class ListaSolicitudProductos extends AppCompatActivity {

    DatabaseReference mDatabaseReference;
    ArrayList<ListaSolicitudProducto> listSolicitudProduct;
    RecyclerView recyclerViewSolicitudProducto;
    SearchView searchViewSolicitudProduct;
    AdapterListaSolicitudProducto adapterSolicitudProduct;
    LinearLayoutManager linearLayoutManager;
    private ProgressDialog mDialogActualizeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_solicitud_producto);
        MiToolbar.Mostrar(this,"Lista de productos solicitados",true);
        mDialogActualizeData = new ProgressDialog(this,R.style.MyAlertDialogData);
        mDialogActualizeData.setCancelable(false);
        mDialogActualizeData.show();
        mDialogActualizeData.setContentView(R.layout.dialog_data);
        mDialogActualizeData.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("SolicitudDeProductos");
        recyclerViewSolicitudProducto = findViewById(R.id.recyclerListaSolicitudProducto);
        searchViewSolicitudProduct = findViewById(R.id.searchSolicitudProducto);
        searchViewSolicitudProduct.setBackgroundColor(Color.WHITE);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewSolicitudProducto.setLayoutManager(linearLayoutManager);
        listSolicitudProduct = new ArrayList<>();
        adapterSolicitudProduct = new AdapterListaSolicitudProducto(listSolicitudProduct);
        recyclerViewSolicitudProducto.setAdapter(adapterSolicitudProduct);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    listSolicitudProduct.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        ListaSolicitudProducto pd = snapshot.getValue(ListaSolicitudProducto.class);
                        listSolicitudProduct.add(pd);
                    }
                    Collections.reverse(listSolicitudProduct);
                    adapterSolicitudProduct.notifyDataSetChanged();
                    mDialogActualizeData.dismiss();
                }
                else {
                    Toasty.info(ListaSolicitudProductos.this, "Sin Solicitudes", Toast.LENGTH_SHORT,true).show();
                    mDialogActualizeData.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(ListaSolicitudProductos.this, "Error de base de datos", Toast.LENGTH_SHORT,true).show();
            }
        });

        searchViewSolicitudProduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        ArrayList<ListaSolicitudProducto> lista = new ArrayList<>();
        for(ListaSolicitudProducto object : listSolicitudProduct){
            if (object.getNombreSocio().toLowerCase().contains(texto.toLowerCase()))
            {
                lista.add(object);
            }
            else {
            }
        }
        AdapterListaSolicitudProducto adapter = new AdapterListaSolicitudProducto(lista);
        recyclerViewSolicitudProducto.setAdapter(adapter);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ListaSolicitudProductos.this, MenuAdmin.class));
        finish();
    }
}
