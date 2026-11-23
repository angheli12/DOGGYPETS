package com.ANG.doggypets;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class HorarioRepositorio {
    private final FirebaseFirestore db;
    private final CollectionReference collectionReference;

    public HorarioRepositorio() {
        db = FirebaseFirestore.getInstance();
        
        // Habilitar persistencia offline
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        String uid = "usuario_prueba";
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        
        collectionReference = db.collection("usuarios").document(uid).collection("horarios");
    }

    public void crear(HorarioComida horario) {
        String id = collectionReference.document().getId();
        horario.setId_horario(id);
        collectionReference.document(id).set(horario);
    }

    public void escucharHorarios(EventListener<QuerySnapshot> listener) {
        collectionReference.orderBy("hora").addSnapshotListener(listener);
    }

    public void actualizar(HorarioComida horario) {
        if (horario.getId_horario() != null) {
            collectionReference.document(horario.getId_horario()).set(horario);
        }
    }

    public void eliminar(String idHorario) {
        collectionReference.document(idHorario).delete();
    }
}
