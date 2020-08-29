package aukde.food.gestordepedidos.paquetes.Menus.Perfiles;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
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
import aukde.food.gestordepedidos.paquetes.Inclusiones.MiToolbar;
import aukde.food.gestordepedidos.paquetes.Providers.AdminProvider;
import aukde.food.gestordepedidos.paquetes.Providers.AuthProviders;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class PerfilAdmin extends AppCompatActivity {

    private CircleImageView photoPerfil , CrMyPhoto;
    private Button btnUpdateX;
    private TextInputEditText txtNombres , txtApellidos;
    private AdminProvider mAdminProvider;
    private AuthProviders mAuthProviders;
    private File mImageFile;
    private ProgressDialog mDialog;
    private DatabaseReference mDatabase;
    private final int GALLERY_REQUEST = 1;
    String upNombre;
    String upApellido;
    private Vibrator vibrator;
    long tiempo = 100;



  @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_admin);
        MiToolbar.Mostrar(this,"Actualizar Perfil",true);
        mDialog = new ProgressDialog(this,R.style.ThemeOverlay);
        mAdminProvider = new AdminProvider();
        mAuthProviders = new AuthProviders();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        photoPerfil = findViewById(R.id.fotodefault);
        CrMyPhoto = findViewById(R.id.myPhoto);
        btnUpdateX = findViewById(R.id.btnUpdate);
        txtNombres = findViewById(R.id.AdminNombres);
        txtApellidos = findViewById(R.id.AdminApellidos);

        btnUpdateX.setOnClickListener(new View.OnClickListener() {
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
            final String id = mAuthProviders.getId();
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
                                    Toasty.success(PerfilAdmin.this, "Foto de perfil actualizada", Toast.LENGTH_LONG,true).show();
                                }
                            });
                        }
                    });
                }
            });
        }
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
        upNombre = txtNombres.getText().toString();
        upApellido = txtApellidos.getText().toString();

          if(!upNombre.equals("") && !upApellido.equals("")){
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
        String id = mAuthProviders.getId();
        mDatabase.child("Usuarios").child("Administrador").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("foto")){
                    //Obtener el url y setearlo en la imagem
                    String setFoto = dataSnapshot.child("foto").getValue().toString();
                    Glide.with(PerfilAdmin.this).load(setFoto).into(CrMyPhoto);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void guardarNombres(){
        final String id = mAuthProviders.getId();
        final String nombre = txtNombres.getText().toString();
        final String apellido = txtApellidos.getText().toString();
        mDatabase.child("Usuarios").child("Administrador").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,Object> datos = new HashMap<>();
                datos.put("nombres",nombre);
                datos.put("apellidos",apellido);
                mDatabase.child("Usuarios").child("Administrador").child(id).updateChildren(datos).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toasty.success(PerfilAdmin.this, "Nombres Actualizados", Toast.LENGTH_SHORT,true).show();
                        ObtenerDataUser();
                        mDialog.dismiss();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(PerfilAdmin.this, "Hubo un error al actualizar datos", Toast.LENGTH_SHORT,true).show();
            }
        });
    }

    @Override
    public void onBackPressed(){
        NavUtils.navigateUpFromSameTask(this);
    }
}