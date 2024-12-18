package es.ucm.fdi.azalea.presentation.showgrades;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StudentShowGradesSharedViewModel extends ViewModel {
    // constantes
    private final String TAG = "StudentShowGradesSharedViewModel";

    // variables
    // datos compartidos entre StudentFragment y GradeSubjectFragment
    private final MutableLiveData<String> studentId = new MutableLiveData<>();

    public LiveData<String> getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        Log.d(TAG, "StudentId cambiando valor");
        this.studentId.postValue(studentId);
    }

}
