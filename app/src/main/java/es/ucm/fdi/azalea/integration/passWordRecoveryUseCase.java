package es.ucm.fdi.azalea.integration;

import android.util.Log;

import es.ucm.fdi.azalea.business.BusinessFactory;

public class passWordRecoveryUseCase {
    private final static String TAG = "passWordRecoveryUseCase";
    public void resetPassword(String mail, CallBack<Boolean> cb){
        BusinessFactory.getInstance().getAuthRepository().sendUpdatePasswordMail(mail, new CallBack<Boolean>() {
            @Override
            public void onSuccess(Event.Success<Boolean> success) {
                Log.d(TAG,"Se ha mandado el correo de recuperacion correctamente");
                cb.onSuccess(success);
            }

            @Override
            public void onError(Event.Error<Boolean> error) {
                Log.d(TAG,"Error al mandar el correo de recuperacion");
                cb.onError(error);
            }
        });
    }
}
