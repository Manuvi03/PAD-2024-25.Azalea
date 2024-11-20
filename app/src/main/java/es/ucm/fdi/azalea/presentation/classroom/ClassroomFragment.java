package es.ucm.fdi.azalea.presentation.classroom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.integration.Event;

public class ClassroomFragment extends Fragment {

    // atributos de la vista
    private RecyclerView classroomList;
    private TextView resultText;

    // atributos para la recyclerview
    private ClassroomListAdapter classroomAdapter;              // el adaptador
    private Event<List<StudentModel>> studentsState = null;     // la info

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.teacher_classroom_fragment, container, false);

        // se encuentra y ajusta la recycler view
        classroomList = view.findViewById(R.id.teacher_classroom_recyclerview);
        classroomList.setHasFixedSize(true);

        // como es un fragment, se obtiene su activity (context) asociada
        classroomList.setLayoutManager(new LinearLayoutManager(getActivity()));

        // se crea el adaptador de la RecyclerView, de momento con una lista vacia
        classroomAdapter = new ClassroomListAdapter(Collections.emptyList(), getActivity());
        classroomList.setAdapter(classroomAdapter);

        // al inicar este fragment, directamente aparece la lista de estudiantes
        resultText = view.findViewById(R.id.teacher_classroom_loading_text);

        // se obtiene el viewmodel
        classroomViewModel classroomViewModel = new ViewModelProvider(this).get(classroomViewModel.class);
        classroomViewModel.readStudentsByClassroom("2");

        // se inicializa el observador
        classroomViewModel.getStudentsState().observe(getActivity(), studentsState ->{

            if(studentsState instanceof Event.Loading){

            }
            else if(studentsState instanceof Event.Success){
                // se obtiene la info
                List<StudentModel> students = ((Event.Success<List<StudentModel>>) studentsState).getData();

                // actualiza la lista de datos en el adaptador
                resultText.setText(R.string.classroom_loading_string);
                classroomAdapter.setStudentsData(students);
                if (students.isEmpty())
                    resultText.setText(R.string.classroom_no_students_string);
                else
                    resultText.setVisibility(View.GONE);
                // notifica al recycler view el cambio de datos
                classroomAdapter.notifyDataSetChanged();
            }
            else if(studentsState instanceof Event.Error){
                //muestra un toast de error (se puede cambiar en un futuro)
                Toast.makeText(getActivity(),R.string.classroom_no_students_string,Toast.LENGTH_LONG).show();
            }

        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
