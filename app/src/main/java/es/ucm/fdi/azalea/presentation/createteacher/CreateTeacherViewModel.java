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
    private MutableLiveData<Event<Boolean>> authenticateTeacherEvent;

    //este userdata le utilizamos para pasar informacion entre fragments mediante el viewModel
    private UserModel userdata;

    public UserModel getUserdata() {
        return userdata;
    }

    public void setUserdata(UserModel userdata) {
        this.userdata = userdata;
    }



    public CreateTeacherViewModel() {
        usecase = new createTeacherUseCase();
        createTeacherEvent = new MutableLiveData<>();
        authenticateTeacherEvent = new MutableLiveData<>();
    }

    public void authenticateTeacher(String mail, String password){
        usecase.authenticateTeacher(mail, password, new CallBack<Boolean>() {
            @Override
            public void onSuccess(Event.Success<Boolean> success) {
                authenticateTeacherEvent.postValue(success);
            }

            @Override
            public void onError(Event.Error<Boolean> error) {
                authenticateTeacherEvent.postValue(error);
            }
        });
    }

    public void createTeacher(UserModel data,String className){
        createTeacherEvent.postValue(new Event.Loading<>());

        usecase.createTeacher(data,className, new CallBack<Boolean>() {
            @Override
            public void onSuccess(Event.Success<Boolean> success) {
                createTeacherEvent.postValue(new Event.Success<String>(""));
            }

            @Override
            public void onError(Event.Error<Boolean> error) {
                createTeacherEvent.postValue(new Event.Error<>(error.getException()));
            }
        });
    }

    public LiveData<Event<String>> getCreateTeacherEvent(){
        return createTeacherEvent;
    }
    public LiveData<Event<Boolean>> getAuthenticateTeacherEvent() {return authenticateTeacherEvent;}
}


