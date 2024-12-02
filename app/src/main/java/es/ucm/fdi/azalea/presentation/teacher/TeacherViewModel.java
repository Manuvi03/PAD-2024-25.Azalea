package es.ucm.fdi.azalea.presentation.teacher;

import androidx.lifecycle.MutableLiveData;

import es.ucm.fdi.azalea.integration.CallBack;
import es.ucm.fdi.azalea.integration.Event;
import es.ucm.fdi.azalea.integration.UpdateTokenUseCase;


public class TeacherViewModel {

    private MutableLiveData<Event<Boolean>> updateDataEvent;
    private UpdateTokenUseCase updateUsecase;

    public TeacherViewModel() {
        this.updateDataEvent = new MutableLiveData<>();
        this.updateUsecase = new UpdateTokenUseCase();
    }

    public MutableLiveData<Event<Boolean>> getUpdateDataEvent() {
        return updateDataEvent;
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
