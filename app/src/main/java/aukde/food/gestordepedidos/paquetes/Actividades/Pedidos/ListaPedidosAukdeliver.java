package aukde.food.gestordepedidos.paquetes.Actividades.Pedidos;

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

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Adaptadores.AdapterPedidoPorLlamada;
import aukde.food.gestordepedidos.paquetes.Adaptadores.AdapterPedidoPorLlamadaAukdeliver;
import aukde.food.gestordepedidos.paquetes.Inclusiones.MiToolbar;
import aukde.food.gestordepedidos.paquetes.Modelos.PedidoLlamada;

public class ListaPedidosAukdeliver extends AppCompatActivity {

    DatabaseReference mDatabaseReference;
    ArrayList<PedidoLlamada> listPedidos;
    RecyclerView recyclerViewPedidos;
    SearchView searchViewPedidos;
    AdapterPedidoPorLlamadaAukdeliver adapterPedidoPorLlamadaAukdeliver;
    LinearLayoutManager linearLayoutManager;
    private ProgressDialog mDialogActualizeData;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme2);
        setContentView(R.layout.activity_lista_pedidos_aukdeliver);
        MiToolbar.Mostrar(this,"Lista de pedidos",true);
        mAuth = FirebaseAuth.getInstance();
        mDialogActualizeData = new ProgressDialog(this,R.style.MyAlertDialogData);
        mDialogActualizeData.setCancelable(false);
        mDialogActualizeData.show();
        mDialogActualizeData.setContentView(R.layout.dialog_data);
        mDialogActualizeData.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        String id = mAuth.getCurrentUser().getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Aukdeliver").child(id).child("pedidos");
        recyclerViewPedidos = findViewById(R.id.recyclerPedidosAukdeliver);
        searchViewPedidos = findViewById(R.id.searchPedidosAukdeliver);
        searchViewPedidos.setBackgroundColor(Color.WHITE);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewPedidos.setLayoutManager(linearLayoutManager);
        listPedidos = new ArrayList<>();
        adapterPedidoPorLlamadaAukdeliver = new AdapterPedidoPorLlamadaAukdeliver(listPedidos);
        recyclerViewPedidos.setAdapter(adapterPedidoPorLlamadaAukdeliver);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        PedidoLlamada pd = snapshot.getValue(PedidoLlamada.class);
                        listPedidos.add(pd);
                    }
                    adapterPedidoPorLlamadaAukdeliver.notifyDataSetChanged();
                    mDialogActualizeData.dismiss();
                }
                else {
                    Toast.makeText(ListaPedidosAukdeliver.this, "Sin Pedidos", Toast.LENGTH_SHORT).show();
                    mDialogActualizeData.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ListaPedidosAukdeliver.this, "Error de base de datos", Toast.LENGTH_SHORT).show();
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
        AdapterPedidoPorLlamadaAukdeliver adapter = new AdapterPedidoPorLlamadaAukdeliver(lista);
        recyclerViewPedidos.setAdapter(adapter);
    }

}