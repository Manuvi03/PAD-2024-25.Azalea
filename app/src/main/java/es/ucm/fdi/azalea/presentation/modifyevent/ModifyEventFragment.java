package es.ucm.fdi.azalea.presentation.modifyevent;

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

public class ModifyEventFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ModifyEventFragment", "onCreate: Fragment añadido a la pila de retroceso.");

        View view = inflater.inflate(R.layout.modify_event_fragment, container, false);

        TeacherHomeFragmentViewModel viewModel = new ViewModelProvider(requireActivity()).get(TeacherHomeFragmentViewModel.class);

        EditText title_et = view.findViewById(R.id.modifyEvent_title);
        TextView date_tw = view.findViewById(R.id.modifyEvent_date);
        TextView time_tw = view.findViewById(R.id.modifyEvent_time);
        EditText location_et = view.findViewById(R.id.modifyEvent_location);
        EditText description_et = view.findViewById(R.id.modifyEvent_description);
        Button date_button = view.findViewById(R.id.modifyEvent_date_picker);
        Button time_button = view.findViewById(R.id.modifyEvent_time_picker);
        Button button = view.findViewById(R.id.modifyEvent_button);


        Bundle bundle = getArguments();
        if (bundle != null) {
            title_et.setText(bundle.getString("title", ""));
            date_tw.setText(bundle.getString("date", ""));
            time_tw.setText(bundle.getString("time", ""));
            location_et.setText(bundle.getString("location", ""));
            description_et.setText(bundle.getString("description", ""));

            Log.d("ModifyEventFragment", "Datos recibidos: " + title_et.getText() + ", " + date_tw.getText());
        } else {
            Log.e("ModifyEventFragment", "No se han recibido datos");
            Toast.makeText(getContext(), R.string.modifyevent_toastNoData, Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        }

        date_button.setOnClickListener(v -> {
            MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText(R.string.addevent_datePickerTitle)
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build();
            materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                @SuppressLint("SimpleDateFormat") String d = new SimpleDateFormat("yyyy-MM-dd").format(new Date(selection));
                Log.d("ModifyDatePicker", "Date: " + d);
                date_tw.setText(d);
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
                    Log.d("ModifyTimePicker", "Time: " + String.format(Locale.getDefault(), "%02d:%02d", materialTimePicker.getHour(), materialTimePicker.getMinute()));
                    time_tw.setText(String.format(Locale.getDefault(), "%02d:%02d", materialTimePicker.getHour(), materialTimePicker.getMinute()));
                }
            });
            materialTimePicker.show(getParentFragmentManager(), "TAG");
        });

        button.setOnClickListener(v -> {
            Log.d("AddEventFragment", "Se crea el evento");
            String em_title = title_et.getText().toString();
            String em_date = date_tw.getText().toString();
            String em_time = time_tw.getText().toString();
            String em_location = location_et.getText().toString();
            String em_description = description_et.getText().toString();

            if (em_title.isEmpty()) {
                Toast.makeText(getContext(), R.string.modifyevent_toastTitleEmpty, Toast.LENGTH_SHORT).show();
                return;
            }
            if (em_date.isEmpty()) {
                Toast.makeText(getContext(), R.string.modifyevent_toastDateEmpty, Toast.LENGTH_SHORT).show();
                return;
            }
            assert bundle != null;
            EventModel event = new EventModel(em_date, em_title, em_description, em_time, em_location, bundle.getString("idClass", ""), bundle.getString("id", ""));
            Log.i("ModifyEventFragment", "Event: " + event.getId());
            viewModel.ModifyEvent(event);

            viewModel.modifyEventLiveData().observe(getViewLifecycleOwner(), result -> {
                if (result != null) {
                    Toast.makeText(getContext(), R.string.modifyevent_toastCorrect, Toast.LENGTH_SHORT).show();
                    // Volver al fragmento anterior (TeacherHomeFragment)
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getContext(), R.string.modifyevent_toastIncorrect, Toast.LENGTH_SHORT).show();
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
