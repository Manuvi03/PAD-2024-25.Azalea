package es.ucm.fdi.azalea.business;

import es.ucm.fdi.azalea.business.Repositories.AuthRepository;
import es.ucm.fdi.azalea.business.Repositories.ChatRepository;
import es.ucm.fdi.azalea.business.Repositories.ClassRoomRepository;
import es.ucm.fdi.azalea.business.Repositories.EventRepository;
import es.ucm.fdi.azalea.business.Repositories.MessageRepository;
import es.ucm.fdi.azalea.business.Repositories.StudentRepository;
import es.ucm.fdi.azalea.business.Repositories.UserRepository;
import es.ucm.fdi.azalea.business.Repositories.implementations.AuthRepositoryImp;
import es.ucm.fdi.azalea.business.Repositories.implementations.ChatRepositoryImp;
import es.ucm.fdi.azalea.business.Repositories.implementations.ClassRoomRepositoryImp;
import es.ucm.fdi.azalea.business.Repositories.implementations.EventRepositoryImp;
import es.ucm.fdi.azalea.business.Repositories.implementations.MessageRepositoryImp;
import es.ucm.fdi.azalea.business.Repositories.implementations.StudentRepositoryImp;
import es.ucm.fdi.azalea.business.Repositories.implementations.UserRepositoryImp;
import es.ucm.fdi.azalea.business.model.ChatModel;
import es.ucm.fdi.azalea.business.model.ClassRoomModel;
import es.ucm.fdi.azalea.business.model.EventModel;
import es.ucm.fdi.azalea.business.model.MessageModel;
import es.ucm.fdi.azalea.business.model.StudentModel;

public class BusinessFactoryImp extends BusinessFactory{


    @Override
    public StudentRepository getStudentRepository() {
        return new StudentRepositoryImp();
    }

    @Override
    public EventRepository EventRepository() {
        return new EventRepositoryImp();
    }

    @Override
    public MessageRepository getMessageRepository() {
        return new MessageRepositoryImp();
    }

    @Override
    public ChatRepository getChatRepository() {
        return new ChatRepositoryImp();
    }

    @Override
    public ClassRoomRepository getClassRoomRepository() {
        return new ClassRoomRepositoryImp();
    }

    @Override
    public UserRepository getUserRepository() {
        return new UserRepositoryImp();
    }

    @Override
    public AuthRepository getAuthRepository() {
        return new AuthRepositoryImp();
    }
}
