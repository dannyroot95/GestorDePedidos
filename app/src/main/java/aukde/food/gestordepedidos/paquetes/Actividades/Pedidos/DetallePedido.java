package aukde.food.gestordepedidos.paquetes.Actividades.Pedidos;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.os.Vibrator;
import android.util.Log;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Mapas.MapaClientePorLlamada;
import aukde.food.gestordepedidos.paquetes.Modelos.FCMBody;
import aukde.food.gestordepedidos.paquetes.Modelos.FCMResponse;
import aukde.food.gestordepedidos.paquetes.Modelos.PedidoLlamada;
import aukde.food.gestordepedidos.paquetes.Providers.NotificationProvider;
import aukde.food.gestordepedidos.paquetes.Providers.TokenProvider;
import aukde.food.gestordepedidos.paquetes.Utils.CalculoHoras;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetallePedido extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    String ruta = "https://firebasestorage.googleapis.com/v0/b/gestor-de-pedidos-aukdefood.appspot.com/o/delete.png?alt=media&token=8eb49918-1713-4e1e-aaf8-38573da32206";
    TextView listNumPedido,listNumPedido2,listNombreCliente , listTelefonoCliente ,listHoraRegistro , listFechaRegistro
            , listHoraEntrega , listFechaEntrega , listTotalPagoProducto , listDireccion , listPagoCliente
            , listTotalACobrar , listVuelto , listRepartidor , listProveedores , listProducto , listDescripcion

            , listPrecioUnitario , listCantidad ,listPrecioTotalPorProducto , listComision , listTotalDelivery ,
            listGananciaDelivery , listGananciaComision , listEstado ,listLatitud , listLongitud;

    Button mButtonShow ;
    Button mButtonShow2;
    Button mMapa;
    Button mButtonEditar;
    Button mAsignar;

    Button mShowReporteDetalle;

    SimpleDateFormat simpleDateFormatFecha = new SimpleDateFormat("dd/MM/yyy");
    Bitmap bmp,ScaledBitmap,imagendelivery,imagendeliveryUbicacion;
    int pageWidth=1200;
    Date dateObje;
    DateFormat dateFormat;

    TextView mGanasteComision , mGanasteDelivery , txtDetallePTotal , detalleGanancia1 , detalleGanancia2 , txtDetalleReferencia;

    private LinearLayout mLinearProductos ;

    private DatabaseReference mDatabase ;
    private ProgressDialog mDialog;
    private TokenProvider tokenProvider;
    private NotificationProvider notificationProvider;
    private Vibrator vibrator;
    long tiempo = 100;
    TextView txtTiempo;
    SimpleDateFormat simpleDateFormatHora = new SimpleDateFormat("HH:mm:ss");
    private FirebaseAuth mAuth;
    CalculoHoras calculoHoras;

    String datosProveedor;
    String datosProducto;
    String datosCantidad;
    String datosPrecioU;
    String datosPrecioTotalProd;
    String datosTelefonoCliente;
    String datosNombreCliente;
    String datosListaNumeroPedidos;
    String datosFecha;
    String datosHora;
    String datosComision;
    String datosDelivery;
    String datosTotalProducto;
    String datosRepartidor;
    String datosDireccion;

    ArrayList<String> arrayList;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeDark);
        setContentView(R.layout.activity_detalle_pedido);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        calculoHoras = new CalculoHoras();
        tokenProvider = new TokenProvider();
        notificationProvider = new NotificationProvider();
        txtTiempo = findViewById(R.id.detalleTiempo);
        mAuth = FirebaseAuth.getInstance();
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
        mButtonEditar = findViewById(R.id.showEditarPedido);
        mAsignar = findViewById(R.id.showAsignarRrepartidor);

        mShowReporteDetalle=findViewById(R.id.showReporteDetalle);
        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.aukfoodreportefinal);
        imagendelivery=BitmapFactory.decodeResource(getResources(),R.drawable.deliveryruta);
        ScaledBitmap= Bitmap.createScaledBitmap(bmp,490,218,false);
        imagendeliveryUbicacion=Bitmap.createScaledBitmap(imagendelivery,490,218,false);
        dateObje= new Date();

        mDialog = new ProgressDialog(this,R.style.ThemeOverlay);

        txtDetalleReferencia = findViewById(R.id.detalleReferencia);

        int alto = 0;
        mLinearProductos = findViewById(R.id.linearProductos);
        LinearLayout mLinearAsignar = findViewById(R.id.linearBtnAsignar);
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,alto);
        mLinearProductos.setLayoutParams(params);

        LinearLayout.LayoutParams parametros = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,alto);

        mButtonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                mLinearProductos.setLayoutParams(params);
            }
        });

        mButtonShow2.setOnClickListener(new View.OnClickListener() {
            int alto1 = 0;
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,alto1);
                mLinearProductos.setLayoutParams(params);
            }
        });

        mMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                mDialog.show();
                mDialog.setCancelable(false);
                mDialog.setMessage("Cargando...");
                Intent intent = new Intent(DetallePedido.this, MapaClientePorLlamada.class);
                intent.putExtra("latitud",listLatitud.getText().toString());
                intent.putExtra("longitud",listLongitud.getText().toString());
                intent.putExtra("nombre",listNombreCliente.getText().toString());
                intent.putExtra("telefono",listTelefonoCliente.getText().toString());
                intent.putExtra("proveedores",listProveedores.getText().toString());
                intent.putExtra("referencia",txtDetalleReferencia.getText().toString());
                startActivity(intent);
            }
        });

        mButtonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                mDialog.show();
                mDialog.setCancelable(false);
                mDialog.setMessage("Cargando...");
                Intent intent = new Intent(DetallePedido.this, EditarPedido.class);
                intent.putExtra("numPedido",listNumPedido.getText().toString());
                startActivity(intent);
            }
        });

        mAsignar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                mDialog.show();
                mDialog.setCancelable(false);
                mDialog.setMessage("Cargando...");
                Intent intent = new Intent(DetallePedido.this, AsignarRepartidor.class);
                intent.putExtra("numPedido",listNumPedido.getText().toString());
                startActivity(intent);
            }
        });

        mShowReporteDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPdf();
                showPDFDialog();
                Toasty.success(DetallePedido.this, "PDF Guardado!", Toast.LENGTH_SHORT).show();

            }
        });

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        PedidoLlamada pedidoLlamada = (PedidoLlamada)bundle.getSerializable("key");
        arrayList = new ArrayList<>();
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
        checkReference();

        String stEstado = listEstado.getText().toString();

        datosProducto=listProducto.getText().toString();
        datosProveedor=listProveedores.getText().toString();
        datosCantidad=listCantidad.getText().toString();
        datosPrecioU=listPrecioUnitario.getText().toString();
        datosPrecioTotalProd=listPrecioTotalPorProducto.getText().toString();
        datosTelefonoCliente=listTelefonoCliente.getText().toString();
        datosNombreCliente=listNombreCliente.getText().toString();
        datosListaNumeroPedidos= listNumPedido2.getText().toString();
        datosFecha=listFechaEntrega.getText().toString();
        datosHora=listHoraEntrega.getText().toString();
        datosComision=listComision.getText().toString();
        datosDelivery=listTotalDelivery.getText().toString();
        datosTotalProducto= txtDetallePTotal.getText().toString();
        datosRepartidor=listRepartidor.getText().toString();
        datosDireccion=listDireccion.getText().toString();


        String Delivery = listGananciaDelivery.getText().toString();
        Double doubleDelivery = Double.parseDouble(Delivery);
        Double finalGananciaDeliveryAukdeliver;
        Double finalGananciaDeliveryAukde;

        if(doubleDelivery < 4.00){
            finalGananciaDeliveryAukdeliver = doubleDelivery * 0.5;
            detalleGanancia1.setText(obtieneDosDecimales(finalGananciaDeliveryAukdeliver));
            detalleGanancia2.setText(obtieneDosDecimales(finalGananciaDeliveryAukdeliver));
        }
        if(doubleDelivery >= 4.00 && doubleDelivery < 9.00){
            finalGananciaDeliveryAukdeliver = doubleDelivery * 0.4;
            finalGananciaDeliveryAukde = doubleDelivery * 0.6;
            detalleGanancia1.setText(obtieneDosDecimales(finalGananciaDeliveryAukde));
            detalleGanancia2.setText(obtieneDosDecimales(finalGananciaDeliveryAukdeliver));
        }
        if(doubleDelivery >= 9.00){
            finalGananciaDeliveryAukdeliver = doubleDelivery * 0.3;
            finalGananciaDeliveryAukde = doubleDelivery * 0.7;
            detalleGanancia1.setText(obtieneDosDecimales(finalGananciaDeliveryAukde));
            detalleGanancia2.setText(obtieneDosDecimales(finalGananciaDeliveryAukdeliver));
        }

        if (stEstado.equals("Completado")){

            listEstado.setTextColor(Color.parseColor("#5bbd00"));
            //mGanasteComision.setText("Por este pedido\n   ganaste   : ");
            //mGanasteDelivery.setText("Por este delivery\n   ganaste : ");
            mLinearAsignar.setLayoutParams(parametros);
        }

        if (stEstado.equals("Cancelado") || stEstado.equals("Rechazado")){
            listEstado.setTextColor(Color.parseColor("#E74C3C"));
        }

        if (stEstado.equals("En espera")){
            listEstado.setTextColor(Color.parseColor("#2E86C1"));
            mLinearAsignar.setLayoutParams(parametros);
        }

        if (stEstado.equals("En proceso")){
            listEstado.setTextColor(Color.parseColor("#FFC300"));
            mLinearAsignar.setLayoutParams(parametros);
        }
        tiempoEntrega();
}


    private String obtieneDosDecimales(double valor) {
        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(2); //Define 2 decimales.
        return format.format(valor);
    }

    public void showPopupEstado(View view){
        vibrator.vibrate(tiempo);
        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.setOnMenuItemClickListener(this);
        if (listEstado.getText().toString().equals("Completado")){
            popupMenu.inflate(R.menu.popup_menu_estado2);
            popupMenu.show();
        }
        if (listEstado.getText().toString().equals("En espera") || listEstado.getText().toString().equals("En proceso")){
            popupMenu.inflate(R.menu.popup_menu_estado);
            popupMenu.show();
        }

        if (listEstado.getText().toString().equals("Cancelado") || listEstado.getText().toString().equals("Rechazado")){
            popupMenu.inflate(R.menu.popup_menu_estado3);
            popupMenu.show();
        }

    }

    public void onClickLlamadaCliente(View v) {
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
                AlertDialog.Builder builderCancel = new AlertDialog.Builder(DetallePedido.this,R.style.ThemeOverlay);
                builderCancel.setTitle("Alerta!");
                builderCancel.setCancelable(false);
                builderCancel.setIcon(R.drawable.ic_error);
                builderCancel.setMessage("Deseas cancelar este pedido..? ");
                builderCancel.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        estadoCanceladoRepartidor();
                        estadoCancelado();
                        sendCancelNotificationAukdeliver();
                        finish();
                    }
                });
                builderCancel.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builderCancel.create();
                builderCancel.show();
                return true;

            case R.id.item3:
                estadoEsperaRepartidor();
                estadoEspera();
                finish();
                return true;

            case R.id.item4:

                AlertDialog.Builder builder = new AlertDialog.Builder(DetallePedido.this,R.style.ThemeOverlay);
                builder.setTitle("Advertencia!");
                builder.setCancelable(false);
                builder.setIcon(R.drawable.ic_error);
                builder.setMessage("Deseas eliminar este pedido..? ");
                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        vibrator.vibrate(tiempo);
                        eliminarPedido();
                        estadoCanceladoRepartidor();
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
                                mDatabase.child("Usuarios").child("Aukdeliver").child(key).child("pedidos").child(key2).removeValue();
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

    private void sendCancelNotificationAukdeliver(){
        String nombreAukdeliver =  listRepartidor.getText().toString();
        final String numeroPedido = listNumPedido.getText().toString();
        Query reference= FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Aukdeliver").orderByChild("nombres").equalTo(nombreAukdeliver);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    tokenProvider.getToken(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                String token = dataSnapshot.child("token").getValue().toString();
                                Map<String,String> map = new HashMap<>();
                                map.put("title","El pedido #"+numeroPedido);
                                map.put("body","ha sido CANCELADO!");
                                map.put("path",ruta);
                                FCMBody fcmBody = new FCMBody(token,"high",map);
                                notificationProvider.sendNotificacion(fcmBody).enqueue(new Callback<FCMResponse>() {
                                    @Override
                                    public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                                        if(response.body() !=null){
                                            if(response.body().getSuccess() == 1){
                                                //Toast.makeText(RealizarPedido.this, "Notificación enviada", Toast.LENGTH_LONG).show();
                                            }
                                            else{
                                                Toasty.error(DetallePedido.this, "NO se pudo ENVIAR la notificación!", Toast.LENGTH_LONG).show();                                            }
                                        }
                                        else {
                                            Toasty.error(DetallePedido.this, "NO se pudo ENVIAR la notificación!", Toast.LENGTH_LONG).show();                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<FCMResponse> call, Throwable t) {
                                        Log.d("Error","Error encontrado"+ t.getMessage());
                                    }
                                });
                            }

                            else {
                                Toasty.error(DetallePedido.this, "NO se pudo ENVIAR la notificación!", Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    //Toast.makeText(EditarPedido.this, key, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void eliminarPedido(){
        String dataNumPedido = listNumPedido.getText().toString();
        Query reference= FirebaseDatabase.getInstance().getReference().child("PedidosPorLlamada").child("pedidos").orderByChild("numPedido").equalTo(dataNumPedido);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String key=childSnapshot.getKey();
                    mDatabase.child("PedidosPorLlamada").child("pedidos").child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toasty.error(DetallePedido.this, "PEDIDO ELIMINADO!", Toast.LENGTH_LONG, true).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.info(DetallePedido.this, "Error al eliminar pedido", Toast.LENGTH_LONG, true).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(DetallePedido.this,R.style.ThemeOverlay);
        builder.setTitle("Confirmar");
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_atras);
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

    private void checkReference(){
        String dataNumPedido = listNumPedido.getText().toString();
        Query reference= FirebaseDatabase.getInstance().getReference().child("PedidosPorLlamada").child("pedidos").orderByChild("numPedido").equalTo(dataNumPedido);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String key=childSnapshot.getKey();
                    mDatabase.child("PedidosPorLlamada").child("pedidos").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild("referencia")){
                                String referencia = dataSnapshot.child("referencia").getValue().toString();
                                txtDetalleReferencia.setText(referencia);
                            }
                            else{
                                txtDetalleReferencia.setText("Ninguna");
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

    private void tiempoEntrega(){
        String dataNumPedido = listNumPedido.getText().toString();
        Query reference = FirebaseDatabase.getInstance().getReference().child("PedidosPorLlamada").child("pedidos").orderByChild("numPedido").equalTo(dataNumPedido);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String key=childSnapshot.getKey();
                    mDatabase.child("PedidosPorLlamada").child("pedidos").child(key).child("tiempo").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild("tiempo2") && dataSnapshot.hasChild("tiempo1")) {
                                String time1 = dataSnapshot.child("tiempo1").getValue().toString();
                                String time2 = dataSnapshot.child("tiempo2").getValue().toString();
                                try {
                                    Date date1 = simpleDateFormatHora.parse(time1);
                                    Date date2 = simpleDateFormatHora.parse(time2);
                                    Date difference = calculoHoras.getDiferenciaHoras(date1,date2);
                                    txtTiempo.setText(simpleDateFormatHora.format(difference));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                //Toast.makeText(DetallePedidoAukdeliver.this, "Tiempo Ini : "+time1+" Tiempo Fin : "+time2, Toast.LENGTH_SHORT).show();
                            }
                            else{
                                txtTiempo.setText("No disponible!");
                                //Toast.makeText(DetallePedidoAukdeliver.this, "Sin tiempo 2", Toast.LENGTH_SHORT).show();
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

    private void createPdf(){

        PdfDocument myPdfDocument= new PdfDocument();
        Paint myPaint =new Paint();
        Paint titlePaint= new Paint();

        PdfDocument.PageInfo myPageInfo1 = new  PdfDocument.PageInfo.Builder(1200 ,2010,1).create();
        PdfDocument.Page myPage1 = myPdfDocument.startPage(myPageInfo1);

        Canvas canvas =myPage1.getCanvas();
        canvas.drawBitmap(ScaledBitmap, 100,80,myPaint);
        canvas.drawBitmap(imagendeliveryUbicacion, 100,540,myPaint);


        titlePaint.setTextAlign(Paint.Align.RIGHT);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));
        titlePaint.setTextSize(60);
        canvas.drawText("Reporte Pedidos",pageWidth-90,190,titlePaint);
        myPaint.setColor(Color.rgb(0,0,0));
        myPaint.setTextSize(30f);
        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("N.Celular: +51 992-997-223",pageWidth-90,250,myPaint);
        canvas.drawText("+51 992-787-316",pageWidth-90,320,myPaint);

        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.ITALIC));
        titlePaint.setTextSize(70);
        //canvas.drawText("Pedidos",pageWidth/2,500,titlePaint);

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTextSize(40f);
        myPaint.setColor(Color.BLACK);
        canvas.drawText("Nombre del Cliente:"+"\t"+ datosNombreCliente,100,400,myPaint);
        canvas.drawText("No.Contacto:"+"\t"+ datosTelefonoCliente,100,470,myPaint);


        myPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("No.Pedido:"+ "234567",pageWidth-90,400,myPaint);

        dateFormat= new SimpleDateFormat("dd/MM/yy");
        canvas.drawText("Fecha de entrega: ",pageWidth-90,470,myPaint);
        canvas.drawText(datosFecha,pageWidth-110,530,myPaint);

        dateFormat= new SimpleDateFormat("HH:mm/ss");
        canvas.drawText("Hora de entrega: ",pageWidth-90,590,myPaint);
        canvas.drawText(datosHora,pageWidth-110,650,myPaint);

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setStyle(Paint.Style.FILL);
        canvas.drawText("Producto: ",100,830,myPaint);
        canvas.drawText("Cantidad: ",570,830,myPaint);
        canvas.drawText("Precio.U: ",770,830,myPaint);
        canvas.drawText("Total: ",970,830,myPaint);

        canvas.drawText("Proveedor: ",100,1100,myPaint);
        canvas.drawText("Comision: ",570,1100,myPaint);
        canvas.drawText("Delivery: ",790,1100,myPaint);


        canvas.drawText("Total(Producto): ",680,1430,myPaint);
        canvas.drawText("Repartidor Encargado: ",100,1430,myPaint);
        canvas.drawText("Direccion del Cliente: ",100,1600,myPaint);




        canvas.drawLine(560,790,560,840,myPaint);
        canvas.drawLine(760,790,760,840,myPaint);
        canvas.drawLine(960,790,960,840,myPaint);

        //Parte de abajo
        canvas.drawLine(560,1050,560,1120,myPaint);
        canvas.drawLine(780,1050,780,1120,myPaint);

        int y=950;
        int w=950;
        int z=950;
        int H=950;
        int k=1200;
        int P=1200;
        int x=1200;
        int o=1500;
        int gh=1500;
        int gj=1650;

        String[] DatosProducto = datosProducto.split("\n");
        String[] CantidadProducto= datosCantidad.split("\n");
        String[] PrecioUnitario= datosPrecioU.split("\n");
        String[] PrecioTotal=datosPrecioTotalProd.split("\n");
        String[] DatoProveedor= datosProveedor.split("\n");
        String[] DatosComision=datosComision.split("\n");
        String[] DatosDelivery=datosDelivery.split("\n");


        for(String txt : DatosProducto){
            canvas.drawText(txt, 100, y, myPaint);
            y += 50;
        }
        for(String txt1 : CantidadProducto){
            canvas.drawText(txt1, 660, w, myPaint);
            w += 50;
        }
        for(String txt2 : PrecioUnitario){
            canvas.drawText(txt2, 820, z, myPaint);
            z += 50;
        }

        for(String txt3 : PrecioTotal){
            canvas.drawText(txt3, 970, H, myPaint);
            H += 50;
        }
        for(String txt4: DatoProveedor){
            canvas.drawText(txt4, 100, k,myPaint);
           k+=50;
        }

        for(String txt5: DatosComision){
            canvas.drawText(txt5, 660, P,myPaint);
            P+=50;
        }
        for(String txt6: DatosDelivery){
            canvas.drawText(txt6, 825,x,myPaint);
            x+=50;
    }

        canvas.drawText(datosTotalProducto, 680,o,myPaint);
        canvas.drawText(datosRepartidor, 100,gh,myPaint);
        canvas.drawText(datosDireccion, 100,gj,myPaint);

        myPdfDocument.finishPage(myPage1);
        String stNumPedido = listNumPedido.getText().toString();
        File file =new File(this.getExternalFilesDir("/"),
                "Pedido-"+stNumPedido+".pdf");

        try{
            myPdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e){
            e.printStackTrace();
        }
        myPdfDocument.close();
    }

    private void showPDFDialog(){
        AlertDialog.Builder builderPDF = new AlertDialog.Builder(DetallePedido.this,R.style.ThemeOverlay);
        builderPDF.setTitle("PDF creado!");
        builderPDF.setCancelable(false);
        builderPDF.setIcon(R.drawable.lista_admin);
        builderPDF.setMessage("Seleccione una opción");
        builderPDF.setPositiveButton("Abrir Carpeta", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory() + "/Android/data/aukde.food.gestordepedidos/files/");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(selectedUri, "resource/folder");
                if (intent.resolveActivityInfo(getPackageManager(), 0) != null)
                {
                    startActivity(intent);
                }
                else
                {
                    Toasty.info(DetallePedido.this, "Se requiere que instale esta APP!", Toast.LENGTH_LONG,false).show();
                    Intent intentx = new Intent(Intent.ACTION_VIEW);
                    Uri.Builder uriBuilder = Uri.parse("https://play.google.com/store/apps/details?id=com.alphainventor.filemanager")
                            .buildUpon()
                            .appendQueryParameter("id", "aukde.food.gestordepedidos")
                            .appendQueryParameter("launch", "true");

                    uriBuilder.appendQueryParameter("referrer", "IdLaunchGGplay");

                    intentx.setData(uriBuilder.build());
                    intentx.setPackage("com.android.vending");
                    startActivity(intentx);
                }
            }
        });
        builderPDF.setNegativeButton("Ver PDF", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                File filePDF = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/aukde.food.gestordepedidos/files/Pedido-"+listNumPedido.getText().toString()+".pdf");
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                Uri uri = Uri.fromFile(filePDF);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/pdf");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        builderPDF.setNeutralButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builderPDF.create();
        builderPDF.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDialog.dismiss();
    }

}