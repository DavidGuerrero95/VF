package com.lenovoexample.tracingvf;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lenovoexample.tracingvf.Objects.Usuarios;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class RegistroActivity extends AppCompatActivity implements View.OnClickListener{

    private static int PETICION_PERMISO_LOCACION = 101;

    private EditText eCorreo, eContrasena, eRepContrasena, eNombre;
    private Button bButon;

    String direcciones = "";
    Double lat = 0.0;
    Double lng = 0.0;

    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        eNombre = findViewById(R.id.eNombre);
        eCorreo = findViewById(R.id.eCorreo);
        eContrasena = findViewById(R.id.eContraseña);
        eRepContrasena = findViewById(R.id.eRepContraseña);
        bButon = findViewById(R.id.bButon);
        bButon.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        miUbicacion();
        registrarUsuario();
    }

    private void registrarUsuario() {
        String nombre = eNombre.getText().toString().trim();
        String correo = eCorreo.getText().toString().trim();
        String contrasena = eContrasena.getText().toString().trim();
        String repContrasena = eRepContrasena.getText().toString().trim();
        if (TextUtils.isEmpty(nombre)) {
            Toast.makeText(this, "Ingresar un nombre", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(correo)) {
            Toast.makeText(this, "Ingresar un email", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(contrasena)) {
            Toast.makeText(this, "Ingresar una contraseña", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(repContrasena)) {
            Toast.makeText(this, "Ingresar contraseña de verificacion ", Toast.LENGTH_LONG).show();
            return;
        }
        if (nombre.equals("") || correo.equals("") || contrasena.equals("") || repContrasena.equals("")) {
            Toast.makeText(getApplicationContext(), "Llene todos los espacios ", Toast.LENGTH_LONG).show();
            return;
        }
        if (contrasena.equals(repContrasena)) {
            progressDialog.setMessage("Registrando");
            progressDialog.show();
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(correo, contrasena)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            String nombre = eNombre.getText().toString().trim();
                            String correo = eCorreo.getText().toString().trim();
                            String direccions = direcciones;
                            Double latitud = lat;
                            Double longitud = lng;
                            if (task.isSuccessful()) {
                                Usuarios usuarios = new Usuarios(nombre, direccions, correo, latitud, longitud);
                                userRef.child("users").push().setValue(usuarios);
                                Toast.makeText(getApplicationContext(), "Usuario creado correctamente", Toast.LENGTH_LONG).show();
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                updateUI(user);
                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage() + "", Toast.LENGTH_LONG).show();
                                updateUI(null);
                            }
                            progressDialog.dismiss();
                        }
                    });
            return;
        } else {
            Toast.makeText(getApplicationContext(), "Contraseñas diferentes ", Toast.LENGTH_LONG).show();
            return;
        }
    }

    private void updateUI(FirebaseUser user) {
        if(user!=null){
            Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void miUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PETICION_PERMISO_LOCACION);
            return;
        }else{
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            ubicacion(location);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1200,0,locListener);

            //onBackPressed();
        }
    }

    private void ubicacion(Location location) {
        if(location!=null){
            lat = location.getLatitude();
            lng = location.getLongitude();
        }
    }

    LocationListener locListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            ubicacion(location);
            setLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            locationStart();
        }
    };

    private void locationStart(){
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!gpsEnabled){
            Intent settingIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingIntent);
        }
    }

    public void setLocation(Location location){
        if (location.getLatitude() != 0.0 && location.getLongitude() != 0.0){
            try{
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                if(!list.isEmpty()){
                    Address dirCalle = list.get(0);
                    direcciones = (dirCalle.getAddressLine(0));
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

}