package es.ucm.fdi.azalea.integration;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.EventModel;

public class GetEventsForDateUseCase{
    public <T> Event<T> getEventsForDateUseCase(){
        try{
            List<EventModel> list = new ArrayList<>();
            list = BusinessFactory.getInstance().getEventRepository().readAll();
            return new Event.Success<>(null);
        }
        catch(Exception e){
            return new Event.Error<>(null);
        }
    }
}
