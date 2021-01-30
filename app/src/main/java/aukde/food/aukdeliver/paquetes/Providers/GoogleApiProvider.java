package aukde.food.aukdeliver.paquetes.Providers;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

import aukde.food.aukdeliver.R;
import aukde.food.aukdeliver.paquetes.Retrofit.RetrofitClient;
import aukde.food.aukdeliver.paquetes.Utils.IGoogleApi;
import retrofit2.Call;

public class GoogleApiProvider {

    private Context context;

    public GoogleApiProvider(Context context){
        this.context = context;
    }

    public Call<String> getDirections(LatLng origenLatLng , LatLng destinoLatLng){

        String baseUrl = "https://maps.googleapis.com";
        String query = "/maps/api/directions/json?mode=driving&transit_routing_preferences=less_driving&"
                + "origin=" + origenLatLng.latitude + "," + origenLatLng.longitude + "&"
                + "destination=" + destinoLatLng.latitude + "," + destinoLatLng.longitude + "&"
                + "departure_time=" + (new Date().getTime() + (60*60*1000)) + "&"
                + "traffic_model=best_guess&"
                + "key=" + context.getResources().getString(R.string.google_maps_key);

        return RetrofitClient.getClient(baseUrl).create(IGoogleApi.class).getDirections(baseUrl+query);

    }

}
