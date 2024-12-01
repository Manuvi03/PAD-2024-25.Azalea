package es.ucm.fdi.azalea.business.Repositories.implementations;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.ucm.fdi.azalea.business.Repositories.ChatRepository;
import es.ucm.fdi.azalea.business.model.ChatModel;
import es.ucm.fdi.azalea.integration.CallBack;
import es.ucm.fdi.azalea.integration.Event;

public class ChatRepositoryImp implements ChatRepository {

    private final static String TAG = "ChatRepository";
    private final FirebaseDatabase database = FirebaseDatabase.getInstance("https://azalea-fde19-default-rtdb.europe-west1.firebasedatabase.app/");
    private final DatabaseReference chatsReference = database.getReference("chats");

    @Override
    public void create(ChatModel model, CallBack<ChatModel> cb) {
        Log.i(TAG, "Create del chat");
        //Si llega aqui significa que el padre no estaba en la base de datos y por tanto el chat es nuevo.
        try{
            chatsReference.child(model.getId()).setValue(model);
            Log.d(TAG, "Chat created with key: " + model.getId());
            cb.onSuccess(new Event.Success<>(model));
        }catch(Exception e){
            cb.onError(new Event.Error<>(e));
        }
    }

    @Override
    public void readById(String id, CallBack<ChatModel> cb) {

        chatsReference.child(id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    Log.d(TAG,"Error recuperando los datos",task.getException());
                    cb.onError(new Event.Error<ChatModel>(task.getException()));

                }
                else {
                    Log.d(TAG, String.valueOf(task.getResult().getValue()));
                    cb.onSuccess(new Event.Success<ChatModel>(task.getResult().getValue(ChatModel.class)));
                }
            }
        });

    }


}
