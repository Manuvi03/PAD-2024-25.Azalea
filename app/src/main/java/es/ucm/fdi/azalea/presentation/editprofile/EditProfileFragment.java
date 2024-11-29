package es.ucm.fdi.azalea.presentation.editprofile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.integration.Event;

public class EditProfileFragment extends Fragment {

    // constantes
    private final String TAG = "EditProfileFragment";

    // atributos

    // view
    private View view;

    // view model
    private EditProfileViewModel editProfileViewModel;

    // componentes de la vista
    private TextView nameText;
    private EditText nameEditText;
    private EditText surnameEditText;
    private EditText mailEditText;
    private EditText genderEditText;

    private Button edit_button;

    // info de la vista
    private UserModel userInfo;
    private boolean emailModified = false;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // se obtiene el viewmodel
        editProfileViewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);

        // se obtienen los componentes de la vista necesarios
        bindComponents();

        // se inicializa la vista con la info del usuario
        initView();

        // se inicializa el listener del boton
        initListeners();

        // asi como su observador
        initEditUserObserver();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        view = inflater.inflate(R.layout.edit_profile_fragment, container, false);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    // se obtienen los componentes de la vista
    private void bindComponents(){
        nameText = view.findViewById(R.id.edit_profile_name_textView);
        nameEditText = view.findViewById(R.id.edit_profile_nameEditText);
        surnameEditText = view.findViewById(R.id.edit_profile_surnamesEditText);
        mailEditText = view.findViewById(R.id.edit_profile_emailEditText);
        genderEditText = view.findViewById(R.id.edit_profile_genderEditText);
        edit_button = view.findViewById(R.id.edit_profile_button);
    }

    // se inicializa la vista
    private void initView(){
        // se obtiene la informacion del usuario
        editProfileViewModel.readUser();

        // se inicializa el observador de la vista
        initUserInfoObserver();
    }

    // inicializa el observador de la informacion del usuario
    private void initUserInfoObserver(){
        // se declara el observador de la info del usuario
        final Observer<Event<UserModel>> userInfoObserver = userEvent -> {

            // mientras se busca, se muestra el texto de cargando
            if(userEvent instanceof Event.Loading){
                Log.d(TAG, "Se esta buscando el usuario");
                nameEditText.setText(R.string.edit_profile_searching_user);
            }

            // cuando se encuentran los resultados, se muestran
            else if(userEvent instanceof Event.Success){
                Log.d(TAG, "Se encontro la informacion del usuario");
                // se obtiene la info
                userInfo = ((Event.Success<UserModel>) userEvent).getData();

                // actualiza la vista con los datos actuales del usuario
                updateViewInfo(userInfo);
            }

            // en caso de error, se muestra al usuario
            else if(userEvent instanceof Event.Error){
                Log.d(TAG, "Hubo algun error buscando la informacion del usuario");
                // se muestra el error mediante un mensaje toast
                Toast.makeText(getActivity(),R.string.edit_profile_search_error,Toast.LENGTH_LONG).show();
            }
        };

        // se inicializa el observador
        editProfileViewModel.getUserState().observe(getViewLifecycleOwner(), userInfoObserver);
    }

    // se actualiza la vista con los datos actuales del usuario
    private void updateViewInfo(UserModel userInfo){
        nameText.setText(userInfo.getName() + " " + userInfo.getSurname());
        nameEditText.setText(userInfo.getName());
        surnameEditText.setText(userInfo.getSurname());
        mailEditText.setText(userInfo.getEmail());
        genderEditText.setText(userInfo.getGender());
    }

    // se inicializan los listeners de la vista
    private void initListeners(){
        edit_button.setOnClickListener(button_listener ->{
            if(allFilled(nameEditText,surnameEditText,genderEditText,mailEditText)){

                // se comprueba si el email es sintacticamente correcto
                Event<String> verifiedEvent = editProfileViewModel.verify(mailEditText.getText().toString());
                if(verifiedEvent instanceof Event.Success){
                    userInfo.setName(nameEditText.getText().toString());
                    userInfo.setSurname(surnameEditText.getText().toString());
                    userInfo.setGender(genderEditText.getText().toString());

                    // se comprueba si el email ha sido modificado
                    if(!Objects.equals(userInfo.getEmail(), mailEditText.getText().toString()))
                        emailModified = true;
                    userInfo.setEmail(mailEditText.getText().toString());

                    // se modifica el usuario
                    editProfileViewModel.editUser(userInfo, emailModified);
                }
                else{
                    // en este caso no puede ser nunca Event.loading ya que la funcion manda
                    // o un error o un success
                    Event.Error<String> error = (Event.Error<String>) verifiedEvent;
                    if(error.getErrorData().equalsIgnoreCase("MailError")){
                        mailEditText.setError(getString(R.string.createteacher_mailFormatError));
                    }
                }
            }
        });
    }

    // los puntos suspensivos permiten pasar varios argumentos, comprueba si todos esos campos estan rellenos
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

    // se inicializa el observador sobre la modificacion del usuario
    private void initEditUserObserver(){
        // se declara el observador de la info del estudiante
        final Observer<Event<Boolean>> editUserObserver = userEvent -> {

            // mientras se busca, se muestra el texto de cargando
            if(userEvent instanceof Event.Loading){
                Log.d(TAG, "Se esta modificando el usuario");
                edit_button.setText(R.string.edit_profile_modifying);
            }

            // cuando se encuentran los resultados, se muestran
            else if(userEvent instanceof Event.Success){
                Log.d(TAG, "Se modifico correctamente el usuario");
                edit_button.setText(R.string.edit_profile_buttonText);

                // se muestra la operacion realizada mediante un mensaje toast
                Toast.makeText(getActivity(),
                        getString(R.string.edit_profile_correctly_modified), Toast.LENGTH_LONG).show();
            }

            // en caso de error, se muestra al usuario
            else if(userEvent instanceof Event.Error){
                Log.d(TAG, "Hubo algun error modificando al usuario");
                // se muestra el error mediante un mensaje toast
                Toast.makeText(getActivity(),getString(R.string.edit_profile_error_modifying,
                                userInfo.getName(), userInfo.getSurname()), Toast.LENGTH_LONG).show();
            }
        };

        // se inicializa el observador
        editProfileViewModel.getUserModifiedState().observe(getViewLifecycleOwner(), editUserObserver);
    }
}
