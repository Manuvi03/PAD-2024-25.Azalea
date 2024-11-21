package es.ucm.fdi.azalea.integration;

import java.util.List;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.StudentModel;

public class ReadStudentsByClassRoomUseCase {

    // caso de uso que, a partir de el id de una clase, busca a sus estudiantes
    public void readStudentsByClassRoomUseCase(String classroomId, CallBack<List<StudentModel>> cb){
        BusinessFactory.getInstance().getStudentRepository().readByClassRoomId(classroomId, new CallBack<List<StudentModel>>(){

            @Override
            public void onSuccess(Event.Success<List<StudentModel>> success) {
                cb.onSuccess(success);
            }

            @Override
            public void onError(Event.Error<List<StudentModel>> error) {
                cb.onError(error);
            }
        });
    }
}
