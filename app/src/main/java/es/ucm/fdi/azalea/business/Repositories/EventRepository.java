package es.ucm.fdi.azalea.business.Repositories;

import android.util.Log;

import com.google.android.datatransport.Event;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.List;

import es.ucm.fdi.azalea.business.model.EventModel;

public class EventRepository {
    // referencia a un lugar de la BD
    private final DatabaseReference eventReference;
    public EventRepository(){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://azalea-fde19-default-rtdb.europe-west1.firebasedatabase.app/");
        eventReference = database.getReference("events");
    }

    public void create(EventModel event, Event<EventModel> callback) {
        Event<EventModel> cb = new Event.Success<>(event);
    }
}
