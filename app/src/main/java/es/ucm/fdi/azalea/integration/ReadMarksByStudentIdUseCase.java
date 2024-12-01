package es.ucm.fdi.azalea.integration;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.MarkModel;
import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.business.model.UserModel;

public class ReadMarksByStudentIdUseCase {

    // caso de uso que busca las notas de un estudiante
    public void readMarksByStudentId(String studentId, CallBack<List<MarkModel>> cb){
        BusinessFactory.getInstance().getMarkRepository().readByStudentId(studentId, new CallBack<List<MarkModel>>(){

            @Override
            public void onSuccess(Event.Success<List<MarkModel>> success) {
                cb.onSuccess(success);
            }

            @Override
            public void onError(Event.Error<List<MarkModel>> error) {
                cb.onError(error);
            }
        });
    }

}
