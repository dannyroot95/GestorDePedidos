package aukde.food.gestordepedidos.paquetes.Menus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Inclusiones.MiToolbar;
import aukde.food.gestordepedidos.paquetes.Modelos.Administrador;
import aukde.food.gestordepedidos.paquetes.Providers.AdminProvider;
import aukde.food.gestordepedidos.paquetes.Providers.AuthProviders;
import aukde.food.gestordepedidos.paquetes.Utils.CompressorBitmapImage;
import aukde.food.gestordepedidos.paquetes.Utils.FileUtil;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import id.zelory.compressor.Compressor;

public class PerfilAdmin extends AppCompatActivity {

    private CircleImageView photoPerfil;
    private Button btnUpdateX;
    private TextInputEditText txtNombres , txtApellidos;
    private AdminProvider mAdminProvider;
    private AuthProviders mAuthProviders;
    private File mImageFile;
    private ProgressDialog mDialog;
    private String urlImage;
    private final int GALLERY_REQUEST = 1;
    String upNombre;
    String upApellido;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_admin);
        MiToolbar.Mostrar(this,"Actualizar Perfil",true);
        mDialog = new ProgressDialog(this,R.style.ThemeOverlay);
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

        photoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGaleria();
            }
        });

        ObtenerDataUser();
    }

    private void abrirGaleria() {

        Intent intentGalery = new Intent(Intent.ACTION_GET_CONTENT);
        intentGalery.setType("image/*");
        startActivityForResult(intentGalery,GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_REQUEST && resultCode==RESULT_OK){
            try {

                mImageFile = FileUtil.from(this,data.getData());
                photoPerfil.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));

            }catch (Exception e){
                Log.d("ERROR","Mensaje"+e.getMessage());
            }
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

        if (!upNombre.equals("") && !upApellido.equals("") && mImageFile != null )
        {
            mDialog.setMessage("Espere un momento...");
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
            guardarImagen();
        }
          else {
            Toasty.error(this, "Ingresa tus nombres y sube foto", Toast.LENGTH_SHORT,true).show();
               }

    }

    private void guardarImagen(){
        byte[] imageByte = CompressorBitmapImage.getImage(this,mImageFile.getPath(),500,500);
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("PhotoAdmin").child(mAuthProviders.getId()+".jpg");
        UploadTask uploadTask = storageReference.putBytes(imageByte);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                   storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                       @Override
                       public void onSuccess(Uri uri) {
                           String image = uri.toString();
                           Administrador administrador = new Administrador();
                           administrador.setFoto(image);
                           administrador.setApellidos(upApellido);
                           administrador.setNombres(upNombre);
                           administrador.setId(mAuthProviders.getId());
                           mAdminProvider.update(administrador).addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {
                                   mDialog.dismiss();
                                   Toasty.success(PerfilAdmin.this, "Datos Actualizados correctamente", Toast.LENGTH_SHORT,true).show();
                               }
                           });
                       }
                   });
                }
                else {
                    Toasty.error(PerfilAdmin.this, "Hubo un error al subir imagen", Toast.LENGTH_SHORT,true).show();
                }
            }
        });
    }

}