package aukde.food.gestordepedidos.paquetes.Menus.ListaUsuarios;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Actividades.Usuarios.EditarAdministrador;
import aukde.food.gestordepedidos.paquetes.Actividades.Usuarios.EditarProveedor;
import aukde.food.gestordepedidos.paquetes.Modelos.ListaProveedor;
import aukde.food.gestordepedidos.paquetes.Providers.NotificationProvider;
import aukde.food.gestordepedidos.paquetes.Providers.TokenProvider;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class DetalleProveedor extends AppCompatActivity {

    TextView txtNombreProveedor , txtApellidoProveedor , txtNombreUsuarioProveedor ,
            txtDniProveedor , txtTelefonoProveedor , txtCorreoProveedor ,txtRucProveedor, txtNombreEmpresaProveedor ;

    CircleImageView clrPhotoProveedor;
    Button BtnEditarProveedor;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    private ProgressDialog mDialog;
    private Vibrator vibrator;
    long tiempo = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeDark);
        setContentView(R.layout.activity_detalle_proveedor);

        mDialog = new ProgressDialog(this,R.style.ThemeOverlay);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        clrPhotoProveedor = findViewById(R.id.photoDetalleProveedor);

        txtNombreProveedor= findViewById(R.id.detalleProveedorNombre);
        txtApellidoProveedor=findViewById(R.id.detalleProveedorApellido);

        txtNombreUsuarioProveedor=findViewById(R.id.detalleProveedorUsuario);
        txtCorreoProveedor=findViewById(R.id.detalleProveedorCorreo);
        txtDniProveedor=findViewById(R.id.detalleProveedorDni);
        txtTelefonoProveedor=findViewById(R.id.detalleProveedorTelefono);
        //txtNombreEmpresaProveedor=findViewById(R.id.detalleProveedorNombreEmpresa);
        txtRucProveedor=findViewById(R.id.detalleProveedorRuc);

        BtnEditarProveedor=findViewById(R.id.btnEditarProveedor);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        ListaProveedor ProveedorDefault = (ListaProveedor) bundle.getSerializable("key");
        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add(ProveedorDefault.getFoto());
        arrayList.add(ProveedorDefault.getNombres());
        arrayList.add(ProveedorDefault.getApellidos());
        arrayList.add(ProveedorDefault.getUsername());
        arrayList.add(ProveedorDefault.getEmail());
        arrayList.add(ProveedorDefault.getDni());
        arrayList.add(ProveedorDefault.getTelefono());
        arrayList.add(ProveedorDefault.getNombre_Empresa());
        arrayList.add(ProveedorDefault.getRuc());


        Picasso
                .get()
                .load(arrayList.get(0))
                .fit()
                .error(R.drawable.ic_error)
                .placeholder(R.drawable.loaderrosa)
                .centerCrop()
                .into(clrPhotoProveedor);

        txtNombreProveedor.setText(arrayList.get(1));
        txtApellidoProveedor.setText(arrayList.get(2));
        txtNombreUsuarioProveedor.setText(arrayList.get(3));
        txtCorreoProveedor.setText(arrayList.get(4));
        txtDniProveedor.setText(arrayList.get(5));
        txtTelefonoProveedor.setText(arrayList.get(6));
        //txtNombreEmpresaProveedor.setText(arrayList.get(7));
        txtRucProveedor.setText(arrayList.get(8));


        BtnEditarProveedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                getDataUser();
            }
        });

    }

    private void getDataUser(){

        Query mReference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Administrador").orderByChild("nombres").equalTo(txtNombreProveedor.getText().toString());
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot childsnapsot : dataSnapshot.getChildren()){
                        String name = childsnapsot.getKey();
                        Intent intent = new Intent(DetalleProveedor.this, EditarProveedor.class);
                        intent.putExtra("key",name);
                        startActivity(intent);
                    }

                }
                else {
                    Toast.makeText(DetalleProveedor.this, "error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void onBackPressed() {
        super.onBackPressed();
        mDialog.dismiss();
    }
}