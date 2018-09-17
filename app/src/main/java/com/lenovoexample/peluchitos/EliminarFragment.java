package com.lenovoexample.peluchitos;


import android.app.Activity;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class EliminarFragment extends Fragment {

    private EditText eNombre;
    private Button bEnviar;
    eliminar interfaz;
    int contador = 0, index;
    ArrayList<String> nombre1 = new ArrayList<String>();
    ArrayList<String> cantidad1 = new ArrayList<String>();
    ArrayList<String> precio1 = new ArrayList<String>();


    public EliminarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_eliminar, container, false);
        eNombre = view.findViewById(R.id.eNombre);
        bEnviar = view.findViewById(R.id.bEnviar);

        Bundle bundle = getArguments();
        if(bundle != null) {
            nombre1 = bundle.getStringArrayList("nombre");
            cantidad1 = bundle.getStringArrayList("cantidad");
            precio1 = bundle.getStringArrayList("precio");
            contador = nombre1.size();
        }

        bEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name;
                name = eNombre.getText().toString();
                if (name.equals("")) {
                    name = "FFFFFF";
                }
                if (nombre1.contains(name)) {
                    index = nombre1.indexOf(name);
                    nombre1.remove(index);
                    cantidad1.remove(index);
                    precio1.remove(index);
                    contador = nombre1.size();
                    Toast.makeText(getActivity(), "Elemento Eliminado", Toast.LENGTH_SHORT).show();
                    cleanWidgets();
                } else {
                    Toast.makeText(getActivity(), "Dato no encontrado", Toast.LENGTH_SHORT).show();
                    cleanWidgets();
                }
            }
        });
        interfaz.eliminoDatos(nombre1,cantidad1,precio1,contador);
        return view;
    }

    private void cleanWidgets() {
        eNombre.setText("");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            interfaz = (eliminar) activity;
        } catch (ClassCastException e) {
        }
    }
}
