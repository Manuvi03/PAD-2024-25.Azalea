package es.ucm.fdi.azalea.presentation.chat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import es.ucm.fdi.azalea.R;
import es.ucm.fdi.azalea.business.model.ChatModel;
import es.ucm.fdi.azalea.business.model.MessageModel;
import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.business.model.UserModel;
import es.ucm.fdi.azalea.integration.Event;
import es.ucm.fdi.azalea.presentation.classroom.ClassroomListAdapter;

public class chatActivity extends AppCompatActivity {

    private final String TAG = "chatActivity";

    private String parentId; //TODO mirar si el nombre el padre o el hijo
    private String classId;
    private String chatRoomId;
    private MessagesChatAdapter adapter;
    private List<MessageModel> messages;

    private EditText messageInput;
    private ImageButton sendButton;
    private ImageButton backButton;
    private TextView otherUserName;
    private RecyclerView recyclerView;
    private chatViewModel chatViewModel;

    /*TODO mirar como me llegaría el otro usuario desde la anterior vista*/
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        chatViewModel = new chatViewModel();
        classId = getIntent().getStringExtra("classId"); //Cogemos el id de la clase pq forma parte del id del chat
        parentId = getIntent().getStringExtra("parentId"); //El id del padre tambien forma parte

        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_message_button);
        backButton = findViewById(R.id.back_button);
        otherUserName = findViewById(R.id.Username);
        otherUserName.setText(getIntent().getStringExtra("parentName"));

        initListeners();

        initRecycler();


    }

    private void initListeners(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        chatRoomId = chatViewModel.getChatId(classId, parentId);

        sendButton.setOnClickListener((e) -> {
            String message = messageInput.getText().toString().trim();
            if(message.isEmpty())
                return;

            messageInput.setText("");
            initRecycler();
            //Mandamos el mensaje si no está vacío y en caso de mandarlo actualizamos el chat.
        });

    }

    private void initRecycler() {
        // se encuentra y ajusta la recycler view
        // de la vista
        recyclerView = findViewById(R.id.chat_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // se crea el adaptador de la RecyclerView, de momento con una lista vacia
        adapter = new MessagesChatAdapter(Collections.emptyList(),"", this);
        recyclerView.setAdapter(adapter);

        chatViewModel.readMessagesByChat(chatRoomId);

        Log.d(TAG, "Hasta aqui llega bien");
        // se inicializa el observador
        initRecyclerViewObserver();
        Log.d(TAG, "Hasta aqui llega bien2");
    }

    // inicializa el observador de la recyclerview
    private void initRecyclerViewObserver(){

        // se declara el observador del recyclerview
        final Observer<Event<List<MessageModel>>> messagesStateObserver = listEvent -> {

            // mientras se busca, se muestra el texto de cargando
            if(listEvent instanceof Event.Loading){
                Log.d(TAG, "Se estan buscando los mensajes del chat");
            }

            // cuando se encuentran los resultados, se muestran
            else if(listEvent instanceof Event.Success){
                Log.d(TAG, "Se encontraron los mensajes del chat");
                // se obtiene la info
                messages = ((Event.Success<List<MessageModel>>) listEvent).getData();

                // actualiza la lista de datos en el adaptador
                adapter.setmMessagesData(messages);

                // se actualiza la recyclerview
                handleMessages(messages);

                //En caso de no entrar en la pantalla avanzaria al ultimo mensaje
                recyclerView.scrollToPosition(messages.size() -1);
            }

            // en caso de error, se muestra al usuario
            else if(listEvent instanceof Event.Error){
                Log.d(TAG, "Hubo algun error buscando los mensajes en la BD");
            }
        };

        // se inicializa el observador
        chatViewModel.getMessagesListState().observe(this, messagesStateObserver);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void handleMessages(List<MessageModel> messages) {
        // notifica al recycler view el cambio de datos
        adapter.notifyDataSetChanged();
    }

}
