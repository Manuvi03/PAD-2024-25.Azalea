package es.ucm.fdi.azalea.business;

import es.ucm.fdi.azalea.business.Repositories.AuthRepository;
import es.ucm.fdi.azalea.business.Repositories.ChatRepository;
import es.ucm.fdi.azalea.business.Repositories.ClassRoomRepository;
import es.ucm.fdi.azalea.business.Repositories.EventRepository;
import es.ucm.fdi.azalea.business.Repositories.MessageRepository;
import es.ucm.fdi.azalea.business.Repositories.Repository;
import es.ucm.fdi.azalea.business.Repositories.StudentRepository;
import es.ucm.fdi.azalea.business.model.ChatModel;
import es.ucm.fdi.azalea.business.model.ClassRoomModel;
import es.ucm.fdi.azalea.business.model.EventModel;
import es.ucm.fdi.azalea.business.model.MessageModel;
import es.ucm.fdi.azalea.business.model.StudentModel;
import es.ucm.fdi.azalea.business.model.UserModel;

public class BusinessFactoryImp extends BusinessFactory{
    @Override
    public Repository<StudentModel> getStudentRepository() {
        return new StudentRepository();
    }

    @Override
    public Repository<UserModel> getAuthRepository() {
        return new AuthRepository();
    }

    @Override
    public Repository<EventModel> EventRepository() {
        return new EventRepository();
    }

    @Override
    public Repository<MessageModel> getMessageRepository() {
        return new MessageRepository();
    }

    @Override
    public Repository<ChatModel> getChatRepository() {
        return new ChatRepository();
    }

    @Override
    public Repository<ClassRoomModel> getClassRoomRepository() {
        return new ClassRoomRepository();
    }
}
