Dog App
-------

Create an app that has CRUD (create/read/update/delete) operations implemented for object called Dog via REST. 

Properties of a dog: 

* name - 1-100 symbols 
* date of birth - must be before NOW, can be null
* height, weight - must be greater than 0

Protocol:

* `POST /dog` - create a new dog. 
* `GET /dog/{id}` - shows the dog’s props in JSON format
* `PUT /dog/{id}` - updates the dog. Whole dog JSON is sent in request body - it overrides all the fields in DB. 
* `DELETE /dog/{id}` - removes the dog from DB

The app needs to be tested on all levels:

* Unit tests to test validation
* DAO tests to test how the entities are saved into DB (H2 can be instantiated during test start)
* Component REST tests - use MockMVC
* System REST tests - run against fully deployed app, send actual HTTP requests