package aukde.food.gestordepedidos.paquetes.Actividades.Registros;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Actividades.Inicio;
import aukde.food.gestordepedidos.paquetes.Actividades.Logins.LoginAdmin;
import aukde.food.gestordepedidos.paquetes.Inclusiones.MiToolbar;
import aukde.food.gestordepedidos.paquetes.Menus.MenuAdmin;
import aukde.food.gestordepedidos.paquetes.Modelos.Aukdeliver;
import aukde.food.gestordepedidos.paquetes.Modelos.Proveedor;
import aukde.food.gestordepedidos.paquetes.Providers.AuthProviders;
import aukde.food.gestordepedidos.paquetes.Providers.ProveedorProvider;

public class RegistroProveedor extends AppCompatActivity {

    private TextInputEditText edtNombres, edtApellidos,edtUsername,edtDni, edtTelefono,
            edtDireccion,edtCategoria,edtNombreEmpresa,edtRUC,edtEmail, edtPassword,
            edtRepetirPass, edtClaveAuth;

    private ProgressDialog mDialog;
    Button mButtonRegistro;
    AuthProviders mAuthProviders;
    ProveedorProvider mProveedorProvider;
    Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_proveedor);
        MiToolbar.Mostrar(this,"Registro Proveedor",true);

        mAuthProviders = new AuthProviders();
        mProveedorProvider = new ProveedorProvider();
        mButtonRegistro = findViewById(R.id.btnRegistrarse);
        mDialog = new ProgressDialog(this);

        edtNombres = findViewById(R.id.ProveedorNombres);
        edtApellidos = findViewById(R.id.ProveedorApellidos);
        edtUsername = findViewById(R.id.ProveedorUsername);
        edtDni = findViewById(R.id.ProveedorDNI);
        edtTelefono = findViewById(R.id.ProveedorTeléfono);
        edtDireccion = findViewById(R.id.ProveedorDireccion);
        edtCategoria = findViewById(R.id.ProveedorCategoria);
        edtCategoria.setEnabled(false);
        edtNombreEmpresa = findViewById(R.id.ProveedorNombreEmpre);
        edtRUC= findViewById(R.id.ProveedorRUC);
        edtEmail = findViewById(R.id.ProveedorEmail);
        edtPassword = findViewById(R.id.ProveedorEdtPassword);
        edtRepetirPass = findViewById(R.id.ProveedorRepetirContrasena);
        edtClaveAuth = findViewById(R.id.ProveedorClaveAutorización);

        mSpinner = findViewById(R.id.spinnerFood);
        ArrayAdapter<CharSequence> adapterSpinnerFood = ArrayAdapter.createFromResource(this,R.
                array.categoriaFood,R.layout.custom_spinner);
        adapterSpinnerFood.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        mSpinner.setAdapter(adapterSpinnerFood);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                edtCategoria.setText(parent.getItemAtPosition(position).toString());
                //String stSpinnerEstado = edtCategoriaLic.getText().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mButtonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickRegistro();
            }
        });

    }

    private void ClickRegistro() {

        final String nombres = edtNombres.getText().toString();
        final String apellidos = edtApellidos.getText().toString();
        final String username = edtUsername.getText().toString();
        final String dni = edtDni.getText().toString();
        final String telefono = edtTelefono.getText().toString();
        final String direccion = edtDireccion.getText().toString();
        final String categoria = edtCategoria.getText().toString();
        final String nombre_empresa = edtNombreEmpresa.getText().toString();
        final String ruc = edtRUC.getText().toString();
        final String email = edtEmail.getText().toString();
        final String password = edtPassword.getText().toString();
        final String repetirPass = edtRepetirPass.getText().toString();
        final String ClaveAuth = edtClaveAuth.getText().toString();

        if(!nombres.isEmpty() && !apellidos.isEmpty() &&!username.isEmpty() && !dni.isEmpty() && !telefono.isEmpty()
                && !direccion.isEmpty() && !categoria.isEmpty() && !nombre_empresa.isEmpty() &&
                !ruc.isEmpty() && !email.isEmpty() && !password.isEmpty()
                && !repetirPass.isEmpty() && !ClaveAuth.isEmpty()){

            mDialog.show();
            mDialog.setMessage("Registrando usuario...");
            if(password.length()>=6){
                if(password.equals(repetirPass)){
                    if(ClaveAuth.equals("AUK2020+*") || ClaveAuth.equals("WRZ20@") || ClaveAuth.equals("GOGOOL*")){
                        registrar(nombres,apellidos,username,dni,telefono,direccion,categoria,nombre_empresa,ruc,email,password);
                    }
                    else {
                        mDialog.dismiss();
                        Toast.makeText(RegistroProveedor.this,"Clave de autorización inválida",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    mDialog.dismiss();
                    Toast.makeText(RegistroProveedor.this,"Las contraseñas no coinciden",Toast.LENGTH_LONG).show();
                }
            }
            else {
                mDialog.dismiss();
                Toast.makeText(RegistroProveedor.this,"La contraseña debe ser mayor a 6 caracteres",Toast.LENGTH_LONG).show();
            }
        }

        else {
            Toast.makeText(RegistroProveedor.this,"Complete los todos campos",Toast.LENGTH_LONG).show();
        }

    }

    private void registrar(final String nombres, final String apellidos,final String username,final String dni,final String telefono,final String direccion,final String categoria,final String nombre_empresa,final String ruc,final String email, String password) {

        mAuthProviders.Registro(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                Proveedor proveedor = new Proveedor(id,nombres,apellidos,username,dni,telefono,direccion,categoria,nombre_empresa,ruc,email);
                mapear(proveedor);
            }
        });
    }

    void mapear(Proveedor proveedor){
        mProveedorProvider.Mapear(proveedor).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    mDialog.dismiss();
                    logout();
                }

                else {
                    mDialog.dismiss();
                    Toast.makeText(RegistroProveedor.this, "No se pudo crear un nuevo usuario", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    void logout() {
        Intent intent = new Intent(getApplicationContext(), LoginAdmin.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("dato","valor");
        startActivity(intent);
        finish();
        mAuthProviders.Logout();
    }

}