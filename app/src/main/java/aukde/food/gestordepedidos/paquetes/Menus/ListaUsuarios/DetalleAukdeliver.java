package aukde.food.gestordepedidos.paquetes.Menus.ListaUsuarios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Actividades.Usuarios.DetalleAdministrador;
import aukde.food.gestordepedidos.paquetes.Modelos.ListaAukdelivery;
import aukde.food.gestordepedidos.paquetes.Providers.NotificationProvider;
import aukde.food.gestordepedidos.paquetes.Providers.TokenProvider;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class DetalleAukdeliver extends AppCompatActivity {

    TextInputEditText  txtApellidoAukdeliver , txtNombreUsuarioAukdeliver ,
            txtDniAukdeliver , txtTelefonoAukdeliver , txtCorreoAukdeliver ,txtCategoriaLicencia ,
            txtMarcaDeMoto , txtPlaca , txtNumLicencia  ;

    ImageView clrPhotoAukdeliver;
    Button BtnEditarAukdeliver;
    EditText txtNombreAukdeliver ;

    LinearLayout mLinearEditarAukdeliver;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    private ProgressDialog mDialog;
    private Vibrator vibrator;
    long tiempo = 100;
    private FloatingActionButton mFloat;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeDark);
        setContentView(R.layout.activity_detalle_aukdeliver);

        mDialog = new ProgressDialog(this,R.style.ThemeOverlay);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        clrPhotoAukdeliver = findViewById(R.id.photoDetalleAukdeliver);
        mFloat = findViewById(R.id.btnEditarPerfilAukdeliver);
        mFloat.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.quantum_googgreen)));
        mFloat.setVisibility(View.INVISIBLE);

        txtNombreAukdeliver= findViewById(R.id.detalleAukdeliverNombre);
        txtApellidoAukdeliver=findViewById(R.id.detalleAukdeliverApellido);
        mLinearEditarAukdeliver = findViewById(R.id.linearEditarPerfilAukdeliver);
        txtMarcaDeMoto = findViewById(R.id.detalleAukdeliverMarcaMoto);
        txtPlaca = findViewById(R.id.detalleAukdeliverPlaca);
        txtNumLicencia = findViewById(R.id.detalleAukdeliverNumeroLic);
        txtNombreUsuarioAukdeliver=findViewById(R.id.detalleAukdeliverUsuario);
        txtCorreoAukdeliver=findViewById(R.id.detalleAukdeliverCorreo);
        txtDniAukdeliver=findViewById(R.id.detalleAukdeliverDni);
        txtTelefonoAukdeliver=findViewById(R.id.detalleAukdeliverTelefono);
        txtCategoriaLicencia=findViewById(R.id.detalleAukdeliverCateL);
        BtnEditarAukdeliver=findViewById(R.id.btnEditarAukdeliver);

        noFocusableEditText();

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        ListaAukdelivery AukdeliverDefault = (ListaAukdelivery) bundle.getSerializable("key");
        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add(AukdeliverDefault.getFoto());
        arrayList.add(AukdeliverDefault.getNombres());
        arrayList.add(AukdeliverDefault.getApellidos());
        arrayList.add(AukdeliverDefault.getUsername());
        arrayList.add(AukdeliverDefault.getDni());
        arrayList.add(AukdeliverDefault.getTelefono());
        arrayList.add(AukdeliverDefault.getEmail());
        arrayList.add(AukdeliverDefault.getCategoria_licencia());
        arrayList.add(AukdeliverDefault.getMarca_de_moto());
        arrayList.add(AukdeliverDefault.getPlaca());
        arrayList.add(AukdeliverDefault.getNumero_Licencia());

        Picasso
                .get()
                .load(arrayList.get(0))
                .fit()
                .error(R.drawable.ic_error)
                .placeholder(R.drawable.loaderrosa)
                .centerCrop()
                .into(clrPhotoAukdeliver);

        txtNombreAukdeliver.setText(arrayList.get(1));
        txtApellidoAukdeliver.setText(arrayList.get(2));
        txtNombreUsuarioAukdeliver.setText(arrayList.get(3));
        txtDniAukdeliver.setText(arrayList.get(4));
        txtTelefonoAukdeliver.setText(arrayList.get(5));
        txtCorreoAukdeliver.setText(arrayList.get(6));
        txtCategoriaLicencia.setText(arrayList.get(7));
        txtMarcaDeMoto.setText(arrayList.get(8));
        txtPlaca.setText(arrayList.get(9));
        txtNumLicencia.setText(arrayList.get(10));


        mFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFloat.setVisibility(View.INVISIBLE);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
                mLinearEditarAukdeliver.setLayoutParams(params);
                updateData();
                noFocusableEditText();
            }
        });

        BtnEditarAukdeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, 0);
                mLinearEditarAukdeliver.setLayoutParams(params);
                focusableEditText();
                mFloat.setVisibility(View.VISIBLE);
            }
        });

    }

    private void updateData(){

        mDialog.show();
        mDialog.setMessage("Actualizando datos...");
        mDialog.setCancelable(false);

        Query reference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Aukdeliver")
                .orderByChild("nombres").equalTo(txtNombreAukdeliver.getText().toString());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    Map<String, Object> map = new HashMap<>();
                    map.put("nombres", txtNombreAukdeliver.getText().toString());
                    map.put("apellidos", txtApellidoAukdeliver.getText().toString());
                    map.put("username", txtNombreUsuarioAukdeliver.getText().toString());
                    map.put("dni", txtDniAukdeliver.getText().toString());
                    map.put("telefono", txtTelefonoAukdeliver.getText().toString());
                    map.put("categoria_licencia", txtCategoriaLicencia.getText().toString());
                    map.put("marca_de_moto", txtMarcaDeMoto.getText().toString());
                    map.put("placa",txtPlaca.getText().toString());
                    map.put("numero_licencia",txtNumLicencia.getText().toString());

                    mDatabase.child("Usuarios").child("Aukdeliver").child(key).updateChildren(map);
                    mDialog.dismiss();
                    Toasty.success(DetalleAukdeliver.this, "Datos Actualizados!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(DetalleAukdeliver.this, "Error al actualizar datos!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void focusableEditText(){
        txtApellidoAukdeliver.setEnabled(true);
        txtApellidoAukdeliver.requestFocus();
        txtDniAukdeliver.setEnabled(true);
        txtTelefonoAukdeliver.setEnabled(true);
        txtMarcaDeMoto.setEnabled(true);
        txtCategoriaLicencia.setEnabled(true);
        txtMarcaDeMoto.setEnabled(true);
        txtPlaca.setEnabled(true);
        txtNumLicencia.setEnabled(true);
    }

    private void noFocusableEditText(){
        txtNombreAukdeliver.setEnabled(false);
        txtApellidoAukdeliver.setEnabled(false);
        txtDniAukdeliver.setEnabled(false);
        txtTelefonoAukdeliver.setEnabled(false);
        txtCorreoAukdeliver.setEnabled(false);
        txtNombreUsuarioAukdeliver.setEnabled(false);
        txtMarcaDeMoto.setEnabled(false);
        txtCategoriaLicencia.setEnabled(false);
        txtMarcaDeMoto.setEnabled(false);
        txtPlaca.setEnabled(false);
        txtNumLicencia.setEnabled(false);
    }

    public void onBackPressed() {
        super.onBackPressed();
        mDialog.dismiss();
    }
}