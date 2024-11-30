package es.ucm.fdi.azalea.presentation.addevent;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.model.EventModel;
import es.ucm.fdi.azalea.presentation.teacher.TeacherHomeFragmentViewModel;

public class AddEventFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d("AddEventFragment", "onCreate: Fragment añadido a la pila de retroceso.");

        View view = inflater.inflate(R.layout.add_event_fragment, container, false);

        TeacherHomeFragmentViewModel viewModel = new ViewModelProvider(requireActivity()).get(TeacherHomeFragmentViewModel.class);

        EditText title = view.findViewById(R.id.addEvent_title);
        TextView date = view.findViewById(R.id.addEvent_date);
        TextView time = view.findViewById(R.id.addEvent_time);
        EditText location = view.findViewById(R.id.addEvent_location);
        EditText description = view.findViewById(R.id.addEvent_description);
        Button date_button = view.findViewById(R.id.addEvent_date_picker);
        Button time_button = view.findViewById(R.id.addEvent_time_picker);
        Button button = view.findViewById(R.id.addEvent_button);

        date_button.setOnClickListener(v -> {
            MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText(R.string.addevent_datePickerTitle)
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build();
            materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                @SuppressLint("SimpleDateFormat") String d = new SimpleDateFormat("yyyy-MM-dd").format(new Date(selection));
                Log.d("DatePicker", "Date: " + d);
                date.setText(d);
            });
            materialDatePicker.show(getParentFragmentManager(), "TAG");
        });

        time_button.setOnClickListener(v -> {
            MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(12)
                    .setMinute(0)
                    .setTitleText(R.string.addevent_timePickerTitle)
                    .build();
            materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View view) {
                    Log.d("TimePicker", "Time: " + String.format(Locale.getDefault(), "%02d:%02d", materialTimePicker.getHour(), materialTimePicker.getMinute()));
                    time.setText(String.format(Locale.getDefault(), "%02d:%02d", materialTimePicker.getHour(), materialTimePicker.getMinute()));
                }
            });
            materialTimePicker.show(getParentFragmentManager(), "TAG");
        });

        button.setOnClickListener(v -> {
            Log.d("AddEventFragment", "Se crea el evento");
            String em_title = title.getText().toString();
            String em_date = date.getText().toString();
            String em_time = time.getText().toString();
            String em_location = location.getText().toString();
            String em_description = description.getText().toString();

            if(em_title.isEmpty()){
                Toast.makeText(getContext(), "El título no puede estar vacío", Toast.LENGTH_SHORT).show();
                return;
            }
            if(em_date.isEmpty()){
                Toast.makeText(getContext(), "La fecha no puede estar vacía", Toast.LENGTH_SHORT).show();
                return;
            }
            EventModel event = new EventModel(em_date, em_title, em_description, em_time, em_location, "", "");
            Log.i("AddEventFragment", "Event: " + event);
            viewModel.CreateEvent(event);

            viewModel.createEventLiveData().observe(getViewLifecycleOwner(), result -> {
                if (result != null ) {
                    Toast.makeText(getContext(), "Evento creado correctamente", Toast.LENGTH_SHORT).show();
                    // Volver al fragmento anterior (TeacherHomeFragment)
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getContext(), "Error al crear el evento. Inténtelo de nuevo", Toast.LENGTH_SHORT).show();
                }
            });
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
