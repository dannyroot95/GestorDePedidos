package aukde.food.gestordepedidos.paquetes.Actividades.Pedidos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.widget.Spinner;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Menus.MenuAdmin;
import aukde.food.gestordepedidos.paquetes.Providers.PedidoProvider;
import es.dmoral.toasty.Toasty;

public class EditarPedido  extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMarkerDragListener  {
    private ProgressDialog mDialog;
    SimpleDateFormat simpleDateFormatHora = new SimpleDateFormat("HH:mm");
    SimpleDateFormat simpleDateFormatFecha = new SimpleDateFormat("dd/MM/yyy");
    int dia, mes, year, horaRel, minutoRel;
    String formatoHora = simpleDateFormatHora.format(new Date());
    String formatoFecha = simpleDateFormatFecha.format(new Date());

    TextView horaPedido, fechaPedido, fechaEntrega , horaEntrega
            , precioProductoTotal, precioNetoTotal, vuelto;

    private Button  btnCalcularVuelto;

    private Spinner mSpinnerProveedor;

    private Button mBtnAdd , mbtnMin , mbtnMax , mBtnClean;

    private TextView edtNumPedido,textSocio,txtProducto,txtDescripcion,txtPrecioUnitario,txtDelivery , edtCantidad , txtCantidad ,
            txtPtotal , txtNeto , txtGananciaPorDelivery , txtPrecioComisionProducto , txtNetoComision , IDpedido;
    private CheckBox chComision;
    public EditText  edtMontoCliente , edtNombreCliente, edtTelefono , edtDireccion , edtReferencia ;
    private EditText mSocio , mProducto , mDescripcion , mPrecioUnitario , mDelivery ,mNuevaComision  ;
    private LinearLayout mLinearMap;
    PedidoProvider mpedidoProvider;

    private MapView mapView;
    private GoogleMap mMap;
    private Geocoder geocoder;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final String TAG = "RealizarPedido";
    private LatLng destino;

    DatabaseReference mUsuarioAukdeliver;
    private DatabaseReference pedidosActualizadoAdmin;
    private DatabaseReference mDatabase;
    private DatabaseReference pedidoParaAukdeliver;
    DatabaseReference mUsuarioProveedor;
    FloatingActionButton mFloatingButton , mFloatingMap;
    TextView estado ;
    TextView txtEncargado , idAukdeliver;
    TextView IDPedidoAukdeliver;
    String stEncargado = "";
    TextView latitud,logitud;
    Button mButtonMapear;


    Bundle numeroPedido;
    private Vibrator vibrator;
    long tiempo = 100;
    //----------------------------

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_pedido);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mSpinnerProveedor = findViewById(R.id.spinnerProveedor);
        horaPedido = findViewById(R.id.horaPedido);
        fechaPedido = findViewById(R.id.fechaPedido);
        horaEntrega = findViewById(R.id.horaEntrega);
        fechaEntrega = findViewById(R.id.fechaEntrega);
        horaEntrega.setText(formatoHora);
        fechaEntrega.setText(formatoFecha);
        vuelto = findViewById(R.id.vuelto);
        horaPedido.setText(formatoHora);
        fechaPedido.setText(formatoFecha);
        edtNombreCliente = findViewById(R.id.nombreCliente);
        edtNumPedido = findViewById(R.id.numPedido);

        IDpedido = findViewById(R.id.idEditPedido);
        IDPedidoAukdeliver = findViewById(R.id.idEditPedidoAukdeliver);
        numeroPedido  = getIntent().getExtras();
        String stExtraNumPedido = numeroPedido.getString("numPedido");
        edtNumPedido.setText(stExtraNumPedido);
        // nuevo
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mBtnAdd = findViewById(R.id.add);
        mbtnMin = findViewById(R.id.btnMin);
        mbtnMax = findViewById(R.id.btnMax);
        mBtnClean = findViewById(R.id.btnClean);

        mSocio = findViewById(R.id.edtSocio);
        mProducto = findViewById(R.id.edtProducto);
        mDescripcion = findViewById(R.id.edtDescripcion);
        mPrecioUnitario = findViewById(R.id.edtPrecUnitario);
        mDelivery = findViewById(R.id.edtPrecioDelivery);
        mNuevaComision = findViewById(R.id.edtNuevaComision);
        edtCantidad = findViewById(R.id.edtCant);

        textSocio = findViewById(R.id.lsSocio);
        txtProducto = findViewById(R.id.lsProducto);
        txtDescripcion = findViewById(R.id.lsDescripcion);
        txtPrecioUnitario = findViewById(R.id.lsPUnitario);
        txtDelivery = findViewById(R.id.lsPDelivery);
        txtCantidad = findViewById(R.id.lsCant);
        txtPtotal = findViewById(R.id.lsPTotal);
        txtNeto = findViewById(R.id.precioProductoTotal);
        txtGananciaPorDelivery = findViewById(R.id.gananciaDelivery);
        txtPrecioComisionProducto = findViewById(R.id.lsComision);
        txtNetoComision = findViewById(R.id.gananciaAdmin);
        precioNetoTotal = findViewById(R.id.neto);
        mNuevaComision.setVisibility(View.INVISIBLE);
        mUsuarioProveedor = FirebaseDatabase.getInstance().getReference();
        chComision = findViewById(R.id.checkboxComision);
        chComision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chComision.isChecked()){
                    mNuevaComision.setVisibility(View.VISIBLE);
                }
                else{
                    mNuevaComision.setVisibility(View.INVISIBLE);
                    mNuevaComision.setText("");
                }
            }
        });

        obtenerPedido();

        final androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(EditarPedido.this,R.style.ThemeOverlay).create();
        alertDialog.setTitle("Hey!");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Deseas agregar otro producto del mismo socio?");
        alertDialog.setIcon(R.drawable.ic_hey);

        alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE, "Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mProducto.setText("");
                mDescripcion.setText("");
                mPrecioUnitario.setText("");
                mDelivery.setText("");
                edtCantidad.setText("1");
                mProducto.requestFocus();
            }
        });

        alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                mSocio.setText("");
                mProducto.setText("");
                mDescripcion.setText("");
                mPrecioUnitario.setText("");
                mDelivery.setText("");
                edtCantidad.setText("1");
                edtNombreCliente.requestFocus();
            }
        });


        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                String S = mSocio.getText().toString();
                String P = mProducto.getText().toString();
                String D = mDescripcion.getText().toString();
                String Pu = mPrecioUnitario.getText().toString();
                String Del = mDelivery.getText().toString();
                String Cant = edtCantidad.getText().toString();
                String netoValor = txtNeto.getText().toString();
                String ganancia = txtGananciaPorDelivery.getText().toString();
                Double dNeto = Double.parseDouble(netoValor);

                if (S.isEmpty() || P.isEmpty() || Pu.isEmpty() || Del.isEmpty()){
                    Toasty.error(EditarPedido.this, "Complete los Campos", Toast.LENGTH_SHORT).show();
                }
                else {
                    textSocio.append(S+"\n");
                    txtProducto.append(P+"\n");
                    txtDescripcion.append(D+"\n");
                    txtPrecioUnitario.append(Pu+"\n");
                    txtDelivery.append(Del+"\n");
                    txtCantidad.append(Cant+"\n");
                    int Cantidad = Integer.parseInt(Cant);
                    Double Precio = Double.parseDouble(Pu);
                    Double total = Cantidad*Precio;
                    String sTotal = String.valueOf(total);
                    txtPtotal.append(sTotal+"\n");

                    Double finalNeto = total + dNeto;
                    String stNeto = String.valueOf(finalNeto);
                    txtNeto.setText(stNeto);
                    precioProductoTotal.setText(stNeto);

                    Double Gan = Double.parseDouble(ganancia);
                    Double dDelivery = Double.parseDouble(Del);
                    Double sumGanancia = Gan+dDelivery;
                    String netoGanancia = String.valueOf(sumGanancia);
                    Double totalCobro = sumGanancia+finalNeto;
                    String netoCobro = String.valueOf(totalCobro);
                    txtGananciaPorDelivery.setText(netoGanancia);
                    precioNetoTotal.setText(netoCobro);

                    String SComision = txtNetoComision.getText().toString();
                    Double DComision = Double.valueOf(SComision);

                    chComision = findViewById(R.id.checkboxComision);
                    chComision.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(chComision.isChecked()){
                                mNuevaComision.setVisibility(View.VISIBLE);
                            }
                            else {
                                mNuevaComision.setVisibility(View.INVISIBLE);
                            }

                        }
                    });


                    if (chComision.isChecked()==false){
                        mNuevaComision.setVisibility(View.INVISIBLE);
                        if (Precio >= 25){
                            Double totalMaxComision = 1.0 * Cantidad;
                            String sTotalMaxComision = String.valueOf(totalMaxComision);

                            Double comisionxProducto = DComision + totalMaxComision;
                            String stComisionxProducto = String.valueOf(comisionxProducto);

                            txtPrecioComisionProducto.append(sTotalMaxComision+"\n");
                            txtNetoComision.setText(stComisionxProducto);
                        }
                        if (Precio < 25) {
                            Double totalMinComision = 0.5 * Cantidad;
                            String sTotalMinComision = String.valueOf(totalMinComision);

                            Double comisionxProductoMin = DComision + totalMinComision;
                            String stComisionxProductoMin = String.valueOf(comisionxProductoMin);

                            txtPrecioComisionProducto.append(sTotalMinComision+"\n");
                            txtNetoComision.setText(stComisionxProductoMin);
                        }
                    }

                    else {
                        mNuevaComision.setVisibility(View.VISIBLE);
                        if (Precio >= 25){
                            String stNuevaComision = mNuevaComision.getText().toString();
                            Double DNuevaComision = Double.parseDouble(stNuevaComision);
                            Double totalMaxComision = DNuevaComision * Cantidad;
                            String sTotalMaxComision = String.valueOf(totalMaxComision);

                            Double comisionxProducto = DComision + totalMaxComision;
                            String stComisionxProducto = String.valueOf(comisionxProducto);

                            txtPrecioComisionProducto.append(sTotalMaxComision+"\n");
                            txtNetoComision.setText(stComisionxProducto);
                        }
                        if (Precio < 25) {
                            String stNuevaComision = mNuevaComision.getText().toString();
                            Double DNuevaComision = Double.parseDouble(stNuevaComision);
                            Double totalMinComision = DNuevaComision * Cantidad;
                            String sTotalMinComision = String.valueOf(totalMinComision);

                            Double comisionxProductoMin = DComision + totalMinComision;
                            String stComisionxProductoMin = String.valueOf(comisionxProductoMin);

                            txtPrecioComisionProducto.append(sTotalMinComision+"\n");
                            txtNetoComision.setText(stComisionxProductoMin);
                        }
                    }

                    alertDialog.show();
                }
            }
        });

        mbtnMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = edtCantidad.getText().toString();
                if (num.equals("1")){
                    edtCantidad.setText("1");
                }
                else {
                    int Cc = Integer.parseInt(num);
                    int res = Cc-1;
                    String Min = String.valueOf(res);
                    edtCantidad.setText(Min);
                }

            }
        });

        mbtnMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = edtCantidad.getText().toString();
                int Cc = Integer.parseInt(num);
                int res = Cc+1;
                String Min = String.valueOf(res);
                edtCantidad.setText(Min);
            }
        });

        mBtnClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                textSocio.setText("");
                txtProducto.setText("");
                txtDescripcion.setText("");
                txtPrecioUnitario.setText("");
                txtDelivery.setText("");
                txtCantidad.setText("");
                txtPtotal.setText("");
                txtNeto.setText("0");
                txtGananciaPorDelivery.setText("0");
                txtPrecioComisionProducto.setText("");
                txtNetoComision.setText("0");
            }
        });

        //---------------
        btnCalcularVuelto = findViewById(R.id.calcularVuelto);
        mDialog = new ProgressDialog(this,R.style.ThemeOverlay);
        edtMontoCliente = findViewById(R.id.montoPagarcliente);
        precioProductoTotal = findViewById(R.id.precioProductoTotal);
        edtTelefono = findViewById(R.id.telefonoCliente);
        edtDireccion = findViewById(R.id.direcionCliente);
        edtDireccion.setEnabled(false);
        edtReferencia = findViewById(R.id.referenciaCliente);
        txtEncargado = findViewById(R.id.txtRepartidor);
        idAukdeliver = findViewById(R.id.txtIdAukdeliver);
        pedidoParaAukdeliver = FirebaseDatabase.getInstance().getReference();

        latitud = findViewById(R.id.txtLatitud);
        logitud = findViewById(R.id.txtLongitud);
        mButtonMapear = findViewById(R.id.btnMapear);

        // intentExtra numero de pedido

        int alto = 0;
        mLinearMap =findViewById(R.id.map_container);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,alto);
        mLinearMap.setLayoutParams(params);
        mpedidoProvider = new PedidoProvider();
        estado = findViewById(R.id.txtEstado);

        mUsuarioAukdeliver = FirebaseDatabase.getInstance().getReference();
        pedidosActualizadoAdmin = FirebaseDatabase.getInstance().getReference("PedidosPorLlamada").child("pedidos");

        mFloatingButton = findViewById(R.id.floatRegister);
        mFloatingButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.quantum_black_100)));
        mFloatingMap = findViewById(R.id.booleanMap);
        mFloatingMap.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.quantum_googred)));

        geocoder = new Geocoder(this);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        fechaEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickFecha();
            }
        });
        horaEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickHora();
            }
        });
        btnCalcularVuelto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                ClickCalcularVuelto();
            }
        });

        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                AlertDialog.Builder builder = new AlertDialog.Builder(EditarPedido.this,R.style.ThemeOverlay);
                builder.setTitle("Actualización de pedido");
                builder.setCancelable(false);
                builder.setIcon(R.drawable.ic_correcto);
                builder.setMessage("Deseas actualizar este pedido? ");
                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        vibrator.vibrate(tiempo);
                        clickActualizarPedido();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        vibrator.vibrate(tiempo);
                        dialog.cancel();
                        //Toast.makeText(EditarPedido.this, "Pedido Cancelado", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.create();
                builder.show();
            }
        });

        mFloatingMap.setVisibility(View.INVISIBLE);
        mFloatingMap.setOnClickListener(new View.OnClickListener() {
            int alto1 = 0;
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,alto1);
                mLinearMap.setLayoutParams(params);
                mFloatingMap.setVisibility(View.INVISIBLE);
            }
        });

        mButtonMapear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                mFloatingMap.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                mLinearMap.setLayoutParams(params);
            }
        });
        obtenerProveedor();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    private void ClickFecha() {
        final Calendar c = Calendar.getInstance();
        dia = c.get(Calendar.DAY_OF_MONTH);
        mes = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(EditarPedido.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                if (month < 10 && dayOfMonth < 10) {
                    fechaEntrega.setText("0" + dayOfMonth + "/" + "0" + (month + 1) + "/" + year);
                } else if (month > 10 && dayOfMonth < 10) {
                    fechaEntrega.setText("0" + dayOfMonth + "/" + (month + 1) + "/" + year);
                } else if (month < 10 && dayOfMonth > 10) {
                    fechaEntrega.setText(dayOfMonth + "/" + "0" + (month + 1) + "/" + year);
                } else {
                    fechaEntrega.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                }
            }
        }, year, mes, dia);
        datePickerDialog.show();
    }

    private void ClickHora() {

        final Calendar c = Calendar.getInstance();
        dia = c.get(Calendar.HOUR_OF_DAY);
        mes = c.get(Calendar.MINUTE);

        TimePickerDialog PickerHora = new TimePickerDialog(EditarPedido.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (hourOfDay < 10 && minute < 10) {
                    horaEntrega.setText("0" + hourOfDay + ":" + "0" + minute);
                } else if (hourOfDay < 10 && minute > 10) {
                    horaEntrega.setText("0" + hourOfDay + ":" + minute);
                } else if (hourOfDay > 10 && minute < 10) {
                    horaEntrega.setText(hourOfDay + ":" + "0" + minute);
                } else {
                    horaEntrega.setText(hourOfDay + ":" + minute);
                }
            }
        }, horaRel, minutoRel, false);

        PickerHora.show();

    }


    private String obtieneDosDecimales(double valor) {
        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(2); //Define 2 decimales.
        return format.format(valor);
    }

    private void ClickCalcularVuelto() {

        String montoCliente = edtMontoCliente.getText().toString();
        String netoMonto = precioNetoTotal.getText().toString();

        if (TextUtils.isEmpty(montoCliente)) {
            Toasty.warning(this, "¿Con cuanto va a pagar? (Cliente)", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(netoMonto)) {
            Toasty.error(this, "Error : Calcule el total", Toast.LENGTH_SHORT).show();
        } else {
            double vueltoNeto;
            double txtmonto = Double.parseDouble(edtMontoCliente.getText().toString());
            double txtneto = Double.parseDouble(precioNetoTotal.getText().toString());
            vueltoNeto = txtmonto - txtneto;
            vuelto.setText("S/" + obtieneDosDecimales(vueltoNeto));
        }
    }





    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }


    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerDragListener(this);

        try {

            List<Address> addresses = geocoder.getFromLocation(-12.5879997, -69.1930283, 1);

            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                LatLng aukde = new LatLng(address.getLatitude(), address.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(aukde)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.aukdemarker))
                        .title("OFICINA AUKDE");
                mMap.addMarker(markerOptions).showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(aukde, 16));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onMapLongClick(final LatLng latLng) {
        Log.d(TAG, "onMapLongClick: " + latLng.toString());
        int height = 100;
        int width = 100;
        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.punto);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap punto = Bitmap.createScaledBitmap(b, width, height, false);
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                String streetAddress = address.getAddressLine(0);
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(streetAddress)
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromBitmap(punto))
                );
                destino = new LatLng(address.getLatitude(), address.getLongitude());
                Toasty.success(this, "Direccion Agregada!", Toast.LENGTH_SHORT).show();
                edtDireccion.setText(streetAddress);
                latitud.setText(""+destino.latitude);
                logitud.setText(""+destino.longitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener(){
            @Override
            public void onInfoWindowClick(final Marker marker){
                AlertDialog.Builder builder = new AlertDialog.Builder(EditarPedido.this);
                builder.setTitle("Alerta!");
                builder.setCancelable(false);
                builder.setMessage("Desea borrar esta poscición?");
                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        marker.remove();
                        edtDireccion.setText("");
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
        });
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        Log.d(TAG, "onMarkerDragStart: ");
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        Log.d(TAG, "onMarkerDrag: ");
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Log.d(TAG, "onMarkerDragEnd: ");
        LatLng latLng = marker.getPosition();
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                String streetAddress = address.getAddressLine(0);
                destino = new LatLng(address.getLatitude(), address.getLongitude());
                Toasty.success(this, "Direccion Actualizada!", Toast.LENGTH_SHORT).show();
                edtDireccion.setText(streetAddress);
                latitud.setText(""+destino.latitude);
                logitud.setText(""+destino.longitude);
                marker.setTitle(streetAddress);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clickActualizarPedido(){

        final String numeroPedido = edtNumPedido.getText().toString();
        final String nombreCliente = edtNombreCliente.getText().toString();
        final String telefonoCliente = edtTelefono.getText().toString();
        final String conCuantoVaAPagar = edtMontoCliente.getText().toString();
        final String direccion = edtDireccion.getText().toString();
        final String idAdminNumPedido = IDpedido.getText().toString();

        if( !nombreCliente.isEmpty() && !telefonoCliente.isEmpty() && !conCuantoVaAPagar.isEmpty() && !direccion.isEmpty()){
            if(!numeroPedido.isEmpty()){
                mDialog.show();
                mDialog.setMessage("Actualizando pedido...");
                Map<String, Object> map = new HashMap<>();
                map.put("numPedido", edtNumPedido.getText().toString());
                map.put("horaEntrega", horaEntrega.getText().toString());
                map.put("fechaEntrega", fechaEntrega.getText().toString());
                map.put("proveedores",textSocio.getText().toString());
                map.put("productos",txtProducto.getText().toString());
                map.put("descripcion",txtDescripcion.getText().toString());
                map.put("precioUnitario",txtPrecioUnitario.getText().toString());
                map.put("cantidad",txtCantidad.getText().toString());
                map.put("precioTotalXProducto",txtPtotal.getText().toString());
                map.put("comision",txtPrecioComisionProducto.getText().toString());
                map.put("totalDelivery",txtDelivery.getText().toString());
                map.put("gananciaDelivery",txtGananciaPorDelivery.getText().toString());
                map.put("gananciaComision",txtNetoComision.getText().toString());
                map.put("totalPagoProducto",precioProductoTotal.getText().toString());
                map.put("nombreCliente", edtNombreCliente.getText().toString());
                map.put("telefono",edtTelefono.getText().toString());
                map.put("conCuantoVaAPagar",edtMontoCliente.getText().toString());
                map.put("totalCobro",precioNetoTotal.getText().toString());
                map.put("vuelto",vuelto.getText().toString());
                map.put("direccion",edtDireccion.getText().toString());
                map.put("encargado",txtEncargado.getText().toString());
                map.put("estado",estado.getText().toString());
                map.put("latitud",latitud.getText().toString());
                map.put("longitud",logitud.getText().toString());
                if (edtReferencia.getText().toString().equals("")){
                    //nada
                }
                else {
                    map.put("referencia",edtReferencia.getText().toString());
                }
                pedidosActualizadoAdmin.child(idAdminNumPedido).updateChildren(map);
                clickActualizacionPedidoAukdeliver();
                mDialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), ListaDePedidos.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                Toasty.success(EditarPedido.this, "PEDIDO ACTUALIZADO!", Toast.LENGTH_LONG, true).show();
            }
            else {

                Toasty.warning(EditarPedido.this, "Agrege el NÚMERO DE PEDIDO", Toast.LENGTH_LONG, true).show();
            }
        }

        else {
            mDialog.dismiss();
            Toasty.warning(EditarPedido.this, "Verifique que los campos no estén vacios", Toast.LENGTH_LONG, true).show();
        }

    }



    private void clickActualizacionPedidoAukdeliver(){
        final String StAukdeliver = idAukdeliver.getText().toString();
        final String keyPedido =   IDPedidoAukdeliver.getText().toString();
        final String dataNumPedido = edtNumPedido.getText().toString();

        mDatabase.child("Usuarios").child("Aukdeliver").child(StAukdeliver).child("pedidos").child(keyPedido).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Query reference= FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Aukdeliver").child(StAukdeliver).child("pedidos").orderByChild("numPedido").equalTo(dataNumPedido);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                                Map<String, Object> map = new HashMap<>();
                                map.put("numPedido", edtNumPedido.getText().toString());
                                map.put("horaEntrega", horaEntrega.getText().toString());
                                map.put("fechaEntrega", fechaEntrega.getText().toString());
                                map.put("proveedores",textSocio.getText().toString());
                                map.put("productos",txtProducto.getText().toString());
                                map.put("descripcion",txtDescripcion.getText().toString());
                                map.put("precioUnitario",txtPrecioUnitario.getText().toString());
                                map.put("cantidad",txtCantidad.getText().toString());
                                map.put("precioTotalXProducto",txtPtotal.getText().toString());
                                map.put("comision",txtPrecioComisionProducto.getText().toString());
                                map.put("totalDelivery",txtDelivery.getText().toString());
                                map.put("gananciaDelivery",txtGananciaPorDelivery.getText().toString());
                                map.put("gananciaComision",txtNetoComision.getText().toString());
                                map.put("totalPagoProducto",precioProductoTotal.getText().toString());
                                map.put("nombreCliente", edtNombreCliente.getText().toString());
                                map.put("telefono",edtTelefono.getText().toString());
                                map.put("conCuantoVaAPagar",edtMontoCliente.getText().toString());
                                map.put("totalCobro",precioNetoTotal.getText().toString());
                                map.put("vuelto",vuelto.getText().toString());
                                map.put("direccion",edtDireccion.getText().toString());
                                map.put("estado",estado.getText().toString());
                                map.put("latitud",latitud.getText().toString());
                                map.put("longitud",logitud.getText().toString());
                                if (edtReferencia.getText().toString().equals(""))
                                {
                                //nada
                                }
                                else {
                                map.put("referencia",edtReferencia.getText().toString());
                                     }
                                pedidoParaAukdeliver.child("Usuarios").child("Aukdeliver").child(StAukdeliver).child("pedidos").child(keyPedido).updateChildren(map);

                                    }
                        else {
                            //Toast.makeText(EditarPedido.this, "No existe este pedido", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditarPedido.this, "Error de base de datos", Toast.LENGTH_LONG).show();
            }
        });

     }

    private void obtenerPedido(){
        String dataNumPedido = edtNumPedido.getText().toString();
        Query reference= FirebaseDatabase.getInstance().getReference().child("PedidosPorLlamada").child("pedidos").orderByChild("numPedido").equalTo(dataNumPedido);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    IDpedido.setText(key);
                    ObtenerDatosDePedido();
                    // Toast.makeText(DetallePedido.this, "Id : "+key, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void obtenerIDAukdeliver(){
        String nombreAukdeliver =  txtEncargado.getText().toString();
        Query reference= FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Aukdeliver").orderByChild("nombres").equalTo(nombreAukdeliver);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    idAukdeliver.setText(key);
                    obtenerPedidoAukdeliver();
                    //Toast.makeText(EditarPedido.this, key, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void obtenerPedidoAukdeliver(){
        String dataNumPedido = edtNumPedido.getText().toString();
        String IDAukdeliverx = idAukdeliver.getText().toString();
        Query reference= FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Aukdeliver").child(IDAukdeliverx).child("pedidos").orderByChild("numPedido").equalTo(dataNumPedido);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    IDPedidoAukdeliver.setText(key);
                    //Toast.makeText(EditarPedido.this, key, Toast.LENGTH_SHORT).show();
                }
                }
                else {
                    //Toast.makeText(EditarPedido.this, "No existe este pedido", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void ObtenerDatosDePedido(){
        String idPedidoData = IDpedido.getText().toString();
        mDatabase.child("PedidosPorLlamada").child("pedidos").child(idPedidoData).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String Socio = dataSnapshot.child("proveedores").getValue().toString();
                    String Producto = dataSnapshot.child("productos").getValue().toString();
                    String Nota = dataSnapshot.child("descripcion").getValue().toString();
                    String PrecioUnitario = dataSnapshot.child("precioUnitario").getValue().toString();
                    String Cantidad = dataSnapshot.child("cantidad").getValue().toString();
                    String PrecioTotal = dataSnapshot.child("precioTotalXProducto").getValue().toString();
                    String Comision = dataSnapshot.child("comision").getValue().toString();
                    String Delivery = dataSnapshot.child("totalDelivery").getValue().toString();
                    String TotalPagoProducto = dataSnapshot.child("totalPagoProducto").getValue().toString();
                    String GDelivery = dataSnapshot.child("gananciaDelivery").getValue().toString();
                    String GComision = dataSnapshot.child("gananciaComision").getValue().toString();
                    String TotalCobrar = dataSnapshot.child("totalCobro").getValue().toString();
                    String MontoPagoCliente = dataSnapshot.child("conCuantoVaAPagar").getValue().toString();
                    String Vuelto = dataSnapshot.child("vuelto").getValue().toString();
                    String Direccion = dataSnapshot.child("direccion").getValue().toString();
                    String nombreClientex = dataSnapshot.child("nombreCliente").getValue().toString();
                    String Telefono = dataSnapshot.child("telefono").getValue().toString();
                    String Aukdeliver = dataSnapshot.child("encargado").getValue().toString();
                    String LatitudX = dataSnapshot.child("latitud").getValue().toString();
                    String LongitudX = dataSnapshot.child("longitud").getValue().toString();
                    String Estado = dataSnapshot.child("estado").getValue().toString();

                    edtNombreCliente.setText(nombreClientex);
                    textSocio.setText(Socio);
                    txtProducto.setText(Producto);
                    txtDescripcion.setText(Nota);
                    txtPrecioUnitario.setText(PrecioUnitario);
                    txtCantidad.setText(Cantidad);
                    txtPtotal.setText(PrecioTotal);
                    txtPrecioComisionProducto.setText(Comision);
                    txtDelivery.setText(Delivery);
                    txtNeto.setText(TotalPagoProducto);
                    txtGananciaPorDelivery.setText(GDelivery);
                    txtNetoComision.setText(GComision);
                    precioNetoTotal.setText(TotalCobrar);
                    edtTelefono.setText(Telefono);
                    edtMontoCliente.setText(MontoPagoCliente);
                    vuelto.setText(Vuelto);
                    edtDireccion.setText(Direccion);
                    txtEncargado.setText(Aukdeliver);
                    latitud.setText(LatitudX);
                    logitud.setText(LongitudX);
                    estado.setText(Estado);

                    if (dataSnapshot.hasChild("referencia"))
                    {
                        String Referencia = dataSnapshot.child("referencia").getValue().toString();
                        edtReferencia.setText(Referencia);
                    }
                    else{
                        //nada
                    }

                    obtenerIDAukdeliver();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            Toasty.error(EditarPedido.this,"Error de peticion",Toast.LENGTH_SHORT,true).show();
            }
        });
    }

    private void obtenerProveedor(){
        final List<aukde.food.gestordepedidos.paquetes.Modelos.Spinner> proveedor = new ArrayList<>();
        mUsuarioProveedor.child("Usuarios").child("Proveedor").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds : dataSnapshot.getChildren() ){
                        String id = ds.getKey();
                        String nombres = ds.child("nombre empresa").getValue().toString();
                        proveedor.add(new aukde.food.gestordepedidos.paquetes.Modelos.Spinner(id,nombres));
                    }

                    final ArrayAdapter<aukde.food.gestordepedidos.paquetes.Modelos.Spinner> arrayAdapter
                            = new ArrayAdapter<>(EditarPedido.this , R.layout.custom_spinner,proveedor);
                    mSpinnerProveedor.setAdapter(arrayAdapter);
                    arrayAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
                    mSpinnerProveedor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(parent.getItemAtPosition(position).toString().equals("-")){
                                mSocio.setText("");
                            }
                            else{
                                String stProveedor = parent.getItemAtPosition(position).toString();
                                mSocio.setText(stProveedor);
                            }


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditarPedido.this,R.style.ThemeOverlay);
        builder.setTitle("Alerta!");
        builder.setIcon(R.drawable.ic_atras);
        builder.setCancelable(false);
        builder.setMessage("Descartar cambios? ");
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