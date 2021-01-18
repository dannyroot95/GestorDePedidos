package aukde.food.gestordepedidos.paquetes.Menus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Actividades.Inicio;
import aukde.food.gestordepedidos.paquetes.Actividades.Pedidos.DetalleSolicitudProveedor;
import aukde.food.gestordepedidos.paquetes.Menus.Perfiles.PerfilAdmin;
import aukde.food.gestordepedidos.paquetes.Menus.Perfiles.Perfilproveedoraukde;
import aukde.food.gestordepedidos.paquetes.Productos.Default.AgregarProductoPorDefecto;
import aukde.food.gestordepedidos.paquetes.Productos.Default.ListaProductosDefault;
import aukde.food.gestordepedidos.paquetes.Productos.MenuAddProduct;
import aukde.food.gestordepedidos.paquetes.Productos.Solicitud.FichaDeSolicitud;
import aukde.food.gestordepedidos.paquetes.Productos.Solicitud.MenuListaDeSolicitudes;
import aukde.food.gestordepedidos.paquetes.Providers.AuthProviders;
import aukde.food.gestordepedidos.paquetes.Providers.ProveedorProvider;
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
    private Button addProducto , listProducto , solicitarDelivery , btnListaSolicitud,btnPerfilX,BtnAsignarHora;

    DeleteCache deleteCache;

    SharedPreferences dataNombre;
    SharedPreferences dataApellido;
    SharedPreferences datanombreEmpresa;
    SharedPreferences dataCategoria;

    SaveStorageImage saveStorageImage;

    Dialog mDialog3;
    private int hora,minutos,segundos;

    Button hora1;
    Button hora2;
    Button BotonHorario;
    EditText EditHora1;
    EditText EditHora2;
    private DatabaseReference HoraAtencionActual;
    private DatabaseReference EstadoAtencionActual;
    private DatabaseReference mDatabaseHora ;
    private ProveedorProvider mProveedorProvider;
    ImageView tuImageView,tuImageView2;
    TextView contenAsignarHora,contentReporte,contentListaDeSolicitudes,contentSolicitarDelivery,contentListaProductos,contentAgregarProducto,contentPerfil;
    TextView disponible,nodisponible;
    ImageView Fotoestado,Fotoestado2;

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

        mDialog3=new Dialog(this);
        mProveedorProvider = new ProveedorProvider();
        tuImageView=(ImageView)findViewById(R.id.fotodefaultatencion);
        tuImageView2=(ImageView)findViewById(R.id.fotodefaultatencion2);
        BtnAsignarHora=(Button)findViewById(R.id.btnAsignarHora);
        contentPerfil=(TextView)findViewById(R.id.ContentPerfil);
        contentListaProductos=(TextView)findViewById(R.id.ContentListaProductos);
        contentAgregarProducto=(TextView)findViewById(R.id.ContentAgregarProducto);
        contentSolicitarDelivery=(TextView)findViewById(R.id.ContentSolicitarDelivery);
        contentListaDeSolicitudes=(TextView)findViewById(R.id.ContentListaDeSolicitudes);
        contentReporte=(TextView)findViewById(R.id.ContentReporte);
        contenAsignarHora=(TextView)findViewById(R.id.ContenAsignarHora);

        Fotoestado=(ImageView)findViewById(R.id.fotoestado);
        Fotoestado2=(ImageView)findViewById(R.id.fotoestado2);

        disponible=(TextView)findViewById(R.id.iddisponible);
        nodisponible=(TextView)findViewById(R.id.idnodisponible);


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
        estadoHoraAtencion();
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

    public void ShowPopupListaHoraAtencion(View vista){
        vibrator.vibrate(tiempo);
        mDialog3.setContentView(R.layout.activity_horario_atencion_aukde);
        TextView txtCerrarHoraAtencion;
        hora1=mDialog3.findViewById(R.id.horaApertura);
        hora2=mDialog3.findViewById(R.id.HoradeCierre);
        BotonHorario=mDialog3.findViewById(R.id.botonHorario);
        EditHora1=mDialog3.findViewById(R.id.horaAtencion);
        EditHora2=mDialog3.findViewById(R.id.horaCierre);
        txtCerrarHoraAtencion = mDialog3.findViewById(R.id.txtClose);
        HoraAtencionActual = FirebaseDatabase.getInstance().getReference();
        EstadoAtencionActual=FirebaseDatabase.getInstance().getReference();

        txtCerrarHoraAtencion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                mDialog3.dismiss();
            }
        });
        hora1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v==hora1){
                    final Calendar c=Calendar.getInstance();
                    hora=c.get(Calendar.HOUR_OF_DAY);
                    minutos=c.get(Calendar.MINUTE);
                    TimePickerDialog timepickerdialog=new TimePickerDialog(MenuProveedor.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int i, int i1) {
                            EditHora1.setText(i+":"+i1);

                        }
                    },hora,minutos,false);
                    timepickerdialog.show();
                }

            }
        });

        hora2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vi) {

                if(vi==hora2){
                    final Calendar c=Calendar.getInstance();
                    hora=c.get(Calendar.HOUR_OF_DAY);
                    minutos=c.get(Calendar.MINUTE);
                    TimePickerDialog timepickerdialog=new TimePickerDialog(MenuProveedor.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int h, int h1) {
                            EditHora2.setText(h+":"+h1);

                        }
                    },hora,minutos,false);
                    timepickerdialog.show();
                }

            }
        });

        BotonHorario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickRegistroHora();
            }
        });


        mDialog3.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog3.show();

    }

    private void clickRegistroHora(){
        String dataNombress = Txtnombres.getText().toString();
        Query query = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Proveedor").orderByChild("nombres").equalTo(dataNombress);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot2 : dataSnapshot.getChildren()) {
                        String key = childSnapshot2.getKey();
                        Map<String, Object> map = new HashMap<>();
                        map.put("HoraApertura", EditHora1.getText().toString());
                        map.put("HoraCierre", EditHora2.getText().toString());
                        HoraAtencionActual.child("Usuarios").child("Proveedor").child(key).updateChildren(map);

                    }
                } else {
                    Toast.makeText(MenuProveedor.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void estadoHoraAtencion(){
        String dataNombress = Txtnombres.getText().toString();
        Query queryAtencion = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Proveedor").child(mAuth.getId());
        queryAtencion.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("HoraApertura") && dataSnapshot.hasChild("HoraCierre")) {
                    String hora1 = dataSnapshot.child("HoraApertura").getValue().toString();
                    DateFormat horarango1 = new SimpleDateFormat("HH:mm");
                    String hora2 = dataSnapshot.child("HoraCierre").getValue().toString();
                    DateFormat horarango2 = new SimpleDateFormat("HH:mm");

                    DateFormat df = new SimpleDateFormat("HH:mm");
                    String time = df.format(Calendar.getInstance().getTime());
                    //Map<String, Object> map = new HashMap<>();

                    try {
                        Date horafinal1 = horarango1.parse(hora1);
                        Date horafinal2 = horarango2.parse(hora2);
                        Date TimeActual = df.parse(time);


                        if(TimeActual.after(horafinal1) && TimeActual.before(horafinal2)){

                        tuImageView.setVisibility(View.VISIBLE);
                        Fotoestado.setVisibility(View.VISIBLE);
                        disponible.setText("Disponible");
                        disponible.setVisibility(View.VISIBLE);
                        }
                        /*else if(TimeActual.after(horafinal2) && TimeActual.before(horafinal1)){
                        tuImageView2.setVisibility(View.VISIBLE);
                        }*/
                        else{
                            //enviarEstadoHora();
                            tuImageView2.setVisibility(View.VISIBLE);
                            Fotoestado2.setVisibility(View.VISIBLE);
                            btnPerfilX.setEnabled(false);
                            contentPerfil.setTextColor(Color.parseColor("#9E9E9E"));
                            listProducto.setEnabled(false);
                            contentListaProductos.setTextColor(Color.parseColor("#9E9E9E"));
                            addProducto.setEnabled(false);
                            contentAgregarProducto.setTextColor(Color.parseColor("#9E9E9E"));
                            solicitarDelivery.setEnabled(false);
                            contentSolicitarDelivery.setTextColor(Color.parseColor("#9E9E9E"));
                            btnListaSolicitud.setEnabled(false);
                            contentListaDeSolicitudes.setTextColor(Color.parseColor("#9E9E9E"));
                            contentReporte.setTextColor(Color.parseColor("#9E9E9E"));

                            nodisponible.setText("No Disponible");
                            nodisponible.setVisibility(View.VISIBLE);

                            /*BtnAsignarHora.setEnabled(false);
                            contenAsignarHora.setTextColor(Color.parseColor("#9E9E9E"));*/

                        /*Toast.makeText(MenuProveedor.this, "error", Toast.LENGTH_SHORT).show();*/
                        }

                    } catch (ParseException e) {
                        Toast.makeText(MenuProveedor.this, "error try cacth", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(MenuProveedor.this, "Tiempos error", Toast.LENGTH_SHORT).show();
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void enviarEstadoHora(){
        String dataNombress = Txtnombres.getText().toString();
        Query query = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Proveedor").orderByChild("nombres").equalTo(dataNombress);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot2 : dataSnapshot.getChildren()) {
                        String key = childSnapshot2.getKey();
                        Map<String, Object> map = new HashMap<>();
                        map.put("Estado", "Cerrado");
                        HoraAtencionActual.child("Usuarios").child("Proveedor").child(key).updateChildren(map);
                    }
                } else {
                    Toast.makeText(MenuProveedor.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void enviarEstadoHora2(){
        String dataNombress = Txtnombres.getText().toString();
        Query query = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Proveedor").orderByChild("nombres").equalTo(dataNombress);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot2 : dataSnapshot.getChildren()) {
                        String key = childSnapshot2.getKey();
                        Map<String, Object> map = new HashMap<>();
                        map.put("Estado", "Abierto");
                        HoraAtencionActual.child("Usuarios").child("Proveedor").child(key).updateChildren(map);
                    }
                } else {
                    Toast.makeText(MenuProveedor.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}