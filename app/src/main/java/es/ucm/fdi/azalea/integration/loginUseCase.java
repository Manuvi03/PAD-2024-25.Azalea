package es.ucm.fdi.azalea.integration;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.Repositories.implementations.AuthRepositoryImp;
import es.ucm.fdi.azalea.business.model.UserModel;

public class loginUseCase {
    private final static String TAG = "loginUseCase";

    public loginUseCase(){

    }

    // la clase task de la api firebase ya gestiona ttodo lo que es acceso al repositorio



    public void logIn(String mail, String password, CallBack<UserModel> callback){

        try{

            // la lambda result es un objeto de tipo OnCompleteListener que espera un objeto  de tipo Task<TResult>
            //vamos a distinguir si consigue hacer el logIn o no lo consigue
            //es importante que para poder hacer el login correctamente el id que tiene asignado una cuenta
            //en el authentication de firebase sea el mismo que el id que tiene asignado en el repositorio de users

            BusinessFactory.getInstance().getAuthRepository().login(mail, password, new CallBack<UserModel>() {
                @Override
                public void onSuccess(Event.Success<UserModel> success) {
                    handleLogin(success.getData().getId(),callback);
                }

                @Override
                public void onError(Event.Error<UserModel> error) {
                    callback.onError(error);
                }
            });



        }catch(Exception e) {
            Log.d(TAG, "excepcion al iniciar sesion  " +
                    e.toString());
            callback.onError(new Event.Error<>(e));
        }

    }
    private void handleLogin(String userUid, CallBack<UserModel> cb){
        BusinessFactory.getInstance().getUserRepository().
                findById(userUid,new CallBack<UserModel>(){

                    @Override
                    public void onSuccess(Event.Success<UserModel> success) {
                        cb.onSuccess(success);                    }

                    @Override
                    public void onError(Event.Error<UserModel> error) {
                        cb.onError(error);
                    }


                });

    }



}
