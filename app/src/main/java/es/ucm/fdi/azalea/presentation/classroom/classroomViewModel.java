package es.ucm.fdi.azalea.presentation.classroom;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.integration.Event;
import es.ucm.fdi.azalea.integration.ReadStudentsByClassRoomUseCase;

public class classroomViewModel extends ViewModel {

    private MutableLiveData<Event<List<StudentModel>>> studentsState;

    public LiveData<Event<List<StudentModel>>> getStudentsState(){
        if (studentsState == null) {
            studentsState = new MutableLiveData<>();
        }
        return studentsState;
    }

    public <T> void readStudentsByClassroom(String classroomId){
        //studentsState.setValue(new Event.Loading<>());
        ReadStudentsByClassRoomUseCase useCase = new ReadStudentsByClassRoomUseCase();
        Event<List<StudentModel>> result = useCase.readStudentsByClassRoomUseCase(classroomId);
        studentsState.postValue(result);
    }
}
