package aukde.food.gestordepedidos.paquetes.Providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import aukde.food.gestordepedidos.paquetes.Modelos.Aukdeliver;
import aukde.food.gestordepedidos.paquetes.Modelos.Proveedor;

public class ProveedorProvider {

    DatabaseReference mDatabaseReference;

    public ProveedorProvider(){
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Proveedor");
    }

    public Task<Void> Mapear(Proveedor proveedor){

        Map<String , Object> map = new HashMap<>();
        map.put("nombres",proveedor.getNombres());
        map.put("apellidos",proveedor.getApellidos());
        map.put("username",proveedor.getNombreUsuario());
        map.put("dni",proveedor.getDni());
        map.put("telefono",proveedor.getTelefono());
        map.put("direccion",proveedor.getDireccion());
        map.put("categoria",proveedor.getCategoria());
        map.put("nombre empresa",proveedor.getNombreEmpresa());
        map.put("ruc",proveedor.getRuc());
        map.put("email",proveedor.getEmail());

        return mDatabaseReference.child(proveedor.getId()).setValue(map);

    }

}
