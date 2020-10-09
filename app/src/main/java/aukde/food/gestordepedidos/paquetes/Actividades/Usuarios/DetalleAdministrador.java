package aukde.food.gestordepedidos.paquetes.Actividades.Usuarios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
import aukde.food.gestordepedidos.paquetes.Modelos.ListaAdministrador;
import aukde.food.gestordepedidos.paquetes.Providers.NotificationProvider;
import aukde.food.gestordepedidos.paquetes.Providers.TokenProvider;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class DetalleAdministrador extends AppCompatActivity {

    Button btnEditar_admin_detalle;
    ImageView clrPhotoDetalleAdmin;
    EditText txtNombre_detalle;
    TextInputEditText txtApellido_detalle ,
            txtCorreo_electronico_detalle,
            txtDni_detalle, txtTele_detalle;

    LinearLayout mLinearEditarAdmin;

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
        setContentView(R.layout.activity_detalle_administrador);

        txtNombre_detalle=findViewById(R.id.detalleAdminNombre);
        txtNombre_detalle.setEnabled(false);
        txtApellido_detalle=findViewById(R.id.detalleAdminApellido);
        txtApellido_detalle.setEnabled(false);
        txtCorreo_electronico_detalle=findViewById(R.id.detalleAdminCorreo);
        txtCorreo_electronico_detalle.setEnabled(false);
        txtDni_detalle=findViewById(R.id.detalleAdminDni);
        txtDni_detalle.setEnabled(false);
        txtTele_detalle=findViewById(R.id.detalleAdminTelefono);
        txtTele_detalle.setEnabled(false);
        clrPhotoDetalleAdmin=findViewById(R.id.photoDetalleAdmin);
        mFloat = findViewById(R.id.btnEditarPerfilAdmin);
        mFloat.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.quantum_googgreen)));
        mFloat.setVisibility(View.INVISIBLE);
        btnEditar_admin_detalle=findViewById(R.id.btnEditarAdmin);
        mLinearEditarAdmin = findViewById(R.id.linearEditarPerfilAdmin);

        mDialog = new ProgressDialog(this,R.style.ThemeOverlay);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        ListaAdministrador DetalleAdmin = (ListaAdministrador) bundle.getSerializable("key");
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(DetalleAdmin.getFoto());
        arrayList.add(DetalleAdmin.getNombres());
        arrayList.add(DetalleAdmin.getApellidos());
        arrayList.add(DetalleAdmin.getDni());
        arrayList.add(DetalleAdmin.getEmail());
        arrayList.add(DetalleAdmin.getTelefono());

        Picasso
                .get()
                .load(arrayList.get(0))
                .fit()
                .error(R.drawable.ic_error)
                .placeholder(R.drawable.loaderrosa)
                .centerCrop()
                .into(clrPhotoDetalleAdmin);

        txtNombre_detalle.setText(arrayList.get(1));
        txtApellido_detalle.setText(arrayList.get(2));
        txtDni_detalle.setText(arrayList.get(3));
        txtCorreo_electronico_detalle.setText(arrayList.get(4));
        txtTele_detalle.setText(arrayList.get(5));

        mFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFloat.setVisibility(View.INVISIBLE);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
                mLinearEditarAdmin.setLayoutParams(params);
                updateData();
                noFocusableEditText();
            }
        });

        btnEditar_admin_detalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(tiempo);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, 0);
                mLinearEditarAdmin.setLayoutParams(params);
                focusableEditText();
                mFloat.setVisibility(View.VISIBLE);

            }
        });

    }

    private void updateData(){

        mDialog.show();
        mDialog.setMessage("Actualizando datos...");
        mDialog.setCancelable(false);

        Query reference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Administrador")
                .orderByChild("nombres").equalTo(txtNombre_detalle.getText().toString());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    Map<String, Object> map = new HashMap<>();
                    map.put("nombres", txtNombre_detalle.getText().toString());
                    map.put("apellidos", txtApellido_detalle.getText().toString());
                    map.put("dni", txtDni_detalle.getText().toString());
                    map.put("telefono", txtTele_detalle.getText().toString());

                    mDatabase.child("Usuarios").child("Administrador").child(key).updateChildren(map);
                    mDialog.dismiss();
                    Toasty.success(DetalleAdministrador.this, "Datos Actualizados!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(DetalleAdministrador.this, "Error al actualizar datos!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void focusableEditText(){
        txtApellido_detalle.setEnabled(true);
        txtDni_detalle.setEnabled(true);
        txtTele_detalle.setEnabled(true);
        txtNombre_detalle.requestFocus();
    }

    private void noFocusableEditText(){
        txtApellido_detalle.setEnabled(false);
        txtDni_detalle.setEnabled(false);
        txtTele_detalle.setEnabled(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mDialog.dismiss();
    }
}