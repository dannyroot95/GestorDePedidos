package aukde.food.gestordepedidos.paquetes.Mapas;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import aukde.food.gestordepedidos.R;
import es.dmoral.toasty.Toasty;

public class MapaClientePorLlamada extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Bundle poscicion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_cliente_por_llamada);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        poscicion = getIntent().getExtras();
        if (poscicion.isEmpty()){
            Toasty.error(this,"Error",Toasty.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        Double latitud = -12.0;
        Double longitud = -69.0;
        String stlatitud = poscicion.getString("latitud");
        String stlongitud = poscicion.getString("longitud");
        String stNombreC = poscicion.getString("nombre");
        int height = 100;
        int width = 100;
        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.usuario);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap usuario = Bitmap.createScaledBitmap(b, width, height, false);
        if (stlatitud.isEmpty() && stlongitud.isEmpty()){
            Toasty.error(this,"Error",Toasty.LENGTH_SHORT).show();
            finish();
        }
        else{
            latitud = Double.parseDouble(stlatitud);
            longitud = Double.parseDouble(stlongitud);
        }

        LatLng point = new LatLng(latitud, longitud);
        mMap.addMarker(new MarkerOptions()
                .position(point)
                .icon(BitmapDescriptorFactory.fromBitmap(usuario))
                .title(stNombreC)).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point,17));
    }
}