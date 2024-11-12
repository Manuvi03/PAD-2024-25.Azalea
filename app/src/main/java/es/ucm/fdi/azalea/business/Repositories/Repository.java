package es.ucm.fdi.azalea.business.Repositories;


import java.util.List;

public interface Repository<T> {
    public String create(T item);
    public T findById(int id);
    public String update(T item);
    public String delete(int id);
    public List<T> readAll();
}
