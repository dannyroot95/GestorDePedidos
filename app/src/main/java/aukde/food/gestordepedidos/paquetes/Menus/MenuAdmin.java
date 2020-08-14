package aukde.food.gestordepedidos.paquetes.Menus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Actividades.Inicio;
import aukde.food.gestordepedidos.paquetes.Actividades.Logins.LoginAdmin;
import aukde.food.gestordepedidos.paquetes.Actividades.Pedidos.ListaDePedidos;
import aukde.food.gestordepedidos.paquetes.Actividades.Pedidos.RealizarPedido;
import aukde.food.gestordepedidos.paquetes.Actividades.Registros.MenuRegistros;
import aukde.food.gestordepedidos.paquetes.Mapas.MonitoreoRepartidor;
import aukde.food.gestordepedidos.paquetes.Providers.AuthProviders;
import aukde.food.gestordepedidos.paquetes.Providers.TokenProvider;

public class MenuAdmin extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private AuthProviders mAuthProviders;
    private ProgressDialog mDialog;
    private DatabaseReference mDatabase;
    SharedPreferences mSharedPreference;
    private Button btnHacerPedido , btnRegistrarUsuarios , btnListaPedidos , btnMapaRepartidores;
    private TextView Txtnombres , Txtapellidos;
    private ShimmerFrameLayout shimmerFrameLayout;
    private LinearLayout LinearShimmer;
    private TokenProvider mTokenProvider;
    private AuthProviders mAuth;
    private Vibrator vibrator;
    long tiempo = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_admin);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mAuthProviders = new AuthProviders();
        mDialog = new ProgressDialog(this,R.style.ThemeOverlay);
        mSharedPreference = getApplicationContext().getSharedPreferences("tipoUsuario",MODE_PRIVATE);
        btnHacerPedido = findViewById(R.id.btnHacerPedido);
        btnRegistrarUsuarios = findViewById(R.id.btnRegUsers);
        btnMapaRepartidores = findViewById(R.id.btnMonitoreoRepartidor);
        btnListaPedidos = findViewById(R.id.botnListaDePedidos);
        Txtnombres = findViewById(R.id.txtNombres);
        Txtapellidos = findViewById(R.id.txtApellidos);

        LinearShimmer = findViewById(R.id.linearShimmer);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        shimmerFrameLayout.startShimmer();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mTokenProvider = new TokenProvider();
        mAuth = new AuthProviders();



        btnHacerPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                mDialog.show();
                mDialog.setCancelable(false);
                mDialog.setMessage("Cargando...");
                startActivity(new Intent(MenuAdmin.this, RealizarPedido.class));
                finish();
            }
        });

        btnRegistrarUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                startActivity(new Intent(MenuAdmin.this, MenuRegistros.class));
            }
        });

        btnListaPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                startActivity(new Intent(MenuAdmin.this, ListaDePedidos.class));
            }
        });

        btnMapaRepartidores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                startActivity(new Intent(MenuAdmin.this, MonitoreoRepartidor.class));
            }
        });

        generarToken();
        getDataUser();
    }



    private void getDataUser(){
        String id = mAuth.getId();
        mDatabase.child("Usuarios").child("Administrador").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    String nombres = dataSnapshot.child("nombres").getValue().toString();
                    String apellidos = dataSnapshot.child("apellidos").getValue().toString();
                    LinearShimmer.setBackground(null);
                    Txtnombres.setBackground(null);
                    Txtnombres.setText(nombres);
                    Txtapellidos.setBackground(null);
                    Txtapellidos.setText(apellidos);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setShimmer(null);
                }
                else {
                    Toast.makeText(MenuAdmin.this,"Error al cargar datos",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MenuAdmin.this,"Error de Base de datos",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void show_popup(View view){

        Context wrapper = new ContextThemeWrapper(this, R.style.popupThemeBlack);
        PopupMenu popupMenu = new PopupMenu(wrapper,view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();

    }


    void logout(){
        final SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putString("","");
        editor.apply();
        mAuthProviders.Logout();
        startActivity(new Intent(MenuAdmin.this, Inicio.class));
        finish();
        mDialog.dismiss();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                //startActivityForResult(intent,GALLERY);
                return true;

            case R.id.item2:
                mDialog.show();
                mDialog.setMessage("Cerrando sesión...");
                logout();
                return true;
            default:
                return false;
        }
    }


    void generarToken(){
        mTokenProvider.create(mAuth.getId());
    }


}