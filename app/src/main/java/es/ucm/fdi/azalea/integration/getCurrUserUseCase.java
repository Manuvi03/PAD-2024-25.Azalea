package es.ucm.fdi.azalea.integration;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.UserModel;

public class getCurrUserUseCase {

    private String TAG = "getCurrUserUseCase";

    public void getCurrentUser(CallBack<FirebaseUser> cb){
        try{
            BusinessFactory.getInstance().getAuthRepository().getCurrUser(new CallBack<FirebaseUser>() {
                @Override
                public void onSuccess(Event.Success<FirebaseUser> success) {
                    Log.d(TAG, "Usuario autenticado obtenido: " + success.getData());
                    cb.onSuccess(success);
                }

                @Override
                public void onError(Event.Error<FirebaseUser> error) {
                    Log.d(TAG, "Error obteniendo usuario autenticado: " + error);
                    cb.onError(error);
                }
            });

        }catch (Exception e){
            cb.onError(new Event.Error<>(e));
        }
    }

    public void getUserModel(String id, CallBack<UserModel> cb ){
        Log.d(TAG, "Buscando usuario con id: " + id);
        try{
            BusinessFactory.getInstance().getUserRepository().findById(id, new CallBack<UserModel>() {
                @Override
                public void onSuccess(Event.Success<UserModel> success) {
                    Log.d(TAG, "Usuario obtenido: " + success.getData());
                    cb.onSuccess(success);
                }

                @Override
                public void onError(Event.Error<UserModel> error) {
                    Log.d(TAG, "Error obteniendo usuario: " + error);
                    cb.onError(error);
                }
            });
        }catch (Exception e){
            cb.onError(new Event.Error<>(e));
        }
    }
}
