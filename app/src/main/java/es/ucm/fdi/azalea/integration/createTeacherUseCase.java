package es.ucm.fdi.azalea.integration;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.UserModel;


//esta clase tiene dos funciones una para autenticar al profesor y otra para crear la clase y el usuario en la base de datos
public class createTeacherUseCase {

    public void createTeacher(UserModel data, CallBack<Boolean> cb){
        try{
            // primero lo autenticamos con firebase authentication
            BusinessFactory.getInstance().getAuthRepository().register(data.getEmail(), data.getPassword(), new CallBack<UserModel>() {
                @Override
                public void onSuccess(Event.Success<UserModel> success) {

                    registerTeacher(data,cb);
                }

                @Override
                public void onError(Event.Error<UserModel> error) {
                    cb.onError(new Event.Error<>(error.getException()));
                }
            });
        }catch (Exception e){
            cb.onError(new Event.Error<>(e));
        }

    }

    private void registerTeacher(UserModel userdata, CallBack<Boolean> cb) {
        try{
            BusinessFactory.getInstance().getUserRepository().create(userdata, new CallBack<UserModel>() {
                @Override
                public void onSuccess(Event.Success<UserModel> success) {
                    cb.onSuccess(new Event.Success<Boolean>(true));
                }

                @Override
                public void onError(Event.Error<UserModel> error) {
                    cb.onError(new Event.Error<>(error.getException()));
                }
            });
        }catch(Exception e){
            cb.onError(new Event.Error<>(e));
        }

    }
}
