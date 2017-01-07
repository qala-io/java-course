package training.java.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import training.java.domain.Dog;
import training.java.domain.ObjectNotFoundException;

import javax.validation.Valid;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@SuppressWarnings(/*Most of the methods are used only by Spring MVC*/"unused")
@RestController
public class DogEndpoint {
    private static ConcurrentMap<String, Dog> ALL_DOGS = new ConcurrentHashMap<>();
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @GetMapping(value = "/dog")
    Collection<Dog> getAllDogs() {
        return ALL_DOGS.values();
    }

    @GetMapping(value = "/dog/{id}")
    ResponseEntity getDog(@PathVariable String id) {
        Dog found = ALL_DOGS.get(id);
        if (found != null) return ResponseEntity.ok(found);
        return ResponseEntity.notFound().build();
    }

    @PostMapping(value = "/dog")
    Dog createDog(@RequestBody @Valid Dog dog) {
        logger.info("Creating a dog: [{}]", dog);
        ALL_DOGS.put(dog.getId(), dog);
        return dog;
    }
    @DeleteMapping(value = "/dog/{id}")
    Dog deleteDog(@PathVariable String id) {
        logger.info("Deleting a dog: [{}]", id);
        Dog removed = ALL_DOGS.remove(id);
        if(removed == null) throw new ObjectNotFoundException(Dog.class, id);
        return removed;
    }

    //todo: migrate to a separate class
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity processValidationError(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(ValidationRestError.fromSpringErrors(e.getBindingResult().getAllErrors()), BAD_REQUEST);
    }
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity processValidationError(ObjectNotFoundException e) {
        return new ResponseEntity<>("{}", NOT_FOUND);
    }
}
