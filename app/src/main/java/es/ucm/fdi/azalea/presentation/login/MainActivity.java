 package es.ucm.fdi.azalea.presentation.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.FirebaseApp;
import com.jakewharton.threetenabp.AndroidThreeTen;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.presentation.teacher.TeacherActivity;

 public class MainActivity extends AppCompatActivity {

     private final static String TAG = "MainActivity";

     private LoginViewModel loginViewModel;


     private EditText mailEditText;
     private EditText passwordEditText;
     private Button loginButton;
     private TextView recoverPasswordText;
     private Button createTeacherButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        Intent in = new Intent(this, TeacherActivity.class);
//        startActivity(in);

        FirebaseApp.initializeApp(this);
        Log.d(TAG, "onCreate: ");
        AndroidThreeTen.init(this); //inicializacion de la libreria de fecha

        // restaura el fragmento que estaba activo antes de cambiar de configuracion
        if (savedInstanceState != null) {
            Fragment restoredFragment = getSupportFragmentManager().getFragment(savedInstanceState, "currentFragment");
            if (restoredFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.login_fragment_container, restoredFragment)
                        .commit();
            }
        }
        else replaceFragment(LoginFragment.class);


    }
     @Override
     protected void onSaveInstanceState(@NonNull Bundle outState) {
         super.onSaveInstanceState(outState);
         // Guarda el fragmento actual (si usas fragmentos).
         if (!getSupportFragmentManager().getFragments().isEmpty()) {
             Fragment currentFragment = getSupportFragmentManager().getFragments().get(0);
             getSupportFragmentManager().putFragment(outState, "currentFragment", currentFragment);
         }
     }
     private void replaceFragment(Class<? extends androidx.fragment.app.Fragment> c){
         getSupportFragmentManager().beginTransaction()
                 .setReorderingAllowed(true)
                 .replace(R.id.login_fragment_container, c, null).commit();

     }



 }