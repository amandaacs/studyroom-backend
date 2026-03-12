package com.project.studyroom.repository;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.project.studyroom.model.Reservation;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ReservationRepository {

    private final Firestore firestore;

    public ReservationRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    //cria reserva
    public String save(Reservation reservation) throws Exception{
        ApiFuture<DocumentReference> future = firestore.collection("reservations").add(reservation);
        return future.get().getId();
    }

    //lista todas as reservas
    public List<Reservation> findAll() throws Exception{
        ApiFuture<QuerySnapshot> future = firestore.collection("reservations").get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        return documents.stream()
                .map(doc -> {
                    Reservation r = doc.toObject(Reservation.class);
                    return r;
                })
                .collect(Collectors.toList());
    }

    //lista reservas por user
    public List<Reservation> findByUserId(String userId) throws Exception{
        ApiFuture<QuerySnapshot> future = firestore.collection("reservations")
                .whereEqualTo("userId", userId)
                .get();

        return future.get().getDocuments().stream()
                .map(doc -> doc.toObject(Reservation.class))
                .collect(Collectors.toList());
    }

    //lista reservas por sala
    public List<Reservation> findByRoom(String roomId) throws Exception{
        ApiFuture<QuerySnapshot> future = firestore.collection("reservations")
                .whereEqualTo("roomId", roomId)
                .get();

        return future.get().getDocuments().stream()
                .map(doc -> doc.toObject(Reservation.class))
                .collect(Collectors.toList());
    }

    //lista reservas com sobreposição
    public List<Reservation> findOverlapping(String roomId, Date start, Date end) throws Exception{
        return firestore.collection("reservations")
                .whereEqualTo("roomId", roomId)
                .whereEqualTo("status", "CONFIRMED")
                .whereLessThan("startTime", end)
                .whereGreaterThan("endTime", start)
                .get().get().getDocuments().stream()
                .map(doc -> doc.toObject(Reservation.class))
                .collect(Collectors.toList());
    }

    //atualiza status
    public void updateStatus(String reservationId, String newStatus) throws Exception{
        firestore.collection("reservations").document(reservationId)
                .update("status", newStatus).get();
    }

    //encontrar reserva pelo id
    public Reservation findById(String reservationId) throws Exception{
        DocumentSnapshot doc = firestore.collection("reservations").document(reservationId).get().get();
        return doc.exists() ? doc.toObject(Reservation.class) : null;
    }

    public void updateTime(String reservationId, Date newStart, Date newEnd) throws Exception{
        firestore.collection("reservations").document(reservationId)
                .update("startTime", newStart,  "endTime", newEnd).get();
    }

    public List<Reservation> findByTimeFrame(Date start, Date end) throws Exception{
        return firestore.collection("reservations")
                .whereGreaterThanOrEqualTo("startTime", start)
                .whereLessThanOrEqualTo("endTime", end)
                .get().get().getDocuments().stream()
                .map(doc -> doc.toObject(Reservation.class))
                .collect(Collectors.toList());
    }
}
