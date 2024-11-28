package es.ucm.fdi.azalea.integration;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Collections;
import java.util.List;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.model.EventModel;

public class GetEventsFromClassroomUseCase {

    private final String TAG = "GetEventsFromClassroomUseCase";

    public GetEventsFromClassroomUseCase(){}

    public LiveData<List<EventModel>> execute(String classroomId) {
        MutableLiveData<List<EventModel>> resultLiveData = new MutableLiveData<>();
        Log.i(TAG, "Entrando en execute. ClassroomId: " + classroomId);
        BusinessFactory.getInstance().getEventRepository().getEventsForClassroom(classroomId, new CallBack<List<EventModel>>() {

            @Override
            public void onSuccess(Event.Success<List<EventModel>> success) {
                Log.d(TAG, "Eventos obtenidos correctamente. Devuelvo: " + success.getData().size() + " resultados.");
                resultLiveData.postValue(success.getData());
            }

            @Override
            public void onError(Event.Error<List<EventModel>> error) {
                Log.e(TAG, "Error al obtener los eventos");
                resultLiveData.postValue(Collections.emptyList());
            }
        });
        return resultLiveData;
    }
}
