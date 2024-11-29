package es.ucm.fdi.azalea.integration;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.Repositories.implementations.StudentRepositoryImp;
import es.ucm.fdi.azalea.business.Repositories.implementations.UserRepositoryImp;
import es.ucm.fdi.azalea.business.model.StudentModel;

public class CreateStudentUseCase {

    public CreateStudentUseCase(){}

    public LiveData<StudentModel> execute(StudentModel sm) {
        MutableLiveData<StudentModel> resultLiveData = new MutableLiveData<>();

        BusinessFactory.getInstance().getStudentRepository().create(sm, new CallBack<StudentModel>() {
            @Override
            public void onSuccess(Event.Success<StudentModel> success) {
                resultLiveData.postValue(success.getData());
            }

            @Override
            public void onError(Event.Error<StudentModel> error) {
                resultLiveData.postValue(null);
            }
        });
        return resultLiveData;
    }
}
