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
import com.example.maipetsfct.models.Cita;

import java.util.ArrayList;

public class CitasAdapter extends RecyclerView.Adapter<CitasAdapter.CitaViewHolder> {

    ArrayList<Cita> citas;
    Context context;

    private int position;

    public CitasAdapter(Context c, ArrayList<Cita> ci) {
        this.citas = ci;
        this.context = c;
    }

    public void setCitas(ArrayList<Cita> citas) {
        this.citas = citas;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Cita cita = citas.get(position);

        return new CitaViewHolder(LayoutInflater.from(context).inflate(R.layout.citas_card,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull CitasAdapter.CitaViewHolder holder, int position) {

        Cita cita = citas.get(position);

        holder.imagen.setImageResource(R.drawable.petscard);
        holder.nombreMasc.setText(citas.get(position).getNombreMascota());
        holder.nombreCita.setText(citas.get(position).getNombreCita());
        holder.fechaCita.setText(citas.get(position).getFechaCita());
        holder.horaCita.setText(citas.get(position).getHoraCita());

        holder.BindHolder(citas.get(position));
    }

    @Override
    public int getItemCount() {return citas.size();}
    public int getIndex() {return position;}


    public class CitaViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        ImageView imagen,addCal;
        TextView nombreMasc,nombreCita,horaCita,fechaCita;

        public View view;

        public CitaViewHolder (View v) {
            super(v);
            this.view = v;
            imagen = v.findViewById(R.id.imgMasc);
            nombreMasc = v.findViewById(R.id.citaPara);
            nombreCita = v.findViewById(R.id.citaEn);
            fechaCita = v.findViewById(R.id.citaFecha);
            horaCita = v.findViewById(R.id.citaHora);

            v.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {}

        public void BindHolder (Cita item){
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
