package es.ucm.fdi.azalea.presentation.createteacher;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.databinding.ActivityMainBinding;
import es.ucm.fdi.azalea.databinding.CreateTeacherActivityBinding;
import es.ucm.fdi.azalea.integration.Event;

public class CreateTeacherActivity extends AppCompatActivity {
    private CreateTeacherActivityBinding binding;

    private CreateTeacherViewModel createTeacherViewModel;

    private EditText nameEditText, surnameEditText,mailEditText,genderEditText,passwordEditText;
    private Button createTeacher_button;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CreateTeacherActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        createTeacherViewModel = new ViewModelProvider(this).get(CreateTeacherViewModel.class);
        nameEditText = binding.createTeacherNameEditText;
        surnameEditText = binding.createTeacherEmailEditText;
        genderEditText = binding.createTeacherGenderEditText;
        mailEditText = binding.createTeacherEmailEditText;
        passwordEditText = binding.createTeacherPasswordEditText;
        createTeacher_button = binding.createTeacherButton;

        initListeners();

    }


    private void initListeners(){

        createTeacherViewModel.getCreateTeacherEvent().observe(this, event->{
            if(event instanceof Event.Success){

            }
            else if(event instanceof Event.Error){

            }
            else{

            }
        });

        createTeacher_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserModel userdata = new UserModel();
                if(allFilled(nameEditText,surnameEditText,genderEditText,mailEditText,passwordEditText)){
                    userdata.setPassword(passwordEditText.getText().toString());
                    userdata.setName(nameEditText.getText().toString());userdata.setSurname(surnameEditText.getText().toString());
                    userdata.setSurname(surnameEditText.getText().toString());
                    userdata.setGender(genderEditText.getText().toString());
                    userdata.setEmail(mailEditText.getText().toString());
                    createTeacherViewModel.createTeacher(userdata);

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
