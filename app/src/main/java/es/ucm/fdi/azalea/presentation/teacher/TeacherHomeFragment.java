package es.ucm.fdi.azalea.presentation.teacher;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarDay;
import com.applandeo.materialcalendarview.CalendarView;
import com.google.android.material.button.MaterialButton;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.model.EventModel;
import es.ucm.fdi.azalea.presentation.addevent.AddEventFragment;
import es.ucm.fdi.azalea.presentation.modifyevent.ModifyEventFragment;

public class TeacherHomeFragment extends Fragment implements EventsTeacherAdapter.OnEventButtonClickListener {

    private View view;
    private TeacherHomeFragmentViewModel teacherHomeFragmentViewModel;
    private EventsTeacherAdapter adapter;
    private TextView resultText;
    public CalendarView calendarView;
    private final List<CalendarDay> eventDays = new ArrayList<>(); // Lista para almacenar los días con eventos
    private CalendarDay previouslySelectedDay; // Referencia al día seleccionado previamente

    private final String TAG = "TeacherHomeFragmentActivity";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.teacher_home_fragment, container, false);

        resultText = view.findViewById(R.id.teacherhomefragment_textResult);
        MaterialButton button = view.findViewById(R.id.teacherhomefragment_buttonCreateEvent);

        calendarView = view.findViewById(R.id.teacherhomefragment_calendarView);
        Calendar today = Calendar.getInstance();
        calendarView.setSelectedDates(Collections.singletonList(today));

        RecyclerView recyclerView = view.findViewById(R.id.teacherhomefragment_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new EventsTeacherAdapter(new ArrayList<>(), getContext(), event -> {
            Log.d(TAG, "Se cambia de fragment a ModifyEventFragment");

            // Crear un Bundle con los datos del evento
            Bundle bundle = new Bundle();
            bundle.putString("id", event.getId());
            bundle.putString("title", event.getTitle());
            bundle.putString("date", event.getDate());
            bundle.putString("time", event.getTime());
            bundle.putString("location", event.getLocation());
            bundle.putString("description", event.getDescription());
            bundle.putString("idClass", event.getIdClass());
            Log.d(TAG, "Datos del evento: " + bundle);
            // Transición al ModifyEventFragment con los datos del evento
            ModifyEventFragment fragment = new ModifyEventFragment();
            fragment.setArguments(bundle);
            ((FragmentActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.teacher_fragment_container_view, fragment)
                    .addToBackStack("ModifyEventFragment")
                    .commit();
        });
        recyclerView.setAdapter(adapter);

        teacherHomeFragmentViewModel = new TeacherHomeFragmentViewModel();
        teacherHomeFragmentViewModel.getEventsForDateLiveData().observe(getViewLifecycleOwner(), this::updateEvents);

        calendarView.setOnCalendarDayClickListener(eventDay -> {
            Calendar calendar = eventDay.getCalendar();
            @SuppressLint("DefaultLocale") String date = String.format("%04d-%02d-%02d",
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH));
            teacherHomeFragmentViewModel.getEventsForDate(date);

            highlightSelectedDay(eventDay);
        });

        // Llamada para pintar de un color distinto los días que tengan algún evento
        teacherHomeFragmentViewModel.getEventsForClassroom("2"); // TODO CAMBIAR ESTO POR EL ID DE LA CLASE CORRESPONDIENTE
        teacherHomeFragmentViewModel.getEventsForClassroomLiveData().observe(getViewLifecycleOwner(), this::paintDaysWithEvents);

        // Llamada para obtener los eventos de hoy
        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(DateTimeFormatter.ISO_DATE);
        Log.i(TAG, "formattedDate: " + formattedDate);

        teacherHomeFragmentViewModel.getEventsForDate(formattedDate);

        button.setOnClickListener(v -> {
            navigateToAddEventFragment();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        teacherHomeFragmentViewModel.getEventsForDate(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
    }


    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    public void updateEvents(List<EventModel> events) {
        Log.i(TAG, "updateEvents: " + events.size());
        if (events.isEmpty()) {
            adapter.setEventsData(Collections.emptyList());
            resultText.setText(getString(R.string.parent_recyclerView_empty)); //Reutilizo los strings de parent_recyclerView
        } else {
            adapter.setEventsData(events);
            resultText.setText(getString(R.string.parent_recyclerView_result) + ": " + events.size());
        }
        adapter.notifyDataSetChanged();
    }

    public void paintDaysWithEvents(List<EventModel> events) {
        Log.i(TAG, "paintDaysWithEvents: " + events.size());
        eventDays.clear();
        for (EventModel event : events) {
            Calendar calendar = Calendar.getInstance();
            Date date = Date.valueOf(event.getDate());
            calendar.setTime(date);
            CalendarDay eventDay = new CalendarDay(calendar);
            eventDay.setImageResource(R.drawable.baseline_event_24);
            eventDays.add(eventDay);
        }
        calendarView.setCalendarDays(eventDays);
    }

    public void highlightSelectedDay(CalendarDay selectedDay) {
        if (previouslySelectedDay != null) {
            previouslySelectedDay.setBackgroundResource(0);
            if (eventDays.contains(previouslySelectedDay)) {
                previouslySelectedDay.setImageResource(R.drawable.baseline_event_24);
            }
        }

        selectedDay.setBackgroundResource(R.drawable.baseline_circle_24);
        List<CalendarDay> selectedDays = new ArrayList<>(eventDays);
        selectedDays.add(selectedDay);
        calendarView.setCalendarDays(selectedDays);

        previouslySelectedDay = selectedDay;
    }

    @Override
    public void onButtonClick(EventModel event) {
        Log.d(TAG, "Se cambia de fragment a ModifyEventFragment");
        ((FragmentActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.teacher_fragment_container_view, ModifyEventFragment.class, null)
                .addToBackStack("ModifyEventFragment") // Agregar a la pila de retroceso
                .commit();

    }

    private void navigateToAddEventFragment() {
        Log.d(TAG, "Se cambia de fragment a AddEventFragment");
        ((FragmentActivity) view.getContext()).getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.teacher_fragment_container_view, AddEventFragment.class, null)
                .addToBackStack("AddEventFragment")
                .commit();
    }
}
