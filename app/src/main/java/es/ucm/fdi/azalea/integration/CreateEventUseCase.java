package es.ucm.fdi.azalea.integration;

import es.ucm.fdi.azalea.business.Repositories.implementations.EventRepositoryImp;

public class CreateEventUseCase {

    EventRepositoryImp eventRepositoryImp;

    public CreateEventUseCase(){
        eventRepositoryImp = new EventRepositoryImp();
    }

}
