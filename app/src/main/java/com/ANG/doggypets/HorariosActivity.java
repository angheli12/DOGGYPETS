package com.ANG.doggypets;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HorariosActivity extends AppCompatActivity implements HorarioAdapter.OnHorarioClickListener {

    private HorarioRepositorio repositorio;
    private HorarioAdapter adapter;
    private String horaSeleccionada = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios);

        repositorio = new HorarioRepositorio();
        
        RecyclerView rvHorarios = findViewById(R.id.rvHorarios);
        rvHorarios.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HorarioAdapter(this);
        rvHorarios.setAdapter(adapter);

        findViewById(R.id.btnAgregar).setOnClickListener(v -> mostrarDialogoHorario(null));
        
        findViewById(R.id.btnCerrarSesion).setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        repositorio.escucharHorarios((value, error) -> {
            if (error != null) {
                Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
                return;
            }
            if (value != null) {
                List<HorarioComida> lista = value.toObjects(HorarioComida.class);
                adapter.setHorarios(lista);
            }
        });
    }

    private void mostrarDialogoHorario(HorarioComida horarioExistente) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_horario, null);
        builder.setView(view);

        Button btnSeleccionarHora = view.findViewById(R.id.btnSeleccionarHora);
        TextView tvHoraSeleccionada = view.findViewById(R.id.tvHoraSeleccionada);
        EditText etGramos = view.findViewById(R.id.etGramos);
        
        CheckBox cbLun = view.findViewById(R.id.cbLun);
        CheckBox cbMar = view.findViewById(R.id.cbMar);
        CheckBox cbMie = view.findViewById(R.id.cbMie);
        CheckBox cbJue = view.findViewById(R.id.cbJue);
        CheckBox cbVie = view.findViewById(R.id.cbVie);
        CheckBox cbSab = view.findViewById(R.id.cbSab);
        CheckBox cbDom = view.findViewById(R.id.cbDom);

        if (horarioExistente != null) {
            horaSeleccionada = horarioExistente.getHora();
            tvHoraSeleccionada.setText("Hora: " + horaSeleccionada);
            etGramos.setText(String.valueOf(horarioExistente.getCantidad_gramos()));
            List<String> dias = horarioExistente.getDias();
            cbLun.setChecked(dias.contains("Lunes"));
            cbMar.setChecked(dias.contains("Martes"));
            cbMie.setChecked(dias.contains("Miércoles"));
            cbJue.setChecked(dias.contains("Jueves"));
            cbVie.setChecked(dias.contains("Viernes"));
            cbSab.setChecked(dias.contains("Sábado"));
            cbDom.setChecked(dias.contains("Domingo"));
        } else {
            horaSeleccionada = "";
        }

        btnSeleccionarHora.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
                horaSeleccionada = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                tvHoraSeleccionada.setText("Hora: " + horaSeleccionada);
            }, 12, 0, true);
            timePickerDialog.show();
        });

        builder.setPositiveButton("Guardar", null);
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String gramosStr = etGramos.getText().toString();
            List<String> diasSeleccionados = new ArrayList<>();
            if (cbLun.isChecked()) diasSeleccionados.add("Lunes");
            if (cbMar.isChecked()) diasSeleccionados.add("Martes");
            if (cbMie.isChecked()) diasSeleccionados.add("Miércoles");
            if (cbJue.isChecked()) diasSeleccionados.add("Jueves");
            if (cbVie.isChecked()) diasSeleccionados.add("Viernes");
            if (cbSab.isChecked()) diasSeleccionados.add("Sábado");
            if (cbDom.isChecked()) diasSeleccionados.add("Domingo");

            if (horaSeleccionada.isEmpty()) {
                Toast.makeText(this, "Selecciona una hora", Toast.LENGTH_SHORT).show();
                return;
            }

            int gramos = 0;
            try {
                gramos = Integer.parseInt(gramosStr);
            } catch (NumberFormatException e) {
                etGramos.setError("Ingresa un número");
                return;
            }

            if (gramos < 50 || gramos > 500) {
                etGramos.setError("Debe ser entre 50 y 500");
                return;
            }

            if (diasSeleccionados.isEmpty()) {
                Toast.makeText(this, "Selecciona al menos un día", Toast.LENGTH_SHORT).show();
                return;
            }

            HorarioComida nuevoHorario = new HorarioComida();
            nuevoHorario.setHora(horaSeleccionada);
            nuevoHorario.setCantidad_gramos(gramos);
            nuevoHorario.setDias(diasSeleccionados);

            if (horarioExistente == null) {
                repositorio.crear(nuevoHorario);
                Toast.makeText(this, "Horario guardado", Toast.LENGTH_SHORT).show();
            } else {
                nuevoHorario.setId_horario(horarioExistente.getId_horario());
                repositorio.actualizar(nuevoHorario);
                Toast.makeText(this, "Horario actualizado", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });
    }

    @Override
    public void onEditClick(HorarioComida horario) {
        mostrarDialogoHorario(horario);
    }

    @Override
    public void onDeleteClick(HorarioComida horario) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar")
                .setMessage("¿Estás seguro de eliminar este horario?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    repositorio.eliminar(horario.getId_horario());
                    Toast.makeText(this, "Horario eliminado", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }
}
