package es.ucm.fdi.azalea.presentation.addstudent;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.integration.CreateStudentUseCase;

public class AddStudentViewModel extends ViewModel {

    private final String TAG = "AddStudentViewModel";
    private final MutableLiveData<StudentModel> studentState = new MutableLiveData<>();
    private final CreateStudentUseCase createStudentUseCase;

    public AddStudentViewModel() {
        createStudentUseCase = new CreateStudentUseCase();
    }

    public LiveData<StudentModel> getStudentState() {return studentState;}

    public void createStudent(StudentModel sm) {
        Log.i(TAG, "createStudent: " + sm);
        createStudentUseCase.execute(sm).observeForever(studentState::setValue);
    }
}
