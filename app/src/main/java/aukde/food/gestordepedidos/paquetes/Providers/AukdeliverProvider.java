package aukde.food.gestordepedidos.paquetes.Providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import aukde.food.gestordepedidos.paquetes.Modelos.Aukdeliver;

public class AukdeliverProvider {

    DatabaseReference mDatabaseReference;

    public AukdeliverProvider(){
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Aukdeliver");
    }

    public Task<Void> Mapear(Aukdeliver aukdeliver){

        Map<String , Object> map = new HashMap<>();
        map.put("nombres",aukdeliver.getNombres());
        map.put("apellidos",aukdeliver.getApellidos());
        map.put("username",aukdeliver.getNombreUsuario());
        map.put("dni",aukdeliver.getDni());
        map.put("telefono",aukdeliver.getTelefono());
        map.put("marca de moto",aukdeliver.getMarcaDeMoto());
        map.put("placa",aukdeliver.getPlaca());
        map.put("categoria_licencia",aukdeliver.getCategoriaLic());
        map.put("numero licencia",aukdeliver.getNumLicencia());
        map.put("email",aukdeliver.getEmail());

        return mDatabaseReference.child(aukdeliver.getId()).setValue(map);

    }

}
