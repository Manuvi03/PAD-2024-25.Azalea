package es.ucm.fdi.azalea.business.Repositories;

import java.util.Collections;
import java.util.List;

import es.ucm.fdi.azalea.business.model.EventModel;
import es.ucm.fdi.azalea.business.model.UserModel;

public class EventRepository implements Repository<EventModel>{
    @Override
    public String create(EventModel item) {
        return "";
    }

    @Override
    public EventModel findById(String id) {
        return null;
    }

    @Override
    public String update(EventModel item) {
        return "";
    }

    @Override
    public String delete(String id) {
        return "";
    }

    @Override
    public List<EventModel> readAll() {
        return Collections.emptyList();
    }
}
/*
package es.ucm.fdi.azalea.business.Repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// estoy probando el firebase database (ESPECIE DE DAO)
public class SubjectRepository {

    //(esta primera parte que genera la conexion a la BD habria que hacerla en otro lado)
    // puesto que la BD no es la predeterminada por Firebase (esta se encuentra en Belgica), hay que pasar la URL
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://azalea-fde19-default-rtdb.europe-west1.firebasedatabase.app/");

    // referencia a un lugar de la BD
    private DatabaseReference subjectReference = database.getReference("subject");

    //(CRUD como si fuera un DAO)
    public String create(SubjectModel subject){
        // el push crea el hijo con una key automatica
        String key = subjectReference.push().getKey();
        if(key != null){
            // subjectReference te da el path a "/subject", con child entras a "/subject/maths" y
            // esta subject "maths" se puede rellenar directamente con la clase Java, es decir,
            // los valores en la BD de "maths" seran rellenados con los del objeto de la clase java
            // hay que imaginarse el JSON como un arbol de rutas y atributos(?)
            subjectReference.child(key).setValue(subject);
        }
        Log.d("SubjectRepository", "Subject created with key: " + key);
        return key;
    }

    public void delete(String subjectId){
        subjectReference.child(subjectId).removeValue();
        Log.d("SubjectRepository", "Subject removed");
    }

    public void update(String subjectId, SubjectModel updatedSubject){
        subjectReference.child(subjectId).setValue(updatedSubject);
        Log.d("SubjectRepository", "Subject updated");
    }

    public void readAll(){
        subjectReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                SubjectModel value = dataSnapshot.getValue(SubjectModel.class);
                Log.d("SubjectRepository", "Value is: " + value); // no consigo transformar en info legible
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w("SubjectRepository", "Failed to read value.", error.toException());
            }
        });
    }
}


 */