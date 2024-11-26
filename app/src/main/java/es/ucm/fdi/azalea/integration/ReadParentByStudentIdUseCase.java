package es.ucm.fdi.azalea.integration;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.business.model.UserModel;

public class ReadParentByStudentIdUseCase {

    // caso de uso que busca la informacion de un tutor a partir del id de un estudiante
    public void readParentByStudentId(String studentId, CallBack<UserModel> cb){
        BusinessFactory.getInstance().getStudentRepository().readById(studentId, new CallBack<StudentModel>(){

            @Override
            public void onSuccess(Event.Success<StudentModel> success) {
                readParent(success.getData().getParentId(), cb);
            }

            @Override
            public void onError(Event.Error<StudentModel> error) {
                readParent(error.getException().toString(), cb);
            }
        });
    }

    // obtiene la informacion del padre y la devuelve
    private void readParent(String parentId, CallBack<UserModel> cb){
        BusinessFactory.getInstance().getUserRepository().findById(parentId, new CallBack<UserModel>(){

            @Override
            public void onSuccess(Event.Success<UserModel> success) {
                cb.onSuccess(success);
            }

            @Override
            public void onError(Event.Error<UserModel> error) {
                cb.onError(error);
            }
        });
    }
}
