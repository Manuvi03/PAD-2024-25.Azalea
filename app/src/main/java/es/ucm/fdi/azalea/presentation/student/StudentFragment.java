package es.ucm.fdi.azalea.presentation.student;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;

import java.util.List;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.integration.Event;

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

    // componentes de la vista
    private ScrollView scrollView;

    private ImageView studentProfileImage;
    private TextView studentNameText;
    private TextView studentBirthadayText;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        view = inflater.inflate(R.layout.student_fragment, container, false);
        Log.d(TAG, "Se ha creado el StudentFragment");

        ClassroomStudentSharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(ClassroomStudentSharedViewModel.class);
        viewModel.getStudentId().observe(getViewLifecycleOwner(), data -> {
            studentId = data;
        });
        viewModel.getStudentProfileImage().observe(getViewLifecycleOwner(), data -> {
            studentImage = data;
        });

        /*
        // se obtienen los argumentos pasados al Fragment
        if(savedInstanceState != null){
            studentId = savedInstanceState.getString(STUDENT_ID_KEY);
            studentImage = savedInstanceState.getString(STUDENT_IMAGE_KEY);
        }*/

        // se obtiene el viewmodel
        studentViewModel = new ViewModelProvider(this).get(StudentViewModel.class);

        // se obtienen los componentes de la vista necesarios
        bindComponents();

        // se obtiene el scrollview y se inicializa su observador
        initScrollView();

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
        studentBirthadayText = view.findViewById(R.id.student_fragment_birthday_textview);
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
        // se encuentra la scrollview
        scrollView = view.findViewById(R.id.student_fragment_scrollView);

        // se obtiene la informacion del estudiante
        studentViewModel.readStudent(studentId);

        // se obtiene informacion del tutor del estudiante
        //studentViewModel.readParentByStudent(studentId);

        // se inicializan los observadores de la recyclerview
        initStudentInfoObserver();
        //initParentInfoObserver();
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
        studentNameText.setText(sm.getName());

        // fecha de nacimiento
        studentBirthadayText.setText(getString(R.string.student_birthday_text, sm.getBirthday()));

        // peso
        studentWeightText.setText(getString(R.string.student_weight_text, sm.getWeight()));

        // altura
        studentHeightText.setText(getString(R.string.student_height_text, sm.getHeight()));

        // alergenos
        studentBirthadayText.setText(getString(R.string.student_allergens_text, sm.getAllergens()));

        // condiciones medicas
        studentBirthadayText.setText(getString(R.string.student_medical_conditions_text, sm.getMedicalConditions()));

        // direccion
        studentBirthadayText.setText(getString(R.string.student_address_text, sm.getAddress()));

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
        for(String s : names) atributesBuilt.append(s).append('\n');

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

}
