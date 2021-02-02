package aukde.food.aukdeliver.paquetes.Servicios;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;
import aukde.food.aukdeliver.R;
import aukde.food.aukdeliver.paquetes.Receptor.Constantes;

public class JobServiceMonitoreo extends JobService {

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult != null && locationResult.getLastLocation() != null) {
                double latitude = locationResult.getLastLocation().getLatitude();
                double longitude = locationResult.getLastLocation().getLongitude();
                final String idUser = mAuth.getUid();
                Map<String , Object> map = new HashMap<>();
                map.put("0",latitude);
                map.put("1",longitude);

                // correccion de error de localizacion cuando se logea un usuario que tenga dos tipos de usuarios (Admin , Aukdeliver)
                if(mAuth.getCurrentUser() != null) {
                    if(!mAuth.getUid().equals("UnwAmhwRzmRLn8aozWjnYFOxYat2") && !mAuth.getUid().equals("nS8J0zEj53OcXSugQsXIdMKUi5r1")
                    && !mAuth.getUid().equals("9sjTQMmowxWYJGTDUY98rAR2jzB3")){
                    mDatabase.child("Monitoreo").child(idUser).child("l").updateChildren(map);
                    keyGeofire();
                    nameGeofire();
                    }
                }
                //Toast.makeText(ServiceMonitoreo.this, "lat : "+lat+" lon : "+lon, Toast.LENGTH_SHORT).show();
                Log.d("LOCATION_UPDATE",latitude+" , "+longitude);
            }
        }
    };

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d("TAG", "onStartJob");
        doBackWork(jobParameters);
        return true;
    }

    private void doBackWork(JobParameters jobParameters){
        Log.d("TAG", "doBackWork");
        new Thread(new Runnable() {
            @Override
            public void run() {
                startLocationService();
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        //estaba en true
        return true;
    }

   private void startLocationService() {
        Intent intent = new Intent("value");
        intent.putExtra("executing","true");
        String channel_id = "location_notification_channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id);
       Intent resultIntent = new Intent();
       PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0
               , resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Notificación de Pedidos");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentText("Activo!");
        builder.setContentIntent(pendingIntent);
        builder.setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY);
        builder.setGroup("mi grupo");
        builder.setGroupSummary(false);
        builder.setColor(Color.GREEN);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null && notificationManager.getNotificationChannel(channel_id) == null) {

                NotificationChannel notificationChannel = new NotificationChannel(channel_id,
                        "Servicio de Notificaciones", NotificationManager.IMPORTANCE_HIGH);

                notificationChannel.setDescription("Cannal de notificaciones");
                notificationManager.createNotificationChannel(notificationChannel);
                notificationChannel.setLightColor(Color.GREEN);
            }
        }

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        startForeground(Constantes.LOCATION_SERVICE_ID,builder.build());
        LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);
    }

    private void keyGeofire(){
        Map<String , Object> map = new HashMap<>();
        map.put("g","6myb3krm07");
        mDatabase.child("Monitoreo").child(mAuth.getUid()).updateChildren(map);
    }

    private void nameGeofire(){
        DatabaseReference mDatabaseX = FirebaseDatabase.getInstance().getReference();
        mDatabaseX.child("Usuarios").child("Aukdeliver").child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String nombre = dataSnapshot.child("nombres").getValue().toString();
                    Map<String, Object> map = new HashMap<>();
                    map.put("nombre", nombre);
                    mDatabase.child("Monitoreo").child(mAuth.getUid()).updateChildren(map);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}