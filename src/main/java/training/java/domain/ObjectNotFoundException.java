package training.java.domain;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(Class objectType, String id) {
        super("Couldn't find object " + objectType.getSimpleName() + " with id: " + id);
    }
}
