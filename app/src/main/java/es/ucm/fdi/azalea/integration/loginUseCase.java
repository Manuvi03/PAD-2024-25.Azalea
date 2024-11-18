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

    private final AuthRepositoryImp authRepositoryImp;
    public loginUseCase(){
        authRepositoryImp = new AuthRepositoryImp();
    }

    // la clase task de la api firebase ya gestiona ttodo lo que es acceso al repositorio



    public void logIn(String mail, String password, CallBack<UserModel> callback){

        try{

            // la lambda result es un objeto de tipo OnCompleteListener que espera un objeto  de tipo Task<TResult>
            //vamos a distinguir si consigue hacer el logIn o no lo consigue
            //es importante que para poder hacer el login correctamente el id que tiene asignado una cuenta
            //en el authentication de firebase sea el mismo que el id que tiene asignado en el repositorio de users

            Task<AuthResult> task =  authRepositoryImp.login(mail, password);


            task.addOnCompleteListener(result ->{
                //hace falta hacer otro try catch dentro del listener para coger excepciones de
                // otros hilos
                try{
                    if (result.isSuccessful()) {
                        Log.d(TAG,"Se ha podido hacer el log In con el usuario " +
                                mail + " y constrasenya " + password);

                        //el callback del repositorio lo tratamos en otra funcion para
                        //mayor claridad de codigo, le pasamos el callback del viewModel
                        handleLogin(result.getResult().getUser().getUid(),callback);


                    } else {
                        //cogemos la excepcion de Task
                        Log.d("loginUseCase","error al iniciar sesion " +
                                result.getResult().toString());

                        callback.onError(new Event.Error<>(result.getException()));

                    }
                }catch(Exception e){
                    Log.d("loginUseCase", "excepcion al iniciar sesion " +
                            e.toString());
                    callback.onError(new Event.Error<>(e));
                }

            });
        }catch(Exception e) {
            Log.d("loginUseCase", "excepcion al iniciar sesion " +
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
