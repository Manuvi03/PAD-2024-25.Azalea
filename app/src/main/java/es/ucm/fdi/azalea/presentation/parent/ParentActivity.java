package es.ucm.fdi.azalea.presentation.parent;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.integration.Event;
import es.ucm.fdi.azalea.presentation.editprofile.EditProfileFragment;
import es.ucm.fdi.azalea.presentation.showgrades.ShowGradesFragment;
import es.ucm.fdi.azalea.presentation.showgrades.ShowGradesViewModel;
import es.ucm.fdi.azalea.presentation.teacher.TeacherViewModel;

public class ParentActivity extends AppCompatActivity {

    // constantes
    private static String TAG = "ParentActivity";

    // atributos
    private BottomNavigationView bottomNavigationView;
    private ParentViewModel parentViewModel;
    private ParentShowGradesSharedViewModel sharedViewModel;

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

        // se obtiene el viewmodel
        parentViewModel = new ViewModelProvider(this).get(ParentViewModel.class);

        // se obtiene el viewmodel compartido
        sharedViewModel = new ViewModelProvider(this).get(ParentShowGradesSharedViewModel.class);

        // se obtiene la barra de navegacion
        bottomNavigationView = findViewById(R.id.parent_bottom_navigation_view);

        // restaura el fragmento que estaba activo antes de cambiar de configuracion
        if (savedInstanceState != null) {
            Fragment restoredFragment = getSupportFragmentManager().getFragment(savedInstanceState, "currentFragment");
            if (restoredFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.parent_fragment_container_view, restoredFragment)
                        .commit();
            }
        }
        else replaceFragment(ParentHomeFragmentActivity.class);

        bottomNavigationView.setOnItemSelectedListener(item->{
            if(item.getItemId() == R.id.teacher_navbar_home){
                replaceFragment(ParentHomeFragmentActivity.class);
            }
            else if(item.getItemId() == R.id.parent_navbar_notes) {

                // se busca al estudiante del profesor y se muestran sus notas
                parentViewModel.readUser();
                initObserver();
            }
            else if(item.getItemId()==R.id.teacher_navbar_profile){
                replaceFragment(EditProfileFragment.class);
            }
            return true;
        });

        //updateToken();
    }

    private void updateToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                String token = task.getResult();
                Log.i("IDTOKEN", token);
                ParentViewModel parentViewModel = new ParentViewModel();
                parentViewModel.updateToken(token);
            }
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

    private void replaceFragment(Class<? extends androidx.fragment.app.Fragment> c){
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.parent_fragment_container_view, c, null)
                .commit();
    }

    private void initObserver(){
        // se declara el observador de la info del usuario actual
        final Observer<Event<UserModel>> userInfoObserver = userEvent -> {
            if(userEvent instanceof Event.Loading){
                Log.d(TAG, "Se busca la informacion del usuario actual");
            }

            // cuando se encuentran los resultados, se pasan al fragmento
            else if(userEvent instanceof Event.Success){
                Log.d(TAG, "Se encontro la informacion del usuario actual");
                // se pasa la info al fragmento
                sharedViewModel.setStudentId(((Event.Success<UserModel>) userEvent).getData().getStudentId());
                // se cambia de fragmento
                replaceFragment(ShowGradesFragment.class);
            }

            // en caso de error, se muestra al usuario
            else if(userEvent instanceof Event.Error){
                Log.d(TAG, "Hubo algun error buscando la informacion del usuario actual");
                // se muestra el error mediante un mensaje toast
                Toast.makeText(this,R.string.edit_profile_search_error,Toast.LENGTH_LONG).show();
            }
        };

        // se inicializa el observador
        parentViewModel.getUserState().observe(this, userInfoObserver);
    }
}
