package es.ucm.fdi.azalea.presentation.parent;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.model.EventModel;
import es.ucm.fdi.azalea.integration.Event;

public class ParentActivity extends AppCompatActivity {

    private ParentViewModel parentViewModel;
    private EventsAdapter adapter;
    private TextView resultText;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.parent_home_fragment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.parentHomeFragment), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        resultText = findViewById(R.id.parenthomefragment_textResult);

        CalendarView calendarView = findViewById(R.id.parent_calendarView);
        long today = System.currentTimeMillis();
        calendarView.setDate(today, true, true);

        RecyclerView recyclerView = findViewById(R.id.parent_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new EventsAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        parentViewModel =new ParentViewModel();
        parentViewModel.getEventsForDateLiveData().observe(this, this::updateEvents);


        TODO
        EventModel em = new EventModel("2024-11-21", "Examen", "Examen de lengua", "18:00", "2");
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("events");
        //genero el id del evento
        String id = db.push().getKey();
        if(id!=null){
            em.setId(id);

            db.child(id).setValue(em)
                    .addOnSuccessListener(task->{
                        cb.onSuccess(new Event.Success<>(em));
                    })      .addOnFailureListener(e->{
                        cb.onError(new Event.Error<>(e));
                    });
        }else{
            cb.onError(new Event.Error<>(new Exception("Error al crear el evento")));
        }

        calendarView.setOnDateChangeListener((calendar1, year, month, day) -> {
            @SuppressLint("DefaultLocale") String date = String.format("%04d-%02d-%02d", year, month + 1, day);
            parentViewModel.getEventsForDate(date);
            });
        }

        @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
        public void updateEvents(List<EventModel> events) {
            adapter.setEventsData(events);
            adapter.notifyDataSetChanged();
            if(events == null) {
                resultText.setText("@string/parent_recyclerView_empty");
            }
            else{
                String results = (events.size() +' '+ "@string/parent_recyclerView_e");
                resultText.setText(results);
        }
        }
    }
