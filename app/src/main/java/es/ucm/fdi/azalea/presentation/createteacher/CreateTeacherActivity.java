package es.ucm.fdi.azalea.presentation.createteacher;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import es.ucm.fdi.azalea.databinding.ActivityMainBinding;
import es.ucm.fdi.azalea.databinding.CreateTeacherActivityBinding;

public class CreateTeacherActivity extends AppCompatActivity {
    private CreateTeacherActivityBinding binding;

    private CreateTeacherViewModel createTeacherViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CreateTeacherActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        createTeacherViewModel = new ViewModelProvider(this).get(CreateTeacherViewModel.class);
    }
}
