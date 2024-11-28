package es.ucm.fdi.azalea.presentation.parent;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.presentation.editstudent.EditStudentFragment;
import es.ucm.fdi.azalea.presentation.showgrades.ShowGradesFragment;

public class ParentActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.parent_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.parentActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomNavigationView = findViewById(R.id.parent_bottom_navigation_view);
        replaceFragment(ParentHomeFragmentActivity.class);
        bottomNavigationView.setOnItemSelectedListener(item->{
            if(item.getItemId() == R.id.teacher_navbar_home){
                replaceFragment(ParentHomeFragmentActivity.class);
            }
            else if(item.getItemId() == R.id.parent_navbar_notes) {
                replaceFragment(ShowGradesFragment.class);
            }
            else if(item.getItemId()==R.id.teacher_navbar_profile){
                replaceFragment(EditStudentFragment.class);
            }
            return true;
        });
    }


    private void replaceFragment(Class<? extends androidx.fragment.app.Fragment> c){
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.parent_fragment_container_view, c, null)
                .commit();
    }

}
