package es.ucm.fdi.azalea.business.Repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import es.ucm.fdi.azalea.business.model.StudentModel;

public class StudentRepositoryImp implements StudentRepository {

    // puesto que la BD no es la predeterminada por Firebase (esta se encuentra en Belgica), hay que pasar la URL
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://azalea-fde19-default-rtdb.europe-west1.firebasedatabase.app/");

    // referencia a un lugar de la BD
    private DatabaseReference studentReference = database.getReference("student");

    @Override
    public String create(StudentModel item){
        // el push crea el hijo con una key automatica
        String key = studentReference.push().getKey();
        if(key != null){
            studentReference.child(key).setValue(item);
        }
        Log.d("StudentRepository", "Alumno creado con id: " + key + ".");
        return key;
    }

    @Override
    public StudentModel readById(String id) {
        return null;
    }

    @Override
    public String update(String id, StudentModel item) {
        /*
        subjectReference.child(subjectId).setValue(updatedSubject);
        Log.d("SubjectRepository", "Subject updated");*/
        return "";
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

    public List<StudentModel> readByClassRoomId(String classroomId){

        // lista de alumnos
        List<StudentModel> list = new ArrayList<StudentModel>();

        // se genera la query
        Query readByClassRoomIdQuery =  studentReference.orderByChild("surnames").equalTo(classroomId);

        // se ejecuta la query
        readByClassRoomIdQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                    // por cada alumno encontrado, si existe se anyade a la lista
                    if(studentSnapshot.exists()) {
                        list.add(studentSnapshot.getValue(StudentModel.class));
                    }
                }
                Log.d("StudentRepository", "Devuelta una lista con " + list.size() + " resultados.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w("StudentRepository", "Excepcion al leer el valor.", error.toException());
            }
        });

        return list;
    }
}
