package com.lenovoexample.tracingvf;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lenovoexample.tracingvf.Objects.AdapterUsers;
import com.lenovoexample.tracingvf.Objects.Usuarios;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends Fragment {

    RecyclerView rv;
    List<Usuarios> usuarios;
    AdapterUsers adapter;

    public UsersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        rv = view.findViewById(R.id.recycler);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        usuarios = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        adapter = new AdapterUsers(usuarios);
        rv.setAdapter(adapter);
        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuarios.removeAll(usuarios);
                for (DataSnapshot snapshot:
                     dataSnapshot.getChildren()) {
                    Usuarios usuario = snapshot.getValue(Usuarios.class);
                    usuarios.add(usuario);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return view;
    }
}
