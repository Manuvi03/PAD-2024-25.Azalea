package es.ucm.fdi.azalea.presentation.chat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.model.UserModel;

public class chatActivity extends AppCompatActivity {

    UserModel otherUser; //TODO mirar si el nombre el padre o el hijo

    EditText messageInput;
    ImageButton sendButton;
    ImageButton backButton;
    TextView otherUserName;
    RecyclerView recyclerView;
    chatViewModel chatViewModel;

    /*TODO mirar como me llegaría el otro usuario desde la anterior vista*/
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        chatViewModel = new chatViewModel();
        //getOtherUser

        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_message_button);
        backButton = findViewById(R.id.back_button);
        recyclerView = findViewById(R.id.chat_recycler_view);

        backButton.setOnClickListener((v)->{
            //TODO volver a la anterior que es la pagina del alumno o en el caso
            // del padre volveria al iniciop o a la vista de eventos.
        });

        chatViewModel.getOrCreateChat();
    }

}
