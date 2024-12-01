package es.ucm.fdi.azalea.presentation.student;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.integration.CallBack;
import es.ucm.fdi.azalea.integration.Event;
import es.ucm.fdi.azalea.integration.ReadParentByStudentIdUseCase;
import es.ucm.fdi.azalea.integration.ReadStudentUseCase;

public class StudentViewModel extends ViewModel {
    // constantes
    private final String TAG = "StudentViewModel";

    // atributo con la info del estudiante que maneja el viewmodel
    private MutableLiveData<Event<StudentModel>> studentState = new MutableLiveData<>();

    // atributo con la info del tutor que maneja el viewmodel
    private MutableLiveData<Event<UserModel>> parentState = new MutableLiveData<>();

    // devuelve un objeto inmodificable de la informacion actual del estudiante del viewmodel
    public LiveData<Event<StudentModel>> getStudentState(){
        Log.d(TAG, "Se obtiene la informacion actual del estudiante del StudentViewModel");
        if (studentState == null) {
            studentState = new MutableLiveData<>();
        }
        return studentState;
    }

    // devuelve un objeto inmodificable de la informacion actual del tutor del viewmodel
    public LiveData<Event<UserModel>> getParentState(){
        Log.d(TAG, "Se obtiene la informacion actual del estudiante del StudentViewModel");
        if (parentState == null) {
            parentState = new MutableLiveData<>();
        }
        return parentState;
    }

    // implementa el caso de uso de leer un estudiante
    public void readStudent(String studentId){
        // el valor de la informacion aun no se ha encontrado, por lo que se marca el evento como cargando
        studentState.postValue(new Event.Loading<>());

        // se realiza el caso de uso
        ReadStudentUseCase useCase = new ReadStudentUseCase();

        // implementando el callback que recibira la informacion cuando esta se busque
        useCase.readStudent(studentId, new CallBack<StudentModel>() {
            @Override
            public void onSuccess(Event.Success<StudentModel> success) {
                Log.d(TAG, "Los datos del estudiante han llegado correctamente al StudentViewModel en readStudent");
                studentState.postValue(success);
            }

            @Override
            public void onError(Event.Error<StudentModel> error) {
                Log.d(TAG, "Los datos del estudiante NO han llegado correctamente al StudentViewModel en readStudent");
                studentState.postValue(error);
            }
        });
    }

    // implementa el caso de uso de leer un tutor a partir del estudiante
    public void readParentByStudent(String studentId){
        // el valor de la informacion aun no se ha encontrado, por lo que se marca el evento como cargando
        parentState.postValue(new Event.Loading<>());

        // se realiza el caso de uso
        ReadParentByStudentIdUseCase useCase = new ReadParentByStudentIdUseCase();

        // implementando el callback que recibira la informacion cuando esta se busque
        useCase.readParentByStudentId(studentId, new CallBack<UserModel>() {
            @Override
            public void onSuccess(Event.Success<UserModel> success) {
                Log.d(TAG, "Los datos del tutor han llegado correctamente al StudentViewModel en readParentByStudent");
                parentState.postValue(success);
            }

            @Override
            public void onError(Event.Error<UserModel> error) {
                Log.d(TAG, "Los datos del tutor NO han llegado correctamente al StudentViewModel en readParentByStudent");
                parentState.postValue(error);
            }
        });
    }

}
