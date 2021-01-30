package aukde.food.aukdeliver.paquetes.Providers;

import aukde.food.aukdeliver.paquetes.Modelos.FCMBody;
import aukde.food.aukdeliver.paquetes.Modelos.FCMResponse;
import aukde.food.aukdeliver.paquetes.Retrofit.IFCMapi;
import aukde.food.aukdeliver.paquetes.Retrofit.RetrofitClient;
import retrofit2.Call;

public class NotificationProvider {

    private  String url = "https://fcm.googleapis.com";

    public NotificationProvider(){
    }

    public Call<FCMResponse> sendNotificacion(FCMBody body){
        return RetrofitClient.getClientObject(url).create(IFCMapi.class).send(body);
    }

}