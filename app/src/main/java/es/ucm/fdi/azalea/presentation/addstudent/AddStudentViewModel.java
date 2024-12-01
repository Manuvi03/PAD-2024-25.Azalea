package es.ucm.fdi.azalea.presentation.addstudent;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import es.ucm.fdi.azalea.business.model.ChatModel;
import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.integration.CallBack;
import es.ucm.fdi.azalea.integration.Event;
import es.ucm.fdi.azalea.integration.CreateStudentAndParentUseCase;

public class AddStudentViewModel extends ViewModel {

    private final String TAG = "AddStudentViewModel";
    private final MutableLiveData<Event<Boolean>> hState = new MutableLiveData<>();

    private final CreateStudentAndParentUseCase createStudentAndParentUseCaseUseCase;


    public AddStudentViewModel() {

        createStudentAndParentUseCaseUseCase = new CreateStudentAndParentUseCase();
    }

    public LiveData<Event<Boolean>> gethState(){return hState;}

    public void funcion(StudentModel student, UserModel parent, ChatModel chat){
        createStudentAndParentUseCaseUseCase.execute(student,parent, chat, new CallBack<Boolean>() {

            @Override
            public void onSuccess(Event.Success<Boolean> success) {
                Log.d(TAG, "onSuccess: Se ha creado el estudiante correctamente");
                hState.postValue(success);
            }

            @Override
            public void onError(Event.Error<Boolean> error) {
                Log.d(TAG, "onError: Error al crear el estudiante");
                hState.postValue(error);
            }
        });
    }


}
