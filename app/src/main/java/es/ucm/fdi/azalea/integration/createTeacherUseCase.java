package es.ucm.fdi.azalea.integration;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.UserModel;

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
}
