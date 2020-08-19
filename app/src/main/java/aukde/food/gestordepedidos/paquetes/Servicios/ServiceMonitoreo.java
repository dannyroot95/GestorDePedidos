package aukde.food.gestordepedidos.paquetes.Servicios;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

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

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Receptor.Constantes;


public class ServiceMonitoreo extends Service {
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private LocationCallback locationCallback = new LocationCallback() {
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
                mDatabase.child("Monitoreo").child(idUser).child("l").updateChildren(map);
                keyGeofire();
                nameGeofire();
                //Toast.makeText(ServiceMonitoreo.this, "lat : "+lat+" lon : "+lon, Toast.LENGTH_SHORT).show();
                Log.d("LOCATION_UPDATE",latitude+" , "+longitude);
            }
        }
    };

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
                Map<String , Object> map = new HashMap<>();
                map.put("nombre",nombre);
                mDatabase.child("Monitoreo").child(mAuth.getUid()).updateChildren(map);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void startLocationService() {
        String channel_id = "location_notification_channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0
                , resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Notificación de Pedidos");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentText("Activo!");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(false);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null && notificationManager.getNotificationChannel(channel_id) == null) {

                NotificationChannel notificationChannel = new NotificationChannel(channel_id,
                        "Location Service", NotificationManager.IMPORTANCE_HIGH);

                notificationChannel.setDescription("Cannal de notificaciones");
                notificationManager.createNotificationChannel(notificationChannel);
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
    }

    private void stopLocationService(){
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
        stopForeground(true);
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null){
            String action = intent.getAction();
            if(action != null) {
                if(action.equals(Constantes.ACTION_START_LOCATION)){
                    startLocationService();
                }
                else if (action.equals(Constantes.ACTION_STOP_LOCATION)){
                    startLocationService();
                }
            }
        }
     super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }
}

