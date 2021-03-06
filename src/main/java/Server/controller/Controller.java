package Server.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import Server.model.Event;
import Server.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    EventRepository repository;

    @GetMapping("/events")
    public ResponseEntity<List<Event>> getAllEvents(@RequestParam(required = false) String title) {
        try {
            List<Event> events = new ArrayList<Event>();

            if (title == null)
                repository.findAll().forEach(events::add);
            else
                repository.findByTitleContaining(title).forEach(events::add);

            if (events.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(events, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable("id") long id) {
        Optional<Event> eventData = repository.findById(id);

        if (eventData.isPresent()) {
            return new ResponseEntity<>(eventData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/eventsPost")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        try {
            Event _event = repository
                    .save(new Event(event.getId(), event.getTitle(), event.getDescription()));
            return new ResponseEntity<>(_event, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/eventsPut/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable("id") long id, @RequestBody Event event) {
        Optional<Event> eventData = repository.findById(id);

        if (eventData.isPresent()) {
            Event _event = eventData.get();
            _event.setTitle(event.getTitle());
            _event.setDescription(event.getDescription());
            return new ResponseEntity<>(repository.save(_event), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/events/{id}")
    public ResponseEntity<HttpStatus> deleteEvent(@PathVariable("id") long id) {
        try {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/eventsDeleteAll")
    public ResponseEntity<HttpStatus> deleteAllEvents() {
        try {
            repository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/events/title")
    public ResponseEntity<List<Event>> findByTitle(String title) {
        try {
            List<Event> events = repository.findByTitle(title);

            if (events.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(events, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
