package io.qala.javatraining.controller;

import io.qala.javatraining.domain.Dog;
import io.qala.javatraining.domain.ObjectNotFoundException;
import io.qala.javatraining.service.DogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@SuppressWarnings(/*Most of the methods are used only by Spring MVC*/"unused")
@RestController
public class DogEndpoint {
    private static ConcurrentMap<String, Dog> ALL_DOGS = new ConcurrentHashMap<>();
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final DogService dogService;

    public DogEndpoint(DogService dogService) {
        this.dogService = dogService;
    }

    @GetMapping(value = "/dog")
    Collection<Dog> getAllDogs() {
        return dogService.getAllDogs();
    }
    @GetMapping(value = "/dog/{id}")
    ResponseEntity<Dog> getDog(@PathVariable String id) {
        return ResponseEntity.ok(dogService.getDog(id));
    }

    @PostMapping(value = "/dog")
    Dog createDog(@RequestBody @Valid Dog dog) {
        logger.info("Creating a dog: [{}]", dog);
        return dogService.createDog(dog);
    }
    @DeleteMapping(value = "/dog/{id}")
    ResponseEntity<Dog> deleteDog(@PathVariable String id) {
        logger.info("Deleting a dog: [{}]", id);
        dogService.deleteDog(id);
        return ResponseEntity.noContent().build();
    }

    //todo: migrate to a separate class
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationRestError>> processValidationError(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(ValidationRestError.fromSpringErrors(e.getBindingResult().getAllErrors()), BAD_REQUEST);
    }
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<String> processValidationError(ObjectNotFoundException e) {
        return new ResponseEntity<>("{}", NOT_FOUND);
    }
}
