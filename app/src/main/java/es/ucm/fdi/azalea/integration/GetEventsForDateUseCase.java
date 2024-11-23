package es.ucm.fdi.azalea.integration;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;

import java.util.Collections;
import java.util.List;

import es.ucm.fdi.azalea.business.BusinessFactory;
import es.ucm.fdi.azalea.business.Repositories.EventRepository;
import es.ucm.fdi.azalea.business.Repositories.implementations.EventRepositoryImp;
import es.ucm.fdi.azalea.business.model.EventModel;

public class GetEventsForDateUseCase{

    EventRepository eventRepository;

    public GetEventsForDateUseCase(){}

    public LiveData<List<EventModel>> execute(String date) {
        MutableLiveData<List<EventModel>> resultLiveData = new MutableLiveData<>();

        // Llama al repositorio para obtener eventos de la fecha
        BusinessFactory.getInstance().getEventRepository().getEventsForDate(date, new CallBack<List<EventModel>>() {
            @Override
            public void onSuccess(Event.Success<List<EventModel>> success) {
                Log.d("GetEventsForDateUseCase", "onSuccess: " + success.getData());
                // Publica los datos obtenidos en el LiveData
                resultLiveData.postValue(success.getData());
            }

            @Override
            public void onError(Event.Error<List<EventModel>> error) {
                Log.d("GetEventsForDateUseCase", "onError: ");
                // Publica una lista vacía o maneja el error
                resultLiveData.postValue(Collections.emptyList());
            }
        });

        return resultLiveData;
    }

}
