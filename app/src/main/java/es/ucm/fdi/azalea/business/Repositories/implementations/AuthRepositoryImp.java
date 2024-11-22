package es.ucm.fdi.azalea.business.Repositories.implementations;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import es.ucm.fdi.azalea.business.Repositories.AuthRepository;
import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.integration.CallBack;
import es.ucm.fdi.azalea.integration.Event;

public class AuthRepositoryImp implements AuthRepository {
    private static String TAG = "AuthRepository";

    public void login(String mail, String password, CallBack<UserModel> cb){
       FirebaseAuth.getInstance().signInWithEmailAndPassword(mail,password).addOnCompleteListener(task ->{
           try{
            if(task.isSuccessful()){
                Log.d(TAG,"Se ha podido hacer el log In con el usuario " +
                        mail + " y constrasenya " + password);
                UserModel data = new UserModel();
                data.setId(task.getResult().getUser().getUid());

                cb.onSuccess(new Event.Success<UserModel>(
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
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(mail,password).addOnCompleteListener(task -> {
            try{
                UserModel data = new UserModel();
                if(task.isSuccessful()){

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

    public void logout(){
        try{
            FirebaseAuth.getInstance().signOut();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
