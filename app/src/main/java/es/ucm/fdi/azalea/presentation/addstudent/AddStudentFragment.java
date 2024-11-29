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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.model.ClassRoomModel;
import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.integration.Event;

public class AddStudentFragment extends Fragment {

    private final String TAG = "AddStudentFragment";

    private AddStudentViewModel addstudentViewModel;
    private FirebaseUser current_firebase_user;
    private UserModel current_user;
    private ClassRoomModel current_class;
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

        addstudentViewModel = new ViewModelProvider(this).get(AddStudentViewModel.class);

        bindComponents();

        saveStudentButton.setOnClickListener(v ->{
            init();
        });

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

    private void init() {
        Log.d(TAG, "init: Iniciando la creación del estudiante.");

        // Llamamos a la función asíncrona para obtener y crear el estudiante
        getStudentInfoAsync(() -> {
            // Éxito: Estudiante creado correctamente
            Log.d(TAG, "onSuccess: El estudiante se ha creado correctamente.");

            // Mostrar el mensaje de éxito
            Toast.makeText(getContext(), R.string.add_student_toast_success, Toast.LENGTH_SHORT).show();

            // Volver a la pantalla anterior
            requireActivity().getSupportFragmentManager().popBackStack();
        }, () -> {
            // Error: No se pudo crear el estudiante
            Log.d(TAG, "onFailure: Ha ocurrido un error al crear el estudiante.");

            // Mostrar el mensaje de error
            Toast.makeText(getContext(), R.string.add_student_toast_error, Toast.LENGTH_SHORT).show();

            // Volver a la pantalla anterior
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }
    private void getStudentInfoAsync(Runnable onSuccess, Runnable onFailure) {
        Log.d(TAG, "getStudentInfoAsync: Obteniendo información del estudiante");
        // Paso 1: Obtener el usuario actual
        addstudentViewModel.getCurrUserFirebase();

        addstudentViewModel.getCurrUserFirebaseLD().observe(getViewLifecycleOwner(), event -> {
            if (event instanceof Event.Success) {
                current_firebase_user = ((Event.Success<FirebaseUser>) event).getData();
                Log.d(TAG, "Se ha obtenido el usuario actual con email: " + current_firebase_user.getEmail());

                //Paso 2: Obtener el modelo de usuario asociado al usuario actual
                Log.d(TAG, "Se ha obtenido el usuario actual con id: " + current_firebase_user.getUid());
                addstudentViewModel.getCurrUser(current_firebase_user.getUid());
                addstudentViewModel.getCurrUserLD().observe(getViewLifecycleOwner(), event1 -> {
                    if (event1 instanceof Event.Success) {
                        current_user = ((Event.Success<UserModel>) event1).getData();

                        // Paso 3: Obtener la clase asociada al usuario
                        String classId = current_user.getClassId();
                        Log.d(TAG, "Se ha obtenido la clase asociada al usuario actual con id: " + classId);
                        addstudentViewModel.getClass(classId);
                        addstudentViewModel.getClassState().observe(getViewLifecycleOwner(), event2 -> {
                            if (event2 instanceof Event.Success) {
                                current_class = ((Event.Success<ClassRoomModel>) event2).getData();
                                Log.d(TAG, "Se ha obtenido la clase asociada al usuario actual con id: " + current_class.getId());

                                // Paso 4: Crear el estudiante
                                StudentModel student = getStudentInfo();
                                if (student == null) {
                                    onFailure.run();
                                    return;
                                }
                                addstudentViewModel.createStudent(student);
                                addstudentViewModel.getStudentState().observe(getViewLifecycleOwner(), result -> {
                                    if (result != null) {
                                        Log.i(TAG, "Se ha creado el estudiante correctamente");
                                        //Paso 5: Crear el auth para el padre
                                        onSuccess.run();

                                    } else {
                                        onFailure.run();
                                    }
                                });
                            } else {
                                Log.d(TAG, "Error al obtener la clase del usuario actual");
                                onFailure.run();
                            }
                        });
                    } else {
                        Log.d(TAG, "Error al obtener el usuario actual en el Authentication");
                        onFailure.run();
                    }
                });
            }
        });
    }

    private StudentModel getStudentInfo() {

        String name = studentName.getText().toString();
        String surname = studentSurname.getText().toString();
        String birthdate = studentBirthdate.getText().toString();
        String weight = studentWeight.getText().toString();
        String height = studentHeight.getText().toString();
        String allergens = studentAllergens.getText().toString();
        String medicalConditions = studentMedicalConditions.getText().toString();
        String parentName1 = parentName.getText().toString();
        String parentSurname1 = parentSurname.getText().toString();
        String parentEmail1 = parentEmail.getText().toString();
        String parentAddress1 = parentAddress.getText().toString();
        String primaryPhone1 = primaryPhone.getText().toString();
        String secondaryPhone1 = secondaryPhone.getText().toString();
        String parentName2 = secondParentName.getText().toString();
        String parentSurname2 = secondParentSurnames.getText().toString();
        String tertiaryPhone2 = tertiaryPhone.getText().toString();

        if(name.isEmpty()||surname.isEmpty()||birthdate.isEmpty() ||
        parentName1.isEmpty()||parentSurname1.isEmpty()||parentEmail1.isEmpty()||
        parentAddress1.isEmpty()||primaryPhone1.isEmpty()) {
            return null;
        }
        if(weight.isEmpty()) {
            weight = "0";
        }
        if(height.isEmpty()) {
            height = "0";
        }

            StudentModel student = new StudentModel(current_class.getSubjects(),
                    allergens,
                    medicalConditions,
                    Double.parseDouble(height),
                    Double.parseDouble(weight),
                    surname,
                    name,
                    primaryPhone1,
                    current_class.getId(),
                    "",
                    birthdate,
                    parentAddress1,
                    List.of(parentName1 + " " + parentSurname1, parentName2 + " " + parentSurname2),
                    List.of(primaryPhone1, secondaryPhone1, tertiaryPhone2));

          return student;
    }
}

