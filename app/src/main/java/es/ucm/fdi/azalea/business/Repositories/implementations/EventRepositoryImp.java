package es.ucm.fdi.azalea.business.Repositories.implementations;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
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

public class EventRepositoryImp implements EventRepository {

    @Override
    public void create(EventModel em, CallBack<List<EventModel>> cb) {

    }

    public void getEventsForDate(String date, CallBack<List<EventModel>> cb ) {
       Task<List<EventModel>> task =  FirebaseDatabase.getInstance().getReference().child("events").child(date).get();


    }
}
