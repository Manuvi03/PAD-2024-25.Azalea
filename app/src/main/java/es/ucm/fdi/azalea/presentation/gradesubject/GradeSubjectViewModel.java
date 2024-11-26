package es.ucm.fdi.azalea.presentation.gradesubject;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import es.ucm.fdi.azalea.business.model.ClassRoomModel;
import es.ucm.fdi.azalea.business.model.MarkModel;
import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.integration.CallBack;
import es.ucm.fdi.azalea.integration.Event;
import es.ucm.fdi.azalea.integration.GradeMarkUseCase;
import es.ucm.fdi.azalea.integration.ReadClassRoomByIdUseCase;
import es.ucm.fdi.azalea.integration.ReadParentByStudentIdUseCase;
import es.ucm.fdi.azalea.integration.ReadStudentUseCase;

public class GradeSubjectViewModel extends ViewModel {
    // constantes
    private final String TAG = "GradeSubjectViewModel";

    // atributo con la info del estudiante que maneja el viewmodel
    private MutableLiveData<Event<StudentModel>> studentState = new MutableLiveData<>();

    // atributo con la info de la clase que maneja el viewmodel
    private MutableLiveData<Event<ClassRoomModel>> classState = new MutableLiveData<>();

    // atributo con la info de la nota que maneja el viewmodel
    private MutableLiveData<Event<StudentModel>> markState = new MutableLiveData<>();

    // devuelve un objeto inmodificable de la informacion actual del estudiante del viewmodel
    public LiveData<Event<StudentModel>> getStudentState(){
        Log.d(TAG, "Se obtiene la informacion actual del estudiante del GradeSubjectViewModel");
        if (studentState == null) {
            studentState = new MutableLiveData<>();
        }
        return studentState;
    }

    // devuelve un objeto inmodificable de la informacion actual de la clase del viewmodel
    public LiveData<Event<ClassRoomModel>> getClassState(){
        Log.d(TAG, "Se obtiene la informacion actual del estudiante del GradeSubjectViewModel");
        if (classState == null) {
            classState = new MutableLiveData<>();
        }
        return classState;
    }

    // devuelve un objeto inmodificable de la informacion actual de la clase del viewmodel
    public LiveData<Event<StudentModel>> getMarkState(){
        Log.d(TAG, "Se obtiene la informacion actual del estudiante del GradeSubjectViewModel");
        if (classState == null) {
            classState = new MutableLiveData<>();
        }
        return markState;
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

    // implementa el caso de uso de leer una clase por su identificador
    public <T> void readClass(String classId){
        // el valor de la informacion aun no se ha encontrado, por lo que se marca el evento como cargando
        classState.postValue(new Event.Loading<>());

        // se realiza el caso de uso
        ReadClassRoomByIdUseCase useCase = new ReadClassRoomByIdUseCase();

        // implementando el callback que recibira la informacion cuando esta se busque
        useCase.readClassRoomById(classId, new CallBack<ClassRoomModel>() {
            @Override
            public void onSuccess(Event.Success<ClassRoomModel> success) {
                Log.d(TAG, "Los datos de la clase han llegado correctamente al GradeSubjectViewModel en readClass");
                classState.postValue(success);
            }

            @Override
            public void onError(Event.Error<ClassRoomModel> error) {
                Log.d(TAG, "Los datos de la clase NO han llegado correctamente al GradeSubjectViewModel en readClass");
                classState.postValue(error);
            }
        });
    }

    // implementa el caso de uso de poner una nota
    public <T> void gradeMark(MarkModel markModel, StudentModel studentModel){
        // el valor de la informacion aun no se ha encontrado, por lo que se marca el evento como cargando
        markState.postValue(new Event.Loading<>());

        // se realiza el caso de uso
        GradeMarkUseCase useCase = new GradeMarkUseCase();

        // implementando el callback que recibira la informacion cuando esta se busque
        useCase.gradeMark(markModel, studentModel, new CallBack<StudentModel>() {
            @Override
            public void onSuccess(Event.Success<StudentModel> success) {
                Log.d(TAG, "Los datos de la nota han llegado correctamente al GradeSubjectViewModel en readClass");
                markState.postValue(success);
            }

            @Override
            public void onError(Event.Error<StudentModel> error) {
                Log.d(TAG, "Los datos de la nota NO han llegado correctamente al GradeSubjectViewModel en readClass");
                markState.postValue(error);
            }
        });
    }
}
