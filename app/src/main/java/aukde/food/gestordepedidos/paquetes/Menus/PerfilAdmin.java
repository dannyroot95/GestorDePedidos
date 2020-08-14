package aukde.food.gestordepedidos.paquetes.Menus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Inclusiones.MiToolbar;
import aukde.food.gestordepedidos.paquetes.Providers.AdminProvider;
import aukde.food.gestordepedidos.paquetes.Providers.AuthProviders;
import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilAdmin extends AppCompatActivity {

    private CircleImageView photoPerfil;
    private Button btnUpdateX;
    private TextView txtNombres , txtApellidos;
    private AdminProvider mAdminProvider;
    private AuthProviders mAuthProviders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

    }

    private void ObtenerDataUser(){

    }

    private void ActualzarPerfil() {
    }

}