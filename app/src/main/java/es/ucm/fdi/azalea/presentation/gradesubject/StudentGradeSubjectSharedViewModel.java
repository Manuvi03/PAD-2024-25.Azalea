package es.ucm.fdi.azalea.presentation.gradesubject;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StudentGradeSubjectSharedViewModel extends ViewModel {
    // constantes
    private final String TAG = "StudentGradeSubjectSharedViewModel";

    // variables
    // datos compartidos entre StudentFragment y GradeSubjectFragment
    private final MutableLiveData<String> studentId = new MutableLiveData<>();
    private final MutableLiveData<String> studentImage = new MutableLiveData<>();

    public LiveData<String> getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        Log.d(TAG, "StudentId cambiando valor");
        this.studentId.postValue(studentId);
    }

}
