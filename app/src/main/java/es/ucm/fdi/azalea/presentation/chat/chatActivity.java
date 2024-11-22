package es.ucm.fdi.azalea.presentation.chat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
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
    String chatRoomId;

    EditText messageInput;
    ImageButton sendButton;
    ImageButton backButton;
    TextView otherUserName;
    RecyclerView recyclerView;
    chatViewModel chatViewModel;

    /*TODO mirar como me llegaría el otro usuario desde la anterior vista*/
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        chatViewModel = new chatViewModel();
        //TODO hacer una func que devuelca el UserModel del getIntent();
        //chatRoomId = chatViewModel.getChatRoomId(FirebaseAuth.getInstance().getUid(), otherUser.getId()); TODO esto va en caso de uso a parte

        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_message_button);
        backButton = findViewById(R.id.back_button);
        //TODO nombre de arriba
        recyclerView = findViewById(R.id.chat_recycler_view);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        chatViewModel.getOrCreateChat(chatRoomId);
    }

}
