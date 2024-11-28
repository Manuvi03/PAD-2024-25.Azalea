package es.ucm.fdi.azalea.presentation.addstudent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import es.ucm.fdi.azalea.R;

public class AddStudentFragment extends Fragment {

    private final String TAG = "AddStudentFragment";

    private View view;
    private TextView addStudentTitle, divider, dividerParents;
    private EditText studentName, studentSurname, studentBirthdate, studentWeight, studentHeight, studentAllergens, studentMedicalConditions;
    private EditText parentName, parentSurname, parentEmail, parentAddress, primaryPhone, secondaryPhone;
    private EditText secondParentName, secondParentSurnames, tertiaryPhone;
    private Button saveStudentButton;
    private ScrollView addStudentScrollView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d("AddStudentFragment", "onCreate: Fragment añadido a la pila de retroceso.");
        view = inflater.inflate(R.layout.add_student_fragment, container, false);

        bindComponents();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void bindComponents() {
        // Enlazar TextView
        addStudentTitle = view.findViewById(R.id.addStudentTitle);
        divider = view.findViewById(R.id.divider);
        dividerParents = view.findViewById(R.id.divider_parents);

        // Enlazar EditText - Estudiante
        studentName = view.findViewById(R.id.student_name);
        studentSurname = view.findViewById(R.id.student_surname);
        studentBirthdate = view.findViewById(R.id.student_birthdate);
        studentWeight = view.findViewById(R.id.student_weight);
        studentHeight = view.findViewById(R.id.student_height);
        studentAllergens = view.findViewById(R.id.student_allergens);
        studentMedicalConditions = view.findViewById(R.id.student_medical_conditions);

        // Enlazar EditText - Primer padre
        parentName = view.findViewById(R.id.parent_name);
        parentSurname = view.findViewById(R.id.parent_surname);
        parentEmail = view.findViewById(R.id.parent_email);
        parentAddress = view.findViewById(R.id.parent_address);
        primaryPhone = view.findViewById(R.id.primary_phone);
        secondaryPhone = view.findViewById(R.id.secondary_phone);

        // Enlazar EditText - Segundo padre
        secondParentName = view.findViewById(R.id.second_parent_name);
        secondParentSurnames = view.findViewById(R.id.second_parent_surnames);
        tertiaryPhone = view.findViewById(R.id.tertiary_phone);

        // Enlazar ScrollView
        addStudentScrollView = view.findViewById(R.id.addStudentScrollView);

        // Enlazar Botón
        saveStudentButton = view.findViewById(R.id.saveStudentButton);
    }
}

