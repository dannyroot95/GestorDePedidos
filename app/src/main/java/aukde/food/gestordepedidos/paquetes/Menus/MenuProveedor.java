package aukde.food.gestordepedidos.paquetes.Menus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Actividades.Inicio;
import aukde.food.gestordepedidos.paquetes.Providers.AuthProviders;

public class MenuProveedor extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private AuthProviders mAuthProviders;
    private ProgressDialog mDialog;
    SharedPreferences mSharedPreference;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeRedCake);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_proveedor);
        mAuthProviders = new AuthProviders();
        mDialog = new ProgressDialog(this);
        mSharedPreference = getApplicationContext().getSharedPreferences("tipoUsuario",MODE_PRIVATE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Mapping();
    }


    public void show_popup(View view){

        Context wrapper = new ContextThemeWrapper(this, R.style.popupThemePink);
        PopupMenu popupMenu = new PopupMenu(wrapper,view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();

    }


    void logout(){
        final SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putString("","");
        editor.apply();
        mAuthProviders.Logout();
        startActivity(new Intent(MenuProveedor.this, Inicio.class));
        finish();
        mDialog.dismiss();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                //startActivityForResult(intent,GALLERY);
                return true;

            case R.id.item2:
                mDialog.show();
                mDialog.setMessage("Cerrando sesión...");
                logout();
                return true;
            default:
                return false;
        }
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

}