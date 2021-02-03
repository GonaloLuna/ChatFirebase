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
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends AppCompatActivity {

    MaterialEditText correo, contraseña;
    Button boton_entrar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        correo = findViewById(R.id.correoLogin);
        contraseña = findViewById(R.id.contraseñaLogin);
        boton_entrar = findViewById(R.id.botonEntrar);

        boton_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto_correo = correo.getText().toString();
                String texto_contraseña = contraseña.getText().toString();

                if(TextUtils.isEmpty(texto_correo) || TextUtils.isEmpty(texto_contraseña)){

                    Toast.makeText(LoginActivity.this, "Todos los campos son requeridos", Toast.LENGTH_SHORT).show();

                }else{

                    auth.signInWithEmailAndPassword(texto_correo, texto_contraseña)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(task.isSuccessful()){

                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();

                                    }else{

                                        Toast.makeText(LoginActivity.this, "La autentificación ha fallado", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });

                }
            }
        });
    }
}