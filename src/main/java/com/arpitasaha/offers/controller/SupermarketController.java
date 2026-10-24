package com.arpitasaha.offers.controller;

import com.arpitasaha.offers.model.Supermarket;
import com.arpitasaha.offers.repository.SupermarketRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/supermarkets")
public class SupermarketController {
    private final SupermarketRepository repository;
    public SupermarketController(SupermarketRepository repository) { this.repository = repository; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Supermarket register(@Valid @RequestBody Supermarket market) {
        if (repository.existsByContactEmailIgnoreCase(market.getContactEmail()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A supermarket with this email already exists");
        return repository.save(market);
    }
    @GetMapping
    public List<Supermarket> list() { return repository.findAll(); }
    @GetMapping("/{id}")
    public Supermarket get(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Supermarket not found"));
    }
}
