package es.ucm.fdi.azalea.presentation.parent;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarDay;
import com.applandeo.materialcalendarview.CalendarView;
import com.jakewharton.threetenabp.AndroidThreeTen;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.model.EventModel;

public class ParentHomeFragmentActivity extends AppCompatActivity {
    private ParentHomeFragmentViewModel parentHomeFragmentViewModel;
    private EventsAdapter adapter;
    private TextView resultText;
    public CalendarView calendarView;
    private final List<CalendarDay> eventDays = new ArrayList<>(); // Lista para almacenar los días con eventos
    private CalendarDay previouslySelectedDay; // Referencia al día seleccionado previamente

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
        AndroidThreeTen.init(this); //inicializacion de la libreria de fechas

        Log.i(this.getClass().getName(), "entro en ParentActivity");

        resultText = findViewById(R.id.parenthomefragment_textResult);

        calendarView = findViewById(R.id.parent_calendarView);
        Calendar today = Calendar.getInstance();
        calendarView.setSelectedDates(Collections.singletonList(today));

        RecyclerView recyclerView = findViewById(R.id.parent_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new EventsAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);

        parentHomeFragmentViewModel = new ParentHomeFragmentViewModel();
        parentHomeFragmentViewModel.getEventsForDateLiveData().observe(this, this::updateEvents);

        calendarView.setOnCalendarDayClickListener(eventDay -> {
            Calendar calendar = eventDay.getCalendar();
            @SuppressLint("DefaultLocale") String date = String.format("%04d-%02d-%02d",
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH));
            parentHomeFragmentViewModel.getEventsForDate(date);

            highlightSelectedDay(eventDay);
        });

        // Llamada para pintar de un color distinto los días que tengan algún evento
        parentHomeFragmentViewModel.getEventsForClassroom("2"); // TODO CAMBIAR ESTO POR EL ID DE LA CLASE CORRESPONDIENTE
        parentHomeFragmentViewModel.getEventsForClassroomLiveData().observe(this, this::paintDaysWithEvents);

        // Llamada para obtener los eventos de hoy
        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(DateTimeFormatter.ISO_DATE);
        Log.i("ParentActivity", "formattedDate: " + formattedDate);

        parentHomeFragmentViewModel.getEventsForDate(formattedDate);

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

    public void paintDaysWithEvents(List<EventModel> events) {
        Log.i("ParentActivity", "paintDaysWithEvents: " + events.size());
        eventDays.clear(); // Limpiar la lista de días con eventos antes de añadir los nuevos
        for (EventModel event : events) {
            Calendar calendar = Calendar.getInstance();
            Date date = Date.valueOf(event.getDate());
            calendar.setTime(date);
            CalendarDay eventDay = new CalendarDay(calendar);
            eventDay.setImageResource(R.drawable.baseline_event_24);
            eventDays.add(eventDay); // Añadir el eventDay configurado
        }
        calendarView.setCalendarDays(eventDays);
    }

    public void highlightSelectedDay(CalendarDay selectedDay) {
        if (previouslySelectedDay != null) {
            // Restaurar el fondo del día previamente seleccionado
            previouslySelectedDay.setBackgroundResource(0); // Quitar el fondo
            if (eventDays.contains(previouslySelectedDay)) {
                previouslySelectedDay.setImageResource(R.drawable.baseline_event_24); // Restaurar la imagen del evento si es un día con evento
            }
        }

        selectedDay.setBackgroundResource(R.drawable.baseline_circle_24); // Asegúrate de tener un drawable llamado circle_background
        List<CalendarDay> selectedDays = new ArrayList<>(eventDays); // Copiar los días con eventos
        selectedDays.add(selectedDay); // Añadir el día seleccionado
        calendarView.setCalendarDays(selectedDays);

        previouslySelectedDay = selectedDay; // Actualizar la referencia al día seleccionado previamente
    }
}
