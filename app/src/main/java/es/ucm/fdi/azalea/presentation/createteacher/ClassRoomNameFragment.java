package es.ucm.fdi.azalea.presentation.createteacher;

import android.content.Intent;
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

import com.google.android.material.tabs.TabLayout;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.databinding.TeacherClassroomNameFragmentBinding;
import es.ucm.fdi.azalea.integration.Event;
import es.ucm.fdi.azalea.presentation.login.LoginViewModel;
import es.ucm.fdi.azalea.presentation.teacher.teacherActivity;

public class ClassRoomNameFragment extends Fragment {
    private static String TAG = "ClassRoomNameFragment";

    TeacherClassroomNameFragmentBinding binding;
    CreateTeacherViewModel viewModel;

    EditText classroomnameEditText;
    Button acceptName_Button;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = TeacherClassroomNameFragmentBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        viewModel = new ViewModelProvider(requireActivity()).get(CreateTeacherViewModel.class);

        classroomnameEditText = binding.editTextTextClassRoomName;
        acceptName_Button = binding.acceptClassRoomNameButton;

        initListeners();

        return view;

    }

    private void initListeners(){

        viewModel.getCreateTeacherEvent().observe(requireActivity(),event ->{
            if(event instanceof Event.Success){
                Toast.makeText(requireActivity(),getString(R.string.ClassRoomName_successToast), Toast.LENGTH_LONG);
                Intent switchToTeacher = new Intent(requireActivity(), teacherActivity.class);
                Log.d(TAG,"profesor creado correctamente");
                requireActivity().startActivity(switchToTeacher);
                //la activity de log in termina ya que no queremos que se pueda volver al log in
                // una vez se ha iniciado sesion
                requireActivity().finish();
            }else if(event instanceof Event.Error){
                Toast.makeText(requireActivity(),getString(R.string.ClassRoomName_error),Toast.LENGTH_LONG).show();
            }else{

            }
        });

        acceptName_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(classroomnameEditText.getText().toString().trim().isEmpty()){
                   classroomnameEditText.setError(getString(R.string.createteacher_filledFieldError));
               }else{

                viewModel.createTeacher(viewModel.getUserdata().getValue(),classroomnameEditText.getText().toString());

               }

            }
        });

    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }
}
