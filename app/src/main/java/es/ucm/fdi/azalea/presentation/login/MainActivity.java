 package es.ucm.fdi.azalea.presentation.login;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.integration.Event;
import es.ucm.fdi.azalea.presentation.createteacher.CreateTeacherActivity;
import es.ucm.fdi.azalea.presentation.parent.parentActivity;
import es.ucm.fdi.azalea.presentation.passwordrecovery.PasswordRecoveryActivity;
import es.ucm.fdi.azalea.presentation.teacher.teacherActivity;

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
        // inicializamos el viewModel
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        mailEditText = findViewById(R.id.editTextEmailAddress);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.button_login);
        recoverPasswordText = findViewById(R.id.textViewRecoverPassword);
        createTeacherButton = findViewById(R.id.buttonCreateTeacherAccount);

        initListeners();

        start_animations();



    }

    private void initListeners(){
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginViewModel.doLogIn(mailEditText.getText().toString(),passwordEditText.getText().toString());
            }
        });

        recoverPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchActivityIntent = new Intent(MainActivity.this, PasswordRecoveryActivity.class);
                startActivity(switchActivityIntent);
            }
        });

        createTeacherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchActivityIntent = new Intent(MainActivity.this, CreateTeacherActivity.class);
                startActivity(switchActivityIntent);
            }

        });

        //tambien declaramos la mainActivity como observer de LoginViewModel

        loginViewModel.getLoginEvent().observe(this,event ->{
            if(event instanceof Event.Loading){
                //TODO logica de carga de usuario
            }else if(event instanceof Event.Success){
                Intent switchActivityIntent;
                //TODO logica de cambiar de vista, dependiendo de si el usuario es profesor o padre tendra que cambiar de vista, para hacer
                //TODO esto deberia leer el usuario de la base de datos con el uid de la clase user de Firebase y leer el booleano de la bd
                Log.d(TAG,"el usuario ha iniciado sesion correctamente");
                UserModel userdata = ((Event.Success<UserModel>) event).getData();
                if(userdata.getParent()){
                    Log.d(TAG, "el usuario es padre");
                    switchActivityIntent = new Intent(this, parentActivity.class);
                }
                else{
                    Log.d(TAG, "el usuario es profesor");
                    switchActivityIntent = new Intent(this, teacherActivity.class);
                }

                startActivity(switchActivityIntent);

            }else if(event instanceof Event.Error){
                //muestra un toast de error (se puede cambiar en un futuro)
                Toast.makeText(this,R.string.login_error,Toast.LENGTH_LONG).show();
            }
        });
    }

     private void replaceFragment(Class<? extends androidx.fragment.app.Fragment> c){
         getSupportFragmentManager().beginTransaction()
                 .setReorderingAllowed(true)
                 .replace(R.id.teacher_fragment_container_view, c, null)
                 .commit();
     }

    //<-----------------    Animation   -------------------->

    private void start_animations(){
        ImageView circleImageView = findViewById(R.id.circle);
        TextView email = findViewById(R.id.editTextEmailAddress);
        TextView contrasenya = findViewById(R.id.editTextPassword);
        TextView textViewTitle = findViewById(R.id.textViewTitle);
        TextView textViewRecoverPassword = findViewById(R.id.textViewRecoverPassword);
        TextView textViewProfesor = findViewById(R.id.textViewProfesor);
        CardView cardView = findViewById(R.id.roundedCardView);
        Button button_login = findViewById(R.id.button_login);
        Button button_create = findViewById(R.id.buttonCreateTeacherAccount);

        ObjectAnimator contrasenya_animator = alpha_animation(contrasenya);
        ObjectAnimator email_animator = alpha_animation(email);
        ObjectAnimator cardView_animator = alpha_animation(cardView);
        ObjectAnimator buttonlogin_animator = alpha_animation(button_login);
        ObjectAnimator buttoncreate_animator = alpha_animation(button_create);
        ObjectAnimator textViewTitle_animator = alpha_animation(textViewTitle);
        ObjectAnimator textViewRecoverPassword_animator = alpha_animation(textViewRecoverPassword);
        ObjectAnimator textViewProfesor_animator = alpha_animation(textViewProfesor);

        circle_animation(circleImageView);
        contrasenya_animator.start();
        email_animator.start();
        cardView_animator.start();
        buttonlogin_animator.start();
        buttoncreate_animator.start();
        textViewTitle_animator.start();
        textViewRecoverPassword_animator.start();
        textViewProfesor_animator.start();
    }

     private void circle_animation(ImageView circleImageView) {
         int screenWidth = getResources().getDisplayMetrics().widthPixels;

         // Establecer el tamaño del ImageView al ancho de la pantalla
         circleImageView.getLayoutParams().width = screenWidth;
         circleImageView.getLayoutParams().height = screenWidth; // El diámetro es igual al ancho de la pantalla
         circleImageView.requestLayout(); // Forzar el ajuste del tamaño

         // Establecer el recurso de círculo
         circleImageView.setImageResource(R.drawable.circle_shape);

         // Obtener la altura total de la pantalla
         int screenHeight = getResources().getDisplayMetrics().heightPixels;

         // Establecer la posición inicial del ImageView fuera de la pantalla (en la parte inferior)
         circleImageView.setTranslationY(screenHeight); // Coloca el círculo fuera de la pantalla en la parte inferior

         // Animación para mover el círculo desde abajo hacia arriba, cortado a la mitad
         ObjectAnimator animator = ObjectAnimator.ofFloat(circleImageView, "translationY", -screenWidth / 2f); // Mover a la mitad superior del círculo fuera de la vista
         animator.setDuration(2000); // 2 segundos para la animación
         animator.setInterpolator(new AccelerateDecelerateInterpolator());
         animator.start();
     }

     private ObjectAnimator alpha_animation(TextView textView) {
         ObjectAnimator animator = ObjectAnimator.ofFloat(textView, "alpha", 0f, 1f);
         animator.setDuration(3500); // Duración de la animación en milisegundos
         return animator;
     }
     private ObjectAnimator alpha_animation(CardView cardView) {
         ObjectAnimator animator = ObjectAnimator.ofFloat(cardView, "alpha", 0f, 1f);
         animator.setDuration(3500); // Duración de la animación en milisegundos
         return animator;
     }

 }