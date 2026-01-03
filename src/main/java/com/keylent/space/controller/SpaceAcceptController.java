package com.keylent.space.controller;

import com.keylent.space.service.IngestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/space")
public class SpaceAcceptController {

    private final IngestionService ingestionService;

    public SpaceAcceptController(IngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @GetMapping("/accept")
    public ResponseEntity<String> accept(
            @RequestParam String id,
            @RequestParam(required = false) String endpoint) {

        try {
            ingestionService.process(id, endpoint);
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("failed");
        }
    }
}
