package es.ucm.fdi.azalea.business.Repositories.implementations;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import es.ucm.fdi.azalea.business.Repositories.StudentRepository;
import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.integration.CallBack;
import es.ucm.fdi.azalea.integration.Event;

public class StudentRepositoryImp implements StudentRepository {

    // constantes
    private final static String TAG = "StudentRepositoryImp";

    // atributos
    // puesto que la BD no es la predeterminada por Firebase (esta se encuentra en Belgica), hay que pasar la URL
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://azalea-fde19-default-rtdb.europe-west1.firebasedatabase.app/");

    // referencia a un lugar de la BD
    private DatabaseReference studentReference = database.getReference("students");

    @Override
    public void create(StudentModel item, CallBack<StudentModel> cb){
        // el push crea el hijo con una key automatica
        String key = studentReference.push().getKey();
        if(key != null){
            item.setId(key);
            studentReference.child(key).setValue(item).addOnSuccessListener(task->{
                Log.d("StudentRepository", "Alumno creado correctamente");
                cb.onSuccess(new Event.Success<>(item));
            })      .addOnFailureListener(e->{
                Log.d("StudentRepository", "Error al crear el alumno", e);
                cb.onError(new Event.Error<>(e));
            });
        }else{
            cb.onError(new Event.Error<>(new Exception("Error al crear el alumno")));
        }
        Log.d("StudentRepository", "Alumno creado con id: " + key + ".");

    }

    @Override
    public void readById(String id, CallBack<StudentModel> cb) {

        // se ejecuta la query, obteniendo una imagen unica de Firebase
        studentReference.child(id).get().addOnCompleteListener(task -> {
            if(!task.isSuccessful()){
                // si no se puede realizar la busqueda, se devuelve un error
                Log.d(TAG,"Error recuperando los datos del estudiante", task.getException());
                cb.onError(new Event.Error<>(task.getException()));
            }
            else {
                Log.d(TAG, "StudentRepository devolvio el estudiante con id "  + id);

                // se devuelve un exito y la informacion, que es el estudiante
                cb.onSuccess(new Event.Success<>(task.getResult().getValue(StudentModel.class)));
            }
        });
    }

    @Override
    public void update(String id, StudentModel item, CallBack<StudentModel> cb) {
        studentReference.child(id).setValue(item).addOnSuccessListener(task->{
            Log.d(TAG, "Estudiante con id " + id + " modificado correctamente");
            cb.onSuccess(new Event.Success<>(item));
        })      .addOnFailureListener(e->{
            Log.d(TAG, "Error al modificar el estudiante", e);
            cb.onError(new Event.Error<>(e));
        });
    }

    @Override
    public String delete(String item){
        studentReference.child(item).removeValue();
        Log.d("StudentRepository", "Alumno con id " + item + " eliminado.");
        return item;
    }

    @Override
    public List<StudentModel> readAll() {
        return Collections.emptyList();
    }

    // busca los estudiantes de una clase dada
    public void readByClassRoomId(String classroomId, CallBack<List<StudentModel>> cb){

        // lista de alumnos
        List<StudentModel> list = new ArrayList<>();

        // se genera la query que ordena los hijos del nodo estudiantes por el id de la clase y busca
        // aquellos que coincidan con el parametro pasado
        // en SQL es equivalente a SELECT * FROM students WHERE s.clasroomId = classroomId
        Query readByClassRoomIdQuery = studentReference.orderByChild("classroomId").equalTo(classroomId);

        // se ejecuta la query, obteniendo una imagen unica de Firebase
        readByClassRoomIdQuery.get().addOnCompleteListener(task -> {
            if(!task.isSuccessful()){
                // si no se puede realizar la busqueda, se devuelve un error
                Log.d(TAG,"Error recuperando los datos",task.getException());
                cb.onError(new Event.Error<>(task.getException()));
            }
            else {
                for(DataSnapshot studentSnapshot : task.getResult().getChildren()){
                    // por cada alumno encontrado, si existe se anyade a la lista
                    if(studentSnapshot.exists()) {
                        list.add(studentSnapshot.getValue(StudentModel.class));
                    }
                }
                Log.d(TAG, "StudentRepository devolvio una lista con " + list.size() + " resultados.");

                // se devuelve un exito y la informacion
                cb.onSuccess(new Event.Success<>(list));
            }
        });
    }
}
