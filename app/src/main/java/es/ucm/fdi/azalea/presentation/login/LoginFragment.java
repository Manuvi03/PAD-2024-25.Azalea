package es.ucm.fdi.azalea.presentation.login;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.databinding.LoginFragmentBinding;
import es.ucm.fdi.azalea.integration.Event;
import es.ucm.fdi.azalea.presentation.createteacher.CreateTeacherFragment;
import es.ucm.fdi.azalea.presentation.parent.ParentActivity;
import es.ucm.fdi.azalea.presentation.passwordrecovery.PasswordRecoveryFragment;
import es.ucm.fdi.azalea.presentation.teacher.TeacherActivity;


public class LoginFragment extends Fragment {

    private final static String TAG = "LoginFragment";
    private LoginFragmentBinding loginBinding;
    private LoginViewModel loginViewModel;
    private View viewRoot;
    private EditText mailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView recoverPasswordText;
    private Button createTeacherButton;
    private FrameLayout loadingView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        loginBinding = LoginFragmentBinding.inflate(inflater,container,false);
        viewRoot = loginBinding.getRoot();

        //inicializamos el ViewModel con la activity (MainActivity) para compartir el mismo ViewModel entre fragments

        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        mailEditText = loginBinding.editTextEmailAddress;
        passwordEditText = loginBinding.editTextPassword;
        loginButton = loginBinding.buttonLogin;
        recoverPasswordText = loginBinding.textViewRecoverPassword;
        createTeacherButton = loginBinding.buttonCreateTeacherAccount;
        loadingView = loginBinding.loadingOverlay;

        //Hacemos un logOut cada vez que se inicia la aplicacion, hemos decidido hacerlo asi por simplicidad, si la aplicacion se cierra el usuario ha de
        //iniciar sesion cada vez que abre la aplicacion
        loginViewModel.logOut();
        initListeners();
        start_animations();

        return viewRoot;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        loginBinding = null;
    }


    private void initListeners(){
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(allFilled(mailEditText,passwordEditText)){
                    loginViewModel.doLogIn(mailEditText.getText().toString(),passwordEditText.getText().toString());
                }

            }
        });

        recoverPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().getSupportFragmentManager().beginTransaction().setReorderingAllowed(true
                ).replace(R.id.login_fragment_container,
                        PasswordRecoveryFragment.class,null).addToBackStack(null).commit();
            }
        });

        createTeacherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((FragmentActivity)viewRoot.getContext()).getSupportFragmentManager().beginTransaction().setReorderingAllowed(true
                ).replace(R.id.login_fragment_container,
                        CreateTeacherFragment.class,null).addToBackStack(TAG).commit();
            }

        });

        //tambien declaramos la mainActivity como observer de LoginViewModel

        loginViewModel.getLoginEvent().observe(requireActivity(),event ->{
            if(event instanceof Event.Loading){
                loadingView.setVisibility(View.VISIBLE);
            }else if(event instanceof Event.Success){
                Intent switchActivityIntent;
                loadingView.setVisibility(View.GONE);
                Log.d(TAG,"el usuario ha iniciado sesion correctamente");
                UserModel userdata = ((Event.Success<UserModel>) event).getData();
                if(userdata.getParent()){
                    Log.d(TAG, "el usuario es padre");
                    switchActivityIntent = new Intent(requireActivity(), ParentActivity.class);

                }
                else{
                    Log.d(TAG, "el usuario es profesor");
                    switchActivityIntent = new Intent(requireActivity(), TeacherActivity.class);

                }

                startActivity(switchActivityIntent);
                requireActivity().finish();

            }else{
                //muestra un toast de error (se puede cambiar en un futuro)
                loadingView.setVisibility(View.GONE);
                passwordEditText.setText("");
                Toast.makeText(requireActivity(),R.string.login_error,Toast.LENGTH_LONG).show();
            }
        });
    }

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



    //<-----------------    Animation   -------------------->

    private void start_animations(){
        ImageView circleImageView = loginBinding.circle;
        TextView email = loginBinding.editTextEmailAddress;
        TextView contrasenya = loginBinding.editTextPassword;
        TextView textViewTitle = loginBinding.textViewTitle;
        TextView textViewRecoverPassword = loginBinding.textViewRecoverPassword;
        TextView textViewProfesor = loginBinding.textViewProfesor;
        CardView cardView = loginBinding.roundedCardView;
        Button button_login = loginBinding.buttonLogin;
        Button button_create = loginBinding.buttonCreateTeacherAccount;

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
