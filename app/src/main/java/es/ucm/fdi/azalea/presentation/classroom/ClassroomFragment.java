package es.ucm.fdi.azalea.presentation.classroom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.model.StudentModel;

public class ClassroomFragment extends Fragment {

    // atributos de la vista
    private RecyclerView classroomList;
    private TextView resultText;

    // atributos para la recyclerview
    private ClassroomListAdapter classroomAdapter;      // el adaptador
    private List<StudentModel> studentsState = null;    // la info

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.teacher_classroom_fragment, container, false);

        /*
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
        classroomViewModel classroomViewModel = new ViewModelProvider(this).get(classroomViewModel.class);
        classroomViewModel.getStudentsState().observe(getActivity(), studentsState ->{
            // actualiza la lista de datos en el adaptador
            resultText.setText(R.string.classroom_loading_string);
            classroomAdapter.setStudentsData(studentsState);
            if (studentsState == null)
                resultText.setText(R.string.classroom_no_students_string);
            // notifica al recycler view el cambio de datos
            classroomAdapter.notifyDataSetChanged();
        });
*/
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
