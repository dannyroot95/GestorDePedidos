package aukde.food.gestordepedidos.paquetes.Menus.Perfiles;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnSuccessListener;
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
import aukde.food.gestordepedidos.paquetes.Providers.AukdeliverProvider;
import aukde.food.gestordepedidos.paquetes.Providers.AuthProviders;
import aukde.food.gestordepedidos.paquetes.Providers.ProveedorProvider;
import aukde.food.gestordepedidos.paquetes.Receptor.GpsReceiver;
import aukde.food.gestordepedidos.paquetes.Receptor.NetworkReceiver;
import aukde.food.gestordepedidos.paquetes.Utils.DeleteCache;
import aukde.food.gestordepedidos.paquetes.Utils.SaveStorageImage;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class PerfilProveedor extends AppCompatActivity {
    private CircleImageView photoPerfil , CrMyPhoto;
    private Button btnUpdateX;

    private TextInputEditText txtNombres , txtApellidos;

    private ProveedorProvider mProveedorProvider;
    private AuthProviders mAuthProviders;
    private ProgressDialog mDialog;
    private DatabaseReference mDatabase;
    private final int GALLERY_REQUEST = 1;
    String upNombre;
    String upApellido;

    NetworkReceiver networkReceiver = new NetworkReceiver();
    GpsReceiver gpsReceiver = new GpsReceiver();
    private Vibrator vibrator;
    long tiempo = 100;
    DeleteCache deleteCache;
    SaveStorageImage saveStorageImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeRedCake);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_proveedor);
        deleteCache = new DeleteCache();
        saveStorageImage = new SaveStorageImage();
        mDialog = new ProgressDialog(this,R.style.ThemeOverlay);
        mProveedorProvider = new ProveedorProvider();
        mAuthProviders = new AuthProviders();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        photoPerfil = findViewById(R.id.fotodefaultProv);
        CrMyPhoto = findViewById(R.id.myPhotoProv);
        btnUpdateX = findViewById(R.id.btnUpdateProv);
        txtNombres = findViewById(R.id.ProvNombres);
        txtApellidos = findViewById(R.id.ProvApellidos);

        btnUpdateX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ActualzarPerfil();
                vibrator.vibrate(tiempo);
                Toasty.error(PerfilProveedor.this, "No tiene permisos para cambiar sus nombres", Toast.LENGTH_LONG).show();
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
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            mDialog.setMessage("Actualizando foto de perfil...");
            mDialog.setCancelable(false);
            mDialog.show();

            Uri uri = data.getData();
            final String id = mAuthProviders.getId();
            final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("PhotoProveedor").child(mAuthProviders.getId()+".jpg");
            String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name);

            File directory = new File(root);
            File photo = new File(root,"/profile.jpg");
            photo.delete();
            directory.delete();
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(final Uri uri) {
                            DatabaseReference imagestore = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Proveedor").child(id);
                            HashMap<String,Object> fotoMap = new HashMap<>();
                            fotoMap.put("foto" , String.valueOf(uri));
                            imagestore.updateChildren(fotoMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    getPhotoUsuario();
                                    String imageURL = String.valueOf(uri);
                                    String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name) + "/";
                                    final File dir = new File(dirPath);
                                    final String fileName = "profile.jpg";
                                    Glide.with(PerfilProveedor.this)
                                            .load(imageURL)
                                            .into(new CustomTarget<Drawable>() {
                                                @Override
                                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                    Bitmap bitmap = ((BitmapDrawable)resource).getBitmap();
                                                    //Toast.makeText(MenuAdmin.this, "Guardando imagen...", Toast.LENGTH_SHORT).show();
                                                    saveStorageImage.saveImage(bitmap, dir, fileName);
                                                    deleteCache.trimCache(PerfilProveedor.this);
                                                }

                                                @Override
                                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                                }

                                                @Override
                                                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                                    super.onLoadFailed(errorDrawable);
                                                    //Toast.makeText(MenuAdmin.this, "Failed to Download Image! Please try again later.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    mDialog.dismiss();
                                    Toasty.success(PerfilProveedor.this, "Foto de perfil actualizada", Toast.LENGTH_LONG,true).show();
                                }
                            });
                        }
                    });
                }
            });

        }
    }

    private void ObtenerDataUser(){
        mProveedorProvider.getProveedorData(mAuthProviders.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
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
        String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name);
        File directory = new File(root);
        if(directory.exists()){
            // Mostrar en el CircleImageView
            File toLoad = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name) + "/profile.jpg");
            Glide.with(PerfilProveedor.this).load(toLoad).into(CrMyPhoto);
        }
        else{
            mDatabase.child("Usuarios").child("Proveedor").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("foto")){
                        //Obtener el url y setearlo en la imagem
                        String setFoto = dataSnapshot.child("foto").getValue().toString();
                        Glide.with(PerfilProveedor.this).load(setFoto).into(CrMyPhoto);
                        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name) + "/";
                        final File dir = new File(dirPath);
                        final String fileName = "profile.jpg";
                        Glide.with(PerfilProveedor.this)
                                .load(setFoto)
                                .into(new CustomTarget<Drawable>() {
                                    @Override
                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                        Bitmap bitmap = ((BitmapDrawable)resource).getBitmap();
                                        //Toast.makeText(MenuAdmin.this, "Guardando imagen...", Toast.LENGTH_SHORT).show();
                                        saveStorageImage.saveImage(bitmap, dir, fileName);
                                    }

                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {

                                    }

                                    @Override
                                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                        super.onLoadFailed(errorDrawable);
                                        //Toast.makeText(MenuAdmin.this, "Failed to Download Image! Please try again later.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void guardarNombres(){
        final String id = mAuthProviders.getId();
        final String nombre = txtNombres.getText().toString();
        final String apellido = txtApellidos.getText().toString();
        mDatabase.child("Usuarios").child("Aukdeliver").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,Object> datos = new HashMap<>();
                datos.put("nombres",nombre);
                datos.put("apellidos",apellido);
                mDatabase.child("Usuarios").child("Proveedor").child(id).updateChildren(datos).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toasty.success(PerfilProveedor.this, "Nombres Actualizados", Toast.LENGTH_SHORT,true).show();
                        ObtenerDataUser();
                        mDialog.dismiss();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(PerfilProveedor.this, "Hubo un error al actualizar datos", Toast.LENGTH_SHORT,true).show();
            }
        });
    }
    @Override
    public void onBackPressed(){
        NavUtils.navigateUpFromSameTask(this);
    }
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkReceiver);
        unregisterReceiver(gpsReceiver);
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);
        registerReceiver(gpsReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));
        super.onStart();
    }

}
