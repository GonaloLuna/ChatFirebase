package com.example.chatfirebase.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatfirebase.Adapter.UsuarioAdapter;
import com.example.chatfirebase.Modelo.Usuario;
import com.example.chatfirebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class UsuariosFragment extends Fragment {

    private RecyclerView recyclerView;
    private UsuarioAdapter usuarioAdapter;
    private List<Usuario> usuarioList;

    public UsuariosFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_usuarios, container, false);

        recyclerView = view.findViewById(R.id.recyclerview_usuarios);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        usuarioList = new ArrayList<>();

        LeerUsuarios();

        return view;
    }

    private void LeerUsuarios() {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                usuarioList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Usuario usuario = snapshot.getValue(Usuario.class);

                    assert usuario != null;

                    if(!usuario.getId().equals(firebaseUser.getUid())){

                        usuarioList.add(usuario);

                    }

                    usuarioAdapter = new UsuarioAdapter(getContext(), usuarioList);
                    recyclerView.setAdapter(usuarioAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}