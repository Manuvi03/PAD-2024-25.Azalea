package es.ucm.fdi.azalea.integration;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.MessageModel;
import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.business.model.UserModel;

public class ReadMessagesByChatUseCase {
    private final String TAG= "ReadMessagesByChatUseCase";

    public void execute(String chatId, CallBack<List<MessageModel>> cb) {
        readMessagesByChat(chatId, cb);
    }

    private void readMessagesByChat(String chatId, CallBack<List<MessageModel>> cb) {
        BusinessFactory.getInstance().getMessageRepository().readByChatId(chatId, new CallBack<List<MessageModel>>(){

            @Override
            public void onSuccess(Event.Success<List<MessageModel>> success) {
                cb.onSuccess(success);
            }

            @Override
            public void onError(Event.Error<List<MessageModel>> error) {
                cb.onError(error);
            }
        });
    }

    // caso de uso que, a partir de el id de una clase, busca a sus estudiantes
    private void readStudentsByClassRoom(String classroomId, CallBack<List<StudentModel>> cb){
        BusinessFactory.getInstance().getStudentRepository().readByClassRoomId(classroomId, new CallBack<List<StudentModel>>(){

            @Override
            public void onSuccess(Event.Success<List<StudentModel>> success) {
                cb.onSuccess(success);
            }

            @Override
            public void onError(Event.Error<List<StudentModel>> error) {
                cb.onError(error);
            }
        });
    }
}
