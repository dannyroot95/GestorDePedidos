package aukde.food.gestordepedidos.paquetes.Actividades.Usuarios;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Providers.AdminProvider;
import aukde.food.gestordepedidos.paquetes.Providers.AuthProviders;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class EditarAdministrador extends AppCompatActivity {

    private CircleImageView photoPerfil , CrMyPhoto;
    private Button btnUpdateXAdministrador;
    private EditText txtNombres,txtApellidos, txtCorreo,txtDni,txtTelefono;

    private AdminProvider mAdminProvider;
    private AuthProviders mAuthProviders;

    private File mImageFile;
    private ProgressDialog mDialog;
    private DatabaseReference mDatabase;
    private final int GALLERY_REQUEST = 1;
    private Vibrator vibrator;

    long tiempo = 100;
    String upNombre;
    String upApellido;
    String upCorreo;
    String upDni;
    String upTelefono;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_administrador);

        mDialog = new ProgressDialog(this,R.style.ThemeOverlay);
        mAdminProvider = new AdminProvider();
        mAuthProviders = new AuthProviders();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        photoPerfil = findViewById(R.id.fotodefaultAdministrador);
        CrMyPhoto = findViewById(R.id.myPhotoAdministrador);
        btnUpdateXAdministrador=findViewById(R.id.btnEditarAdministrador);
        txtNombres=findViewById(R.id.edtNombreAdministrador);
        txtApellidos=findViewById(R.id.edtApellidoAdministrador);
        txtCorreo=findViewById(R.id.edtCorreoAdministrador);
        txtDni=findViewById(R.id.edtDniAdministrador);
        txtTelefono=findViewById(R.id.edtTelefonoAdministrador);


        btnUpdateXAdministrador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                ActualzarPerfil();
            }
        });

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
            final  String id = getIntent().getStringExtra("key");
            //final String id = mAuthProviders.getId();
            final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("PhotoAdmin").child(mAuthProviders.getId()+".jpg");

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            DatabaseReference imagestore = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Administrador").child(id);
                            HashMap<String,Object> fotoMap = new HashMap<>();
                            fotoMap.put("foto" , String.valueOf(uri));
                            imagestore.updateChildren(fotoMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    getPhotoUsuario();
                                    mDialog.dismiss();
                                    Toasty.success(EditarAdministrador.this, "Foto de perfil actualizada", Toast.LENGTH_LONG,true).show();
                                }
                            });
                        }
                    });
                }
            });
        }
    }



    private void ObtenerDataUser(){
        String name = getIntent().getStringExtra("key");
        mDatabase.child("Usuarios").child("Administrador").child(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nombre = dataSnapshot.child("nombres").getValue().toString();
                String apellido = dataSnapshot.child("apellidos").getValue().toString();
                String correo= dataSnapshot.child("email").getValue().toString();
                String dni=dataSnapshot.child("dni").getValue().toString();
                String telefono=dataSnapshot.child("telefono").getValue().toString();
                txtNombres.setText(nombre);
                txtApellidos.setText(apellido);
                txtCorreo.setText(correo);
                txtDni.setText(dni);
                txtTelefono.setText(telefono);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ActualzarPerfil() {
        upNombre = txtNombres.getText().toString();
        upApellido = txtApellidos.getText().toString();
        upCorreo=txtCorreo.getText().toString();
        upDni=txtDni.getText().toString();
        upTelefono=txtTelefono.getText().toString();

        if(!upNombre.equals("") && !upApellido.equals("") && !upCorreo.equals("") && !upDni.equals("") && !upTelefono.equals("")){
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
        String id = getIntent().getStringExtra("key");
        mDatabase.child("Usuarios").child("Administrador").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("foto")){
                    //Obtener el url y setearlo en la imagem
                    String setFoto = dataSnapshot.child("foto").getValue().toString();
                    Glide.with(EditarAdministrador.this).load(setFoto).into(CrMyPhoto);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void guardarNombres(){
        final  String id = getIntent().getStringExtra("key");
        final String nombre = txtNombres.getText().toString();
        final String apellido = txtApellidos.getText().toString();
        final String correo= txtCorreo.getText().toString();
        final String dni= txtDni.getText().toString();
        final String telefono= txtTelefono.getText().toString();

        mDatabase.child("Usuarios").child("Administrador").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,Object> datos = new HashMap<>();
                datos.put("nombres",nombre);
                datos.put("apellidos",apellido);
                datos.put("email",correo);
                datos.put("dni",dni);
                datos.put("telefono",telefono);

                mDatabase.child("Usuarios").child("Administrador").child(id).updateChildren(datos).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toasty.success(EditarAdministrador.this, "Nombres Actualizados", Toast.LENGTH_SHORT,true).show();
                        ObtenerDataUser();
                        mDialog.dismiss();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(EditarAdministrador.this, "Hubo un error al actualizar datos", Toast.LENGTH_SHORT,true).show();
            }
        });
    }


}