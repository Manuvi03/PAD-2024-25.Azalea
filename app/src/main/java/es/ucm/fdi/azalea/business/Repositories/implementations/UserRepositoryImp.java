package es.ucm.fdi.azalea.business.Repositories.implementations;


import android.telecom.Call;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import es.ucm.fdi.azalea.business.Repositories.UserRepository;
import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.integration.CallBack;
import es.ucm.fdi.azalea.integration.Event;

public class UserRepositoryImp implements UserRepository {

    private final static String TAG = "UserRepository";
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://azalea-fde19-default-rtdb.europe-west1.firebasedatabase.app/");
    private DatabaseReference usersReference = database.getReference("users");


    public void create(UserModel item, CallBack<UserModel> cb) {
        try{
            usersReference.child(item.getId()).setValue(item);
            Log.d(TAG, "User created with key: " + item.getId());
            cb.onSuccess(new Event.Success<>(item));
        }catch(Exception e){
            cb.onError(new Event.Error<>(e));
        }

    }


    public void findById(String id, CallBack<UserModel> cb) {
        Log.d(TAG, "Buscando usuario directamente con id: " + id);
        usersReference.child(id).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful() || task.getResult() == null) {
                Log.e(TAG, "Error al obtener el usuario", task.getException());
                cb.onError(new Event.Error<>(task.getException()));
                return;
            }

            UserModel user = task.getResult().getValue(UserModel.class);
            if (user == null) {
                Log.d(TAG, "No se encontró usuario con id: " + id);
                cb.onError(new Event.Error<>(new Exception("Usuario no encontrado")));
            } else {
                Log.d(TAG, "Usuario encontrado: " + user.getEmail());
                cb.onSuccess(new Event.Success<>(user));
            }
        });
    }

    public void checkUserExists(String mail,CallBack<Boolean> cb){
        try{
           Query query = usersReference.orderByChild("email").equalTo(mail);

           query.get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    cb.onSuccess(new Event.Success<>(true));
                }else if(!task.isSuccessful() || task.getResult() == null){
                    cb.onError(new Event.Error<>(task.getException()));
                }
           });

        }catch(Exception e){
            cb.onError(new Event.Error<>(e));
        }
    }


    public void update(UserModel item) {


    }


    public void delete(String id) {

    }


    public void readAll() {

    }
}
