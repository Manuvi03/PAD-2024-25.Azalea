package es.ucm.fdi.azalea.presentation.showgrades;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import es.ucm.fdi.azalea.business.model.MarkModel;
import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.integration.CallBack;
import es.ucm.fdi.azalea.integration.Event;
import es.ucm.fdi.azalea.integration.ReadMarksByStudentIdUseCase;
import es.ucm.fdi.azalea.integration.ReadStudentUseCase;

public class ShowGradesViewModel extends ViewModel {
    // constantes
    private final String TAG = "ShowGradesViewModel";

    // atributo con la info del estudiante que maneja el viewmodel
    private MutableLiveData<Event<StudentModel>> studentState = new MutableLiveData<>();

    // atributo con la info de los estudiantes que maneja el viewmodel
    private MutableLiveData<Event<List<MarkModel>>> marksState = new MutableLiveData<>();

    // devuelve un objeto inmodificable de la informacion actual del estudiante del viewmodel
    public LiveData<Event<StudentModel>> getStudentState(){
        Log.d(TAG, "Se obtiene la informacion actual del estudiante del GradeSubjectViewModel");
        if (studentState == null) {
            studentState = new MutableLiveData<>();
        }
        return studentState;
    }

    // devuelve un objeto inmodificable de la informacion actual del viewmodel
    public LiveData<Event<List<MarkModel>>> getMarksState(){
        Log.d(TAG, "Se obtiene la informacion actual del ShowGradesViewModel");
        if (marksState == null) {
            marksState = new MutableLiveData<>();
        }
        return marksState;
    }

    // implementa el caso de uso de leer un estudiante
    public <T> void readStudent(String studentId){
        // el valor de la informacion aun no se ha encontrado, por lo que se marca el evento como cargando
        studentState.postValue(new Event.Loading<>());

        // se realiza el caso de uso
        ReadStudentUseCase useCase = new ReadStudentUseCase();

        // implementando el callback que recibira la informacion cuando esta se busque
        useCase.readStudent(studentId, new CallBack<StudentModel>() {
            @Override
            public void onSuccess(Event.Success<StudentModel> success) {
                Log.d(TAG, "Los datos del estudiante han llegado correctamente al GradeSubjectViewModel en readStudent");
                studentState.postValue(success);
            }

            @Override
            public void onError(Event.Error<StudentModel> error) {
                Log.d(TAG, "Los datos del estudiante NO han llegado correctamente al GradeSubjectViewModel en readStudent");
                studentState.postValue(error);
            }
        });
    }

    // metodo que implementa el caso de uso
    public <T> void readMarksByStudentId(String studentId){
        // el valor de la informacion aun no se ha encontrado, por lo que se marca el evento como cargando
        marksState.postValue(new Event.Loading<>());

        // se realiza el caso de uso
        ReadMarksByStudentIdUseCase useCase = new ReadMarksByStudentIdUseCase();

        // implementando el callback que recibira la informacion cuando esta se busque
        useCase.readMarksByStudentId(studentId, new CallBack<List<MarkModel>>() {
            @Override
            public void onSuccess(Event.Success<List<MarkModel>> success) {
                Log.d(TAG, "Los datos han llegado correctamente al ShowGradesViewModel en readMarksByStudentId");
                marksState.postValue(success);
            }

            @Override
            public void onError(Event.Error<List<MarkModel>> error) {
                Log.d(TAG, "Los datos NO han llegado correctamente al ShowGradesViewModel en readMarksByStudentId");
                marksState.postValue(error);
            }
        });
    }
}
