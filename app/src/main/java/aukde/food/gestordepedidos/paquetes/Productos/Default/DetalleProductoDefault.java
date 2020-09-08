package aukde.food.gestordepedidos.paquetes.Productos.Default;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Actividades.Pedidos.DetallePedidoAukdeliver;
import aukde.food.gestordepedidos.paquetes.Modelos.ProductoDefault;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class DetalleProductoDefault extends AppCompatActivity {

    TextView  txtNombreProducto , txtStock , txtDecripcion ,
            txtContenido , txtEmbalaje , txtCodigo ;
    CircleImageView clrPhotoDetalleProducto;
    Button mButtonMax , mButtomMin , mButtonEditarProductoDefault , mButtonActualizarStockDefault;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeRedCake);
        setContentView(R.layout.activity_detalle_producto_default);
        mDialog = new ProgressDialog(this,R.style.ThemeOverlay);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        clrPhotoDetalleProducto = findViewById(R.id.photoDetalleProductoDefault);
        txtNombreProducto = findViewById(R.id.detalleNombreProductoDefault);
        txtStock = findViewById(R.id.detalleStockDefault);
        txtDecripcion = findViewById(R.id.detalleDefaultDescripcion);
        txtContenido = findViewById(R.id.detalleContenidoDefault);
        txtEmbalaje = findViewById(R.id.detalleEmbalajeDefault);
        txtCodigo = findViewById(R.id.detalleCodigoDefault);

        mButtomMin = findViewById(R.id.btnMinDefault);
        mButtonMax = findViewById(R.id.btnMaxDefault);
        mButtonEditarProductoDefault = findViewById(R.id.btnEditarProductoDefault);
        mButtonActualizarStockDefault = findViewById(R.id.btnActualizarStockDefault);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        ProductoDefault productoDefault = (ProductoDefault) bundle.getSerializable("key");
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(productoDefault.getUrlPhoto());
        arrayList.add(productoDefault.getNombreProducto());
        arrayList.add(productoDefault.getStock());
        arrayList.add(productoDefault.getDescripcionProducto());
        arrayList.add(productoDefault.getContenidoProducto());
        arrayList.add(productoDefault.getEmbalaje());
        arrayList.add(productoDefault.getCodigoINEA());

        Picasso
                .get()
                .load(arrayList.get(0))
                .fit()
                .error(R.drawable.ic_error)
                .placeholder(R.drawable.loaderrosa)
                .centerCrop()
                .into(clrPhotoDetalleProducto);

        txtNombreProducto.setText(arrayList.get(1));
        txtStock.setText(arrayList.get(2));
        txtDecripcion.setText(arrayList.get(3));
        txtContenido.setText(arrayList.get(4));
        txtEmbalaje.setText(arrayList.get(5));
        txtCodigo.setText(arrayList.get(6));

        mButtomMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = txtStock.getText().toString();
                if (num.equals("1")){
                    txtStock.setText("1");
                }
                else {
                    int Cc = Integer.parseInt(num);
                    int res = Cc-1;
                    String Min = String.valueOf(res);
                    txtStock.setText(Min);
                }

            }
        });

        mButtonMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = txtStock.getText().toString();
                int Cc = Integer.parseInt(num);
                int res = Cc+1;
                String Min = String.valueOf(res);
                txtStock.setText(Min);
            }
        });

        mButtonEditarProductoDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.success(DetalleProductoDefault.this, "Activity", Toast.LENGTH_SHORT,true).show();
            }
        });

        mButtonActualizarStockDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarStockDefault();            }
        });

    }

    private void actualizarStockDefault() {
        final String dataStockDefault = txtStock.getText().toString();
        final String dataCodigoINEA = txtCodigo.getText().toString();
        Query reference = FirebaseDatabase.getInstance().getReference().child("Usuarios").child("Proveedor").child(mAuth.getUid()).child("Productos").orderByChild("codigoINEA").equalTo(dataCodigoINEA);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    Map<String, Object> map = new HashMap<>();
                    map.put("stock", dataStockDefault);
                    mDatabase.child("Usuarios").child("Proveedor").child(mAuth.getUid()).child("Productos").child(key).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toasty.success(DetalleProductoDefault.this, "Stock Actualizado!", Toast.LENGTH_LONG, true).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.error(DetalleProductoDefault.this, "Error al actualizar Stock", Toast.LENGTH_LONG, true).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mDialog.dismiss();
    }
}
