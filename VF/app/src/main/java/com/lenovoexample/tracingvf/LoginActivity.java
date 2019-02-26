package com.lenovoexample.tracingvf;

import android.Manifest;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lenovoexample.tracingvf.Objects.Usuarios;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private EditText eCorreo, eContraseña;
    private Button bRegistro;
    private TextView tRegistro;
    String correo = "", contraseña ="", repContraseña ="";

    private FirebaseAuth mAuth;

    private String mailAdmin = "david";
    private String passAdmin = "1234";
    ArrayList<String> correosGoogle = new ArrayList<String>();

    //FB
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseAuth.AuthStateListener getFirebaseAuthListener;

    //Login Google
    private GoogleApiClient googleApiClient;
    private SignInButton signInButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener gofirebaseAuthListener;
    public static final int SIGN_IN_CODE=777;

    private static final String TAG = "ViewDataBase";
    private FirebaseAuth mAuthAutentication;
    private FirebaseAuth.AuthStateListener mAuthListener;

    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference();

    boolean bandera = true;
    String namePerfil ="";
    String mailPerfil = "";
    String direcciones = "";
    Double lat = 0.0;
    Double lng = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        miUbicacion();
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        getFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            }
        };

        eCorreo = findViewById(R.id.eCorreo);
        eContraseña = findViewById(R.id.eContraseña);
        bRegistro = findViewById(R.id.bRegistro);
        tRegistro = findViewById(R.id.tRegistro);

        mAuth = FirebaseAuth.getInstance();
        getFirebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if(user!=null){

                    goMainScreen();
                }
            }
        };

        signInButton=(SignInButton)findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(i,SIGN_IN_CODE);
            }
        });

        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("523035990227-dcq4e23euhqr7a9n9i642i1vfuc3v574.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,  this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email","public_profile ");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
                miUbicacion();
                Usuarios usuarios = new Usuarios(namePerfil, direcciones, mailPerfil, lat, lng);
                userRef.child("users").push().setValue(usuarios);
            }

            @Override
            public void onCancel() {
                Toast.makeText(getBaseContext(), "Login Cancelado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getBaseContext(), "Login Error", Toast.LENGTH_SHORT).show();
            }
        });

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(AccessToken.getCurrentAccessToken() != null){
                    Toast.makeText(LoginActivity.this,AccessToken.getCurrentAccessToken().getExpires().toString(),Toast.LENGTH_LONG).show();
                }
                if(user != null){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    //here
                    namePerfil = user.getDisplayName();
                    mailPerfil = user.getEmail();
                    //
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        };

        tRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegistroActivity.class);
                startActivity(intent);
            }
        });
        if (getIntent().getBooleanExtra("EXIT", false)) { finish(); }
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(getBaseContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if(requestCode==SIGN_IN_CODE){
            miUbicacion();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {

        if(result.isSuccess()){
            GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuthWithGoogle(acct);
            namePerfil = acct.getDisplayName();
            mailPerfil = acct.getEmail();
            String direc = direcciones;
            if (lat != 0.0 && lng != 0.0){
                try{
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    List<Address> list = geocoder.getFromLocation(lat,lng,1);
                    if(!list.isEmpty()){
                        Address dirCalle = list.get(0);
                        direcciones = (dirCalle.getAddressLine(0)); }
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
            FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        Usuarios usuarios = dataSnapshot.getValue(Usuarios.class);
                        try{usuarios.setName(ds.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue(Usuarios.class).getName());

                            if(!mailPerfil.equals(usuarios.getName())){
                                bandera = false;
                            }
                        }catch (Exception e){

                        }
                    }
                    if(bandera) {
                        Usuarios usuarios = new Usuarios(namePerfil, direcciones, mailPerfil, lat, lng);
                        userRef.child("users").push().setValue(usuarios);
                        bandera = false;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }else{
            Toast.makeText(this,"no se puede acceder",Toast.LENGTH_SHORT).show();
        }
    }

    private static int PETICION_PERMISO_LOCACION = 101;

    private void miUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PETICION_PERMISO_LOCACION);
            return;
        }else{
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            ubicacion(location);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1200,0,locListener);
        }
    }

    private void ubicacion(Location location) {
        if(location!=null){
            lat = location.getLatitude();
            lng = location.getLongitude();
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

    private void firebaseAuthWithGoogle(GoogleSignInAccount signInAccount) {
        AuthCredential credencial = GoogleAuthProvider.getCredential(signInAccount.getIdToken(),null);
        FirebaseAuth.getInstance().signInWithCredential(credencial).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"No se logro autenticar con firebase",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goMainScreen() {
        Intent i= new Intent(LoginActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }


    public void enviardatos(View view) {
        if(eCorreo.getText().toString().equals("")||eContraseña.getText().toString().equals("")){
            Toast.makeText(this, "Ingrese los datos", Toast.LENGTH_SHORT).show();
        }else if(eCorreo.getText().toString().equals(mailAdmin) && eContraseña.getText().toString().equals(passAdmin)){
            sesionAdmin();
            Toast.makeText(this, "Administrador", Toast.LENGTH_SHORT).show();
        }else {
            verificacionFb();
        }
    }

    private void sesionAdmin() {
        Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
        startActivity(intent);
    }

    private void verificacionFb() {
        String pass = eContraseña.getText().toString();
        String mail = eCorreo.getText().toString();
        mAuth.signInWithEmailAndPassword(mail, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(), "Bienvenido Tracing", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), task.getException().getMessage()+"",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart(){
        super.onStart();

        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(firebaseAuthListener != null){
            FirebaseAuth.getInstance().removeAuthStateListener(firebaseAuthListener);
        }
    }

    @Override
    public void onBackPressed(){
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Error de conexion!", Toast.LENGTH_SHORT).show();
    }

}
