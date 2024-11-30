package es.ucm.fdi.azalea.presentation.classroom;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.integration.Event;
import es.ucm.fdi.azalea.presentation.addstudent.AddStudentFragment;

public class ClassroomFragment extends Fragment implements SearchView.OnQueryTextListener {

    // constantes
    private final String TAG = "ClassroomFragment";

    // atributos

    // view
    private View view;

    // view model
    private classroomViewModel classroomViewModel;

    private TextView resultText, classnameText;

    // para la recyclerview
    private ClassroomListAdapter classroomAdapter;  // su adaptador
    private List<StudentModel> students;            // la info

    // para compartir la informacion necesaria en el StudentFragment se utiliza un viewmodel compartido
    ClassroomStudentSharedViewModel viewModel;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // se genera el viewmodel compartido
        viewModel = new ViewModelProvider((FragmentActivity) view.getContext()).get(ClassroomStudentSharedViewModel.class);

        // se obtiene el viewmodel
        classroomViewModel = new ViewModelProvider(this).get(classroomViewModel.class);

        // se inicializa la recyclerview
        initRecyclerView();

        // se encuentra la searchview y se inicializa su listener
        SearchView searchView = view.findViewById(R.id.teacher_classroom_searchview);
        searchView.setOnQueryTextListener(this);

        // se encuentra el textview para mostrar el nombre de la clase
        classnameText = view.findViewById(R.id.teacher_classroom_info_text);
        classroomViewModel.getClassName();
        classroomViewModel.getClassNameState().observe(getViewLifecycleOwner(), event -> {
            if (event == null) {
                Log.e(TAG, "classNameState es null");
            } else if (event instanceof Event.Success) {
                String className = ((Event.Success<String>) event).getData();
                Log.d(TAG, "Nombre de la clase recibido: " + className);
                classnameText.setText(className);
            } else {
                Log.d(TAG, "Evento recibido: " + event.getClass().getSimpleName());
            }
        });

        // se encuentra el boton para anyadir un alumno y se inicializa su listener
        AppCompatImageButton addStudentButton = view.findViewById(R.id.teacher_classroom_add_student_button);
        addStudentButton.setOnClickListener(view1 -> {
            Log.d(TAG, "Se cambia de fragment a AddStudentFragment");
            ((FragmentActivity) view1.getContext()).getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.teacher_fragment_container_view, AddStudentFragment.class, null)
                    .addToBackStack(TAG)
                    .commit();
        });

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // se enlaza la vista a la clase
        view = inflater.inflate(R.layout.teacher_classroom_fragment, container, false);
        Log.d(TAG, "Se ha creado el ClassroomFragment");

        return view;
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "Se destruye el ClassroomFragment");
        super.onDestroyView();
    }

    // manejo de la recyclerview
    private void initRecyclerView(){
        // se encuentra y ajusta la recycler view
        // de la vista
        RecyclerView classroomList = view.findViewById(R.id.teacher_classroom_recyclerview);
        classroomList.setHasFixedSize(true);

        // como es un fragment, se obtiene su activity (context) asociada
        classroomList.setLayoutManager(new LinearLayoutManager(getActivity()));

        // se crea el adaptador de la RecyclerView, de momento con una lista vacia
        classroomAdapter = new ClassroomListAdapter(Collections.emptyList(), getActivity(), viewModel);
        classroomList.setAdapter(classroomAdapter);

        // al inicar este fragment, directamente aparece la lista de estudiantes
        resultText = view.findViewById(R.id.teacher_classroom_loading_text);

        classroomViewModel.readStudentsByClassroom();

        // se inicializa el observador
        initRecyclerViewObserver();
    }

    // inicializa el observador de la recyclerview
    private void initRecyclerViewObserver(){

        // se declara el observador del recyclerview
        final Observer<Event<List<StudentModel>>> studentsStateObserver = listEvent -> {

            // mientras se busca, se muestra el texto de cargando
            if(listEvent instanceof Event.Loading){
                Log.d(TAG, "Se estan buscando los estudiantes de la clase");
                resultText.setText(R.string.classroom_loading_string);
            }

            // cuando se encuentran los resultados, se muestran
            else if(listEvent instanceof Event.Success){
                Log.d(TAG, "Se encontraron los estudiantes de la clase");
                // se obtiene la info
                students = ((Event.Success<List<StudentModel>>) listEvent).getData();

                // actualiza la lista de datos en el adaptador
                classroomAdapter.setStudentsData(students);
                classroomAdapter.setStudentsFilteredData(students);

                // se actualiza la recyclerview
                handleStudents(students);
            }

            // en caso de error, se muestra al usuario
            else if(listEvent instanceof Event.Error){
                Log.d(TAG, "Hubo algun error buscando los estudiantes de la clase");
                // se muestra el error mediante un mensaje toast
                resultText.setVisibility(View.GONE);
                Toast.makeText(getActivity(),R.string.classroom_search_error,Toast.LENGTH_LONG).show();
            }
        };

        // se inicializa el observador
        classroomViewModel.getStudentsState().observe(getViewLifecycleOwner(), studentsStateObserver);
    }

    // metodos para implementar la searchview
    @Override
    public boolean onQueryTextSubmit(String s) {
        // no se implemente pues se ha decidido hacer la busqueda caracter a caracter
        // sin necesidad de clicar una posible lupa o darle al enter en el teclado
        return false;
    }

    // por cada letra buscada se realiza el filtrado
    @Override
    public boolean onQueryTextChange(String s) {
        Log.d(TAG, "Se recibe un cambio en el texto del buscador");
        // se buscan los estudiantes
        List<StudentModel> list = classroomAdapter.searchStudent(s.trim().toLowerCase());

        // actualiza la lista de datos en el adaptador
        classroomAdapter.setStudentsFilteredData(list);

        // se actualiza la recyclerview
        handleStudents(list);

        return false;
    }

    // maneja los resultados de la lista de estudiantes
    @SuppressLint("NotifyDataSetChanged")
    private void handleStudents(List<StudentModel> list){

        // en caso de lista vacia, se avisa al usuario
        if (list.isEmpty()){
            resultText.setVisibility(View.VISIBLE);
            resultText.setText(R.string.classroom_no_students_string);
        }

        // en caso de encontrar resultados, el texto informativo desaparece
        else resultText.setVisibility(View.GONE);

        // notifica al recycler view el cambio de datos
        classroomAdapter.notifyDataSetChanged();
    }
}
