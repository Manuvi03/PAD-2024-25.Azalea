package es.ucm.fdi.azalea.business;

import es.ucm.fdi.azalea.business.Repositories.ChatRepository;
import es.ucm.fdi.azalea.business.Repositories.ClassRoomRepository;
import es.ucm.fdi.azalea.business.Repositories.EventRepository;
import es.ucm.fdi.azalea.business.Repositories.MessageRepository;
import es.ucm.fdi.azalea.business.Repositories.StudentRepository;
import es.ucm.fdi.azalea.business.Repositories.UserRepository;
import es.ucm.fdi.azalea.business.Repositories.implementations.UserRepositoryImp;
import es.ucm.fdi.azalea.business.model.ChatModel;
import es.ucm.fdi.azalea.business.model.ClassRoomModel;
import es.ucm.fdi.azalea.business.model.EventModel;
import es.ucm.fdi.azalea.business.model.MessageModel;
import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.business.model.UserModel;

public abstract class BusinessFactory {
    private static BusinessFactory instance;

    public static BusinessFactory getInstance(){
        if(instance == null){
            instance = new BusinessFactoryImp();
        }

        return instance;
    }

    public abstract StudentRepository getStudentRepository();
    public abstract EventRepository getEventRepository();
    public abstract MessageRepository getMessageRepository();
    public abstract ChatRepository getChatRepository();
    public abstract ClassRoomRepository getClassRoomRepository();
    public abstract UserRepository getUserRepository();
}
