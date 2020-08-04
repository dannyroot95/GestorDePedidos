package aukde.food.gestordepedidos.paquetes.Actividades.Pedidos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Mapas.MapaClientePorLlamada;
import aukde.food.gestordepedidos.paquetes.Modelos.PedidoLlamada;
import es.dmoral.toasty.Toasty;

public class DetallePedidoAukdeliver extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    TextView listNumPedido, listNumPedido2, listNombreCliente, listTelefonoCliente, listHoraRegistro,
            listFechaRegistro, listHoraEntrega, listFechaEntrega, listTotalPagoProducto, listDireccion,
            listPagoCliente, listTotalACobrar, listVuelto, listRepartidor, listProveedores, listProducto,
            listDescripcion, listPrecio1, listPrecio2, listPrecio3, listDelivery1,
            listDelivery2, listDelivery3, listEstado  ,listLatitud , listLongitud;

    Button mButtonShow;
    Button mButtonShow2;
    Button bottonEstado, btnError;
    Button mMapa;

    private DatabaseReference mDatabase;
    private LinearLayout mLinearProductos, mLinearProductos1, mLinearProductos2,
            mLinearProductos3 , mLinearCliente , mLinearTelefono , mLinearDireccion;
    private FirebaseAuth mAuth;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeDark);
        setContentView(R.layout.activity_detalle_pedido_aukdeliver);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        listHoraRegistro = findViewById(R.id.detalleHoraRegistro);
        listFechaRegistro = findViewById(R.id.detalleFechaRegistro);
        listHoraEntrega = findViewById(R.id.detalleHoraEntrega);
        listFechaEntrega = findViewById(R.id.detalleFechaEntrega);
        listTotalPagoProducto = findViewById(R.id.detalleTotalPago);
        listNumPedido = findViewById(R.id.detalleNumPedido);
        listNumPedido2 = findViewById(R.id.detalleNumPedido2);
        listNombreCliente = findViewById(R.id.detalleNombreCliente);
        listTelefonoCliente = findViewById(R.id.detalleTelefonoCliente);
        listDireccion = findViewById(R.id.detalleDireccionCliente);
        listPagoCliente = findViewById(R.id.detallePagoCliente);
        listTotalACobrar = findViewById(R.id.detalleTotalCobro);
        listVuelto = findViewById(R.id.detalleVuelto);
        listRepartidor = findViewById(R.id.detallevRepartidor);
        listProveedores = findViewById(R.id.detalleProveedor);
        listProducto = findViewById(R.id.detalleProductos);
        listDescripcion = findViewById(R.id.detalleProductosDescripcion);
        listPrecio1 = findViewById(R.id.detalleProductoprecio1);
        listPrecio2 = findViewById(R.id.detalleProductoprecio2);
        listPrecio3 = findViewById(R.id.detalleProductoprecio3);
        listDelivery1 = findViewById(R.id.detalleDelivery1);
        listDelivery2 = findViewById(R.id.detalleDelivery2);
        listDelivery3 = findViewById(R.id.detalleDelivery3);
        listEstado = findViewById(R.id.detalleEstado);
        listLatitud = findViewById(R.id.detalleLatitud);
        listLongitud = findViewById(R.id.detalleLongitudd);


        mButtonShow = findViewById(R.id.showProducto);
        mButtonShow2 = findViewById(R.id.showDetalle);
        bottonEstado = findViewById(R.id.btnEstado);
        btnError = findViewById(R.id.btnReporte);
        mMapa = findViewById(R.id.showMapa);

        int alto = 0;
        mLinearProductos = findViewById(R.id.linearProductos);
        mLinearProductos1 = findViewById(R.id.idLinearProducto1);
        mLinearProductos2 = findViewById(R.id.idLinearProducto2);
        mLinearProductos3 = findViewById(R.id.idLinearProducto3);
        mLinearCliente = findViewById(R.id.linearCliente);
        mLinearTelefono = findViewById(R.id.linearTelefono);
        mLinearDireccion = findViewById(R.id.linearDireccion);
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, alto);
        mLinearProductos.setLayoutParams(params);

        mButtonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mLinearProductos.setLayoutParams(params);
            }
        });

        mButtonShow2.setOnClickListener(new View.OnClickListener() {
            int alto1 = 0;

            @Override
            public void onClick(View v) {
                CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, alto1);
                mLinearProductos.setLayoutParams(params);
            }
        });

        mMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetallePedidoAukdeliver.this, MapaClientePorLlamada.class);
                intent.putExtra("latitud",listLatitud.getText().toString());
                intent.putExtra("longitud",listLongitud.getText().toString());
                intent.putExtra("nombre",listNombreCliente.getText().toString());
                startActivity(intent);
            }
        });

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        PedidoLlamada pedidoLlamada = (PedidoLlamada) bundle.getSerializable("key");
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(pedidoLlamada.getNumPedido());
        arrayList.add(pedidoLlamada.getHoraPedido());
        arrayList.add(pedidoLlamada.getFechaPedido());
        arrayList.add(pedidoLlamada.getHoraEntrega());
        arrayList.add(pedidoLlamada.getFechaEntrega());
        arrayList.add(pedidoLlamada.getTotalPagoProducto());
        arrayList.add(pedidoLlamada.getNombreCliente());
        arrayList.add(pedidoLlamada.getTelefono());
        arrayList.add("S/" + pedidoLlamada.getConCuantoVaAPagar());
        arrayList.add("S/" + pedidoLlamada.getTotalCobro());
        arrayList.add(pedidoLlamada.getVuelto());
        arrayList.add(pedidoLlamada.getEncargado());
        arrayList.add(pedidoLlamada.getDireccion());
        arrayList.add(pedidoLlamada.getProveedores());
        arrayList.add(pedidoLlamada.getProductos());
        arrayList.add(pedidoLlamada.getDescripcion());
        arrayList.add(pedidoLlamada.getPrecio1());
        arrayList.add(pedidoLlamada.getPrecio2());
        arrayList.add(pedidoLlamada.getPrecio3());
        arrayList.add(pedidoLlamada.getDelivery1());
        arrayList.add(pedidoLlamada.getDelivery2());
        arrayList.add(pedidoLlamada.getDelivery3());
        arrayList.add(pedidoLlamada.getEstado());
        arrayList.add(pedidoLlamada.getLatitud());
        arrayList.add(pedidoLlamada.getLongitud());


        listNumPedido.setText(arrayList.get(0));
        listNumPedido2.setText(arrayList.get(0));
        listHoraRegistro.setText(arrayList.get(1));
        listFechaRegistro.setText(arrayList.get(2));
        listHoraEntrega.setText(arrayList.get(3));
        listFechaEntrega.setText(arrayList.get(4));
        listTotalPagoProducto.setText(arrayList.get(5));
        listNombreCliente.setText(arrayList.get(6));
        listTelefonoCliente.setText(arrayList.get(7));
        listPagoCliente.setText(arrayList.get(8));
        listTotalACobrar.setText(arrayList.get(9));
        listVuelto.setText(arrayList.get(10));
        listRepartidor.setText(arrayList.get(11));
        listDireccion.setText(arrayList.get(12));
        listProveedores.setText(arrayList.get(13));
        listProducto.setText(arrayList.get(14));
        listDescripcion.setText(arrayList.get(15));
        listPrecio1.setText(arrayList.get(16));
        listPrecio2.setText(arrayList.get(17));
        listPrecio3.setText(arrayList.get(18));
        listDelivery1.setText(arrayList.get(19));
        listDelivery2.setText(arrayList.get(20));
        listDelivery3.setText(arrayList.get(21));
        listEstado.setText(arrayList.get(22));
        listLatitud.setText(arrayList.get(23));
        listLongitud.setText(arrayList.get(24));


        String stPrecio1 = listPrecio1.getText().toString();
        String stPrecio2 = listPrecio2.getText().toString();
        String stPrecio3 = listPrecio3.getText().toString();
        String stDelivery1 = listDelivery1.getText().toString();
        String stDelivery2 = listDelivery2.getText().toString();
        String stDelivery3 = listDelivery3.getText().toString();

        String stEstado = listEstado.getText().toString();

        if (stPrecio1.equals("0") && stDelivery1.equals("0")) {
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, alto);
            mLinearProductos1.setLayoutParams(params1);
        }

        if (stPrecio2.equals("0") && stDelivery2.equals("0")) {
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, alto);
            mLinearProductos2.setLayoutParams(params2);
        }

        if (stPrecio3.equals("0") && stDelivery3.equals("0")) {
            LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, alto);
            mLinearProductos3.setLayoutParams(params3);
        }

        if (stEstado.equals("Completado")) {
            listEstado.setTextColor(Color.parseColor("#000000"));
            bottonEstado.setVisibility(View.INVISIBLE);
            btnError.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, alto);
            mLinearCliente.setLayoutParams(params4);
            mLinearTelefono.setLayoutParams(params4);
            mLinearDireccion.setLayoutParams(params4);
            mMapa.setVisibility(View.INVISIBLE);
        }
        if (stEstado.equals("Cancelado")) {
            listEstado.setTextColor(Color.parseColor("#E74C3C"));
            bottonEstado.setVisibility(View.INVISIBLE);
            btnError.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params5 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, alto);
            mLinearCliente.setLayoutParams(params5);
            mLinearTelefono.setLayoutParams(params5);
            mLinearDireccion.setLayoutParams(params5);
            mMapa.setVisibility(View.INVISIBLE);
        }
        if (stEstado.equals("En espera")) {
            listEstado.setTextColor(Color.parseColor("#232C9B"));
            btnError.setVisibility(View.INVISIBLE);
        }


    }

    public void showPopupEstado(View view) {

        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu_estado_aukdeliver);
        popupMenu.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                estadoCompletadoAdmin();
                estadoCompletadoAukdeliver();
                finish();
                return true;

            case R.id.item2:
                estadoCanceladoAdmin();
                estadoCanceladoAukdeliver();
                finish();
                return true;

            default:
                return false;
        }
    }

    // Crear un query para su usuario Con firebase Auth
    // ocultar el boton al decir si el pedido esta completo

    private void estadoCompletadoAdmin() {
        String dataNumPedido = listNumPedido.getText().toString();
        Query reference = FirebaseDatabase.getInstance().getReference().child("PedidosPorLlamada").child("pedidos").orderByChild("numPedido").equalTo(dataNumPedido);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    Map<String, Object> map = new HashMap<>();
                    map.put("estado", "Completado");
                    mDatabase.child("PedidosPorLlamada").child("pedidos").child(key).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void estadoCompletadoAukdeliver() {
        String dataNumPedido = listNumPedido.getText().toString();
        Query reference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Aukdeliver").child(mAuth.getUid()).child("pedidos").orderByChild("numPedido").equalTo(dataNumPedido);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    Map<String, Object> map = new HashMap<>();
                    map.put("estado", "Completado");
                    mDatabase.child("Usuarios").child("Aukdeliver").child(mAuth.getUid()).child("pedidos").child(key).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toasty.success(DetallePedidoAukdeliver.this, "PEDIDO COMPLETADO!", Toast.LENGTH_LONG, true).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.info(DetallePedidoAukdeliver.this, "Error al actualizar estado", Toast.LENGTH_LONG, true).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void estadoCanceladoAdmin() {
        String dataNumPedido = listNumPedido.getText().toString();
        Query reference = FirebaseDatabase.getInstance().getReference().child("PedidosPorLlamada").child("pedidos").orderByChild("numPedido").equalTo(dataNumPedido);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    //Toast.makeText(DetallePedido.this, "Id : "+key, Toast.LENGTH_SHORT).show();
                    Map<String, Object> map = new HashMap<>();
                    map.put("estado", "Cancelado");
                    mDatabase.child("PedidosPorLlamada").child("pedidos").child(key).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void estadoCanceladoAukdeliver() {
        String dataNumPedido = listNumPedido.getText().toString();
        Query reference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Aukdeliver").child(mAuth.getUid()).child("pedidos").orderByChild("numPedido").equalTo(dataNumPedido);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    Map<String, Object> map = new HashMap<>();
                    map.put("estado", "Cancelado");
                    mDatabase.child("Usuarios").child("Aukdeliver").child(mAuth.getUid()).child("pedidos").child(key).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toasty.error(DetallePedidoAukdeliver.this, "PEDIDO CANCELADO!", Toast.LENGTH_LONG, true).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.info(DetallePedidoAukdeliver.this, "Error al actualizar estado", Toast.LENGTH_LONG, true).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onClickLlamada(View v) {
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:992997223"));
        startActivity(i);
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetallePedidoAukdeliver.this);
        builder.setTitle("Confirmar");
        builder.setCancelable(false);
        builder.setMessage("Deseas volver a la lista de pedidos? ");
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create();
        builder.show();
    }


}