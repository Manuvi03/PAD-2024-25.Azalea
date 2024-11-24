package es.ucm.fdi.azalea.business.Repositories;

import java.util.List;

import es.ucm.fdi.azalea.business.model.EventModel;
import es.ucm.fdi.azalea.integration.CallBack;


public interface EventRepository {

    void create(EventModel em, CallBack<EventModel> cb);
    void getEventsForDate(String date, CallBack<List<EventModel>> cb);
    void getEventsForClassroom(String idclassroom, CallBack<List<EventModel>> cb);
    }
