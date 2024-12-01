package es.ucm.fdi.azalea.presentation.classroom;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.integration.CallBack;
import es.ucm.fdi.azalea.integration.Event;
import es.ucm.fdi.azalea.integration.GetClassNameUserCase;
import es.ucm.fdi.azalea.integration.ReadStudentsByClassRoomUseCase;

public class classroomViewModel extends ViewModel {

    // constantes
    private final String TAG = "classroomViewModel";

    // atributo con la info de los estudiantes que maneja el viewmodel
    private MutableLiveData<Event<List<StudentModel>>> studentsState = new MutableLiveData<>();
    private MutableLiveData<Event<String>> nameclassState = new MutableLiveData<>();

    // devuelve un objeto inmodificable de la informacion actual del viewmodel
    public LiveData<Event<List<StudentModel>>> getStudentsState(){
        Log.d(TAG, "Se obtiene la informacion actual del ClassroomViewModel");
        if (studentsState == null) {
            studentsState = new MutableLiveData<>();
        }
        return studentsState;
    }

    // metodo que implementa el caso de uso
    public void readStudentsByClassroom(){
        // el valor de la informacion aun no se ha encontrado, por lo que se marca el evento como cargando
        studentsState.postValue(new Event.Loading<>());

        // se realiza el caso de uso
        ReadStudentsByClassRoomUseCase useCase = new ReadStudentsByClassRoomUseCase();

        // implementando el callback que recibira la informacion cuando esta se busque
        useCase.execute(new CallBack<List<StudentModel>>() {
            @Override
            public void onSuccess(Event.Success<List<StudentModel>> success) {
                Log.d(TAG, "Los datos han llegado correctamente al ClassRoomViewModel en readStudentsByClassroom");
                studentsState.postValue(success);
            }

            @Override
            public void onError(Event.Error<List<StudentModel>> error) {
                Log.d(TAG, "Los datos NO han llegado correctamente al ClassRoomViewModel en readStudentsByClassroom");
                studentsState.postValue(error);
            }
        });
    }

    public LiveData<Event<String>> getClassNameState(){
        Log.d(TAG, "Se obtiene el nombre de la clase");
        if (nameclassState == null) {
            Log.d(TAG, "nameclassState es null");
            nameclassState = new MutableLiveData<>();
        }
        Log.d(TAG, "Se devuelve nameclassState");
        return nameclassState;
    }

    public void getClassName(){
        nameclassState.postValue(new Event.Loading<>());

        GetClassNameUserCase useCase = new GetClassNameUserCase();
        Log.d(TAG, "Se realiza el caso de uso");
        useCase.execute(new CallBack<String>(){

            @Override
            public void onSuccess(Event.Success<String> success) {
                Log.d(TAG, "Se ha obtenido el nombre de la clase: " + success.getData());
                nameclassState.postValue(success);
            }

            @Override
            public void onError(Event.Error<String> error) {
                nameclassState.postValue(error);
            }
        });
    }
}
