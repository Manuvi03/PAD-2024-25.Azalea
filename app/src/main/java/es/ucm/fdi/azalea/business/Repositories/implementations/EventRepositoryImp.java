package es.ucm.fdi.azalea.business.Repositories.implementations;

import java.util.List;

import es.ucm.fdi.azalea.business.Repositories.EventRepository;
import es.ucm.fdi.azalea.business.model.EventModel;
import es.ucm.fdi.azalea.integration.CallBack;

public class EventRepositoryImp implements EventRepository {

    @Override
    public void create(EventModel em, CallBack<List<EventModel>> cb) {

    }
}
