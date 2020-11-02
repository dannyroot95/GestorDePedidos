package aukde.food.gestordepedidos.paquetes.Reportes;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;


import java.util.ArrayList;

import aukde.food.gestordepedidos.R;
import es.dmoral.toasty.Toasty;

public class GraficoGanancias extends AppCompatActivity {

    Bundle value;
    LineChart mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico_ganancias);

        mData = (LineChart) findViewById(R.id.dateChart2);
        mData.setDragEnabled(true);
        mData.setScaleEnabled(true);
        mData.setScaleMinima(8f, 1f);
        mData.zoom(3,1,3,1);

        value = getIntent().getExtras();
        if (value.isEmpty()) {
            Toasty.error(this, "Error", Toasty.LENGTH_SHORT).show();
            finish();
        }

        getAllData();

    }

    private void getAllData(){

        String dataVal = getIntent().getStringExtra("keyValue");
        String [] Desc = dataVal.split("\n");
        ArrayList<Entry> yValues = new ArrayList<>();

            for (int index = 0; index < Desc.length; index++) {
                Float mFloat = Float.parseFloat(Desc[index]);
                yValues.add(new Entry(index,mFloat));
        }

            LineDataSet set = new LineDataSet(yValues, "Ganancias");
            set.setFillAlpha(110);
            set.setLineWidth(1.5f);
            set.setColor(Color.rgb(126, 0, 138));
            //set.setCircleColor(Color.rgb(126, 0, 138));
            set.setDrawCircles(false);
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set);
            LineData data = new LineData(dataSets);
            mData.setData(data);


    }

    @Override
    public void onBackPressed() {
        finish();
    }
}