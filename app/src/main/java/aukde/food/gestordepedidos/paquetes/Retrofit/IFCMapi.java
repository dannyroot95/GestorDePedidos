
package aukde.food.gestordepedidos.paquetes.Retrofit;
import aukde.food.gestordepedidos.paquetes.Modelos.FCMBody;
import aukde.food.gestordepedidos.paquetes.Modelos.FCMResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMapi {

    @Headers({
            "Content-type:application/json",
            "Authorization:key=AAAAgBArVjQ:APA91bHHyd7LD4N8pNHsVlQou6LM89xKap2KntkgEAwhF7zUrk4e_vT5xFq4nc6HNez9OqNYPi2frOZUtO4-XqpPleSRJSs_ukJiVQIOBwxytMSy7P2bHmP04pGvxz7n1Ycwd_OS9Z1f"
    })
    @POST("fcm/send")
    Call<FCMResponse> send(@Body FCMBody body);

}