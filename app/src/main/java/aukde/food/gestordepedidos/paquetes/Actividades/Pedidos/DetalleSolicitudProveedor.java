package aukde.food.gestordepedidos.paquetes.Actividades.Pedidos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Modelos.ListaSolicitud;
import aukde.food.gestordepedidos.paquetes.Providers.NotificationProvider;
import aukde.food.gestordepedidos.paquetes.Providers.TokenProvider;
import es.dmoral.toasty.Toasty;

public class DetalleSolicitudProveedor extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    String ruta = "https://firebasestorage.googleapis.com/v0/b/gestor-de-pedidos-aukdefood.appspot.com/o/delete.png?alt=media&token=8eb49918-1713-4e1e-aaf8-38573da32206";
    TextView listTelefonoCliente,listEstadoSolicitud,listProveedor,listNumPedido;

    EditText listdetalleTotalNetoProducto,listdetalleSolicitudProveedorNombre,listdetalleSoliciTelefonoCliente;
    EditText listdetalleSolicitudReferCliente,listdetalleSolicitudCantidadProduc,listdetalleSolicitudCostoDelivery,listdetalleSolicitudCostoTotalProd;
    EditText listdetalleSolicitudPrecioU,listdetalleSolicitudListadeProductos;
    private Vibrator vibrator;
    long tiempo = 100;
    private DatabaseReference mDatabaseSolicitudDeliver ;
    private TokenProvider tokenProvider;
    private NotificationProvider notificationProvider;
    private FloatingActionButton mFloat;
    LinearLayout mLinearEditarSolicitud;
    ImageView clrPhotoDetalleSolicitud;
    ArrayList<String> arrayList;
    private ProgressDialog mDialog;
    Button btnEditar_solicitud;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_solicitud_proveedor);
        listTelefonoCliente=findViewById(R.id.detalleSoliciTelefonoCliente);
        mDatabaseSolicitudDeliver = FirebaseDatabase.getInstance().getReference();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mDialog = new ProgressDialog(this,R.style.ThemeOverlay);
        btnEditar_solicitud=findViewById(R.id.btnEditarSolicitud);

        listEstadoSolicitud = findViewById(R.id.detalleEstadoSolicitud);

        listNumPedido = findViewById(R.id.detalleNumSolicitud);

        listdetalleTotalNetoProducto=findViewById(R.id.detalleTotalNetoProducto);
        listdetalleTotalNetoProducto.setEnabled(false);

        listdetalleSolicitudProveedorNombre=findViewById(R.id.detalleSolicitudProveedorNombre);
        listdetalleSolicitudProveedorNombre.setEnabled(false);

        listdetalleSoliciTelefonoCliente=findViewById(R.id.detalleSoliciTelefonoCliente);
        listdetalleSoliciTelefonoCliente.setEnabled(false);

        listProveedor = findViewById(R.id.detalleSolicitudProveedor);
        listProveedor.setEnabled(false);

        listdetalleSolicitudReferCliente=findViewById(R.id.detalleSolicitudReferCliente);
        listdetalleSolicitudReferCliente.setEnabled(false);

        listdetalleSolicitudCantidadProduc=findViewById(R.id.detalleSolicitudCantidadProduc);
        listdetalleSolicitudCantidadProduc.setEnabled(false);

        listdetalleSolicitudCostoDelivery=findViewById(R.id.detalleSolicitudCostoDelivery);
        listdetalleSolicitudCostoDelivery.setEnabled(false);

        listdetalleSolicitudCostoTotalProd=findViewById(R.id.detalleSolicitudCostoTotalProd);
        listdetalleSolicitudCostoTotalProd.setEnabled(false);

        listdetalleSolicitudPrecioU=findViewById(R.id.detalleSolicitudPrecioU);
        listdetalleSolicitudPrecioU.setEnabled(false);

        listdetalleSolicitudListadeProductos=findViewById(R.id.detalleSolicitudListadeProductos);
        listdetalleSolicitudListadeProductos.setEnabled(false);

        clrPhotoDetalleSolicitud=findViewById(R.id.photoDetalleSolicitud);
        mLinearEditarSolicitud = findViewById(R.id.linearEditarSolicitud);

        mFloat=findViewById(R.id.btnEditarSolicitudfinal);
        mFloat.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.quantum_googgreen)));
        mFloat.setVisibility(View.INVISIBLE);

        tokenProvider = new TokenProvider();
        notificationProvider = new NotificationProvider();

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        ListaSolicitud listaSolicitud = (ListaSolicitud)bundle.getSerializable("key");
        arrayList = new ArrayList<>();
        arrayList.add(listaSolicitud.getPhoto());
        arrayList.add(listaSolicitud.getNumSolicitud());
        arrayList.add(listaSolicitud.getEstado());
        arrayList.add(listaSolicitud.getTotalNetoProducto());
        arrayList.add(listaSolicitud.getNombreCliente());
        arrayList.add(listaSolicitud.getTelefonoCliente());
        arrayList.add(listaSolicitud.getProveedor());
        arrayList.add(listaSolicitud.getReferenciaCliente());
        arrayList.add(listaSolicitud.getCantidadProducto());
        arrayList.add(listaSolicitud.getCostoDelivery());
        arrayList.add(listaSolicitud.getPrecioTotalProductos());
        arrayList.add(listaSolicitud.getPrecioUnitario());
        arrayList.add(listaSolicitud.getListaDeProductos());


        Picasso
                .get()
                .load(arrayList.get(0))
                .fit()
                .error(R.drawable.ic_error)
                .placeholder(R.drawable.loaderrosa)
                .centerCrop()
                .into(clrPhotoDetalleSolicitud);

        listNumPedido.setText(arrayList.get(1));
        listEstadoSolicitud.setText(arrayList.get(2));
        listdetalleTotalNetoProducto.setText(arrayList.get(3));
        listdetalleSolicitudProveedorNombre.setText(arrayList.get(4));
        listdetalleSoliciTelefonoCliente.setText(arrayList.get(5));
        listProveedor.setText(arrayList.get(6));
        listdetalleSolicitudReferCliente.setText(arrayList.get(7));
        listdetalleSolicitudCantidadProduc.setText(arrayList.get(8));
        listdetalleSolicitudCostoDelivery.setText(arrayList.get(9));
        listdetalleSolicitudCostoTotalProd.setText(arrayList.get(10));
        listdetalleSolicitudPrecioU.setText(arrayList.get(11));
        listdetalleSolicitudListadeProductos.setText(arrayList.get(12));
        mFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFloat.setVisibility(View.INVISIBLE);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
                mLinearEditarSolicitud.setLayoutParams(params);
                updateData();
                updateDataSolicitud();
                noFocusableEditText();
            }
        });

        btnEditar_solicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(tiempo);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, 0);
                mLinearEditarSolicitud.setLayoutParams(params);
                focusableEditText();
                mFloat.setVisibility(View.VISIBLE);

            }
        });

    }

    public void onClickLlamadaCliente(View v) {
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+listTelefonoCliente.getText().toString()));
        startActivity(i);
    }

    public void showPopupEstadoS(View view){
        vibrator.vibrate(tiempo);
        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.setOnMenuItemClickListener(this);
        if (listEstadoSolicitud.getText().toString().equals("Confirmado")){
            popupMenu.inflate(R.menu.popup_menu_solicitud_estado2);
            popupMenu.show();
        }
        if (listEstadoSolicitud.getText().toString().equals("Sin confirmar") || listEstadoSolicitud.getText().toString().equals("En proceso")){
            popupMenu.inflate(R.menu.popup_menu_solicitud_estado);
            popupMenu.show();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item){
        switch (item.getItemId()){
            case R.id.item1:
                estadoCompletadoSolicitudDelivery();
                estadoCompletadoSolicitud();
                finish();
                return true;

            case R.id.item2:
                estadoEsperaSolicitudDelivery();
                estadoEsperaSolicitud();
                finish();
                return true;

            case R.id.item3:

                AlertDialog.Builder builder = new AlertDialog.Builder(DetalleSolicitudProveedor.this,R.style.ThemeOverlay);
                builder.setTitle("Advertencia!");
                builder.setCancelable(false);
                builder.setIcon(R.drawable.ic_error);
                builder.setMessage("Deseas eliminar este Solicitud..? ");
                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        vibrator.vibrate(tiempo);
                        eliminarSolicitudDelivery();
                        estadoCanceladoSolicitud();
                        finish();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        vibrator.vibrate(tiempo);
                        dialog.cancel();
                    }
                });
                builder.create();
                builder.show();
                return true;

            default:
                return false;
        }
    }



    private void estadoCompletadoSolicitudDelivery(){
        String dataNombres = listProveedor.getText().toString();
        final String dataNumPedido = listNumPedido.getText().toString();
        Query reference= FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Proveedor").orderByChild("nombre_empresa").equalTo(dataNombres);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    final String key=childSnapshot.getKey();
                    Query reference2= FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Proveedor").child(key).child("Solicitudes").orderByChild("numSolicitud").equalTo(dataNumPedido);
                    reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                            for (DataSnapshot childSnapshot2: dataSnapshot2.getChildren()) {
                                String key2=childSnapshot2.getKey();
                                Map<String , Object> map = new HashMap<>();
                                map.put("estado","Confirmado");
                                mDatabaseSolicitudDeliver.child("Usuarios").child("Proveedor").child(key).child("Solicitudes").child(key2).updateChildren(map);
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

    private void estadoCompletadoSolicitud(){
        String dataNumPedido = listNumPedido.getText().toString();
        Query reference= FirebaseDatabase.getInstance().getReference().child("SolicitudDelivery").orderByChild("numSolicitud").equalTo(dataNumPedido);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String key=childSnapshot.getKey();
                    // Toast.makeText(DetallePedido.this, "Id : "+key, Toast.LENGTH_SHORT).show();
                    Map<String , Object> map = new HashMap<>();
                    map.put("estado","Confirmado");
                    mDatabaseSolicitudDeliver.child("SolicitudDelivery").child(key).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toasty.success(DetalleSolicitudProveedor.this, "PEDIDO CONFIRMADO!", Toast.LENGTH_LONG, true).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.info(DetalleSolicitudProveedor.this, "Error al actualizar estado", Toast.LENGTH_LONG, true).show();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void estadoEsperaSolicitudDelivery(){
        String dataNombres = listProveedor.getText().toString();
        final String dataNumPedido = listNumPedido.getText().toString();
        Query reference= FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Proveedor").orderByChild("nombre_empresa").equalTo(dataNombres);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {

                    for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                        final String key = childSnapshot.getKey();
                        Query reference2 = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Proveedor").child(key).child("Solicitudes").orderByChild("numSolicitud").equalTo(dataNumPedido);
                        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                if (dataSnapshot2.exists()) {
                                    for (DataSnapshot childSnapshot2 : dataSnapshot2.getChildren()) {
                                        String key2 = childSnapshot2.getKey();
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("estado", "Sin confirmar");
                                        mDatabaseSolicitudDeliver.child("Usuarios").child("Proveedor").child(key).child("Solicitudes").child(key2).updateChildren(map);
                                    }
                                } else {
                                    Toast.makeText(DetalleSolicitudProveedor.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }


                }
                else {
                    Toast.makeText(DetalleSolicitudProveedor.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void estadoEsperaSolicitud(){
        String dataNumPedido = listNumPedido.getText().toString();
        Query reference= FirebaseDatabase.getInstance().getReference().child("SolicitudDelivery").orderByChild("numSolicitud").equalTo(dataNumPedido);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String key=childSnapshot.getKey();
                    //Toast.makeText(DetallePedido.this, "Id : "+key, Toast.LENGTH_SHORT).show();
                    Map<String , Object> map = new HashMap<>();
                    map.put("estado","Sin confirmar");
                    mDatabaseSolicitudDeliver.child("SolicitudDelivery").child(key).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toasty.info(DetalleSolicitudProveedor.this, "PEDIDO SIN CONFIRMAR!", Toast.LENGTH_LONG, true).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.info(DetalleSolicitudProveedor.this, "Error al actualizar estado", Toast.LENGTH_LONG, true).show();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void eliminarSolicitudDelivery(){
        String dataNumPedido = listNumPedido.getText().toString();
        Query reference= FirebaseDatabase.getInstance().getReference().child("SolicitudDelivery").orderByChild("numSolicitud").equalTo(dataNumPedido);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String key=childSnapshot.getKey();
                    mDatabaseSolicitudDeliver.child("SolicitudDelivery").child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toasty.error(DetalleSolicitudProveedor.this, "SOLICITUD ELIMINADO!", Toast.LENGTH_LONG, true).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.info(DetalleSolicitudProveedor.this, "Error al eliminar solicitud", Toast.LENGTH_LONG, true).show();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void estadoCanceladoSolicitud(){
        String dataNombres = listProveedor.getText().toString();
        final String dataNumSolicitud = listNumPedido.getText().toString();
        Query reference= FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Proveedor").orderByChild("nombres").equalTo(dataNombres);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    final String key=childSnapshot.getKey();
                    Query reference2= FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Proveedor").child(key).child("Solicitudes").orderByChild("numSolicitud").equalTo(dataNumSolicitud);
                    reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot childSnapshot2: dataSnapshot.getChildren()) {
                                String key2=childSnapshot2.getKey();
                                mDatabaseSolicitudDeliver.child("Usuarios").child("Proveedor").child(key).child("Solicitudes").child(key2).removeValue();
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

    private void updateData(){
        mDialog.show();
        mDialog.setMessage("Actualizando datos...");
        mDialog.setCancelable(false);
        String dataNombres2 = listProveedor.getText().toString();
        final String dataNumPedido2 = listNumPedido.getText().toString();
        Query reference= FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Proveedor").orderByChild("nombre_empresa").equalTo(dataNombres2);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        final String key = childSnapshot.getKey();
                        Query reference2 = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Proveedor").child(key).child("Solicitudes").orderByChild("numSolicitud").equalTo(dataNumPedido2);
                        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                if(dataSnapshot2.exists()){
                                    for(DataSnapshot childSnapshot2 : dataSnapshot2.getChildren()){
                                        String key2 = childSnapshot2.getKey();
                                        Map<String, Object> mapp = new HashMap<>();
                                        mapp.put("totalNetoProducto:", listdetalleTotalNetoProducto.getText().toString());
                                        mapp.put("nombreCliente", listdetalleSolicitudProveedorNombre.getText().toString());
                                        mapp.put("telefonoCliente", listdetalleSoliciTelefonoCliente.getText().toString());
                                        mapp.put("proveedor", listProveedor.getText().toString());
                                        mapp.put("referenciaCliente", listdetalleSolicitudReferCliente.getText().toString());
                                        mapp.put("cantidadProducto", listdetalleSolicitudCantidadProduc.getText().toString());
                                        mapp.put("costoDelivery", listdetalleSolicitudCostoDelivery.getText().toString());
                                        mapp.put("precioTotalProductos", listdetalleSolicitudCostoTotalProd.getText().toString());
                                        mapp.put("precioUnitario", listdetalleSolicitudPrecioU.getText().toString());
                                        mapp.put("listaDeProductos", listdetalleSolicitudListadeProductos.getText().toString());

                                        mDatabaseSolicitudDeliver.child("Usuarios").child("Proveedor").child(key).child("Solicitudes").child(key2).updateChildren(mapp);
                                        mDialog.dismiss();
                                        Toasty.success(DetalleSolicitudProveedor.this, "Datos Actualizados!", Toast.LENGTH_SHORT).show();

                                    }

                                }
                                else {
                                    Toast.makeText(DetalleSolicitudProveedor.this, "Error", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                }
                else{
                    Toast.makeText(DetalleSolicitudProveedor.this, "Error fatal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(DetalleSolicitudProveedor.this, "Error al actualizar datos!", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void updateDataSolicitud() {
        mDialog.show();
        mDialog.setMessage("Actualizando datos...");
        mDialog.setCancelable(false);
        String dataNumPedido2 = listNumPedido.getText().toString();
        Query reference= FirebaseDatabase.getInstance().getReference().child("SolicitudDelivery").orderByChild("numSolicitud").equalTo(dataNumPedido2);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String key2=childSnapshot.getKey();
                    // Toast.makeText(DetallePedido.this, "Id : "+key, Toast.LENGTH_SHORT).show();
                    Map<String , Object> mapp = new HashMap<>();
                    mapp.put("totalNetoProducto:", listdetalleTotalNetoProducto.getText().toString());
                    mapp.put("nombreCliente", listdetalleSolicitudProveedorNombre.getText().toString());
                    mapp.put("telefonoCliente", listdetalleSoliciTelefonoCliente.getText().toString());
                    mapp.put("proveedor", listProveedor.getText().toString());
                    mapp.put("referenciaCliente", listdetalleSolicitudReferCliente.getText().toString());
                    mapp.put("cantidadProducto", listdetalleSolicitudCantidadProduc.getText().toString());
                    mapp.put("costoDelivery", listdetalleSolicitudCostoDelivery.getText().toString());
                    mapp.put("precioTotalProductos", listdetalleSolicitudCostoTotalProd.getText().toString());
                    mapp.put("precioUnitario", listdetalleSolicitudPrecioU.getText().toString());
                    mapp.put("listaDeProductos", listdetalleSolicitudListadeProductos.getText().toString());

                    mDatabaseSolicitudDeliver.child("SolicitudDelivery").child(key2).updateChildren(mapp).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toasty.success(DetalleSolicitudProveedor.this, "PEDIDO ACTUALIZADO!", Toast.LENGTH_LONG, true).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.info(DetalleSolicitudProveedor.this, "Error al actualizar", Toast.LENGTH_LONG, true).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void focusableEditText(){
        listdetalleTotalNetoProducto.setEnabled(true);
        listdetalleSolicitudProveedorNombre.setEnabled(true);
        listdetalleSoliciTelefonoCliente.setEnabled(true);
        listProveedor.setEnabled(true);
        listdetalleSolicitudReferCliente.setEnabled(true);
        listdetalleSolicitudCantidadProduc.setEnabled(true);
        listdetalleSolicitudCostoDelivery.setEnabled(true);
        listdetalleSolicitudCostoTotalProd.setEnabled(true);
        listdetalleSolicitudPrecioU.setEnabled(true);
        listdetalleSolicitudListadeProductos.requestFocus();

    }

    private void noFocusableEditText(){
        listdetalleTotalNetoProducto.setEnabled(false);
        listdetalleSolicitudProveedorNombre.setEnabled(false);
        listdetalleSoliciTelefonoCliente.setEnabled(false);
        listProveedor.setEnabled(false);
        listdetalleSolicitudReferCliente.setEnabled(false);
        listdetalleSolicitudCantidadProduc.setEnabled(false);
        listdetalleSolicitudCostoDelivery.setEnabled(false);
        listdetalleSolicitudCostoTotalProd.setEnabled(false);
        listdetalleSolicitudPrecioU.setEnabled(false);
        listdetalleSolicitudListadeProductos.setEnabled(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mDialog.dismiss();
    }




}