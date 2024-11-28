package es.ucm.fdi.azalea.integration;
import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.UserModel;

public class getCurrUserUseCase {

    public void getCurrentUser(CallBack<UserModel> cb){
        try{
            BusinessFactory.getInstance().getAuthRepository().getCurrUser(new CallBack<UserModel>() {
                @Override
                public void onSuccess(Event.Success<UserModel> success) {
                    getUserModel(cb,success.getData());
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

    private void getUserModel(CallBack<UserModel> cb, UserModel user_auth){
        try{
            BusinessFactory.getInstance().getUserRepository().findById(user_auth.getId(), new CallBack<UserModel>() {
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
