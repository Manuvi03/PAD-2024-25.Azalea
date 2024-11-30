package es.ucm.fdi.azalea.presentation.addstudent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.model.ClassRoomModel;
import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.integration.Event;

public class AddStudentFragment extends Fragment {

    private final String TAG = "AddStudentFragment";

    private StudentModel student_generated;
    private AddStudentViewModel addstudentViewModel;
    private FirebaseUser current_firebase_user;
    private UserModel current_user, parent_generated;
    private ClassRoomModel current_class;
    private boolean exist, updated;

    private View view;
    private EditText studentName, studentSurname, studentBirthdate, studentWeight, studentHeight, studentAllergens, studentMedicalConditions;
    private EditText parentName, parentSurname, parentGender, parentEmail, parentAddress, primaryPhone, secondaryPhone;
    private EditText secondParentName, secondParentSurnames, tertiaryPhone;
    private Button saveStudentButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("AddStudentFragment", "onCreate: Fragment añadido a la pila de retroceso.");
        view = inflater.inflate(R.layout.add_student_fragment, container, false);

        addstudentViewModel = new ViewModelProvider(this).get(AddStudentViewModel.class);

        exist = false;
        updated = false;

        bindComponents();

        saveStudentButton.setOnClickListener(v -> {
            init();
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void bindComponents() {

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
        parentGender = view.findViewById(R.id.parent_gender);
        parentEmail = view.findViewById(R.id.parent_email);
        parentAddress = view.findViewById(R.id.parent_address);
        primaryPhone = view.findViewById(R.id.primary_phone);
        secondaryPhone = view.findViewById(R.id.secondary_phone);

        // Enlazar EditText - Segundo padre
        secondParentName = view.findViewById(R.id.second_parent_name);
        secondParentSurnames = view.findViewById(R.id.second_parent_surnames);
        tertiaryPhone = view.findViewById(R.id.tertiary_phone);

        // Enlazar Botón
        saveStudentButton = view.findViewById(R.id.saveStudentButton);
    }

    private void init() {
        Log.d(TAG, "init: Iniciando la creación del estudiante.");

        getStudent().thenAccept(result -> {
            switch (result) {
                case -1:
                    Toast.makeText(getContext(), "Error al obtener el usuario actual", Toast.LENGTH_SHORT).show();
                    break;
                case -2:
                    Toast.makeText(getContext(), "Error al obtener el usuario actual", Toast.LENGTH_SHORT).show();
                    break; // El mismo mensaje para la RealtimeDatabase
                case -3:
                    Toast.makeText(getContext(), "Error al obtener la clase del usuario actual", Toast.LENGTH_SHORT).show();
                    break;
                case -4:
                    Toast.makeText(getContext(), "Error al leer los datos", Toast.LENGTH_SHORT).show();
                    break;
                case -5:
                    Toast.makeText(getContext(), "Error al crear el estudiante", Toast.LENGTH_SHORT).show();
                    break;
                case -6:
                    Toast.makeText(getContext(), "El padre ya existe", Toast.LENGTH_SHORT).show();
                    break;
                case -7:
                    Toast.makeText(getContext(), "Error al crear el padre", Toast.LENGTH_SHORT).show();
                    break;
                case -8:
                    Toast.makeText(getContext(), "Error al actualizar el estudiante", Toast.LENGTH_SHORT).show();
                    break;
                case -9:
                    Toast.makeText(getContext(), "Error desconocido, no se puede generar el estudiante", Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    Toast.makeText(getContext(), "Estudiante creado correctamente", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                    break;
            }
        }).exceptionally(ex -> {
            Log.e(TAG, "Error inesperado al crear el estudiante.", ex);
            Toast.makeText(getContext(), "Error inesperado: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        });
    }

    private CompletableFuture<Integer> getStudent() {
        Log.d(TAG, "getStudentInfo: Obteniendo información del estudiante");

        return CompletableFuture.supplyAsync(() -> {
            try {
                // Paso 1: Obtener el usuario actual
                CompletableFuture<FirebaseUser> currentUserFuture = new CompletableFuture<>();
                addstudentViewModel.getCurrUserFirebase();
                addstudentViewModel.getCurrUserFirebaseLD().observe(getViewLifecycleOwner(), event -> {
                    if (event instanceof Event.Success) {
                        FirebaseUser user = ((Event.Success<FirebaseUser>) event).getData();
                        currentUserFuture.complete(user);
                    } else {
                        currentUserFuture.completeExceptionally(new Exception("Error al obtener el usuario actual."));
                    }
                });
                FirebaseUser currentUser = currentUserFuture.join();
                if (currentUser.isAnonymous() || currentUser.getEmail() == null) {
                    return -1;
                }

                // Paso 2: Obtener el modelo de usuario asociado
                CompletableFuture<UserModel> userModelFuture = new CompletableFuture<>();
                addstudentViewModel.getCurrUser(currentUser.getUid());
                addstudentViewModel.getCurrUserLD().observe(getViewLifecycleOwner(), event -> {
                    if (event instanceof Event.Success) {
                        UserModel user = ((Event.Success<UserModel>) event).getData();
                        userModelFuture.complete(user);
                    } else {
                        userModelFuture.completeExceptionally(new Exception("Error al obtener el modelo de usuario."));
                    }
                });
                UserModel currentUserModel = userModelFuture.join();
                if (currentUserModel == null || currentUserModel.getId() == null) {
                    return -2;
                }

                // Paso 3: Obtener la clase asociada al usuario
                CompletableFuture<ClassRoomModel> classRoomFuture = new CompletableFuture<>();
                addstudentViewModel.getClass(currentUserModel.getClassId());
                addstudentViewModel.getClassState().observe(getViewLifecycleOwner(), event -> {
                    if (event instanceof Event.Success) {
                        ClassRoomModel classRoom = ((Event.Success<ClassRoomModel>) event).getData();
                        classRoomFuture.complete(classRoom);
                    } else {
                        classRoomFuture.completeExceptionally(new Exception("Error al obtener la clase asociada."));
                    }
                });
                ClassRoomModel classRoom = classRoomFuture.join();
                if (classRoom == null || classRoom.getId() == null) {
                    return -3;
                }

                // Paso 4: Crear el estudiante
                StudentModel student = getStudentInfo();
                if (student == null) {
                    return -4;
                }

                CompletableFuture<Void> createStudentFuture = new CompletableFuture<>();
                addstudentViewModel.createStudent(student);
                addstudentViewModel.getStudentState().observe(getViewLifecycleOwner(), event -> {
                    if (event != null) {
                        Log.i(TAG, "Se ha creado el estudiante correctamente");
                        student_generated = student;
                        createStudentFuture.complete(null);
                    } else {
                        createStudentFuture.completeExceptionally(new Exception("Error al crear el estudiante."));
                    }
                });
                createStudentFuture.join();

                if (student_generated == null || student_generated.getId() == null) {
                    return -5;
                }

                // Paso 5: Verificar si el padre existe
                CompletableFuture<Boolean> checkParentFuture = new CompletableFuture<>();
                addstudentViewModel.checkParent(parentEmail.getText().toString());
                addstudentViewModel.getCheckParentState().observe(getViewLifecycleOwner(), event -> {
                    if (event instanceof Event.Success) {
                        boolean parentExists = ((Event.Success<Boolean>) event).getData();
                        checkParentFuture.complete(parentExists);
                    } else {
                        checkParentFuture.completeExceptionally(new Exception("Error al verificar si el padre existe."));
                    }
                });
                exist = checkParentFuture.join();
                if (exist) {
                    return -6;
                }

                // Paso 6: Crear el padre si no existe
                CompletableFuture<Void> createParentFuture = new CompletableFuture<>();
                UserModel parent = getParentInfo();
                addstudentViewModel.createParent(parent);
                addstudentViewModel.getParentState().observe(getViewLifecycleOwner(), event -> {
                    if (event instanceof Event.Success) {
                        parent_generated = parent;
                        createParentFuture.complete(null);
                    } else {
                        createParentFuture.completeExceptionally(new Exception("Error al crear el padre."));
                    }
                });
                createParentFuture.join();

                if (parent_generated == null || parent_generated.getId() == null) {
                    return -7;
                }

                // Paso 7: Actualizar el estudiante con el id del padre
                CompletableFuture<Void> updateStudentFuture = new CompletableFuture<>();
                student_generated.setParentId(parent_generated.getId());
                addstudentViewModel.updateStudent(student_generated.getId(), student_generated);
                addstudentViewModel.getUpdateStudentState().observe(getViewLifecycleOwner(), event -> {
                    if (event instanceof Event.Success) {
                        updated = ((Event.Success<Boolean>) event).getData();
                        updateStudentFuture.complete(null);
                    } else {
                        updateStudentFuture.completeExceptionally(new Exception("Error al actualizar el estudiante."));
                    }
                });
                updateStudentFuture.join();

                if (!updated) {
                    return -8;
                }
            } catch (Exception e) {
                Log.e(TAG, "Error desconocido", e);
                return -9;
            }
            return 0;
        });
    }

    private UserModel getParentInfo() {
        UserModel parent = new UserModel();
        parent.setEmail(parentEmail.getText().toString());
        parent.setName(parentName.getText().toString());
        parent.setSurname(parentSurname.getText().toString());
        parent.setGender(parentGender.getText().toString());
        parent.setPassword(PasswordGenerate.generateRandomPassword());
        parent.setParent(true);
        parent.setClassId(current_class.getId());
        parent.setStudentId(student_generated.getId());
        return parent;
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

        if (name.isEmpty() || surname.isEmpty() || birthdate.isEmpty() ||
                parentName1.isEmpty() || parentSurname1.isEmpty() || parentEmail1.isEmpty() ||
                parentAddress1.isEmpty() || primaryPhone1.isEmpty()) {
            return null;
        }
        if (weight.isEmpty()) {
            weight = "0";
        }
        if (height.isEmpty()) {
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

