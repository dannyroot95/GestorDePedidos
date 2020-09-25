package aukde.food.gestordepedidos.paquetes.Menus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.PowerManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.File;
import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Actividades.Inicio;
import aukde.food.gestordepedidos.paquetes.Actividades.Pedidos.ListaPedidosAukdeliver;
import aukde.food.gestordepedidos.paquetes.Menus.Perfiles.PerfilAukdeliver;
import aukde.food.gestordepedidos.paquetes.Providers.AuthProviders;
import aukde.food.gestordepedidos.paquetes.Providers.TokenProvider;
import aukde.food.gestordepedidos.paquetes.Receptor.GpsReceiver;
import aukde.food.gestordepedidos.paquetes.Receptor.NetworkReceiver;
import aukde.food.gestordepedidos.paquetes.Servicios.JobServiceMonitoreo;
import aukde.food.gestordepedidos.paquetes.Utils.ApplicationCronometro;
import aukde.food.gestordepedidos.paquetes.Utils.DeleteCache;
import aukde.food.gestordepedidos.paquetes.Utils.SaveStorageImage;
import de.hdodenhof.circleimageview.CircleImageView;


public class MenuAukdeliver extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    NetworkReceiver networkReceiver = new NetworkReceiver();
    GpsReceiver gpsReceiver = new GpsReceiver();
    private AuthProviders mAuthProviders;
    private ProgressDialog mDialog;
    private Button btnLista , btnPerfil;
    SharedPreferences mSharedPreference;
    private DatabaseReference mDatabase;
    private AuthProviders mAuth;
    private CircleImageView foto;
    private TokenProvider mTokenProvider;
    private TextView Txtnombres, Txtapellidos;
    private ShimmerFrameLayout shimmerFrameLayout;
    private LinearLayout LinearShimmer;
    public static final int LOCATION_REQUEST_CODE = 1;
    private final static int ID_SERVICIO = 99;
    public static final int SETTINGS_REQUEST_CODE = 2;
    private FusedLocationProviderClient mFusedLocation;
    private LocationRequest mLocationRequest;
    private LatLng origen;
    private Vibrator vibrator;
    long tiempo = 100;
    TextView txtCronometro;
    SharedPreferences dataNombre;
    SharedPreferences dataApellido;
    DeleteCache deleteCache;
    SaveStorageImage saveStorageImage;

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                origen = new LatLng(location.getLatitude(), location.getLongitude());
                if (getApplicationContext() != null) {
                    //obtener locatizacion en tiempo real
                    Double lat = location.getLatitude();
                    String SLat = String.valueOf(lat);
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme2);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_aukdeliver);
        mFusedLocation = LocationServices.getFusedLocationProviderClient(this);
        mAuthProviders = new AuthProviders();
        saveStorageImage = new SaveStorageImage();
        mDialog = new ProgressDialog(MenuAukdeliver.this,R.style.ThemeOverlay);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mSharedPreference = getApplicationContext().getSharedPreferences("tipoUsuario", MODE_PRIVATE);
        btnLista = findViewById(R.id.btnListaPedidosAukdeliver);
        btnPerfil = findViewById(R.id.btnPerfil);
        Txtnombres = findViewById(R.id.txtNombres);
        Txtapellidos = findViewById(R.id.txtApellidos);
        deleteCache = new DeleteCache();
        foto = findViewById(R.id.fotodefault);
        dataNombre = PreferenceManager.getDefaultSharedPreferences(this);
        dataApellido = PreferenceManager.getDefaultSharedPreferences(this);
        LinearShimmer = findViewById(R.id.linearShimmer);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        shimmerFrameLayout.startShimmer();

        txtCronometro = findViewById(R.id.realTimeCronometro);

        mAuth = new AuthProviders();
        mTokenProvider = new TokenProvider();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }

        btnLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                mDialog.show();
                mDialog.setMessage("Cargando...");
                startActivity(new Intent(MenuAukdeliver.this, ListaPedidosAukdeliver.class));
            }
        });

        btnPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                mDialog.show();
                mDialog.setMessage("Cargando...");
                startActivity(new Intent(MenuAukdeliver.this, PerfilAukdeliver.class));
            }
        });

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        getPhotoUsuario();
        generarToken();
        getDataUser();
        startLocacion();
        verify();
        startJobSchedule();
        showCronometro();

    }


    private boolean gpsActive() {
        boolean isActive = false;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            isActive = true;
        }

        return isActive;
    }

    public void checkLocationPermision() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Proporciona los permisos para continuar")
                        .setCancelable(false)
                        .setMessage("Esta aplicación requiere los permisos para continuar")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MenuAukdeliver.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(MenuAukdeliver.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }

    private void getDataUser() {

        String mypref = dataNombre.getString("Mi llave","Vacío");
        String mypref2 = dataApellido.getString("Mi llave 2","Vacío");

        if (mypref.equals("Vacío")) {
            String id = mAuth.getId();
            mDatabase.child("Usuarios").child("Aukdeliver").child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String nombres = dataSnapshot.child("nombres").getValue().toString();
                        String apellidos = dataSnapshot.child("apellidos").getValue().toString();
                        LinearShimmer.setBackground(null);
                        Txtnombres.setBackground(null);
                        Txtnombres.setText(nombres);
                        Txtapellidos.setBackground(null);
                        Txtapellidos.setText(apellidos);
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setShimmer(null);
                        SharedPreferences.Editor editor = dataNombre.edit();
                        SharedPreferences.Editor editor2 = dataApellido.edit();
                        editor.putString("Mi llave",Txtnombres.getText().toString());
                        editor2.putString("Mi llave 2",Txtapellidos.getText().toString());
                        editor.apply();
                        editor2.apply();
                    } else {
                        Toast.makeText(MenuAukdeliver.this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MenuAukdeliver.this, "Error de Base de datos", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            LinearShimmer.setBackground(null);
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setShimmer(null);
            Txtnombres.setText(mypref);
            Txtapellidos.setText(mypref2);
        }
    }

    public void startLocacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (gpsActive()) {
                    mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                } else {
                    showAlertDialog();
                }

            } else {
                checkLocationPermision();
            }

        } else {
            if (gpsActive()) {
                mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            } else {
                showAlertDialog();
            }

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (gpsActive()) {
                        mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                       // startLocationService();
                    } else {
                        showAlertDialog();
                    }
                } else {
                    checkLocationPermision();
                }
            } else {
                checkLocationPermision();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == SETTINGS_REQUEST_CODE && gpsActive()) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mFusedLocation.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());

            } else {
                showAlertDialog();
            }
    }

    private void getPhotoUsuario(){
        String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name);
        File directory = new File(root);
        if(directory.exists()){
            // Mostrar en el CircleImageView
            File toLoad = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name) + "/profile.jpg");
            Glide.with(MenuAukdeliver.this).load(toLoad).into(foto);
        }
        else {
            String id = mAuth.getId();
            mDatabase.child("Usuarios").child("Aukdeliver").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("foto")) {
                        //Obtener el url y setearlo en la imagem
                        String setFoto = dataSnapshot.child("foto").getValue().toString();
                        Glide.with(MenuAukdeliver.this).load(setFoto).into(foto);
                        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name) + "/";
                        final File dir = new File(dirPath);
                        final String fileName = "profile.jpg";
                        Glide.with(MenuAukdeliver.this)
                                .load(setFoto)
                                .into(new CustomTarget<Drawable>() {
                                    @Override
                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                        Bitmap bitmap = ((BitmapDrawable)resource).getBitmap();
                                        //Toast.makeText(MenuAdmin.this, "Guardando imagen...", Toast.LENGTH_SHORT).show();
                                        saveStorageImage.saveImage(bitmap, dir, fileName);
                                    }

                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {

                                    }

                                    @Override
                                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                        super.onLoadFailed(errorDrawable);
                                        //Toast.makeText(MenuAdmin.this, "Failed to Download Image! Please try again later.", Toast.LENGTH_SHORT).show();
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

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Por favor activa tu ubicación para continuar")
                .setCancelable(false)
                .setPositiveButton("Configurar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDialog.show();
                        mDialog.setMessage("Accediendo...");
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), SETTINGS_REQUEST_CODE);
                    }
                })
                .setNegativeButton("Refrescar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(getIntent());
                    }
                })
                .create().show();
    }

    public void show_popup(View view) {
        Context wrapper = new ContextThemeWrapper(this, R.style.popupThemeGreen);
        PopupMenu popupMenu = new PopupMenu(wrapper, view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }


    void logout() {
        /*final SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putString("", "");
        editor.apply();*/
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().apply();
        deleteDirectory();
        mAuthProviders.Logout();
        Intent intent = new Intent(MenuAukdeliver.this, Inicio.class);
        startActivity(intent);
        finish();
        mDialog.dismiss();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                startActivity(new Intent(MenuAukdeliver.this, PerfilAukdeliver.class));
                return true;

            case R.id.item2:
                mDialog.show();
                mDialog.setMessage("Cerrando sesión...");
                stopJobSchedule();
                DeleteDataAdmin();
                deleteTokenFCM();
                logout();
                return true;
            default:
                return false;
        }
    }


    void generarToken() {
        mTokenProvider.create(mAuth.getId());
    }

    private void deleteTokenFCM(){
        mDatabase.child("Tokens").child(mAuth.getId()).removeValue();
    }

    private void startJobSchedule(){
        ComponentName componentName = new ComponentName(getApplicationContext(), JobServiceMonitoreo.class);
        JobInfo info;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            info = new JobInfo.Builder(ID_SERVICIO, componentName)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setPersisted(true)
                    .setBackoffCriteria(1000,JobInfo.BACKOFF_POLICY_EXPONENTIAL)
                    //.setMinimumLatency(1000)
                    .build();

        }else{
            info = new JobInfo.Builder(ID_SERVICIO, componentName)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setPersisted(true)
                    .setBackoffCriteria(1000,JobInfo.BACKOFF_POLICY_EXPONENTIAL)
                    //.setPeriodic(1000)
                    .build();
        }
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultado = scheduler.schedule(info);
        if(resultado == JobScheduler.RESULT_SUCCESS){
            Log.d("TAG", "Job Acabado");
        }else{
            Log.d("TAG", "Job ha fallado");
        }
    }

    private void stopJobSchedule(){
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(ID_SERVICIO);
    }


    private void showCronometro(){
        mDatabase.child("Tiempo").child(mAuth.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String time = dataSnapshot.child("tiempo").getValue().toString();
                    txtCronometro.setText(time);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void deleteDirectory(){
        String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name);
        File directory = new File(root);
        File photo = new File(root,"/profile.jpg");
        photo.delete();
        directory.delete();
    }

    private void DeleteDataAdmin() {
        SharedPreferences.Editor editor = dataNombre.edit();
        SharedPreferences.Editor editor2 = dataApellido.edit();
        editor.remove("Mi llave");
        editor2.remove("Mi llave 2");
        editor.apply();
        editor2.apply();
    }


    private void verify(){
        if (!verifyPermissions()) {
            return;
        }
    }

    public Boolean verifyPermissions() {
        // Esto devolverá el estado actual
        int permissionExternalMemory = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionExternalMemory != PackageManager.PERMISSION_GRANTED) {
            String[] STORAGE_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //Si no se otorga el permiso, solicite permiso en tiempo real.
            ActivityCompat.requestPermissions(this, STORAGE_PERMISSIONS, 1);
            return false;
        }
        return true;
    }

    private void deleteAppData() {
        try {
            // clearing app data
            String packageName = getApplicationContext().getPackageName();
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("pm clear "+packageName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        deleteCache.trimCache(MenuAukdeliver.this);
        startLocacion();
        unregisterReceiver(networkReceiver);
        unregisterReceiver(gpsReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        deleteCache.trimCache(MenuAukdeliver.this);
        mDialog.dismiss();
        startLocacion();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        deleteCache.trimCache(MenuAukdeliver.this);
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);
        registerReceiver(gpsReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        deleteCache.trimCache(this);
        super.onDestroy();
    }

}