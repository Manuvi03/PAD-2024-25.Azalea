package es.ucm.fdi.azalea.presentation.createteacher;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import es.ucm.fdi.azalea.databinding.ActivityMainBinding;
import es.ucm.fdi.azalea.databinding.CreateTeacherActivityBinding;

public class CreateTeacherActivity extends AppCompatActivity {
    private CreateTeacherActivityBinding binding;

    private CreateTeacherViewModel createTeacherViewModel;

    private EditText nameEditText, surnameEditText,mailEditText,genderEditText,passwordEditText;




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

    }
}
