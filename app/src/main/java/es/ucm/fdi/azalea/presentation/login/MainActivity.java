 package es.ucm.fdi.azalea.presentation.login;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.Subject;
import es.ucm.fdi.azalea.integration.model.SubjectModel;

 public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });





        // estoy probando el firebase database
        Subject subject1 = new Subject("Maths");
        Subject subject2 = new Subject("PE");
        Subject modifiedSubject = new Subject("English");

        SubjectModel sm = new SubjectModel();

        String key1 = sm.create(subject1);
        String key2 = sm.create(subject2);

        sm.readAll();

        // investigando algunos guardan la key en el transfer otros no... no entiendo como hacerlo
        // bien esto, de momento la devuelvo del create para ir probando
        sm.update(key1, modifiedSubject);

        sm.readAll();

        sm.delete(key2);

        // todos estos mensajes salen por consola
        sm.readAll();
    }
}