package aukde.food.gestordepedidos.paquetes.Actividades.Usuarios;

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
import aukde.food.gestordepedidos.paquetes.Adaptadores.AdapterListaPorProveedor;
import aukde.food.gestordepedidos.paquetes.Inclusiones.MiToolbar;
import aukde.food.gestordepedidos.paquetes.Menus.MenuAdmin;
import aukde.food.gestordepedidos.paquetes.Modelos.ListaProveedor;

public class ListaDeProveedor extends AppCompatActivity {

    private ProgressDialog mDialogActualizeData;
    DatabaseReference mDatabaseReference;
    RecyclerView recyclerViewProveedor;
    SearchView searchViewUsuariosProveedor;
    LinearLayoutManager linearLayoutManager;
    AdapterListaPorProveedor adapterListaPorProveedor;

    ArrayList<ListaProveedor> listUsuariosProveedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeDark);
        setContentView(R.layout.activity_lista_de_proveedor);

        MiToolbar.Mostrar(this,"Lista de Proveedor",false);
        mDialogActualizeData = new ProgressDialog(this,R.style.MyAlertDialogData);
        mDialogActualizeData.setCancelable(false);
        mDialogActualizeData.show();
        mDialogActualizeData.setContentView(R.layout.dialog_data);
        mDialogActualizeData.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios");

        recyclerViewProveedor = findViewById(R.id.recyclerListaProveedor);
        searchViewUsuariosProveedor = findViewById(R.id.searchListaProveedor);

        searchViewUsuariosProveedor.setBackgroundColor(Color.WHITE);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewProveedor.setLayoutManager(linearLayoutManager);
        listUsuariosProveedor = new ArrayList<>();
        adapterListaPorProveedor = new AdapterListaPorProveedor(listUsuariosProveedor);
        recyclerViewProveedor.setAdapter(adapterListaPorProveedor);

        String [] usersEmpleado = {"Proveedor"};
        for(final String datos:usersEmpleado){

            mDatabaseReference.child(datos).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        listUsuariosProveedor.clear();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            ListaProveedor pd = snapshot.getValue(ListaProveedor.class);
                            listUsuariosProveedor.add(pd);
                        }
                        Collections.reverse(listUsuariosProveedor);
                        adapterListaPorProveedor.notifyDataSetChanged();
                        mDialogActualizeData.dismiss();
                    }
                    else {
                        Toast.makeText(ListaDeProveedor.this, "Sin Usuarios", Toast.LENGTH_SHORT).show();
                        mDialogActualizeData.dismiss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ListaDeProveedor.this, "Error de base de datos", Toast.LENGTH_SHORT).show();
                }
            });

        }

        searchViewUsuariosProveedor.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        ArrayList<ListaProveedor> lista = new ArrayList<>();
        for(ListaProveedor object : listUsuariosProveedor){
            if (object.getNombres().toLowerCase().contains(texto.toLowerCase()) || object.getDni().toLowerCase().contains(texto.toLowerCase())
                    || object.getTelefono().toLowerCase().contains(texto.toLowerCase()))
            {
                lista.add(object);
            }
            else {
            }
        }
        AdapterListaPorProveedor adapter = new AdapterListaPorProveedor(lista);
        recyclerViewProveedor.setAdapter(adapter);

    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onBackPressed() {
        //NavUtils.navigateUpFromSameTask(this);
        startActivity(new Intent(ListaDeProveedor.this, MenuAdmin.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}