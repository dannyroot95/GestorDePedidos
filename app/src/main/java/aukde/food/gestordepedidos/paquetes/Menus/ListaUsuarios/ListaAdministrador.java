package aukde.food.gestordepedidos.paquetes.Menus.ListaUsuarios;


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
import aukde.food.gestordepedidos.paquetes.Adaptadores.AdapterListaPorAdministrador;
import aukde.food.gestordepedidos.paquetes.Inclusiones.MiToolbar;
import aukde.food.gestordepedidos.paquetes.Menus.MenuAdmin;

public class ListaAdministrador extends AppCompatActivity {

    private ProgressDialog mDialogActualizeData;
    DatabaseReference mDatabaseReference;
    RecyclerView recyclerViewPedidos;
    SearchView searchViewUsuarios;
    LinearLayoutManager linearLayoutManager;
    AdapterListaPorAdministrador adapterListaPorAdministrador;

    ArrayList<aukde.food.gestordepedidos.paquetes.Modelos.ListaAdministrador> listAdministrador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeDark);
        setContentView(R.layout.activity_lista_de_administrador);
        MiToolbar.Mostrar(this,"Lista de Administrador",false);
        mDialogActualizeData = new ProgressDialog(this,R.style.MyAlertDialogData);
        mDialogActualizeData.setCancelable(false);
        mDialogActualizeData.show();
        mDialogActualizeData.setContentView(R.layout.dialog_data);
        mDialogActualizeData.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        //mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Administrador");

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios");

        recyclerViewPedidos = findViewById(R.id.recyclerListaAdministrador);
        searchViewUsuarios = findViewById(R.id.searchListaAdministrador);


        searchViewUsuarios.setBackgroundColor(Color.WHITE);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewPedidos.setLayoutManager(linearLayoutManager);
        listAdministrador = new ArrayList<>();
        adapterListaPorAdministrador = new AdapterListaPorAdministrador(listAdministrador);
        recyclerViewPedidos.setAdapter(adapterListaPorAdministrador);

        String [] users = {"Administrador"};
        for(final String datos:users){

            mDatabaseReference.child(datos).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        listAdministrador.clear();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            aukde.food.gestordepedidos.paquetes.Modelos.ListaAdministrador pd = snapshot.getValue(aukde.food.gestordepedidos.paquetes.Modelos.ListaAdministrador.class);
                            listAdministrador.add(pd);
                        }
                        Collections.reverse(listAdministrador);
                        adapterListaPorAdministrador.notifyDataSetChanged();
                        mDialogActualizeData.dismiss();
                    }
                    else {
                        Toast.makeText(ListaAdministrador.this, "Sin Usuarios", Toast.LENGTH_SHORT).show();
                        mDialogActualizeData.dismiss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ListaAdministrador.this, "Error de base de datos", Toast.LENGTH_SHORT).show();
                }
            });

        }


        searchViewUsuarios.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        ArrayList<aukde.food.gestordepedidos.paquetes.Modelos.ListaAdministrador> lista = new ArrayList<>();
        for(aukde.food.gestordepedidos.paquetes.Modelos.ListaAdministrador object : listAdministrador){
            if (object.getNombres().toLowerCase().contains(texto.toLowerCase()) || object.getDni().toLowerCase().contains(texto.toLowerCase())
                    || object.getTelefono().toLowerCase().contains(texto.toLowerCase()))
            {
                lista.add(object);
            }
            else {
            }
        }
        AdapterListaPorAdministrador adapter = new AdapterListaPorAdministrador(lista);
        recyclerViewPedidos.setAdapter(adapter);
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
        startActivity(new Intent(ListaAdministrador.this, MenuAdmin.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}