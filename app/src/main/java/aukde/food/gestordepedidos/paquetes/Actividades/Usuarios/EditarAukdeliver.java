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
import android.widget.TextView;
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
import aukde.food.gestordepedidos.paquetes.Providers.AukdeliverProvider;
import aukde.food.gestordepedidos.paquetes.Providers.AuthProviders;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class EditarAukdeliver extends AppCompatActivity {


    private CircleImageView photoPerfil , CrMyPhoto;
    private Button btnUpdateXAukdeliver;

    private TextView IDUsuario,IDUsuarioAukdeliver, edtNumPedido;

    private EditText txtNombres,txtApellidos, txtCorreo,txtDni,txtTelefono,txtNombreUsuario,txtCategoriaLicencia;
    private EditText txtMarcaMoto,txtNumeroLicencia,txtPlaca;

    private AukdeliverProvider mAukdeliverProvider;
    private AuthProviders mAuthProviders;

    private File mImageFile;
    private ProgressDialog mDialog;
    private DatabaseReference mDatabase;
    private final int GALLERY_REQUEST = 1;
    private Vibrator vibrator;
    long tiempo = 100;

    String upNombre;
    String upApellido;
    String upNombreUsuario;
    String upCorreo;
    String upDni;
    String upTelefono;
    String upCategoriaLicencia;
    String upMarcadeMoto;
    String upNumerodeLicencia;
    String upPlaca;

    Bundle ListaDatosAukde;
    TextView txtEncargado,idDelivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_aukdeliver);

        mDialog = new ProgressDialog(this,R.style.ThemeOverlay);
        mAukdeliverProvider= new AukdeliverProvider();
        //mAdminProvider = new AdminProvider();
        mAuthProviders = new AuthProviders();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        photoPerfil = findViewById(R.id.fotodefaultAukdeliver);
        CrMyPhoto = findViewById(R.id.myPhotoAukdeliver);
        btnUpdateXAukdeliver=findViewById(R.id.btnEditarAukdeliver);
        txtNombres=findViewById(R.id.edtNombreAukde);
        txtApellidos=findViewById(R.id.edtApellidoAukde);
        txtNombreUsuario=findViewById(R.id.edtUsuarioAukdeliver);


        ListaDatosAukde  = getIntent().getExtras();


        txtEncargado=findViewById(R.id.txtEditarRepartidor);
        idDelivery=findViewById(R.id.txtIdAukdeliver);



        txtCorreo=findViewById(R.id.detalleAukdeCorreo);
        txtDni=findViewById(R.id.detalleAukdeDni);
        txtTelefono=findViewById(R.id.detalleAukdeTelefono);
        txtCategoriaLicencia=findViewById(R.id.edtAukdeliverCl);

        txtMarcaMoto=findViewById(R.id.edtAukdeliverMarcaMoto);
        txtNumeroLicencia=findViewById(R.id.edtAukdeliverNumeroLic);
        txtPlaca=findViewById(R.id.edtAukdeliverPlaca);


        btnUpdateXAukdeliver.setOnClickListener(new View.OnClickListener() {
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
            final String name = getIntent().getStringExtra("key");
            //final String id = mAuthProviders.getId();
            final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("PhotoAukdeliver").child(mAuthProviders.getId()+".jpg");

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            DatabaseReference imagestore = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Aukdeliver").child(name);
                            HashMap<String,Object> fotoMap = new HashMap<>();
                            fotoMap.put("foto" , String.valueOf(uri));
                            imagestore.updateChildren(fotoMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    getPhotoUsuario();
                                    mDialog.dismiss();
                                    Toasty.success(EditarAukdeliver.this, "Foto de perfil actualizada", Toast.LENGTH_LONG,true).show();
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
        //String idPedidoData = IDUsuario.getText().toString();

        mDatabase.child("Usuarios").child("Aukdeliver").child(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String nombre = dataSnapshot.child("nombres").getValue().toString();
                    String apellido = dataSnapshot.child("apellidos").getValue().toString();
                    String nombreUsuario = dataSnapshot.child("username").getValue().toString();
                    String correo = dataSnapshot.child("email").getValue().toString();
                    String dni = dataSnapshot.child("dni").getValue().toString();
                    String telefono = dataSnapshot.child("telefono").getValue().toString();
                    String CategoriaLicencia = dataSnapshot.child("categoria_licencia").getValue().toString();
                    String MarcadeMoto=dataSnapshot.child("marca_de_moto").getValue().toString();
                    String NumeroLicencia=dataSnapshot.child("numero_licencia").getValue().toString();
                    String Placa=dataSnapshot.child("placa").getValue().toString();


                    txtNombres.setText(nombre);
                    txtApellidos.setText(apellido);
                    txtNombreUsuario.setText(nombreUsuario);
                    txtCorreo.setText(correo);
                    txtDni.setText(dni);
                    txtTelefono.setText(telefono);
                    txtCategoriaLicencia.setText(CategoriaLicencia);
                    txtMarcaMoto.setText(MarcadeMoto);
                    txtNumeroLicencia.setText(NumeroLicencia);
                    txtPlaca.setText(Placa);

                }


                else
                {
                    Toast.makeText(EditarAukdeliver.this, "error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ActualzarPerfil() {
        upNombre = txtNombres.getText().toString();
        upApellido = txtApellidos.getText().toString();
        upNombreUsuario=txtNombreUsuario.getText().toString();
        upCorreo=txtCorreo.getText().toString();
        upDni=txtDni.getText().toString();
        upTelefono=txtTelefono.getText().toString();
        upCategoriaLicencia=txtCategoriaLicencia.getText().toString();
        upMarcadeMoto=txtMarcaMoto.getText().toString();
        upNumerodeLicencia=txtNumeroLicencia.getText().toString();
        upPlaca=txtPlaca.getText().toString();

        if(!upNombre.equals("") && !upApellido.equals("") &&  !upNombreUsuario.equals("")  && !upCorreo.equals("") && !upDni.equals("") && !upTelefono.equals("") && !upCategoriaLicencia.equals("") && !upMarcadeMoto.equals("")&& !upNumerodeLicencia.equals("") && !upPlaca.equals("")){
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
        String name = getIntent().getStringExtra("key");
        //String id = mAuthProviders.getId();
        mDatabase.child("Usuarios").child("Aukdeliver").child(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("foto")){
                    //Obtener el url y setearlo en la imagem
                    String setFoto = dataSnapshot.child("foto").getValue().toString();
                    Glide.with(EditarAukdeliver.this).load(setFoto).into(CrMyPhoto);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void guardarNombres(){
        final  String id = getIntent().getStringExtra("key");
        //final String id = mAuthProviders.getId();
        final String nombre = txtNombres.getText().toString();
        final String apellido = txtApellidos.getText().toString();
        final String nombreusuario= txtNombreUsuario.getText().toString();
        final String correo= txtCorreo.getText().toString();
        final String dni= txtDni.getText().toString();
        final String telefono= txtTelefono.getText().toString();
        final String categorialicencia = txtCategoriaLicencia.getText().toString();
        final String marcamoto=txtMarcaMoto.getText().toString();
        final String numerolicencia=txtNumeroLicencia.getText().toString();
        final String placa=txtPlaca.getText().toString();

        mDatabase.child("Usuarios").child("Aukdeliver").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,Object> datos = new HashMap<>();
                datos.put("nombres",nombre);
                datos.put("apellidos",apellido);
                datos.put("username",nombreusuario);
                datos.put("email",correo);
                datos.put("dni",dni);
                datos.put("telefono",telefono);
                datos.put("categoria_licencia",categorialicencia);
                datos.put("marca_de_moto", marcamoto);
                datos.put("numero_licencia",numerolicencia);
                datos.put("placa",placa);

                mDatabase.child("Usuarios").child("Aukdeliver").child(id).updateChildren(datos).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toasty.success(EditarAukdeliver.this, "Nombres Actualizados", Toast.LENGTH_SHORT,true).show();
                        ObtenerDataUser();
                        mDialog.dismiss();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(EditarAukdeliver.this, "Hubo un error al actualizar datos", Toast.LENGTH_SHORT,true).show();
            }
        });
    }

}