package aukde.food.gestordepedidos.paquetes.Menus.ListaUsuarios;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Modelos.ListaAdministrador;
import aukde.food.gestordepedidos.paquetes.Providers.NotificationProvider;
import aukde.food.gestordepedidos.paquetes.Providers.TokenProvider;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class DetalleAdministrador extends AppCompatActivity {

    TextView txtNombre_detalle,txtApellido_detalle,txtNombre_usuario_detalle,txtCorreo_electronico_detalle,txtDni_detalle,
            txtTele_detalle;
    Button btnEditar_admin_detalle;
    CircleImageView clrPhotoDetalleAdmin;


    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    private ProgressDialog mDialog;
    private Vibrator vibrator;
    long tiempo = 100;
    private NotificationProvider notificationProvider;
    private TokenProvider tokenProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeDark);
        setContentView(R.layout.activity_detalle_administrador);

        txtNombre_detalle=findViewById(R.id.detalleAdminNombre);
        txtApellido_detalle=findViewById(R.id.detalleAdminApellido);
        //txtNombre_usuario_detalle=findViewById(R.id.detalleAdminUsuario);
        txtCorreo_electronico_detalle=findViewById(R.id.detalleAdminCorreo);
        txtDni_detalle=findViewById(R.id.detalleAdminDni);
        txtTele_detalle=findViewById(R.id.detalleAdminTelefono);

        clrPhotoDetalleAdmin=findViewById(R.id.photoDetalleAdmin);


        btnEditar_admin_detalle=findViewById(R.id.btnEditarAdmin);

        tokenProvider = new TokenProvider();
        mDialog = new ProgressDialog(this,R.style.ThemeOverlay);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        notificationProvider = new NotificationProvider();

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

        btnEditar_admin_detalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(tiempo);
                Toasty.success(DetalleAdministrador.this, "Activity", Toast.LENGTH_SHORT,true).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mDialog.dismiss();
    }
}