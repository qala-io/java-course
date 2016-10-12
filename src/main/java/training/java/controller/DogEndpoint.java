package training.java.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import training.java.domain.Dog;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
public class DogEndpoint {
    private static ConcurrentMap<String, Dog> ALL_DOGS = new ConcurrentHashMap<>();

    @RequestMapping(value = "/dog", method = GET)
    Collection<Dog> getAllDogs() {
        return ALL_DOGS.values();
    }

    @RequestMapping(value = "/dog/{id}", method = GET, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity getDog(@PathVariable String id) {
        Dog found = ALL_DOGS.get(id);
        if (found != null) return ResponseEntity.ok(found);
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/dog", method = POST)
    Dog createDog(@RequestBody Dog dog) {
        ALL_DOGS.put(dog.getId(), dog);
        return dog;
    }
}
