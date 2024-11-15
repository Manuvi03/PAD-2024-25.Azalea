package es.ucm.fdi.azalea.presentation.classroom;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.integration.Event;
import es.ucm.fdi.azalea.integration.ReadStudentsByClassRoomUseCase;

public class classroomViewModel extends ViewModel {

    private MutableLiveData<List<StudentModel>> studentsState = new MutableLiveData<>(new ArrayList<StudentModel>());

    public LiveData<List<StudentModel>> getStudentsState(){
        return studentsState;
    }

    public <T> void readStudentsByClassroom(){
        ReadStudentsByClassRoomUseCase useCase = new ReadStudentsByClassRoomUseCase();
        Event<T> event = useCase.readStudentsByClassRoomUseCase();
    }
}
