package aukde.food.gestordepedidos.paquetes.Providers;

import aukde.food.gestordepedidos.paquetes.Modelos.FCMBody;
import aukde.food.gestordepedidos.paquetes.Modelos.FCMResponse;
import aukde.food.gestordepedidos.paquetes.Retrofit.IFCMapi;
import aukde.food.gestordepedidos.paquetes.Retrofit.RetrofitClient;
import retrofit2.Call;

public class NotificationProvider {

    private  String url = "https://fcm.googleapis.com";

    public NotificationProvider(){
    }

    public Call<FCMResponse> sendNotificacion(FCMBody body){
        return RetrofitClient.getClientObject(url).create(IFCMapi.class).send(body);
    }

}
