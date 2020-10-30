package aukde.food.gestordepedidos.paquetes.Reportes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Adaptadores.AdapterAllData;
import aukde.food.gestordepedidos.paquetes.Modelos.AllData;
import es.dmoral.toasty.Toasty;

public class ReportePedidoPorLlamada extends AppCompatActivity{

    FirebaseAuth mAuth;
    TextView txtGanancia , txtFecha1 , txtFecha2 , txtCountTotal , txtGanancia2;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private ProgressDialog mDialogActualizeData;
    int dia, mes, year;
    CheckBox checkBoxFiltro;
    LinearLayout mLinearLayout,mLinearLayoutFilter;
    Button btnGananciaDate;
    TextView txtFechaIni , txtFechaFin;
    SimpleDateFormat simpleDateFormatFecha = new SimpleDateFormat("dd/MM/yyy");
    private Vibrator vibrator;
    long tiempo = 100;
    TextView id1,id2;
    LineChart LnMoney;
    ArrayList<Entry> x;
    ArrayList<String> y;
    //private AdapterAllData mAdapterAllData;
    //private RecyclerView recyclerViewData;
    //private ArrayList<AllData> allDataArrayList = new ArrayList<>();
    TextView txt ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_pedido_por_llamada);
        // para reutilizar recycler view  - >  recyclerViewData = (RecyclerView) findViewById(R.id.recyclerAllData);
        // ""  recyclerViewData.setLayoutManager(new LinearLayoutManager(this));
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        checkBoxFiltro = findViewById(R.id.checkFilter);
        mLinearLayout = findViewById(R.id.linearFiltro);
        mLinearLayout.setVisibility(View.INVISIBLE);
        mLinearLayoutFilter = findViewById(R.id.showFilterGanancia);
        mLinearLayoutFilter.setVisibility(View.INVISIBLE);
        txt = findViewById(R.id.txtGananciaDeliveryReporte);

        id1 = findViewById(R.id.idPedidoSeleccionado1);
        id2 = findViewById(R.id.idPedidoSeleccionado2);

        txtFechaIni = findViewById(R.id.txtIni);
        txtFechaFin = findViewById(R.id.txtFin);
        btnGananciaDate = findViewById(R.id.btnFechaGanancia);

        mDialogActualizeData = new ProgressDialog(this,R.style.MyAlertDialogData);
        mDialogActualizeData.setCancelable(false);
        mDialogActualizeData.show();
        mDialogActualizeData.setContentView(R.layout.dialog_data);
        mDialogActualizeData.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        mAuth = FirebaseAuth.getInstance();
        txtGanancia = findViewById(R.id.txtReporteGanancia);
        txtGanancia2 = findViewById(R.id.txtGananciaFiltrada);
        txtFecha1 = findViewById(R.id.txtPrimerPedido);
        txtFecha2 = findViewById(R.id.txtUltimoPedido);
        txtCountTotal = findViewById(R.id.txtTotalCompletados);
        x = new ArrayList<Entry>();
        y = new ArrayList<String>();
        LnMoney = findViewById(R.id.dateChart);
        LnMoney.setDragEnabled(true);
        LnMoney.setDrawGridBackground(false);
        LnMoney.setScaleEnabled(true);
        LnMoney.setTouchEnabled(true);
        LnMoney.setPinchZoom(true);
        LnMoney.setScaleMinima(2f, 1f);
        LnMoney.fitScreen();
        exampleChart();
        checkBoxFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxFiltro.isChecked()) {
                    mLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    mLinearLayout.setVisibility(View.INVISIBLE);
                }
            }
        });

        txtFechaIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(tiempo);
                ClickFechaIni();
            }
        });

        txtFechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(tiempo);
                ClickFechaFin();
            }
        });

        btnGananciaDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(tiempo);
                getMoneyForDate();
            }
        });

        getMoney();
        totalPedidosCompletados();
        getAllData();

    }


    private void getMoney(){

        Query reference = FirebaseDatabase.getInstance().getReference().child("PedidosPorLlamada").child("pedidos").orderByChild("estado").equalTo("Completado");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    double c = 0;
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String delivery = childSnapshot.child("gananciaDelivery").getValue().toString();
                        Double d = Double.parseDouble(delivery);
                        Double e;

                        if (d < 4.00) {
                            e = d * 0.5;
                            c = (c + e);
                        }
                        if (d >= 4.00 && d < 9.00) {
                            e = d * 0.6;
                            c = (c + e);
                        }
                        if (d >= 9.00) {
                            e = d * 0.7;
                            c = (c + e);
                        }

                        String dd = obtieneDosDecimales(c);
                        txtGanancia.setText("S/" + dd);
                    }
                    //implementar
                } else {
                    Toasty.error(ReportePedidoPorLlamada.this, "Sin pedidos completados", Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(ReportePedidoPorLlamada.this, "Error de base de datos", Toast.LENGTH_SHORT,true).show();
            }
        });

    }


    private void ClickFechaIni() {
        final Calendar c = Calendar.getInstance();
        dia = c.get(Calendar.DAY_OF_MONTH);
        mes = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(ReportePedidoPorLlamada.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                final String strDate = format.format(calendar.getTime());

                Query reference = FirebaseDatabase.getInstance().getReference().child("PedidosPorLlamada").child("pedidos")
                        .orderByChild("fechaEntrega").equalTo(strDate);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                String key1 = childSnapshot.getKey();
                                id1.setText(key1);
                                txtFechaIni.setText(strDate);
                            }
                        }
                        else {
                            Toasty.error(ReportePedidoPorLlamada.this, "Sin fecha!", Toast.LENGTH_SHORT).show();
                            txtFechaIni.setText("PULSE AQUÍ!");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                txtFechaIni.setText("Cargando....");
            }

        }, year, mes, dia);
        datePickerDialog.show();
    }



    private void ClickFechaFin() {

        final Calendar c = Calendar.getInstance();
        dia = c.get(Calendar.DAY_OF_MONTH);
        mes = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(ReportePedidoPorLlamada.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                final String strDate = format.format(calendar.getTime());

                Query reference = FirebaseDatabase.getInstance().getReference().child("PedidosPorLlamada").child("pedidos")
                        .orderByChild("fechaEntrega").equalTo(strDate);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                String key2 = childSnapshot.getKey();
                                id2.setText(key2);
                                txtFechaFin.setText(strDate);

                            }
                        }
                        else {
                            Toasty.error(ReportePedidoPorLlamada.this, "Sin fecha!", Toast.LENGTH_SHORT).show();
                            txtFechaFin.setText("PULSE AQUÍ!");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                txtFechaFin.setText("Cargando...");
            }

        }, year, mes, dia);
        datePickerDialog.show();
    }


    private void totalPedidosCompletados(){

        mDatabase.child("PedidosPorLlamada").child("pedidos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    long n  =  dataSnapshot.getChildrenCount();
                    String num = String.valueOf(n);
                    txtCountTotal.setText(num);
                    mDialogActualizeData.dismiss();
                }
                else {
                    finish();
                    Toasty.info(ReportePedidoPorLlamada.this, "error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(ReportePedidoPorLlamada.this, "Error de base de datos", Toast.LENGTH_SHORT,true).show();
            }
        });
    }

    private void getMoneyForDate(){

        String stFechaIni = txtFechaIni.getText().toString();
        String stFechaFin = txtFechaFin.getText().toString();
        String stID1 = id1.getText().toString();
        String stID2 = id2.getText().toString();

        if(!stFechaIni.equals("PULSE AQUÍ!") && !stFechaFin.equals("PULSE AQUÍ!") && !stFechaIni.equals("Cargando...") && !stFechaFin.equals("Cargando...")){
            Query reference = FirebaseDatabase.getInstance().getReference().child("PedidosPorLlamada").child("pedidos").orderByKey().startAt(stID1).endAt(stID2);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        double c = 0;
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                String delivery = childSnapshot.child("gananciaDelivery").getValue().toString();
                                Double d = Double.parseDouble(delivery);
                                Double e;
                                if (d < 4.00) {
                                    e = d * 0.5;
                                    c = (c + e);
                                }
                                if (d >= 4.00 && d < 9.00) {
                                    e = d * 0.6;
                                    c = (c + e);
                                }
                                if (d >= 9.00) {
                                    e = d * 0.7;
                                    c = (c + e);
                                }
                                mLinearLayoutFilter.setVisibility(View.VISIBLE);
                                String dd = obtieneDosDecimales(c);
                                txtGanancia2.setText("S/" + dd);

                        }
                        //implementar
                    }
                    else {
                        Toasty.error(ReportePedidoPorLlamada.this, "Sin pedidos completados", Toast.LENGTH_SHORT, true).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toasty.error(ReportePedidoPorLlamada.this, "Error de base de datos", Toast.LENGTH_SHORT,true).show();
                }
            });

        }

        else {
            Toasty.error(this, "Seleccione las fechas!", Toast.LENGTH_SHORT).show();
        }

    }

    private String obtieneDosDecimales(double valor) {
        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(2); //Define 2 decimales.
        return format.format(valor);
    }

    private void exampleChart(){

        Query reference = FirebaseDatabase.getInstance().getReference().child("PedidosPorLlamada").child("pedidos").orderByChild("estado").equalTo("Completado");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              if(dataSnapshot.exists()) {
                  for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                      String value = childSnapshot.child("gananciaComision").getValue().toString();
                      Float val = Float.parseFloat(value);
                      x.add(new Entry(val, dataSnapshot.getChildrenCount()));
                  }
                  LineDataSet set = new LineDataSet(x,"Ganancias");
                  set.setFillAlpha(110);
                  set.setColor(Color.RED);
                  set.setValueTextSize(10f);
                  set.setValueTextColor(Color.BLACK);
                  ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                  dataSets.add(set);
                  LineData data = new LineData(dataSets);
                  LnMoney.setData(data);
              }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getAllData(){
        mDatabase.child("PedidosPorLlamada").child("pedidos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        String delivery = ds.child("gananciaDelivery").getValue().toString();
                        txt.append(delivery+"\n");
                        //allDataArrayList.add(new AllData(delivery));
                    }
                    //mAdapterAllData = new AdapterAllData(allDataArrayList , R.layout.row_all_data);
                    //recyclerViewData.setAdapter(mAdapterAllData);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    @Override
    public void onBackPressed() {
        finish();
    }
}