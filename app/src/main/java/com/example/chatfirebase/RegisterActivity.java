package com.example.chatfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    MaterialEditText usuario, correo, contraseña;
    Button boton_registrar;

    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usuario = findViewById(R.id.usuario);
        correo = findViewById(R.id.correo);
        contraseña = findViewById(R.id.contraseña);

        boton_registrar = findViewById(R.id.botonRegistrar);

        auth = FirebaseAuth.getInstance();

        boton_registrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                String texto_usuario = usuario.getText().toString();
                String texto_correo = correo.getText().toString();
                String texto_contraseña = contraseña.getText().toString();
                
                if(TextUtils.isEmpty(texto_usuario) || TextUtils.isEmpty(texto_correo) || TextUtils.isEmpty(texto_contraseña)){

                    Toast.makeText(RegisterActivity.this, "Todos los campos son requeridos", Toast.LENGTH_SHORT).show();
                    
                }else if(texto_contraseña.length() < 6){

                    Toast.makeText(RegisterActivity.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    
                }else{

                    register(texto_usuario, texto_correo, texto_contraseña);

                }

            }
        });
    }

    private void register(String usuario, String correo, String contraseña){

        auth.createUserWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Usuarios").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("usuario", usuario);
                            hashMap.put("imagenURL", "default");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){

                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();

                                    }

                                }
                            });

                        }else{

                            Toast.makeText(RegisterActivity.this, "No te puedes registrar sin correo o contraseña", Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }

}