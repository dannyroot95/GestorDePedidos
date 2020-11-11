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
import aukde.food.gestordepedidos.paquetes.Adaptadores.AdapterListaSolicitud;
import aukde.food.gestordepedidos.paquetes.Adaptadores.AdapterPedidoPorLlamada;
import aukde.food.gestordepedidos.paquetes.Inclusiones.MiToolbar;
import aukde.food.gestordepedidos.paquetes.Menus.MenuAdmin;
import aukde.food.gestordepedidos.paquetes.Modelos.ListaSolicitud;
import aukde.food.gestordepedidos.paquetes.Modelos.PedidoLlamada;
import es.dmoral.toasty.Toasty;

public class ListaSolicitudProveedor extends AppCompatActivity {

    DatabaseReference mDatabaseReference;
    ArrayList<ListaSolicitud> listSolicitud;
    RecyclerView recyclerViewSolicitud;
    SearchView searchViewSolicitud;
    AdapterListaSolicitud adapterSolicitud;
    LinearLayoutManager linearLayoutManager;
    private ProgressDialog mDialogActualizeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_solicitud);
        MiToolbar.Mostrar(this,"Lista de solicitudes",true);
        mDialogActualizeData = new ProgressDialog(this,R.style.MyAlertDialogData);
        mDialogActualizeData.setCancelable(false);
        mDialogActualizeData.show();
        mDialogActualizeData.setContentView(R.layout.dialog_data);
        mDialogActualizeData.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("SolicitudDelivery");
        recyclerViewSolicitud = findViewById(R.id.recyclerSolicitud);
        searchViewSolicitud = findViewById(R.id.searchSolicitud);
        searchViewSolicitud.setBackgroundColor(Color.WHITE);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewSolicitud.setLayoutManager(linearLayoutManager);
        listSolicitud = new ArrayList<>();
        adapterSolicitud = new AdapterListaSolicitud(listSolicitud);
        recyclerViewSolicitud.setAdapter(adapterSolicitud);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    listSolicitud.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        ListaSolicitud pd = snapshot.getValue(ListaSolicitud.class);
                        listSolicitud.add(pd);
                    }
                    Collections.reverse(listSolicitud);
                    adapterSolicitud.notifyDataSetChanged();
                    mDialogActualizeData.dismiss();
                }
                else {
                    Toasty.info(ListaSolicitudProveedor.this, "Sin Solicitudes", Toast.LENGTH_SHORT,true).show();
                    mDialogActualizeData.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(ListaSolicitudProveedor.this, "Error de base de datos", Toast.LENGTH_SHORT,true).show();
            }
        });

        searchViewSolicitud.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        ArrayList<ListaSolicitud> lista = new ArrayList<>();
        for(ListaSolicitud object : listSolicitud){
            if (object.getNombreCliente().toLowerCase().contains(texto.toLowerCase()))
            {
                lista.add(object);
            }
            else {
            }
        }
        AdapterListaSolicitud adapter = new AdapterListaSolicitud(lista);
        recyclerViewSolicitud.setAdapter(adapter);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(ListaSolicitudProveedor.this, MenuAdmin.class));
        finish();
    }
}
