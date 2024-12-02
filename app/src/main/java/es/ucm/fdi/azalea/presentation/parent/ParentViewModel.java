package es.ucm.fdi.azalea.presentation.parent;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.integration.CallBack;
import es.ucm.fdi.azalea.integration.Event;
import es.ucm.fdi.azalea.integration.UpdateTokenUseCase;
import es.ucm.fdi.azalea.integration.getCurrUserUseCase;

public class ParentViewModel extends ViewModel {
    // constantes
    private final String TAG = "ParentViewModel";

    // atributo con la info del usuario actual que maneja el viewmodel
    private MutableLiveData<Event<UserModel>> userState = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> updateDataEvent = new MutableLiveData<>();
    private UpdateTokenUseCase updateUsecase = new UpdateTokenUseCase();

    // devuelve un objeto inmodificable de la informacion sobre el usuario actual
    public LiveData<Event<UserModel>> getUserState(){
        Log.d(TAG, "Se obtiene la info sobre la modificacion del ParentViewModel");
        if (userState == null) {
            userState = new MutableLiveData<>();
        }
        return userState;
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
                Log.d(TAG, "Los datos del usuario han llegado correctamente al ParentViewModel en readUser");
                userState.postValue(success);
            }

            @Override
            public void onError(Event.Error<UserModel> error) {
                Log.d(TAG, "Los datos del usuario NO han llegado correctamente al ParentViewModel en readUser");
                userState.postValue(error);
            }
        });
    }

    public void updateToken(String token) {
        updateDataEvent.postValue(new Event.Loading<>());
        updateUsecase.updateToken(token, new CallBack<Boolean>() {
            @Override
            public void onSuccess(Event.Success<Boolean> success) {
                updateDataEvent.postValue(success);
            }

            @Override
            public void onError(Event.Error<Boolean> error) {
                updateDataEvent.postValue(error);
            }
        });
    }
}

