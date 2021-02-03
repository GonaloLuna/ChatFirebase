package com.example.chatfirebase.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatfirebase.MessageActivity;
import com.example.chatfirebase.Modelo.Usuario;
import com.example.chatfirebase.R;

import java.util.List;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.ViewHolder> {

    private Context context;
    private List<Usuario> usuariosList;

    public UsuarioAdapter(Context context, List<Usuario> usuariosList) {
        this.context = context;
        this.usuariosList = usuariosList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.mostrar_usuario,
        parent,false);

        return new UsuarioAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Usuario usuarios = usuariosList.get(position);

        holder.usuario.setText(usuarios.getUsuario());

        if (usuarios.getImagenURL().equals("default")){

            holder.imageView.setImageResource(R.mipmap.ic_launcher);

        }else{

            Glide.with(context)
                    .load(usuarios.getImagenURL())
                    .into(holder.imageView);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("usuarioId", usuarios.getId());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return usuariosList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView usuario;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            usuario = itemView.findViewById(R.id.mostraNombreUsuario);
            imageView = itemView.findViewById(R.id.mostrarImagenPerfil);
        }
    }
}
