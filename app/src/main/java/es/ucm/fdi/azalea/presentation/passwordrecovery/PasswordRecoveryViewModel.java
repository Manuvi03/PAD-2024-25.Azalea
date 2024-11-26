package es.ucm.fdi.azalea.presentation.passwordrecovery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import es.ucm.fdi.azalea.integration.CallBack;
import es.ucm.fdi.azalea.integration.Event;
import es.ucm.fdi.azalea.integration.passWordRecoveryUseCase;

public class PasswordRecoveryViewModel extends ViewModel {
    private passWordRecoveryUseCase usecase;
    private MutableLiveData<Event<Boolean>> data;

    public PasswordRecoveryViewModel(){
        usecase = new passWordRecoveryUseCase();
        data = new MutableLiveData<>();
    }

    public LiveData<Event<Boolean>> getRecoveryEvent(){
        return data;
    }

    public void resetPassword(String mail){
        data.setValue(new Event.Loading<>());

        usecase.resetPassword(mail, new CallBack<Boolean>() {
            @Override
            public void onSuccess(Event.Success<Boolean> success) {
                data.setValue(success);
            }

            @Override
            public void onError(Event.Error<Boolean> error) {
                data.setValue(error);
            }
        });
    }

    public boolean verifyMail(String mail){
        return mail.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

}
