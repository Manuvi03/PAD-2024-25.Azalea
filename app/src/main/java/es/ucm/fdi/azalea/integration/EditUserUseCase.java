package es.ucm.fdi.azalea.integration;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.business.model.UserModel;

public class EditUserUseCase {

    // caso de uso que actualiza el correo de la bd de autenticacion
    public void updateUserInAuth(UserModel userModel, CallBack<Boolean> cb){
        BusinessFactory.getInstance().getAuthRepository().updateCurrUserMail(userModel.getEmail(), new CallBack<Boolean>(){

            @Override
            public void onSuccess(Event.Success<Boolean> success) {
                cb.onSuccess(success);
            }

            @Override
            public void onError(Event.Error<Boolean> error) {
                cb.onError(error);
            }
        });
    }

    // obtiene la informacion del padre y la devuelve
    public void updateUser(UserModel userModel, CallBack<Boolean> cb){
        BusinessFactory.getInstance().getUserRepository().update(userModel.getId(), userModel, new CallBack<Boolean>(){

            @Override
            public void onSuccess(Event.Success<Boolean> success) {
                cb.onSuccess(success);
            }

            @Override
            public void onError(Event.Error<Boolean> error) {
                cb.onError(error);
            }
        });
    }
}
