package com.example.maipetsfct.adapters;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maipetsfct.R;
import com.example.maipetsfct.models.Usuario;

import java.util.ArrayList;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {

    ArrayList<Usuario> usuarios;
    Context context;

    private int position;

    public UsuarioAdapter(Context c,ArrayList<Usuario> u) {
        this.context = c;
        this.usuarios = u;
    }

    public void setUsuarios(ArrayList<Usuario> lista) {
        this.usuarios = lista;
        this.notifyDataSetChanged();
    }

    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UsuarioViewHolder(LayoutInflater.from(context).inflate(R.layout.services_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        Usuario usu = usuarios.get(position);


        if (usu.getServCode().equals("1")){
            holder.imagen.setImageResource(R.drawable.clinica);
        } else if (usu.getServCode().equals("2")){
            holder.imagen.setImageResource(R.drawable.peluqueria);
        } else if (usu.getServCode().equals("3")){
            holder.imagen.setImageResource(R.drawable.comida);
        } else if (usu.getServCode().equals("4")){
            holder.imagen.setImageResource(R.drawable.guarde);
        }  else if (usu.getServCode().equals("5")){
            holder.imagen.setImageResource(R.drawable.protectora);
        }

        holder.nombre.setText(usuarios.get(position).getActividad());

        holder.BindHolder(usuarios.get(position));
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }
    public int getIndex(){
        return position;
    }

    public class UsuarioViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        ImageView imagen;
        TextView nombre;

        public View view;

        public UsuarioViewHolder(View v){

            super(v);
            this.view = v;
            imagen = v.findViewById(R.id.aniImg);
            nombre = v.findViewById(R.id.nombre);
            v.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {}

        public void BindHolder (Usuario item){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    position = getAdapterPosition();
                    return false;
                }
            });
        }

    }
}