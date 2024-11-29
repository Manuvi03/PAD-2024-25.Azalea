package es.ucm.fdi.azalea.integration;

import android.util.Log;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.UserModel;

public class CreateUserUseCase {

    private String TAG = "CreateUserUseCase";

    public void createUser(String id, String email, String password, CallBack<UserModel> cb){
        try {
            BusinessFactory.getInstance().getAuthRepository().register(email, password, new CallBack<UserModel>() {
                @Override
                public void onSuccess(Event.Success<UserModel> success) {

                }

                @Override
                public void onError(Event.Error<UserModel> error) {

                }
            });
        } catch(Exception e){

        }
    }
}
