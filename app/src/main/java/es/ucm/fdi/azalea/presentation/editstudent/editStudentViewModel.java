package es.ucm.fdi.azalea.presentation.editstudent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.integration.Event;

public class editStudentViewModel extends ViewModel {
    private MutableLiveData<Event<StudentModel>> studentDataEvent;
    private MutableLiveData<Event<Boolean>> updateDataEvent;


    public editStudentViewModel(){
        studentDataEvent = new MutableLiveData<>();
        updateDataEvent = new MutableLiveData<>();
    }

    public LiveData<Event<StudentModel>> getStudentDataEvent(){
        return studentDataEvent;
    }

    public LiveData<Event<Boolean>> getUpdateDataEvent() {
        return updateDataEvent;
    }

    public void getStudentData(){

    }

}
