package aukde.food.gestordepedidos.paquetes.Menus;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.app.ProgressDialog;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Actividades.Inicio;
import aukde.food.gestordepedidos.paquetes.Actividades.Pedidos.ListaDePedidos;
import aukde.food.gestordepedidos.paquetes.Actividades.Pedidos.RealizarPedido;
import aukde.food.gestordepedidos.paquetes.Actividades.Registros.MenuRegistros;
import aukde.food.gestordepedidos.paquetes.Mapas.MapaProveedores;
import aukde.food.gestordepedidos.paquetes.Mapas.MonitoreoRepartidor;
import aukde.food.gestordepedidos.paquetes.Menus.Perfiles.Cronometro;
import aukde.food.gestordepedidos.paquetes.Menus.Perfiles.PerfilAdmin;
import aukde.food.gestordepedidos.paquetes.Providers.AuthProviders;
import aukde.food.gestordepedidos.paquetes.Providers.TokenProvider;
import aukde.food.gestordepedidos.paquetes.Utils.DeleteCache;
import aukde.food.gestordepedidos.paquetes.Utils.SaveStorageImage;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class MenuAdmin extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private AuthProviders mAuthProviders;
    private ProgressDialog mDialog;
    private CircleImageView foto;
    private DatabaseReference mDatabase;
    SharedPreferences mSharedPreference;
    private Button btnHacerPedido , btnRegistrarUsuarios , btnListaPedidos
            , btnMapaRepartidores , btnPerfilX , btnMapProveedor , btnPrueba , btnFinanza;
    private TextView Txtnombres , Txtapellidos;
    private ShimmerFrameLayout shimmerFrameLayout;
    private LinearLayout LinearShimmer;
    private TokenProvider mTokenProvider;
    private AuthProviders mAuth;
    Cronometro cronometro;
    private Vibrator vibrator;
    SharedPreferences dataNombre;
    SharedPreferences dataApellido;
    private static final int GALLERY = 1;
    SaveStorageImage saveStorageImage;
    long tiempo = 100;
    DeleteCache deleteCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_admin);
        deleteCache = new DeleteCache();
        saveStorageImage = new SaveStorageImage();
        foto = findViewById(R.id.fotodefault);
        dataNombre = PreferenceManager.getDefaultSharedPreferences(this);
        dataApellido = PreferenceManager.getDefaultSharedPreferences(this);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        mAuthProviders = new AuthProviders();
        cronometro = new Cronometro();
        btnFinanza = findViewById(R.id.btnFinanzas);
        mDialog = new ProgressDialog(this,R.style.ThemeOverlay);
        mSharedPreference = getApplicationContext().getSharedPreferences("tipoUsuario",MODE_PRIVATE);
        btnHacerPedido = findViewById(R.id.btnHacerPedido);
        btnRegistrarUsuarios = findViewById(R.id.btnRegUsers);
        btnMapaRepartidores = findViewById(R.id.btnMonitoreoRepartidor);
        btnMapProveedor = findViewById(R.id.btnMapaProveedor);
        btnPrueba = findViewById(R.id.btnListaDeUsuarios);
        btnListaPedidos = findViewById(R.id.botnListaDePedidos);
        btnPerfilX = findViewById(R.id.btnPerfil);
        Txtnombres = findViewById(R.id.txtNombres);
        Txtapellidos = findViewById(R.id.txtApellidos);
        LinearShimmer = findViewById(R.id.linearShimmer);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        shimmerFrameLayout.startShimmer();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mTokenProvider = new TokenProvider();
        mAuth = new AuthProviders();
        getDataUser();
        btnHacerPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                mDialog.show();
                mDialog.setCancelable(false);
                mDialog.setMessage("Cargando...");
                startActivity(new Intent(MenuAdmin.this, RealizarPedido.class));
                finish();
            }
        });
        btnRegistrarUsuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                mDialog.show();
                mDialog.setCancelable(false);
                mDialog.setMessage("Cargando...");
                startActivity(new Intent(MenuAdmin.this, MenuRegistros.class));
                finish();
            }
        });
        btnListaPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                mDialog.show();
                mDialog.setCancelable(false);
                mDialog.setMessage("Cargando...");
                startActivity(new Intent(MenuAdmin.this, ListaDePedidos.class));
            }
        });
        btnMapaRepartidores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                mDialog.show();
                mDialog.setCancelable(false);
                mDialog.setMessage("Cargando...");
                startActivity(new Intent(MenuAdmin.this, MonitoreoRepartidor.class));
                finish();
            }
        });
        btnMapProveedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                mDialog.show();
                mDialog.setCancelable(false);
                mDialog.setMessage("Cargando...");
                startActivity(new Intent(MenuAdmin.this, MapaProveedores.class));
                finish();
            }
        });
        btnPerfilX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                mDialog.show();
                mDialog.setCancelable(false);
                mDialog.setMessage("Cargando...");
                startActivity(new Intent(MenuAdmin.this, PerfilAdmin.class));
            }
        });
        btnPrueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*vibrator.vibrate(tiempo);
                mDialog.show();
                mDialog.setCancelable(false);
                mDialog.setMessage("Cargando...");*/
            }
        });
        btnFinanza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        generarToken();
        getPhotoUsuario();
        verify();
    }

    private void getPhotoUsuario(){
        String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name);
        File directory = new File(root);

        if(directory.exists()){
            // Mostrar en el CircleImageView
            File toLoad = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name) + "/profile.jpg");
            Glide.with(MenuAdmin.this).load(toLoad).into(foto);
        }
        else {
            //Toast.makeText(MenuAdmin.this, "NO Existe!", Toast.LENGTH_SHORT).show();
            String id = mAuth.getId();
            mDatabase.child("Usuarios").child("Administrador").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("foto")){
                        //Obtener el url y setearlo en la imagem
                        String setFoto = dataSnapshot.child("foto").getValue().toString();
                        Glide.with(MenuAdmin.this).load(setFoto).into(foto);
                        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name) + "/";
                        final File dir = new File(dirPath);
                        final String fileName = "profile.jpg";
                        Glide.with(MenuAdmin.this)
                                .load(setFoto)
                                .into(new CustomTarget<Drawable>() {
                                    @Override
                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                        Bitmap bitmap = ((BitmapDrawable)resource).getBitmap();
                                        //Toast.makeText(MenuAdmin.this, "Guardando imagen...", Toast.LENGTH_SHORT).show();
                                        saveStorageImage.saveImage(bitmap, dir, fileName);
                                        deleteCache.trimCache(MenuAdmin.this);
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

    private void getDataUser(){

        String mypref = dataNombre.getString("Mi llave","Vacío");
        String mypref2 = dataApellido.getString("Mi llave 2","Vacío");

        if (mypref.equals("Vacío")){
            String id = mAuth.getId();
            Query query = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Administrador").child(id);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String nombres = dataSnapshot.child("nombres").getValue().toString();
                    String apellidos = dataSnapshot.child("apellidos").getValue().toString();
                    LinearShimmer.setBackground(null);
                    Txtnombres.setBackground(null);
                    Txtnombres.setText(nombres);
                    Txtapellidos.setBackground(null);
                    Txtapellidos.setText(apellidos);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setShimmer(null);
                    SharedPreferences.Editor editor = dataNombre.edit();
                    SharedPreferences.Editor editor2 = dataApellido.edit();
                    editor.putString("Mi llave",Txtnombres.getText().toString());
                    editor2.putString("Mi llave 2",Txtapellidos.getText().toString());
                    editor.apply();
                    editor2.apply();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
        else{
            LinearShimmer.setBackground(null);
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setShimmer(null);
            Txtnombres.setText(mypref);
            Txtapellidos.setText(mypref2);
        }
    }

    public void show_popup(View view){
        Context wrapper = new ContextThemeWrapper(this, R.style.popupThemeBlack);
        PopupMenu popupMenu = new PopupMenu(wrapper,view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    void logout(){
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().apply();
        mAuthProviders.Logout();
        Intent intent = new Intent(MenuAdmin.this, Inicio.class);
        startActivity(intent);
        deleteCache.trimCache(this);
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
                DeleteDataAdmin();
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
            Uri uri = data.getData();
            final String id = mAuthProviders.getId();
            final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("PhotoAdmin").child(mAuthProviders.getId()+".jpg");
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
                            DatabaseReference imagestore = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Administrador").child(id);
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
                                    Glide.with(MenuAdmin.this)
                                            .load(imageURL)
                                            .into(new CustomTarget<Drawable>() {
                                                @Override
                                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                                    Bitmap bitmap = ((BitmapDrawable)resource).getBitmap();
                                                    //Toast.makeText(MenuAdmin.this, "Guardando imagen...", Toast.LENGTH_SHORT).show();
                                                    saveStorageImage.saveImage(bitmap, dir, fileName);
                                                    deleteCache.trimCache(MenuAdmin.this);
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
                                    Toasty.success(MenuAdmin.this, "Foto de perfil actualizada", Toast.LENGTH_LONG,true).show();

                                }
                            });
                        }
                    });
                }
            });
        }
    }

    void generarToken(){
        mTokenProvider.create(mAuth.getId());
    }

    private void deleteTokenFCM(){
       mDatabase.child("Tokens").child(mAuth.getId()).removeValue();
    }

    private void DeleteDataAdmin() {
        SharedPreferences.Editor editor = dataNombre.edit();
        SharedPreferences.Editor editor2 = dataApellido.edit();
        editor.remove("Mi llave");
        editor2.remove("Mi llave 2");
        editor.apply();
        editor2.apply();
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
        deleteCache.trimCache(MenuAdmin.this);
        super.onResume();
        //mDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        deleteCache.trimCache(this);
        super.onDestroy();
    }
}