package es.ucm.fdi.azalea.business.Repositories.implementations;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.ucm.fdi.azalea.business.Repositories.ClassRoomRepository;
import es.ucm.fdi.azalea.business.model.ClassRoomModel;
import es.ucm.fdi.azalea.integration.CallBack;
import es.ucm.fdi.azalea.integration.Event;

public class ClassRoomRepositoryImp implements ClassRoomRepository {
    private final String TAG = "ClassRoomRepositoryImp";

    private final FirebaseDatabase db = FirebaseDatabase.getInstance("https://azalea-fde19-default-rtdb.europe-west1.firebasedatabase.app/");
    private final DatabaseReference classRoomReference = db.getReference("classrooms");

    @Override
    public void create(ClassRoomModel data, CallBack<ClassRoomModel> cb) {
        try{
            String key = classRoomReference.push().getKey();
            if(key != null)
            {
                data.setId(key);
                classRoomReference.child(key).setValue(data).addOnSuccessListener(event->{
                    Log.d(TAG, "ClassRoom created with key: " + data.getId());
                    cb.onSuccess(new Event.Success<>(data));
                }).addOnFailureListener(e->{
                    Log.d(TAG,"Failed to create ClassRoom",e);
                    cb.onError(new Event.Error<>(e));
                });

                cb.onSuccess(new Event.Success<>(data));
            }else{
                cb.onError(new Event.Error<>(new Exception("Error al crear la clase")));
            }
        }catch(Exception e){
            cb.onError(new Event.Error<>(e));
        }
    }

    @Override
    public void read(String id, CallBack<ClassRoomModel> cb) {

    }

    @Override
    public void update(ClassRoomModel data, CallBack<ClassRoomModel> cb) {

    }

    @Override
    public void delete(String id, CallBack<ClassRoomModel> cb) {

    }

    @Override
    public void readByIdTeacher(String idTeacher, CallBack<ClassRoomModel> cb) {

    }
}
