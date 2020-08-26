package aukde.food.gestordepedidos.paquetes.Providers;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GeofireProviderProveedor {
    private DatabaseReference mDatabase;
    private GeoFire mGeofire;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String Auth = mAuth.getUid();

    public GeofireProviderProveedor(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("MapaProveedor");
        mGeofire = new GeoFire(mDatabase);
    }

    public GeoQuery getProveedores(LatLng latLng, double radius){
        GeoQuery geoQuery = mGeofire.queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), radius);
        geoQuery.removeAllListeners();
        return geoQuery;
    }

}
