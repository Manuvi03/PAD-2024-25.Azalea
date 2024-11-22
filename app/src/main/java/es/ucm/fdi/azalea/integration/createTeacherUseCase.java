package es.ucm.fdi.azalea.integration;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.UserModel;


//esta clase tiene dos funciones una para autenticar al profesor y otra para crear la clase y el usuario en la base de datos
public class createTeacherUseCase {

    public void createTeacher(UserModel data, CallBack<Boolean> cb){
        try{
            BusinessFactory.getInstance().getAuthRepository().register(data.getEmail(), data.getPassword(), new CallBack<UserModel>() {
                @Override
                public void onSuccess(Event.Success<UserModel> success) {

                }

                @Override
                public void onError(Event.Error<UserModel> error) {

                }
            });
        }catch (Exception e){

        }

    }

    public void authenticateTeacher(UserModel userdata, CallBack<UserModel> cb){
        BusinessFactory.getInstance().getAuthRepository().register(userdata.getEmail(), userdata.getPassword(), new CallBack<UserModel>() {
            @Override
            public void onSuccess(Event.Success<UserModel> success) {
                cb.onSuccess(new Event.Success<>(userdata));
            }

            @Override
            public void onError(Event.Error<UserModel> error) {
                cb.onError(error);
            }
        });
    }
}
