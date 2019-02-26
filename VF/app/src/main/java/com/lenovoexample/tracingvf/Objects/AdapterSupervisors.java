package com.lenovoexample.tracingvf.Objects;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lenovoexample.tracingvf.R;

import java.util.List;

public class AdapterSupervisors extends RecyclerView.Adapter<AdapterSupervisors.SuperviewHolder> {

    List<Supervisores> supervisores;

    public AdapterSupervisors(List<Supervisores> supervisores) {
        this.supervisores = supervisores;
    }

    @NonNull
    @Override
    public SuperviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.supervisors_recycler, parent,false);
        SuperviewHolder holder = new SuperviewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SuperviewHolder holder, int position) {
        Supervisores supervisor = supervisores.get(position);
        holder.tNombre.setText(supervisor.getName());
        holder.tDescripion.setText(supervisor.getAdress());
        holder.tCelular.setText(supervisor.getCellphone());
        holder.tCorreo.setText(supervisor.getEmail());
    }

    @Override
    public int getItemCount() {
        return supervisores.size();
    }

    public static class SuperviewHolder extends RecyclerView.ViewHolder{

        TextView tNombre, tDescripion, tCorreo, tCelular;

        public SuperviewHolder(View itemView) {
            super(itemView);
            tNombre = itemView.findViewById(R.id.tNombre);
            tDescripion = itemView.findViewById(R.id.tDescripcion);
            tCelular = itemView.findViewById(R.id.tCelular);
            tCorreo = itemView.findViewById(R.id.tCorreo);
        }
    }
}
