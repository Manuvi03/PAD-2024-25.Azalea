package es.ucm.fdi.azalea.integration;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.Repositories.implementations.EventRepositoryImp;
import es.ucm.fdi.azalea.business.model.EventModel;

public class ModifyEventUseCase {

    EventRepositoryImp eventRepositoryImp;

    public ModifyEventUseCase(){
        eventRepositoryImp = new EventRepositoryImp();
    }

    public LiveData<EventModel> execute(EventModel em){
        MutableLiveData<EventModel> resultLiveData = new MutableLiveData<>();

        BusinessFactory.getInstance().getEventRepository().modify(em, new CallBack<EventModel>() {

            @Override
            public void onSuccess(Event.Success<EventModel> success) {
                resultLiveData.postValue(success.getData());
            }

            @Override
            public void onError(Event.Error<EventModel> error) {
                resultLiveData.postValue(null);
            }
        });
        return resultLiveData;
    }
}
