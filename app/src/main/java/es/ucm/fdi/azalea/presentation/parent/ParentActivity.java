package es.ucm.fdi.azalea.presentation.parent;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.model.EventModel;

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

        Log.i(this.getClass().getName(), "entro en ParentActivity");

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

        calendarView.setOnDateChangeListener((calendar1, year, month, day) -> {
            @SuppressLint("DefaultLocale") String date = String.format("%04d-%02d-%02d", year, month + 1, day);
            parentViewModel.getEventsForDate(date);
            });
        }

        @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
        public void updateEvents(List<EventModel> events) {
        Log.i("ParentActivity", "updateEvents: " + events.size());
            if (events.isEmpty()) {
                adapter.setEventsData(Collections.emptyList());
                resultText.setText(getString(R.string.parent_recyclerView_empty));
            } else {
                adapter.setEventsData(events);
                resultText.setText(getString(R.string.parent_recyclerView_result) + ": " + events.size());
            }
            adapter.notifyDataSetChanged();
        }
    }
