package es.ucm.fdi.azalea.business.Repositories;


import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.List;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.ucm.fdi.azalea.business.model.UserModel;

public class UserRepository implements Repository<UserModel> {

    private final static String TAG = "UserRepository";
    private UserModel userdata;
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://azalea-fde19-default-rtdb.europe-west1.firebasedatabase.app/");
    private DatabaseReference usersReference = database.getReference("users");

    @Override
    public String create(UserModel item) {


        usersReference.child(item.getId()).setValue(item);
        Log.d(TAG, "User created with key: " + item.getId());
        return item.getId();
    }

    @Override
    public UserModel findById(String id) {
        userdata = null;
        usersReference.child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    Log.d(TAG,"Error recuperando los datos",task.getException());

                }
                else {
                    Log.d(TAG,String.valueOf(task.getResult().getValue()));
                    userdata = task.getResult().getValue(UserModel.class);
                }
            }
        });

        return userdata;
    }

    @Override
    public String update(UserModel item) {
        return "";
    }

    @Override
    public String delete(String id) {
        return "";
    }

    @Override
    public List<UserModel> readAll() {
        return Collections.emptyList();
    }
}
