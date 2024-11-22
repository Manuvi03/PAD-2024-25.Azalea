package es.ucm.fdi.azalea.presentation.createteacher;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.integration.CallBack;
import es.ucm.fdi.azalea.integration.Event;
import es.ucm.fdi.azalea.integration.createTeacherUseCase;
import es.ucm.fdi.azalea.integration.passWordRecoveryUseCase;

public class CreateTeacherViewModel extends ViewModel {
    private createTeacherUseCase usecase;
    private MutableLiveData<Event<String>> createTeacherEvent;

    public CreateTeacherViewModel() {
        usecase = new createTeacherUseCase();
        createTeacherEvent = new MutableLiveData<>();
    }

    public void createTeacher(UserModel data){
        createTeacherEvent.postValue(new Event.Loading<>());
        usecase.createTeacher(data, new CallBack<Boolean>() {
            @Override
            public void onSuccess(Event.Success<Boolean> success) {

            }

            @Override
            public void onError(Event.Error<Boolean> error) {

            }
        });
    }

    public LiveData<Event<String>> getCreateTeacherEvent(){
        return createTeacherEvent;
    }
}


