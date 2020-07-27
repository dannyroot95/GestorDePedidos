package aukde.food.gestordepedidos.paquetes.Menus;

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

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Actividades.Inicio;
import aukde.food.gestordepedidos.paquetes.Providers.AuthProviders;

public class MenuProveedor extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private AuthProviders mAuthProviders;
    private ProgressDialog mDialog;
    SharedPreferences mSharedPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeRedCake);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_proveedor);
        mAuthProviders = new AuthProviders();
        mDialog = new ProgressDialog(this);
        mSharedPreference = getApplicationContext().getSharedPreferences("tipoUsuario",MODE_PRIVATE);
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
}