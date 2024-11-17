package es.ucm.fdi.azalea.business;

import es.ucm.fdi.azalea.business.Repositories.Repository;
import es.ucm.fdi.azalea.business.Repositories.UserRepository;
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

    public abstract Repository<StudentModel> getStudentRepository();
    public abstract Repository<EventModel> EventRepository();
    public abstract Repository<MessageModel> getMessageRepository();
    public abstract Repository<ChatModel> getChatRepository();
    public abstract Repository<ClassRoomModel> getClassRoomRepository();
    public abstract UserRepository getUserRepository();
}
