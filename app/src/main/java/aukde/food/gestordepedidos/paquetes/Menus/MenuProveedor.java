package aukde.food.gestordepedidos.paquetes.Menus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.shimmer.ShimmerFrameLayout;
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
import aukde.food.gestordepedidos.paquetes.Actividades.Inicio;
import aukde.food.gestordepedidos.paquetes.Menus.Perfiles.PerfilAdmin;
import aukde.food.gestordepedidos.paquetes.Menus.Perfiles.Perfilproveedoraukde;
import aukde.food.gestordepedidos.paquetes.Productos.Default.AgregarProductoPorDefecto;
import aukde.food.gestordepedidos.paquetes.Productos.Default.ListaProductosDefault;
import aukde.food.gestordepedidos.paquetes.Productos.MenuAddProduct;
import aukde.food.gestordepedidos.paquetes.Productos.Solicitud.FichaDeSolicitud;
import aukde.food.gestordepedidos.paquetes.Productos.Solicitud.MenuListaDeSolicitudes;
import aukde.food.gestordepedidos.paquetes.Providers.AuthProviders;
import aukde.food.gestordepedidos.paquetes.Providers.TokenProvider;
import aukde.food.gestordepedidos.paquetes.Utils.DeleteCache;
import aukde.food.gestordepedidos.paquetes.Utils.SaveStorageImage;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class MenuProveedor extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private AuthProviders mAuthProviders;
    private ProgressDialog mDialog;
    SharedPreferences mSharedPreference;
    private DatabaseReference mDatabase;
    private TokenProvider mTokenProvider;
    private AuthProviders mAuth;
    private LinearLayout LinearShimmer;
    private TextView Txtnombres, Txtapellidos , TxtNombreEmpresa , TxtCategoria , TxtTituloCat , txtUrlPhoto;
    private ShimmerFrameLayout shimmerFrameLayout;
    private final static int ID_SERVICIO = 99;
    private Vibrator vibrator;
    private static final int GALLERY = 1;
    long tiempo = 100;
    private CircleImageView foto;
    private Button addProducto , listProducto , solicitarDelivery , btnListaSolicitud,btnPerfilX;

    DeleteCache deleteCache;

    SharedPreferences dataNombre;
    SharedPreferences dataApellido;
    SharedPreferences datanombreEmpresa;
    SharedPreferences dataCategoria;

    SaveStorageImage saveStorageImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        stopJobSchedule();
        setTheme(R.style.AppThemeRedCake);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_proveedor);
        mDialog = new ProgressDialog(this, R.style.ThemeOverlay);
        mAuthProviders = new AuthProviders();
        solicitarDelivery = findViewById(R.id.btnSolicitarDelivery);
        btnListaSolicitud = findViewById(R.id.btnListaDeSolicitudes);
        mAuth = new AuthProviders();
        foto = findViewById(R.id.fotodefault);
        addProducto = findViewById(R.id.btnAgregarProducto);
        listProducto = findViewById(R.id.btnListaProductos);
        txtUrlPhoto = findViewById(R.id.urlPhotoProv);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mSharedPreference = getApplicationContext().getSharedPreferences("tipoUsuario",MODE_PRIVATE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mTokenProvider = new TokenProvider();

        Txtnombres = findViewById(R.id.txtNombres);
        Txtapellidos = findViewById(R.id.txtApellidos);
        TxtNombreEmpresa = findViewById(R.id.txtEmpresa);
        TxtCategoria = findViewById(R.id.txtCategoria);
        TxtTituloCat = findViewById(R.id.txtTituloCat);
        btnPerfilX = findViewById(R.id.btnPerfil);

        LinearShimmer = findViewById(R.id.linearShimmer);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        shimmerFrameLayout.startShimmer();

        deleteCache = new DeleteCache();
        dataNombre = PreferenceManager.getDefaultSharedPreferences(this);
        dataApellido = PreferenceManager.getDefaultSharedPreferences(this);
        datanombreEmpresa= PreferenceManager.getDefaultSharedPreferences(this);
        dataCategoria=PreferenceManager.getDefaultSharedPreferences(this);
        saveStorageImage = new SaveStorageImage();

        addProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                String cat = TxtCategoria.getText().toString();
                if (!cat.equals("")) {
                    mDialog.show();
                    mDialog.setCancelable(false);
                    mDialog.setMessage("Cargando...");
                    Intent intent = new Intent(MenuProveedor.this, MenuAddProduct.class);
                    intent.putExtra("keyProduct", cat);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toasty.info(MenuProveedor.this, "Espere un momento...", Toast.LENGTH_SHORT).show();
                }

            }
        });

        listProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.show();
                mDialog.setCancelable(false);
                mDialog.setMessage("Cargando...");
                vibrator.vibrate(tiempo);
                startActivity(new Intent(MenuProveedor.this, ListaProductosDefault.class));
                finish();
            }
        });

        solicitarDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                String urlPhoto = txtUrlPhoto.getText().toString();
                String cat = TxtNombreEmpresa.getText().toString();
                if (!cat.equals("")) {
                    mDialog.show();
                    mDialog.setCancelable(false);
                    mDialog.setMessage("Cargando...");
                    Intent intent = new Intent(MenuProveedor.this, FichaDeSolicitud.class);
                    intent.putExtra("keyProduct", cat);
                    intent.putExtra("keyPhoto",urlPhoto);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toasty.info(MenuProveedor.this, "Espere un momento...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnListaSolicitud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.show();
                mDialog.setCancelable(false);
                mDialog.setMessage("Cargando...");
                vibrator.vibrate(tiempo);
                startActivity(new Intent(MenuProveedor.this, MenuListaDeSolicitudes.class));
                finish();
            }
        });
        btnPerfilX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(tiempo);
                mDialog.show();
                mDialog.setCancelable(false);
                mDialog.setMessage("Cargando...");
                startActivity(new Intent(MenuProveedor.this, Perfilproveedoraukde.class));
            }
        });

        Mapping();
        generarToken();
        getDataUser();
        getPhotoUsuario();
        verify();
    }


    public void show_popup(View view){

        Context wrapper = new ContextThemeWrapper(this, R.style.popupThemePink);
        PopupMenu popupMenu = new PopupMenu(wrapper,view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();

    }

    void logout() {
        /*final SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putString("", "");
        editor.apply();*/
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().apply();
        mAuthProviders.Logout();
        Intent intent = new Intent(MenuProveedor.this, Inicio.class);
        deleteCache.trimCache(this);
        startActivity(intent);
        finish();
        mDialog.dismiss();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY);
                return true;

            case R.id.item2:
                mDialog.show();
                mDialog.setMessage("Cerrando sesión...");
                DeleteDataProveedor();
                deleteDirectory();
                deleteTokenFCM();
                logout();
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY && resultCode == RESULT_OK){
            mDialog.setMessage("Actualizando foto de perfil...");
            mDialog.setCancelable(false);
            mDialog.show();
            Uri uri2 = data.getData();
            final String id = mAuthProviders.getId();
            final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("PhotoProveedor").child(mAuthProviders.getId()+".jpg");
            String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name);
            File directory = new File(root);
            File photo = new File(root,"/profile.jpg");
            photo.delete();
            directory.delete();

            filepath.putFile(uri2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(final Uri uri2) {
                            DatabaseReference imagestore = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Proveedor").child(id);
                            HashMap<String,Object> fotoMap = new HashMap<>();
                            fotoMap.put("foto" , String.valueOf(uri2));

                            imagestore.updateChildren(fotoMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    getPhotoUsuario();
                                    String imageURL = String.valueOf(uri2);
                                    String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name) + "/";
                                    final File dir = new File(dirPath);
                                    final String fileName = "profile.jpg";

                                    Glide.with(MenuProveedor.this)
                                            .load(imageURL)
                                            .into(new CustomTarget<Drawable>() {
                                                @Override
                                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                    Bitmap bitmap = ((BitmapDrawable)resource).getBitmap();
                                                    //Toast.makeText(MenuAdmin.this, "Guardando imagen...", Toast.LENGTH_SHORT).show();
                                                    saveStorageImage.saveImage(bitmap, dir, fileName);
                                                    deleteCache.trimCache(MenuProveedor.this);
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
                                    Toasty.success(MenuProveedor.this, "Foto de perfil actualizada!", Toast.LENGTH_LONG,true).show();
                                }
                            });

                        }
                    });
                }
            });
        }
    }

    private void getPhotoUsuario(){
        String id = mAuth.getId();
        mDatabase.child("Usuarios").child("Proveedor").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("foto")){
                    //Obtener el url y setearlo en la imagem
                    String setFoto = dataSnapshot.child("foto").getValue().toString();
                    Glide.with(MenuProveedor.this).load(setFoto).into(foto);
                    txtUrlPhoto.setText(setFoto);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Mapping(){
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    mDatabase.child("Usuarios").child("Proveedor").child(mAuthProviders.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                String stLatitud = dataSnapshot.child("latitud").getValue().toString();
                                String stLongitud= dataSnapshot.child("longitud").getValue().toString();
                                String sNombre = dataSnapshot.child("nombre empresa").getValue().toString();
                                Double latitud = Double.parseDouble(stLatitud);
                                Double longitud = Double.parseDouble(stLongitud);
                                Map<String , Object> map = new HashMap<>();
                                map.put("0",latitud);
                                map.put("1",longitud);
                                mDatabase.child("MapaProveedor").child(mAuthProviders.getId()).child("l").setValue(map);
                                Map<String , Object> map2 = new HashMap<>();
                                map2.put("nombre",sNombre);
                                map2.put("g","6myb3krm08");
                                mDatabase.child("MapaProveedor").child(mAuthProviders.getId()).updateChildren(map2);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getDataUser() {
        String mypref = dataNombre.getString("Mi llave","Vacío");
        String mypref2 = dataApellido.getString("Mi llave 2","Vacío");
        String mypref3=datanombreEmpresa.getString("Mi llave 3","Vacío");
        String mypref4=dataCategoria.getString("Mi llave 4","Vacío");

        if(mypref.equals("Vacío")){
            String id = mAuth.getId();
            mDatabase.child("Usuarios").child("Proveedor").child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String nombres = dataSnapshot.child("nombres").getValue().toString();
                        String apellidos = dataSnapshot.child("apellidos").getValue().toString();
                        String nombreEmpresa = dataSnapshot.child("nombre empresa").getValue().toString();
                        String categoria = dataSnapshot.child("categoria").getValue().toString();
                        LinearShimmer.setBackground(null);
                        Txtnombres.setBackground(null);
                        Txtnombres.setText(nombres);
                        Txtapellidos.setBackground(null);
                        Txtapellidos.setText(apellidos);
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setShimmer(null);
                        TxtNombreEmpresa.setText(nombreEmpresa);
                        TxtTituloCat.setVisibility(View.VISIBLE);
                        TxtCategoria.setText(categoria);

                        SharedPreferences.Editor editor = dataNombre.edit();
                        SharedPreferences.Editor editor2 = dataApellido.edit();
                        SharedPreferences.Editor editor3 = datanombreEmpresa.edit();
                        SharedPreferences.Editor editor4 = dataCategoria.edit();

                        editor.putString("Mi llave",Txtnombres.getText().toString());
                        editor2.putString("Mi llave 2",Txtapellidos.getText().toString());
                        editor3.putString("Mi llave 3",TxtNombreEmpresa.getText().toString());
                        editor4.putString("Mi llave 4",TxtCategoria.getText().toString());

                        editor.apply();
                        editor2.apply();
                        editor3.apply();
                        editor4.apply();


                    } else {
                        Toast.makeText(MenuProveedor.this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MenuProveedor.this, "Error de Base de datos", Toast.LENGTH_SHORT).show();
                }
            });

        }
        else{
            LinearShimmer.setBackground(null);
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setShimmer(null);
            Txtnombres.setText(mypref);
            Txtapellidos.setText(mypref2);
            TxtNombreEmpresa.setText(mypref3);
            TxtTituloCat.setVisibility(View.VISIBLE);
            TxtCategoria.setText(mypref4);
        }
    }

    void generarToken() {
        mTokenProvider.create(mAuth.getId());
    }

    private void deleteTokenFCM(){
        mDatabase.child("Tokens").child(mAuth.getId()).removeValue();
    }

    private void stopJobSchedule(){
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(ID_SERVICIO);
    }
    private void DeleteDataProveedor() {
        SharedPreferences.Editor editor = dataNombre.edit();
        SharedPreferences.Editor editor2 = dataApellido.edit();
        SharedPreferences.Editor editor3 = datanombreEmpresa.edit();
        SharedPreferences.Editor editor4 = dataCategoria.edit();

        editor.remove("Mi llave");
        editor2.remove("Mi llave 2");
        editor3.remove("Mi llave 3");
        editor4.remove("Mi llave 4");
        editor.apply();
        editor2.apply();
        editor3.apply();
        editor4.apply();
    }

    private void verify(){
        if (!verifyPermissions()) {
            return;
        }
    }
    public Boolean verifyPermissions() {
        // Esto devolverá el estado actual
        int permissionExternalMemory = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionExternalMemory != PackageManager.PERMISSION_GRANTED) {
            String[] STORAGE_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //Si no se otorga el permiso, solicite permiso en tiempo real.
            ActivityCompat.requestPermissions(this, STORAGE_PERMISSIONS, 1);
            return false;
        }

        return true;

    }

    private void deleteDirectory(){
        String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name);
        File directory = new File(root);
        File photo = new File(root,"/profile.jpg");
        photo.delete();
        directory.delete();
    }
    @Override
    protected void onResume() {
        deleteCache.trimCache(MenuProveedor.this);
        super.onResume();
        //mDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        deleteCache.trimCache(this);
        super.onDestroy();
    }


}