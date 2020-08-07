package aukde.food.gestordepedidos.paquetes.Actividades.Pedidos;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
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

public class DetallePedido extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    TextView listNumPedido,listNumPedido2,listNombreCliente , listTelefonoCliente ,listHoraRegistro , listFechaRegistro
    , listHoraEntrega , listFechaEntrega , listTotalPagoProducto , listDireccion , listPagoCliente
            , listTotalACobrar , listVuelto , listRepartidor , listProveedores , listProducto , listDescripcion

            , listPrecioUnitario , listCantidad ,listPrecioTotalPorProducto , listComision , listTotalDelivery ,
            listGananciaDelivery , listGananciaComision , listEstado ,listLatitud , listLongitud;

    Button mButtonShow ;
    Button mButtonShow2;
    Button mMapa;

    TextView mGanasteComision , mGanasteDelivery , txtDetallePTotal , detalleGanancia1 , detalleGanancia2;

    private LinearLayout mLinearProductos ;

    private DatabaseReference mDatabase ;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeDark);
        setContentView(R.layout.activity_detalle_pedido);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        listHoraRegistro = findViewById(R.id.detalleHoraRegistro);
        listFechaRegistro = findViewById(R.id.detalleFechaRegistro);
        listHoraEntrega = findViewById(R.id.detalleHoraEntrega);
        listFechaEntrega = findViewById(R.id.detalleFechaEntrega);
        listTotalPagoProducto = findViewById(R.id.detalleTotalPago);
        listNumPedido = findViewById(R.id.detalleNumPedido);
        listNumPedido2 = findViewById(R.id.detalleNumPedido2);
        listNombreCliente = findViewById(R.id.detalleNombreCliente);
        listTelefonoCliente=findViewById(R.id.detalleTelefonoCliente);
        listDireccion = findViewById(R.id.detalleDireccionCliente);
        listPagoCliente = findViewById(R.id.detallePagoCliente);
        listTotalACobrar = findViewById(R.id.detalleTotalCobro);
        listVuelto = findViewById(R.id.detalleVuelto);
        listRepartidor = findViewById(R.id.detallevRepartidor);
        listProveedores = findViewById(R.id.detalleSocio);
        listProducto = findViewById(R.id.detalleProductos);
        listDescripcion = findViewById(R.id.detalleProductosDescripcion);
        //
        listPrecioUnitario = findViewById(R.id.detallePrecioUnitario);
        listCantidad = findViewById(R.id.detalleCantidad);
        listPrecioTotalPorProducto = findViewById(R.id.detallePreciosTotalesDelproducto);
        listComision = findViewById(R.id.detalleComision);
        listTotalDelivery = findViewById(R.id.detalleTotalProductoDelivery);
        listGananciaDelivery = findViewById(R.id.detalleGananciaDelivery);
        listGananciaComision = findViewById(R.id.detalleGananciaComision);
        //
        listEstado = findViewById(R.id.detalleEstado);
        listLatitud = findViewById(R.id.detalleLatitud);
        listLongitud = findViewById(R.id.detalleLongitudd);

        mGanasteComision = findViewById(R.id.txtGanaste);
        mGanasteDelivery = findViewById(R.id.txtGanasteDelivery);
        txtDetallePTotal = findViewById(R.id.DetallePTotal);

        detalleGanancia1 = findViewById(R.id.detalleGananciaDeliveryAukde);
        detalleGanancia2 = findViewById(R.id.detalleGananciaDeliveryAukdeliver);

        mButtonShow = findViewById(R.id.showProducto);
        mButtonShow2 = findViewById(R.id.showDetalle);
        mMapa = findViewById(R.id.showMapa);

        int alto = 0;
        mLinearProductos = findViewById(R.id.linearProductos);
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,alto);
        mLinearProductos.setLayoutParams(params);

        mButtonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                mLinearProductos.setLayoutParams(params);
            }
        });

        mButtonShow2.setOnClickListener(new View.OnClickListener() {
            int alto1 = 0;
            @Override
            public void onClick(View v) {
                    CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,alto1);
                    mLinearProductos.setLayoutParams(params);
            }
        });

        mMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetallePedido.this, MapaClientePorLlamada.class);
                intent.putExtra("latitud",listLatitud.getText().toString());
                intent.putExtra("longitud",listLongitud.getText().toString());
                intent.putExtra("nombre",listNombreCliente.getText().toString());
                startActivity(intent);
            }
        });

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        PedidoLlamada pedidoLlamada = (PedidoLlamada)bundle.getSerializable("key");
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(pedidoLlamada.getNumPedido());
        arrayList.add(pedidoLlamada.getHoraPedido());
        arrayList.add(pedidoLlamada.getFechaPedido());
        arrayList.add(pedidoLlamada.getHoraEntrega());
        arrayList.add(pedidoLlamada.getFechaEntrega());
        arrayList.add(pedidoLlamada.getTotalPagoProducto());
        arrayList.add(pedidoLlamada.getNombreCliente());
        arrayList.add(pedidoLlamada.getTelefono());
        arrayList.add("S/"+pedidoLlamada.getConCuantoVaAPagar());
        arrayList.add("S/"+pedidoLlamada.getTotalCobro());
        arrayList.add(pedidoLlamada.getVuelto());
        arrayList.add(pedidoLlamada.getEncargado());
        arrayList.add(pedidoLlamada.getDireccion());

        arrayList.add(pedidoLlamada.getProveedores());
        arrayList.add(pedidoLlamada.getProductos());
        arrayList.add(pedidoLlamada.getDescripcion());
        //
        arrayList.add(pedidoLlamada.getPrecioUnitario());
        arrayList.add(pedidoLlamada.getCantidad());
        arrayList.add(pedidoLlamada.getPrecioTotalXProducto());
        arrayList.add(pedidoLlamada.getComision());
        arrayList.add(pedidoLlamada.getTotalDelivery());
        arrayList.add(pedidoLlamada.getGananciaDelivery());
        arrayList.add(pedidoLlamada.getGananciaComision());
        //
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
        txtDetallePTotal.setText(arrayList.get(5));
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
        listPrecioUnitario.setText(arrayList.get(16));
        listCantidad.setText(arrayList.get(17));
        listPrecioTotalPorProducto.setText(arrayList.get(18));
        listComision.setText(arrayList.get(19));
        listTotalDelivery.setText(arrayList.get(20));
        listGananciaDelivery.setText(arrayList.get(21));
        listGananciaComision.setText(arrayList.get(22));
        listEstado.setText(arrayList.get(23));
        listLatitud.setText(arrayList.get(24));
        listLongitud.setText(arrayList.get(25));


        String stEstado = listEstado.getText().toString();

        String Delivery = listGananciaDelivery.getText().toString();
        Double doubleDelivery = Double.parseDouble(Delivery);
        Double finalGananciaDeliveryAukdeliver;
        Double finalGananciaDeliveryAukde;

        if(doubleDelivery < 4.00){
            finalGananciaDeliveryAukdeliver = doubleDelivery * 0.5;
            String ganancia50P = String.valueOf(finalGananciaDeliveryAukdeliver);
            detalleGanancia1.setText(ganancia50P);
            detalleGanancia2.setText(ganancia50P);
        }
        if(doubleDelivery >= 4.00 && doubleDelivery < 9.00){
            finalGananciaDeliveryAukdeliver = doubleDelivery * 0.4;
            finalGananciaDeliveryAukde = doubleDelivery * 0.6;
            String ganancia40P = String.valueOf(finalGananciaDeliveryAukdeliver);
            String ganancia60P = String.valueOf(finalGananciaDeliveryAukde);
            detalleGanancia1.setText(ganancia60P);
            detalleGanancia2.setText(ganancia40P);
        }
        if(doubleDelivery >= 9.00){
            finalGananciaDeliveryAukdeliver = doubleDelivery * 0.3;
            finalGananciaDeliveryAukde = doubleDelivery * 0.7;
            String ganancia30P = String.valueOf(finalGananciaDeliveryAukdeliver);
            String ganancia70P = String.valueOf(finalGananciaDeliveryAukde);
            detalleGanancia1.setText(ganancia70P);
            detalleGanancia2.setText(ganancia30P);
        }

        if (stEstado.equals("Completado")){
            listEstado.setTextColor(Color.parseColor("#5bbd00"));
            mGanasteComision.setText("Por este pedido ganaste   : ");
            mGanasteDelivery.setText("Por este delivery ganaste : ");
        }
        if (stEstado.equals("Cancelado")){
            listEstado.setTextColor(Color.parseColor("#E74C3C"));
        }
        if (stEstado.equals("En espera")){
            listEstado.setTextColor(Color.parseColor("#2E86C1"));
        }

}

    public void showPopupEstado(View view){

        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu_estado);
        popupMenu.show();

    }

    public void onClickLlamada(View v) {
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+listTelefonoCliente.getText().toString()));
        startActivity(i);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item){
        switch (item.getItemId()){
            case R.id.item1:
                estadoCompletadoRepartidor();
                estadoCompletado();
                finish();
                return true;

            case R.id.item2:
                estadoCanceladoRepartidor();
                estadoCancelado();
                finish();
                return true;

            case R.id.item3:
                estadoEsperaRepartidor();
                estadoEspera();
                finish();
                return true;

            default:
                return false;
        }
    }

    private void estadoCompletado(){
        String dataNumPedido = listNumPedido.getText().toString();
        Query reference= FirebaseDatabase.getInstance().getReference().child("PedidosPorLlamada").child("pedidos").orderByChild("numPedido").equalTo(dataNumPedido);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String key=childSnapshot.getKey();
                   // Toast.makeText(DetallePedido.this, "Id : "+key, Toast.LENGTH_SHORT).show();
                    Map<String , Object> map = new HashMap<>();
                    map.put("estado","Completado");
                    mDatabase.child("PedidosPorLlamada").child("pedidos").child(key).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toasty.success(DetallePedido.this, "PEDIDO COMPLETADO!", Toast.LENGTH_LONG, true).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.info(DetallePedido.this, "Error al actualizar estado", Toast.LENGTH_LONG, true).show();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void estadoCompletadoRepartidor(){
        String dataNombres = listRepartidor.getText().toString();
        final String dataNumPedido = listNumPedido.getText().toString();
        Query reference= FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Aukdeliver").orderByChild("nombres").equalTo(dataNombres);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                   final String key=childSnapshot.getKey();
                    Query reference2= FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Aukdeliver").child(key).child("pedidos").orderByChild("numPedido").equalTo(dataNumPedido);
                    reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot childSnapshot2: dataSnapshot.getChildren()) {
                                String key2=childSnapshot2.getKey();
                                Map<String , Object> map = new HashMap<>();
                                map.put("estado","Completado");
                                mDatabase.child("Usuarios").child("Aukdeliver").child(key).child("pedidos").child(key2).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void estadoCancelado(){
        String dataNumPedido = listNumPedido.getText().toString();
        Query reference= FirebaseDatabase.getInstance().getReference().child("PedidosPorLlamada").child("pedidos").orderByChild("numPedido").equalTo(dataNumPedido);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String key=childSnapshot.getKey();
                    //Toast.makeText(DetallePedido.this, "Id : "+key, Toast.LENGTH_SHORT).show();
                    Map<String , Object> map = new HashMap<>();
                    map.put("estado","Cancelado");
                    mDatabase.child("PedidosPorLlamada").child("pedidos").child(key).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toasty.error(DetallePedido.this, "PEDIDO CANCELADO!", Toast.LENGTH_LONG, true).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.info(DetallePedido.this, "Error al actualizar estado", Toast.LENGTH_LONG, true).show();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void estadoCanceladoRepartidor(){
        String dataNombres = listRepartidor.getText().toString();
        final String dataNumPedido = listNumPedido.getText().toString();
        Query reference= FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Aukdeliver").orderByChild("nombres").equalTo(dataNombres);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    final String key=childSnapshot.getKey();
                    Query reference2= FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Aukdeliver").child(key).child("pedidos").orderByChild("numPedido").equalTo(dataNumPedido);
                    reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot childSnapshot2: dataSnapshot.getChildren()) {
                                String key2=childSnapshot2.getKey();
                                Map<String , Object> map = new HashMap<>();
                                map.put("estado","Cancelado");
                                mDatabase.child("Usuarios").child("Aukdeliver").child(key).child("pedidos").child(key2).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void estadoEspera(){
        String dataNumPedido = listNumPedido.getText().toString();
        Query reference= FirebaseDatabase.getInstance().getReference().child("PedidosPorLlamada").child("pedidos").orderByChild("numPedido").equalTo(dataNumPedido);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String key=childSnapshot.getKey();
                    //Toast.makeText(DetallePedido.this, "Id : "+key, Toast.LENGTH_SHORT).show();
                    Map<String , Object> map = new HashMap<>();
                    map.put("estado","En espera");
                    mDatabase.child("PedidosPorLlamada").child("pedidos").child(key).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toasty.info(DetallePedido.this, "PEDIDO EN ESPERA!", Toast.LENGTH_LONG, true).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.info(DetallePedido.this, "Error al actualizar estado", Toast.LENGTH_LONG, true).show();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void estadoEsperaRepartidor(){
        String dataNombres = listRepartidor.getText().toString();
        final String dataNumPedido = listNumPedido.getText().toString();
        Query reference= FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Aukdeliver").orderByChild("nombres").equalTo(dataNombres);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    final String key=childSnapshot.getKey();
                    Query reference2= FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Aukdeliver").child(key).child("pedidos").orderByChild("numPedido").equalTo(dataNumPedido);
                    reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot childSnapshot2: dataSnapshot.getChildren()) {
                                String key2=childSnapshot2.getKey();
                                Map<String , Object> map = new HashMap<>();
                                map.put("estado","En espera");
                                mDatabase.child("Usuarios").child("Aukdeliver").child(key).child("pedidos").child(key2).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetallePedido.this);
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
