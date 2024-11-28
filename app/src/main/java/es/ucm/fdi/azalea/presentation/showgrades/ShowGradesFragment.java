package es.ucm.fdi.azalea.presentation.showgrades;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.model.MarkModel;
import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.integration.Event;

public class ShowGradesFragment extends Fragment {

    // constantes
    private final String TAG = "ShowGradesFragment";

    // atributos

    // view
    private View view;

    // view model
    private ShowGradesViewModel showGradesViewModel;

    // de la vista
    private ImageView studentProfileImage;
    private TextView studentNameText;
    private TextView resultText;
    private RecyclerView marksList;

    // info de la vista
    private StudentModel studentInfo;
    private String studentId;
    private String studentImage;

    // para la recyclerview
    private ShowGradesListAdapter showGradesAdapter;    // su adaptador
    private List<MarkModel> marks;                      // la info

    // para compartir la informacion necesaria en el entre el StudentFragment y el actual
    StudentShowGradesSharedViewModel showGradesSharedViewModel;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // se genera el viewmodel compartido y se observan los valores que se necesitan pasar
        showGradesSharedViewModel = new ViewModelProvider((FragmentActivity) view.getContext()).get(StudentShowGradesSharedViewModel.class);
        showGradesSharedViewModel.getStudentId().observe((FragmentActivity) view.getContext(), data -> {
            Log.d(TAG, "StudentId recibido");
            studentId = data;
        });
        showGradesSharedViewModel.getStudentProfileImage().observe((FragmentActivity) view.getContext(), data -> {
            Log.d(TAG, "StudentImage recibido");
            studentImage = data;
        });
        // se obtiene el viewmodel
        showGradesViewModel = new ViewModelProvider(this).get(ShowGradesViewModel.class);

        // se enlazan los componentes necesarios
        bindComponents();

        // se inciializa la info del estudiante
        showGradesViewModel.readStudent(studentId);
        initStudentInfoObserver();

        // se inicializa la recyclerview
        initRecyclerView();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // se enlaza la vista a la clase
        view = inflater.inflate(R.layout.show_grades_fragment, container, false);
        Log.d(TAG, "Se ha creado el ShowGradesFragment");

        return view;
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "Se destruye el ShowGradesFragment");
        super.onDestroyView();
    }

    // se enlazan los componentes de la vista
    private void bindComponents(){
        studentProfileImage = view.findViewById(R.id.show_grades_fragment_profile_image);
        studentNameText = view.findViewById(R.id.show_grades_fragment_name_textView);
        resultText = view.findViewById(R.id.show_grades_loading_text);

        // se encuentra y ajusta la recycler view
        marksList = view.findViewById(R.id.show_grades_fragment_recyclerview);
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
        showGradesViewModel.getStudentState().observe(getViewLifecycleOwner(), studentInfoObserver);
    }

    // actualiza la textview con el nombre del estudiante
    @SuppressLint("SetTextI18n")
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

    // manejo de la recyclerview
    private void initRecyclerView(){
        // como es un fragment, se obtiene su activity (context) asociada
        marksList.setHasFixedSize(true);
        marksList.setLayoutManager(new LinearLayoutManager(getActivity()));

        // se crea el adaptador de la RecyclerView, de momento con una lista vacia
        showGradesAdapter = new ShowGradesListAdapter(Collections.emptyList(), getActivity());
        marksList.setAdapter(showGradesAdapter);

        // se busca la lista de notas
        showGradesViewModel.readMarksByStudentId(studentId);

        // se inicializa el observador
        initRecyclerViewObserver();
    }

    // inicializa el observador de la recyclerview
    private void initRecyclerViewObserver(){

        // se declara el observador del recyclerview
        final Observer<Event<List<MarkModel>>> marksStateObserver = listEvent -> {

            // mientras se busca, se muestra el texto de cargando
            if(listEvent instanceof Event.Loading){
                Log.d(TAG, "Se estan buscando las notas del estudiante");
                resultText.setText(R.string.classroom_loading_string);
            }

            // cuando se encuentran los resultados, se muestran
            else if(listEvent instanceof Event.Success){
                Log.d(TAG, "Se encontraron las notas del estudiante");
                // se obtiene la info
                marks = ((Event.Success<List<MarkModel>>) listEvent).getData();

                // actualiza la lista de datos en el adaptador
                showGradesAdapter.setStudentsData(marks);

                // se actualiza la recyclerview
                handleMarks(marks);
            }

            // en caso de error, se muestra al usuario
            else if(listEvent instanceof Event.Error){
                Log.d(TAG, "Hubo algun error buscando los estudiantes de la clase");
                // se muestra el error mediante un mensaje toast
                resultText.setVisibility(View.GONE);
                Toast.makeText(getActivity(),((Event.Error<List<MarkModel>>) listEvent).getException().getMessage(),Toast.LENGTH_LONG).show();
            }
        };

        // se inicializa el observador
        showGradesViewModel.getMarksState().observe(getViewLifecycleOwner(), marksStateObserver);
    }

    // maneja los resultados de la lista de estudiantes
    @SuppressLint("NotifyDataSetChanged")
    private void handleMarks(List<MarkModel> list){

        // en caso de lista vacia, se avisa al usuario
        if (list.isEmpty()){
            resultText.setVisibility(View.VISIBLE);
            resultText.setText(R.string.show_grades_no_marks);
        }

        // en caso de encontrar resultados, el texto informativo desaparece
        else resultText.setVisibility(View.GONE);

        // notifica al recycler view el cambio de datos
        showGradesAdapter.notifyDataSetChanged();
    }
}
