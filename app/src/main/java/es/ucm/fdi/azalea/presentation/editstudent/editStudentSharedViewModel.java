package es.ucm.fdi.azalea.presentation.editstudent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class editStudentSharedViewModel extends ViewModel {
    MutableLiveData<String> idStudent;
    public editStudentSharedViewModel(){
        idStudent = new MutableLiveData<>();
    }

    public void setIdStudent(String idStudent) {
        this.idStudent.postValue(idStudent);
    }

    public LiveData<String> getIdStudent() {
        return idStudent;
    }
}
