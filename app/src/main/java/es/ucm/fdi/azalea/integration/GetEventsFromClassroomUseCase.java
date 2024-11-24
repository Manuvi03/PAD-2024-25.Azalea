package es.ucm.fdi.azalea.integration;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Collections;
import java.util.List;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.EventModel;

public class GetEventsFromClassroomUseCase {

    public GetEventsFromClassroomUseCase(){}

    public LiveData<List<EventModel>> execute(String classroomId) {
        MutableLiveData<List<EventModel>> resultLiveData = new MutableLiveData<>();

        BusinessFactory.getInstance().getEventRepository().getEventsForClassroom(classroomId, new CallBack<List<EventModel>>() {

            @Override
            public void onSuccess(Event.Success<List<EventModel>> success) {
                resultLiveData.postValue(success.getData());
            }

            @Override
            public void onError(Event.Error<List<EventModel>> error) {
                resultLiveData.postValue(Collections.emptyList());
            }
        });
        return resultLiveData;
    }
}
