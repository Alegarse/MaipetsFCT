package com.maipetsfct.adapters;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maipetsfct.R;
import com.maipetsfct.models.Usuario;

import java.util.ArrayList;

public class ActividadAdapter
        extends RecyclerView.Adapter<ActividadAdapter.UsuarioViewHolder>
        implements View.OnClickListener {

    ArrayList<Usuario> usuarios;
    Context context;

    private View.OnClickListener listener;

    private int position;

    public ActividadAdapter(Context c,ArrayList<Usuario> u) {
        this.context = c;
        this.usuarios = u;
    }

    public void setUsuarios(ArrayList<Usuario> lista) {
        this.usuarios = lista;
        this.notifyDataSetChanged();
    }

    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.servdates_card,parent,false);
        view.setOnClickListener(this);
        return new UsuarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        Usuario usu = usuarios.get(position);
        holder.nombre.setText(usuarios.get(position).getActividad());
        holder.BindHolder(usuarios.get(position));
        holder.view.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }
    public int getIndex(){
        return position;
    }

    public void setOnClickListener (View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public class UsuarioViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        TextView nombre;
        public View view;

        public UsuarioViewHolder(View v){

            super(v);
            this.view = v;
            nombre = v.findViewById(R.id.nombreActiv);
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