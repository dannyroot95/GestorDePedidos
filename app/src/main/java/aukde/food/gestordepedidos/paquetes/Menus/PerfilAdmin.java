package aukde.food.gestordepedidos.paquetes.Menus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Inclusiones.MiToolbar;
import aukde.food.gestordepedidos.paquetes.Providers.AdminProvider;
import aukde.food.gestordepedidos.paquetes.Providers.AuthProviders;
import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilAdmin extends AppCompatActivity {

    private CircleImageView photoPerfil;
    private Button btnUpdateX;
    private TextInputEditText txtNombres , txtApellidos;
    private AdminProvider mAdminProvider;
    private AuthProviders mAuthProviders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_admin);
        MiToolbar.Mostrar(this,"Actualizar Perfil",true);

        mAdminProvider = new AdminProvider();
        mAuthProviders = new AuthProviders();

        photoPerfil = findViewById(R.id.fotodefault);
        btnUpdateX = findViewById(R.id.btnUpdate);
        txtNombres = findViewById(R.id.AdminNombres);
        txtApellidos = findViewById(R.id.AdminApellidos);

        btnUpdateX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActualzarPerfil();
            }
        });
        ObtenerDataUser();

    }

    private void ObtenerDataUser(){
        mAdminProvider.getAdminData(mAuthProviders.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nombre = dataSnapshot.child("nombres").getValue().toString();
                String apellido = dataSnapshot.child("apellidos").getValue().toString();
                txtNombres.setText(nombre);
                txtApellidos.setText(apellido);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ActualzarPerfil() {
    }

}