package es.ucm.fdi.azalea.business.Repositories.implementations;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.azalea.business.Repositories.MessageRepository;
import es.ucm.fdi.azalea.business.model.MessageModel;
import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.integration.CallBack;
import es.ucm.fdi.azalea.integration.Event;

public class MessageRepositoryImp implements MessageRepository {

    private final static String TAG = "MessageRepositoryImp";

    // puesto que la BD no es la predeterminada por Firebase (esta se encuentra en Belgica), hay que pasar la URL
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://azalea-fde19-default-rtdb.europe-west1.firebasedatabase.app/");

    // referencia a los mensajes de la bd
    private DatabaseReference messagesReference = database.getReference("messages");

    @Override
    public void create(MessageModel model, CallBack<MessageModel> cb) {
        // el push crea el hijo con una key automatica
        String key = messagesReference.push().getKey();
        if(key != null){
            model.setId(key);
            messagesReference.child(key).setValue(model).addOnSuccessListener(task->{
                Log.d(TAG, "Mensaje creado correctamente");
                cb.onSuccess(new Event.Success<>(model));
            })      .addOnFailureListener(e->{
                Log.d(TAG, "Error al crear el mensaje", e);
                cb.onError(new Event.Error<>(e));
            });
        }else{
            cb.onError(new Event.Error<>(new Exception("Error al crear el mensaje")));
        }
        Log.d(TAG, "Mensaje creado con id: " + key + ".");
    }

    @Override
    public void findById(String id, CallBack<MessageModel> cb) {

    }

    @Override
    public void readByChatId(String chatId, CallBack<List<MessageModel>> cb) {
        // lista de mensajes
        Log.d(TAG, "llego hasta aqui");
        List<MessageModel> list = new ArrayList<>();

        // se genera la query que ordena los hijos del nodo mensajes por el id del chat
        //y los ordena segun este.
        Query readByChatIdQuery = messagesReference.orderByChild("chatId").equalTo(chatId);

        // se ejecuta la query, obteniendo una imagen unica de Firebase
        readByChatIdQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot messageSnapshot : snapshot.getChildren()){
                    // por cada mensaje encontrado, si existe se anyade a la lista
                    if(messageSnapshot.exists()) {
                        list.add(messageSnapshot.getValue(MessageModel.class));
                    }
                }
                Log.d(TAG, "Se devolvio una lista con " + list.size() + " mensajes en el chat " + chatId);
                // se devuelve un exito y la informacion
                cb.onSuccess(new Event.Success<>(list));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // si no se puede realizar la busqueda, se devuelve un error
                Log.d(TAG,"Error recuperando los datos", error.toException());
                cb.onError(new Event.Error<>(error.toException()));
            }
        }); /*{
            if(!task.isSuccessful()){
                // si no se puede realizar la busqueda, se devuelve un error
                Log.d(TAG,"Error recuperando los datos",task.getException());
                cb.onError(new Event.Error<>(task.getException()));
            }
            else {
                for(DataSnapshot messageSnapshot : task.getResult().getChildren()){
                    // por cada mensaje encontrado, si existe se anyade a la lista
                    if(messageSnapshot.exists()) {
                        list.add(messageSnapshot.getValue(MessageModel.class));
                    }
                }
                Log.d(TAG, "Se devolvio una lista con " + list.size() + " mensajes en el chat " + chatId);

                // se devuelve un exito y la informacion
                cb.onSuccess(new Event.Success<>(list));
            }
        });*/
    }
}
