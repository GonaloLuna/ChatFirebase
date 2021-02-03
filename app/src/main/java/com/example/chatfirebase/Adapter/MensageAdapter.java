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
import com.example.chatfirebase.Modelo.Chat;
import com.example.chatfirebase.Modelo.Usuario;
import com.example.chatfirebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MensageAdapter extends RecyclerView.Adapter<MensageAdapter.ViewHolder> {

    private Context context;
    private List<Chat> chatList;

    FirebaseUser firebaseUser;


    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    public MensageAdapter(Context context, List<Chat> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public MensageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == MSG_TYPE_RIGHT){

            View view = LayoutInflater.from(context).inflate(R.layout.chat_mensaje_derecho,
                    parent,false);

            return new MensageAdapter.ViewHolder(view);

        }else {

            View view = LayoutInflater.from(context).inflate(R.layout.chat_mensaje_izquierdo,
                    parent,false);

            return new MensageAdapter.ViewHolder(view);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull MensageAdapter.ViewHolder holder, int position) {

        Chat chat = chatList.get(position);

        holder.mostrar_mensaje.setText(chat.getMensaje());

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mostrar_mensaje;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mostrar_mensaje = itemView.findViewById(R.id.mensaje_chat);
        }
    }

    @Override
    public int getItemViewType(int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(chatList.get(position).getEmisor().equals(firebaseUser.getUid())){

            return  MSG_TYPE_RIGHT;

        }else {

            return MSG_TYPE_LEFT;

        }

    }
}
