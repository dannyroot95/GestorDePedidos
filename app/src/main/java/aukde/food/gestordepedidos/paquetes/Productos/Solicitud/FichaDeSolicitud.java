package aukde.food.gestordepedidos.paquetes.Productos.Solicitud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import aukde.food.gestordepedidos.R;
import es.dmoral.toasty.Toasty;

public class FichaDeSolicitud extends AppCompatActivity implements OnMapReadyCallback{

    Spinner mSpinner;
    DatabaseReference mProducto;
    EditText edtProducto , edtPrecio;
    TextView edtCantidad;
    FirebaseAuth mAuth;
    private Button mBtnAdd, mbtnMin, mbtnMax, mBtnClean;
    public EditText  edtNombreCliente, edtTelefono, edtReferencia;
    Button  edtDireccion;
    private TextView txtNumSolicitud,txtCantidad,txtProducto, txtPrecioUnitario,txtPtotal , txtNeto;
    private Vibrator vibrator;
    long tiempo = 100;

    private MapView mapView;
    private GoogleMap mMap;
    private Geocoder geocoder;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final String TAG = "SolicitarDelivery";
    public static final int LOCATION_REQUEST_CODE = 1;
    public static final int SETTINGS_REQUEST_CODE = 2;
    private FusedLocationProviderClient mFusedLocation;
    private LinearLayout mLinearMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha_de_solicitud);
        mAuth = FirebaseAuth.getInstance();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        txtNumSolicitud = findViewById(R.id.numSolicitud);

        edtProducto = findViewById(R.id.edtProductoSolicitud);
        edtProducto.setEnabled(false);
        edtPrecio = findViewById(R.id.edtPrecUnitarioSolicitud);
        edtCantidad = findViewById(R.id.edtCant);
        edtDireccion = findViewById(R.id.edtDireccionSolicitud);

        edtPrecio.setEnabled(false);
        mProducto = FirebaseDatabase.getInstance().getReference();
        mSpinner = findViewById(R.id.spinnerProducto);

        mBtnAdd = findViewById(R.id.add);
        mbtnMin = findViewById(R.id.btnMin);
        mbtnMax = findViewById(R.id.btnMax);
        mBtnClean = findViewById(R.id.btnClean);

        txtProducto = findViewById(R.id.lsProducto);
        txtPrecioUnitario = findViewById(R.id.lsPUnitario);
        txtCantidad = findViewById(R.id.lsCant);
        txtPtotal = findViewById(R.id.lsPTotal);
        txtNeto = findViewById(R.id.txtTotalProductoSolicitud);
        mLinearMap = findViewById(R.id.map_container);
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        mLinearMap.setLayoutParams(params);

        geocoder = new Geocoder(this);
        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);


        checkUltimaSolicitud();
        getProduct();

        mbtnMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                String num = edtCantidad.getText().toString();
                if (num.equals("1")) {
                    edtCantidad.setText("1");
                } else {
                    int Cc = Integer.parseInt(num);
                    int res = Cc - 1;
                    String Min = String.valueOf(res);
                    edtCantidad.setText(Min);
                }

            }
        });

        mbtnMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                String num = edtCantidad.getText().toString();
                int Cc = Integer.parseInt(num);
                int res = Cc + 1;
                String Min = String.valueOf(res);
                edtCantidad.setText(Min);
            }
        });

        mBtnClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                txtProducto.setText("");
                txtPrecioUnitario.setText("");
                txtCantidad.setText("");
                txtPtotal.setText("");
                txtNeto.setText("0");

            }
        });

        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                String P = edtProducto.getText().toString();
                String Pu = edtPrecio.getText().toString();
                String Cant = edtCantidad.getText().toString();
                String netoValor = txtNeto.getText().toString();
                Double dNeto = Double.parseDouble(netoValor);
                if ( P.isEmpty() || Pu.isEmpty()) {
                    Toasty.error(FichaDeSolicitud.this, "Complete los Campos", Toast.LENGTH_SHORT).show();
                } else {
                    txtProducto.append(P + "\n");
                    txtPrecioUnitario.append(Pu + "\n");
                    txtCantidad.append(Cant + "\n");
                    int Cantidad = Integer.parseInt(Cant);
                    Double Precio = Double.parseDouble(Pu);
                    Double total = Cantidad * Precio;
                    String sTotal = String.valueOf(total);
                    txtPtotal.append(sTotal + "\n");

                    Double finalNeto = total + dNeto;
                    String stNeto = String.valueOf(finalNeto);
                    txtNeto.setText(stNeto);


                }
            }
        });

        edtDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toasty.info(FichaDeSolicitud.this, "Seleccione una zona de su envío!", Toast.LENGTH_LONG).show();
                CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mLinearMap.setLayoutParams(params);
            }
        });

        checkDirecctions();

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

    public void getProduct(){
        final List<aukde.food.gestordepedidos.paquetes.Modelos.Spinner> proveedor = new ArrayList<>();
        mProducto.child("Usuarios").child("Proveedor").child(mAuth.getUid()).child("Productos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String id = ds.getKey();
                        String nombres = ds.child("nombreProducto").getValue().toString();
                        proveedor.add(new aukde.food.gestordepedidos.paquetes.Modelos.Spinner(id, nombres));
                    }

                    final ArrayAdapter<aukde.food.gestordepedidos.paquetes.Modelos.Spinner> arrayAdapter
                            = new ArrayAdapter<>(FichaDeSolicitud.this, R.layout.custom_spinner, proveedor);
                    mSpinner.setAdapter(arrayAdapter);
                    arrayAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
                    mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String stProducto = parent.getItemAtPosition(position).toString();
                            edtProducto.setText(stProducto);
                            getDataProduct();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            edtProducto.setText("");
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getDataProduct(){
        Query ultimoDato = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Proveedor")
                .child(mAuth.getUid()).child("Productos").orderByChild("nombreProducto").equalTo(edtProducto.getText().toString());
        ultimoDato.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String num = childSnapshot.child("tarifaPublicada").getValue().toString();
                    edtPrecio.setText(num);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void checkUltimaSolicitud(){
        Query ultimoDato = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Proveedor")
                .child(mAuth.getUid()).child("Solicitudes").orderByKey().limitToLast(1);
        ultimoDato.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String num = childSnapshot.child("numPedido").getValue().toString();
                        int numToString = Integer.parseInt(num);
                        int newNumPedido = numToString + 1;
                        String stNewNumPedido = String.valueOf(newNumPedido);
                        txtNumSolicitud.setText(stNewNumPedido);
                    }
                }
                else {
                    txtNumSolicitud.setText("0");
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void checkDirecctions(){

        String [] joya = {"joya","JOYA","Joya"};

       String dir = edtDireccion.getText().toString();
       if (dir.contains("mayo")){
           Toast.makeText(this, "delivery 3 lucas", Toast.LENGTH_SHORT).show();
       }
       else if (dir.contains("agosto")){
           Toast.makeText(this, "delivery 4 lucas", Toast.LENGTH_SHORT).show();
       }

       for (final String c1 : joya){
          if (dir.contains(c1)){
                Toast.makeText(this, "delivery 8 lucas", Toast.LENGTH_SHORT).show();
            }
          }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        try {

            List<Address> addresses = geocoder.getFromLocation(-12.594210, -69.191407, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                LatLng aukde = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(aukde, 14));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Polygon polygon1 = googleMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(
                        new LatLng(-12.593482, -69.174982),
                        new LatLng(-12.592228, -69.176685),
                        new LatLng(-12.594270, -69.178766),
                        new LatLng(-12.582445, -69.192109),
                        new LatLng(-12.582131, -69.192914),
                        new LatLng(-12.582466, -69.193675),
                        new LatLng(-12.582487, -69.194373),
                        new LatLng(-12.580812, -69.202741),
                        new LatLng(-12.580856, -69.203307),
                        new LatLng(-12.599560, -69.201145),
                        new LatLng(-12.599644, -69.197218),
                        new LatLng(-12.602586, -69.197165),
                        new LatLng(-12.605716, -69.195877),
                        new LatLng(-12.603277, -69.190309),
                        new LatLng(-12.605580, -69.187659),
                        new LatLng(-12.596691, -69.177853),
                        new LatLng(-12.594942, -69.174924),
                        new LatLng(-12.594277, -69.175627),
                        new LatLng(-12.593471, -69.174972)));

        polygon1.setTag("ZONA A");
        polygon1.setFillColor(0x35FD7826);
        polygon1.setStrokeColor(0x35FD7826);
        polygon1.setStrokeWidth(5);

        int height = 80;
        int width = 200;
        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.zonaa);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        LatLng zona1 = new LatLng(-12.594895, -69.191054);
        googleMap.addMarker(new MarkerOptions().position(zona1)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));


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
    public void onBackPressed() {
        finish();
    }
}