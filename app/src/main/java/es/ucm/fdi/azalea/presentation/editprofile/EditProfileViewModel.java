package es.ucm.fdi.azalea.presentation.editprofile;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.integration.CallBack;
import es.ucm.fdi.azalea.integration.EditUserUseCase;
import es.ucm.fdi.azalea.integration.Event;
import es.ucm.fdi.azalea.integration.getCurrUserUseCase;

public class EditProfileViewModel extends ViewModel {
    // constantes
    private final String TAG = "EditProfileViewModel";

    // atributo con la info del usuario que maneja el viewmodel
    private MutableLiveData<Event<UserModel>> userState = new MutableLiveData<>();

    // atributo que indica si se ha modificado correctamente al usuario
    private MutableLiveData<Event<Boolean>> userModified = new MutableLiveData<>();

    // devuelve un objeto inmodificable de la informacion actual del usuario del viewmodel
    public LiveData<Event<UserModel>> getUserState(){
        Log.d(TAG, "Se obtiene la informacion actual del usuario del EditProfileViewModel");
        if (userState == null) {
            userState = new MutableLiveData<>();
        }
        return userState;
    }

    // devuelve un objeto inmodificable de la informacion sobre la modificacion del usuario
    public LiveData<Event<Boolean>> getUserModifiedState(){
        Log.d(TAG, "Se obtiene la info sobre la modificacion del EditProfileViewModel");
        if (userModified == null) {
            userModified = new MutableLiveData<>();
        }
        return userModified;
    }

    // implementa el caso de uso de leer el usuario actual
    public <T> void readUser(){
        // el valor de la informacion aun no se ha encontrado, por lo que se marca el evento como cargando
        userState.postValue(new Event.Loading<>());

        // se realiza el caso de uso
        getCurrUserUseCase useCase = new getCurrUserUseCase();

        // implementando el callback que recibira la informacion cuando esta se busque
        useCase.getCurrentUser(new CallBack<UserModel>() {
            @Override
            public void onSuccess(Event.Success<UserModel> success) {
                Log.d(TAG, "Los datos del usuario han llegado correctamente al EditProfileViewModel en readUser");
                userState.postValue(success);
            }

            @Override
            public void onError(Event.Error<UserModel> error) {
                Log.d(TAG, "Los datos del usuario NO han llegado correctamente al EditProfileViewModel en readUser");
                userState.postValue(error);
            }
        });
    }

    // implementa el caso de uso de modificar el usuario
    public <T> void editUser(UserModel userModel, boolean emailModified){
        // el valor de la informacion aun no se ha encontrado, por lo que se marca el evento como cargando
        userModified.postValue(new Event.Loading<>());

        // se realiza el caso de uso
        EditUserUseCase useCase = new EditUserUseCase();

        // primero se actualiza el correo en la base de datos de autenticacion, si es necesario
        if(emailModified) useCase.updateUserInAuth(userModel, new CallBack<Boolean>() {
            @Override
            public void onSuccess(Event.Success<Boolean> success) {
                Log.d(TAG, "Los datos del usuario se han modificado correctamente en auth");
                // tras ello se modifica el usuario
                modifyUser(userModel, useCase);
            }

            @Override
            public void onError(Event.Error<Boolean> error) {
                Log.d(TAG, "Los datos del usuario se han modificado correctamente en auth");
                userModified.postValue(error);
            }
        });
        else{
            // sino, se modifica el usuario
            modifyUser(userModel, useCase);
        }
    }

    // modifica al usuario en la bd
    private void modifyUser(UserModel userModel, EditUserUseCase useCase){
        // se actualiza el usuario
        useCase.updateUser(userModel, new CallBack<Boolean>() {
            @Override
            public void onSuccess(Event.Success<Boolean> success) {
                userModified.postValue(success);
            }

            @Override
            public void onError(Event.Error<Boolean> error) {
                Log.d(TAG, "Los datos del usuario NO se han modificado correctamente");
                userModified.postValue(error);
            }
        });
    }

    public Event<String> verify(String mail){
        Event<String> result = new Event.Success<String>("");
        /*regex para la comprobacion del email
        comprueeba varios requisitos como el hecho de que tenga @ , . y solo ciertos tipos de
        caracteres especiales*/
        if(!(mail).matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")){
            Event.Error<String> error = new Event.Error<>();
            error.setErrorData("MailError");
            return error;
        }
        return result;
    }
}
