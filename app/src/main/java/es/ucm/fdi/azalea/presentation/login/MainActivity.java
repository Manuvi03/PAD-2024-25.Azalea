 package es.ucm.fdi.azalea.presentation.login;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.jakewharton.threetenabp.AndroidThreeTen;

import es.ucm.fdi.azalea.R;

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

        FirebaseApp.initializeApp(this);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        Log.d(TAG, "onCreate: ");
        AndroidThreeTen.init(this); //inicializacion de la libreria de fecha
        replaceFragment(LoginFragment.class);


    }

     private void replaceFragment(Class<? extends androidx.fragment.app.Fragment> c){
         getSupportFragmentManager().beginTransaction()
                 .setReorderingAllowed(true)
                 .replace(R.id.login_fragment_container, c, null).commit();

     }



 }