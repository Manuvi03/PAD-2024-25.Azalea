package es.ucm.fdi.azalea.business.Repositories.implementations;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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
            Log.d("EventRepositoryImp", "ID del evento generado: " + id);
            em.setId(id);
            Log.d("EventRepositoryImp", "Evento creado: " + em.toString());
            eventsReference.child(id).setValue(em)
                    .addOnSuccessListener(task->{
                        Log.d("EventRepositoryImp", "Evento creado correctamente");
                cb.onSuccess(new Event.Success<>(em));
            })      .addOnFailureListener(e->{
                Log.d("EventRepositoryImp", "Error al crear el evento", e);
                cb.onError(new Event.Error<>(e));
            });
        }else{
            cb.onError(new Event.Error<>(new Exception("Error al crear el evento")));
        }
    }

    public void getEventsForDate(String date, CallBack<List<EventModel>> cb ) {
        Log.i(TAG, "Entro en getEventsForDate");

        db.orderByChild("events").equalTo(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<EventModel> events = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    EventModel event = snapshot.getValue(EventModel.class);
                    if (event != null) {
                        events.add(event);
                    }
                }
                if (!events.isEmpty()) {
                    // Llama al callback con un Event.Success
                    cb.onSuccess(new Event.Success<>(events));
                } else {
                    // Si no hay eventos, devuelve un estado vacío
                    cb.onSuccess(new Event.Success<>(new ArrayList<>()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                cb.onError(new Event.Error<>(error.toException()));
            }
        });
    }
}
