package es.ucm.fdi.azalea.business.Repositories.implementations;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.ucm.fdi.azalea.business.Repositories.ClassRoomRepository;
import es.ucm.fdi.azalea.business.model.ClassRoomModel;
import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.integration.CallBack;
import es.ucm.fdi.azalea.integration.Event;

public class ClassRoomRepositoryImp implements ClassRoomRepository {

    // constantes
    private final static String TAG = "ClassRoomRepositoryImp";

    // atributos
    // puesto que la BD no es la predeterminada por Firebase (esta se encuentra en Belgica), hay que pasar la URL
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://azalea-fde19-default-rtdb.europe-west1.firebasedatabase.app/");

    // referencia a un lugar de la BD
    private DatabaseReference classroomReference = database.getReference("classrooms");


    @Override
    public void readById(String id, CallBack<ClassRoomModel> cb) {
        // se ejecuta la query, obteniendo una imagen unica de Firebase
        classroomReference.child(id).get().addOnCompleteListener(task -> {
            if(!task.isSuccessful()){
                // si no se puede realizar la busqueda, se devuelve un error
                Log.d(TAG,"Error recuperando los datos de la clase", task.getException());
                cb.onError(new Event.Error<>(task.getException()));
            }
            else {
                Log.d(TAG, "ClassRoomRepositoryImp devolvio la clase con id "  + id);

                // se devuelve un exito y la informacion, que es el estudiante
                cb.onSuccess(new Event.Success<>(task.getResult().getValue(ClassRoomModel.class)));
            }
        });
    }
}
