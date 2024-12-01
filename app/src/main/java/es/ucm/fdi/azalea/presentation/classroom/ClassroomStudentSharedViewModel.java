package es.ucm.fdi.azalea.presentation.classroom;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ClassroomStudentSharedViewModel extends ViewModel {

    // constantes
    private final String TAG = "ClassroomStudentSharedViewModel";

    // variables
    // datos compartidos entre ClassRoomFragment y StudentFragment
    private final MutableLiveData<String> studentId = new MutableLiveData<>();

    public LiveData<String> getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        Log.d(TAG, "StudentId cambiando valor");
        this.studentId.postValue(studentId);
    }
}
