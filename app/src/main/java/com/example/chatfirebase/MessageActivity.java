package com.example.chatfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chatfirebase.Adapter.MensageAdapter;
import com.example.chatfirebase.Modelo.Chat;
import com.example.chatfirebase.Modelo.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    TextView usuarioMensaje;
    ImageView imagenView;

    RecyclerView recyclerView;
    EditText mensaje;
    ImageButton enviar;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    Intent intent;

    MensageAdapter mensageAdapter;
    List<Chat> chatList;

    String usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        imagenView = findViewById(R.id.mensaje_imagen);
        usuarioMensaje = findViewById(R.id.mensaje_nombre);

        enviar = findViewById(R.id.botonEnviar);
        mensaje = findViewById(R.id.texto_enviar);

        recyclerView = findViewById(R.id.mensaje_recycler);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        intent = getIntent();
        usuarioId = intent.getStringExtra("usuarioId");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Usuarios").child(usuarioId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                usuarioMensaje.setText(usuario.getUsuario());

                if(usuario.getImagenURL().equals("default")){

                    imagenView.setImageResource(R.mipmap.ic_launcher);

                }else{

                    Glide.with(MessageActivity.this)
                            .load(usuario.getImagenURL())
                            .into(imagenView);

                }

                leerMensaje(firebaseUser.getUid(), usuarioId);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        enviar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                String mensa = mensaje.getText().toString();

                if(!mensa.equals("")){

                    enviarMensaje(firebaseUser.getUid(), usuarioId, mensa);

                }else{

                    Toast.makeText(MessageActivity.this, "Escribe algo", Toast.LENGTH_SHORT).show();
                    
                }

                mensaje.setText("");
            }
        });
    }

    private void enviarMensaje(String emisor, String receptor, String mensaje){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("emisor", emisor);
        hashMap.put("receptor", receptor);
        hashMap.put("mensaje", mensaje);

        reference.child("Chats").push().setValue(hashMap);

        final DatabaseReference chatReference = FirebaseDatabase.getInstance()
                .getReference("ChatList")
        .child(firebaseUser.getUid())
        .child(usuarioId);

        chatReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!dataSnapshot.exists()){

                    chatReference.child("id").setValue(usuarioId);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void leerMensaje(String id, String usuarioId){

        chatList = new ArrayList<>();

        reference= FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                chatList.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Chat chat = snapshot.getValue(Chat.class);

                    if(chat.getReceptor().equals(id) && chat.getEmisor().equals(usuarioId) || chat.getReceptor().equals(usuarioId) && chat.getEmisor().equals(id)){

                        chatList.add(chat);


                    }

                    mensageAdapter = new MensageAdapter(MessageActivity.this, chatList);
                    recyclerView.setAdapter(mensageAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}