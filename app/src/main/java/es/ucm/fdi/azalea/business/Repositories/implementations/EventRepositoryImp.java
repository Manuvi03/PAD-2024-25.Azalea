package es.ucm.fdi.azalea.business.Repositories.implementations;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.ucm.fdi.azalea.business.Repositories.EventRepository;
import es.ucm.fdi.azalea.business.model.EventModel;
import es.ucm.fdi.azalea.integration.CallBack;
import es.ucm.fdi.azalea.integration.Event;

public class EventRepositoryImp implements EventRepository {

    private final String TAG = "EventRepositoryImp";
    private final FirebaseDatabase db = FirebaseDatabase.getInstance("https://azalea-fde19-default-rtdb.europe-west1.firebasedatabase.app/");
    private final DatabaseReference eventsReference = db.getReference("events");

    @Override
    public void create(EventModel em, CallBack<EventModel> cb) {
        Log.i(TAG, "Entro en create");
        //genero el id del evento
        String id = eventsReference.push().getKey();
        if(id!=null){
            Log.d(TAG, "ID del evento generado: " + id);
            em.setId(id);
            Log.d(TAG, "Evento creado: " + em);
            eventsReference.child(id).setValue(em)
                    .addOnSuccessListener(task->{
                        Log.d("EventRepositoryImp", "Evento creado correctamente");
                cb.onSuccess(new Event.Success<>(em));
            })      .addOnFailureListener(e->{
                Log.d(TAG, "Error al crear el evento", e);
                cb.onError(new Event.Error<>(e));
            });
        }else{
            cb.onError(new Event.Error<>(new Exception("Error al crear el evento")));
        }
    }

    public void getEventsForDate(String date, String classroomId, CallBack<List<EventModel>> cb) {
        Log.i(TAG, "Entrando en getEventsForDateAndClassroom. Date: " + date + ", ClassroomId: " + classroomId);
        List<EventModel> filteredEvents = new ArrayList<>();

        // Primera consulta: Filtrar por classroomId
        Query getEventsByClassroom = eventsReference.orderByChild("idClass").equalTo(classroomId);
        getEventsByClassroom.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    List<EventModel> classroomEvents = new ArrayList<>();

                    // Procesar resultados de la primera consulta
                    for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                        if (eventSnapshot.exists()) {
                            EventModel event = eventSnapshot.getValue(EventModel.class);
                            if (event != null) {
                                classroomEvents.add(event);
                            }
                        }
                    }

                    Log.d(TAG, "Eventos filtrados por classroomId: " + classroomEvents.size());

                    // Segunda consulta: Filtrar los eventos obtenidos por la fecha
                    for (EventModel event : classroomEvents) {
                        if (date.equals(event.getDate())) {
                            filteredEvents.add(event);
                        }
                    }

                    Log.d(TAG, "Eventos filtrados por fecha: " + filteredEvents.size());
                    cb.onSuccess(new Event.Success<>(filteredEvents));
                }else{
                    cb.onError(new Event.Error<>());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Error al obtener eventos por classroomId", error.toException());
                cb.onError(new Event.Error<>(error.toException()));
            }
        });
        /*getEventsByClassroom.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful() || task.getResult() == null) {
                Log.e(TAG, "Error al obtener eventos por classroomId", task.getException());
                cb.onError(new Event.Error<>(task.getException()));
                return;
            }

            List<EventModel> classroomEvents = new ArrayList<>();

            // Procesar resultados de la primera consulta
            for (DataSnapshot eventSnapshot : task.getResult().getChildren()) {
                if (eventSnapshot.exists()) {
                    EventModel event = eventSnapshot.getValue(EventModel.class);
                    if (event != null) {
                        classroomEvents.add(event);
                    }
                }
            }

            Log.d(TAG, "Eventos filtrados por classroomId: " + classroomEvents.size());

            // Segunda consulta: Filtrar los eventos obtenidos por la fecha
            for (EventModel event : classroomEvents) {
                if (date.equals(event.getDate())) {
                    filteredEvents.add(event);
                }
            }

            Log.d(TAG, "Eventos filtrados por fecha: " + filteredEvents.size());
            cb.onSuccess(new Event.Success<>(filteredEvents));
        });*/
    }


    @Override
    public void getEventsForClassroom(String idclassroom, CallBack<List<EventModel>> cb) {
        Log.i(TAG + "getEventsForClassroom", "Entrando en getEventsForClassroom. ClassroomId: " + idclassroom);
        List<EventModel> events = new ArrayList<>();
        Query getEventsForClassroom = eventsReference.orderByChild("idClass").equalTo(idclassroom);
        getEventsForClassroom.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG + "getEventsForClassroom", "Eventos obtenidos correctamente. Devuelvo: " + snapshot.getChildrenCount() + " resultados.");
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    Log.d(TAG + "getEventsForClassroom", "Evento obtenido: " + eventSnapshot.getValue());
                    if (eventSnapshot.exists()) {
                        EventModel event = eventSnapshot.getValue(EventModel.class);
                        if (event != null) {
                            events.add(event);
                        }
                    }
                }

                Log.d(TAG + "getEventsForClassroom", "Eventos obtenidos correctamente. Devuelvo: " + events.size() + " resultados.");
                cb.onSuccess(new Event.Success<>(events));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG + "getEventsForClassroom", "Error al obtener los eventos",error.toException());
                cb.onError(new Event.Error<>(error.toException()));
            }
        });

        /*getEventsForClassroom.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful() || task.getResult() == null) {
                Log.e(TAG + "getEventsForClassroom", "Error al obtener los eventos", task.getException());
                cb.onError(new Event.Error<>(task.getException()));
                return;
            }
            Log.d(TAG + "getEventsForClassroom", "Eventos obtenidos correctamente. Devuelvo: " + task.getResult().getChildrenCount() + " resultados.");
            for (DataSnapshot eventSnapshot : task.getResult().getChildren()) {
                Log.d(TAG + "getEventsForClassroom", "Evento obtenido: " + eventSnapshot.getValue());
                if (eventSnapshot.exists()) {
                    EventModel event = eventSnapshot.getValue(EventModel.class);
                    if (event != null) {
                        events.add(event);
                    }
                }
            }

            Log.d(TAG + "getEventsForClassroom", "Eventos obtenidos correctamente. Devuelvo: " + events.size() + " resultados.");
            cb.onSuccess(new Event.Success<>(events));
        });*/
    }

    @Override
    public void modify(EventModel em, CallBack<EventModel> callBack) {
        Log.i(TAG, "Entro en modify, modify: " + em.getId());
        Query query = eventsReference.orderByChild("id").equalTo(em.getId());
        Log.i(TAG, "Query ejecutada con ID: " + em.getId());
        query.get().addOnSuccessListener(task -> {
            if (task.exists()) {
                Log.i(TAG, "Resultados encontrados: " + task.getChildrenCount());
                for (DataSnapshot snapshot : task.getChildren()) {
                    snapshot.getRef().updateChildren(new HashMap<String, Object>() {{
                        put("title", em.getTitle());
                        put("description", em.getDescription());
                        put("date", em.getDate());
                        put("time", em.getTime());
                        put("location", em.getLocation());
                    }}).addOnSuccessListener(task1 -> {
                        Log.d(TAG, "Evento modificado correctamente");
                        callBack.onSuccess(new Event.Success<>(em));
                    }).addOnFailureListener(e -> {
                        Log.e(TAG, "Error al modificar el evento", e);
                        callBack.onError(new Event.Error<>(e));
                    });
                }
            }
            else{
                Log.e(TAG, "Error al modificar el evento");
                callBack.onError(new Event.Error<>(new Exception("Error al modificar el evento")));
            }

        }).addOnFailureListener(e -> {
            Log.e(TAG, "Error al modificar el evento", e);
            callBack.onError(new Event.Error<>(e));
        });

    }
}
