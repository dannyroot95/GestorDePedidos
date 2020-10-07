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
import aukde.food.gestordepedidos.paquetes.Adaptadores.AdapterListaPorAukdelivery;
import aukde.food.gestordepedidos.paquetes.Inclusiones.MiToolbar;
import aukde.food.gestordepedidos.paquetes.Menus.MenuAdmin;
import aukde.food.gestordepedidos.paquetes.Modelos.ListaAukdelivery;

public class ListaDeAukdeliver extends AppCompatActivity {

    private ProgressDialog mDialogActualizeData;
    DatabaseReference mDatabaseReference;
    RecyclerView recyclerViewAukdeDelivery;
    SearchView searchViewUsuariosAukdeDelivery;
    LinearLayoutManager linearLayoutManager;
    AdapterListaPorAukdelivery adapterListaPorAukdelivery;

    ArrayList<ListaAukdelivery> listUsuariosAukdeDelivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeDark);
        setContentView(R.layout.activity_lista_de_aukdelivery);
        MiToolbar.Mostrar(this,"Lista de AukDelivery",false);
        mDialogActualizeData = new ProgressDialog(this,R.style.MyAlertDialogData);
        mDialogActualizeData.setCancelable(false);
        mDialogActualizeData.show();
        mDialogActualizeData.setContentView(R.layout.dialog_data);
        mDialogActualizeData.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        //mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Administrador");

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios");

        recyclerViewAukdeDelivery = findViewById(R.id.recyclerListaAukdeDelivery);
        searchViewUsuariosAukdeDelivery = findViewById(R.id.searchAukdeDelivery);

        searchViewUsuariosAukdeDelivery.setBackgroundColor(Color.WHITE);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewAukdeDelivery.setLayoutManager(linearLayoutManager);
        listUsuariosAukdeDelivery = new ArrayList<>();
        adapterListaPorAukdelivery = new AdapterListaPorAukdelivery(listUsuariosAukdeDelivery);
        recyclerViewAukdeDelivery.setAdapter(adapterListaPorAukdelivery);

        String [] usersEmpleado = {"Aukdeliver"};
        for(final String datos:usersEmpleado){

            mDatabaseReference.child(datos).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        listUsuariosAukdeDelivery.clear();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            ListaAukdelivery pd = snapshot.getValue(ListaAukdelivery.class);
                            listUsuariosAukdeDelivery.add(pd);
                        }
                        Collections.reverse(listUsuariosAukdeDelivery);
                        adapterListaPorAukdelivery.notifyDataSetChanged();
                        mDialogActualizeData.dismiss();
                    }
                    else {
                        Toast.makeText(ListaDeAukdeliver.this, "Sin Usuarios", Toast.LENGTH_SHORT).show();
                        mDialogActualizeData.dismiss();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ListaDeAukdeliver.this, "Error de base de datos", Toast.LENGTH_SHORT).show();
                }
            });

        }


        searchViewUsuariosAukdeDelivery.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        ArrayList<ListaAukdelivery> lista = new ArrayList<>();
        for(ListaAukdelivery object : listUsuariosAukdeDelivery){
            if (object.getNombres().toLowerCase().contains(texto.toLowerCase()) || object.getDni().toLowerCase().contains(texto.toLowerCase())
                    || object.getTelefono().toLowerCase().contains(texto.toLowerCase()))
            {
                lista.add(object);
            }
            else {
            }
        }
        AdapterListaPorAukdelivery adapter = new AdapterListaPorAukdelivery(lista);
        recyclerViewAukdeDelivery.setAdapter(adapter);

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
        startActivity(new Intent(ListaDeAukdeliver.this, MenuAdmin.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}