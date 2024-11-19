package es.ucm.fdi.azalea.presentation.createteacher;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import es.ucm.fdi.azalea.integration.Event;
import es.ucm.fdi.azalea.integration.createTeacherUseCase;
import es.ucm.fdi.azalea.integration.passWordRecoveryUseCase;

public class CreateTeacherViewModel extends ViewModel {
    private createTeacherUseCase usecase;
    private MutableLiveData<Event<Boolean>> data;

    public CreateTeacherViewModel() {
        usecase = new createTeacherUseCase();
        data = new MutableLiveData<>();
    }
}


