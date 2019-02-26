package com.lenovoexample.tracingvf;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity {

    private android.support.v4.app.FragmentManager fm;
    private FragmentTransaction ft;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            fm = getSupportFragmentManager();
            ft = fm.beginTransaction();

            switch (item.getItemId()) {
                case R.id.mEventos:

                    return true;
                case R.id.mClientes:
                    UsersFragment usersFragment = new UsersFragment();
                    ft.replace(R.id.frames, usersFragment).commit();
                    return true;
                case R.id.mSupervisores:
                    SupervisorFragment supervisorFragment = new SupervisorFragment();
                    ft.replace(R.id.frames,supervisorFragment).commit();
                    return true;
                case R.id.mMapa:

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigations);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        UsersFragment usersFragment = new UsersFragment();
        ft.replace(R.id.frames, usersFragment).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //coloca menu
        getMenuInflater().inflate(R.menu.admindosmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        switch (id){
            case R.id.mAgregar:
                intent = new Intent(AdminActivity.this, AgregarSupervisorActivity.class);
                startActivity(intent);
                break;
            case R.id.mBorrar:

                break;
            case R.id.mSesion:
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                intent = new Intent(AdminActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){finish();}

}
