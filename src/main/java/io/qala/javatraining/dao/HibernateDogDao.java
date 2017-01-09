package io.qala.javatraining.dao;

import io.qala.javatraining.domain.Dog;
import io.qala.javatraining.domain.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.Collection;

public class HibernateDogDao implements DogDao {
    public HibernateDogDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override public Collection<Dog> getAllDogs() {
        Query query = session().createNamedQuery("getAllDogs");
        //noinspection unchecked
        return query.list();
    }

    @Override public Dog getDog(String id) {
        Dog dog = session().get(Dog.class, id);
        if(dog == null) throw new ObjectNotFoundException(Dog.class, id);
        return dog;
    }

    @Override public Dog createDog(Dog dog) {
        session().save(dog);
        return dog;
    }

    @Override public boolean deleteDog(String id) {
        try {
            session().delete(session().load(Dog.class, id));
        } catch (javax.persistence.EntityNotFoundException e) {//todo: this is a temp (and a very bad) fix
            return false;
        }
        return true;
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
    }
    void flushAndClear() {
        session().flush();
        session().clear();
    }

    private final SessionFactory sessionFactory;
}
