package es.ucm.fdi.azalea.presentation.login;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.integration.Event;
import es.ucm.fdi.azalea.integration.loginUseCase;

public class LoginViewModel extends ViewModel {
    private final loginUseCase login_integration;
    //En este caso el evento solo trae un booleano que indica si se ha podido iniciar sesion o no
    private final MutableLiveData<Event<Boolean>> loginEvent;

    public LoginViewModel(){
        //inicializo el caso de uso en el propio viewModel pero tambien lo podemos hacer con factorías y reducimos las dependencias
        login_integration = new loginUseCase();
        loginEvent = new MutableLiveData<>();
    }
    //el mutableLiveData se devuelve bajo la interfaz LiveData
    public LiveData<Event<Boolean>> getLoginEvent(){
        return loginEvent;
    }

    public void doLogIn(String mail, String password){
        loginEvent.setValue(new Event.Loading<>());


        Event<Boolean> result = login_integration.logIn(mail,password);
        loginEvent.postValue(result);
    }

}
