package es.ucm.fdi.azalea.presentation.parent;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import es.ucm.fdi.azalea.integration.GetEventsForDateUseCase;

public class ParentViewModel extends AndroidViewModel {

    private final MutableLiveData<List<String>> events = new MutableLiveData<>();

    public ParentViewModel() {
        super();
    }

}
