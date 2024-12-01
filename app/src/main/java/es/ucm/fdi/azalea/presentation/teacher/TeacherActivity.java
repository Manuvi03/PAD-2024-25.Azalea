package es.ucm.fdi.azalea.presentation.teacher;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.presentation.classroom.ClassroomFragment;
import es.ucm.fdi.azalea.presentation.editprofile.EditProfileFragment;

public class TeacherActivity extends AppCompatActivity {

    // atributos de componentes de la vista
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.teacher_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.teacher_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // se encuentra la barra de navegacion
        bottomNavigationView = findViewById(R.id.teacher_bottom_navigation_view);


        // restaura el fragmento que estaba activo antes de cambiar de configuracion
        if (savedInstanceState != null) {
            Fragment restoredFragment = getSupportFragmentManager().getFragment(savedInstanceState, "currentFragment");
            if (restoredFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.teacher_fragment_container_view, restoredFragment)
                        .commit();
            }
        }
        // primero se muestra el fragmento predeterminado, el home
        else replaceFragment(TeacherHomeFragment.class);


        // se implemente la barra de navegacion para cambiar de fragmento
        bottomNavigationView.setOnItemSelectedListener(item -> {
            for(int i =0; i < getSupportFragmentManager().getBackStackEntryCount(); i++){
                getSupportFragmentManager().popBackStack();
            }
            // se reemplazan los fragmentos por los correspondientes
            if(item.getItemId() == R.id.teacher_navbar_home){
                replaceFragment(TeacherHomeFragment.class);
            }
            else if(item.getItemId() == R.id.teacher_navbar_classroom) {
                replaceFragment(ClassroomFragment.class);
            }
            // en caso de actividades, se incian
            else if(item.getItemId() == R.id.teacher_navbar_profile) {
                replaceFragment(EditProfileFragment.class);
            }
            return true;
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // se guarda el fragmento actual
        if (!getSupportFragmentManager().getFragments().isEmpty()) {
            Fragment currentFragment = getSupportFragmentManager().getFragments().get(0);
            getSupportFragmentManager().putFragment(outState, "currentFragment", currentFragment);
        }
    }

    // reemplaza el fragmento actual por el seleccionado en la barra de navegacion
    public void replaceFragment(Class<? extends androidx.fragment.app.Fragment> c){
        Fragment fragment = null;
        try {
            fragment = c.newInstance(); // Instanciar el fragmento
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.teacher_fragment_container_view, fragment, null)
                .commit();
    }
}