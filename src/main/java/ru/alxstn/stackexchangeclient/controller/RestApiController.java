package ru.alxstn.stackexchangeclient.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.alxstn.stackexchangeclient.exception.DataProviderException;
import ru.alxstn.stackexchangeclient.service.DataProviderService;
import ru.alxstn.stackexchangeclient.model.taret.TargetDataWrapper;

@RestController
@RequestMapping(path = "/api/questions", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestApiController {
    private final DataProviderService service;

    public RestApiController(DataProviderService service) {
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<?> search(@RequestParam(required = false) String title,
                                 @RequestParam(required = false) Integer page) {
        try {
            var result = service.searchByTitle(title, java.util.Optional.ofNullable(page));
            return result.map(responseWrapper ->
                    ResponseEntity.ok(new TargetDataWrapper(responseWrapper))).orElseGet(() ->
                    ResponseEntity.noContent().build());

        } catch (DataProviderException e) {
            e.getResponseHeaders();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
