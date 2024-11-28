package es.ucm.fdi.azalea.presentation.student;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;

import java.util.List;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.integration.Event;
import es.ucm.fdi.azalea.presentation.chat.chatActivity;
import es.ucm.fdi.azalea.presentation.classroom.ClassroomStudentSharedViewModel;
import es.ucm.fdi.azalea.presentation.editstudent.EditStudentFragment;
import es.ucm.fdi.azalea.presentation.gradesubject.GradeSubjectFragment;
import es.ucm.fdi.azalea.presentation.gradesubject.StudentGradeSubjectSharedViewModel;
import es.ucm.fdi.azalea.presentation.showgrades.ShowGradesFragment;
import es.ucm.fdi.azalea.presentation.showgrades.StudentShowGradesSharedViewModel;

public class StudentFragment extends Fragment {

    // constantes
    private final String TAG = "StudentFragment";
    private final String STUDENT_ID_KEY = "studentId";
    private final String STUDENT_IMAGE_KEY = "studentImage";

    // atributos

    // view
    private View view;

    // view model
    private StudentViewModel studentViewModel;
    ClassroomStudentSharedViewModel classroomSharedViewModel;
    StudentGradeSubjectSharedViewModel gradeMarkSharedViewModel;

    // componentes de la vista
    private AppCompatImageButton sendMessageButton;
    private AppCompatImageButton gradeMarkButton;
    private AppCompatImageButton showGradesButton;
    private AppCompatImageButton editStudentButton;

    private ImageView studentProfileImage;
    private TextView studentNameText;
    private TextView studentBirthdayText;
    private TextView studentWeightText;
    private TextView studentHeightText;
    private TextView studentAllergensText;
    private TextView studentMedicalConditionsText;

    private TextView parentNameText;
    private TextView parentPhoneText;
    private TextView parentEmailText;
    private TextView studentAddressText;

    // info de la vista
    private String studentId;
    private String studentImage;
    private StudentModel studentInfo;
    private UserModel parentInfo;

        @Override
        public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            // se genera el viewmodel compartido con classroom y se observan los valores que se reciben
            classroomSharedViewModel = new ViewModelProvider((FragmentActivity) view.getContext()).get(ClassroomStudentSharedViewModel.class);
            classroomSharedViewModel.getStudentId().observe((FragmentActivity) view.getContext(), data -> {
                Log.d(TAG, "StudentId recibido");
                studentId = data;
            });
            classroomSharedViewModel.getStudentProfileImage().observe((FragmentActivity) view.getContext(), data -> {
                Log.d(TAG, "StudentImage recibido");
                studentImage = data;
            });

            // se obtiene el viewmodel
            studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);

            // se obtienen los componentes de la vista necesarios
            bindComponents();

            // se obtiene el scrollview y se inicializa su observador
            initScrollView();

        }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // se enlaza la vista a la clase
        view = inflater.inflate(R.layout.student_fragment, container, false);
        Log.d(TAG, "Se ha creado el StudentFragment");

        return view;
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "Se destruye el StudentFragment");
        super.onDestroyView();
    }

    // enlaza los componentes de la vista con esta clase
    private void bindComponents(){
        studentProfileImage = view.findViewById(R.id.student_fragment_profile_image);
        studentNameText = view.findViewById(R.id.student_fragment_name_textView);

        sendMessageButton = view.findViewById(R.id.student_fragment_send_message_button);
        gradeMarkButton = view.findViewById(R.id.student_fragment_grade_mark_button);
        showGradesButton = view.findViewById(R.id.student_fragment_show_grades_button);
        editStudentButton = view.findViewById(R.id.student_fragment_edit_student_button);

        studentBirthdayText = view.findViewById(R.id.student_fragment_birthday_textview);
        studentWeightText = view.findViewById(R.id.student_fragment_weight_textview);
        studentHeightText = view.findViewById(R.id.student_fragment_height_textview);
        studentAllergensText = view.findViewById(R.id.student_fragment_allergens_textview);
        studentMedicalConditionsText = view.findViewById(R.id.student_fragment_medical_conditions_textview);

        parentNameText = view.findViewById(R.id.student_fragment_parent_name_textview);
        parentPhoneText = view.findViewById(R.id.student_fragment_parent_phone_textview);
        parentEmailText = view.findViewById(R.id.student_fragment_parent_email_textview);
        studentAddressText = view.findViewById(R.id.student_fragment_address_textview);
    }

    // inicializa la scrollview
    private void initScrollView(){
        // se obtiene la informacion del estudiante
        studentViewModel.readStudent(studentId);

        // se obtiene informacion del tutor del estudiante
        studentViewModel.readParentByStudent(studentId);

        // se inicializan los observadores de la recyclerview
        initStudentInfoObserver();
        initParentInfoObserver();

        // se inicializan los listeners de los botones
        initOnClickListeners();
    }

    // inicializa el observador de la informacion del estudiante
    private void initStudentInfoObserver(){
        // se declara el observador de la info del estudiante
        final Observer<Event<StudentModel>> studentInfoObserver = studentEvent -> {

            // mientras se busca, se muestra el texto de cargando
            if(studentEvent instanceof Event.Loading){
                Log.d(TAG, "Se esta buscando el estudiante");
                studentNameText.setText(R.string.searching_student);
            }

            // cuando se encuentran los resultados, se muestran
            else if(studentEvent instanceof Event.Success){
                Log.d(TAG, "Se encontro la informacion del estudiante");
                // se obtiene la info
                studentInfo = ((Event.Success<StudentModel>) studentEvent).getData();

                // actualiza la searchview con los datos del estudiante
                updateScrollViewStudentInfo(studentInfo);
            }

            // en caso de error, se muestra al usuario
            else if(studentEvent instanceof Event.Error){
                Log.d(TAG, "Hubo algun error buscando la informacion del estudiante");
                // se muestra el error mediante un mensaje toast
                Toast.makeText(getActivity(),R.string.student_search_error,Toast.LENGTH_LONG).show();
            }
        };

        // se inicializa el observador
        studentViewModel.getStudentState().observe(getViewLifecycleOwner(), studentInfoObserver);
    }

    // actualiza los datos del estudiante de la vista
    private void updateScrollViewStudentInfo(StudentModel sm){
        // foto de perfil del estudiante
        Picasso.get()
                .load(studentImage)
                .placeholder(R.drawable.teacher_classroom_student_image)
                .error(R.drawable.teacher_classroom_student_image_error)
                .into(studentProfileImage);

        // nombre del estudiante
        studentNameText.setText(sm.getName() + " " + sm.getSurnames());

        // fecha de nacimiento
        studentBirthdayText.setText(getString(R.string.student_birthday_text, sm.getBirthday()));

        // peso
        studentWeightText.setText(getString(R.string.student_weight_text, String.format("%.2f", sm.getWeight())));

        // altura
        studentHeightText.setText(getString(R.string.student_height_text, String.format("%.2f", sm.getHeight())));

        // alergenos
        studentAllergensText.setText(getString(R.string.student_allergens_text, sm.getAllergens()));

        // condiciones medicas
        studentMedicalConditionsText.setText(getString(R.string.student_medical_conditions_text, sm.getMedicalConditions()));

        // direccion
        studentAddressText.setText(getString(R.string.student_address_text, sm.getAddress()));

        // nombre del tutor
        parentNameText.setText(getString(R.string.student_parent_name_text, parentsAtributesToString(sm.getParentsNames())));

        // telefono
        parentPhoneText.setText(getString(R.string.student_phone_text, parentsAtributesToString(sm.getParentsPhones())));
    }

    // convierte la lista de atributos de los padres a un string legible por la vista
    private String parentsAtributesToString(List<String> names){

        // se crea un stringbuilder
        StringBuilder atributesBuilt = new StringBuilder();

        // por cada entrada de la lista, se agrega al stringbuilder en una nueva linea
        for(String s : names) atributesBuilt.append('\n').append(s);

        // se devuelve convertido a string
        return atributesBuilt.toString();
    }

    // inicializa el observador de la informacion del tutor
    private void initParentInfoObserver(){
        // se declara el observador de la info del tutor
        final Observer<Event<UserModel>> parentStateObserver = parentEvent -> {

            // mientras se busca, se muestra el texto de cargando
            if(parentEvent instanceof Event.Loading){
                Log.d(TAG, "Se esta buscando la informacion del tutor del estudiante");
                parentNameText.setText(R.string.searching_student_parent);
            }

            // cuando se encuentran los resultados, se muestran
            else if(parentEvent instanceof Event.Success){
                Log.d(TAG, "Se encontro la informacion del tutor del estudiante");
                // se obtiene la info
                parentInfo = ((Event.Success<UserModel>) parentEvent).getData();

                // actualiza la searchview con los datos del tutor
                updateScrollViewParentInfo(parentInfo);
            }

            // en caso de error, se muestra al usuario
            else if(parentEvent instanceof Event.Error){
                Log.d(TAG, "Hubo algun error buscando la informacion del tutor del estudiante");
                // se muestra el error mediante un mensaje toast
                Toast.makeText(getActivity(),R.string.students_parent_search_error,Toast.LENGTH_LONG).show();
            }
        };

        // se inicializa el observador
        studentViewModel.getParentState().observe(getViewLifecycleOwner(), parentStateObserver);
    }

    // actualiza los datos del estudiante de la vista
    private void updateScrollViewParentInfo(UserModel um){
        // correo electronico
        parentEmailText.setText(getString(R.string.student_email_text, um.getEmail()));
    }

    // se inicializan los listeners de los botones
    private void initOnClickListeners(){

        // inicia el chat
        sendMessageButton.setOnClickListener(listener -> {
            //todo?
            Intent intent = new Intent(getActivity(), chatActivity.class);
            startActivity(intent);
        });

        gradeMarkButton.setOnClickListener(listener -> {
            // se genera el viewmodel compartido
            gradeMarkSharedViewModel = new ViewModelProvider((FragmentActivity) view.getContext()).get(StudentGradeSubjectSharedViewModel.class);

            // se pasan los valores compartidos
            gradeMarkSharedViewModel.setStudentId(studentId);
            gradeMarkSharedViewModel.setStudentProfileImage(studentImage);

            // se reemplaza el fragmento
            replaceFragment(GradeSubjectFragment.class,null);
        });

        showGradesButton.setOnClickListener(listener -> {
            // se genera el viewmodel compartido
            studentShowGradesSharedViewModel = new ViewModelProvider((FragmentActivity) view.getContext()).get(StudentShowGradesSharedViewModel.class);

            // se pasan los valores compartidos
            studentShowGradesSharedViewModel.setStudentId(studentId);
            studentShowGradesSharedViewModel.setStudentProfileImage(studentImage);

            replaceFragment(ShowGradesFragment.class);
        });

        editStudentButton.setOnClickListener(listener -> {

            Bundle data = new Bundle();
            data.putString("direccion",studentInfo.getAddress());
            data.putString("alergenos",studentInfo.getAllergens());
            data.putString("dianac",studentInfo.getBirthday());
            data.putDouble("altura",studentInfo.getHeight());
            data.putString("condmed",studentInfo.getMedicalConditions());
            data.putString("nombre",studentInfo.getName());
            data.putString("apellido",studentInfo.getSurnames());
            data.putString("id",studentInfo.getId());
            data.putString("idclassroom",studentInfo.getClassroomId());
            data.putDouble("peso",studentInfo.getWeight());
            data.putString("idpadre",studentInfo.getParentId());
            data.putString("contactorapido",studentInfo.getQuickContact());
            data.putString("nombrep1",studentInfo.getParentsNames().get(0));
            data.putString("nombrep1",studentInfo.getParentsNames().get(1));
            data.putString("movilp1",studentInfo.getParentsPhones().get(0));
            data.putString("movilp2",studentInfo.getParentsNames().get(0));

            int i = 0;
            for(String s: studentInfo.getSubjects()){
                data.putString("subject" + i,studentInfo.getSubjects().get(i));
                i++;
            }

            int j = 0;
            for(String s: studentInfo.getMarksId()){
                data.putString("mark" + i,studentInfo.getSubjects().get(j));
                j++;
            }
            replaceFragment(EditStudentFragment.class,data);
        });
    }

    // reemplaza este Fragment por el correspondiente segun el boton
    private void replaceFragment(Class<? extends androidx.fragment.app.Fragment> fragment,Bundle args){
        requireActivity().getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.teacher_fragment_container_view, fragment, args)
                .addToBackStack(TAG)
                .commit();
    }

}
