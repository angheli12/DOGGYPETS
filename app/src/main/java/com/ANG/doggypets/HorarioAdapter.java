package com.ANG.doggypets;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HorarioAdapter extends RecyclerView.Adapter<HorarioAdapter.ViewHolder> {

    private List<HorarioComida> horarios = new ArrayList<>();
    private final OnHorarioClickListener listener;

    public interface OnHorarioClickListener {
        void onEditClick(HorarioComida horario);
        void onDeleteClick(HorarioComida horario);
    }

    public HorarioAdapter(OnHorarioClickListener listener) {
        this.listener = listener;
    }

    public void setHorarios(List<HorarioComida> horarios) {
        this.horarios = horarios;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horario, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HorarioComida horario = horarios.get(position);
        holder.tvHora.setText(horario.getHora());
        holder.tvGramos.setText(horario.getCantidad_gramos() + "g");
        holder.tvDias.setText(String.join(", ", horario.getDias()));

        holder.btnEditar.setOnClickListener(v -> listener.onEditClick(horario));
        holder.btnBorrar.setOnClickListener(v -> listener.onDeleteClick(horario));
    }

    @Override
    public int getItemCount() {
        return horarios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHora, tvGramos, tvDias;
        Button btnEditar, btnBorrar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHora = itemView.findViewById(R.id.tvHora);
            tvGramos = itemView.findViewById(R.id.tvGramos);
            tvDias = itemView.findViewById(R.id.tvDias);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnBorrar = itemView.findViewById(R.id.btnBorrar);
        }
    }
}
