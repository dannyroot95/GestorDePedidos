package aukde.food.gestordepedidos.paquetes.Menus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Actividades.Inicio;
import aukde.food.gestordepedidos.paquetes.Actividades.Pedidos.ListaDePedidos;
import aukde.food.gestordepedidos.paquetes.Actividades.Pedidos.ListaPedidosAukdeliver;
import aukde.food.gestordepedidos.paquetes.Providers.AuthProviders;

public class MenuAukdeliver extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private AuthProviders mAuthProviders;
    private ProgressDialog mDialog;
    private Button btnLista;
    SharedPreferences mSharedPreference;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private TextView Txtnombres , Txtapellidos;
    private ShimmerFrameLayout shimmerFrameLayout;
    private LinearLayout LinearShimmer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_aukdeliver);
        mAuthProviders = new AuthProviders();
        mDialog = new ProgressDialog(this);
        mSharedPreference = getApplicationContext().getSharedPreferences("tipoUsuario",MODE_PRIVATE);
        btnLista = findViewById(R.id.btnListaPedidosAukdeliver);
        Txtnombres = findViewById(R.id.txtNombres);
        Txtapellidos = findViewById(R.id.txtApellidos);

        LinearShimmer = findViewById(R.id.linearShimmer);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        shimmerFrameLayout.startShimmer();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuAukdeliver.this, ListaPedidosAukdeliver.class));
            }
        });

        getDataUser();

    }


    private void getDataUser(){
        String id = mAuth.getCurrentUser().getUid();
        mDatabase.child("Usuarios").child("Aukdeliver").child(id).addValueEventListener(new ValueEventListener() {
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
                    Toast.makeText(MenuAukdeliver.this,"Error al cargar datos",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MenuAukdeliver.this,"Error de Base de datos",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void show_popup(View view)
    {
        Context wrapper = new ContextThemeWrapper(this, R.style.popupThemeGreen);
        PopupMenu popupMenu = new PopupMenu(wrapper,view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }


    void logout()
    {
        final SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putString("","");
        editor.apply();
        mAuthProviders.Logout();
        startActivity(new Intent(MenuAukdeliver.this, Inicio.class));
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
}