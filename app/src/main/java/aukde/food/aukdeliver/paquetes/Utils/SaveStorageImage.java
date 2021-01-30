package aukde.food.aukdeliver.paquetes.Utils;

import android.graphics.Bitmap;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class SaveStorageImage extends AppCompatActivity {

    public void saveImage(Bitmap image, File storageDir, String imageFileName) {

        boolean successDirCreated = false;
        if (!storageDir.exists()) {
            successDirCreated = storageDir.mkdir();
        }
        if (successDirCreated) {
            File imageFile = new File(storageDir, imageFileName);
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
                // Toast.makeText(MenuAdmin.this, "Imagen guardada!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                //Toast.makeText(MenuAdmin.this, "ERROR!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }
        else{
            // Toast.makeText(this, "No se pudo guardar la foto", Toast.LENGTH_SHORT).show();
        }
    }

}
