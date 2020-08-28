package aukde.food.gestordepedidos.paquetes.Mapas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Providers.GoogleApiProvider;
import aukde.food.gestordepedidos.paquetes.Utils.DecodePoints;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapaClientePorLlamada extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Bundle poscicion;
    private LatLng origen;
    private Marker mMarker;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocation;
    private final static int LOCATION_REQUEST_CODE = 1;
    private final static int SETTINGS_REQUEST_CODE = 2;
    private GoogleApiProvider mGoogleapiProvider;
    private List<LatLng> mPolylineList;
    private PolylineOptions mPolylineOptions;
    private TextView nombres , distancia , tiempo;
    private FloatingActionButton mFloat;
    private ProgressDialog mDialog;

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            mDialog.show();
            mDialog.setCancelable(false);
            mDialog.setMessage("Cargando mapa...");
            for (Location location : locationResult.getLocations()) {
                if (getApplicationContext() != null) {
                    if (mMarker != null) {
                        mMarker.remove();
                    }
                    origen = new LatLng(location.getLatitude(), location.getLongitude());
                    String stlatitud = poscicion.getString("latitud");
                    String stlongitud = poscicion.getString("longitud");
                    if (stlatitud.isEmpty() && stlongitud.isEmpty())
                    {
                        //finish();
                    }
                    else{
                        Double latitud = Double.parseDouble(stlatitud);
                        Double longitud = Double.parseDouble(stlongitud);
                        LatLng destino = new LatLng(latitud,longitud);
                        mGoogleapiProvider.getDirections(origen, destino).enqueue(new Callback<String>() {
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
                                    mPolylineOptions.width(15f);
                                    mPolylineOptions.startCap(new SquareCap());
                                    mPolylineOptions.jointType(JointType.ROUND);
                                    mPolylineOptions.addAll(mPolylineList);
                                    mMap.addPolyline(mPolylineOptions).setPoints(mPolylineList);

                                    JSONArray legs = route.getJSONArray("legs");
                                    JSONObject leg = legs.getJSONObject(0);
                                    JSONObject Jdistancia = leg.getJSONObject("distance");
                                    JSONObject Jduracion = leg.getJSONObject("duration");
                                    String stDistancia = Jdistancia.getString("value");
                                    String stDuracion = Jduracion.getString("value");

                                    int Dtiempo = Integer.parseInt(stDuracion);
                                    int intTiempo = (Dtiempo/60)-1;

                                    Double DtDistancia = Double.parseDouble(stDistancia);
                                    Double intDistancia = (DtDistancia/1000);
                                    BigDecimal bd = new BigDecimal(intDistancia);
                                    bd = bd.setScale(1, RoundingMode.HALF_UP);
                                    String km = String.valueOf(bd);
                                    String time = String.valueOf(intTiempo);

                                    if(intTiempo <= 0){
                                        tiempo.setText("1"+" min");
                                        mDialog.dismiss();
                                    }
                                    else
                                        {
                                            tiempo.setText(time+" min");
                                            mDialog.dismiss();
                                        }
                                    if(intDistancia<1.0){
                                        distancia.setText(stDistancia+" m");
                                        mDialog.dismiss();
                                    }
                                    else {
                                        distancia.setText(km+" Km");
                                        mDialog.dismiss();
                                    }



                                }catch (Exception e){
                                    Log.d("Error","Error encontrado"+ e.getMessage());
                                }
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                            }

                        });
                    }
                    mMarker = mMap.addMarker(new MarkerOptions().position(
                            new LatLng(location.getLatitude(), location.getLongitude())
                            )
                                    .title("Tú Poscición")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.head))
                    );
                    mMarker.showInfoWindow();
                    // OBTENER LA LOCALIZACION DEL USUARIO EN TIEMPO REAL
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_cliente_por_llamada);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGoogleapiProvider = new GoogleApiProvider(MapaClientePorLlamada.this);
        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        mDialog = new ProgressDialog(this,R.style.ThemeOverlay);

        nombres = findViewById(R.id.textViewName);
        distancia = findViewById(R.id.textViewDistancia);
        tiempo = findViewById(R.id.textViewTiempo);

        mFloat = findViewById(R.id.floatingLlamada);
        mFloat.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.quantum_googgreen)));

        poscicion = getIntent().getExtras();
        if (poscicion.isEmpty()){
            Toasty.error(this,"Error",Toasty.LENGTH_SHORT).show();
            finish();
        }

        mFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLlamadaCliente();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        startLocacion();


        String stlatitud = poscicion.getString("latitud");
        String stlongitud = poscicion.getString("longitud");
        String stNombreC = poscicion.getString("nombre");

        nombres.setText(stNombreC.toUpperCase());

        if (stlatitud.isEmpty() && stlongitud.isEmpty()){
            Toasty.error(this,"Error",Toasty.LENGTH_SHORT).show();
            finish();
        }
        else{
            Double latitud = Double.parseDouble(stlatitud);
            Double longitud = Double.parseDouble(stlongitud);

            LatLng point = new LatLng(latitud, longitud);
            mMap.addMarker(new MarkerOptions()
                .position(point)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.flag))
                .title(stNombreC)).showInfoWindow();
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,15f));
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if(gpsActive()){
                        mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                                ActivityCompat.requestPermissions(MapaClientePorLlamada.this , new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();
            }
            else {
                ActivityCompat.requestPermissions(MapaClientePorLlamada.this , new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);
            }
        }
    }
    //----------

    private void onClickLlamadaCliente() {
        String stTelefono = poscicion.getString("telefono");
        Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+stTelefono));
        startActivity(i);
    }

}