package aukde.food.gestordepedidos.paquetes.Adaptadores;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import aukde.food.gestordepedidos.R;
import aukde.food.gestordepedidos.paquetes.Modelos.AllData;


public class AdapterAllData extends RecyclerView.Adapter<AdapterAllData.ViewHolder> {

    private int resource;
    private ArrayList<AllData> allDataArrayList;

    public AdapterAllData(ArrayList<AllData> allDataArrayList , int resource) {
        this.allDataArrayList = allDataArrayList;
        this.resource = resource;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int index) {
          AllData allData = allDataArrayList.get(index);
          holder.txtGananciaAdapter.setText(allData.getGananciaDelivery());
    }

    @Override
    public int getItemCount() {
        return allDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtGananciaAdapter;
        public View view;

        public ViewHolder(View view){
              super(view);
              this.view = view;
              this.txtGananciaAdapter = (TextView) view.findViewById(R.id.itemGanancias);
        }

    }

}
