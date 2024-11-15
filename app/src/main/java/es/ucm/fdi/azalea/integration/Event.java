package es.ucm.fdi.azalea.integration;

//clase evento que implementa las respuestas que pueden dar los casos de uso al ser llamados por el viewModel
public abstract class Event<T> {

    private Event() {
    }

    public static final class Success<T> extends Event<T> {
        private final T data;

        public Success(T data) {
            this.data = data;
        }

        public T getData() {
            return data;
        }
    }

    public static final class Error<T> extends Event<T> {
        private final Exception exception;

        public Error(Exception exception) {
            this.exception = exception;
        }

        public Exception getException() {
            return exception;
        }
    }
    //este evento se podra usar para actualizar la vista si es necesario que muestre una accion de loading como en el caso de un recycler view
    public static final class Loading<T> extends Event<T> {
        public Loading() {
        }
    }

    public static final class Empty<T> extends Event<T> {
        public Empty() {
        }
    }
}
