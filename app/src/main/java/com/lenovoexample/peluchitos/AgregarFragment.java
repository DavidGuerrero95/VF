package com.lenovoexample.peluchitos;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import android.app.Activity;


/**
 * A simple {@link Fragment} subclass.
 */
public class AgregarFragment extends Fragment {
    private EditText eNombre, eCantidad, ePrecio;
    private Button bEnviar;
    int contador = 0,contador1 = 0;
    comunicador interfaz;
    ArrayList<String> nombre = new ArrayList<String>();
    ArrayList<String> cantidad = new ArrayList<String>();
    ArrayList<String> precio = new ArrayList<String>();
    ArrayList<String> nombre1 = new ArrayList<String>();
    ArrayList<String> cantidad1 = new ArrayList<String>();
    ArrayList<String> precio1 = new ArrayList<String>();

    public AgregarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_agregar, container, false);

        eNombre = view.findViewById(R.id.eNombre);
        eCantidad = view.findViewById(R.id.eCantidad);
        ePrecio = view.findViewById(R.id.ePrecio);
        bEnviar = view.findViewById(R.id.bEnviar);

        Bundle bundle = getArguments();
        if(bundle != null) {
            contador1 = bundle.getInt("contador");
            nombre1 = bundle.getStringArrayList("nombre");
            cantidad1 = bundle.getStringArrayList("cantidad");
            precio1 = bundle.getStringArrayList("precio");
        }

        bEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = eNombre.getText().toString();
                String cant = eCantidad.getText().toString();
                String val = ePrecio.getText().toString();
                if (name.equals("") || cant.equals("") || val.equals("")) {
                    Toast.makeText(getActivity(), "Ingrese Todos los datos", Toast.LENGTH_SHORT).show();
                }else{
                    contador = nombre.size();
                    nombre.add(contador, name);
                    cantidad.add(contador, cant);
                    precio.add(contador, cant);
                    contador++;
                    Toast.makeText(getActivity(), "Peluche Agregado", Toast.LENGTH_SHORT).show();
                    cleanWidgets();
                }
            }
        });

        interfaz.envioDatos(nombre, cantidad, precio, contador);
        return view;
    }

    private void cleanWidgets() {
        eNombre.setText("");
        eCantidad.setText("");
        ePrecio.setText("");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            interfaz = (comunicador) activity;
        }catch (ClassCastException e){
        }
    }
}