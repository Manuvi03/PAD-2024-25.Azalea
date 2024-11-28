package es.ucm.fdi.azalea.integration;

public interface CallBack<T> {
    public void onSuccess(Event.Success<T> success);
    public void onError(Event.Error<T> error);

}
