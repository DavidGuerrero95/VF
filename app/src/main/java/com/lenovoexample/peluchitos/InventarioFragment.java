package com.lenovoexample.peluchitos;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class InventarioFragment extends Fragment {
    private TextView tDatos;
    int contador = 0;
    ArrayList<String> nombre = new ArrayList<String>();
    ArrayList<String> cantidad = new ArrayList<String>();
    ArrayList<String> precio = new ArrayList<String>();
    ArrayList<String> listDatos;
    RecyclerView recycler;
    InventarioAdapter adapter ;

    public InventarioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inventario, container, false);
        Bundle bundle = getArguments();

        if(bundle != null) {
            nombre = bundle.getStringArrayList("nombre");
            cantidad = bundle.getStringArrayList("cantidad");
            precio = bundle.getStringArrayList("precio");
            contador = bundle.getInt("contador");
        }
        recycler = view.findViewById(R.id.recyclerId);
        listDatos = new ArrayList<String>();
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        for(int i = 0; i<nombre.size(); i++){
            listDatos.add("\npeluche:"+(i+1)+ "\nNombre: " + nombre.get(i) + "\nCantidad: "
                        + cantidad.get(i) + "\nPrecio: " + precio.get(i));
        }
        adapter = new InventarioAdapter(listDatos);
        recycler.setAdapter(adapter);
        return view;
    }
}