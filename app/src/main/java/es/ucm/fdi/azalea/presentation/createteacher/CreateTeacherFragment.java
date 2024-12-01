package es.ucm.fdi.azalea.presentation.createteacher;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.databinding.CreateTeacherFragmentBinding;
import es.ucm.fdi.azalea.integration.Event;
import es.ucm.fdi.azalea.presentation.login.LoginFragment;
import es.ucm.fdi.azalea.presentation.login.MainActivity;
import es.ucm.fdi.azalea.presentation.teacher.TeacherActivity;

public class CreateTeacherFragment extends Fragment {
    private static String TAG = "CreateTeacherFragment";
    private CreateTeacherFragmentBinding binding;

    private CreateTeacherViewModel createTeacherViewModel;
    private View viewRoot;

    private EditText nameEditText, surnameEditText,mailEditText,genderEditText,passwordEditText,classRoomNameEditText;
    private Button createTeacher_button;
    FrameLayout loadingView;





    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        binding = CreateTeacherFragmentBinding.inflate(inflater,container,false);
        viewRoot = binding.getRoot();

        //al crear el viewModel le paso this ya que quiero que este viewModel sea unico para el fragment,
        //asi se destruye al destruirse el fragment, de la otra manera el viewModel no se destruye y
        // al volver al abrir el fragment se guarda el event anterior con los datos de ese event
        createTeacherViewModel = new ViewModelProvider(this).get(CreateTeacherViewModel.class);
        nameEditText = binding.createTeacherNameEditText;
        surnameEditText = binding.createTeacherSurnamesEditText;
        genderEditText = binding.createTeacherGenderEditText;
        mailEditText = binding.createTeacherEmailEditText;
        passwordEditText = binding.createTeacherPasswordEditText;
        createTeacher_button = binding.createTeacherButton;
        loadingView = binding.loadingOverlay;
        classRoomNameEditText = binding.createTeacherClassRoomNameEditText;

        initListeners();
        return viewRoot;
    }

    @Override
    public void onResume() {
        super.onResume();
        // el Fragment puede verse en ambas orientaciones
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    @Override
    public void onPause() {
        super.onPause();
        // restablece la orientacion a la de la activity
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void initListeners(){
        createTeacherViewModel.getAuthenticateTeacherEvent().observe(getViewLifecycleOwner(),booleanEvent -> {
            if(booleanEvent instanceof Event.Success){
                //si confirmamos que no existe en la bd (no esta registrado) lo registramos
                loadingView.setVisibility(View.GONE);
                createTeacherViewModel.createTeacher(createTeacherViewModel.getUserdata().getValue(),classRoomNameEditText.getText().toString());

            }
            else if(booleanEvent instanceof Event.Error){
                loadingView.setVisibility(View.GONE);
                Toast.makeText(requireActivity(),R.string.createteacher_teacherAuthError,Toast.LENGTH_LONG).show();
            }
            else{
                loadingView.setVisibility(View.VISIBLE);
            }
        });

        createTeacherViewModel.getCreateTeacherEvent().observe(getViewLifecycleOwner(),event ->{
            if(event instanceof Event.Success){
                loadingView.setVisibility(View.GONE);
                Toast.makeText(requireActivity(),getString(R.string.ClassRoomName_successToast), Toast.LENGTH_LONG).show();
                Intent switchToTeacher = new Intent(requireActivity(), TeacherActivity.class);
                Log.d(TAG,"profesor creado correctamente");
                requireActivity().startActivity(switchToTeacher);
                //la activity de log in termina ya que no queremos que se pueda volver al log in
                // una vez se ha iniciado sesion
                requireActivity().finish();
            }else if(event instanceof Event.Error){
                loadingView.setVisibility(View.GONE);
                if(viewRoot.getContext() != null)
                    Toast.makeText(viewRoot.getContext(),getString(R.string.ClassRoomName_error),Toast.LENGTH_LONG).show();
            }else{
                loadingView.setVisibility(View.VISIBLE);
            }
        });



        createTeacher_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserModel userdata = new UserModel();
                if(allFilled(nameEditText,surnameEditText,genderEditText,mailEditText,passwordEditText,classRoomNameEditText)){

                    //funcoon auxiliar del viewModel para comprobar que la contrasenya y el mail estan
                    //correctos
                    Event<String> verifiedEvent = createTeacherViewModel.verify(mailEditText.getText().toString()
                            ,passwordEditText.getText().toString());
                    if(verifiedEvent instanceof Event.Success){
                        userdata.setPassword(passwordEditText.getText().toString());
                        userdata.setName(nameEditText.getText().toString());
                        userdata.setSurname(surnameEditText.getText().toString());
                        userdata.setGender(genderEditText.getText().toString());
                        userdata.setEmail(mailEditText.getText().toString());
                        userdata.setParent(false);
                        createTeacherViewModel.setUserdata(userdata, requireActivity());
                        createTeacherViewModel.authenticateTeacher(userdata.getEmail(),userdata.getPassword());
                    }
                    else{
                        // en este caso no puede ser nunca Event.loading ya que la funcion manda
                        // o un error o un success
                        Event.Error<String> error = (Event.Error<String>) verifiedEvent;
                        if(error.getErrorData().equalsIgnoreCase("MailError")){
                            mailEditText.setError(getString(R.string.createteacher_mailFormatError));
                        }else if(error.getErrorData().equalsIgnoreCase("PasswordError")){
                            passwordEditText.setError(getString(R.string.createteacher_passwordFormatError));
                        }
                    }

                }

            }
        });
    }


    // los puntos suspensivos permiten pasar varios argumentos
    private boolean allFilled(EditText... fields){
        boolean filled = true;
        for(EditText field: fields){
            //el trim detecta que si hay espacios en el string tambien lo detecte como empty
            if(field.getText().toString().trim().isEmpty()){
                field.setError(getString(R.string.createteacher_filledFieldError));
                filled = false;
            }
        }

        return filled;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;

    }

}
