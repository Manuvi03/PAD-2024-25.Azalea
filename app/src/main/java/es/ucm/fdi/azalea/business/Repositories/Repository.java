package es.ucm.fdi.azalea.business.Repositories;


import com.google.android.datatransport.Event;

import java.util.List;

public interface Repository<T> {
    public String create(T item, Event<T> callback);
    public void findById(String id, Event<T> callback);
    public String update(T item, Event<T> callback);
    public String delete(String id, Event <T> callback);
    public List<T> readAll(Event<T> callback);
}
