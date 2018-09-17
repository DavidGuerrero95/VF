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


/**
 * A simple {@link Fragment} subclass.
 */
public class BuscarFragment extends Fragment {
    private EditText eNombre;
    private TextView tDatos;
    private Button bEnviar;
    int contador = 0;
    ArrayList<String> nombre = new ArrayList<String>();
    ArrayList<String> cantidad = new ArrayList<String>();
    ArrayList<String> precio = new ArrayList<String>();

    public BuscarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buscar, container, false);
        eNombre = view.findViewById(R.id.eNombre);
        tDatos = view.findViewById(R.id.tDatos);
        bEnviar = view.findViewById(R.id.bEnviar);

        bEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name;
                Bundle bundle = getArguments();
                if(bundle != null) {
                    nombre = bundle.getStringArrayList("nombre");
                    cantidad = bundle.getStringArrayList("cantidad");
                    precio = bundle.getStringArrayList("precio");
                    contador = bundle.getInt("contador");
                }
                name = eNombre.getText().toString();
                if (name.equals("")) {
                    name = "#FFFFFF";
                }
                if (nombre.contains(name)) {
                    tDatos.setText("id: " + (nombre.indexOf(name)+1) + "\nNombre: " + nombre.get(nombre.indexOf(name)) + "\nCantidad: "
                            + cantidad.get(nombre.indexOf(name)) + "\nPrecio: " + precio.get(nombre.indexOf(name)));
                    cleanWidgets();
                } else {
                    Toast.makeText(getActivity(), "Dato no encontrado", Toast.LENGTH_SHORT).show();
                    cleanBar();
                    cleanWidgets();
                }
            }
        });
        return view;
    }

    private void cleanBar() {
        tDatos.setText("");
    }

    private void cleanWidgets() {
        eNombre.setText("");
    }
}
