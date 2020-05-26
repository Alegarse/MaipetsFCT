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
import com.example.maipetsfct.models.mascota;

import java.util.ArrayList;


public class MascotaAdapter extends RecyclerView.Adapter<MascotaAdapter.MascotaViewHolder> {

    ArrayList<mascota> mascotas;
    Context context;

    private int position;

    public MascotaAdapter(Context c, ArrayList<mascota> m) {
        context = c;
        mascotas = m;
    }

    public void setMascotas(ArrayList<mascota> lista)
    {
        this.mascotas = lista ;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MascotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MascotaViewHolder(LayoutInflater.from(context).inflate(R.layout.mascota_card,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MascotaViewHolder holder, int position) {
        mascota masc = mascotas.get(position);
        holder.imagen.setImageResource(R.drawable.mascotas);
        holder.nombre.setText(mascotas.get(position).getNombre());

        holder.BindHolder(mascotas.get(position));
    }

    @Override
    public int getItemCount() {
        return mascotas.size();
    }
    public int getIndex(){
        return position;
    }

    public class MascotaViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        ImageView imagen;
        TextView nombre;

        public View view;

        public MascotaViewHolder(View v){

            super(v);
            this.view = v;
            imagen = v.findViewById(R.id.aniImg);
            nombre = v.findViewById(R.id.nombre);
            v.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {}

            public void BindHolder (mascota item){
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
