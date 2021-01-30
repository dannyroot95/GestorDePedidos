package aukde.food.aukdeliver.paquetes.Inclusiones;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import aukde.food.aukdeliver.R;

public class MiToolbar {

    public static void Mostrar(AppCompatActivity activity , String titulo , boolean upBottom){

        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(titulo);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(upBottom);

    }


}

