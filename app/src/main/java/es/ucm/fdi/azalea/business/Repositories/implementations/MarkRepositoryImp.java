package es.ucm.fdi.azalea.business.Repositories.implementations;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.ucm.fdi.azalea.business.Repositories.MarkRepository;
import es.ucm.fdi.azalea.business.model.MarkModel;
import es.ucm.fdi.azalea.integration.CallBack;
import es.ucm.fdi.azalea.integration.Event;

public class MarkRepositoryImp implements MarkRepository {

    // constantes
    private final static String TAG = "MarkRepositoryImp";

    // atributos
    // puesto que la BD no es la predeterminada por Firebase (esta se encuentra en Belgica), hay que pasar la URL
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://azalea-fde19-default-rtdb.europe-west1.firebasedatabase.app/");

    // referencia a un lugar de la BD
    private DatabaseReference marksReference = database.getReference("marks");

    @Override
    public void create(MarkModel item, CallBack<MarkModel> cb) {
        // el push crea el hijo con una key automatica
        String key = marksReference.push().getKey();
        if(key != null){
            item.setId(key);
            marksReference.child(key).setValue(item).addOnSuccessListener(task->{
                Log.d(TAG, "Nota creada correctamente con id " + key + ".");
                cb.onSuccess(new Event.Success<>(item));
            })      .addOnFailureListener(e->{
                Log.d(TAG, "Error al crear la nota", e);
                cb.onError(new Event.Error<>(e));
            });
        }else{
            cb.onError(new Event.Error<>(new Exception("Error al crear la nota ")));
        };
    }
}
