package aukde.food.gestordepedidos.paquetes.Actividades.Registros;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.List;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Actividades.Inicio;
import aukde.food.gestordepedidos.paquetes.Actividades.Logins.LoginAdmin;
import aukde.food.gestordepedidos.paquetes.Actividades.Pedidos.RealizarPedido;
import aukde.food.gestordepedidos.paquetes.Inclusiones.MiToolbar;
import aukde.food.gestordepedidos.paquetes.Menus.MenuAdmin;
import aukde.food.gestordepedidos.paquetes.Modelos.Aukdeliver;
import aukde.food.gestordepedidos.paquetes.Modelos.Proveedor;
import aukde.food.gestordepedidos.paquetes.Providers.AuthProviders;
import aukde.food.gestordepedidos.paquetes.Providers.ProveedorProvider;
import es.dmoral.toasty.Toasty;

public class RegistroProveedor extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMarkerDragListener  {

    private TextInputEditText edtNombres, edtApellidos,edtUsername,edtDni, edtTelefono,
            edtDireccion,edtCategoria,edtNombreEmpresa,edtRUC,edtEmail, edtPassword,
            edtRepetirPass, edtClaveAuth;

    private ProgressDialog mDialog;
    Button mButtonRegistro,btnMap;
    AuthProviders mAuthProviders;
    ProveedorProvider mProveedorProvider;
    Spinner mSpinner;
    private MapView mapView;
    private GoogleMap mMap;
    private Geocoder geocoder;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final String TAG = "RealizarPedido";
    FloatingActionButton  mFloatingMap;
    private LinearLayout mLinearMap;
    private EditText edtLongitud , edtLatitud;
    private LatLng destino;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_proveedor);
        MiToolbar.Mostrar(this,"Registro Proveedor",true);

        mAuthProviders = new AuthProviders();
        mProveedorProvider = new ProveedorProvider();
        mButtonRegistro = findViewById(R.id.btnRegistrarse);
        mDialog = new ProgressDialog(this);

        edtNombres = findViewById(R.id.ProveedorNombres);
        edtApellidos = findViewById(R.id.ProveedorApellidos);
        edtUsername = findViewById(R.id.ProveedorUsername);
        edtDni = findViewById(R.id.ProveedorDNI);
        edtTelefono = findViewById(R.id.ProveedorTeléfono);
        edtDireccion = findViewById(R.id.ProveedorDireccion);
        edtDireccion.setEnabled(false);
        edtCategoria = findViewById(R.id.ProveedorCategoria);
        edtCategoria.setEnabled(false);
        edtNombreEmpresa = findViewById(R.id.ProveedorNombreEmpre);
        edtRUC= findViewById(R.id.ProveedorRUC);
        edtEmail = findViewById(R.id.ProveedorEmail);
        edtPassword = findViewById(R.id.ProveedorEdtPassword);
        edtRepetirPass = findViewById(R.id.ProveedorRepetirContrasena);
        edtClaveAuth = findViewById(R.id.ProveedorClaveAutorización);
        edtLongitud = findViewById(R.id.lon);
        edtLongitud.setEnabled(false);
        edtLatitud = findViewById(R.id.lat);
        edtLatitud.setEnabled(false);
        btnMap = findViewById(R.id.btnMapear);
        mapView = findViewById(R.id.map_view);


        geocoder = new Geocoder(this);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        mFloatingMap = findViewById(R.id.booleanMap);
        mFloatingMap.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.quantum_googred)));
        int alto = 0;
        mLinearMap = findViewById(R.id.map_container);
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,alto);
        mLinearMap.setLayoutParams(params);


        btnMap.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                mFloatingMap.setVisibility(View.VISIBLE);
                CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                mLinearMap.setLayoutParams(params);
            }
        });

        mFloatingMap.setVisibility(View.INVISIBLE);
        mFloatingMap.setOnClickListener(new View.OnClickListener() {
            int alto1 = 0;
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,alto1);
                mLinearMap.setLayoutParams(params);
                mFloatingMap.setVisibility(View.INVISIBLE);
            }
        });

        edtDireccion.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                edtDireccion.setTextColor(Color.RED);
            }
        });

        mSpinner = findViewById(R.id.spinnerFood);
        ArrayAdapter<CharSequence> adapterSpinnerFood = ArrayAdapter.createFromResource(this,R.
                array.categoriaFood,R.layout.custom_spinner);
        adapterSpinnerFood.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        mSpinner.setAdapter(adapterSpinnerFood);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                edtCategoria.setText(parent.getItemAtPosition(position).toString());
                //String stSpinnerEstado = edtCategoriaLic.getText().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mButtonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickRegistro();
            }
        });

    }

    private void ClickRegistro() {

        final String nombres = edtNombres.getText().toString();
        final String apellidos = edtApellidos.getText().toString();
        final String username = edtUsername.getText().toString();
        final String dni = edtDni.getText().toString();
        final String telefono = edtTelefono.getText().toString();
        final String direccion = edtDireccion.getText().toString();
        final String categoria = edtCategoria.getText().toString();
        final String nombre_empresa = edtNombreEmpresa.getText().toString();
        final String ruc = edtRUC.getText().toString();
        final String email = edtEmail.getText().toString();
        final String password = edtPassword.getText().toString();
        final String repetirPass = edtRepetirPass.getText().toString();
        final String ClaveAuth = edtClaveAuth.getText().toString();
        final String latitud = edtLatitud.getText().toString();
        final String longitud = edtLongitud.getText().toString();

        if(!nombres.isEmpty() && !apellidos.isEmpty() &&!username.isEmpty() && !dni.isEmpty() && !telefono.isEmpty()
                && !direccion.isEmpty() && !categoria.isEmpty() && !nombre_empresa.isEmpty() &&
                !ruc.isEmpty() && !email.isEmpty() && !password.isEmpty()
                && !repetirPass.isEmpty() && !ClaveAuth.isEmpty() && !latitud.isEmpty() && !longitud.isEmpty()){

            mDialog.show();
            mDialog.setMessage("Registrando usuario...");
            if(password.length()>=6){
                if(password.equals(repetirPass)){
                    if(ClaveAuth.equals("AUK2020+*") || ClaveAuth.equals("WRZ20@") || ClaveAuth.equals("GOGOOL*")){
                        registrar(nombres,apellidos,username,dni,telefono,direccion,categoria,nombre_empresa,ruc,email,latitud,longitud,password);
                    }
                    else {
                        mDialog.dismiss();
                        Toast.makeText(RegistroProveedor.this,"Clave de autorización inválida",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    mDialog.dismiss();
                    Toast.makeText(RegistroProveedor.this,"Las contraseñas no coinciden",Toast.LENGTH_LONG).show();
                }
            }
            else {
                mDialog.dismiss();
                Toast.makeText(RegistroProveedor.this,"La contraseña debe ser mayor a 6 caracteres",Toast.LENGTH_LONG).show();
            }
        }

        else {
            Toast.makeText(RegistroProveedor.this,"Complete los todos campos",Toast.LENGTH_LONG).show();
        }

    }

    private void registrar(final String nombres, final String apellidos,final String username,final String dni,final String telefono,final String direccion,final String categoria,final String nombre_empresa,final String ruc,final String email,final String latitud,final String longitud, String password) {

        mAuthProviders.Registro(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Proveedor proveedor = new Proveedor(id,nombres,apellidos,username,dni,telefono,direccion,categoria,nombre_empresa,ruc,email,latitud,longitud);
                mapear(proveedor);
            }
        });
    }

    void mapear(Proveedor proveedor){
        mProveedorProvider.Mapear(proveedor).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    mDialog.dismiss();
                    logout();
                }

                else {
                    mDialog.dismiss();
                    Toast.makeText(RegistroProveedor.this, "No se pudo crear un nuevo usuario", Toast.LENGTH_SHORT).show();
                }

            }
        });
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
                edtLatitud.setText(""+destino.latitude);
                edtLongitud.setText(""+destino.longitude);
                edtDireccion.setEnabled(true);
                edtDireccion.setTextColor(Color.BLACK);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener(){
            @Override
            public void onInfoWindowClick(final Marker marker){
                AlertDialog.Builder builder = new AlertDialog.Builder(RegistroProveedor.this);
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
                edtLatitud.setText(""+destino.latitude);
                edtLongitud.setText(""+destino.longitude);
                edtDireccion.setEnabled(true);
                edtDireccion.setTextColor(Color.BLACK);
                marker.setTitle(streetAddress);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        Log.d(TAG, "onMarkerDragStart: ");
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        Log.d(TAG, "onMarkerDrag: ");
    }

    void logout() {
        Intent intent = new Intent(getApplicationContext(), LoginAdmin.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("dato","valor");
        startActivity(intent);
        finish();
        mAuthProviders.Logout();
    }

}