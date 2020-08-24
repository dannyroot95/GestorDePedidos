package aukde.food.gestordepedidos.paquetes.Actividades.Pedidos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import org.json.JSONArray;
import org.json.JSONObject;
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
import aukde.food.gestordepedidos.paquetes.Modelos.FCMBody;
import aukde.food.gestordepedidos.paquetes.Modelos.FCMResponse;
import aukde.food.gestordepedidos.paquetes.Modelos.PedidoLlamada;
import aukde.food.gestordepedidos.paquetes.Providers.GoogleApiProvider;
import aukde.food.gestordepedidos.paquetes.Providers.NotificationProvider;
import aukde.food.gestordepedidos.paquetes.Providers.PedidoProvider;
import aukde.food.gestordepedidos.paquetes.Providers.TokenProvider;
import aukde.food.gestordepedidos.paquetes.Utils.DecodePoints;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RealizarPedido extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMarkerDragListener {

    private ProgressDialog mDialog;
    SimpleDateFormat simpleDateFormatHora = new SimpleDateFormat("HH:mm");
    SimpleDateFormat simpleDateFormatFecha = new SimpleDateFormat("dd/MM/yyy");
    int dia, mes, year, horaRel, minutoRel;
    String formatoHora = simpleDateFormatHora.format(new Date());
    String formatoFecha = simpleDateFormatFecha.format(new Date());

    TextView horaPedido, fechaPedido, fechaEntrega , horaEntrega
            , precioProductoTotal, precioNetoTotal, vuelto;

    private Button  btnCalcularVuelto;

    private Button mBtnAdd , mbtnMin , mbtnMax , mBtnClean;

    private TextView edtNumPedido,textSocio,txtProducto,txtDescripcion,txtPrecioUnitario,txtDelivery , edtCantidad , txtCantidad ,
            txtPtotal , txtNeto , txtGananciaPorDelivery , txtPrecioComisionProducto , txtNetoComision;


    private CheckBox chComision;

    public EditText  edtMontoCliente , edtNombreCliente, edtTelefono , edtDireccion ;

    private EditText mSocio , mProducto , mDescripcion , mPrecioUnitario , mDelivery ,mNuevaComision  ;

    private LinearLayout mLinearMap;
    PedidoProvider mpedidoProvider;

    private MapView mapView;
    private GoogleMap mMap;
    private Geocoder geocoder;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final String TAG = "RealizarPedido";
    private GoogleApiProvider mGoogleapiProvider;
    private List<LatLng> mPolylineList;
    private PolylineOptions mPolylineOptions;

    private LatLng origen;
    private LatLng destino;

    //variables de poscicion actual
    private LocationRequest mLocationRequest;
    public static final int LOCATION_REQUEST_CODE = 1;
    public static final int SETTINGS_REQUEST_CODE = 2;
    private FusedLocationProviderClient mFusedLocation;
    DatabaseReference mUsuarioAukdeliver;
    DatabaseReference pedidos;
    private DatabaseReference pedidoParaAukdeliver;
    Spinner mSpinner, mSpinnerEstado;
    FloatingActionButton mFloatingButton , mFloatingMap;
    TextView estado ;
    TextView txtEncargado , idAukdeliver;
    String stEncargado = "";
    TextView latitud,logitud;
    Button mButtonMapear;

    private NotificationProvider notificationProvider;
    private TokenProvider tokenProvider;
    private FirebaseAuth mAuth ;

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                origen = new LatLng(location.getLatitude(),location.getLongitude());
                if (getApplicationContext() != null) {
                    Toast.makeText(RealizarPedido.this, "Tu poscicion : "+origen, Toast.LENGTH_LONG).show();
                    //obtener locatizacion en tiempo real
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                    .zoom(15f)
                                    .build()
                    ));
                }
            }
        }
    };

    //----------------------------

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realizar_pedido);
        mAuth = FirebaseAuth.getInstance();
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
        edtNumPedido.setEnabled(false);

        // nuevo

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

        final androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(RealizarPedido.this,R.style.ThemeOverlay).create();
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
                mSocio.requestFocus();
            }
        });


        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    Toasty.error(RealizarPedido.this, "Complete los Campos", Toast.LENGTH_SHORT).show();
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
                textSocio.setText(" ");
                txtProducto.setText(" ");
                txtDescripcion.setText(" ");
                txtPrecioUnitario.setText(" ");
                txtDelivery.setText(" ");
                txtCantidad.setText(" ");
                txtPtotal.setText(" ");
                txtNeto.setText("0");
                txtGananciaPorDelivery.setText("0");
                txtPrecioComisionProducto.setText(" ");
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
        txtEncargado = findViewById(R.id.txtRepartidor);
        idAukdeliver = findViewById(R.id.txtIdAukdeliver);
        pedidoParaAukdeliver = FirebaseDatabase.getInstance().getReference();
        notificationProvider = new NotificationProvider();
        tokenProvider = new TokenProvider();

        latitud = findViewById(R.id.txtLatitud);
        logitud = findViewById(R.id.txtLongitud);
        mButtonMapear = findViewById(R.id.btnMapear);


        Query ultimoDato = FirebaseDatabase.getInstance().getReference().child("PedidosPorLlamada").child("pedidos").orderByKey().limitToLast(1);
        ultimoDato.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String num = childSnapshot.child("numPedido").getValue().toString();
                    int numToString = Integer.parseInt(num);
                    int newNumPedido = numToString + 1;
                    String stNewNumPedido = String.valueOf(newNumPedido);
                    edtNumPedido.setText(stNewNumPedido);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        int alto = 0;
        mLinearMap =findViewById(R.id.map_container);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,alto);
        mLinearMap.setLayoutParams(params);

        mpedidoProvider = new PedidoProvider();


        mSpinner = findViewById(R.id.spinnerAukdeliver);
        mSpinnerEstado = findViewById(R.id.spEstado);
        ArrayAdapter<CharSequence> adapterSpinnerEstado = ArrayAdapter.createFromResource(this,R.
                array.estado,R.layout.custom_spinner);
        estado = findViewById(R.id.txtEstado);

        adapterSpinnerEstado.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        mSpinnerEstado.setAdapter(adapterSpinnerEstado);
        mSpinnerEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                estado.setText(parent.getItemAtPosition(position).toString());
                String stSpinnerEstado = estado.getText().toString();
                if (stSpinnerEstado.equals("En espera")){
                    estado.setTextColor(Color.parseColor("#2E86C1"));
                }
                if (stSpinnerEstado.equals("Completado")){
                    estado.setTextColor(Color.parseColor("#5bbd00"));
                }
                if (stSpinnerEstado.equals("Cancelado")){
                    estado.setTextColor(Color.parseColor("#E74C3C"));
                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mUsuarioAukdeliver = FirebaseDatabase.getInstance().getReference();
        pedidos = FirebaseDatabase.getInstance().getReference("PedidosPorLlamada").child("pedidos");

        obtenerUsuarioAukdeliver();

        mFloatingButton = findViewById(R.id.floatRegister);
        mFloatingButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.quantum_googgreen)));
        mFloatingMap = findViewById(R.id.booleanMap);
        mFloatingMap.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.quantum_googred)));

        geocoder = new Geocoder(this);
        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        mGoogleapiProvider = new GoogleApiProvider(RealizarPedido.this);
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
                ClickCalcularVuelto();
            }
        });

        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RealizarPedido.this,R.style.ThemeOverlay);
                builder.setTitle("Confirmacion de pedido");
                builder.setCancelable(false);
                builder.setIcon(R.drawable.ic_correcto);
                builder.setMessage("Deseas guardar este pedido? ");
                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clickRegistroPedido();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Toast.makeText(RealizarPedido.this, "Pedido Cancelado", Toast.LENGTH_SHORT).show();
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
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,alto1);
                mLinearMap.setLayoutParams(params);
                mFloatingMap.setVisibility(View.INVISIBLE);
            }
        });

        mButtonMapear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFloatingMap.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                mLinearMap.setLayoutParams(params);
            }
        });

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

        DatePickerDialog datePickerDialog = new DatePickerDialog(RealizarPedido.this, new DatePickerDialog.OnDateSetListener() {
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

        TimePickerDialog PickerHora = new TimePickerDialog(RealizarPedido.this, new TimePickerDialog.OnTimeSetListener() {
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

    public void obtenerUsuarioAukdeliver(){
        final List<aukde.food.gestordepedidos.paquetes.Modelos.Spinner> aukdelivers = new ArrayList<>();
        mUsuarioAukdeliver.child("Usuarios").child("Aukdeliver").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds : dataSnapshot.getChildren() ){
                        String id = ds.getKey();
                        String nombres = ds.child("nombres").getValue().toString();
                        aukdelivers.add(new aukde.food.gestordepedidos.paquetes.Modelos.Spinner(id,nombres));
                    }

                    final ArrayAdapter<aukde.food.gestordepedidos.paquetes.Modelos.Spinner> arrayAdapter
                            = new ArrayAdapter<>(RealizarPedido.this , R.layout.custom_spinner,aukdelivers);
                    mSpinner.setAdapter(arrayAdapter);
                    arrayAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
                    mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            stEncargado = parent.getItemAtPosition(position).toString();
                            String idx = aukdelivers.get(position).getId();
                            txtEncargado.setText(stEncargado);
                            idAukdeliver.setText(idx);
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
    //obtener poscicion

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if(gpsActive()){
                        mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                    }
                    else { showAlertDialog(); }
                } else {
                    checkLocationPermision();
                }
            } else {
                checkLocationPermision();
            }
        }
    }

    // Gps Activo


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_REQUEST_CODE && gpsActive()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        }
        else {
            showAlertDialog();
        }
    }

    private void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Por favor activa tu ubicación para continuar")
                .setPositiveButton("Configuraciones", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),SETTINGS_REQUEST_CODE);
                    }
                }).create().show();
    }

    private boolean gpsActive(){
        boolean isActive = false;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            isActive = true;
        }

        return isActive;
    }

    //-----------
    //verificar version de android

    public void startLocacion(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                if(gpsActive())
                {
                    mFusedLocation.requestLocationUpdates(mLocationRequest,mLocationCallback, Looper.myLooper());
                    mMap.setMyLocationEnabled(true);
                }
                else{
                    showAlertDialog();
                }

            }

            else {
                checkLocationPermision();
            }

        }
        else {
            if(gpsActive()){
                mFusedLocation.requestLocationUpdates(mLocationRequest,mLocationCallback, Looper.myLooper());
                mMap.setMyLocationEnabled(true);

            }
            else {
                showAlertDialog();
            }

        }
    }

    //------------------------------

    public void checkLocationPermision(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            mFusedLocation.requestLocationUpdates(mLocationRequest,mLocationCallback, Looper.myLooper());

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                new AlertDialog.Builder(this)
                        .setTitle("Proporciona los permisos para continuar")
                        .setMessage("Esta aplicación requiere los permisos para continuar")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(RealizarPedido.this , new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();
            }
            else {
                ActivityCompat.requestPermissions(RealizarPedido.this , new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);
            }
        }
    }
    //----------

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
                AlertDialog.Builder builder = new AlertDialog.Builder(RealizarPedido.this);
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

    public void drawRoute(){

        Toast.makeText(this, "Tu poscicion : "+origen, Toast.LENGTH_LONG).show();

        mGoogleapiProvider.getDirections(origen , destino).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try{

                    JSONObject jsonObject = new JSONObject(response.body());
                    JSONArray jsonArray = jsonObject.getJSONArray("routes");
                    JSONObject route = jsonArray.getJSONObject(0);
                    JSONObject polylines = route.getJSONObject("overview_polyline");
                    String points = polylines.getString("points");

                    mPolylineList = DecodePoints.decodePoly(points);
                    mPolylineOptions = new PolylineOptions();
                    mPolylineOptions.color(Color.DKGRAY);
                    mPolylineOptions.width(13f);
                    mPolylineOptions.startCap(new SquareCap());
                    mPolylineOptions.jointType(JointType.ROUND);
                    mPolylineOptions.addAll(mPolylineList);
                    mMap.addPolyline(mPolylineOptions).setPoints(mPolylineList);
                    JSONArray legs = route.getJSONArray("legs");
                    JSONObject leg = legs.getJSONObject(0);

                }catch (Exception e){
                    Log.d("Error","Error encontrado"+ e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }

        });
    }

    private void sendNotificaction(){
        final String numPedNotify = edtNumPedido.getText().toString();
        tokenProvider.getToken(idAukdeliver.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String token = dataSnapshot.child("token").getValue().toString();
                    Map<String,String> map = new HashMap<>();
                    map.put("title","Nuevo pedido #"+numPedNotify);
                    map.put("body","Usted tiene un nuevo pedido"+"\n"+"Nombre del cliente : "
                            +edtNombreCliente.getText().toString()+"\n"+"Teléfono : "+edtTelefono.getText().toString());
                    map.put("idClient",mAuth.getUid());
                    map.put("numPedido",numPedNotify);
                    map.put("nombre",edtNombreCliente.getText().toString());
                    map.put("telefono",edtTelefono.getText().toString());
                    map.put("direccion",edtDireccion.getText().toString());
                    map.put("hora",horaEntrega.getText().toString());
                    map.put("fecha",fechaEntrega.getText().toString());
                    map.put("ganancia",txtGananciaPorDelivery.getText().toString());
                    map.put("repartidor",txtEncargado.getText().toString());
                    FCMBody fcmBody = new FCMBody(token,"high",map);
                    notificationProvider.sendNotificacion(fcmBody).enqueue(new Callback<FCMResponse>() {
                        @Override
                        public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                            if(response.body() !=null){
                                if(response.body().getSuccess() == 1){
                                    //Toast.makeText(RealizarPedido.this, "Notificación enviada", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(RealizarPedido.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(RealizarPedido.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<FCMResponse> call, Throwable t) {
                            Log.d("Error","Error encontrado"+ t.getMessage());
                        }
                    });
                }

                else {
                    Toast.makeText(RealizarPedido.this, "No existe token se sesión", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void clickRegistroPedido(){

        final String numeroPedido = edtNumPedido.getText().toString();
        final String stHoraPedido = horaPedido.getText().toString();
        final String stFechaPedido = fechaPedido.getText().toString();
        final String stHoraEntrega = horaEntrega.getText().toString();
        final String stFechaEntrega= fechaEntrega.getText().toString();
        final String producto = txtProducto.getText().toString();
        final String proveedor = textSocio.getText().toString();
        final String descripción = txtDescripcion.getText().toString();
        final String sTPrecioUnitario = txtPrecioUnitario.getText().toString();
        final String stCantidad = txtCantidad.getText().toString();
        final String stPrecioTotalXProducto = txtPtotal.getText().toString();
        final String stComision = txtPrecioComisionProducto.getText().toString();
        final String stTotalDelivery = txtDelivery.getText().toString();
        final String stGananciaComision = txtNetoComision.getText().toString();
        final String stGananciaDelivery = txtGananciaPorDelivery.getText().toString();
        final String totalPagoProducto = precioProductoTotal.getText().toString();
        final String telefono = edtTelefono.getText().toString();
        final String totalCobro = precioNetoTotal.getText().toString();
        final String stVuelto = vuelto.getText().toString();
        final String nombreCliente = edtNombreCliente.getText().toString();
        final String telefonoCliente = edtTelefono.getText().toString();
        final String conCuantoVaAPagar = edtMontoCliente.getText().toString();
        final String direccion = edtDireccion.getText().toString();
        final String encargado = txtEncargado.getText().toString();
        final String estadoPedido = estado.getText().toString();
        final String sTlatitud = latitud.getText().toString();
        final String sTLongitud = logitud.getText().toString();

        if( !nombreCliente.isEmpty() && !telefonoCliente.isEmpty() && !conCuantoVaAPagar.isEmpty() && !direccion.isEmpty()){
            sendNotificaction();
            if(!numeroPedido.isEmpty()){
                mDialog.show();
                mDialog.setMessage("Registrando pedido...");
                //metodos
                registrarPedido(stHoraPedido , stFechaPedido, stHoraEntrega, stFechaEntrega, proveedor,
                        producto, descripción , sTPrecioUnitario, stCantidad , stPrecioTotalXProducto , stComision , stTotalDelivery, stGananciaDelivery, stGananciaComision ,
                        totalPagoProducto , nombreCliente , telefono , conCuantoVaAPagar , totalCobro , stVuelto, direccion, numeroPedido , encargado , estadoPedido , sTlatitud , sTLongitud);

                clickRegistroPedidoAukdeliver();
            }
            else {

                Toasty.warning(RealizarPedido.this, "Agrege el NÚMERO DE PEDIDO", Toast.LENGTH_LONG, true).show();
            }
        }

        else {
            mDialog.dismiss();
            Toasty.warning(RealizarPedido.this, "Verifique que los campos no estén vacios", Toast.LENGTH_LONG, true).show();
        }

    }

    private void registrarPedido(final String horaPedido, final String fechaPedido,
                                 final String horaEntrega, final String fechaEntrega, final String proveedor,
                                 final String producto, final String descripción, final String sTPrecioUnitario,
                                 final String stCantidad , final String stPrecioTotalXProducto, final String stComision,
                                 final String stTotalDelivery, final String stGananciaDelivery, final String stGananciaComision, final String totalPagoProducto,
                                 final String nombreCliente, final String telefono, final String conCuantoVaAPagar,
                                 final String totalCobro, final String stVuelto, final String direccion,final String numPedido , final String encargado ,final String estadoPedido,final String latitud , final String longitud){



        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        PedidoLlamada pedidoLlamada = new  PedidoLlamada(id,horaPedido,  fechaPedido, horaEntrega,  fechaEntrega,  proveedor,
                producto,  descripción,sTPrecioUnitario,stCantidad,stPrecioTotalXProducto,stComision,stTotalDelivery,stGananciaDelivery,stGananciaComision,
                totalPagoProducto, nombreCliente,  telefono,  conCuantoVaAPagar, totalCobro,  stVuelto,  direccion,numPedido,encargado ,estadoPedido,latitud,longitud);
        mapear(pedidoLlamada);
    }

    void mapear(PedidoLlamada pedidoLlamada){

        mpedidoProvider.Mapear(pedidoLlamada).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    mDialog.dismiss();
                    startActivity(new Intent( RealizarPedido.this , MenuAdmin.class));
                    finish();
                    Toasty.success(RealizarPedido.this, "REGISTRO EXITOSO", Toast.LENGTH_SHORT, true).show();
                }

                else {
                    mDialog.dismiss();
                    Toasty.warning(RealizarPedido.this, "No se pudo registar el pedido", Toast.LENGTH_LONG, true).show();
                }

            }
        });

    }

    private void clickRegistroPedidoAukdeliver(){

        String StAukdeliver = idAukdeliver.getText().toString();

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

        pedidoParaAukdeliver.child("Usuarios").child("Aukdeliver").child(StAukdeliver).child("pedidos").push().setValue(map);
    }



    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(RealizarPedido.this,R.style.ThemeOverlay);
        builder.setTitle("Alerta!");
        builder.setIcon(R.drawable.ic_atras);
        builder.setCancelable(false);
        builder.setMessage("Deseas Salir? ");
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(RealizarPedido.this,MenuAdmin.class));
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