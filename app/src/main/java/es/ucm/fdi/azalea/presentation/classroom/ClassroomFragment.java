package es.ucm.fdi.azalea.presentation.classroom;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import es.ucm.fdi.azalea.business.model.StudentModel;

public class ClassroomFragment extends Fragment {

    private ClassroomFragmentBinding binding;
    private ClassroomListAdapter classroomAdapter;
    private List<StudentModel> studentsState = null;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        binding = ClassroomFragmentBinding.inflate(inflater, container, false);

        // se encuentra y ajusta la recycler view
        RecyclerView recyclerView = binding.content.recyclew_view;
        recyclerView.setHasFixedSize(true);
        // como es un fragment, se obtiene su activity (context) asociada
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // se crea el adaptador de la RecyclerView, de momento con una lista vacia
        classroomAdapter = new ClassroomListAdapter(Collections.emptyList());
        recyclerView.setAdapter(classroomAdapter);

        // al inicar este fragment, directamente aparece la lista de estudiantes
        classroomViewModel classroomViewModel = new ViewModelProvider(this).get(classroomViewModel.class);
        classroomViewModel.getStudentsState().observe(getActivity(), studentsState ->{
            // actualiza la lista de datos en el adaptador
            resultText.setText(R.string.cargando_string);
            classroomAdapter.setStudentsData(studentsState);
            if (studentsState == null) {
                resultText.setText(R.string.nullResultado_string);
            } else {
                resultText.setText(R.string.resultado_string);
            }
            // notifica al recycler view el cambio de datos
            classroomAdapter.notifyDataSetChanged();
        });



        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
