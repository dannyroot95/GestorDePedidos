package aukde.food.gestordepedidos.paquetes.Actividades.Usuarios;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Actividades.Pedidos.EditarPedido;
import aukde.food.gestordepedidos.paquetes.Providers.AdminProvider;
import aukde.food.gestordepedidos.paquetes.Providers.AuthProviders;
import aukde.food.gestordepedidos.paquetes.Providers.PedidoProvider;
import aukde.food.gestordepedidos.paquetes.Providers.ProveedorProvider;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class EditarProveedor extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMarkerDragListener {
    private CircleImageView photoPerfil , CrMyPhoto;

    private EditText txtNombres,txtApellidos,txtNombreUsuario, txtCorreo,txtDni,txtTelefono,txtRuc,txtNombreEmpresa;
    private AdminProvider mAdminProvider;
    private AuthProviders mAuthProviders;

    private File mImageFile;
    private ProgressDialog mDialog;
    private DatabaseReference mDatabase;
    private final int GALLERY_REQUEST = 1;
    private Vibrator vibrator;


    private MapView mapView;
    private GoogleMap mMap;
    private Geocoder geocoder;

    public EditText edtDireccion;

    Button mButtonMapearProveedor;
    FloatingActionButton mFloatingButton, mFloatingMap;

    TextView latitudProveedor,logitudProveedor;

    ProveedorProvider mproveedorProvider;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final String TAG = "RealizarCambio";

    long tiempo = 100;
    String upNombre;
    String upApellido;
    String upNombreUsuario;
    String upCorreo;
    String upDni;
    String upTelefono;
    String upRuc;
    String upNombreEmpresa;
    String upLatitud;
    String upLongitud;
    String upDireccion;

    private LinearLayout mLinearMap;
    TextView estado ;
    private LatLng destino;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_proveedor);

        mDialog = new ProgressDialog(this,R.style.ThemeOverlay);
        mAdminProvider = new AdminProvider();
        mAuthProviders = new AuthProviders();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        photoPerfil = findViewById(R.id.fotodefaultProveedor);
        CrMyPhoto = findViewById(R.id.myPhotoProveedor);

        txtNombres=findViewById(R.id.edtNombreProveedor);
        txtApellidos=findViewById(R.id.edtApellidoProveedor);
        txtNombreUsuario=findViewById(R.id.edtUsuarioProveedor);
        txtCorreo=findViewById(R.id.edtCorreoProveedor);
        txtDni=findViewById(R.id.edtDniProveedor);
        txtTelefono=findViewById(R.id.edtTelefonoProveedor);
        txtRuc=findViewById(R.id.edtProveedorRuc);
        txtNombreEmpresa=findViewById(R.id.edtNombreEmpresaProveedor);

        edtDireccion=findViewById(R.id.edtProveedorDireccion);
        edtDireccion.setEnabled(false);
        mButtonMapearProveedor=findViewById(R.id.btnEditarProveedorMapear);
        latitudProveedor=findViewById(R.id.txtLatitudProveedor);
        logitudProveedor=findViewById(R.id.txtLongitudProveedor);

        //-----------------------------------------------------
        int alto = 0;
        mLinearMap =findViewById(R.id.map_container);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,alto);
        mLinearMap.setLayoutParams(params);
        mproveedorProvider = new ProveedorProvider();
        estado = findViewById(R.id.txtEstado);
        mFloatingButton=findViewById(R.id.floatRegisterProveedor);
        mFloatingButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.quantum_black_100)));

        mFloatingMap = findViewById(R.id.booleanMapProveedor);
        mFloatingMap.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.quantum_googred)));

        geocoder = new Geocoder(this);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }
        mapView = findViewById(R.id.map_viewProveedor);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);


        photoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // abrirGaleria();
                vibrator.vibrate(tiempo);
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_REQUEST);
            }
        });

        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                AlertDialog.Builder builder = new AlertDialog.Builder(EditarProveedor.this,R.style.ThemeOverlay);
                builder.setTitle("Actualización de pedido");
                builder.setCancelable(false);
                builder.setIcon(R.drawable.ic_correcto);
                builder.setMessage("Deseas actualizar este pedido? ");
                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        vibrator.vibrate(tiempo);
                        ActualzarPerfil();
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

        mButtonMapearProveedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                mFloatingMap.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
                mLinearMap.setLayoutParams(params);
            }
        });



        ObtenerDataUser();
        getPhotoUsuario();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            mDialog.setMessage("Actualizando foto de perfil...");
            mDialog.setCancelable(false);
            mDialog.show();
            Uri uri = data.getData();
            final  String id = getIntent().getStringExtra("keyProveedor");
            //final String id = mAuthProviders.getId();
            final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("PhotoProveedor").child(mAuthProviders.getId()+".jpg");

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            DatabaseReference imagestore = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Proveedor").child(id);
                            HashMap<String,Object> fotoMap = new HashMap<>();
                            fotoMap.put("foto" , String.valueOf(uri));
                            imagestore.updateChildren(fotoMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    getPhotoUsuario();
                                    mDialog.dismiss();
                                    Toasty.success(EditarProveedor.this, "Foto de perfil actualizada", Toast.LENGTH_LONG,true).show();
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    private void ObtenerDataUser(){
        String name = getIntent().getStringExtra("keyProveedor");
        mDatabase.child("Usuarios").child("Proveedor").child(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nombre = dataSnapshot.child("nombres").getValue().toString();
                String apellido = dataSnapshot.child("apellidos").getValue().toString();
                String nombreusuario= dataSnapshot.child("username").getValue().toString();
                String correo= dataSnapshot.child("email").getValue().toString();
                String dni=dataSnapshot.child("dni").getValue().toString();
                String telefono=dataSnapshot.child("telefono").getValue().toString();
                String ruc=dataSnapshot.child("ruc").getValue().toString();
                String nombreempresa=dataSnapshot.child("nombre_empresa").getValue().toString();
                String direccionproveedor=dataSnapshot.child("direccion").getValue().toString();


                txtNombres.setText(nombre);
                txtApellidos.setText(apellido);
                txtNombreUsuario.setText(nombreusuario);
                txtCorreo.setText(correo);
                txtDni.setText(dni);
                txtTelefono.setText(telefono);
                txtRuc.setText(ruc);
                txtNombreEmpresa.setText(nombreempresa);
                edtDireccion.setText(direccionproveedor);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ActualzarPerfil() {
        upNombre = txtNombres.getText().toString();
        upApellido = txtApellidos.getText().toString();
        upNombreUsuario=txtNombreUsuario.getText().toString();
        upCorreo=txtCorreo.getText().toString();
        upDni=txtDni.getText().toString();
        upTelefono=txtTelefono.getText().toString();
        upRuc=txtRuc.getText().toString();
        upNombreEmpresa=txtNombreEmpresa.getText().toString();
        upDireccion=edtDireccion.getText().toString();

        upLatitud=latitudProveedor.getText().toString();
        upLongitud=logitudProveedor.getText().toString();

        if(!upNombre.equals("") && !upApellido.equals("") && !upNombreUsuario.equals("") && !upCorreo.equals("") && !upDni.equals("") && !upTelefono.equals("") && !upRuc.equals("") && !upNombreEmpresa.equals("") && !upLatitud.equals("") && !upLongitud.equals("") && !upDireccion.equals("")){
            mDialog.setMessage("Actualizando...");
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
            guardarNombres();
        }

        else {
            mDialog.dismiss();
            Toasty.info(this, "Verifica los campos", Toast.LENGTH_SHORT,true).show();
        }

    }

    private void getPhotoUsuario(){
        String id = getIntent().getStringExtra("keyProveedor");
        mDatabase.child("Usuarios").child("Proveedor").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("foto")){
                    //Obtener el url y setearlo en la imagem
                    String setFoto = dataSnapshot.child("foto").getValue().toString();
                    Glide.with(EditarProveedor.this).load(setFoto).into(CrMyPhoto);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void guardarNombres(){
        final  String id = getIntent().getStringExtra("keyProveedor");
        final String nombre = txtNombres.getText().toString();
        final String apellido = txtApellidos.getText().toString();
        final String nombreusuario= txtNombreUsuario.getText().toString();
        final String correo= txtCorreo.getText().toString();
        final String dni= txtDni.getText().toString();
        final String telefono= txtTelefono.getText().toString();
        final String Ruc=txtRuc.getText().toString();
        final String nombreempresa=txtNombreEmpresa.getText().toString();
        final String direccion = edtDireccion.getText().toString();
        final String latitud=latitudProveedor.getText().toString();
        final String logitud  =logitudProveedor.getText().toString();

        mDatabase.child("Usuarios").child("Proveedor").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,Object> datos = new HashMap<>();
                datos.put("nombres",nombre);
                datos.put("apellidos",apellido);
                datos.put("username",nombreusuario);
                datos.put("email",correo);
                datos.put("dni",dni);
                datos.put("telefono",telefono);
                datos.put("ruc",Ruc);
                datos.put("nombre_empresa",nombreempresa);
                datos.put("direccion",direccion);
                datos.put("latitud",latitud);
                datos.put("logitud",logitud);


                mDatabase.child("Usuarios").child("Proveedor").child(id).updateChildren(datos).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toasty.success(EditarProveedor.this, "Nombres Actualizados", Toast.LENGTH_SHORT,true).show();
                        ObtenerDataUser();
                        mDialog.dismiss();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(EditarProveedor.this, "Hubo un error al actualizar datos", Toast.LENGTH_SHORT,true).show();
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
                latitudProveedor.setText(""+destino.latitude);
                logitudProveedor.setText(""+destino.longitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener(){
            @Override
            public void onInfoWindowClick(final Marker marker){
                AlertDialog.Builder builder = new AlertDialog.Builder(EditarProveedor.this);
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
    public void onBackPressed(){
        NavUtils.navigateUpFromSameTask(this);
        /*startActivity(new Intent(PerfilAdmin.this, MenuAdmin.class));
        finish();*/
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
                latitudProveedor.setText(""+destino.latitude);
                logitudProveedor.setText(""+destino.longitude);
                marker.setTitle(streetAddress);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}