package training.java.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import training.java.domain.Dog;

import javax.validation.Valid;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
public class DogEndpoint {
    private static ConcurrentMap<String, Dog> ALL_DOGS = new ConcurrentHashMap<>();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    //todo: make it work when the collection is empty
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
    //todo: migrate to a separate class
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity processValidationError(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(ValidationRestError.fromSpringErrors(e.getBindingResult().getAllErrors()), HttpStatus.BAD_REQUEST);
    }
}
