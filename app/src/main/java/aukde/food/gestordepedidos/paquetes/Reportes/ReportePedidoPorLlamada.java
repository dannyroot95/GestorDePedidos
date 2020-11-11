package aukde.food.gestordepedidos.paquetes.Reportes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import aukde.food.gestordepedidos.paquetes.Inclusiones.MiToolbar;
import es.dmoral.toasty.Toasty;

public class ReportePedidoPorLlamada extends AppCompatActivity{

    FirebaseAuth mAuth;
    TextView txtGanancia , txtFecha1 , txtFecha2 , txtCountTotal , txtCountEspera , txtCountProcesando ,
            txtFechaGeneral1, txtFechaGeneral2
            , txtGananciaAukdeliver , txtGananciaDeliveryTotal;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    DatabaseReference referenceDataIni = FirebaseDatabase.getInstance().getReference();
    DatabaseReference referenceDataFin = FirebaseDatabase.getInstance().getReference();
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
    String [] m = {"Ene","Feb","Mar","Abr","May","Jun","Jul","Ago","Sep","Oct","Nov","Dic"};
    //private AdapterAllData mAdapterAllData;
    //private RecyclerView recyclerViewData;
    //private ArrayList<AllData> allDataArrayList = new ArrayList<>();
    TextView txt , txtGananciaComisionTotal , txtGananciaTotal ;
    Button btnGrafico;
    FloatingActionButton btnRefresh;
    LineChart mData;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_pedido_por_llamada);
        MiToolbar.Mostrar(ReportePedidoPorLlamada.this,"Financias y Reportes",true);
        // para reutilizar recycler view  - >  recyclerViewData = (RecyclerView) findViewById(R.id.recyclerAllData);
        // ""  recyclerViewData.setLayoutManager(new LinearLayoutManager(this));
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        checkBoxFiltro = findViewById(R.id.checkFilter);
        mLinearLayout = findViewById(R.id.linearFiltro);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        mLinearLayout.setLayoutParams(params);

        mLinearLayoutFilter = findViewById(R.id.showFilterGanancia);
        txt = findViewById(R.id.txtGananciaDeliveryReporte);
        id1 = findViewById(R.id.idPedidoSeleccionado1);
        id2 = findViewById(R.id.idPedidoSeleccionado2);

        txtFechaIni = findViewById(R.id.txtIni);
        txtFechaFin = findViewById(R.id.txtFin);
        btnGananciaDate = findViewById(R.id.btnFechaGanancia);
        btnGrafico = findViewById(R.id.btn_graph);

        mDialogActualizeData = new ProgressDialog(this,R.style.MyAlertDialogData);
        mDialogActualizeData.setCancelable(false);
        mDialogActualizeData.show();
        mDialogActualizeData.setContentView(R.layout.dialog_data);
        mDialogActualizeData.getWindow().setBackgroundDrawableResource(android.R.color.white);

        mAuth = FirebaseAuth.getInstance();
        txtGanancia = findViewById(R.id.txtReporteGanancia);
        txtGananciaAukdeliver = findViewById(R.id.txtReporteGananciaRepartidor);
        txtGananciaDeliveryTotal = findViewById(R.id.txtReporteGananciaDeliveryTotal);
        txtFecha1 = findViewById(R.id.txtPrimerPedido);
        txtFecha2 = findViewById(R.id.txtUltimoPedido);
        txtCountTotal = findViewById(R.id.txtTotalCompletados);
        txtCountEspera = findViewById(R.id.txtTotalEnEspera);
        txtCountProcesando = findViewById(R.id.txtTotalProcesando);
        txtFechaGeneral1 = findViewById(R.id.txtDate1Report);
        txtFechaGeneral2 = findViewById(R.id.txtDate2Report);
        txtGananciaComisionTotal = findViewById(R.id.txtGananciaComision);
        txtGananciaTotal = findViewById(R.id.txtTotalGanancia);
        btnRefresh = findViewById(R.id.floatBtnRefresh);

        mData = (LineChart) findViewById(R.id.dateChart);
        mData.setDragEnabled(true);
        mData.getLegend().setEnabled(false);
        mData.setScaleEnabled(true);
        mData.setScaleMinima(6f, 1f);
        mData.zoom(3,1,3,1);

        checkBoxFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxFiltro.isChecked()) {
                    LinearLayout.LayoutParams params = new LinearLayout
                            .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    mLinearLayout.setLayoutParams(params);
                } else {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
                    mLinearLayout.setLayoutParams(params);
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
                //totalPedidosCompletadosFiltrado();
                getComisionForDate();
            }
        });

        btnGrafico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(tiempo);
                Intent intent = new Intent(ReportePedidoPorLlamada.this,GraficoGanancias.class);
                intent.putExtra("keyValue",txt.getText().toString());
                startActivity(intent);
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(ReportePedidoPorLlamada.this,ReportePedidoPorLlamada.class));
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
        getMoneyAukdeliver();
        getComision();
    }


    private void getComision(){

        Query reference = FirebaseDatabase.getInstance().getReference().child("PedidosPorLlamada").child("pedidos").orderByChild("estado").equalTo("Completado");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    double c = 0;
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String comision = childSnapshot.child("gananciaComision").getValue().toString();
                        Double d = Double.parseDouble(comision);
                        c = c + d;
                        String dd = obtieneDosDecimales(c);
                        txtGananciaComisionTotal.setText("S/" + dd);
                        String sTcomision = txtGananciaComisionTotal.getText().toString();
                        String sTDelivery = txtGanancia.getText().toString();
                        String [] desc1 = sTcomision.split("S/");
                        String [] desc2 = sTDelivery.split("S/");
                        Double com = Double.parseDouble(desc1[1]);
                        Double del = Double.parseDouble(desc2[1]);
                        Double sum = com + del;
                        String stSum = String.valueOf(sum);
                        txtGananciaTotal.setText("S/"+stSum);
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

    private void getMoneyAukdeliver(){

        Query reference = FirebaseDatabase.getInstance().getReference().child("PedidosPorLlamada").child("pedidos").orderByChild("estado").equalTo("Completado");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    double c = 0;
                    double cc = 0;
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String delivery = childSnapshot.child("gananciaDelivery").getValue().toString();
                        Double d = Double.parseDouble(delivery);
                        Double total =  Double.parseDouble(delivery);
                        cc = cc + total;
                        String stTotal = obtieneDosDecimales(cc);
                        txtGananciaDeliveryTotal.setText("S/"+stTotal);
                        Double e;

                        if (d < 4.00) {
                            e = d * 0.5;
                            c = (c + e);
                        }
                        if (d >= 4.00 && d < 9.00) {
                            e = d * 0.4;
                            c = (c + e);
                        }
                        if (d >= 9.00) {
                            e = d * 0.3;
                            c = (c + e);
                        }

                        String dd = obtieneDosDecimales(c);
                        txtGananciaAukdeliver.setText("S/" + dd);
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
        getDateAll();
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
                final String strDateIni = format.format(calendar.getTime());

                String[] descomponer = strDateIni.split("/");
                if (descomponer[1].equals("01")) {
                    txtFechaGeneral1.setText(m[0] + " " + strDateIni);
                } else if (descomponer[1].equals("02")) {
                    txtFechaGeneral1.setText(m[1] + " " + strDateIni);
                } else if (descomponer[1].equals("03")) {
                    txtFechaGeneral1.setText(m[2] + " " + strDateIni);
                } else if (descomponer[1].equals("04")) {
                    txtFechaGeneral1.setText(m[3] + " " + strDateIni);
                } else if (descomponer[1].equals("05")) {
                    txtFechaGeneral1.setText(m[4] + " " + strDateIni);
                } else if (descomponer[1].equals("06")) {
                    txtFechaGeneral1.setText(m[5] + " " + strDateIni);
                } else if (descomponer[1].equals("07")) {
                    txtFechaGeneral1.setText(m[6] + " " + strDateIni);
                } else if (descomponer[1].equals("08")) {
                    txtFechaGeneral1.setText(m[7] + " " + strDateIni);
                } else if (descomponer[1].equals("09")) {
                    txtFechaGeneral1.setText(m[8] + " " + strDateIni);
                } else if (descomponer[1].equals("10")) {
                    txtFechaGeneral1.setText(m[9] + " " + strDateIni);
                } else if (descomponer[1].equals("11")) {
                    txtFechaGeneral1.setText(m[10] + " " + strDateIni);
                } else {
                    txtFechaGeneral1.setText(m[11] + " " + strDateIni);
                }

                Query reference = FirebaseDatabase.getInstance().getReference().child("PedidosPorLlamada").child("pedidos")
                        .orderByChild("fechaEntrega").equalTo(strDateIni).limitToFirst(1);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                String key1 = childSnapshot.getKey();
                                id1.setText(key1);
                                txtFechaIni.setText(strDateIni);
                            }
                        }
                        else {
                            Toasty.error(ReportePedidoPorLlamada.this, "Sin registros!", Toast.LENGTH_SHORT).show();
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

                String[] descomponer = strDate.split("/");

                if (descomponer[1].equals("01")) {
                    txtFechaGeneral2.setText("vs "+m[0] + " " + strDate);
                } else if (descomponer[1].equals("02")) {
                    txtFechaGeneral2.setText("vs "+m[1] + " " + strDate);
                } else if (descomponer[1].equals("03")) {
                    txtFechaGeneral2.setText("vs "+m[2] + " " + strDate);
                } else if (descomponer[1].equals("04")) {
                    txtFechaGeneral2.setText("vs "+m[3] + " " + strDate);
                } else if (descomponer[1].equals("05")) {
                    txtFechaGeneral2.setText("vs "+m[4] + " " + strDate);
                } else if (descomponer[1].equals("06")) {
                    txtFechaGeneral2.setText("vs "+m[5] + " " + strDate);
                } else if (descomponer[1].equals("07")) {
                    txtFechaGeneral2.setText("vs "+m[6] + " " + strDate);
                } else if (descomponer[1].equals("08")) {
                    txtFechaGeneral2.setText("vs "+m[7] + " " + strDate);
                } else if (descomponer[1].equals("09")) {
                    txtFechaGeneral2.setText("vs "+m[8] + " " + strDate);
                } else if (descomponer[1].equals("10")) {
                    txtFechaGeneral2.setText("vs "+m[9] + " " + strDate);
                } else if (descomponer[1].equals("11")) {
                    txtFechaGeneral2.setText("vs "+m[10] + " " + strDate);
                } else {
                    txtFechaGeneral2.setText("vs "+m[11] + " " + strDate);
                }

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
                            Toasty.error(ReportePedidoPorLlamada.this, "Sin registros!", Toast.LENGTH_SHORT).show();
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

        mDatabase.child("PedidosPorLlamada").child("pedidos").orderByChild("estado").equalTo("Completado").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    long n  =  dataSnapshot.getChildrenCount();
                    String num = String.valueOf(n);
                    txtCountTotal.setText(num);
                    totalPedidosEnEspera();
                    totalPedidosProcesando();
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


    private void totalPedidosEnEspera(){

        mDatabase.child("PedidosPorLlamada").child("pedidos").orderByChild("estado").equalTo("En espera").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    long n  =  dataSnapshot.getChildrenCount();
                    String num = String.valueOf(n);
                    txtCountEspera.setText(num);
                }
                else {
                    Toasty.info(ReportePedidoPorLlamada.this, "error", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(ReportePedidoPorLlamada.this, "Error de base de datos", Toast.LENGTH_SHORT,true).show();
            }
        });
    }

    private void totalPedidosProcesando(){

        mDatabase.child("PedidosPorLlamada").child("pedidos").orderByChild("estado").equalTo("Cancelado").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    long n  =  dataSnapshot.getChildrenCount();
                    String num = String.valueOf(n);
                    txtCountProcesando.setText(num);
                }
                else {
                    Toasty.info(ReportePedidoPorLlamada.this, "error", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(ReportePedidoPorLlamada.this, "Error de base de datos", Toast.LENGTH_SHORT,true).show();
            }
        });
    }

    private void getMoneyForDate() {

        String stFechaIni = txtFechaIni.getText().toString();
        String stFechaFin = txtFechaFin.getText().toString();
        String stID1 = id1.getText().toString();
        String stID2 = id2.getText().toString();


        if(!stFechaIni.equals("PULSE AQUÍ!") && !stFechaFin.equals("PULSE AQUÍ!") && !stFechaIni.equals("Cargando...") && !stFechaFin.equals("Cargando...")){
            txt.setText("");
            reference.child("PedidosPorLlamada").child("pedidos").orderByKey().startAt(stID1).endAt(stID2).addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        double cc = 0;
                        double ccc = 0;
                        int ctxComple = 0;
                        int ctxEsper = 0;
                        int ctxProce = 0;

                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            String delivery = childSnapshot.child("gananciaDelivery").getValue().toString();
                            String completados = childSnapshot.child("estado").getValue().toString();
                                double c = 0;
                                Double total =  Double.parseDouble(delivery);
                                cc = cc + total;
                                String stTotal = obtieneDosDecimales(cc);
                                txtGananciaDeliveryTotal.setText("S/"+stTotal);

                                Double d = Double.parseDouble(delivery);
                                Double e;
                                if (d < 4.00) {
                                    e = d * 0.5;
                                    ccc = (ccc + e);
                                    c = (c + e);
                                }
                                if (d >= 4.00 && d < 9.00) {
                                    e = d * 0.6;
                                    ccc = (ccc + e);
                                    c = (c+ e);
                                }
                                if (d >= 9.00) {
                                    e = d * 0.7;
                                    ccc = (ccc + e);
                                    c = (c + e);
                                }

                                String dd = obtieneDosDecimales(ccc);
                                String ddd = obtieneDosDecimales(c);
                                txtGanancia.setText("S/" + dd);
                                txt.append(ddd+"\n");

                            if(completados.equals("Completado")){
                                ctxComple = ctxComple + 1;
                            }
                            else if(completados.equals("En espera")){
                                ctxEsper = ctxEsper + 1;
                            }
                            else if(completados.equals("En proceso")){
                                ctxProce= ctxProce+ 1;
                            }

                            String sTCtx1 = String.valueOf(ctxComple);
                            String sTCtx2 = String.valueOf(ctxEsper);
                            String sTCtx3 = String.valueOf(ctxProce);
                            txtCountTotal.setText(sTCtx1);
                            txtCountEspera.setText(sTCtx2);
                            txtCountProcesando.setText(sTCtx3);


                        }

                        String [] Desc = txt.getText().toString().split("\n");
                        ArrayList<Entry> yValues = new ArrayList<>();

                        for (int index = 0; index < Desc.length; index++) {
                            Float mFloat = Float.parseFloat(Desc[index]);
                            yValues.add(new Entry(index,mFloat));
                        }

                        LineDataSet set = new LineDataSet(yValues,null);
                        set.setFillAlpha(110);
                        set.setLineWidth(1.5f);
                        set.setColor(Color.rgb(126, 0, 138));
                        //set.setCircleColor(Color.rgb(126, 0, 138));
                        set.setDrawCircles(false);
                        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                        dataSets.add(set);
                        LineData data = new LineData(dataSets);
                        mData.clear();
                        mData.invalidate();
                        mData.setData(data);

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
            getMoneyForDateAukdeliver();
        }

        else {
            Toasty.error(this, "Seleccione las fechas!", Toast.LENGTH_SHORT).show();
        }
    }

    private void getMoneyForDateAukdeliver(){

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
                                e = d * 0.4;
                                c = (c + e);
                            }
                            if (d >= 9.00) {
                                e = d * 0.3;
                                c = (c + e);
                            }
                            String dd = obtieneDosDecimales(c);
                            txtGananciaAukdeliver.setText("S/" + dd);
                        }
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

    private void getAllData(){
        mDatabase.child("PedidosPorLlamada").child("pedidos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        String delivery = ds.child("gananciaDelivery").getValue().toString();
                        Double d = Double.parseDouble(delivery);
                        double c = 0;
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
                        String ctx = obtieneDosDecimales(c);
                        txt.append(ctx+"\n");

                    }

                    String [] Desc = txt.getText().toString().split("\n");
                    ArrayList<Entry> yValues = new ArrayList<>();

                    for (int index = 0; index < Desc.length; index++) {
                        Float mFloat = Float.parseFloat(Desc[index]);
                        yValues.add(new Entry(index,mFloat));
                    }

                    LineDataSet set = new LineDataSet(yValues,null);
                    set.setFillAlpha(110);
                    set.setLineWidth(1.5f);
                    set.setColor(Color.rgb(126, 0, 138));
                    //set.setCircleColor(Color.rgb(126, 0, 138));
                    set.setDrawCircles(false);
                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                    dataSets.add(set);
                    LineData data = new LineData(dataSets);
                    mData.invalidate();
                    mData.setData(data);
                    mDialogActualizeData.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getDateAll(){

        referenceDataIni.child("PedidosPorLlamada").child("pedidos").orderByKey().limitToFirst(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String fechaIni = childSnapshot.child("fechaPedido").getValue().toString();
                    String[] descomponer = fechaIni.split("/");
                    if (descomponer[1].equals("01")) {
                        txtFechaGeneral1.setText(m[0] + " " + fechaIni);
                    } else if (descomponer[1].equals("02")) {
                        txtFechaGeneral1.setText(m[1] + " " + fechaIni);
                    } else if (descomponer[1].equals("03")) {
                        txtFechaGeneral1.setText(m[2] + " " + fechaIni);
                    } else if (descomponer[1].equals("04")) {
                        txtFechaGeneral1.setText(m[3] + " " + fechaIni);
                    } else if (descomponer[1].equals("05")) {
                        txtFechaGeneral1.setText(m[4] + " " + fechaIni);
                    } else if (descomponer[1].equals("06")) {
                        txtFechaGeneral1.setText(m[5] + " " + fechaIni);
                    } else if (descomponer[1].equals("07")) {
                        txtFechaGeneral1.setText(m[6] + " " + fechaIni);
                    } else if (descomponer[1].equals("08")) {
                        txtFechaGeneral1.setText(m[7] + " " + fechaIni);
                    } else if (descomponer[1].equals("09")) {
                        txtFechaGeneral1.setText(m[8] + " " + fechaIni);
                    } else if (descomponer[1].equals("10")) {
                        txtFechaGeneral1.setText(m[9] + " " + fechaIni);
                    } else if (descomponer[1].equals("11")) {
                        txtFechaGeneral1.setText(m[10] + " " + fechaIni);
                    } else {
                        txtFechaGeneral1.setText(m[11] + " " + fechaIni);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        referenceDataFin.child("PedidosPorLlamada").child("pedidos").orderByKey().limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {

                            String fechaIni = childSnapshot.child("fechaPedido").getValue().toString();
                            String[] descomponer = fechaIni.split("/");

                            if (descomponer[1].equals("01")) {
                                txtFechaGeneral2.setText("vs "+m[0] + " " + fechaIni);
                            } else if (descomponer[1].equals("02")) {
                                txtFechaGeneral2.setText("vs "+m[1] + " " + fechaIni);
                            } else if (descomponer[1].equals("03")) {
                                txtFechaGeneral2.setText("vs "+m[2] + " " + fechaIni);
                            } else if (descomponer[1].equals("04")) {
                                txtFechaGeneral2.setText("vs "+m[3] + " " + fechaIni);
                            } else if (descomponer[1].equals("05")) {
                                txtFechaGeneral2.setText("vs "+m[4] + " " + fechaIni);
                            } else if (descomponer[1].equals("06")) {
                                txtFechaGeneral2.setText("vs "+m[5] + " " + fechaIni);
                            } else if (descomponer[1].equals("07")) {
                                txtFechaGeneral2.setText("vs "+m[6] + " " + fechaIni);
                            } else if (descomponer[1].equals("08")) {
                                txtFechaGeneral2.setText("vs "+m[7] + " " + fechaIni);
                            } else if (descomponer[1].equals("09")) {
                                txtFechaGeneral2.setText("vs "+m[8] + " " + fechaIni);
                            } else if (descomponer[1].equals("10")) {
                                txtFechaGeneral2.setText("vs "+m[9] + " " + fechaIni);
                            } else if (descomponer[1].equals("11")) {
                                txtFechaGeneral2.setText("vs "+m[10] + " " + fechaIni);
                            } else {
                                txtFechaGeneral2.setText("vs "+m[11] + " " + fechaIni);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void getComisionForDate(){

        String stID1 = id1.getText().toString();
        String stID2 = id2.getText().toString();

        Query reference = FirebaseDatabase.getInstance().getReference().child("PedidosPorLlamada").child("pedidos").orderByKey().startAt(stID1).endAt(stID2);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    double c = 0;
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String comision = childSnapshot.child("gananciaComision").getValue().toString();
                        Double d = Double.parseDouble(comision);
                        c = c + d;
                        String dd = obtieneDosDecimales(c);
                        txtGananciaComisionTotal.setText("S/" + dd);
                        String sTcomision = txtGananciaComisionTotal.getText().toString();
                        String sTDelivery = txtGanancia.getText().toString();
                        String [] desc1 = sTcomision.split("S/");
                        String [] desc2 = sTDelivery.split("S/");
                        Double com = Double.parseDouble(desc1[1]);
                        Double del = Double.parseDouble(desc2[1]);
                        Double sum = com + del;
                        String stSum = String.valueOf(sum);
                        txtGananciaTotal.setText("S/"+stSum);
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

    @Override
    public void onBackPressed() {
        finish();
    }
}