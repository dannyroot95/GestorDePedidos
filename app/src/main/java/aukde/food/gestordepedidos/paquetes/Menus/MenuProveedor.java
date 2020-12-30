package aukde.food.gestordepedidos.paquetes.Menus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Actividades.Inicio;
import aukde.food.gestordepedidos.paquetes.Productos.Default.AgregarProductoPorDefecto;
import aukde.food.gestordepedidos.paquetes.Productos.Default.ListaProductosDefault;
import aukde.food.gestordepedidos.paquetes.Productos.MenuAddProduct;
import aukde.food.gestordepedidos.paquetes.Productos.Solicitud.FichaDeSolicitud;
import aukde.food.gestordepedidos.paquetes.Productos.Solicitud.MenuListaDeSolicitudes;
import aukde.food.gestordepedidos.paquetes.Providers.AuthProviders;
import aukde.food.gestordepedidos.paquetes.Providers.TokenProvider;
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
    private CardView addProducto , listProducto , solicitarDelivery , btnListaSolicitud;

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

        LinearShimmer = findViewById(R.id.linearShimmer);
        shimmerFrameLayout = findViewById(R.id.shimmer);
        shimmerFrameLayout.startShimmer();

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

        Mapping();
        generarToken();
        getDataUser();
        getPhotoUsuario();

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
            final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("PhotoProveedor").child(mAuthProviders.getId()+".jpg");

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            DatabaseReference imagestore = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Proveedor").child(id);
                            HashMap<String,Object> fotoMap = new HashMap<>();
                            fotoMap.put("foto" , String.valueOf(uri));
                            imagestore.updateChildren(fotoMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    getPhotoUsuario();
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

}