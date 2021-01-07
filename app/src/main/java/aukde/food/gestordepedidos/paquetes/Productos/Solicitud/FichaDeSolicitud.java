package aukde.food.gestordepedidos.paquetes.Productos.Solicitud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.gms.maps.model.Marker;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Actividades.Pedidos.SolicitarProducto;
import aukde.food.gestordepedidos.paquetes.Menus.MenuProveedor;
import aukde.food.gestordepedidos.paquetes.Modelos.FCMBody;
import aukde.food.gestordepedidos.paquetes.Modelos.FCMResponse;
import aukde.food.gestordepedidos.paquetes.Productos.MenuAddProduct;
import aukde.food.gestordepedidos.paquetes.Productos.Pizza.AgregarProductoPizza;
import aukde.food.gestordepedidos.paquetes.Providers.NotificationProvider;
import aukde.food.gestordepedidos.paquetes.Providers.TokenProvider;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FichaDeSolicitud extends AppCompatActivity implements OnMapReadyCallback , GoogleMap.OnMarkerClickListener {

    Spinner mSpinner;
    DatabaseReference mProducto;
    EditText edtProducto , edtPrecio;
    TextView edtCantidad , mTxtStock , mIDProducto;
    FirebaseAuth mAuth;
    private Button mBtnAdd, mbtnMin, mbtnMax, mBtnClean;
    public EditText  edtNombreCliente, edtTelefono, edtReferencia , edtSolDireccion;
    Button  edtDireccion;
    private TextView txtNumSolicitud,txtCantidad,txtProducto, txtPrecioUnitario,txtPtotal , txtNeto;
    private TextView txtCostoDelivery,txtCostoDeliveryB,txtCostoDeliveryC,txtCostoDeliveryD,txtCostoDeliveryE,txtCostoDeliveryF
            ,txtCostoDeliveryG,txtCostoDeliveryH,txtCostoDeliveryI,txtCostoDeliveryJ , txtCostoDeliveryConfirmacion;
    private Button btnZonaA,btnZonaB,btnZonaC,btnZonaD,btnZonaE,btnZonaF,btnZonaG,btnZonaH,btnZonaI,btnZonaJ;
    private Vibrator vibrator;
    long tiempo = 100;
    private Marker markerA,markerB,markerC,markerD,markerE,markerF,markerG,markerH,markerI,markerJ;
    private Dialog zonaA,zonaB,zonaC,zonaD,zonaE,zonaF,zonaG,zonaH,zonaI,zonaJ,dialogConfirm;
    private Button cerrarPopupZonaA,cerrarPopupZonaB,cerrarPopupZonaC,cerrarPopupZonaD,cerrarPopupZonaE,cerrarPopupZonaF
            ,cerrarPopupZonaG,cerrarPopupZonaH,cerrarPopupZonaI,cerrarPopupZonaJ;
    private ImageView cerrarImgZonaA,cerrarImgZonaB,cerrarImgZonaC,cerrarImgZonaD,cerrarImgZonaE,cerrarImgZonaF,cerrarImgZonaG
            ,cerrarImgZonaH,cerrarImgZonaI,cerrarImgZonaJ,cerrarConfirmacion;

    private Button btnConfirm,btnCloseMap;

    private MapView mapView;
    private GoogleMap mMap;
    private Geocoder geocoder;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final String TAG = "SolicitarDelivery";
    public static final int LOCATION_REQUEST_CODE = 1;
    public static final int SETTINGS_REQUEST_CODE = 2;
    private FusedLocationProviderClient mFusedLocation;
    private LinearLayout mLinearMap;

    private TextView txtDeliverySolicitud;
    DatabaseReference mDatabase;
    private TokenProvider tokenProvider;
    private NotificationProvider notificationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeRedCake);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficha_de_solicitud);
        tokenProvider = new TokenProvider();
        notificationProvider = new NotificationProvider();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
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

        zonaA = new Dialog(this);
        zonaB = new Dialog(this);
        zonaC = new Dialog(this);
        zonaD = new Dialog(this);
        zonaE = new Dialog(this);
        zonaF = new Dialog(this);
        zonaG = new Dialog(this);
        zonaH = new Dialog(this);
        zonaI = new Dialog(this);
        zonaJ = new Dialog(this);
        dialogConfirm = new Dialog(this);

        txtProducto = findViewById(R.id.lsProducto);
        mTxtStock = findViewById(R.id.txtStock);
        mIDProducto = findViewById(R.id.txtIdProducto);
        txtPrecioUnitario = findViewById(R.id.lsPUnitario);
        txtCantidad = findViewById(R.id.lsCant);
        txtPtotal = findViewById(R.id.lsPTotal);
        txtNeto = findViewById(R.id.txtTotalProductoSolicitud);
        txtDeliverySolicitud = findViewById(R.id.txtPrecioDeliverySol);
        btnCloseMap = findViewById(R.id.CloseMap);
        btnCloseMap.setVisibility(View.INVISIBLE);


        Query ultimoDato = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Proveedor").child(mAuth.getUid()).child("Solicitudes").orderByKey().limitToLast(1);
        ultimoDato.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String num = childSnapshot.child("numSolicitud").getValue().toString();
                        int numToString = Integer.parseInt(num);
                        int newNumSolicitud = numToString + 1;
                        String stNewNumSolicitud = String.valueOf(newNumSolicitud);
                        txtNumSolicitud.setText(stNewNumSolicitud);
                    }
                }
                else{
                    txtNumSolicitud.setText("0");
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


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
                reduceStock();
                vibrator.vibrate(tiempo);
                String desc [] = txtProducto.getText().toString().split("\n");
                String desc2 [] = txtPrecioUnitario.getText().toString().split("\n");
                String desc3 [] = txtCantidad.getText().toString().split("\n");
                String desc4 [] = txtPtotal.getText().toString().split("\n");
                StringBuilder builder = new StringBuilder();
                StringBuilder builder2 = new StringBuilder();
                StringBuilder builder3 = new StringBuilder();
                StringBuilder builder4 = new StringBuilder();
                for (int i = 0 ; i<desc.length - 1 ; i++){
                    builder.append(desc[i]+"\n");
                }
                for (int j = 0 ; j<desc2.length - 1 ; j++){
                    builder2.append(desc2[j]+"\n");
                }
                for (int k = 0 ; k<desc3.length - 1 ; k++){
                    builder3.append(desc3[k]+"\n");
                }
                for (int l = 0 ; l<desc4.length - 1 ; l++){
                    builder4.append(desc4[l]+"\n");
                }
                String joined = builder.toString();
                String joined2 = builder2.toString();
                String joined3 = builder3.toString();
                String joined4 = builder4.toString();
                txtProducto.setText("");
                txtPrecioUnitario.setText("");
                txtCantidad.setText("");
                txtPtotal.setText("");
                txtProducto.append(joined);
                txtPrecioUnitario.append(joined2);
                txtCantidad.append(joined3);
                txtPtotal.append(joined4);
                subtractPriceList();
                //listReduceProduct();
            }
        });

        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                int stock = Integer.parseInt(edtCantidad.getText().toString());
                int cant = Integer.parseInt(mTxtStock.getText().toString());
                int res;
                if (!(stock > cant)) {
                    String P = edtProducto.getText().toString();
                    String Pu = edtPrecio.getText().toString();
                    String Cant = edtCantidad.getText().toString();
                    String netoValor = txtNeto.getText().toString();
                    Double dNeto = Double.parseDouble(netoValor);

                    if (P.isEmpty() || Pu.isEmpty()) {
                        Toasty.error(FichaDeSolicitud.this, "Complete los Campos", Toast.LENGTH_SHORT).show();
                    }

                    else {
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

                        res = cant - stock;
                        String newStock = String.valueOf(res);
                        mTxtStock.setText(newStock);
                    }
                }
                else {
                    Toasty.error(FichaDeSolicitud.this, "Stock Insuficiente", Toast.LENGTH_SHORT).show();
                }
            }
        });

        edtDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtProducto.getText().toString().equals("")) {
                    Toasty.info(FichaDeSolicitud.this, "Seleccione una zona de su envío!", Toast.LENGTH_LONG).show();
                    CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    mLinearMap.setLayoutParams(params);
                    btnCloseMap.setVisibility(View.VISIBLE);
                }
                else{
                    Toasty.info(FichaDeSolicitud.this, "Agrege productos a su lista!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCloseMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
                mLinearMap.setLayoutParams(params);
                btnCloseMap.setVisibility(View.INVISIBLE);
            }
        });

        //checkDirecctions();

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
                            edtCantidad.setText("1");
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
                    String id = childSnapshot.getKey();
                    String num = childSnapshot.child("tarifaPublicada").getValue().toString();
                    String stock = childSnapshot.child("stock").getValue().toString();
                    edtPrecio.setText(num);
                    mTxtStock.setText(stock);
                    mIDProducto.setText(id);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void subtractPriceList(){
        String desc [] = txtPtotal.getText().toString().split("\n");
        double sub = 0;
        for (int i = 0 ; i<desc.length ; i++){
            if(!desc[i].isEmpty()){
                sub += Double.parseDouble(desc[i]);
            }
            else{
                txtNeto.setText("0.0");
            }
        }
        String stSub = String.valueOf(sub);
        txtNeto.setText(stSub);
    }

    /*private void checkDirecctions(){

        String [] joya = {"joya","JOYA","Joya"};
       String dir = edtDireccion.getText().toString();
       if (dir.contains("mayo")){
           Toast.makeText(this, "delivery 3 lucas", Toast.LENGTH_SHORT).show();
       }
       else if (dir.contains("agosto")){
           Toast.makeText(this, "delivery 4 lucas", Toast.LENGTH_SHORT).show();
       }
       for (final String c1 : joya) {
           if (dir.contains(c1)) {
               Toast.makeText(this, "delivery 8 lucas", Toast.LENGTH_SHORT).show();
           }
       }
    }*/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnMarkerClickListener(this);
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
                        new LatLng(-12.593482, -69.174982), new LatLng(-12.592228, -69.176685), new LatLng(-12.594270, -69.178766),
                        new LatLng(-12.582445, -69.192109), new LatLng(-12.582131, -69.192914), new LatLng(-12.582466, -69.193675),
                        new LatLng(-12.582487, -69.194373), new LatLng(-12.580812, -69.202741), new LatLng(-12.580856, -69.203307),//--
                        new LatLng(-12.595631, -69.201218), new LatLng(-12.599560, -69.201145), new LatLng(-12.599644, -69.197218),
                        new LatLng(-12.602586, -69.197165), new LatLng(-12.605716, -69.195877), new LatLng(-12.603277, -69.190309),
                        new LatLng(-12.605580, -69.187659), new LatLng(-12.596691, -69.177853), new LatLng(-12.594942, -69.174924),
                        new LatLng(-12.594277, -69.175627), new LatLng(-12.593471, -69.174972)));

        polygon1.setTag("ZONA A");
        polygon1.setFillColor(0x35FD7826);
        polygon1.setStrokeColor(0x35FD7826);
        polygon1.setStrokeWidth(5);

        int height = 80;
        int width = 200;
        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.zonaa);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


        markerA = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(-12.594895, -69.191054))
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));

        //-----------------------------------------------------------------

        Polygon polygon2 = googleMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(
                        new LatLng(-12.580870, -69.203288), new LatLng(-12.581417, -69.206823), new LatLng(-12.582690, -69.208899),
                        new LatLng(-12.584417, -69.213292), new LatLng(-12.587360, -69.212300), new LatLng(-12.587265, -69.210937),
                        new LatLng(-12.589894, -69.210551), new LatLng(-12.590229, -69.212225), new LatLng(-12.590815, -69.212160),
                        new LatLng(-12.590648, -69.210798), new LatLng(-12.594302, -69.210347), new LatLng(-12.594229, -69.209102),
                        new LatLng(-12.595558, -69.209360), new LatLng(-12.596061, -69.208909), new LatLng(-12.597077, -69.208974),
                        new LatLng(-12.599003, -69.209585), new LatLng(-12.600406, -69.205111), new LatLng(-12.599710, -69.202283),
                        new LatLng(-12.599542, -69.201146), new LatLng(-12.595631, -69.201218), new LatLng(-12.592071, -69.201647),
                        new LatLng(-12.587139, -69.202323), new LatLng(-12.580870, -69.203288)));

        polygon2.setTag("ZONA B");
        polygon2.setFillColor(0x3A77C157);
        polygon2.setStrokeColor(0x3A77C157);
        polygon2.setStrokeWidth(5);

        BitmapDrawable bitmapdraw2 = (BitmapDrawable)getResources().getDrawable(R.drawable.zonab);
        Bitmap b2 = bitmapdraw2.getBitmap();
        Bitmap smallMarker2 = Bitmap.createScaledBitmap(b2, width, height, false);

        markerB = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(-12.590186, -69.206071))
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker2)));

        //-----------------------------------------------------------------

        Polygon polygon3 = googleMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(
                        new LatLng(-12.584417, -69.213292), new LatLng(-12.587360, -69.212300), new LatLng(-12.587265, -69.210937),
                        new LatLng(-12.589894, -69.210551), new LatLng(-12.590229, -69.212225), new LatLng(-12.590815, -69.212160),
                        new LatLng(-12.590648, -69.210798), new LatLng(-12.594302, -69.210347), new LatLng(-12.594229, -69.209102),
                        new LatLng(-12.595558, -69.209360), new LatLng(-12.596061, -69.208909), new LatLng(-12.597077, -69.208974),
                        new LatLng(-12.599003, -69.209585), new LatLng(-12.597694, -69.214043), new LatLng(-12.597401, -69.215481),
                        new LatLng(-12.597448, -69.219901), new LatLng(-12.588920, -69.219547), new LatLng(-12.588490, -69.219332),
                        new LatLng(-12.584689, -69.213914), new LatLng(-12.584417, -69.213292)));

        polygon3.setTag("ZONA C");
        polygon3.setFillColor(0x3F95326A);
        polygon3.setStrokeColor(0x3F95326A);
        polygon3.setStrokeWidth(5);


        BitmapDrawable bitmapdraw3 = (BitmapDrawable)getResources().getDrawable(R.drawable.zonac);
        Bitmap b3 = bitmapdraw3.getBitmap();
        Bitmap smallMarker3 = Bitmap.createScaledBitmap(b3, width, height, false);


        markerC = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(-12.595841, -69.215427))
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker3)));

        //-----------------------------------------------------------------

        Polygon polygon4 = googleMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(
                        new LatLng(-12.597448, -69.219901), new LatLng(-12.588920, -69.219547), new LatLng(-12.589082, -69.220121),
                        new LatLng(-12.591009, -69.222814), new LatLng(-12.592004, -69.224150), new LatLng(-12.592532, -69.225110), new LatLng(-12.593831, -69.228189), new LatLng(-12.599517, -69.228243),
                        new LatLng(-12.599391, -69.219960), new LatLng(-12.597433, -69.219853), new LatLng(-12.597448, -69.219901)));

        polygon4.setTag("ZONA D");
        polygon4.setFillColor(0x3A57A0C1);
        polygon4.setStrokeColor(0x3A57A0C1);
        polygon4.setStrokeWidth(5);

        BitmapDrawable bitmapdraw4 = (BitmapDrawable)getResources().getDrawable(R.drawable.zonad);
        Bitmap b4 = bitmapdraw4.getBitmap();
        Bitmap smallMarker4 = Bitmap.createScaledBitmap(b4, width, height, false);


        markerD = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(-12.597490, -69.224171))
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker4)));


        //-----------------------------------------------------------------

        Polygon polygon5 = googleMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(
                        new LatLng(-12.597403, -69.219835), new LatLng(-12.599437, -69.220154), new LatLng(-12.599814, -69.219790),
                        new LatLng(-12.602453, -69.219757), new LatLng(-12.606180, -69.219918), new LatLng(-12.606295, -69.218760),
                        new LatLng(-12.607143, -69.219489), new LatLng(-12.609513, -69.216470), new LatLng(-12.607985, -69.215188),
                        new LatLng(-12.608408, -69.214597), new LatLng(-12.607730, -69.213985), new LatLng(-12.605901, -69.212177),
                        new LatLng(-12.602624, -69.209023), new LatLng(-12.601566, -69.207928), new LatLng(-12.600707, -69.205664),
                        new LatLng(-12.600519, -69.205010), new LatLng(-12.600310, -69.205163), new LatLng(-12.597679, -69.213896),
                        new LatLng(-12.597386, -69.215398), new LatLng(-12.597403, -69.219835)));

        polygon5.setTag("ZONA E");
        polygon5.setFillColor(0x3AB1C157);
        polygon5.setStrokeColor(0x3AB1C157);
        polygon5.setStrokeWidth(5);


        BitmapDrawable bitmapdraw5 = (BitmapDrawable)getResources().getDrawable(R.drawable.zonae);
        Bitmap b5 = bitmapdraw5.getBitmap();
        Bitmap smallMarker5 = Bitmap.createScaledBitmap(b5, width, height, false);


        markerE = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(-12.604045, -69.214969))
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker5)));

        //-----------------------------------------------------------------

        Polygon polygon6 = googleMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(
                        new LatLng(-12.605981, -69.212245), new LatLng(-12.605981, -69.212245), new LatLng(-12.601584, -69.207959),
                        new LatLng(-12.600537, -69.205052), new LatLng(-12.599652, -69.202241), new LatLng(-12.599542, -69.201023),
                        new LatLng(-12.599647, -69.199607), new LatLng(-12.599657, -69.197161), new LatLng(-12.602254, -69.197128),
                        new LatLng(-12.602929, -69.197134), new LatLng(-12.603484, -69.196957), new LatLng(-12.604165, -69.196646),
                        new LatLng(-12.605112, -69.196195), new LatLng(-12.605756, -69.195900), new LatLng(-12.606138, -69.196651),
                        new LatLng(-12.606803, -69.197439), new LatLng(-12.606960, -69.197568), new LatLng(-12.606850, -69.198416),
                        new LatLng(-12.606934, -69.198834), new LatLng(-12.607562, -69.199714), new LatLng(-12.608096, -69.201795),
                        new LatLng(-12.611153, -69.205775), new LatLng(-12.611342, -69.207986), new LatLng(-12.607028, -69.212170),
                        new LatLng(-12.605981, -69.212245)));

        polygon6.setTag("ZONA F");
        polygon6.setFillColor(0x3F6EDCED);
        polygon6.setStrokeColor(0x3F6EDCED);
        polygon6.setStrokeWidth(5);

        BitmapDrawable bitmapdraw6 = (BitmapDrawable)getResources().getDrawable(R.drawable.zonaf);
        Bitmap b6 = bitmapdraw6.getBitmap();
        Bitmap smallMarker6 = Bitmap.createScaledBitmap(b6, width, height, false);


        markerF = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(-12.607656, -69.205035))
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker6)));


        //-----------------------------------------------------------------

        Polygon polygon7 = googleMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(
                        new LatLng(-12.607130, -69.219474), new LatLng(-12.609478, -69.216469), new LatLng(-12.607970, -69.215187),
                        new LatLng(-12.608400, -69.214591), new LatLng(-12.605981, -69.212245), new LatLng(-12.607028, -69.212170),
                        new LatLng(-12.611342, -69.207986), new LatLng(-12.611153, -69.205775), new LatLng(-12.615508, -69.207964),
                        new LatLng(-12.616765, -69.213457), new LatLng(-12.616137, -69.218263), new LatLng(-12.614126, -69.222598),
                        new LatLng(-12.611813, -69.221306), new LatLng(-12.610420, -69.222347), new LatLng(-12.607130, -69.219474)));

        polygon7.setTag("ZONA G");
        polygon7.setFillColor(0x3FED6E6E);
        polygon7.setStrokeColor(0x3FED6E6E);
        polygon7.setStrokeWidth(5);

        BitmapDrawable bitmapdraw7 = (BitmapDrawable)getResources().getDrawable(R.drawable.zonag);
        Bitmap b7 = bitmapdraw7.getBitmap();
        Bitmap smallMarker7 = Bitmap.createScaledBitmap(b7, width, height, false);

        markerG = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(-12.613991, -69.214815))
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker7)));

        //-----------------------------------------------------------------

        Polygon polygon8 = googleMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(
                        new LatLng(-12.608368, -69.198175), new LatLng(-12.611153, -69.197048), new LatLng(-12.610106, -69.193851),
                        new LatLng(-12.609237, -69.190246), new LatLng(-12.604693, -69.185246), new LatLng(-12.604442, -69.183616),
                        new LatLng(-12.603353, -69.181641), new LatLng(-12.599793, -69.176277), new LatLng(-12.597071, -69.172007),
                        new LatLng(-12.596254, -69.169990), new LatLng(-12.595228, -69.170473), new LatLng(-12.593631, -69.174421),
                        new LatLng(-12.593465, -69.174979), new LatLng(-12.594270, -69.175634), new LatLng(-12.594942, -69.174919),
                        new LatLng(-12.596690, -69.177863), new LatLng(-12.605551, -69.187671), new LatLng(-12.603269, -69.190295),
                        new LatLng(-12.605681, -69.195890), new LatLng(-12.606147, -69.196652), new LatLng(-12.606937, -69.197543),
                        new LatLng(-12.607728, -69.197950), new LatLng(-12.608368, -69.198175)));

        polygon8.setTag("ZONA H");
        polygon8.setFillColor(0x38000000);
        polygon8.setStrokeColor(0x38000000);
        polygon8.setStrokeWidth(5);


        BitmapDrawable bitmapdraw8 = (BitmapDrawable)getResources().getDrawable(R.drawable.zonah);
        Bitmap b8 = bitmapdraw8.getBitmap();
        Bitmap smallMarker8 = Bitmap.createScaledBitmap(b8, width, height, false);

        markerH = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(-12.602086, -69.180279))
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker8)));


        //-----------------------------------------------------------------

        Polygon polygon9 = googleMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(
                        new LatLng(-12.582930, -69.193009), new LatLng(-12.582637, -69.194007), new LatLng(-12.581360, -69.199618),
                        new LatLng(-12.580836, -69.202407), new LatLng(-12.580852, -69.203193), new LatLng(-12.572278, -69.202234), new LatLng(-12.574289, -69.192857),
                        new LatLng(-12.582930, -69.193009)));

        polygon9.setTag("ZONA I");
        polygon9.setFillColor(0x47543781);
        polygon9.setStrokeColor(0x47543781);
        polygon9.setStrokeWidth(5);

        BitmapDrawable bitmapdraw9 = (BitmapDrawable)getResources().getDrawable(R.drawable.zonai);
        Bitmap b9 = bitmapdraw9.getBitmap();
        Bitmap smallMarker9 = Bitmap.createScaledBitmap(b9, width, height, false);

        markerI = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(-12.579252, -69.198200))
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker9)));

        //-----------------------------------------------------------------

        Polygon polygon10 = googleMap.addPolygon(new PolygonOptions()
                .clickable(true)
                .add(
                        new LatLng(-12.574289, -69.192857), new LatLng(-12.572278, -69.202234), new LatLng(-12.568550, -69.201376),
                        new LatLng(-12.569095, -69.197299), new LatLng(-12.565576, -69.195325), new LatLng(-12.566414, -69.191162),
                        new LatLng(-12.570330, -69.191505), new LatLng(-12.574289, -69.192857)));

        polygon10.setTag("ZONA J");
        polygon10.setFillColor(0x474DB370);
        polygon10.setStrokeColor(0x474DB370);
        polygon10.setStrokeWidth(5);

        BitmapDrawable bitmapdraw10 = (BitmapDrawable)getResources().getDrawable(R.drawable.zonaj);
        Bitmap b10 = bitmapdraw10.getBitmap();
        Bitmap smallMarker10 = Bitmap.createScaledBitmap(b10, width, height, false);

        markerJ = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(-12.568948, -69.196183))
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker10)));

    }

    private void showZonaA(){

        String StProducto = txtProducto.getText().toString();
        if (StProducto.equals("")){
            Toasty.error(this, "Agrege sus productos", Toast.LENGTH_SHORT,true).show();
            zonaA.dismiss();
        }

        else {

            zonaA.setContentView(R.layout.dialog_zona_a);
            cerrarImgZonaA = (ImageView) zonaA.findViewById(R.id.closeDialog);
            cerrarPopupZonaA = (Button) zonaA.findViewById(R.id.btnCerrarDialog);
            txtCostoDelivery = (TextView) zonaA.findViewById(R.id.txtSolicitudCostoDelivery);
            btnZonaA = (Button) zonaA.findViewById(R.id.btnConfirmacion);

            if (StProducto.contains("Pizza") || StProducto.contains("pizza") || StProducto.contains("Torta") || StProducto.contains("torta")) {
                txtCostoDelivery.setText("5");
                txtDeliverySolicitud.setText("5");
            } else {
                txtCostoDelivery.setText("3");
                txtDeliverySolicitud.setText("3");
            }

            cerrarImgZonaA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(tiempo);
                    zonaA.dismiss();
                    txtDeliverySolicitud.setText("");
                }
            });

            cerrarPopupZonaA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(tiempo);
                    zonaA.dismiss();
                    txtDeliverySolicitud.setText("");
                }
            });

            btnZonaA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(tiempo);
                    showConfirmation();
                }
            });

            zonaA.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            zonaA.setCancelable(false);
            zonaA.show();
        }
    }

    private void showZonaB(){

        String StProducto = txtProducto.getText().toString();
        if (StProducto.equals("")){
            Toasty.error(this, "Agrege sus productos", Toast.LENGTH_SHORT,true).show();
            zonaB.dismiss();
        }

        else {

            zonaB.setContentView(R.layout.dialog_zona_b);
            cerrarImgZonaB = (ImageView) zonaB.findViewById(R.id.closeDialog);
            cerrarPopupZonaB = (Button) zonaB.findViewById(R.id.btnCerrarDialog);
            txtCostoDeliveryB = (TextView) zonaB.findViewById(R.id.txtSolicitudCostoDeliveryB);
            btnZonaB = (Button) zonaB.findViewById(R.id.btnConfirmacion);

            if (StProducto.contains("Pizza") || StProducto.contains("pizza") || StProducto.contains("Torta") || StProducto.contains("torta")) {
                txtCostoDeliveryB.setText("6");
                txtDeliverySolicitud.setText("6");
            } else {
                txtCostoDeliveryB.setText("4");
                txtDeliverySolicitud.setText("4");
            }


            cerrarImgZonaB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(tiempo);
                    zonaB.dismiss();
                    txtDeliverySolicitud.setText("");
                }
            });

            cerrarPopupZonaB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(tiempo);
                    zonaB.dismiss();
                    txtDeliverySolicitud.setText("");
                }
            });

            btnZonaB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(tiempo);
                    showConfirmation();
                }
            });

            zonaB.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            zonaB.setCancelable(false);
            zonaB.show();
        }
    }

    private void showZonaC(){

        String StProducto = txtProducto.getText().toString();
        if (StProducto.equals("")){
            Toasty.error(this, "Agrege sus productos", Toast.LENGTH_SHORT,true).show();
            zonaC.dismiss();
        }

        else {

            zonaC.setContentView(R.layout.dialog_zona_c);
            cerrarImgZonaC = (ImageView) zonaC.findViewById(R.id.closeDialog);
            cerrarPopupZonaC = (Button) zonaC.findViewById(R.id.btnCerrarDialog);
            txtCostoDeliveryC = (TextView) zonaC.findViewById(R.id.txtSolicitudCostoDelivery);
            btnZonaC = (Button) zonaC.findViewById(R.id.btnConfirmacion);


            if (StProducto.contains("Pizza") || StProducto.contains("pizza") || StProducto.contains("Torta") || StProducto.contains("torta")) {
                txtCostoDeliveryC.setText("7");
                txtDeliverySolicitud.setText("7");
            } else {
                txtCostoDeliveryC.setText("5");
                txtDeliverySolicitud.setText("5");
            }


            cerrarImgZonaC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(tiempo);
                    zonaC.dismiss();
                    txtDeliverySolicitud.setText("");
                }
            });

            cerrarPopupZonaC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(tiempo);
                    zonaC.dismiss();
                    txtDeliverySolicitud.setText("");
                }
            });

            btnZonaC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(tiempo);
                    showConfirmation();
                }
            });

            zonaC.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            zonaC.setCancelable(false);
            zonaC.show();
        }
    }

    private void showZonaD(){

        String StProducto = txtProducto.getText().toString();
        if (StProducto.equals("")){
            Toasty.error(this, "Agrege sus productos", Toast.LENGTH_SHORT,true).show();
            zonaD.dismiss();
        }

        else {

            zonaD.setContentView(R.layout.dialog_zona_d);
            cerrarImgZonaD = (ImageView) zonaD.findViewById(R.id.closeDialog);
            cerrarPopupZonaD = (Button) zonaD.findViewById(R.id.btnCerrarDialog);
            txtCostoDeliveryD = (TextView) zonaD.findViewById(R.id.txtSolicitudCostoDelivery);
            btnZonaD = (Button) zonaD.findViewById(R.id.btnConfirmacion);

            if (StProducto.contains("Pizza") || StProducto.contains("pizza") || StProducto.contains("Torta") || StProducto.contains("torta")) {
                txtCostoDeliveryD.setText("8");
                txtDeliverySolicitud.setText("8");
            } else {
                txtCostoDeliveryD.setText("6");
                txtDeliverySolicitud.setText("6");
            }


            cerrarImgZonaD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(tiempo);
                    zonaD.dismiss();
                    txtDeliverySolicitud.setText("");
                }
            });

            cerrarPopupZonaD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(tiempo);
                    zonaD.dismiss();
                    txtDeliverySolicitud.setText("");
                }
            });

            btnZonaD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(tiempo);
                    showConfirmation();
                }
            });


            zonaD.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            zonaD.setCancelable(false);
            zonaD.show();
        }

    }

    private void showZonaE(){

        String StProducto = txtProducto.getText().toString();
        if (StProducto.equals("")){
            Toasty.error(this, "Agrege sus productos", Toast.LENGTH_SHORT,true).show();
            zonaE.dismiss();
        }

        else {

            zonaE.setContentView(R.layout.dialog_zona_e);
            cerrarImgZonaE = (ImageView) zonaE.findViewById(R.id.closeDialog);
            cerrarPopupZonaE = (Button) zonaE.findViewById(R.id.btnCerrarDialog);
            txtCostoDeliveryE = (TextView) zonaE.findViewById(R.id.txtSolicitudCostoDelivery);
            btnZonaE = (Button) zonaE.findViewById(R.id.btnConfirmacion);

            if (StProducto.contains("Pizza") || StProducto.contains("pizza") || StProducto.contains("Torta") || StProducto.contains("torta")) {
                txtCostoDeliveryE.setText("7");
                txtDeliverySolicitud.setText("7");
            } else {
                txtCostoDeliveryE.setText("5");
                txtDeliverySolicitud.setText("5");
            }


            cerrarImgZonaE.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(tiempo);
                    zonaE.dismiss();
                    txtDeliverySolicitud.setText("");
                }
            });

            cerrarPopupZonaE.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(tiempo);
                    zonaE.dismiss();
                    txtDeliverySolicitud.setText("");
                }
            });

            btnZonaE.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(tiempo);
                    showConfirmation();
                }
            });


            zonaE.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            zonaE.setCancelable(false);
            zonaE.show();
        }
    }

    private void showZonaF(){

        String StProducto = txtProducto.getText().toString();
        if (StProducto.equals("")){
            Toasty.error(this, "Agrege sus productos", Toast.LENGTH_SHORT,true).show();
            zonaF.dismiss();
        }

        else {

            zonaF.setContentView(R.layout.dialog_zona_f);
            cerrarImgZonaF = (ImageView) zonaF.findViewById(R.id.closeDialog);
            cerrarPopupZonaF = (Button) zonaF.findViewById(R.id.btnCerrarDialog);
            txtCostoDeliveryF = (TextView) zonaF.findViewById(R.id.txtSolicitudCostoDelivery);
            btnZonaF = (Button) zonaF.findViewById(R.id.btnConfirmacion);

            if (StProducto.contains("Pizza") || StProducto.contains("pizza") || StProducto.contains("Torta") || StProducto.contains("torta")) {
                txtCostoDeliveryF.setText("6");
                txtDeliverySolicitud.setText("6");
            } else {
                txtCostoDeliveryF.setText("4");
                txtDeliverySolicitud.setText("4");
            }


            cerrarImgZonaF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(tiempo);
                    zonaF.dismiss();
                    txtDeliverySolicitud.setText("");
                }
            });

            cerrarPopupZonaF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(tiempo);
                    zonaF.dismiss();
                    txtDeliverySolicitud.setText("");
                }
            });

            btnZonaF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(tiempo);
                    showConfirmation();
                }
            });

            zonaF.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            zonaF.setCancelable(false);
            zonaF.show();
        }
    }

    private void showZonaG(){

        String StProducto = txtProducto.getText().toString();
        if (StProducto.equals("")){
            Toasty.error(this, "Agrege sus productos", Toast.LENGTH_SHORT,true).show();
            zonaG.dismiss();
        }

        else {

            zonaG.setContentView(R.layout.dialog_zona_g);
            cerrarImgZonaG = (ImageView) zonaG.findViewById(R.id.closeDialog);
            cerrarPopupZonaG = (Button) zonaG.findViewById(R.id.btnCerrarDialog);
            txtCostoDeliveryG = (TextView) zonaG.findViewById(R.id.txtSolicitudCostoDelivery);
            btnZonaG = (Button) zonaG.findViewById(R.id.btnConfirmacion);

            if (StProducto.contains("Pizza") || StProducto.contains("pizza") || StProducto.contains("Torta") || StProducto.contains("torta")) {
                txtCostoDeliveryG.setText("8");
                txtDeliverySolicitud.setText("8");
            } else {
                txtCostoDeliveryG.setText("6");
                txtDeliverySolicitud.setText("6");
            }


            cerrarImgZonaG.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(tiempo);
                    zonaG.dismiss();
                    txtDeliverySolicitud.setText("");
                }
            });

            cerrarPopupZonaG.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(tiempo);
                    zonaG.dismiss();
                    txtDeliverySolicitud.setText("");
                }
            });

            btnZonaG.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(tiempo);
                    showConfirmation();
                }
            });

            zonaG.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            zonaG.setCancelable(false);
            zonaG.show();
        }
    }

    private void showZonaH(){

        String StProducto = txtProducto.getText().toString();
        if (StProducto.equals("")){
            Toasty.error(this, "Agrege sus productos", Toast.LENGTH_SHORT,true).show();
            zonaH.dismiss();
        }

        else {

            zonaH.setContentView(R.layout.dialog_zona_h);
            cerrarImgZonaH = (ImageView) zonaH.findViewById(R.id.closeDialog);
            cerrarPopupZonaH = (Button) zonaH.findViewById(R.id.btnCerrarDialog);
            txtCostoDeliveryH = (TextView) zonaH.findViewById(R.id.txtSolicitudCostoDelivery);
            btnZonaH = (Button) zonaH.findViewById(R.id.btnConfirmacion);

            if (StProducto.contains("Pizza") || StProducto.contains("pizza") || StProducto.contains("Torta") || StProducto.contains("torta")) {
                txtCostoDeliveryH.setText("6");
                txtDeliverySolicitud.setText("6");
            } else {
                txtCostoDeliveryH.setText("4");
                txtDeliverySolicitud.setText("4");
            }


            cerrarImgZonaH.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(tiempo);
                    zonaH.dismiss();
                    txtDeliverySolicitud.setText("");
                }
            });

            cerrarPopupZonaH.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(tiempo);
                    zonaH.dismiss();
                    txtDeliverySolicitud.setText("");
                }
            });

            btnZonaH.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(tiempo);
                    showConfirmation();
                }
            });
            zonaH.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            zonaH.setCancelable(false);
            zonaH.show();
        }
    }

    private void showZonaI(){

        String StProducto = txtProducto.getText().toString();
        if (StProducto.equals("")){
            Toasty.error(this, "Agrege sus productos", Toast.LENGTH_SHORT,true).show();
            zonaI.dismiss();
        }

        else {

            zonaI.setContentView(R.layout.dialog_zona_i);
            cerrarImgZonaI = (ImageView) zonaI.findViewById(R.id.closeDialog);
            cerrarPopupZonaI = (Button) zonaI.findViewById(R.id.btnCerrarDialog);
            txtCostoDeliveryI = (TextView) zonaI.findViewById(R.id.txtSolicitudCostoDelivery);
            btnZonaI = (Button) zonaI.findViewById(R.id.btnConfirmacion);

            if (StProducto.contains("Pizza") || StProducto.contains("pizza") || StProducto.contains("Torta") || StProducto.contains("torta")) {
                txtCostoDeliveryI.setText("7");
                txtDeliverySolicitud.setText("7");
            } else {
                txtCostoDeliveryI.setText("5");
                txtDeliverySolicitud.setText("5");
            }


            cerrarImgZonaI.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(tiempo);
                    zonaI.dismiss();
                    txtDeliverySolicitud.setText("");
                }
            });

            cerrarPopupZonaI.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(tiempo);
                    zonaI.dismiss();
                    txtDeliverySolicitud.setText("");
                }
            });

            btnZonaI.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(tiempo);
                    showConfirmation();
                }
            });

            zonaI.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            zonaI.setCancelable(false);
            zonaI.show();
        }
    }

    private void showZonaJ(){

        String StProducto = txtProducto.getText().toString();
        if (StProducto.equals("")){
            Toasty.error(this, "Agrege sus productos", Toast.LENGTH_SHORT,true).show();
            zonaJ.dismiss();
        }

        else {

            zonaJ.setContentView(R.layout.dialog_zona_j);
            cerrarImgZonaJ = (ImageView) zonaJ.findViewById(R.id.closeDialog);
            cerrarPopupZonaJ = (Button) zonaJ.findViewById(R.id.btnCerrarDialog);
            txtCostoDeliveryJ = (TextView) zonaJ.findViewById(R.id.txtSolicitudCostoDelivery);
            btnZonaJ = (Button) zonaJ.findViewById(R.id.btnConfirmacion);

            if (StProducto.contains("Pizza") || StProducto.contains("pizza")) {
                txtCostoDeliveryJ.setText("7");
                txtDeliverySolicitud.setText("7");
            } else {
                txtCostoDeliveryJ.setText("5");
                txtDeliverySolicitud.setText("5");
            }


            cerrarImgZonaJ.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(tiempo);
                    zonaJ.dismiss();
                    txtDeliverySolicitud.setText("");
                }
            });

            cerrarPopupZonaJ.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(tiempo);
                    zonaJ.dismiss();
                    txtDeliverySolicitud.setText("");
                }
            });

            btnZonaJ.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(tiempo);
                    showConfirmation();
                }
            });

            zonaJ.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            zonaJ.setCancelable(false);
            zonaJ.show();
        }
    }

    private void showConfirmation(){

            dialogConfirm.setContentView(R.layout.dialog_confirmar);
            cerrarConfirmacion = (ImageView) dialogConfirm.findViewById(R.id.closeDialog);
            txtCostoDeliveryConfirmacion = (TextView) dialogConfirm.findViewById(R.id.txtSolicitudCostoDeliveryConfirm);
            btnConfirm = (Button) dialogConfirm.findViewById(R.id.btnSolicitudCompletada);
            edtSolDireccion = (EditText) dialogConfirm.findViewById(R.id.edtSolicitudDireccion);
            edtReferencia = (EditText) dialogConfirm.findViewById(R.id.edtSolicitudReferencia);
            edtTelefono = (EditText) dialogConfirm.findViewById(R.id.edtTelefonoSolicitud);
            edtNombreCliente = (EditText) dialogConfirm.findViewById(R.id.edtNombreClienteSolicitud);

           String s =  txtDeliverySolicitud.getText().toString();
           txtCostoDeliveryConfirmacion.setText(s);

            cerrarConfirmacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vibrator.vibrate(tiempo);
                    dialogConfirm.dismiss();
                }
            });

            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String stNombre = edtNombreCliente.getText().toString();
                    String stTelefono = edtTelefono.getText().toString();
                    String stDireccion = edtSolDireccion.getText().toString();
                    String stReferencia = edtReferencia.getText().toString();
                    final String stNombreEmpresa = getIntent().getStringExtra("keyProduct");
                    final String stPhoto = getIntent().getStringExtra("keyPhoto");

                    if (!stNombre.isEmpty() && !stTelefono.isEmpty() && !stDireccion.isEmpty() && !stReferencia.isEmpty()) {

                        vibrator.vibrate(tiempo);
                        AlertDialog.Builder builder = new AlertDialog.Builder(FichaDeSolicitud.this,R.style.ThemeOverlay);
                        builder.setTitle("Confirme!");
                        builder.setIcon(R.drawable.ic_error);
                        builder.setCancelable(false);
                        builder.setMessage("Todos los datos son Correctos?");
                        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("id", mAuth.getUid());
                                map.put("numSolicitud", txtNumSolicitud.getText().toString());
                                map.put("listaDeProductos", txtProducto.getText().toString());
                                map.put("cantidadProducto", txtCantidad.getText().toString());
                                map.put("precioUnitario", txtPrecioUnitario.getText().toString());
                                map.put("precioTotalProductos", txtPtotal.getText().toString());
                                map.put("nombreCliente", edtNombreCliente.getText().toString());
                                map.put("telefonoCliente", edtTelefono.getText().toString());
                                map.put("direccionCliente", edtSolDireccion.getText().toString());
                                map.put("referenciaCliente", edtReferencia.getText().toString());
                                map.put("totalNetoProducto",txtNeto.getText().toString());
                                map.put("costoDelivery", txtCostoDeliveryConfirmacion.getText().toString());
                                map.put("proveedor",stNombreEmpresa);
                                map.put("photo",stPhoto);
                                map.put("estado","Sin confirmar");
                                mDatabase.child("SolicitudDelivery").push().setValue(map);
                                mDatabase.child("Usuarios").child("Proveedor").child(mAuth.getUid()).child("Solicitudes").push().setValue(map);
                                listReduceProduct();
                                sendSolicitudeNotification();
                                Toasty.success(FichaDeSolicitud.this, "Solicitud Enviada!", Toast.LENGTH_SHORT, true).show();
                                startActivity(new Intent(FichaDeSolicitud.this,MenuProveedor.class));
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
                    else {
                        Toasty.warning(FichaDeSolicitud.this, "Complete los campos", Toast.LENGTH_SHORT,true).show();
                    }
            }
            });

            dialogConfirm.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogConfirm.setCancelable(false);
            dialogConfirm.show();
    }

                private void sendSolicitudeNotification(){

                    final String nameBussiness = getIntent().getStringExtra("keyProduct");
                    final String photo = getIntent().getStringExtra("keyPhoto");
                    String[] admins = {"nS8J0zEj53OcXSugQsXIdMKUi5r1", "UnwAmhwRzmRLn8aozWjnYFOxYat2",
                            "9sjTQMmowxWYJGTDUY98rAR2jzB3"};

                    for (int i = 0; i < admins.length; i++) {
                        tokenProvider.getToken(admins[i]).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String token = dataSnapshot.child("token").getValue().toString();
                                    Map<String, String> map = new HashMap<>();
                                    map.put("title", "Solicitud de DELIVERY!");
                                    map.put("body",  "El SOCIO : " +nameBussiness+" está solicitando delivery");
                                    map.put("path", photo);
                                    FCMBody fcmBody = new FCMBody(token, "high", map);
                                    notificationProvider.sendNotificacion(fcmBody).enqueue(new Callback<FCMResponse>() {
                                        @Override
                                        public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                                            if (response.body() != null) {
                                                if (response.body().getSuccess() == 1) {
                                                    //Toast.makeText(RealizarPedido.this, "Notificación enviada", Toast.LENGTH_LONG).show();
                                                } else {
                                                    Toasty.error(FichaDeSolicitud.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toasty.error(FichaDeSolicitud.this, "No se envió la notificación", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        @Override
                                        public void onFailure(Call<FCMResponse> call, Throwable t) {
                                            Log.d("Error", "Error encontrado" + t.getMessage());
                                        }
                                    });
                                } else {
                                    Toast.makeText(FichaDeSolicitud.this, "No existe token se sesión", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
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
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(markerA)) {
            showZonaA();
        }else if(marker.equals(markerB)){
            showZonaB();
        }
        else if(marker.equals(markerC)){
            showZonaC();
        }
        else if(marker.equals(markerD)){
            showZonaD();
        }
        else if(marker.equals(markerE)){
            showZonaE();
        }
        else if(marker.equals(markerF)){
            showZonaF();
        }else if(marker.equals(markerG)){
            showZonaG();
        }
        else if(marker.equals(markerH)){
            showZonaH();
        }
        else if(marker.equals(markerI)){
            showZonaI();
        }
        else if(marker.equals(markerJ)){
            showZonaJ();
        }
        return false;
    }


    private void listReduceProduct(){
        final String desc [] = txtProducto.getText().toString().split("\n");
        for (final String s : desc){
            Query reference = FirebaseDatabase.getInstance().getReference()
                    .child("Usuarios").child("Proveedor").child(mAuth.getUid())
                    .child("Productos").orderByChild("nombreProducto").equalTo(s);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                            final String key = childSnapshot.getKey();
                          mDatabase.child("Usuarios").child("Proveedor").child(mAuth.getUid())
                                    .child("Productos").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                              @Override
                              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                  String sTstock = dataSnapshot.child("stock").getValue().toString();
                                  int intStock = Integer.parseInt(sTstock);
                                  String descCant [] = txtCantidad.getText().toString().split("\n");
                                  for(final String c : descCant){
                                      int cant = Integer.parseInt(c);
                                      int newStock = intStock - cant;
                                      String newStockSt = String.valueOf(newStock);
                                      Map<String,Object> map = new HashMap<>();
                                      map.put("stock",newStockSt);
                                      mDatabase.child("Usuarios").child("Proveedor").child(mAuth.getUid())
                                              .child("Productos").child(key).updateChildren(map);
                                  }
                              }
                              @Override
                              public void onCancelled(@NonNull DatabaseError databaseError) {
                              }
                          });
                        }
                    }
                    else{
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void reduceStock(){
        if(!txtProducto.getText().toString().equals("")) {
            String desc[] = txtCantidad.getText().toString().split("\n");
            String desc2[] = txtProducto.getText().toString().split("\n");
            String ultimoStock = desc[desc.length - 1];
            String ultimoProducto = desc2[desc2.length - 1];
            int ultimoStockInt = Integer.parseInt(ultimoStock);
            int stock = Integer.parseInt(mTxtStock.getText().toString());
            int sum;

            if (ultimoProducto.equals(edtProducto.getText().toString())) {
                sum = stock + ultimoStockInt;
                String newStock = String.valueOf(sum);
                mTxtStock.setText(newStock);
            } else {
            }
            removeVariousCant();
        }
        else {
            Toasty.info(this, "Lista vacía", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeVariousCant(){
        String desc [] = txtCantidad.getText().toString().split("\n");
        String desc2 [] = txtProducto.getText().toString().split("\n");
        int suma = 0;
        for(int i = 0 ; i<desc2.length ; i++) {
            if (desc2[i].equals(edtProducto.getText().toString())){
                int cant = Integer.parseInt(desc[i]);
                //Toast.makeText(this, desc2[i] + " -> " + desc[i], Toast.LENGTH_SHORT).show();
                suma = cant + suma;
        }
        }
        /*String s = String.valueOf(suma);
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();*/
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(FichaDeSolicitud.this, MenuProveedor.class));
        finish();
    }


}