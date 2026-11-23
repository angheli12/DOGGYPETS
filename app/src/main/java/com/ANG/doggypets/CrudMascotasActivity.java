package com.ANG.doggypets;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CrudMascotasActivity extends AppCompatActivity {

    private EditText edtId, edtNombre, edtPeso, edtRacion;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_mascotas);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        edtId = findViewById(R.id.edtIdMascota);
        edtNombre = findViewById(R.id.edtNombre);
        edtPeso = findViewById(R.id.edtPeso);
        edtRacion = findViewById(R.id.edtRacion);

        Button btnGuardar = findViewById(R.id.btnGuardar);
        Button btnBuscar = findViewById(R.id.btnBuscar);
        Button btnActualizar = findViewById(R.id.btnActualizar);
        Button btnEliminar = findViewById(R.id.btnEliminar);
        Button btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        btnGuardar.setOnClickListener(v -> guardar());
        btnBuscar.setOnClickListener(v -> buscar());
        btnActualizar.setOnClickListener(v -> actualizar());
        btnEliminar.setOnClickListener(v -> eliminar());
        btnCerrarSesion.setOnClickListener(v -> cerrarSesion());
    }

    private void cerrarSesion() {
        mAuth.signOut();
        Intent intent = new Intent(CrudMascotasActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void mensaje(String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }

    private String obtenerId() {
        return edtId.getText().toString().trim();
    }

    private Map<String, Object> obtenerDatos() {
        String nombre = edtNombre.getText().toString().trim();
        String pesoTexto = edtPeso.getText().toString().trim();
        String racionTexto = edtRacion.getText().toString().trim();

        if (nombre.isEmpty() || pesoTexto.isEmpty()
                || racionTexto.isEmpty()) {
            mensaje("Completa todos los campos");
            return null;
        }

        try {
            float peso = Float.parseFloat(pesoTexto);
            float racion = Float.parseFloat(racionTexto);

            if (!Float.isFinite(peso) || !Float.isFinite(racion)
                    || peso <= 0 || racion < 0) {
                mensaje("Revisa el peso y la ración");
                return null;
            }

            Map<String, Object> datos = new HashMap<>();
            datos.put("nombre", nombre);
            datos.put("peso", peso);
            datos.put("racion", racion);
            return datos;

        } catch (NumberFormatException e) {
            mensaje("Ingresa números válidos");
            return null;
        }
    }

    private void guardar() {
        String id = obtenerId();
        if (id.isEmpty()) {
            mensaje("Ingresa el ID");
            return;
        }

        Map<String, Object> datos = obtenerDatos();
        if (datos == null) return;

        db.collection("mascotas").document(id)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        mensaje("Ese ID ya existe");
                    } else {
                        db.collection("mascotas").document(id)
                                .set(datos)
                                .addOnSuccessListener(unused ->
                                        mensaje("Mascota guardada"))
                                .addOnFailureListener(e ->
                                        mensaje("Error: " + e.getMessage()));
                    }
                })
                .addOnFailureListener(e ->
                        mensaje("Error: " + e.getMessage()));
    }

    private void buscar() {
        String id = obtenerId();
        if (id.isEmpty()) {
            mensaje("Ingresa el ID");
            return;
        }

        db.collection("mascotas").document(id)
                .get()
                .addOnSuccessListener(this::mostrarMascota)
                .addOnFailureListener(e ->
                        mensaje("Error: " + e.getMessage()));
    }

    private void mostrarMascota(DocumentSnapshot doc) {
        if (!doc.exists()) {
            mensaje("Mascota no encontrada");
            return;
        }

        String nombre = doc.getString("nombre");
        Number peso = (Number) doc.get("peso");
        Number racion = (Number) doc.get("racion");

        edtNombre.setText(nombre == null ? "" : nombre);
        edtPeso.setText(peso == null ? "" : peso.toString());
        edtRacion.setText(racion == null ? "" : racion.toString());

        mensaje("Mascota encontrada");
    }

    private void actualizar() {
        String id = obtenerId();
        if (id.isEmpty()) {
            mensaje("Ingresa el ID");
            return;
        }

        Map<String, Object> datos = obtenerDatos();
        if (datos == null) return;

        db.collection("mascotas").document(id)
                .update(datos)
                .addOnSuccessListener(unused ->
                        mensaje("Mascota actualizada"))
                .addOnFailureListener(e ->
                        mensaje("No se pudo actualizar: " + e.getMessage()));
    }

    private void eliminar() {
        String id = obtenerId();
        if (id.isEmpty()) {
            mensaje("Ingresa el ID");
            return;
        }

        db.collection("mascotas").document(id)
                .delete()
                .addOnSuccessListener(unused -> {
                    edtId.setText("");
                    edtNombre.setText("");
                    edtPeso.setText("");
                    edtRacion.setText("");
                    mensaje("Operación de eliminación completada");
                })
                .addOnFailureListener(e ->
                        mensaje("Error: " + e.getMessage()));
    }
}