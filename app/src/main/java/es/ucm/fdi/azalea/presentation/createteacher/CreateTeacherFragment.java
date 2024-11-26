package es.ucm.fdi.azalea.presentation.createteacher;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.databinding.CreateTeacherFragmentBinding;
import es.ucm.fdi.azalea.integration.Event;
import es.ucm.fdi.azalea.presentation.login.MainActivity;

public class CreateTeacherFragment extends Fragment {
    private CreateTeacherFragmentBinding binding;

    private CreateTeacherViewModel createTeacherViewModel;

    private EditText nameEditText, surnameEditText,mailEditText,genderEditText,passwordEditText;
    private Button createTeacher_button;





    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        binding = CreateTeacherFragmentBinding.inflate(inflater,container,false);
        View view = binding.getRoot();


        createTeacherViewModel = new ViewModelProvider(requireActivity()).get(CreateTeacherViewModel.class);
        nameEditText = binding.createTeacherNameEditText;
        surnameEditText = binding.createTeacherSurnamesEditText;
        genderEditText = binding.createTeacherGenderEditText;
        mailEditText = binding.createTeacherEmailEditText;
        passwordEditText = binding.createTeacherPasswordEditText;
        createTeacher_button = binding.createTeacherButton;

        initListeners();
        return view;
    }


    private void initListeners(){
        createTeacherViewModel.getAuthenticateTeacherEvent().observe(requireActivity(),booleanEvent -> {
            if(booleanEvent instanceof Event.Success){
                //LLama directamente al supportFragmentManager para cambiar
                requireActivity().getSupportFragmentManager().beginTransaction().setReorderingAllowed(true
                ).replace(R.id.login_fragment_container, ClassRoomNameFragment.class,null).commit();
            }
            else if(booleanEvent instanceof Event.Error){
                Toast.makeText(requireActivity(),R.string.createteacher_teacherAuthError,Toast.LENGTH_LONG).show();
            }
            else{
                //TODO crear los loadingView
            }
        });



        createTeacher_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserModel userdata = new UserModel();
                if(allFilled(nameEditText,surnameEditText,genderEditText,mailEditText,passwordEditText)){

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
                        createTeacherViewModel.setUserdata(userdata);
                        createTeacherViewModel.authenticateTeacher(userdata.getEmail(),userdata.getPassword());
                    }
                    else{
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



}
