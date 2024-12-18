package es.ucm.fdi.azalea.presentation.parent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarDay;
import com.applandeo.materialcalendarview.CalendarView;
import com.google.android.material.button.MaterialButton;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.model.EventModel;
import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.integration.Event;
import es.ucm.fdi.azalea.presentation.chat.chatActivity;

public class ParentHomeFragmentActivity extends Fragment  {

    private UserModel parentInfo;
    private ParentHomeFragmentViewModel parentHomeFragmentViewModel;
    private EventsParentAdapter adapter;
    private TextView resultText;
    public CalendarView calendarView;
    private final List<CalendarDay> eventDays = new ArrayList<>(); // Lista para almacenar los días con eventos
    private CalendarDay previouslySelectedDay; // Referencia al día seleccionado previamente

    private final String TAG = "ParentHomeFragmentActivity";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.parent_home_fragment, container, false);

        Log.i(TAG, "entro en ParentHomeFragmentActivity");

        resultText = view.findViewById(R.id.parenthomefragment_textResult);

        calendarView = view.findViewById(R.id.parenthomefragment_calendarView);
        Calendar today = Calendar.getInstance();
        calendarView.setSelectedDates(Collections.singletonList(today));

        RecyclerView recyclerView = view.findViewById(R.id.parenthomefragment_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new EventsParentAdapter(new ArrayList<>(), getContext());
        recyclerView.setAdapter(adapter);

        parentHomeFragmentViewModel = new ParentHomeFragmentViewModel();
        parentHomeFragmentViewModel.getEventsForDate(getCurrentDate());
        parentHomeFragmentViewModel.getEventsForDateLiveData().observe(getViewLifecycleOwner(), events -> {
            if (events instanceof Event.Success) {
                updateEvents(((Event.Success<List<EventModel>>) events).getData());
            }
        });

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
        parentHomeFragmentViewModel.getEventsForClassroom();
        parentHomeFragmentViewModel.getEventsForClassroomLiveData().observe(getViewLifecycleOwner(), events -> {
            if (events instanceof Event.Success) {
                paintDaysWithEvents(((Event.Success<List<EventModel>>) events).getData());
            }
        });

        // Llamada para obtener los eventos de hoy
        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(DateTimeFormatter.ISO_DATE);
        Log.i(TAG, "formattedDate: " + formattedDate);

        parentHomeFragmentViewModel.getEventsForDate(formattedDate);

        MaterialButton chatButton = view.findViewById(R.id.parenthomefragment_buttonChat);
        parentHomeFragmentViewModel.getParentForClassroom();
        parentHomeFragmentViewModel.getParentForClassroomLiveData().observe(getViewLifecycleOwner(), parent -> {
            if (parent instanceof Event.Success) {
                parentInfo = ((Event.Success<UserModel>) parent).getData();
            }
        });


        chatButton.setOnClickListener(v -> {
            if(parentInfo!=null){
                    Intent intent = new Intent(getActivity(), chatActivity.class);
                    intent.putExtra("classId", parentInfo.getClassId());
                    intent.putExtra("parentId", parentInfo.getId());
                    intent.putExtra("parentName", parentInfo.getName());
                    startActivity(intent);
            }
            else{
                Toast.makeText(getActivity(), R.string.parent_no_chat, Toast.LENGTH_SHORT).show();
            }

        });



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // el Fragment puede verse solo en vertical
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        parentHomeFragmentViewModel.getEventsForDate(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
    }

    @Override
    public void onPause() {
        super.onPause();
        // restablece la orientacion a la de la activity
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    public void updateEvents(List<EventModel> events) {
        Log.i(TAG, "updateEvents: " + events.size());
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
        Log.i(TAG, "paintDaysWithEvents: " + events.size());
        eventDays.clear(); // Limpiar la lista de días con eventos antes de añadir los nuevos
        for (EventModel event : events) {
            Calendar calendar = Calendar.getInstance();
            java.sql.Date date = java.sql.Date.valueOf(event.getDate());
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

        selectedDay.setBackgroundResource(R.drawable.baseline_circle_24);
        List<CalendarDay> selectedDays = new ArrayList<>(eventDays); // Copiar los días con eventos
        selectedDays.add(selectedDay); // Añadir el día seleccionado
        calendarView.setCalendarDays(selectedDays);

        previouslySelectedDay = selectedDay; // Actualizar la referencia al día seleccionado previamente
    }
    public static String getCurrentDate() {
        // Crear un formato de fecha
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        // Obtener la fecha actual
        Date today = new Date();
        // Devolver la fecha formateada
        return dateFormat.format(today);
    }
}
