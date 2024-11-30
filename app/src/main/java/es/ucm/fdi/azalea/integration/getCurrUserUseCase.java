package es.ucm.fdi.azalea.integration;
import com.google.firebase.auth.FirebaseUser;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.UserModel;

public class getCurrUserUseCase {

    public void getCurrentUser(CallBack<UserModel> cb){
        try{
            BusinessFactory.getInstance().getAuthRepository().getCurrUser(new CallBack<FirebaseUser>() {
                @Override
                public void onSuccess(Event.Success<FirebaseUser> success) {
                    getUserModel(cb,success.getData().getUid());
                }

                @Override
                public void onError(Event.Error<FirebaseUser> error) {
                    cb.onError(new Event.Error<>());
                }
            });
        }catch (Exception e){
            cb.onError(new Event.Error<>(e));
        }
    }

    private void getUserModel(CallBack<UserModel> cb, String uid){
        try{
            BusinessFactory.getInstance().getUserRepository().findById(uid, new CallBack<UserModel>() {
                @Override
                public void onSuccess(Event.Success<UserModel> success) {
                    cb.onSuccess(success);
                }

                @Override
                public void onError(Event.Error<UserModel> error) {
                    cb.onError(error);
                }
            });
        }catch (Exception e){
            cb.onError(new Event.Error<>(e));
        }
    }
}