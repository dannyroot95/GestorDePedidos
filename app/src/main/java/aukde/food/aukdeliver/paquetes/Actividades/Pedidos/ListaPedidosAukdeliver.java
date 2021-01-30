package aukde.food.aukdeliver.paquetes.Actividades.Pedidos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
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
import aukde.food.aukdeliver.R;
import aukde.food.aukdeliver.paquetes.Adaptadores.AdapterPedidoPorLlamadaAukdeliver;
import aukde.food.aukdeliver.paquetes.Inclusiones.MiToolbar;
import aukde.food.aukdeliver.paquetes.Modelos.PedidoLlamada;
import aukde.food.aukdeliver.paquetes.Receptor.GpsReceiver;
import aukde.food.aukdeliver.paquetes.Receptor.NetworkReceiver;
import es.dmoral.toasty.Toasty;

public class ListaPedidosAukdeliver extends AppCompatActivity {

    DatabaseReference mDatabaseReference;
    ArrayList<PedidoLlamada> listPedidos;
    RecyclerView recyclerViewPedidos;
    SearchView searchViewPedidos;
    AdapterPedidoPorLlamadaAukdeliver adapterPedidoPorLlamadaAukdeliver;
    LinearLayoutManager linearLayoutManager;
    private ProgressDialog mDialogActualizeData;
    private FirebaseAuth mAuth;
    NetworkReceiver networkReceiver = new NetworkReceiver();
    GpsReceiver gpsReceiver = new GpsReceiver();

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
                    listPedidos.clear();
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        PedidoLlamada pd = snapshot.getValue(PedidoLlamada.class);
                        listPedidos.add(pd);
                    }

                    Collections.reverse(listPedidos);
                    adapterPedidoPorLlamadaAukdeliver.notifyDataSetChanged();
                    mDialogActualizeData.dismiss();
                }
                else {
                    Toasty.info(ListaPedidosAukdeliver.this, "Sin Pedidos", Toast.LENGTH_SHORT,true).show();
                    mDialogActualizeData.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(ListaPedidosAukdeliver.this, "Error de base de datos", Toast.LENGTH_SHORT,true).show();
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

    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkReceiver);
        unregisterReceiver(gpsReceiver);
    }


    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);
        registerReceiver(gpsReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
        super.onStart();
    }

    @Override
    public void onBackPressed(){
        NavUtils.navigateUpFromSameTask(this);
    }

}