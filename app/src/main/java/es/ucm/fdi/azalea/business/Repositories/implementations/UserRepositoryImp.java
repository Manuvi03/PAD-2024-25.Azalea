package es.ucm.fdi.azalea.business.Repositories.implementations;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.ucm.fdi.azalea.business.Repositories.UserRepository;
import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.integration.CallBack;
import es.ucm.fdi.azalea.integration.Event;

public class UserRepositoryImp implements UserRepository {

    private final static String TAG = "UserRepository";
    private UserModel userdata;
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://azalea-fde19-default-rtdb.europe-west1.firebasedatabase.app/");
    private DatabaseReference usersReference = database.getReference("users");


    public void create(UserModel item) {


        usersReference.child(item.getId()).setValue(item);
        Log.d(TAG, "User created with key: " + item.getId());
        //return item.getId();
    }


    public void findById(String id, CallBack<UserModel> cb) {
        userdata = null;

       usersReference.child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    Log.d(TAG,"Error recuperando los datos",task.getException());
                    cb.onError(new Event.Error<UserModel>(task.getException()));

                }
                else {
                    Log.d(TAG,String.valueOf(task.getResult().getValue()));
                    cb.onSuccess(new Event.Success<>(task.getResult().getValue(UserModel.class)));
                }
            }
        });

    }


    public void update(UserModel item) {


    }


    public void delete(String id) {

    }


    public void readAll() {

    }
}
