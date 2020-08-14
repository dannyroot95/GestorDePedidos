package aukde.food.gestordepedidos.paquetes.Providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import aukde.food.gestordepedidos.paquetes.Modelos.Administrador;

public class AdminProvider {

    DatabaseReference mDatabaseReference;

    public AdminProvider(){
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Administrador");
    }

    public Task<Void> Mapear(Administrador administrador) {

        Map<String , Object> map = new HashMap<>();
        map.put("nombres",administrador.getNombres());
        map.put("apellidos",administrador.getApellidos());
        map.put("dni",administrador.getDni());
        map.put("telefono",administrador.getTelefono());
        map.put("email",administrador.getEmail());

        return mDatabaseReference.child(administrador.getId()).setValue(map);
    }

    public Task<Void>update(Administrador administrador) {

        Map<String , Object> map = new HashMap<>();
        map.put("nombres",administrador.getNombres());
        map.put("apellidos",administrador.getApellidos());
        map.put("foto",administrador.getFoto());
        return mDatabaseReference.child(administrador.getId()).updateChildren(map);
    }

    public DatabaseReference getAdminData(String idAdmin){
        return mDatabaseReference.child(idAdmin);
    }

}
