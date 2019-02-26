package com.lenovoexample.tracingvf;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lenovoexample.tracingvf.Objects.Supervisores;

public class AgregarSupervisorActivity extends AppCompatActivity {

    EditText eNombre, eCorreo, eCelular, eDireccion,eContraseña;
    Button bButon;
    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_supervisor);

        eNombre = findViewById(R.id.eNombre);
        eCorreo = findViewById(R.id.eCorreo);
        eCelular = findViewById(R.id.eCelular);
        eContraseña = findViewById(R.id.eContraseña);
        eDireccion = findViewById(R.id.eDireccion);
        progressDialog = new ProgressDialog(this);
        bButon = findViewById(R.id.bButon);
        bButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarSupervisor();
            }
        });
    }

    private void registrarSupervisor() {
        String nombre = eNombre.getText().toString().trim();
        String correo = eCorreo.getText().toString().trim();
        String celular = eCelular.getText().toString().trim();
        String direccion = eDireccion.getText().toString().trim();
        String contrasena = eContraseña.getText().toString().trim();
        if(TextUtils.isEmpty(nombre)){
            Toast.makeText(this, "Ingresar un nombre", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(correo)){
            Toast.makeText(this, "Ingresar un correo", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(celular)) {
            Toast.makeText(this, "Ingresar un celular", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(direccion)) {
            Toast.makeText(this, "Ingresar una direccion", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(contrasena)) {
            Toast.makeText(this, "Ingresar contraseña", Toast.LENGTH_LONG).show();
            return;
        }
        if (nombre.equals("")||direccion.equals("")||correo.equals("") || contrasena.equals("") || celular.equals("")) {
            Toast.makeText(this, "Llene todos los espacios ", Toast.LENGTH_LONG).show();
            return;
        }
        if(correo.contains("@tracing.com")){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(correo,contrasena)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            String nombre = eNombre.getText().toString().trim();
                            String direccion = eDireccion.getText().toString().trim();
                            String celular = eCelular.getText().toString().trim();
                            String correo = eCorreo.getText().toString().trim();
                            if (task.isSuccessful()) {
                                Supervisores supervisores = new Supervisores(nombre,direccion,correo,celular);
                                userRef.child("supervisors").push().setValue(supervisores);
                                Toast.makeText(getApplicationContext(), "Usuario creado correctamente", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(AgregarSupervisorActivity.this, AdminActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage()+"", Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
        }else{
            Toast.makeText(this, "Ingresar un correo valido", Toast.LENGTH_LONG).show();
        }
    }
}
