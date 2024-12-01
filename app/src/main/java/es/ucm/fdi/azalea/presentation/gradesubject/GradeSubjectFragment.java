package es.ucm.fdi.azalea.presentation.gradesubject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.model.ClassRoomModel;
import es.ucm.fdi.azalea.business.model.MarkModel;
import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.integration.Event;

public class GradeSubjectFragment extends Fragment {
    // constantes
    private final String TAG = "GradeSubjectFragment";

    // atributos

    // view
    private View view;

    // view model
    private GradeSubjectViewModel gradeSubjectViewModel;
    StudentGradeSubjectSharedViewModel studentSharedViewModel;

    // componentes de la vista
    private ImageView studentProfileImage;
    private TextView studentNameText;
    private Spinner subjectsSpinner;
    private EditText markEditText;
    private Button gradeSubjectButton;

    // info de la vista todo
    private String studentId;
    private String studentImage;
    private String classId = "-OCiwTrNUUv5c5G5-3zd";//todo
    private StudentModel studentInfo;
    private ClassRoomModel classInfo;

    private String subjectSelected;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // se genera el viewmodel compartido y se observan los valores que se necesitan pasar
        studentSharedViewModel = new ViewModelProvider((FragmentActivity) view.getContext()).get(StudentGradeSubjectSharedViewModel.class);
        studentSharedViewModel.getStudentId().observe((FragmentActivity) view.getContext(), data -> {
            Log.d(TAG, "StudentId recibido");
            studentId = data;
        });
        studentSharedViewModel.getStudentProfileImage().observe((FragmentActivity) view.getContext(), data -> {
            Log.d(TAG, "StudentImage recibido");
            studentImage = data;
        });

        // se obtiene el viewmodel
        gradeSubjectViewModel = new ViewModelProvider(this).get(GradeSubjectViewModel.class);

        // se obtienen los componentes de la vista necesarios
        bindComponents();

        // se inicializa la vista con la info del estudiante y de las asignaturas de la clase
        initView();

        // se inicializan los listeners del spinner y del boton
        initListeners();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // se enlaza la vista a la clase
        view = inflater.inflate(R.layout.grade_subject_fragment, container, false);
        Log.d(TAG, "Se ha creado el StudentFragment");

        return view;
    }


    // se obtienen los componentes de la vista
    private void bindComponents(){
        studentProfileImage = view.findViewById(R.id.grade_subject_profile_image);
        studentNameText = view.findViewById(R.id.grade_subject_name_textView);
        subjectsSpinner = view.findViewById(R.id.grade_subject_subjects_spinner);
        markEditText = view.findViewById(R.id.grade_subject_mark_edittext);
        gradeSubjectButton = view.findViewById(R.id.grade_subject_button);
    }

    // se inicializa la vista
    private void initView(){
        // se obtiene la informacion del estudiante
        gradeSubjectViewModel.readStudent(studentId);

        // se obtiene informacion de la clase
        gradeSubjectViewModel.readClass(classId);

        // se inicializan los observadores de la vista
        initStudentInfoObserver();
        initClassInfoObserver();
        initMarkInfoObserver();
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
                updateTextViewInfo(studentInfo);
            }

            // en caso de error, se muestra al usuario
            else if(studentEvent instanceof Event.Error){
                Log.d(TAG, "Hubo algun error buscando la informacion del estudiante");
                // se muestra el error mediante un mensaje toast
                Toast.makeText(getActivity(),R.string.student_search_error,Toast.LENGTH_LONG).show();
            }
        };

        // se inicializa el observador
        gradeSubjectViewModel.getStudentState().observe(getViewLifecycleOwner(), studentInfoObserver);
    }

    // actualiza la textview con el nombre del estudiante
    private void updateTextViewInfo(StudentModel sm){
        // foto de perfil del estudiante
        Picasso.get()
                .load(studentImage)
                .placeholder(R.drawable.teacher_classroom_student_image)
                .error(R.drawable.teacher_classroom_student_image_error)
                .into(studentProfileImage);

        // nombre del estudiante
        studentNameText.setText(sm.getName() + " " + sm.getSurnames());
    }

    // inicializa el observador de la informacion de la clase
    private void initClassInfoObserver(){
        // se declara el observador de la info de la clase
        final Observer<Event<ClassRoomModel>> classInfoObserver = classEvent -> {

            // mientras se busca, se muestra el texto de cargando
            if(classEvent instanceof Event.Loading){
                Log.d(TAG, "Se esta buscando la clase");
                studentNameText.setText(R.string.searching_class);
            }

            // cuando se encuentran los resultados, se muestran
            else if(classEvent instanceof Event.Success){
                Log.d(TAG, "Se encontro la informacion del estudiante");
                // se obtiene la info
                classInfo = ((Event.Success<ClassRoomModel>) classEvent).getData();

                // actualiza la searchview con los datos del estudiante
                updateSpinnerInfo(classInfo);
            }

            // en caso de error, se muestra al usuario
            else if(classEvent instanceof Event.Error){
                Log.d(TAG, "Hubo algun error buscando la informacion de la clase");
                // se muestra el error mediante un mensaje toast
                Toast.makeText(getActivity(),R.string.class_search_error,Toast.LENGTH_LONG).show();
            }
        };

        // se inicializa el observador
        gradeSubjectViewModel.getClassState().observe(getViewLifecycleOwner(), classInfoObserver);
    }

    // actualiza la informacion del spinner con las asignaturas de la clase
    private void updateSpinnerInfo(ClassRoomModel crm){
        List<String> list = new ArrayList<String>();
        if(crm.getSubjects() != null) list.addAll(crm.getSubjects());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireActivity(),
                android.R.layout.simple_spinner_item, crm.getSubjects());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectsSpinner.setAdapter(adapter);
    }

    // se inicializan los listeners
    private void initListeners(){

        // listener del spinner
        subjectsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // obtiene el valor actual seleccionado
                subjectSelected = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // si no se ha seleccionado asignatura, se muestra el error
                Toast.makeText(requireActivity(), R.string.grade_subject_no_selected, Toast.LENGTH_SHORT).show();
            }
        });

        // listener del boton de enviar nota
        gradeSubjectButton.setOnClickListener(listener -> {
            // se comprueba que se ha rellenado el campo
            if(markEditText.getText().toString().isBlank()) {
                Toast.makeText(requireActivity(), R.string.grade_subject_no_mark, Toast.LENGTH_SHORT).show();
            }else{
                // se obtiene la nota
                double mark = Double.parseDouble(markEditText.getText().toString());

                // se comprueba que es correcta
                if(mark > 10 || mark < 0){
                    Toast.makeText(requireActivity(), R.string.grade_subject_incorrect_grading, Toast.LENGTH_SHORT).show();
                }
                // y se envia la nota
                else gradeSubjectViewModel.gradeMark(new MarkModel(null, subjectSelected, mark, studentId), studentInfo);
            }
        });
    }

    // inicializa el observador de la informacion de la nota puesta
    private void initMarkInfoObserver(){
        // se declara el observador de la info de la nota
        final Observer<Event<StudentModel>> markInfoObserver = markEvent -> {

            // mientras se busca, se muestra el texto de cargando
            if(markEvent instanceof Event.Loading){
                Log.d(TAG, "Se esta calificando al estudiante");
                gradeSubjectButton.setText(R.string.grade_subject_grading_mark);
            }

            // cuando se realiza la operacion, se comunica al usuario
            else if(markEvent instanceof Event.Success){
                Log.d(TAG, "Se califico al estudiante");
                gradeSubjectButton.setText(R.string.grade_subject_send_grade_text);

                // se muestra la operacion realizada mediante un mensaje toast
                Toast.makeText(getActivity(),
                        getString(R.string.grade_subject_correctly_graded,
                                (studentInfo.getName() + " " + studentInfo.getSurnames())), Toast.LENGTH_LONG).show();
            }

            // en caso de error, se muestra al usuario
            else if(markEvent instanceof Event.Error){
                Log.d(TAG, "Hubo algun error buscando la informacion del estudiante");
                gradeSubjectButton.setText(R.string.grade_subject_send_grade_text);

                // se muestra el error mediante un mensaje toast
                Toast.makeText(getActivity(),
                        getString(R.string.grade_subject_error_grading,
                                (studentInfo.getName() + " " + studentInfo.getSurnames())),Toast.LENGTH_LONG).show();
            }
        };

        // se inicializa el observador
        gradeSubjectViewModel.getMarkState().observe(getViewLifecycleOwner(), markInfoObserver);
    }
}
