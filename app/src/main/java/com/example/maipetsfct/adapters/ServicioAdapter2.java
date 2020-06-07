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
import com.example.maipetsfct.models.servicio;

import java.util.ArrayList;

public class ServicioAdapter2 extends RecyclerView.Adapter<ServicioAdapter2.ServicioViewHolder>{

    ArrayList<servicio> servicios;
    Context context;

    private int position;

    public ServicioAdapter2(Context c,ArrayList<servicio> s) {
        this.context = c;
        this.servicios = s;
    }

    public void setServicios(ArrayList<servicio> lista) {
        this.servicios = lista;
        this.notifyDataSetChanged();
    }

    @Override
    public ServicioAdapter2.ServicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ServicioAdapter2.ServicioViewHolder(LayoutInflater.from(context).inflate(R.layout.sgedb_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ServicioAdapter2.ServicioViewHolder holder, int position) {
        servicio serv = servicios.get(position);

        holder.nombre.setText(servicios.get(position).getNombre());

        holder.BindHolder(servicios.get(position));
    }

    @Override
    public int getItemCount() {
        return servicios.size();
    }
    public int getIndex(){
        return position;
    }

    public class ServicioViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        TextView nombre;

        public View view;

        public ServicioViewHolder(View v){

            super(v);
            this.view = v;
            nombre = v.findViewById(R.id.nombreUsuario);
            v.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {}

        public void BindHolder (servicio item){
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
