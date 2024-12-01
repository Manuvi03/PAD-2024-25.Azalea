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

import es.ucm.fdi.azalea.business.Repositories.MarkRepository;
import es.ucm.fdi.azalea.business.model.MarkModel;
import es.ucm.fdi.azalea.business.model.StudentModel;
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

    @Override
    public void update(String id, MarkModel item, CallBack<MarkModel> cb) {
        marksReference.child(id).setValue(item).addOnSuccessListener(task->{
            Log.d(TAG, "Nota con id " + id + " modificada correctamente");
            cb.onSuccess(new Event.Success<>(item));
        })      .addOnFailureListener(e->{
            Log.d(TAG, "Error al modificar la nota", e);
            cb.onError(new Event.Error<>(e));
        });
    }

    @Override
    public void findById(String id, CallBack<MarkModel> cb) {

        // se ejecuta la query, obteniendo una imagen unica de Firebase
        marksReference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Log.d(TAG, "MarkRepositoryImp devolvio la nota con id "  + id);

                    // se devuelve un exito y la informacion, que es la nota
                    cb.onSuccess(new Event.Success<>(snapshot.getValue(MarkModel.class)));
                }else{
                    Log.d(TAG, "No existe la nota con id: "  + id);
                    cb.onError(new Event.Error<>());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // si no se puede realizar la busqueda, se devuelve un error
                Log.d(TAG,"Error recuperando los datos de la nota", error.toException());
                cb.onError(new Event.Error<>(error.toException()));
            }
        });

        /*
        marksReference.child(id).get().addOnCompleteListener(task -> {
            if(!task.isSuccessful()){
                // si no se puede realizar la busqueda, se devuelve un error
                Log.d(TAG,"Error recuperando los datos de la nota", task.getException());
                cb.onError(new Event.Error<>(task.getException()));
            }
            else {
                Log.d(TAG, "MarkRepositoryImp devolvio la nota con id "  + id);

                // se devuelve un exito y la informacion, que es la nota
                cb.onSuccess(new Event.Success<>(task.getResult().getValue(MarkModel.class)));
            }
        });*/
    }

    @Override
    public void readByStudentId(String studentId, CallBack<List<MarkModel>> cb) {

        // lista de notas del estudiante
        List<MarkModel> list = new ArrayList<>();

        // se genera la query que ordena los hijos del nodo notas por el id de estudiante y busca
        // aquellos que coincidan con el parametro pasado
        // en SQL es equivalente a SELECT * FROM marks WHERE s.studentId = studentId
        Query readByStudentIdQuery = marksReference.orderByChild("studentId").equalTo(studentId);

        // se ejecuta la query, obteniendo una imagen unica de Firebase
        readByStudentIdQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot markSnapshot : snapshot.getChildren()){
                    // por cada alumno encontrado, si existe se anyade a la lista
                    if(markSnapshot.exists()) {
                        list.add(markSnapshot.getValue(MarkModel.class));
                    }
                }
                Log.d(TAG, "MarkRepository devolvio una lista con " + list.size() + " resultados.");

                // se devuelve un exito y la informacion
                cb.onSuccess(new Event.Success<>(list));
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG,"Error recuperando los datos", error.toException());
                cb.onError(new Event.Error<>(error.toException()));
            }
        });
        /*
        readByStudentIdQuery.get().addOnCompleteListener(task -> {
            if(!task.isSuccessful()){
                // si no se puede realizar la busqueda, se devuelve un error
                Log.d(TAG,"Error recuperando los datos",task.getException());
                cb.onError(new Event.Error<>(task.getException()));
            }
            else {
                for(DataSnapshot markSnapshot : task.getResult().getChildren()){
                    // por cada alumno encontrado, si existe se anyade a la lista
                    if(markSnapshot.exists()) {
                        list.add(markSnapshot.getValue(MarkModel.class));
                    }
                }
                Log.d(TAG, "MarkRepository devolvio una lista con " + list.size() + " resultados.");

                // se devuelve un exito y la informacion
                cb.onSuccess(new Event.Success<>(list));
            }
        });*/
    }
}
