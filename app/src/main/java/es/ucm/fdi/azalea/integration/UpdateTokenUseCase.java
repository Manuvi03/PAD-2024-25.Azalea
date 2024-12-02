package es.ucm.fdi.azalea.integration;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.ChatModel;
import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.business.model.TokenModel;
import es.ucm.fdi.azalea.business.model.UserModel;

public class UpdateTokenUseCase {

    public String TAG = "UpdateTokenUseCase";

    public void updateToken(String token, CallBack<Boolean> cb) {
        getCurrentUser(token, new CallBack<Boolean>() {
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

    public void getCurrentUser(String token, CallBack<Boolean> cb) {
        try {
            BusinessFactory.getInstance().getAuthRepository().getCurrUser(new CallBack<FirebaseUser>() {
                @Override
                public void onSuccess(Event.Success<FirebaseUser> success) {
                    Log.d(TAG, "Paso 1 completado. Usuario autenticado obtenido: " + success.getData().getUid());
                    executeUpdate(token, success.getData().getUid(), cb);
                }

                @Override
                public void onError(Event.Error<FirebaseUser> error) {
                    Log.d(TAG, "Error obteniendo usuario autenticado: " + error);
                    cb.onError(new Event.Error<>());
                }
            });

        } catch (Exception e) {
            cb.onError(new Event.Error<>(e));
        }
    }

    private void executeUpdate(String token, String associatedId, CallBack<Boolean> cb) {

        TokenModel tokenModel = new TokenModel(token);
        tokenModel.setIdAssociated(associatedId);
        try {
            BusinessFactory.getInstance().getTokenRepository().update(tokenModel, new CallBack<TokenModel>() {
                @Override
                public void onSuccess(Event.Success<TokenModel> success) {
                    cb.onSuccess(new Event.Success<>(true));
                }

                @Override
                public void onError(Event.Error<TokenModel> error) {
                    cb.onError(new Event.Error<>(error.getException()));
                }
            });
        }catch (Exception e){
            cb.onError(new Event.Error<>(e));
        }
    }
}
