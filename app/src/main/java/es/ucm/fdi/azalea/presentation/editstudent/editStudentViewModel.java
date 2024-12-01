package es.ucm.fdi.azalea.presentation.editstudent;

import android.widget.EditText;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.checkerframework.checker.units.qual.C;

import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.integration.CallBack;
import es.ucm.fdi.azalea.integration.Event;
import es.ucm.fdi.azalea.integration.ReadStudentUseCase;
import es.ucm.fdi.azalea.integration.updateStudentUseCase;

public class editStudentViewModel extends ViewModel {
    private MutableLiveData<Event<StudentModel>> studentDataEvent;
    private MutableLiveData<Event<Boolean>> updateDataEvent;
    private updateStudentUseCase updateUsecase;
    private ReadStudentUseCase readUsecase;


    public editStudentViewModel(){
        studentDataEvent = new MutableLiveData<>();
        updateDataEvent = new MutableLiveData<>();
        updateUsecase = new updateStudentUseCase();
        readUsecase = new ReadStudentUseCase();
    }

    public LiveData<Event<StudentModel>> getStudentDataEvent(){
        return studentDataEvent;
    }

    public LiveData<Event<Boolean>> getUpdateDataEvent() {
        return updateDataEvent;
    }

    public void getStudentData(String idStudent){
        studentDataEvent.postValue(new Event.Loading<>());
        readUsecase.readStudent(idStudent, new CallBack<StudentModel>() {
            @Override
            public void onSuccess(Event.Success<StudentModel> success) {
                studentDataEvent.postValue(success);
            }

            @Override
            public void onError(Event.Error<StudentModel> error) {
                studentDataEvent.postValue(error);
            }
        });
    }

    public void updateStudent(StudentModel student){
        studentDataEvent.postValue(new Event.Loading<>());
        updateUsecase.updateStudent(student, new CallBack<Boolean>() {
            @Override
            public void onSuccess(Event.Success<Boolean> success) {
                updateDataEvent.postValue(success);
            }

            @Override
            public void onError(Event.Error<Boolean> error) {
                updateDataEvent.postValue(error);
            }
        });
    }

    public boolean verifyDate(String error,EditText text){
        if(!text.getText().toString().isEmpty() && !text.getText().toString().matches("^[0-9]{1,2}\\/[0-9]{1,2}\\/[0-9]{4}$")){
            text.setError(error);
            return false;
        }
        return true;

    }

    public boolean verifyDoubles(String error,EditText... texts){
        boolean correct = true;
        for(EditText t: texts){
            if(!t.getText().toString().isEmpty() && !parsedouble(t.getText().toString())){
                t.setError(error);
                correct = false;
            }
        }
        return correct;

    }

    private boolean parsedouble(String s){
        try{
            Double.parseDouble(s);
        }catch(Exception e){
            return false;
        }
        return true;
    }

    public boolean verifyPhones(String error, EditText... texts){
        boolean correct = true;
        for(EditText t: texts){
            if(!t.getText().toString().isEmpty() && !t.getText().toString().matches("^\\d{9}$")){
                t.setError(error);
                correct = false;
            }
        }
        return correct;
    }



}
