 package es.ucm.fdi.azalea.presentation.login;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.model.Subject;
import es.ucm.fdi.azalea.business.Repositories.SubjectRepository;

 public class MainActivity extends AppCompatActivity {

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

        start_animations();

        // estoy probando el firebase database
        Subject subject1 = new Subject("Maths");
        Subject subject2 = new Subject("PE");
        Subject modifiedSubject = new Subject("English");

        SubjectRepository sm = new SubjectRepository();

        String key1 = sm.create(subject1);
        String key2 = sm.create(subject2);

        sm.readAll();

        // investigando algunos guardan la key en el transfer otros no... no entiendo como hacerlo
        // bien esto, de momento la devuelvo del create para ir probando
        sm.update(key1, modifiedSubject);

        sm.readAll();

        sm.delete(key2);

        // todos estos mensajes salen por consola
        sm.readAll();


    }

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