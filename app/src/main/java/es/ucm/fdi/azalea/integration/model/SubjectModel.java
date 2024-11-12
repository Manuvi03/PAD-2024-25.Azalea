package es.ucm.fdi.azalea.integration.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.ucm.fdi.azalea.business.Subject;

// estoy probando el firebase database (ESPECIE DE DAO)
public class SubjectModel {

    //(esta primera parte que genera la conexion a la BD habria que hacerla en otro lado)
    // puesto que la BD no es la predeterminada por Firebase (esta se encuentra en Belgica), hay que pasar la URL
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://azalea-fde19-default-rtdb.europe-west1.firebasedatabase.app/");

    // referencia a un lugar de la BD
    private DatabaseReference subjectReference = database.getReference("subject");

    //(CRUD como si fuera un DAO)
    public String create(Subject subject){
        // el push crea el hijo con una key automatica
        String key = subjectReference.push().getKey();
        if(key != null){
            // subjectReference te da el path a "/subject", con child entras a "/subject/maths" y
            // esta subject "maths" se puede rellenar directamente con la clase Java, es decir,
            // los valores en la BD de "maths" seran rellenados con los del objeto de la clase java
            // hay que imaginarse el JSON como un arbol de rutas y atributos(?)
            subjectReference.child(key).setValue(subject);
        }
        Log.d("SubjectModel", "Subject created with key: " + key);
        return key;
    }

    public void delete(String subjectId){
        subjectReference.child(subjectId).removeValue();
        Log.d("SubjectModel", "Subject removed");
    }

    public void update(String subjectId, Subject updatedSubject){
        subjectReference.child(subjectId).setValue(updatedSubject);
        Log.d("SubjectModel", "Subject updated");
    }

    public void readAll(){
        subjectReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Subject value = dataSnapshot.getValue(Subject.class);
                Log.d("SubjectModel", "Value is: " + value); // no consigo transformar en info legible
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w("SubjectModel", "Failed to read value.", error.toException());
            }
        });
    }
}
