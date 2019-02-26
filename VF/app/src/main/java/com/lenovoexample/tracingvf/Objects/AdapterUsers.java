package com.lenovoexample.tracingvf.Objects;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lenovoexample.tracingvf.R;

import java.util.List;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.UsersviewHolder>{

    List<Usuarios> usuarios;

    public AdapterUsers(List<Usuarios> usuarios) {
        this.usuarios = usuarios;
    }

    @NonNull
    @Override
    public UsersviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_recycler, parent,false);
        UsersviewHolder holder = new UsersviewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UsersviewHolder holder, int position) {
        Usuarios usuario = usuarios.get(position);
        holder.tNombre.setText(usuario.getName());
        holder.tDescripion.setText(usuario.getAdress());
        holder.tCorreo.setText(usuario.getEmail());
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public static class UsersviewHolder extends RecyclerView.ViewHolder{

        TextView tNombre, tDescripion, tCorreo;

        public UsersviewHolder(View itemView) {
            super(itemView);
            tNombre = itemView.findViewById(R.id.tNombre);
            tDescripion = itemView.findViewById(R.id.tDescripcion);
            tCorreo = itemView.findViewById(R.id.tCorreo);
        }
    }
}
