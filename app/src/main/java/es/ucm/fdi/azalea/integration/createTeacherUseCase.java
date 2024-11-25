package es.ucm.fdi.azalea.integration;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.ClassRoomModel;
import es.ucm.fdi.azalea.business.model.UserModel;


//esta clase tiene dos funciones una para autenticar al profesor y otra para crear la clase y el usuario en la base de datos
public class createTeacherUseCase {

    public void createTeacher(UserModel data,String className, CallBack<Boolean> cb){
        try{
            ClassRoomModel new_class = new ClassRoomModel();
            new_class.setIdTeacher(data.getId());
            new_class.setName(className);
            BusinessFactory.getInstance().getClassRoomRepository().create(new_class, new CallBack<ClassRoomModel>() {
                @Override
                public void onSuccess(Event.Success<ClassRoomModel> success) {
                    data.setClassId(success.getData().getId());
                    registerTeacher(data,cb);
                }

                @Override
                public void onError(Event.Error<ClassRoomModel> error) {
                    cb.onError(new Event.Error<>(error.getException()));
                }
            });
        }catch(Exception e){
            cb.onError(new Event.Error<>(e));
        }

    }

    public void authenticateTeacher(String mail, String password, CallBack<Boolean> cb){
        try{
            // primero lo autenticamos con firebase authentication
            BusinessFactory.getInstance().getAuthRepository().register(mail, password, new CallBack<UserModel>() {
                @Override
                public void onSuccess(Event.Success<UserModel> success) {

                    cb.onSuccess(new Event.Success<Boolean>(true));
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
