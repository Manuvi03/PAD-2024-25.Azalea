package es.ucm.fdi.azalea.integration;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.StudentModel;

public class ReadStudentUseCase {

    // caso de uso que, a partir de el id de un estudiante, busca su informacion
    public void readStudent(String studentId,  CallBack<StudentModel> cb){
        BusinessFactory.getInstance().getStudentRepository().readById(studentId, new CallBack<StudentModel>(){

            @Override
            public void onSuccess(Event.Success<StudentModel> success) {
                cb.onSuccess(success);
            }

            @Override
            public void onError(Event.Error<StudentModel> error) {
                cb.onError(error);
            }
        });
    }
}
