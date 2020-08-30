package aukde.food.gestordepedidos.paquetes.Actividades.Pedidos;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.NavUtils;
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
import aukde.food.gestordepedidos.paquetes.Adaptadores.AdapterPedidoPorLlamada;
import aukde.food.gestordepedidos.paquetes.Inclusiones.MiToolbar;
import aukde.food.gestordepedidos.paquetes.Menus.MenuAdmin;
import aukde.food.gestordepedidos.paquetes.Modelos.PedidoLlamada;

public class ListaDePedidos extends AppCompatActivity {

    DatabaseReference mDatabaseReference;
    ArrayList<PedidoLlamada> listPedidos;
    RecyclerView recyclerViewPedidos;
    SearchView searchViewPedidos;
    AdapterPedidoPorLlamada adapterPedidoPorLlamada;
    LinearLayoutManager linearLayoutManager;
    private ProgressDialog mDialogActualizeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_pedidos);
        MiToolbar.Mostrar(this,"Lista de pedidos",true);
        mDialogActualizeData = new ProgressDialog(this,R.style.MyAlertDialogData);
        mDialogActualizeData.setCancelable(false);
        mDialogActualizeData.show();
        mDialogActualizeData.setContentView(R.layout.dialog_data);
        mDialogActualizeData.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        //String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("PedidosPorLlamada").child("pedidos");
        recyclerViewPedidos = findViewById(R.id.recyclerPedidos);
        searchViewPedidos = findViewById(R.id.searchPedidos);
        searchViewPedidos.setBackgroundColor(Color.WHITE);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewPedidos.setLayoutManager(linearLayoutManager);
        listPedidos = new ArrayList<>();
        adapterPedidoPorLlamada = new AdapterPedidoPorLlamada(listPedidos);
        recyclerViewPedidos.setAdapter(adapterPedidoPorLlamada);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    listPedidos.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        PedidoLlamada pd = snapshot.getValue(PedidoLlamada.class);
                        listPedidos.add(pd);
                    }
                    Collections.reverse(listPedidos);
                    adapterPedidoPorLlamada.notifyDataSetChanged();
                    mDialogActualizeData.dismiss();
                }
                else {
                    Toast.makeText(ListaDePedidos.this, "Sin Pedidos", Toast.LENGTH_SHORT).show();
                    mDialogActualizeData.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ListaDePedidos.this, "Error de base de datos", Toast.LENGTH_SHORT).show();
            }
        });

        searchViewPedidos.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        ArrayList<PedidoLlamada> lista = new ArrayList<>();
        for(PedidoLlamada object : listPedidos){
            if (object.getNombreCliente().toLowerCase().contains(texto.toLowerCase()) || object.getNumPedido().toLowerCase().contains(texto.toLowerCase())
                    || object.getDireccion().toLowerCase().contains(texto.toLowerCase()))
            {
                lista.add(object);
            }
            else {
            }
        }
        AdapterPedidoPorLlamada adapter = new AdapterPedidoPorLlamada(lista);
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
        NavUtils.navigateUpFromSameTask(this);/*
        startActivity(new Intent(ListaDePedidos.this, MenuAdmin.class));
        finish(); */
    }
}