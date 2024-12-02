package es.ucm.fdi.azalea.business.Repositories.implementations;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.ucm.fdi.azalea.business.Repositories.AuthRepository;
import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.integration.CallBack;
import es.ucm.fdi.azalea.integration.Event;

public class AuthRepositoryImp implements AuthRepository {
    private static String TAG = "AuthRepository";
    private FirebaseAuth instance;

    public AuthRepositoryImp(){
        instance = FirebaseAuth.getInstance();
    }

    //Constructor implementado para poder hacer pruebas con Mockito
    public AuthRepositoryImp(FirebaseAuth firebaseAuth){
        instance = firebaseAuth;
    }

    public void login(String mail, String password, CallBack<UserModel> cb){
        instance.signInWithEmailAndPassword(mail,password).addOnCompleteListener(task ->{
           try{
            if(task.isSuccessful()){
                Log.d(TAG,"Se ha podido hacer el log In con el usuario " +
                        mail + " y constrasenya " + password);
                UserModel data = new UserModel();
                data.setPassword(password);
                data.setEmail(mail);
                data.setId(task.getResult().getUser().getUid());

                cb.onSuccess(new Event.Success<>(
                        data));
            }else{
                Log.d(TAG,"error al iniciar sesion " +
                        task.getResult().toString());
                cb.onError(new Event.Error<>(task.getException()));
            }
           }catch(Exception e){
               Log.d(TAG, "excepcion al iniciar sesion " +
                       e.toString());
                cb.onError(new Event.Error<>(e));
           }
       });
    }

    public void register(String mail, String password, CallBack<UserModel> cb){
        instance.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(task -> {
            try{
                UserModel data = new UserModel();
                if(task.isSuccessful()){
                    data.setPassword(password);
                    data.setEmail(mail);
                    data.setId(task.getResult().getUser().getUid());
                    cb.onSuccess(new Event.Success<>(data));
                }else{

                    cb.onError(new Event.Error<>(task.getException()));

                }
            }catch(Exception e){

                cb.onError(new Event.Error<>(e));
            }
        });
    }


    public void updateCurrUserMail(String mail, CallBack<Boolean> cb){
        try{
            FirebaseUser user = instance.getCurrentUser();

            user.updateEmail(mail)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "se ha actulizado el mail del usuario actual");
                                cb.onSuccess(new Event.Success<>(true));
                            }
                            else{
                                cb.onError(new Event.Error<>(task.getException()));
                            }
                        }
                    });
        }catch(Exception e){
            cb.onError(new Event.Error<>(e));
        }

    }

    public void sendUpdatePasswordMail(String mail, CallBack<Boolean> cb){
        try{
            instance.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        cb.onSuccess(new Event.Success<>(true));
                    }else{
                        cb.onError(new Event.Error<>(task.getException()));
                    }
                }
            });
        }catch(Exception e){
            cb.onError(new Event.Error<>(e));
        }
    }

    public void updateCurrUserPassword(String password,CallBack<Boolean> cb){
        try{
            FirebaseUser user = instance.getCurrentUser();

            user.updatePassword(password)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Se ha actualizado la contrasenya del usuario actual");
                                cb.onSuccess(new Event.Success<>(true));
                            }else{
                                cb.onError(new Event.Error<>(task.getException()));
                            }
                        }
                    });
        }catch (Exception e){
            cb.onError(new Event.Error<>(e));
        }
    }

    public void getCurrUser(CallBack<FirebaseUser> cb){
        try {
            FirebaseUser user_auth = instance.getCurrentUser();
            if(user_auth == null){
                cb.onError(new Event.Error<>());
            }else{
                cb.onSuccess(new Event.Success<>(user_auth));
            }

        }catch (Exception e){
            cb.onError(new Event.Error<>(e));
        }
    }

    public void logout(){
        try{
            instance.signOut();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
