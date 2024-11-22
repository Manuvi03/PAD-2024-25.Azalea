package es.ucm.fdi.azalea.integration;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Collections;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.Repositories.implementations.EventRepositoryImp;
import es.ucm.fdi.azalea.business.model.EventModel;

public class CreateEventUseCase {

    EventRepositoryImp eventRepositoryImp;

    public CreateEventUseCase(EventModel em){
        eventRepositoryImp = new EventRepositoryImp();
    }

    public void execute(EventModel em){
        MutableLiveData<EventModel> resultLiveData = new MutableLiveData<>();

        BusinessFactory.getInstance().getEventRepository().create(em, new CallBack<EventModel>() {
            @Override
            public void onSuccess(Event.Success<EventModel> success) {
            resultLiveData.postValue(success.getData());
            }

            @Override
            public void onError(Event.Error<EventModel> error) {
                resultLiveData.postValue(error.);
            }
        });
    }

}
